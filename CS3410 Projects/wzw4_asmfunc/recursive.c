#include <stdio.h>
#include <stdlib.h>

unsigned long r_fibonacci(int n);

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

    printf("%lu\n", r_fibonacci(n));
    return 0;
}
