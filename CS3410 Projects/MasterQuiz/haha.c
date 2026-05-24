#include <stdio.h>
#include <stdint.h>

int main() {
    uint32_t comb = 0b00000000000000001010101000001111;
    uint32_t mask = 0b00000000000000000000000011111111;
    printf("%032b\n", comb & mask);
    printf("%032b\n", comb | mask);
    printf("%032b\n", comb ^ mask);
    return 0; 
}