#include <stdio.h>
#include <stdlib.h>

unsigned long tail_r_fibonacci(int n, unsigned long a, unsigned long b);

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

    printf("%lu\n", tail_r_fibonacci(n, 0, 1));
    return 0;
}
