#include "spinlock.h"

void spin_lock(volatile int *lock) {
  int val;

  while (1) {
    __asm__ volatile("1:\n"
                     "lr.w.aqrl %0, (%1)\n"
                     "bnez  %0, 1b\n"
                     "li    %0, 1\n"
                     "sc.w.aqrl %0, %0, (%1)\n"
                     : "=&r"(val)
                     : "r"(lock)
                     : "memory");
    if (val == 0)
      break;
  }
}

void spin_unlock(volatile int *lock) {
  __asm__ volatile("sw zero, (%0)\n" : : "r"(lock) : "memory");
}
