#include "utils.h"

void print_list(PQNode *head, void (*print_one_element_fn)(void *))
{
  // print_one_element_fn(…) is a function
  for (PQNode *curr = head; curr != NULL; curr = curr->next)
  {
    printf("[");
    print_one_element_fn(curr->a_value); // print this element
    printf("]");
    if (curr->next != NULL)
    { // If curr is not the tail...
      printf("->");
    }
  }
  printf("\n");
}

void _print_tree_node(void *a_node)
{
  TreeNode *node = (TreeNode *)a_node;
  if (node == NULL)
  {
    printf("{NULL}");
    return;
  }
  printf("{char: %c, freq: %zu}", node->character, node->frequency);
}

