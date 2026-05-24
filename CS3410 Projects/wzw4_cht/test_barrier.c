#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <pthread.h>
#include "barrier.h"

typedef struct thread_arg {
    barrier* bar;
    int id;  // Example variable
} thread_arg;

void* thread_function(void* arg) {
    // TODO
    return NULL;
}

int main(int argc, char* argv[]) {
    assert(argc == 2);
    int NUM_THREADS = atoi(argv[1]);
    pthread_t threads[NUM_THREADS];
    thread_arg* args[NUM_THREADS];
    barrier* bar = init_barrier(NUM_THREADS);

    for (int i = 0; i < NUM_THREADS; ++i) {
        args[i] = malloc(sizeof(thread_arg));
        args[i]->bar = bar;
        args[i]->id = i;
    }
    for (int i = 0; i < NUM_THREADS; ++i) {
        pthread_create(&threads[i], NULL, thread_function, (void*)args[i]);
    }
    for (int i = 0; i < NUM_THREADS; ++i) {
        pthread_join(threads[i], NULL);
    }
    return 0;
}
