#ifndef BARRIER_H
#define BARRIER_H
#include <stdint.h>
#include <stdlib.h>
#include "spinlock.h"
#include "wait-broadcast.h"

typedef struct {
    volatile int num_waiting;
    int total;
    int generation;
    volatile int lock;
    volatile uint32_t condition;
} barrier;

/* Create and set up a new barrier (i.e., a barrier struct), where
 * NUM_THREADS is how many threads must reach the barrier before the
 * waiting threads can move on. Returns a pointer to the created barrier.
 * Note: caller is responsible for deallocation of the return value.
 */
barrier* init_barrier(int NUM_THREADS);

/* If the thread is the num_waiting'th thread to reach the barrier, it
 * should wake up the waiting threads, and they should all continue.
 * The thread that calls this function should otherwise wait. The barrier
 * should be able to be used for multiple generations (i.e. if there are
 * (num_waiting * 2 - 1) threads that each call this function, the first
 * num_waiting threads should continue, and the remaining (num_waiting - 1)
 * threads should wait until another thread reaches the barrier).
 */
void barrier_wait(barrier* bar);

#endif
