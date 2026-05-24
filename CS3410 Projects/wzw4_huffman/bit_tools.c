#include "bit_tools.h"
#include <assert.h>

BitWriter open_bit_writer(const char *path)
{
  return (BitWriter){.file = fopen(path, "wb"), .current_byte = 0, .num_bits_left = 8};
}

void write_bits(BitWriter *a_writer, uint8_t bits, uint8_t num_bits_to_write)
{
  assert(num_bits_to_write <= 8);
  assert(a_writer->num_bits_left >= 1 && a_writer->num_bits_left <= 8);

  if (a_writer->file != NULL)
  {
    if (num_bits_to_write <= a_writer->num_bits_left)
    {
      uint8_t mask = (1 << num_bits_to_write) - 1;
      bits &= mask;
      a_writer->current_byte |= bits << (a_writer->num_bits_left - num_bits_to_write);
      a_writer->num_bits_left -= num_bits_to_write;

      if (a_writer->num_bits_left == 0)
      {
        fwrite(&(a_writer->current_byte), sizeof(a_writer->current_byte), 1, a_writer->file);
        a_writer->current_byte = 0;
        a_writer->num_bits_left = 8;
      }
    }
    else
    {
      // Write the first a_writer->num_bits_left bits of bits
      uint8_t mask = (1 << a_writer->num_bits_left) - 1;
      uint8_t bits_to_write = (bits >> (num_bits_to_write - a_writer->num_bits_left)) & mask;
      a_writer->current_byte |= bits_to_write;
      fwrite(&(a_writer->current_byte), sizeof(a_writer->current_byte), 1, a_writer->file);
      a_writer->current_byte = 0;
      int num_bits_left = num_bits_to_write - a_writer->num_bits_left;
      a_writer->num_bits_left = 8;

      // Write the remaining num_bits_to_write - a_writer->num_bits_left bits of bits
      uint8_t leftover_mask = (1 << num_bits_left) - 1;
      bits &= leftover_mask;
      a_writer->current_byte |= bits << (a_writer->num_bits_left - num_bits_left);
      a_writer->num_bits_left -= num_bits_left;
    }
  }

  assert(a_writer->num_bits_left >= 1 && a_writer->num_bits_left <= 8);
}

void flush_bit_writer(BitWriter *a_writer)
{
  fwrite(&(a_writer->current_byte), sizeof(a_writer->current_byte), 1, a_writer->file);
  a_writer->current_byte = 0;
  a_writer->num_bits_left = 8;
}

void close_bit_writer(BitWriter *a_writer)
{
  flush_bit_writer(a_writer);
  fclose(a_writer->file);
  a_writer->file = NULL;
}

BitReader open_bit_reader(const char *path)
{
  return (BitReader){.file = fopen(path, "rb"), .current_byte = 0, .current_bit = -1};
}

uint8_t read_bit(BitReader *a_reader)
{
  if (a_reader->current_bit < 0)
  {
    int next_byte = fgetc(a_reader->file);
    if (next_byte == EOF)
    {
      fclose(a_reader->file);
      a_reader->file = NULL;
      return 0;
    }
    else
    {
      a_reader->current_byte = (uint8_t)next_byte;
      a_reader->current_bit = 7;
    }
  }

  uint8_t bit = (a_reader->current_byte >> a_reader->current_bit) & 1;
  a_reader->current_bit--;

  return bit;
}

uint8_t read_bits(BitReader *a_reader, uint8_t num_bits_to_read)
{
  uint8_t bits = 0;
  for (uint8_t i = 0; i < num_bits_to_read; i++)
  {
    bits = (bits << 1) | read_bit(a_reader);
  }

  return bits;
}

void close_bit_reader(BitReader *a_reader)
{
  if (a_reader->file != NULL)
  {
    fclose(a_reader->file);
    a_reader->file = NULL;
  }
}
