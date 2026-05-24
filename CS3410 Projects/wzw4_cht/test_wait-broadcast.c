#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <pthread.h>
#include "wait-broadcast.h"

typedef struct thread_arg {
    volatile int* lock;
    int* condition;
    int* shared;  // Example var
} thread_arg;

void* thread_function(void* arg) {
    // TODO
    return NULL;
}

int main(int argc, char* argv[]) {
    assert(argc == 2);
    int NUM_THREADS = atoi(argv[1]);
    pthread_t threads[NUM_THREADS];
    volatile int lock = 0;
    int condition = 0;
    int shared = 0;
    thread_arg* args[NUM_THREADS];

    for (int i = 0; i < NUM_THREADS; ++i) {
        args[i] = malloc(sizeof(thread_arg));
        args[i]->lock = &lock;
        args[i]->shared = &shared;
        args[i]->condition = &condition;
    }
    for (int i = 0; i < NUM_THREADS; ++i) {
        pthread_create(&threads[i], NULL, thread_function, (void*)args[i]);
    }
    for (int i = 0; i < NUM_THREADS; ++i) {
        pthread_join(threads[i], NULL);
    }
    return 0;
}
