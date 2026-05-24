#include "huffman.h"
#include "cu_unit.h"
#include <stdio.h>
#include <stdlib.h>

static bool verify_weights(TreeNode *root)
{
  if (root == NULL)
    return true;

  // If it's a leaf node, we don't need to check further
  if (root->left == NULL && root->right == NULL)
  {
    return true;
  }

  // If it's an internal node, check its frequency
  int left_freq = root->left ? root->left->frequency : 0;
  int right_freq = root->right ? root->right->frequency : 0;

  // Check if current node's frequency equals the sum of its children's frequencies
  if (root->frequency != left_freq + right_freq)
  {
    return false; // Invalid if the frequencies don't match
  }

  // Recursively verify the left and right subtrees
  return verify_weights(root->left) && verify_weights(root->right);
}

static int count_nodes(TreeNode *root)
{
  if (root == NULL)
    return 0;
  return 1 + count_nodes(root->left) + count_nodes(root->right);
}

static int get_leaf_frequencies(TreeNode *root)
{
  if (root == NULL)
    return 0;
  if (root->left == NULL && root->right == NULL)
    return root->frequency;
  return get_leaf_frequencies(root->left) + get_leaf_frequencies(root->right);
}

static int get_num_characters(const char *path)
{
  FILE *stream = fopen(path, "r");
  int character_count = 0;
  for (char ch = fgetc(stream); !feof(stream); ch = fgetc(stream))
  {
    character_count++;
  }
  fclose(stream);
  return character_count;
}

static int get_num_distinct_characters(Frequencies freq)
{
  int count = 0;
  for (int i = 0; i < 256; i++)
  {
    if (freq[i] > 0)
    {
      count++;
    }
  }
  return count;
}

static int _test_huffman_tree_cornell()
{
  cu_start();
  // -------------------------------
  Frequencies freq = {0};
  const char *error = NULL;
  cu_check(calc_frequencies(freq, "tests/cornell.txt", &error));
  TreeNode *root = make_huffman_tree(freq);
  cu_check(verify_weights(root));
  cu_check(count_nodes(root) == get_num_distinct_characters(freq) * 2 - 1);
  cu_check(get_leaf_frequencies(root) == get_num_characters("tests/cornell.txt"));
  destroy_huffman_tree(&root);
  // -------------------------------
  cu_end();
}

static int _test_huffman_tree_gophers()
{
  cu_start();
  // -------------------------------
  Frequencies freq = {0};
  const char *error = NULL;
  cu_check(calc_frequencies(freq, "tests/gophers.txt", &error));
  TreeNode *root = make_huffman_tree(freq);
  cu_check(verify_weights(root));
  cu_check(count_nodes(root) == get_num_distinct_characters(freq) * 2 - 1);
  cu_check(get_leaf_frequencies(root) == get_num_characters("tests/gophers.txt"));
  destroy_huffman_tree(&root);
  // -------------------------------
  cu_end();
}

static int _test_huffman_tree_ex()
{
  cu_start();
  // -------------------------------
  Frequencies freq = {0};
  const char *error = NULL;
  cu_check(calc_frequencies(freq, "tests/ex.txt", &error));
  TreeNode *root = make_huffman_tree(freq);
  cu_check(verify_weights(root));
  cu_check(count_nodes(root) == get_num_distinct_characters(freq) * 2 - 1);
  cu_check(get_leaf_frequencies(root) == get_num_characters("tests/ex.txt"));
  destroy_huffman_tree(&root);
  // -------------------------------
  cu_end();
}

int main(int argc, char *argv[])
{
  cu_start_tests();
  cu_run(_test_huffman_tree_cornell);
  cu_run(_test_huffman_tree_gophers);
  cu_run(_test_huffman_tree_ex);
  cu_end_tests();
  return 0;
}
