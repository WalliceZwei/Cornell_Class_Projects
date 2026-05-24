#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

/**
 * Returns the index of the smallest element in long array arr with length len.
 */
int smallest_idx(long *arr, int len) {
  int smallest_i = 0;
  long smallest = arr[0];

  for (int i = 1; i < len; i++) {
    if (arr[i] < smallest) {
      smallest = arr[i];
      smallest_i = i;
    }
  }

  return smallest_i;
}

/**
 * Swaps the values stored at the memory addresses pointed to by a and b.
 */
void swap(long *a, long *b) {
  long tmp = *a;
  *a = *b;
  *b = tmp;
}

/**
 * Performs an in-place selection sort on long array arr with length len.
 */
void selection_sort(long *arr, int len) {

  for (int i = 0; i < len; i++) {
    int swap_idx = smallest_idx(&arr[i], len - i);
    swap((long *)arr[i], (long *)arr[swap_idx]);
  }
}

/**
 * Prints the long array arr with length len to stdout.
 */
void print_array(long *arr, int len) {
  int i = 0;
  while (i <= len) {
    printf("%ld ", arr[i]);
    i++;
  }
  puts("");
}

int main(int argc, char *argv[]) {
  long test_array[5] = {1, 4, 2, 0, 3};
  long test_array2[25] = {
      0xddad409b, 0x6b401dbe, 0xc59beda0, 0xf29ec713, 0xbd22bf00,
      0x8ea28e5a, 0x2dbfc88e, 0xced646a2, 0x658b9fb0, 0x60fd9368,
      0x84feeed6, 0xa41554d7, 0x1759b87f, 0xe30b22c7, 0x11058345,
      0x642a9aa6, 0x9d3162cf, 0x00fef5ec, 0x6fc75229, 0x221237bd,
      0x4d687204, 0x5d210617, 0x7cf400f9, 0x53d6b17d, 0x00064ffe};

  selection_sort(test_array, 5);
  selection_sort(test_array2, 25);

  printf("Test array #1:\n");
  print_array(test_array, 5);
  printf("\nTest array #2:\n");
  print_array(test_array2, 25);
}