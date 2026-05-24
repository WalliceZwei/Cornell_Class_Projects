################################################################################
#              Makefile for the CS 3410 Cache Simulation Project
################################################################################

# Additional flags for the compiler
# always enable debugging because its more convenient
CFLAGS := -Wall -Wextra -Wpedantic -Wshadow -Wformat=2 -std=c23 -fsanitize=address,undefined -g3
LFLAGS := -lm

.PHONY: all clean run

all: clean cachesim

cachesim: cache.o cache_stats.o simulator.o print_helpers.o
	gcc $(CFLAGS) -o $@ $@.c $^ $(LFLAGS)

# Wildcard rule that allows for the compilation of a *.c file to a *.o file
%.o : %.c
	gcc -c $(CFLAGS) $< -o $@

# Removes any executables and compiled object files
clean:
	rm -f cachesim *.o
