#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <pthread.h>
#include <string.h>
#include "hash_table.h"

size_t modulo_hash(const int key, const size_t num_buckets) {
    return key % num_buckets;
}

/* This function is called once for each worker thread. Given a thread ID <n>, the
 * file t<n>.txt is opened. Each line of the form "insert <int> <int>", "get <int>"
 * or "delete <int>" is parsed, and the respective operation is added to the end
 * of the thread's cht_request_queue.
 */
void load_requests_from_file(const int thread_id, cht_request_queue* requests) {
    char filename[20];
    snprintf(filename, sizeof(filename), "t%d.txt", thread_id);

    FILE* request_file = fopen(filename, "r");

    char op[10];
    int key, value;
    while (fscanf(request_file, "%s %d %d", op, &key, &value) != EOF) {
        cht_request request;
        if (strcmp(op, "insert") == 0) {
            request.op = CHT_INSERT;
            request.key = key;
            request.value = value;
        } else if (strcmp(op, "get") == 0) {
            request.op = CHT_GET;
            request.key = key;
            request.value = 0;
        } else if (strcmp(op, "delete") == 0) {
            request.op = CHT_DELETE;
            request.key = key;
            request.value = 0;
        }
        enqueue_cht_request(requests, request);
    }
    fclose(request_file);
}

int main(int argc, char* argv[]) {
    assert(argc == 3);
    int NUM_THREADS = atoi(argv[1]);
    size_t NUM_BUCKETS = atoi(argv[2]);
    pthread_t threads[NUM_THREADS];
    cht_thread_arg* args[NUM_THREADS];

    cht* ht = init_cht(NUM_BUCKETS, modulo_hash);
    barrier* global_barrier = init_barrier(NUM_THREADS + 1);

    for (int i = 0; i < NUM_THREADS; ++i) {
        args[i] = malloc(sizeof(cht_thread_arg));
        cht_request_queue* requests = init_cht_request_queue();
        load_requests_from_file(i, requests);
        args[i]->ht = ht;
        args[i]->global_barrier = global_barrier;
        args[i]->requests = requests;
        args[i]->result_count = 0;
        args[i]->results = NULL;
    }
    for (int i = 0; i < NUM_THREADS; ++i) {
        pthread_create(&threads[i], NULL, thread_cht_requests, (void*)args[i]);
    }
    barrier_wait(global_barrier);  // This ensures that the main thread does not read the results until the worker threads have completed
                                   // their operations.
    for (int i = 0; i < NUM_THREADS; ++i) {
        printf("Results for thread %d:\n", i);
        for (int j = 0; j < args[i]->result_count; ++j) {
            printf("Result %d: %d\n", j, args[i]->results[j]);  // Note that each worker thread's results[] is being read from by the main
                                                                // thread even after the completion of thread_cht_requests()
        }
    }
    return 0;
}