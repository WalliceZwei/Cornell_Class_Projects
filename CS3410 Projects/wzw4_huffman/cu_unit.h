#ifndef CU_UNIT_H
#define CU_UNIT_H

#include <stdio.h>
#include <unistd.h>
#include <stdbool.h>

#define cu_start() int __cu_line_number_of_first_failure = 0

#define cu_check(condition)                                   \
  if (!(condition) && __cu_line_number_of_first_failure == 0) \
  __cu_line_number_of_first_failure = __LINE__

#define cu_end() return __cu_line_number_of_first_failure

#define cu_start_tests() int __cu_number_of_tests = 0, __cu_number_of_successes = 0

#define cu_run(test)                                                               \
  do                                                                               \
  {                                                                                \
    ++__cu_number_of_tests;                                                        \
    int __cu_result = test();                                                      \
    if (__cu_result == 0)                                                          \
    {                                                                              \
      if (isatty(STDOUT_FILENO))                                                   \
      {                                                                            \
        printf("\x1b[32mTest passed: %s\x1b[0m\n", #test);                         \
      }                                                                            \
      else                                                                         \
      {                                                                            \
        printf("Test passed: %s\n", #test);                                        \
      }                                                                            \
      ++__cu_number_of_successes;                                                  \
    }                                                                              \
    else                                                                           \
    {                                                                              \
      if (isatty(STDOUT_FILENO))                                                   \
      {                                                                            \
        printf("\x1b[31mTest failed: %s at line %d\x1b[0m\n", #test, __cu_result); \
      }                                                                            \
      else                                                                         \
      {                                                                            \
        printf("Test failed: %s at line %d\n", #test, __cu_result);                \
      }                                                                            \
    }                                                                              \
  } while (false)

#define cu_end_tests()                                                                                \
  do                                                                                                  \
  {                                                                                                   \
    if (isatty(STDOUT_FILENO))                                                                        \
    {                                                                                                 \
      printf(__cu_number_of_successes == __cu_number_of_tests ? "\x1b[32m" : "\x1b[31m");             \
      printf("Summary: %d/%d tests passed\x1b[0m\n", __cu_number_of_successes, __cu_number_of_tests); \
    }                                                                                                 \
    else                                                                                              \
    {                                                                                                 \
      printf("Summary: %d/%d tests passed\n", __cu_number_of_successes, __cu_number_of_tests);        \
    }                                                                                                 \
  } while (false)

#endif // CU_UNIT