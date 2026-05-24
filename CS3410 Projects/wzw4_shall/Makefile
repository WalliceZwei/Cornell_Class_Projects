# Compiler and flags
CC = gcc
CFLAGS = -Wall -Wpedantic -fsanitize=address,undefined -Wshadow -Wformat=2 -Wconversion -std=c23 -g -D_GNU_SOURCE
EXECS = shall

all: $(EXECS)

shall: shall.o parser.o reader.o token.o exec.o
	$(CC) $(CFLAGS) -o $@ $^

%.o: %.c
	$(CC) $(CFLAGS) -c -o $@ $<

clean:
	rm -f $(EXECS) *.o

.PHONY: all shall clean
