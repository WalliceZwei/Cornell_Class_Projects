#include "huffman.h"
#include "memcheck.h"
#include <stdlib.h>
#include <stdio.h>

size_t list_size(PQNode *a_head)
{
  size_t size = 0;
  while (a_head != NULL)
  {
    size++;
    a_head = a_head->next;
  }
  return size;
}

TreeNode *reconstruct_huffman_tree(BitReader *a_reader)
{
  PQNode *stack = NULL;
  PQNode *allocated_stack_nodes = NULL;
  while (a_reader->file != NULL)
  {
    uint8_t bit = read_bit(a_reader);
    if (bit == 1) // Leaf node
    {
      uint8_t c = read_bits(a_reader, 8);
      TreeNode *new_tree_node = my_malloc(sizeof(*new_tree_node));
      *new_tree_node = (TreeNode){.character = (uchar)c, .frequency = 0, .left = NULL, .right = NULL};
      stack_push(&stack, new_tree_node);
    }
    else // Internal Node
    {
      if (list_size(stack) == 1)
      {
        PQNode *stack_node = stack_pop(&stack);
        TreeNode *huffman_tree = stack_node->a_value;
        destroy_list(&allocated_stack_nodes);
        my_free(stack_node);
        return huffman_tree;
      }
      PQNode *right = stack_pop(&stack);
      PQNode *left = stack_pop(&stack);
      TreeNode *right_tree_node = right->a_value;
      TreeNode *left_tree_node = left->a_value;
      TreeNode *new_tree_node = my_malloc(sizeof(*new_tree_node));
      *new_tree_node = (TreeNode){.character = '\0', .frequency = 0, .left = left_tree_node, .right = right_tree_node};
      stack_push(&stack, new_tree_node);
      stack_push(&allocated_stack_nodes, left);
      stack_push(&allocated_stack_nodes, right);
    }
  }

  return NULL;
}

void decompress(BitReader *a_reader, FILE *uncompressed, TreeNode *root)
{
  uint32_t num_uncompressed_bytes;
  fread(&num_uncompressed_bytes, sizeof(uint32_t), 1, a_reader->file);

  uint32_t num_bytes_written = 0;
  TreeNode *curr = root;

  while (num_bytes_written < num_uncompressed_bytes)
  {
    while (curr->left != NULL && curr->right != NULL)
    {
      uint8_t bit = read_bit(a_reader);
      if (bit == 0)
      {
        curr = curr->left;
      }
      else
      {
        curr = curr->right;
      }
    }
    fwrite(&(curr->character), sizeof(curr->character), 1, uncompressed);
    num_bytes_written++;
    curr = root;
  }
}

int main(int argc, char *argv[])
{
  if (argc != 4)
  {
    printf("Usage: %s <compressed_file> <coding_table_file> <uncompressed_filename>\n", argv[0]);
    return EXIT_FAILURE;
  }

  BitReader table_reader = open_bit_reader(argv[2]);
  TreeNode *reconstructed_root = reconstruct_huffman_tree(&table_reader);
  BitReader compressed_reader = open_bit_reader(argv[1]);
  FILE *uncompressed = fopen(argv[3], "w");
  decompress(&compressed_reader, uncompressed, reconstructed_root);
  destroy_huffman_tree(&reconstructed_root);
  close_bit_reader(&table_reader);
  fclose(uncompressed);

  return EXIT_SUCCESS;
}
