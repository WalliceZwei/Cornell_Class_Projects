#include "cu_unit.h"
#include "huffman.h" // contains Frequencies and calc_frequencies
#include <stdbool.h>
#include <stdio.h>
#include <string.h>

static int _test_calc_frequencies() {
  cu_start();

  Frequencies freqs = {0};
  const char *error = NULL;

  bool ok = calc_frequencies(freqs, "animals.txt", &error);
  cu_check(ok); // File should open successfully

  // Check counts of each character
  cu_check(freqs['c'] == 2);
  cu_check(freqs['a'] == 2);
  cu_check(freqs['t'] == 2);
  cu_check(freqs['d'] == 1);
  cu_check(freqs['o'] == 1);
  cu_check(freqs['g'] == 1);

  // Check total number of characters counted
  size_t total = 0;
  for (int i = 0; i < 256; i++) {
    total += freqs[i];
    if (freqs[i] > 0) {
      printf("%d", i);
      printf("\n");
    }
  }

  //   printf("%d", total);
  cu_check(total == 10);

  cu_end();
}

static int _test_missing_file() {
  cu_start();

  Frequencies freqs = {0};
  const char *error = NULL;

  bool ok = calc_frequencies(freqs, "nonexistent.txt", &error);
  cu_check(!ok);           // Should fail to open file
  cu_check(error != NULL); // Error message should be set

  cu_end();
}

int main(int argc, char *argv[]) {
  cu_start_tests();

  cu_run(_test_calc_frequencies);
  cu_run(_test_missing_file);

  cu_end_tests();
  return 0;
}
