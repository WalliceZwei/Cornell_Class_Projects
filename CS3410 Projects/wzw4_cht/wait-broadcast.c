#include "wait-broadcast.h"

#define futex 98
#define futex_wait 0
#define futex_wake 1

void wait(volatile int *lock, volatile uint32_t *condition) {
  uint32_t value = *condition;
  spin_unlock(lock);
  __asm__ volatile("mv a0, %0\n"
                   "li a1, %1\n"
                   "mv a2, %2\n"
                   "li a3, 0\n"
                   "li a4, 0\n"
                   "li a5, 0\n"
                   "li a7, %3\n"
                   "ecall\n"
                   :
                   : "r"(condition), "i"(futex_wait), "r"(value), "i"(futex)
                   : "a0", "a1", "a2", "a3", "a4", "a5", "a7");

  spin_lock(lock);
}

void broadcast(volatile uint32_t *condition) {
  (*condition)++;
  __asm__ volatile("mv a0, %0\n"
                   "li a1, %1\n"
                   "li a2, 0x7fffffff\n"
                   "li a3, 0\n"
                   "li a4, 0\n"
                   "li a5, 0\n"
                   "li a7, %2\n"
                   "ecall\n"
                   :
                   : "r"(condition), "i"(futex_wake), "i"(futex)
                   : "a0", "a1", "a2", "a3", "a4", "a5", "a7");
}