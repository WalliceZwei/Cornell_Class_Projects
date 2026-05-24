#include <stdio.h>
#include <stdlib.h> 

void *my_malloc_impl(size_t size, const char *file, int line);
void my_free_impl(void *ptr, const char *file, int line);

// Macros that automatically add file and line information
#define my_malloc(size) my_malloc_impl(size, __FILE__, __LINE__)
#define my_free(ptr) my_free_impl(ptr, __FILE__, __LINE__)