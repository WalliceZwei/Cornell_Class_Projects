#ifndef SPINLOCK_H
#define SPINLOCK_H

/* The value of the lock is contained in the int pointed to by lock.
 * A non-zero value indicates that lock is taken. A zero value
 * indicates that lock is free.
 * This function should repeatedly attempt to acquire the lock until
 * it is successful.
 */
void spin_lock(volatile int* lock);

/* This function should set the value pointed to by lock to indicate
 * that the lock is free, thereby releasing it.
 */
void spin_unlock(volatile int* lock);

#endif