/* Author: Robbert van Renesse 2015
 */

typedef struct token *token_t;
typedef struct tokenizer *tokenizer_t;
typedef struct element *element_t;
typedef struct parser *parser_t;
typedef struct reader *reader_t;
typedef struct command *command_t;

enum token_type {
	TOKEN_EOF,					// EOF has been reached
	TOKEN_SEMI,					// ;
	TOKEN_EOLN,					// \n
	TOKEN_STRING,				// a sequence of non-special chars
	TOKEN_AMPERSAND,			// &
	TOKEN_GT,					// >
	TOKEN_LT,					// <
	TOKEN_CB_OPEN,				// {
	TOKEN_CB_CLOSE				// }
};
/* Tokens produced by the tokenizer.
 */
struct token {
	enum token_type type;
	char *string;
};

enum element_type {
	ELEMENT_ARG,						// arg
	ELEMENT_REDIR_FILE_IN,				// < file
	ELEMENT_REDIR_FILE_OUT,				// > file
	ELEMENT_REDIR_FILE_APPEND,			// >> file
	ELEMENT_REDIR_FD_IN,				// < { fd }
	ELEMENT_REDIR_FD_OUT,				// > { fd }
	ELEMENT_SEMI,						// ;
	ELEMENT_BACKGROUND,					// &
	ELEMENT_EOLN,						// newline
	ELEMENT_ERROR,
	ELEMENT_EOF
};

/* A command is a list of elements.
 */
struct element {
	enum element_type type;
	union {
		struct {
			char *string;
		} arg;
		struct {
			int fd;
			char *name;
		} redir_file;
		struct {
			int fd1, fd2;
		} redir_fd;
	} u;
};

/* Contains the specifics of a command.  Normal arguments and redirection
 * elements have been split into separate lists.
 */
struct command {
	/* Arguments are collected here.
	 */
	char **argv;
	int argc;		// warning: includes the 0 pointer at the end of argv

	/* Redirections are collected here.
	 */
	element_t *redirs;
	int nredirs;
};

/*  Defined in token.c */
tokenizer_t tokenizer_create(reader_t reader);
token_t tokenizer_next(tokenizer_t);
void token_free(token_t);
void tokenizer_free(tokenizer_t tokenizer);
/********************************/

/* Defined in parser.c */
parser_t parser_create(tokenizer_t tokenizer);
element_t parser_next(parser_t parser);
void element_free(element_t elt);
void parser_free(parser_t parser);
/********************************/

/* Defined in reader.c */
reader_t reader_create(int fd);
int reader_next(reader_t reader);
void reader_free(reader_t reader);
/********************************/

/* Defined in exec.c */
void interrupts_disable();                       // (your job)
void interrupts_enable();                        // (your job)
void interrupts_catch();                         // (your job)
void perform(command_t command, int background); // (completed)
/********************************/

/* Defined in shall.c (along with `main`)*/
void interpret(reader_t reader, int interactive);  //
void free_command(command_t command);
