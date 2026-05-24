#include "priority_queue.h"
#include "memcheck.h"

// TODO: Task 1
PQNode *pq_enqueue(PQNode **a_head, void *a_value,
                   int (*cmp_fn)(const void *, const void *)) {
  PQNode **head = a_head;
  PQNode *new = my_malloc(sizeof(PQNode));
  new->a_value = a_value;

  while (*a_head != NULL && cmp_fn((*a_head)->a_value, new->a_value) <= 0) {
    a_head = &(*a_head)->next;
  }
  new->next = *a_head;
  *a_head = new;

  return *head;
}

// TODO: Task 1
PQNode *pq_dequeue(PQNode **a_head) {
  if ((*a_head) == NULL) {
    return NULL;
  }
  PQNode *front = *a_head;
  *a_head = (*a_head)->next;
  front->next = NULL;
  return front;
}

// TODO: Task 1
PQNode *stack_push(PQNode **stack, void *a_value) {
  PQNode *new = my_malloc(sizeof(PQNode));
  new->a_value = a_value;
  new->next = *stack;
  *stack = new;
  return new;
}

// TODO: Task 1
PQNode *stack_pop(PQNode **stack) {
  if ((*stack) == NULL) {
    return NULL;
  }
  PQNode *front = *stack;
  *stack = (*stack)->next;
  front->next = NULL;
  return front;
}

// TODO: Task 1
void destroy_list(PQNode **a_head) {
  PQNode *temp;
  while (*a_head != NULL) {
    temp = *a_head;
    *a_head = (*a_head)->next;
    my_free(temp->a_value); 
    my_free(temp);          
  }
}