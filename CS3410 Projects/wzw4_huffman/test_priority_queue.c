#include "priority_queue.h"
#include "memcheck.h"
#include "cu_unit.h"
#include "utils.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

static bool is_sorted(PQNode *head, int (*cmp_fn)(const void *, const void *))
{
  PQNode *curr = head;
  while (curr != NULL && curr->next != NULL)
  {
    if (cmp_fn(curr->a_value, curr->next->a_value) > 0)
    {
      return false;
    }
    curr = curr->next;
  }
  return true;
}

static int _cmp_int(const void *a, const void *b)
{
  int x = *((int *)a);
  int y = *((int *)b);
  return x - y;
}

static int _cmp_char(const void *a, const void *b)
{
  char x = *((char *)a);
  char y = *((char *)b);
  return x - y;
}

static int _cmp_int_rev(const void *a, const void *b)
{
  return -_cmp_int(a, b);
}

static void _print_int(void *a_n)
{
  int n = *((int *)a_n);
  printf("%d", n);
}

static int _cmp_strings_by_length(const void *a, const void *b)
{
  size_t len_a = strlen(a);
  size_t len_b = strlen(b);
  return len_a - len_b;
}

static char *_copy_string(const char *src_string)
{
  size_t num_chars = strlen(src_string);
  char *dst_string = my_malloc(sizeof(*dst_string) * (num_chars + 1));
  return strcpy(dst_string, src_string);
}

static int _test_simple_pq()
{
  cu_start();
  // -------------------------------
  PQNode *head = NULL;
  int n1 = 5, n2 = 7, n3 = 6;
  pq_enqueue(&head, &n1, _cmp_int);
  pq_enqueue(&head, &n2, _cmp_int);
  pq_enqueue(&head, &n3, _cmp_int);
  cu_check(is_sorted(head, _cmp_int));
  PQNode *first_node = pq_dequeue(&head);
  cu_check(*((int *)first_node->a_value) == 5);
  PQNode *second_node = pq_dequeue(&head);
  cu_check(*((int *)second_node->a_value) == 6);
  PQNode *third_node = pq_dequeue(&head);
  cu_check(*((int *)third_node->a_value) == 7);
  cu_check(head == NULL);
  my_free(first_node);
  my_free(second_node);
  my_free(third_node);
  // -------------------------------
  cu_end();
}

static int _test_simple_stack()
{
  cu_start();
  // -------------------------------
  PQNode *stack = NULL;
  int n1 = 5, n2 = 7, n3 = 6;
  stack_push(&stack, &n1);
  stack_push(&stack, &n2);
  stack_push(&stack, &n3);
  PQNode *first_node = stack_pop(&stack);
  cu_check(*((int *)first_node->a_value) == 6);
  PQNode *second_node = stack_pop(&stack);
  cu_check(*((int *)second_node->a_value) == 7);
  PQNode *third_node = stack_pop(&stack);
  cu_check(*((int *)third_node->a_value) == 5);
  cu_check(stack == NULL);
  my_free(first_node);
  my_free(second_node);
  my_free(third_node);
  // -------------------------------
  cu_end();
}

static int _test_string_single_pq()
{
  cu_start();
  // -------------------------------
  PQNode *head = NULL;
  pq_enqueue(&head, _copy_string("Reginald"), _cmp_strings_by_length);
  cu_check(head != NULL);
  cu_check(head->a_value != NULL);
  cu_check(strcmp(head->a_value, "Reginald") == 0);
  destroy_list(&head);
  cu_end();
}

static int _test_string_pq()
{
  cu_start();
  // -------------------------------
  PQNode *head = NULL;
  pq_enqueue(&head, _copy_string("Reginald"), _cmp_strings_by_length);
  pq_enqueue(&head, _copy_string("Bobby"), _cmp_strings_by_length);
  pq_enqueue(&head, _copy_string("Alice"), _cmp_strings_by_length);
  cu_check(is_sorted(head, _cmp_strings_by_length));
  PQNode *first_node = pq_dequeue(&head);
  cu_check(strcmp(first_node->a_value, "Bobby") == 0);
  PQNode *second_node = pq_dequeue(&head);
  cu_check(strcmp(second_node->a_value, "Alice") == 0);
  PQNode *third_node = pq_dequeue(&head);
  cu_check(strcmp(third_node->a_value, "Reginald") == 0);
  cu_check(head == NULL);
	destroy_list(&first_node);
	destroy_list(&second_node);
	destroy_list(&third_node);
  // -------------------------------
  cu_end();
}

