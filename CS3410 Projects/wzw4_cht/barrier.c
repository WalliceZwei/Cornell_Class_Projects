#include "barrier.h"

barrier *init_barrier(int NUM_THREADS) {
  barrier *bar = (barrier *)malloc(sizeof(barrier));
  bar->num_waiting = 0;
  bar->total = NUM_THREADS;
  bar->generation = 0;
  bar->lock = 0;
  bar->condition = 0;
  return bar;
}

void barrier_wait(barrier *bar) {

  spin_lock(&bar->lock);

  int generation_num = bar->generation;

  bar->num_waiting++;

  if (bar->num_waiting == bar->total) {
    bar->generation++;
    bar->num_waiting = 0;
    broadcast(&bar->condition);
    spin_unlock(&bar->lock);
    return;
  }

  while (generation_num == bar->generation) {
    wait(&bar->lock, &bar->condition);
  }

  spin_unlock(&bar->lock);
}
