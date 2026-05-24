#include "memcheck.h"

static FILE* get_log_file_ptr() {
    FILE *log_file = fopen("memcheck_log.txt", "a");
    if (log_file == NULL) {
        fprintf(stderr, "Error: Could not open memcheck_logs.txt for writing.\n");
        return stderr;
    }
    return log_file;
}

// The wrapper for malloc with logging and error handling
void *my_malloc_impl(size_t size, const char *file, int line)
{
  FILE *log_file = get_log_file_ptr();

  void *ptr = malloc(size);
  if (ptr == NULL)
  {
    fprintf(log_file, "Memory allocation failed at %s line %d\n", file, line);
    if (log_file != stderr) {
        fclose(log_file);
    }
    exit(EXIT_FAILURE);
  }
  
  fprintf(log_file, "malloc(%zu) = %p at %s line %d\n", size, ptr, file, line);

  if (log_file != stderr) {
      fclose(log_file);
  }
  
  return ptr;
}

// The wrapper for free with logging
void my_free_impl(void *ptr, const char *file, int line)
{
  FILE *log_file = get_log_file_ptr();

  if (ptr == NULL) {
    fprintf(log_file, "free(NULL) at %s line %d\n", file, line);
    if (log_file != stderr) {
        fclose(log_file);
    }
    return;
  }
  
  fprintf(log_file, "free(%p) at %s line %d\n", ptr, file, line);
  free(ptr);

  if (log_file != stderr) {
      fclose(log_file);
  }
}