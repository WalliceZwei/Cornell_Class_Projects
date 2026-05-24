#include "minifloat.h"

int main(void)
{
    // part 1
    print_mini(56); // +1.000000
    printf("\n");
    print_mini(184); // -1.000000
    printf("\n");

    // part 2
    printf("%d\n", mini_eq(100, 104));  // eq(01100100, 01101000) = 0
    printf("%d\n", mini_eq(100, 88));   // eq(01100100, 01011000) = 1

    // Add 1 and 1: mini_add(1, 1) = 2 => 00000010
    printf("%u\n", mini_add(1, 1));     
    // Add two minifoats with different exponents: mini_add(10011000, 10000010) = 153 => 10011001
    printf("%u\n", mini_add(152, 130)); 

    // Multiplication of 1 and a minifloat: mini_mul(00111000, 01001100) = 76 => 01001100
    printf("%u\n", mini_mul(56, 76));   
    // Multiplication of 0 and a minifloat: mini_mul(01111111, 00010000) = 0 => 00000000
    printf("%u\n", mini_mul(127, 16));  

    return 0;
}