#define _POSIX_C_SOURCE 200809L
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "print_helpers.h"
#include "simulator.h"

simulator_t *make_simulator() {
  simulator_t *sim = malloc(sizeof(simulator_t));

  sim->trace = "route.short.txt";
  sim->verbose_f = false;

  sim->limit_insn_f = false;
  sim->insn_limit = 0;

  return sim;
}

/*
 * Goes through the trace line by line (i.e., instruction by
 * instruction) and simulates the program being executed on a
 * multicore processor.
 */
void process_trace(simulator_t *sim) {
  char *line = NULL;
  // Program Stats
  long total_insn = 0;

  printf("Processing trace...\n");

  char *path = malloc(strlen(sim->trace) + 7);
  strncpy(path, "trace/", 7);
  strcat(path, sim->trace);
  FILE *trace = fopen(path, "r");
  if (trace == NULL) {
    printf("File \'%s\' not found\n", sim->trace);
    exit(EXIT_FAILURE);
  }
  size_t len = 0;
  size_t read;

  while ((read = getline(&line, &len, trace)) != (size_t)-1) {
    if (sim->limit_insn_f && total_insn == sim->insn_limit) {
      printf("Reached insn limit of %d. Ending Simulation...\n",
             sim->insn_limit);
      break;
    }

    enum action_t action = (line[0] == 'r') ? LOAD : STORE;
    unsigned long address = strtol(&line[2], NULL, 16);

    total_insn++;

    // access the cache
    bool hit_f = access_cache(sim->cache, address, action);

    // prints the insn
    if (sim->verbose_f)
      print_insn_info(sim, line[0], address, hit_f);
  }

  fclose(trace);
  if (line)
    free(line);

  printf("Processed %ld lines.\n", total_insn);

  // compute cache statistics
  calculate_stat_rates(sim->cache->stats, sim->cache->block_size);
  print_stats(sim->cache->stats);
}
