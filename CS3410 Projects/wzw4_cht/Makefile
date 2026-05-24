CC = gcc
CFLAGS = -Wall -Wextra -Wpedantic -Wshadow -Wformat=2 -std=c17 -pthread -fsanitize=address,undefined

EXECUTABLES = test_barrier test_hash_table test_readers-writer test_spinlock test_wait-broadcast

all: $(EXECUTABLES)

barrier: test_barrier

hash_table: test_hash_table

readers-writer: test_readers-writer

spinlock: test_spinlock

wait-broadcast: test_wait-broadcast

test_barrier: test_barrier.o barrier.o spinlock.o wait-broadcast.o
	$(CC) $(CFLAGS) -o $@ $^

test_hash_table: test_hash_table.o hash_table.o readers-writer.o barrier.o spinlock.o wait-broadcast.o
	$(CC) $(CFLAGS) -o $@ $^

test_readers-writer: test_readers-writer.o readers-writer.o spinlock.o wait-broadcast.o
	$(CC) $(CFLAGS) -o $@ $^

test_spinlock: test_spinlock.o spinlock.o
	$(CC) $(CFLAGS) -o $@ $^

test_wait-broadcast: test_wait-broadcast.o wait-broadcast.o spinlock.o
	$(CC) $(CFLAGS) -o $@ $^

%.o: %.c
	$(CC) $(CFLAGS) -c -o $@ $<

clean:
	rm -f $(EXECUTABLES) *.o

.PHONY: all barrier hash_table readers-writer spinlock wait-broadcast clean
