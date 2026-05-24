#include "huffman.h"
#include "memcheck.h"
#include "utils.h"
#include <stdint.h>
#include <inttypes.h>

uint32_t get_total_bytes(Frequencies freqs)
{
  uint32_t total_bytes = 0;
  for (size_t i = 0; i < 256; i++)
  {
    total_bytes += freqs[i];
  }
  return total_bytes;
}

uint8_t *read_file(const char *path)
{
  FILE *file = fopen(path, "r");
  if (file == NULL)
  {
    return NULL;
  }

  fseek(file, 0, SEEK_END);
  long file_size = ftell(file);
  fseek(file, 0, SEEK_SET);

  uint8_t *buffer = my_malloc(file_size + 1);
  fread(buffer, 1, file_size, file);
  buffer[file_size] = '\0';

  fclose(file);
  return buffer;
}

int main(int argc, char *argv[])
{
  if (argc != 2)
  {
    printf("Usage: %s <filename>\n", argv[0]);
    return EXIT_FAILURE;
  }

  Frequencies freq = {0};
  const char *error = NULL;
  const char *filename = argv[1];
  uchar *uncompressed_bytes = read_file(filename);

  if (calc_frequencies(freq, filename, &error))
  {
    uint32_t total_bytes = get_total_bytes(freq);
    TreeNode *root = make_huffman_tree(freq);
    BitWriter compressed_writer = open_bit_writer("compressed.bits");
    fwrite(&total_bytes, sizeof(uint32_t), 1, compressed_writer.file);
    write_compressed(&compressed_writer, uncompressed_bytes, root);
    BitWriter coding_table_writer = open_bit_writer("coding_table.bits");
    write_coding_table(root, &coding_table_writer);
    close_bit_writer(&compressed_writer);
    close_bit_writer(&coding_table_writer);
    destroy_huffman_tree(&root);
  }
  else if (error != NULL)
  {
    printf("Error: %s\n", error);
    return EXIT_FAILURE;
  }
  my_free(uncompressed_bytes);

  return EXIT_SUCCESS;
}