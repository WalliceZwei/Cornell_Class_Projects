#include "readers-writer.h"

rw_lock *init_rwl(void) {
  rw_lock *lock = (rw_lock *)malloc(sizeof(rw_lock));
  if (lock == NULL) {
    return NULL;
  }
  lock->lock = 0;
  lock->condition = 0;
  lock->active_writer = 0;
  lock->waiting_writers = 0;
  lock->readers = 0;
  return lock;
}

void start_read(rw_lock *rwl) {
  spin_lock(&rwl->lock);
  while (rwl->active_writer || rwl->waiting_writers > 0) {
    wait(&rwl->lock, &rwl->condition);
  }
  rwl->readers++;
  spin_unlock(&rwl->lock);
}

void end_read(rw_lock *rwl) {
  spin_lock(&rwl->lock);
  rwl->readers--;
  if (rwl->readers == 0) {
    broadcast(&rwl->condition);
  }
  spin_unlock(&rwl->lock);
}

void start_write(rw_lock *rwl) {
  spin_lock(&rwl->lock);
  rwl->waiting_writers++;
  while (rwl->active_writer || rwl->readers > 0) {
    wait(&rwl->lock, &rwl->condition);
  }
  rwl->waiting_writers--;
  rwl->active_writer = 1;
  spin_unlock(&rwl->lock);
}

void end_write(rw_lock *rwl) {
  spin_lock(&rwl->lock);
  rwl->active_writer = 0;
  broadcast(&rwl->condition);
  spin_unlock(&rwl->lock);
}
