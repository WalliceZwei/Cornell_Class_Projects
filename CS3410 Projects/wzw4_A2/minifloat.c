#include "minifloat.h"

// Constants to avoid magic numbers
#define EXP_SIZE (uint8_t)3
#define SIG_SIZE (uint8_t)4

// PART ONE

void print_mini(uint8_t mini) {
  char sign_sym = '+';
  int whole_number = 0;
  int decimal = 0;

  if ((128 & mini) == 128) {
    sign_sym = '-';
  }
  int baselol = -3;
  int exponent = ((112 & mini) >> 4) - 3;

  baselol = baselol + exponent;

  int mantissa = (15 & mini);

  int bigboss_constant = 1000000;

  for (int i = 0; i < 4; i++) {
    if ((mantissa & (1 << i)) == (1 << i)) {
      if (baselol + i < 0) {
        decimal += (bigboss_constant / (1 << (-(baselol + i))));
      } else {
        whole_number += (1 << (baselol + i));
      }
    }
  }
  // Prints the decimal of the minifloat, with exactly 6 decimal places
  printf("%c%d.%06d", sign_sym, whole_number, decimal);
}

// PART TWO

int mini_eq(uint8_t mini1, uint8_t mini2) {
  if (mini1 == mini2) {
    return 1;
  } else if (((15 & mini1) == 0) && ((15 & mini2) == 0)) {
    return 1;
  } else if ((128 & mini1) != (128 & mini2)) {
    return 0;
  } else {
    uint8_t exp1 = ((112 & mini1) >> 4);
    uint8_t exp2 = ((112 & mini2) >> 4);

    if (exp1 > exp2) {
      unsigned short extended1 =
          (unsigned short)((15 & mini1) << (exp1 - exp2));
      return extended1 == (15 & mini2);
    } else if (exp2 > exp1) {
      unsigned short extended2 =
          (unsigned short)((15 & mini2) << (exp2 - exp1));
      return extended2 == (15 & mini1);
    }
  }
  return 0;
}

// unsigned short backtracker() {}

uint8_t mini_add(uint8_t mini1, uint8_t mini2) {
  uint8_t sign1 = 128 & mini1;
  uint8_t sign2 = 128 & mini2;
  uint8_t new_one = 128;
  if (sign1 == sign2) {
    new_one = sign1;
  }
  uint8_t exp1 = ((112 & mini1) >> 4);
  uint8_t exp2 = ((112 & mini2) >> 4);
  uint8_t cur_exp = exp2;
  uint8_t exp_dif = 0;
  unsigned short mantissa1 = 15 & mini1;
  unsigned short mantissa2 = 15 & mini2;
  unsigned short new_m;
  unsigned short new_s;
  // different signs matter, twos compl

  // take the negative first

  if (exp1 > exp2) {
    cur_exp = exp2;
    exp_dif = exp1 - exp2;
    mantissa1 = (unsigned short)(mantissa1 << exp_dif);
    ;

  } else if (exp2 > exp1) {
    cur_exp = exp1;
    exp_dif = exp2 - exp1;
    mantissa2 = (unsigned short)(mantissa2 << exp_dif);
  }

  if (sign1 == sign2) {
    new_m = mantissa2 + mantissa1;
    new_s = sign1;
  } else { // n1<0, n2>0
    if (mantissa1 > mantissa2) {
      new_m = mantissa1 - mantissa2;
      new_s = sign1;
    } else {
      new_m = mantissa2 - mantissa1;
      new_s = sign2;
    }
  }
  unsigned short last_dig;
  while (new_m > 15) {
    last_dig = new_m & 1; // added
    new_m >>= 1;
    cur_exp += 1;
  }
  if (last_dig == 1) {
    new_m += 1;
    if (new_m > 15) {
      new_m >>= 1;
      cur_exp += 1;
    }
  }

  // check this one out
  while (((new_m & 8) == 0) && (new_m > 0) && cur_exp > 0) {
    new_m = (unsigned short)(new_m << 1);
    cur_exp -= 1;
  }

  // edge cases lol
  new_one = (uint8_t)(new_s + (cur_exp << 4) + new_m);

  return new_one;
}
uint8_t mini_mul(uint8_t mini1, uint8_t mini2) {
  // TODO
  return 0;
}

double mini_to_double(uint8_t mini) {
  uint8_t exponent_mask = 0x70;
  uint8_t significand_mask = 0x0f;

  uint8_t sign = (uint8_t)((mini & 0x80) >> (EXP_SIZE + SIG_SIZE));
  uint8_t exponent = (mini & exponent_mask) >> SIG_SIZE;
  uint8_t significand = mini & significand_mask;

  // In the zero case, just return 0.0
  if (significand == 0) {
    return 0.0;
  }

  // Divide out both the bias and the significand offset
  double result = significand / 64.0;

  // Simple power of 2
  while (exponent > 0) {
    result *= 2.0;
    exponent -= 1;
  }

  // Apply the sign
  if (sign) {
    result = -result;
  }

  return result;
}
