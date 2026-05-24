#ifndef MINIFLOAT_H_
#define MINIFLOAT_H_

#include "stdio.h"
#include "stdint.h"

// PART 1

/*
 * Prints a formatted 3410 minifloat to console in the form
 *   sign number '.' decimal
 * Note that there is no space between each of these terms
 * This result is displayed in base-10, and the sign is always written
 * The whole number may be 0, but otherwise has no extra zeros
 * The decimal is written with six digits (hint: we cannot have more than that)
 * Note that the decimal must be exact
 *
 * Examples:
 *   1 000 0000 -> "-0.000000"
 *   0 011 1000 -> "+1.000000"
 *   1 011 1100 -> "-1.500000"
 *   1 100 1011 -> "-2.750000"
 *   0 010 0010 -> "+0.125000"
 *   0 111 1000 -> "+16.000000"
 */
void print_mini(uint8_t mini);

// PART 2

/*
 * Returns 1 if two minifloats are equal, and 0 otherwise
 * Note that two minifloats may be equal even if they do not have bit equality
 * Examples:
 *   mini_eq(01100100, 01100100) -> 1
 *   mini_eq(01100100, 01101000) -> 0
 *   mini_eq(01100100, 01011000) -> 1
 *   mini_eq(11010000, 00000000) -> 1
 */
int mini_eq(uint8_t mini1, uint8_t mini2);

/*
 * Returns the minifloat result from mini1 + mini2,
 *   rounded to the nearest minifloat
 * In case of a tie, rounds _away_ from zero
 *   (so 1.1875 -> 1.25 and -1.1875 -> -1.25)
 * If there are multiple equal potential minifloats,
 *   returns the minifloat with the _smallest_ exponent
 * If we would return 0, return exactly 0000 0000
 *
 * Examples:
 *   mini_add(00000001, 00000001) -> 00000010
 *   mini_add(00010001, 00000001) -> 00000011
 *   mini_add(10011000, 10000010) -> 10011001
 *   mini_add(00101010, 11000001) -> 00011100
 */
uint8_t mini_add(uint8_t mini1, uint8_t mini2);

/*
 * Returns the minifloat result from mini1 * mini2,
 *   rounded to the nearest minifloat
 * In case of a tie, rounds _away_ from zero
 *   (so 1.1875 -> 1.25 and -1.1875 -> -1.25)
 * If there are multiple equal potential minifloats,
 *   returns the minifloat with the _smallest_ exponent
 * If we would return 0, return exactly 0000 0000
 *
 * Examples:
 *   mini_mul(00111000, 01001100) -> 01001100
 *   mini_mul(01111111, 00010000) -> 00000000
 *   mini_mul(10101000, 00111001) -> 10101001
 *   mini_mul(00111100, 00111100) -> 01001001
 */
uint8_t mini_mul(uint8_t mini1, uint8_t mini2);

/*
 * Returns a double with roughly the same decimal as a given minifloat
 * Useful for debugging and testing composed minifloat results
 */
double mini_to_double(uint8_t mini);

#endif // MINIFLOAT_H_
