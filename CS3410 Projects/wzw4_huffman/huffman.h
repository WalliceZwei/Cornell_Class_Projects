#ifndef HUFFMAN_H
#define HUFFMAN_H

#include "priority_queue.h"
#include "bit_tools.h"

#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <inttypes.h>
#include <errno.h>
#include <stdbool.h>

// Defines the type uchar as unsigned char
typedef unsigned char uchar;

/*
 * Defines the type Frequencies as an array of 256 integers, each of which
 * is of type uint64_t
 */
typedef uint64_t Frequencies[256];

/**
 * Opens a file at `path` and either stores the character frequencies in
 * `freqs` or sets *a_error to `strerror(errno)` if the file could not be
 * opened.
 *
 * @param freqs an array of 256 integers. Caller is responsible for initializing
 * freqs[ch] to 0 for all ch in [0, 255].
 * @param path the path to the file to read
 * @param a_error a pointer to a string that will be set to an error message
 *
 * @return bool
 */
bool calc_frequencies(Frequencies freqs, const char *path, const char **a_error);

/**
 * A struct representing a node in a Huffman tree. Each node has a character,
 * a frequency, and pointers to its left and right children.
 */
typedef struct _TreeNode
{
  uchar character;
  size_t frequency;
  struct _TreeNode *left;
  struct _TreeNode *right;
} TreeNode;

/**
 * Constructs a Huffman tree from the character frequencies in `freq`.
 *
 * @param freq an array of 256 integers representing the character frequencies
 *
 * @return TreeNode* a pointer to the root of the Huffman tree
 */
TreeNode *make_huffman_tree(Frequencies freq);

/**
 * @brief Destroy a Huffman tree that was created using make_huffman_tree(...).
 *
 * @param a_root the root of the Huffman tree to destroy. This function should
 * set `a_root` to NULL after freeing all memory associated with the tree.
 */
void destroy_huffman_tree(TreeNode **a_root);

/**
 * @brief Write the coding table represented in the Huffman tree to the file
 * referenced by a_writer.
 *
 * @param root the root of the Huffman tree to encode
 * @param a_writer a pointer to the BitWriter struct that contains the
 * file to write the coding table to
 */
void write_coding_table(TreeNode *root, BitWriter *a_writer);

/**
 * @brief Compress and write the bytes in `uncompressed_bytes` to the file using
 * the Huffman tree at root and the BitWriter at a_writer.
 *
 * @param a_writer a pointer to the BitWriter struct that contains the file
 * to write the compressed data to
 * @param uncompressed_bytes the uncompressed text that needs to be compressed
 * and written to the file
 * @param root the root of the Huffman tree to use for compression
 */
void write_compressed(BitWriter *a_writer, uint8_t *uncompressed_bytes, TreeNode *root);

#endif // HUFFMAN_H