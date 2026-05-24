#ifndef __PRINT_HELPERS_H
#define __PRINT_HELPERS_H

#include "cache.h"
#include "cache_stats.h"
#include "simulator.h"
#include <stdbool.h>

/* if you want verbose mode to work, you will need to call these 2 functions */
void log_set(int set);
void log_way(int way);

void print_simulator_header(simulator_t *sim);

void print_insn_info(simulator_t *sim, char cmd, unsigned long address,
                     bool hit_f);
void print_trace_stats(cache_stats_t *stats);

void print_stats(cache_stats_t *stats);

void print_cache_config(cache_t *cache);

#endif // PRINT_HELPERS
