#include <stdio.h>
#include <stdlib.h>

unsigned long m_fibonacci(int n, unsigned long* memo_table, int size);

int main(int argc, char *argv[]) {
    if (argc != 2) {
        fprintf(stderr, "Usage: %s <n>\n", argv[0]);
        return -1;
    }

    int n = atoi(argv[1]);
    if (n < 0) {
        fprintf(stderr, "Error: n must be a non-negative integer\n");
        return -1;
    }

    int size = n + 1;
    unsigned long *memo_table = (unsigned long *)calloc(size, sizeof(unsigned long));

    printf("%lu\n", m_fibonacci(n, memo_table, size));

    free(memo_table);
    return 0;
}
