# Compiler and flags
CC = gcc
CFLAGS = -Wall -Wextra -fsanitize=address,undefined -g -std=c23

# Source files
SRC_FILES = huffman.c priority_queue.c bit_tools.c utils.c memcheck.c
OBJ_FILES = $(SRC_FILES:.c=.o)

# Executables and source files
COMPRESS_SRC_FILE = compress.c
COMPRESS_EXECUTABLE = compress

DECOMPRESS_SRC_FILE = decompress.c
DECOMPRESS_EXECUTABLE = decompress

# Default target
all: $(COMPRESS_EXECUTABLE) $(DECOMPRESS_EXECUTABLE)

# Build the compress executable
$(COMPRESS_EXECUTABLE): $(OBJ_FILES) $(COMPRESS_SRC_FILE:.c=.o)
	$(CC) $(CFLAGS) $(OBJ_FILES) $(COMPRESS_SRC_FILE:.c=.o) -o $(COMPRESS_EXECUTABLE)

# Build the decompress executable
$(DECOMPRESS_EXECUTABLE): $(OBJ_FILES) $(DECOMPRESS_SRC_FILE:.c=.o)
	$(CC) $(CFLAGS) $(OBJ_FILES) $(DECOMPRESS_SRC_FILE:.c=.o) -o $(DECOMPRESS_EXECUTABLE)

# Test for priority queue 
pqtest: priority_queue.c test_priority_queue.c utils.c memcheck.c
	$(CC) $(CFLAGS) priority_queue.c test_priority_queue.c utils.c memcheck.c -o test_priority_queue
	
hufftest: huffman.c priority_queue.c bit_tools.c utils.c memcheck.c test_huffman.c
	$(CC) $(CFLAGS) huffman.c priority_queue.c bit_tools.c utils.c memcheck.c test_huffman.c -o test_huffman

# Compile source files into object files
%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

# Clean up the build
clean:
	rm -f $(OBJ_FILES) $(COMPRESS_EXECUTABLE) $(DECOMPRESS_EXECUTABLE) $(COMPRESS_SRC_FILE:.c=.o) $(DECOMPRESS_SRC_FILE:.c=.o) && \
	rm -f *.bits && \
	rm -f test_priority_queue test_huffman && \
	rm uncompressed.txt

# Phony targets
.PHONY: all clean compress decompress
