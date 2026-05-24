#ifndef __SIMULATOR_H
#define __SIMULATOR_H

#include "cache.h"
#include "cache_stats.h"
#include <stdbool.h>

typedef struct {
  char *trace;

  // print per access information, by default off
  bool verbose_f;

  // optionally limit the simulation to the first N insns
  // do not break this functionality when you complete the code!
  bool limit_insn_f;
  int insn_limit;

  cache_t *cache;
} simulator_t;

simulator_t *make_simulator();
void process_trace(simulator_t *sim);

#endif // SIMULATOR
