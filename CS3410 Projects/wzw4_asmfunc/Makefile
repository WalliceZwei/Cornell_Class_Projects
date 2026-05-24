VERSIONS := recursive tail_recursive memoization opt_tail

CC = /opt/riscv/bin/riscv64-unknown-linux-gnu-gcc
CFLAGS = -Wall -Wextra -std=c23
MAKEFLAGS += -r  # Disable built-in rules.

all: $(VERSIONS)

%: %.o %.s
	$(CC) $(CFLAGS) $^ -o $@

compare: compare.o recursive.s memoization.s tail_recursive.s opt_tail.s
	$(CC) $(CFLAGS) $^ -o $@

%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

clean:
	rm -f *.o $(VERSIONS) compare
