#ifndef RWL_H
#define RWL_H
#include <stdint.h>
#include <stdlib.h>
#include "spinlock.h"
#include "wait-broadcast.h"
typedef struct {
    volatile int lock;
    volatile uint32_t condition;
    volatile int active_writer;
    volatile int waiting_writers;
    volatile int readers;
} rw_lock;

/* NOTE: This readers-writer lock must implement a write-preferring policy.
 * This means that, when there is a writer waiting to acquire the lock, no
 * new readers can acquire it.
 */

/* Create and set up a new readers-writer lock (i.e., a rw_lock struct)
 * Return a pointer to the created readers-writer lock.
 * Note: caller is responsible for deallocation of the return value.
 */
rw_lock* init_rwl(void);

/* A thread that wishes to read from the data protected by rwl calls
 * this function. If there is an active writer, the calling thread
 * should sleep until the writer releases the lock.
 */
void start_read(rw_lock* rwl);

/* A thread calls this function to indicate that its read from the
 * data protected by rwl has completed. This should release its
 * hold on the lock.
 */
void end_read(rw_lock* rwl);

/* A thread that wishes to write to the data protected by rwl calls
 * this function. If there is an active writer or active reader,
 * the calling thread should sleep until the lock is released.
 */
void start_write(rw_lock* rwl);

/* A thread calls this function to indicate that its write from the
* data protected by rwl has completed. This should release its hold
on the lock.
*/
void end_write(rw_lock* rwl);

#endif