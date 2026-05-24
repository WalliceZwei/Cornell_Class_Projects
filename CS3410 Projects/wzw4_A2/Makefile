CFLAGS=-Wall -Wpedantic -fsanitize=address,undefined -Werror -Wshadow -Wformat=2 -Wconversion -std=c23
CC = gcc

all: part1 part2 smoke_test

part1: minifloat.c minifloat_test_part1.c minifloat_test_part1.expected
	$(CC) $(CFLAGS) minifloat.c minifloat_test_part1.c -o minifloat_test_part1.out

part2: minifloat.c minifloat_test_part2.c
	$(CC) $(CFLAGS) minifloat.c minifloat_test_part2.c -o minifloat_test_part2.out

smoke_test: minifloat.c smoke_test.c minifloat_smoke_test.expected
	$(CC) $(CFLAGS) minifloat.c smoke_test.c -o minifloat_smoke_test.out

clean:
	rm -f *.out.stackdump
	rm -f *.out
	rm -f *.txt

.PHONY: all clean
