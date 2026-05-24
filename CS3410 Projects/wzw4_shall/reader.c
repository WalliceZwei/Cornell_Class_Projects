/* Author: Robbert van Renesse 2018
 *
 * The interface is as follows:
 *	reader_t reader_create(int fd);
 *		Create a reader that reads characters from the given file
 * descriptor.
 *
 *	int reader_next(reader_t reader):
 *		Return the next character or -1 upon EOF (or error...)
 *
 *	void reader_free(reader_t reader):
 *		Release any memory allocated.
 */

#include "shall.h"
#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

struct reader {
  int fd;
  int index;
  char buffer[512];
  int buffer_size;
};

reader_t reader_create(int fd) {
  reader_t reader = calloc(1, sizeof(*reader));
  reader->fd = fd;
  reader->index = 0;
  reader->buffer_size = 0;
  return reader;
}

int reader_next(reader_t reader) {
  if (reader->index >= reader->buffer_size) {
    int n = (int)read(reader->fd, reader->buffer, 512);
    if (n < 1) {
      return EOF;
    }
    reader->index = 0;
    reader->buffer_size = n;
  }
  return (unsigned char)(reader->buffer[reader->index++]);
}

void reader_free(reader_t reader) { free(reader); }