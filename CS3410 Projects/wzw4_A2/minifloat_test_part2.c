#include "minifloat.h"

#include <stdint.h>
#include <stdio.h>

// Utility functions

/*
 * Helper method to return 0 if mini_eq produced the expected result
 *   Otherwise prints an error message and returns 1
 */
int eq_check(uint8_t mini1, uint8_t mini2, int expected)
{
    int result = mini_eq(mini1, mini2);
    if (result == expected)
    {
        return 0;
    }
    else
    {
        printf("Equality between %02x and %02x: expected %d, got %d\n",
               mini1, mini2, expected, result);
        return 1;
    }
}

/*
 * Helper method to return 0 if mini_add produced the expected result
 *   Otherwise prints an error message and returns 1
 */
int add_check(uint8_t mini1, uint8_t mini2, uint8_t expected)
{
    uint8_t result = mini_add(mini1, mini2);
    if (result == expected)
    {
        return 0;
    }
    else
    {
        printf("Adding %02x and %02x: expected %02x, got %02x\n",
               mini1, mini2, expected, result);
        return 1;
    }
}

/*
 * Helper method to return 0 if mini_mul produced the expected result
 *   Otherwise prints an error message and returns 1
 */
int mul_check(uint8_t mini1, uint8_t mini2, uint8_t expected)
{
    uint8_t result = mini_mul(mini1, mini2);
    if (result == expected)
    {
        return 0;
    }
    else
    {
        printf("Multiplying %02x and %02x: expected %02x, got %02x\n",
               mini1, mini2, expected, result);
        return 1;
    }
}

// Tests

/*
 * Tests mini_eq
 */
int test_eq(void)
{
    int errors = 0;

    errors += eq_check(0x00, 0x00, 1);
    errors += eq_check(0x01, 0x00, 0);
    errors += eq_check(0xf0, 0x00, 1);
    errors += eq_check(0x80, 0x70, 1);

    errors += eq_check(40, 168, 0); // different sign, although same everything else 
    errors += eq_check(240, 128, 1); // different representations of 0, equal 
    errors += eq_check(210, 184, 1); // same number, just different exponents (differ by two), mantissa's shifted over my exponent
    errors += eq_check(184, 210, 1); // Order shouldn't matter 
    errors += eq_check(159, 159, 1); // same number should always be equal 
    errors += eq_check(209, 184, 0); // Differing exponents/mantissas, but not equal 

    // Add at least 4 more tests here

    return errors;
}

/*
 * Tests mini_add
 */
int test_add(void)
{
    int errors = 0;

    errors += add_check(0x01, 0x01, 0x02); // Adding identical minifloats
    errors += add_check(0x11, 0x01, 0x03); // Adding minifloats with different exponents but identical mantissas
    errors += add_check(0x98, 0x82, 0x99); // Adding minifloats with different exponents and mantissa
    errors += add_check(0x2a, 0xc1, 0x1c); // Adding positive and negative minifloats

    errors += add_check(0x07, 0x80, 0x07); // Adding 0 to a minifloat
    errors += add_check(0x11, 0x00, 0x02); // Adding 0 to a minifloat without the smallest exponent
    errors += add_check(0x18, 0x1a, 0x29); // Adding minifloats and the direct sum of their mantissas is 5 binary digit. 
    errors += add_check(0x48, 0x08, 0x49); // rounding test in which the result should be rounded up
    errors += add_check(0x48, 0x04, 0x48); // rounding test in which the result should be rounded down
    
    // my tests 

    errors += add_check(104, 202, 91); // Adding 8 and -2.5, two relatively large numbers together, positive and negative
    errors += add_check(128, 0, 0); // -0.0+0.0 = 0.0
    errors += add_check(202, 168, 204); // -2.5 + -.5 = -3, two more negative numbers to add together
    errors += add_check(207, 177, 216); // Rounding, carry over exponent when rounding, 11001111 + 10110001 = 11011000
    

    // Add at least 4 more tests here
    // Suggestions: check addition cases with different representations of 0;
    //              check more additions between negative and positive minifloats;
    //              think about how to deal with addition results that could be represented with different exponents;
    //              check addition cases whose results need rounding;
    return errors;
}

/*
 * Tests mini_mul
 */
int test_mul(void)
{
    int errors = 0;

    errors += mul_check(0x38, 0x4c, 0x4c); // Multiplying 1 with a minifloat
    errors += mul_check(0x7f, 0x00, 0x00); // Multiplying 0 with a minifloat
    errors += mul_check(0xa8, 0x39, 0xa9); // Multiplying a minifloat with 1 as mantissa and another minifloat with 1 as exponent
    errors += mul_check(0x3c, 0x3c, 0x49); // Multiplying two identical minifloats with 1 as exponents

    errors += mul_check(0x05, 0x28, 0x03); // Rounding test where the result should round up
    errors += mul_check(0xac, 0xac, 0x29); // Multiplying two negative minifloats whose result needs exponent shifting
    errors += mul_check(0x4a, 0x5b, 0x6e); // Rounding test where the result should round up 
    errors += mul_check(0x1c, 0x7a, 0x5f); // Multiplying two minifloats with negative and positive exponents
    errors += mul_check(0x3a, 0x49, 0x4b); // Rounding test where the result should round down
    errors += mul_check(0x25, 0x25, 0x06); // Rounding test where the results should round down

    // Add at least 4 more tests here if choose two implement 
    // Suggestions: check multiplication cases with different representations of 0 and 1;
    //              check more multiplication between minifloats with different signs or same signs (negative + negative);
    //              check more cases where the exponent of the result needs to be shifted. 
    //              check more cases where the results should be rounded;


    return errors;
}

int main(void)
{
    int errors;
    errors = test_eq();
    printf("\n");
    printf("test_eq %s with %d errors\n", errors == 0 ? "passed" : "failed", errors);
    printf("\n");
    errors = test_add();
    printf("\n");
    printf("test_add %s with %d errors\n", errors == 0 ? "passed" : "failed", errors);
    printf("\n");
    errors = test_mul();
    printf("\n");
    printf("test_mul %s with %d errors\n", errors == 0 ? "passed" : "failed", errors);
    printf("\n");
}