static int _test_dequeue_from_empty()
{
  cu_start();
  // -------------------------------
  PQNode *head = NULL;
  PQNode *node = pq_dequeue(&head);
  cu_check(node == NULL);
  // -------------------------------
  cu_end();
}

static int _test_enqueue_to_empty()
{
  cu_start();
  // -------------------------------
  PQNode *head = NULL;
  int n1 = 5;
  pq_enqueue(&head, &n1, _cmp_int);
  PQNode *node = pq_dequeue(&head);
  cu_check(*((int *)node->a_value) == 5);
  cu_check(head == NULL);
  my_free(node);
  destroy_list(&head);
  // -------------------------------
  cu_end();
}

static int _test_int_pq_descending()
{
  cu_start();
  // -------------------------------
  PQNode *head = NULL;
  int n1 = 5, n2 = 7, n3 = 6;
  pq_enqueue(&head, &n1, _cmp_int_rev);
  pq_enqueue(&head, &n2, _cmp_int_rev);
  pq_enqueue(&head, &n3, _cmp_int_rev);
  cu_check(is_sorted(head, _cmp_int_rev));
  PQNode *first_node = pq_dequeue(&head);
  cu_check(*((int *)first_node->a_value) == 7);
  PQNode *second_node = pq_dequeue(&head);
  cu_check(*((int *)second_node->a_value) == 6);
  PQNode *third_node = pq_dequeue(&head);
  cu_check(*((int *)third_node->a_value) == 5);
  cu_check(head == NULL);
  my_free(first_node);
  my_free(second_node);
  my_free(third_node);
  // -------------------------------
  cu_end();
}

static int _test_int_random()
{
  cu_start();
  // -------------------------------
  PQNode *head = NULL;
  srand(time(NULL));
  int min = 0;
  int max = 1000;
  for (int i = 0; i < 20; i++)
  {
    int *n = my_malloc(sizeof(int));
    *n = rand() % (max - min + 1) + min;
    pq_enqueue(&head, n, _cmp_int);
  }
  cu_check(is_sorted(head, _cmp_int));
  destroy_list(&head);
  // -------------------------------
  cu_end();
}

static int _test_char_single()
{
  cu_start();
  // -------------------------------
  PQNode *head = NULL;
  char *c = (char *)my_malloc(sizeof(char)); 
  if (c == NULL) {
    return 0;
  }
  *c = 'a';
  pq_enqueue(&head, c, _cmp_int);
  cu_check(head != NULL);
  cu_check(head->a_value != NULL);
  cu_check(*((char *)head->a_value) == 'a');
  destroy_list(&head);
  // -------------------------------
  cu_end();
}

static int _test_char_pq()
{
  cu_start();
  // -------------------------------
  PQNode *head = NULL;
  char c1 = 'a', c2 = 'b', c3 = 'c';
  pq_enqueue(&head, &c1, _cmp_char);
  pq_enqueue(&head, &c2, _cmp_char);
  pq_enqueue(&head, &c3, _cmp_char);
  cu_check(is_sorted(head, _cmp_char));
  PQNode *first_node = pq_dequeue(&head);
  cu_check(*((char *)first_node->a_value) == 'a');
  PQNode *second_node = pq_dequeue(&head);
  cu_check(*((char *)second_node->a_value) == 'b');
  PQNode *third_node = pq_dequeue(&head);
  cu_check(*((char *)third_node->a_value) == 'c');
  cu_check(head == NULL);
  my_free(first_node);
  my_free(second_node);
  my_free(third_node);
  // -------------------------------
  cu_end();
}

int main(int argc, char *argv[])
{
  cu_start_tests();
  cu_run(_test_simple_pq);
  cu_run(_test_simple_stack);
  cu_run(_test_string_single_pq);
  cu_run(_test_string_pq);
  cu_run(_test_dequeue_from_empty);
  cu_run(_test_enqueue_to_empty);
  cu_run(_test_int_pq_descending);
  cu_run(_test_int_random);
  cu_run(_test_char_single);
  cu_run(_test_char_pq);
  cu_end_tests();
  return EXIT_SUCCESS;
}
