import sys

def decode_coding_table_bits(file_path):
    """
    Decodes the binary content of a Huffman coding table bits file.
    The format is: 1 bit for type (1=leaf, 0=internal), then 8 bits for character if a leaf.
    """
    try:
        with open(file_path, 'rb') as f:
            content = f.read()
    except FileNotFoundError:
        print(f"Error: The file '{file_path}' was not found.")
        sys.exit(1)

    output = ""
    byte_index = 0
    bit_index = 0

    while byte_index < len(content):
        # Read the next bit
        current_byte = content[byte_index]
        is_leaf = (current_byte >> (7 - bit_index)) & 1

        output += str(is_leaf)
        bit_index += 1

        if is_leaf == 1:
            # If it's a leaf, read the next 8 bits for the character
            character_bits = ""
            char_code = 0
            
            for i in range(8):
                if bit_index == 8:
                    byte_index += 1
                    bit_index = 0
                    if byte_index >= len(content):
                        break # End of file
                    current_byte = content[byte_index]

                bit = (current_byte >> (7 - bit_index)) & 1
                char_code = (char_code << 1) | bit
                character_bits += str(bit)
                bit_index += 1

            if character_bits:
                output += chr(char_code)
        
        # Move to the next byte if all bits have been processed
        if bit_index == 8:
            byte_index += 1
            bit_index = 0

    print(output)

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python3 decode_bits.py <path_to_coding_table.bits>")
    else:
        decode_coding_table_bits(sys.argv[1])