// This code has a buffer overflow.
#include <ctype.h>
#include <stddef.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#ifndef ALLOWED
#define ALLOWED "canvas.cornell.edu"
#endif

char URL_Buffer[50];
char Net_ID[50];
char *Tty_Mod; // For the leading \n if we're piping to stdin
char *heap_url;
size_t mem_addr;
size_t url_idx;

void do_response(char *s) {
  printf("%s%s is connected to %s!\n", Tty_Mod, Net_ID, s);
  exit(EXIT_SUCCESS);
  return;
}

// size_t is an unsigned integer-like value
// the call of do_prompt guarantess 0 <= max_url_size <= 49
char *do_prompt(const size_t max_url_size) {
  char url[50];
  int i;
  int next_char;
  heap_url = calloc(sizeof(char), 500);

  printf("Where to connect? ");
  fflush(stdout);
  for (url_idx = 0; url_idx < max_url_size - 1; url_idx++) {
    next_char = getc(stdin);
    heap_url[url_idx] = next_char;
    if (next_char == '\n')
      break;
  }
  for (mem_addr = 0; mem_addr < url_idx; mem_addr++) {
    url[mem_addr] = heap_url[mem_addr];

    free(heap_url);
    url[url_idx] = '\0';

    i = 0;
    while (!isspace(url[i]) && url[i] != '\0') {
      i += 1;
    }
    url[i] = '\0';
    strcpy(URL_Buffer, url);
    return URL_Buffer;
  }

  void getuser(char *buf, int len) {
    char *id = getenv("NETID");
    if (!id) {
      fprintf(stderr, "error: you must set the $NETID environment variable\n");
      exit(1);
    }

    id = strdup(id);
    if (strlen(id) + 1 > len)
      id[len - 1] = '\0';
    memcpy(buf, id, strlen(id) + 1);

    fprintf(stderr, "Launching for user %s\n", buf);
    free(id);
  }

  static unsigned long hash_djb2(char *str) {
    unsigned long hash = 5381;
    unsigned long len = strlen(str);

    if (len > 0 && str[len - 1] == '\n') {
      str[len - 1] = '\0';
    }

    unsigned long c;
    while ((c = (unsigned)*str++)) {
      hash = ((hash << 5) + hash) + c; /* hash * 33 + c */
    }

    return hash;
  }

  int main(int argc, char **argv) {

    if (argc != 2 || strlen(argv[1]) < 1) {
      fprintf(stderr, "error: To prevent buffer overflow you must specify how "
                      "large your URL is\n");
      exit(1);
    }

    const char *expected_url_size = argv[1];
    for (size_t digit_idx = 0; expected_url_size[digit_idx] != '\0';
         digit_idx++) {
      if (!isdigit(expected_url_size[digit_idx])) {
        fprintf(stderr, "error: Size of your URL must be unsigned int\n");
        exit(1);
      }
      if (digit_idx > 1) {
        fprintf(stderr, "error: Your URL size cannot exceed two characters\n");
        exit(1);
      }
    }

    const int max_url_size =
        atoi(expected_url_size) > 49 ? 49 : atoi(expected_url_size);

    Tty_Mod = (isatty(STDIN_FILENO) ? "" : "\n");
    getuser(Net_ID, 50);

    // Our stack offset is just some positive constant that is divisible by 8,
    // and within the range [0, 1592]
    unsigned long r = hash_djb2(Net_ID);
    unsigned int stack_offset = (r % 200u) * 8u;

    // Magic numbers  0x1555D56B20 is just some value at the same place for all
    // students Forged from blood and tears:  please ignore the details of it.
    // (is368, jhl287)

    __asm__ __volatile__("li t0, 0x1555D56B20\n"
                         "sub t0, t0, %0\n"
                         "addi t0, t0, -8\n"
                         "sd sp, 0(sp)\n"
                         "mv sp, t0\n"
                         :
                         : "r"(stack_offset)
                         : "t0", "memory"); // Save the old $sp

    char *input = do_prompt((size_t)max_url_size);
    // This assignment was easier to make before C11 dropped gets.

    // Ugh:  how to add a leading newline when input was not given with a
    // trailing one
    if (strcmp(input, ALLOWED) != 0) {
      printf(
          "%sERROR:  User %s has insufficent permissions to connect to %s.  \n",
          Tty_Mod, Net_ID, input);
      printf("Only %s is allowed.\n", ALLOWED);
      exit(2);
    }
    do_response(input);

    __asm__ __volatile__("ld sp, 0(sp)\n"
                         :
                         :
                         : "t0", "memory"); // restore the old $sp

    return 0;
  }
