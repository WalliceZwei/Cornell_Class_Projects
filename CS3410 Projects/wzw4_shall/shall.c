/* Author: Robbert van Renesse 2015
 *
 * Main code.
 */

#include "shall.h"
#include <assert.h>
#include <ctype.h>
#include <fcntl.h>
#include <regex.h>
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>

static void arg_append(command_t command, char *arg) {
    command->argv = realloc(command->argv, ((size_t)command->argc + 1) *
                                               sizeof(*command->argv));
    command->argv[command->argc++] = arg;
}

static void redir_append(command_t command, element_t elt) {
    command->redirs = realloc(command->redirs, ((size_t)command->nredirs + 1) *
                                                   sizeof(command->redirs));
    command->redirs[command->nredirs++] = elt;
}

/* Display the next prompt.
 */
static void display_prompt() { fprintf(stderr, "-> "); }

/* Expand environment variables in a string. Supports $VAR and ${VAR} syntax. */
static char *expand_env_vars(const char *str) {
    if (!str) {
        return nullptr;
    }

    const char *cursor = str;
    const char *pattern = "\\$\\{?([A-Za-z_][A-Za-z0-9_]*)\\}?";

    regex_t regex;
    regmatch_t pmatch[2]; // pmatch[0]="${XYZ}" and pmatch[1]="XYZ"

    if (regcomp(&regex, pattern, REG_EXTENDED)) {
        fprintf(stderr, "Could not compile regex\n");
        abort();
    }

    char *return_string = calloc(1, sizeof(char));

    while (regexec(&regex, cursor, 2, pmatch, 0) == 0) {
        const size_t start = (size_t)pmatch[0].rm_so;
        const size_t end = (size_t)pmatch[0].rm_eo;

        const size_t name_start = (size_t)pmatch[1].rm_so;
        const size_t name_end = (size_t)pmatch[1].rm_eo;
        const size_t name_length = (size_t)name_end - name_start;

        char *buffer = calloc(name_length + 1, sizeof(char));
        memcpy(buffer, cursor + name_start, name_length);

        const char *env_var_value = getenv(buffer);

        // Handle the case where the environment variable is not set
        if (!env_var_value) {
            env_var_value = "";
        }

        // return_string += cursor[0:start] + env_var_value
        size_t new_length =
            strlen(return_string) + start + strlen(env_var_value) + 1;
        char *new_return_string = calloc(new_length, sizeof(char));

        strncpy(new_return_string, return_string, strlen(return_string));
        strncat(new_return_string, cursor, start);
        strcat(new_return_string, env_var_value);

        free(return_string);
        free(buffer);

        // move cursor to next match
        cursor += end;
        return_string = new_return_string;
    }

    // Append the remaining part of the string
    size_t final_length = strlen(return_string) + strlen(cursor) + 1;
    char *final_return_string = calloc(final_length, sizeof(char));
    strcpy(final_return_string, return_string);
    strcat(final_return_string, cursor);
    free(return_string);
    regfree(&regex);
    return final_return_string;
}

void free_command(command_t command) {
    for (int i = 0; i < command->argc; i++) {
        free(command->argv[i]);
    }
    command->argc = 0;
    for (int i = 0; i < command->nredirs; i++) {
        element_free(command->redirs[i]);
    }
    command->nredirs = 0;
}

/* A complete command (and all of its arguments) has been parsed:
 * perform the corresponding execution
 */
static void gotline(command_t command, int background) {
    if (command->argc > 0) {
        arg_append(command, 0);
        perform(command, background);
    }
    free_command(command);
}



void interpret(reader_t reader, int interactive) {
    struct command command;

    memset(&command, 0, sizeof(command));

    tokenizer_t tokenizer = tokenizer_create(reader);
    parser_t parser = parser_create(tokenizer);

    if (interactive) {
        display_prompt();
    }

    int more = 1;
    while (more) {
        element_t elt = parser_next(parser);
        switch (elt->type) {
        case ELEMENT_ARG: {
            char *expanded = expand_env_vars(elt->u.arg.string);
            free(elt->u.arg.string);
            arg_append(&command, expanded);
            elt->u.arg.string = nullptr;
            element_free(elt);
            break;
        }
        case ELEMENT_REDIR_FILE_IN:
        case ELEMENT_REDIR_FILE_OUT:
        case ELEMENT_REDIR_FILE_APPEND: {
            if (elt->u.redir_file.name) {
                char *expanded_name = expand_env_vars(elt->u.redir_file.name);
                free(elt->u.redir_file.name);
                elt->u.redir_file.name = expanded_name;
            }
            redir_append(&command, elt);
            break;
        }
        case ELEMENT_REDIR_FD_IN:
        case ELEMENT_REDIR_FD_OUT:
            redir_append(&command, elt);
            break;
        case ELEMENT_EOLN:
            element_free(elt);
            gotline(&command, 0);
            if (interactive) {
                display_prompt();
            }
            break;
        case ELEMENT_SEMI:
            element_free(elt);
            gotline(&command, 0);
            break;
        case ELEMENT_BACKGROUND:
            element_free(elt);
            gotline(&command, 1);
            break;
        case ELEMENT_ERROR:
            element_free(elt);
            if (interactive) {
                display_prompt();
            }
            break;
        case ELEMENT_EOF:
            element_free(elt);
            if (interactive) {
                fprintf(stderr, "EOF\n");
            }
            gotline(&command, 0);
            more = 0;
            break;
        default:
            assert(0);
        }
    }

    parser_free(parser);
    tokenizer_free(tokenizer);
    free(command.argv);
    free(command.redirs);
}

/* Main code.  If interactive, print prompts.  Read pipelines from input
 * and execute them.
 */
int main() {
    interrupts_catch();
    reader_t reader = reader_create(0);
    interpret(reader, isatty(0));
    reader_free(reader);
    return 0;
}
