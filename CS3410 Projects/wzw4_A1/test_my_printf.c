#include "my_printf.h"

int main(int argc, char *argv[])
{
  // PRINT INTEGER TESTS
  // TODO: Add more tests
  printf("----------------------------");
  printf("\nPRINT INTEGER TESTS:\n");
  printf("----------------------------\n");
  print_integer(768336, 10, ""); // Test 1
  fputc('\n', stdout);
  print_integer(-768336, 10, ""); // Test 2
  fputc('\n', stdout);
  print_integer(-768336, 10, "$"); // Test 3, etc.
  fputc('\n', stdout);

  // More Tests
  // TODO: Add more tests
  printf("\n----------------------------");
  printf("\nMore print_integer TESTS:\n");
  printf("----------------------------\n");
  print_integer(3410, 16, "0x");
  fputc('\n', stdout);
  print_integer(-3410, 16, "0x");
  fputc('\n', stdout);
  print_integer(3410, 2, "ob");
  fputc('\n', stdout);
  print_integer(-3410, 2, "0b");
  fputc('\n', stdout);

  // TODO: Add more tests
  print_integer(100, 2, "ob");
  fputc('\n', stdout);
  print_integer(-100, 2, "0b");
  fputc('\n', stdout);
  print_integer(100, 3, "Three-");
  fputc('\n', stdout);
  print_integer(-100, 3, "Three-");
  fputc('\n', stdout);
  print_integer(100, 4, "Four-");
  fputc('\n', stdout);
  print_integer(-100, 4, "Four-");
  fputc('\n', stdout);
  print_integer(-100, 4, "Four-");
  fputc('\n', stdout);
  print_integer(100, 5, "Five-");
  fputc('\n', stdout);
  print_integer(-100, 5, "Five-");
  fputc('\n', stdout);
  print_integer(100, 6, "Six-");
  fputc('\n', stdout);
  print_integer(-100, 6, "Six-");
  fputc('\n', stdout);
  print_integer(100, 7, "Seven-");
  fputc('\n', stdout);
  print_integer(-100, 7, "Seven-");
  fputc('\n', stdout);
  print_integer(100, 8, "Eight-");
  fputc('\n', stdout);
  print_integer(-100, 8, "Eight-");
  fputc('\n', stdout);
  print_integer(100, 9, "Nine-");
  fputc('\n', stdout);
  print_integer(-100, 9, "Nine-");
  fputc('\n', stdout);
  print_integer(100, 11, "Eleven-");
  fputc('\n', stdout);
  print_integer(-100, 11, "Eleven-");
  fputc('\n', stdout);
  print_integer(100, 12, "Twelve-");
  fputc('\n', stdout);
  print_integer(-100, 12, "Twelve-");
  fputc('\n', stdout);
  print_integer(100, 13, "Thirteen-");
  fputc('\n', stdout);
  print_integer(-100, 13, "Thirteen-");
  fputc('\n', stdout);
  print_integer(100, 14, "Fourteen-");
  fputc('\n', stdout);
  print_integer(-100, 14, "Fourteen-");
  fputc('\n', stdout);
  print_integer(100, 15, "Fifteen-");
  fputc('\n', stdout);
  print_integer(-100, 15, "Fifteen-");
  fputc('\n', stdout);
  print_integer(100, 16, "0x");
  fputc('\n', stdout);
  print_integer(-100, 16, "0x");
  fputc('\n', stdout);

  // 32 bit max/min
  print_integer(2147483647, 10, ""); // Test 1
  fputc('\n', stdout);
  print_integer(-2147483648, 10, ""); // Test 2
  fputc('\n', stdout);
  print_integer(-2147483648, 2, ""); // Test 2
  fputc('\n', stdout);
  print_integer(2147483647, 2, ""); // Test 1
  fputc('\n', stdout);
  print_integer(-1, 2, ""); // Test 1
  fputc('\n', stdout);

  // MY_PRINTF TESTS
  // TODO: Add more tests
  printf("\n----------------------------");
  printf("\nMY_PRINTF TESTS:\n");
  printf("----------------------------\n");
  my_printf("768336\n");                                  // Test 1
  my_printf("My favorite number is %d!\n", 768336);       // Test 2
  my_printf("%d written in hex is %x\n", 768336, 768336); // Test 3, etc.
  // More Tests
  my_printf("Giga%s\n", "chad");
  my_printf("Huh%c\n", '!');
  my_printf("%d written in binary is %b\n", 10, 10);
  my_printf("I love this, 100%%\n");
  my_printf("%c, %s, %s, %s: %b, %d, %x\n", 'I', "Binary", "Decimal", "Hex", 42, 42, 42);
  my_printf("%%%%\n");
  my_printf("%s\n", "");
  my_printf("Null Str: %s\n", NULL);
  my_printf("%q\n");
  return EXIT_SUCCESS;
}
