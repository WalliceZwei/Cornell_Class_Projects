#ifndef PRIORITY_QUEUE_H
#define PRIORITY_QUEUE_H

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <assert.h>
#include <stdint.h>

/**
 * A struct representing a node in a priority queue. Each node has a value and a
 * pointer to the next node in the queue.
 */
typedef struct _PQNode
{
  void *a_value;
  struct _PQNode *next;
} PQNode;

/**
 * @brief Add a new node with a_value to the priority queue located at a_head 
 * using the function cmp_fn to determine the ordering of the priority queue.  
 * 
 * @param a_head the head of the priority queue
 * @param a_value the value to be enqueued
 * @param cmp_fn a comparison function to determine the ordering of the priority queue
 * @return PQNode* 
 */
PQNode *pq_enqueue(PQNode **a_head, void *a_value, int (*cmp_fn)(const void *, const void *));

/**
 * @brief Detach the head of the priority queue located at a_head and return it.
 * 
 * @param a_head the head of the priority queue
 * @return PQNode* 
 */
PQNode *pq_dequeue(PQNode **a_head);

/**
 * @brief Add a new node with value a_value to the top of the stack located at stack.
 * 
 * @param stack a pointer to the first node in the linked list
 * @param a_value the value to be pushed onto the stack
 * @return PQNode* 
 */
PQNode *stack_push(PQNode **stack, void *a_value);

/**
 * @brief Remove the top node from the stack located at stack and return it.
 * 
 * Hint: This function is very similar to pq_dequeue.
 * 
 * @param stack a pointer to the first node in the linked list
 * @return PQNode* 
 */
PQNode *stack_pop(PQNode **stack);

/**
 * @brief Deallocate all nodes in the linked list starting at a_head. 
 * 
 * @param a_head the head of the linked list
 */
void destroy_list(PQNode **a_head);

#endif // PRIORITY_QUEUE_H