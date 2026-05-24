#ifndef WB_H
#define WB_H
#include <stdint.h>
#include "spinlock.h"

/* The thread that calls this function should be put to sleep
 * indefinitely until it receives a call to wake up.
 * Note the significance of uint32_t* condition in avoiding
 * missed wakeups, as explained: https://man7.org/linux/man-pages/man2/futex.2.html
 * NOTE: A sleeping thread can be spuriously woken up.
 */
void wait(volatile int* lock, volatile uint32_t* condition);

/* When this function is called, all sleeping threads should
 * be woken. Modificiations to the value pointed to by condition
 * should be done in this function.
 */
void broadcast(volatile uint32_t* condition);

#endif