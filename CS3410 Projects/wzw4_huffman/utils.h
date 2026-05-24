#ifndef UTILS_H
#define UTILS_H

#include "priority_queue.h"
#include "huffman.h"

/**
 * @brief Utility function to print a list of elements.
 * 
 * @param head the address of the first node in the linked list
 * @param print_one_element_fn the address of a function that prints 
 * a single element in the linked list
 */
void print_list(PQNode *head, void (*print_one_element_fn)(void *));

/**
 * @brief Utility function to print a single tree node. 
 * 
 * @param a_node the address of the tree node to print
 */
void _print_tree_node(void *a_node);

#endif // UTILS_H