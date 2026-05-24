#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <stdbool.h>

unsigned long r_fibonacci(int n);
unsigned long tail_r_fibonacci(int n, unsigned long a, unsigned long b);
unsigned long m_fibonacci(int n, unsigned long log_array[], int size);
unsigned long i_fibonacci(int n);
unsigned long opt_tail_fibonacci(int n, unsigned long a, unsigned long b);

int main(int argc, char *argv[]) {
    if (argc != 3) {
        fprintf(stderr, "Usage: %s <method> <n> \n", argv[0]);
        fprintf(stderr, "Methods: all, recursive, tail_recursive, memoization, opt_tail\n");
        return -1;
    }

    // Choose which implementations to compare.
    bool use_recursive = false;
    bool use_tail_recursive = false;
    bool use_memoization = false;
    bool use_opt_tail = false;
    if (strcmp(argv[1], "all") == 0) {
        use_recursive = use_tail_recursive = use_memoization =
             use_opt_tail = true;
    } else if (strcmp(argv[1], "recursive") == 0) {
        use_recursive = true;
    } else if (strcmp(argv[1], "tail_recursive") == 0) {
        use_tail_recursive = true;
    } else if (strcmp(argv[1], "memoization") == 0) {
        use_memoization = true;
    } else if (strcmp(argv[1], "opt_tail") == 0) {
        use_opt_tail = true;
    } else {
        fprintf(stderr, "Unknown method: %s\n", argv[1]);
        fprintf(stderr, "Methods: all, recursive, tail_recursive, memoization, opt_tail\n");
        return -1;
    }

    int n = atoi(argv[2]);
    if (n < 0) {
        fprintf(stderr, "Error: n must be a non-negative integer\n");
        return -1;
    }
    int size = n + 1;
    unsigned long *log_array = (unsigned long *)calloc(size, sizeof(unsigned long));
    clock_t start, end;
    double time_taken;
    unsigned long result;
    int iterations = 10000;

    if (use_recursive) {
        start = clock();
        for (int i = 0; i < iterations; i++) {
            result = r_fibonacci(n);
        }
        end = clock();
        time_taken = ((double)(end - start)) / CLOCKS_PER_SEC;
        printf("Recursive Fibonacci %d is %lu, time taken: %f seconds\n", n, result, time_taken);
    }
    if (use_tail_recursive) {
        start = clock();
        for (int i = 0; i < iterations; i++) {
            result = tail_r_fibonacci(n, 0, 1);
        }
        end = clock();
        time_taken = ((double)(end - start)) / CLOCKS_PER_SEC;
        printf("Tail Recursive Fibonacci %d is %lu, time taken: %f seconds\n", n, result, time_taken);
    }
    if (use_memoization) {
        start = clock();
        for (int i = 0; i < iterations; i++) {
            result = m_fibonacci(n, log_array, size);
            memset(log_array, 0, size * sizeof(unsigned long));
        }
        end = clock();
        time_taken = ((double)(end - start)) / CLOCKS_PER_SEC;
        printf("Memoized Fibonacci %d is %lu, time taken: %f seconds\n", n, result, time_taken);
    }
    if (use_opt_tail) {
        start = clock();
        for (int i = 0; i < iterations; i++) {
            result = opt_tail_fibonacci(n, 0, 1);
        }
        end = clock();
        time_taken = ((double)(end - start)) / CLOCKS_PER_SEC;
        printf("Optimized Tail Recursive Fibonacci %d is %lu, time taken: %f seconds\n", n, result, time_taken);
    }

    free(log_array);
    return 0;
}
