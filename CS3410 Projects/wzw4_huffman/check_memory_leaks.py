import re
import sys
import os

def check_memory_leaks(log_file_path):
    """
    Scans a memory check log file for memory leaks.
    """
    malloc_addresses = {}
    free_addresses = set()

    malloc_pattern = re.compile(r'malloc\(\d+\) = (0x[0-9a-fA-F]+) at (.*)')
    free_pattern = re.compile(r'free\((0x[0-9a-fA-F]+)\) at (.*)')

    try:
        with open(log_file_path, 'r') as f:
            for line in f:
                line = line.strip()

                malloc_match = malloc_pattern.match(line)
                if malloc_match:
                    address = malloc_match.group(1)
                    location = malloc_match.group(2)
                    malloc_addresses[address] = location
                    continue

                free_match = free_pattern.match(line)
                if free_match:
                    address = free_match.group(1)
                    free_addresses.add(address)
    except FileNotFoundError:
        print(f"Error: The file '{log_file_path}' was not found.")
        return False, 0, 0, 0

    leaks_found = 0
    print(f"--- Memory Leak Report for '{log_file_path}' ---")

    # Check for un-freed allocations
    for address, location in malloc_addresses.items():
        if address not in free_addresses:
            print(f"LEAK: Address {address} was not freed. Allocated at {location}")
            leaks_found += 1

    if leaks_found == 0:
        print("✅ Congratulations! No memory leaks detected.")
    else:
        print(f"❗ {leaks_found} memory leak(s) detected.")

    print("--- End of Report ---")
    return True, leaks_found, len(malloc_addresses), len(free_addresses)


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: python3 check_leaks.py <log_file_name> [-delete]")
        sys.exit(1)

    log_file_name = sys.argv[1]
    delete_flag = '-delete' in sys.argv

    success, leaks, mallocs, frees = check_memory_leaks(log_file_name)

    if success and delete_flag:
        try:
            os.remove(log_file_name)
            print(f"\nSuccessfully deleted log file: {log_file_name}")
        except OSError as e:
            print(f"\nError: Could not delete {log_file_name} - {e.strerror}")