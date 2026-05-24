#ifndef BIT_TOOLS_H
#define BIT_TOOLS_H

#include <stdio.h>
#include <stdint.h>

/**
 * A struct representing a bit writer. The bit writer writes bits to a file.
 * The bit writer writes bits to the file in the order they are written.
 */
typedef struct _BitWriter
{
  FILE *file;
  uint8_t current_byte;
  uint8_t num_bits_left;
} BitWriter;

/**
 * @brief Open a file at `path` and return a BitWriter that writes to the file.
 *
 * @param path the path to the file to write to
 * @return BitWriter
 */
BitWriter open_bit_writer(const char *path);

/**
 * @brief Write the least significant num_bits_to_write bits of bits to the file.
 *
 * @param a_writer the address of the BitWriter object, the .file
 * feld of which is already opened for writing
 * @param bits the bits to write
 * @param num_bits_to_write the number of bits to write (always between 0 and 8)
 */
void write_bits(BitWriter *a_writer, uint8_t bits, uint8_t num_bits_to_write);

/**
 * @brief Write the current byte to the file.
 *
 * @param a_writer the address of the BitWriter object, the .file
 * feld of which is already opened for writing
 */
void flush_bit_writer(BitWriter *a_writer);

/**
 * @brief Close the given BitWriter and reset its fields.
 *
 * @param a_writer the address of the BitWriter object to close
 */
void close_bit_writer(BitWriter *a_writer);

/**
 * A struct representing a bit reader. The bit reader reads bits from a file.
 *
 */
typedef struct _BitReader
{
  FILE *file;
  uint8_t current_byte;
  int8_t current_bit;
} BitReader;

/**
 * @brief Open a file at `path` and return a BitReader that reads from the file.
 * 
 * @param path 
 * @return BitReader 
 */
BitReader open_bit_reader(const char *path);

/**
 * @brief Read a single bit from the file.
 * 
 * @param a_reader the address of the BitReader object, the .file of which is 
 * already opened for reading
 * @return uint8_t 
 */
uint8_t read_bit(BitReader *a_reader);

/**
 * @brief Read multiple bits from the file. 
 * 
 * @param a_reader the address of the BitReader object, the .file of which is
 * already opened for reading
 * @param num_bits_to_read the number of bits to read
 * @return uint8_t 
 */
uint8_t read_bits(BitReader *a_reader, uint8_t num_bits_to_read);

/**
 * @brief Close the given BitReader and reset its fields.
 * 
 * @param a_reader the address of the BitReader object to close
 */
void close_bit_reader(BitReader *a_reader);

#endif // BIT_TOOLS_H