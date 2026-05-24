#include "huffman.h"
#include "memcheck.h"

// TODO: Task 1
bool calc_frequencies(Frequencies freqs, const char *path,
                      const char **a_error) {
  FILE *stream = fopen(path, "r");
  if (stream == NULL) {
    *a_error = strerror(errno);
    return false;
  } else {
    int ch;
    while ((ch = fgetc(stream)) != EOF) {
      freqs[(unsigned char)ch]++;
    }
    fclose(stream);
    return true;
  }
}

static int _cmp_node(const void *a, const void *b) {
  TreeNode *node_a = (TreeNode *)a;
  TreeNode *node_b = (TreeNode *)b;
  if (node_a->frequency < node_b->frequency)
    return -1;
  if (node_a->frequency > node_b->frequency)
    return 1;
  return 0;
}

// TODO: Task 1
TreeNode *make_huffman_tree(Frequencies freq) {
  PQNode *pq = NULL;
  size_t total_freq = 0;

  for (int i = 0; i < 256; i++) {
    if (freq[i] > 0) {
      total_freq++;
      TreeNode *node = my_malloc(sizeof(TreeNode));
      node->left = NULL;
      node->right = NULL;
      node->frequency = (size_t)freq[i];
      node->character = (uchar)i;
      pq_enqueue(&pq, node, _cmp_node);
    }
  }

  for (size_t i = 0; i < (total_freq - 1); i++) {
    PQNode *pqnode1 = pq_dequeue(&pq);
    TreeNode *node1 = pqnode1->a_value;
    my_free(pqnode1);
    PQNode *pqnode2 = pq_dequeue(&pq);
    TreeNode *node2 = pqnode2->a_value;
    my_free(pqnode2);
    TreeNode *newnode = my_malloc(sizeof(TreeNode));
    newnode->left = node1;
    newnode->right = node2;
    newnode->frequency = ((node1->frequency) + (node2->frequency));
    pq_enqueue(&pq, newnode, _cmp_node);
  }

  PQNode *pqnode = pq_dequeue(&pq);
  if (pqnode == NULL) {
    return NULL;
  }
  TreeNode *node = pqnode->a_value;
  my_free(pqnode);
  destroy_list(&pq);
  return node;
}

// TODO: Task 1
void destroy_huffman_tree(TreeNode **a_root) {
  if (*a_root == NULL) {
    return;
  }
  destroy_huffman_tree(&(*a_root)->left);
  destroy_huffman_tree(&(*a_root)->right);
  my_free(*a_root);
  *a_root = NULL;
}

// TODO: Task 2
void write_coding_table(TreeNode *root, BitWriter *a_writer) {
  if ((root->left) != NULL) {
    write_coding_table((root->left), a_writer);
  }
  if ((root->right) != NULL) {
    write_coding_table((root->right), a_writer);
  }
  if (((root->left) == NULL) && ((root->right) == NULL)) {
    write_bits(a_writer, 1, 1);
    write_bits(a_writer, root->character, 8);
  } else {
    write_bits(a_writer, 0, 1);
  }
}

void embedder(TreeNode *root, size_t *num_arr, size_t *length_arr,
              size_t current_num, size_t current_length) {
  if (root == NULL) {
    return;
  }
  if (root->left == NULL && root->right == NULL) {
    num_arr[root->character] = current_num;
    length_arr[root->character] = current_length;
    return;
  }
  if (root->left != NULL) {
    embedder(root->left, num_arr, length_arr, (current_num << 1),
             current_length + 1);
  }

  if (root->right != NULL) {
    embedder(root->right, num_arr, length_arr, (current_num << 1) | 1,
             current_length + 1);
  }
}

// TODO: Task 2
void write_compressed(BitWriter *a_writer, uint8_t *uncompressed_bytes,
                      TreeNode *root) {

  size_t embeddings_num[256];
  size_t embeddings_length[256];
  memset(embeddings_num, 0, sizeof(embeddings_num));
  memset(embeddings_length, 0, sizeof(embeddings_length));

  embedder(root, embeddings_num, embeddings_length, 0, 0);

  for (size_t i = 0; uncompressed_bytes[i] != '\0'; i++) {
    size_t new_num = embeddings_num[uncompressed_bytes[i]];
    size_t new_len = embeddings_length[uncompressed_bytes[i]];
    while (new_len > 0) {
      size_t chunk_len = new_len > 8 ? 8 : new_len;
      size_t shift = new_len - chunk_len;
      size_t chunk = (new_num >> shift) & ((1 << chunk_len) - 1);

      write_bits(a_writer, chunk, chunk_len);

      new_len -= chunk_len;
      new_num &= (1 << shift) - 1;
    }
  }
}
