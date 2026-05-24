#ifndef HT_H
#define HT_H
#include "readers-writer.h"
#include "barrier.h"
#include <stddef.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <limits.h>

typedef struct cht_node {
    int key;
    int value;
    struct cht_node* next;
} cht_node;

typedef struct {
    cht_node* head;
    rw_lock* bucket_lock;
} cht_bucket;

typedef struct {
    cht_bucket** buckets;
    int num_buckets;
    size_t (*hash_function)(const int key, const size_t num_buckets);
} cht;

/* Create and set up a new hash_table (i.e., a cht struct) from the
 * given size value (num_buckets) and hash function.  Return a pointer to the created cht.
 * Note:  caller is responsible for deallocation of the return value.
 */
cht* init_cht(const size_t num_buckets, size_t (*hash_function)(const int key, const size_t num_buckets));

/* Insert a key/value pair into the hash table ht.
 * If key already exists in ht, its value should be overwritten.
 */
void cht_insert(cht* ht, const int key, const int value);

/* Remove the node with the specified key from hash table ht.  If key is found in ht,
 * it is deleted, along with its associated value.  If key is not found, there is no effect.
 */
void cht_delete(cht* ht, const int key);

/* Return the value in the ht associated with specified key, if key is contained in ht.
 * If there is no such key, return INT_MIN.
 */
int cht_get(cht* ht, const int key);

typedef enum { CHT_INSERT, CHT_DELETE, CHT_GET } cht_operation;

typedef struct {
    cht_operation op;
    int key;
    int value;
} cht_request;

typedef struct cht_request_node {
    cht_request request;
    struct cht_request_node* next;
} cht_request_node;

typedef struct {
    cht_request_node* head;
    cht_request_node* tail;
} cht_request_queue;

/* (COMPLETE)
 * Creates and initializes a new cht_request_queue.  Returns a pointer to the newly-created queue.
 * Caller is responsible for deallocation.
 */
cht_request_queue* init_cht_request_queue(void);

/* (COMPLETE)
 * Adds the specified cht_request to the requests queue.
 */
void enqueue_cht_request(cht_request_queue* requests, cht_request request);

/* (COMPLETE)
 * Removes the cht_request at the front of the requests queue.  If requests is empty, a call to this
 * procedure results in a fatal error.
 */
cht_request dequeue_cht_request(cht_request_queue* requests);

typedef struct cht_thread_arg {
    cht* ht;
    cht_request_queue* requests;
    barrier* global_barrier;
    int* results;
    int result_count;
} cht_thread_arg;

/* Thread function for accessing the contents of a cht.  Per the assignment, arg should be a pointer to
 * a cht_thread_arg, defined above.  It should repeatedly dequeue operations (cht_request values) and
 * perform them: i.e., look at request.op, which is one of CHT_INSERT, CHT_DELETE, or CHT_GET, and
 * call one of your cht_* functions accordingly.
 * At the end of the function, the results array should hold the in-order results of each CHT_GET
 * operation. The result_count variable should be the number of elements in the results array
 * (i.e. the number of CHT_GET operations that this thread performed).
 */
void* thread_cht_requests(void* arg);

#endif
