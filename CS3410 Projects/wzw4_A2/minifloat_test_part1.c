#include "minifloat.h"

#include <stdint.h>
#include <stdio.h>

// Utility functions

/*
 * Helper for test_print_mini
 * Formatted print for the given minifloat to help with debugging
 */
void print_mini_check(uint8_t mini) {
  printf("print(0x%02x) -> ", mini);
  print_mini(mini);
  printf("\n");
}

// Tests

/*
 * Prints test case results for print_mini
 */
void test_print_mini(void) {
  print_mini_check(0x74);
  print_mini_check(0x34);
  print_mini_check(0xcb);
  print_mini_check(0x80);

  // Add at least 4 more tests here

  print_mini_check(224); // -0.0
  print_mini_check(96);  // 0.0
  print_mini_check(8);   // 0.125, a smaller number input
  print_mini_check(255); // max_num, -30
  print_mini_check(127); // 30
  print_mini_check(0);   // 0.0
}

int main(void) {
  test_print_mini();
  return 0;
}
