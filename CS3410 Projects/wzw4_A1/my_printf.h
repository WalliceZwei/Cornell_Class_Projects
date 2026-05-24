#ifndef MY_PRINTF_H
#define MY_PRINTF_H

#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include <stdbool.h>
#include <assert.h>
#include <stdarg.h>

/**
 * This function prints a given integer `n` to the standard output in the specified
 * `radix` (base), with an optional `prefix` string that is printed before the number.
 * If the integer is negative, a '-' sign is printed before the prefix and the number.
 *
 * @param n The integer to be printed. It can be a positive or negative number.
 * @param radix The base in which the integer should be printed. The base can
 *              be any number from 2-36 inclusive.
 * @param prefix A null-terminated string to be printed before the number.
 *               If no prefix is desired, an empty string can be passed.
 *
 * @return void
 */
void print_integer(int n, int radix, char *prefix);

/**
 * Prints string to the console, substituting format codes for formatted
 * arguments to the function.
 *
 * Supported format specifiers:
 * - %d : Print an integer in decimal.
 * - %x : Print an integer in hexadecimal with '0x' prefix.
 * - %b : Print an integer in binary with '0b' prefix.
 * - %c : Print a single character.
 * - %s : Print a null-terminated string.
 * - %% : Print a literal '%' character.
 *
 * @param format A null-terminated string specifying the format of the output.
 *               It can include literal text and format specifiers starting with '%'.
 * @param ... Variadic arguments corresponding to the format specifiers.
 *            The number and types of arguments must match the format
 *            specifiers.
 *
 * @return void
 */
void my_printf(const char *format, ...);

#endif // MY_PRINTF_H