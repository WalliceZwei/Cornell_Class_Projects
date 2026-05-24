#include <math.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

#include "cache.h"
#include "print_helpers.h"

cache_t *make_cache(int capacity, int block_size, int assoc) {
  cache_t *cache = malloc(sizeof(cache_t));
  cache->stats = make_cache_stats();

  cache->capacity = capacity;     // in Bytes
  cache->block_size = block_size; // in Bytes
  cache->assoc = assoc;           // 1, 2, 3... etc.

  // FIX THIS CODE!
  // first, correctly set these 5 variables. THEY ARE ALL WRONG
  // note: you may find math.h's log2 function useful
  cache->n_cache_line = capacity / block_size;
  cache->n_set = (capacity / block_size) / assoc;
  cache->n_offset_bit = (int)log2(block_size);
  cache->n_index_bit = (int)log2(cache->n_set);
  cache->n_tag_bit = 32 - (cache->n_offset_bit) - (cache->n_index_bit);

  // next create the cache lines and the array of LRU bits
  // - malloc an array with n_rows
  // - for each element in the array, malloc another array with n_col
  // FIX THIS CODE!

  cache->lines = malloc(cache->n_set * sizeof(cache_line_t *));
  cache->lru_way = malloc(cache->n_set * sizeof(int));

  // initializes cache tags to 0, dirty bits to false,
  // is_valid to false, and LRU bits to 0
  // FIX THIS CODE!
  for (int i = 0; i < cache->n_set; i++) {
    cache->lines[i] = malloc(cache->assoc * sizeof(cache_line_t));
    cache->lru_way[i] = 0;
    for (int j = 0; j < cache->assoc; j++) {
      cache->lines[i][j].tag = 0;
      cache->lines[i][j].dirty_f = false;
      cache->lines[i][j].is_valid = false;
    }
  }
  return cache;
}

/* Given a configured cache, returns the tag portion of the given address.
 *
 * Example: a cache with 4 bits each in tag, index, offset
 * in binary -- get_cache_tag(0b111101010001) returns 0b1111
 * in decimal -- get_cache_tag(3921) returns 15
 */
unsigned long get_cache_tag(cache_t *cache, unsigned long addr) {
  // FIX THIS CODE!
  return addr >> (cache->n_index_bit + cache->n_offset_bit);
}

/* Given a configured cache, returns the index portion of the given address.
 *
 * Example: a cache with 4 bits each in tag, index, offset
 * in binary -- get_cache_index(0b111101010001) returns 0b0101
 * in decimal -- get_cache_index(3921) returns 5
 */
unsigned long get_cache_index(cache_t *cache, unsigned long addr) {
  addr >>= cache->n_offset_bit;
  // Mask to keep only the index bits.
  unsigned long m = (1UL << cache->n_index_bit) - 1;
  return addr & m;
}

/* Given a configured cache, returns the given address with the offset bits
 * zeroed out.
 *
 * Example: a cache with 4 bits each in tag, index, offset
 * in binary -- get_cache_block_addr(0b111101010001) returns 0b111101010000
 * in decimal -- get_cache_block_addr(3921) returns 3920
 */
unsigned long get_cache_block_addr(cache_t *cache, unsigned long addr) {
  // FIX THIS CODE!
  unsigned long m = ~((1UL << cache->n_offset_bit) - 1);
  return addr & m;
}

/* this method takes a cache, an address, and an action
 * it proceses the cache access. functionality in no particular order:
 *   - look up the address in the cache, determine if hit or miss
 *   - update the LRU_way, cacheTags, is_valid, dirty flags if necessary
 *   - update the cache statistics (call update_stats)
 * return true if there was a hit, false if there was a miss
 * Use the "get" helper functions above. They make your life easier.
 */

bool access_cache(cache_t *cache, unsigned long addr, enum action_t action) {

  // FIX THIS CODE!
  // cache hit returns true

  unsigned long ind = get_cache_index(cache, addr);
  unsigned long tag = get_cache_tag(cache, addr);

  for (int way = 0; way < cache->assoc; way++) {
    cache_line_t *line = &cache->lines[ind][way];
    if (line->is_valid && line->tag == tag) {
      // hit
      if (action == STORE) {
        line->dirty_f = true;
      }
      cache->lru_way[ind] = (way + 1) % cache->assoc;
      update_stats(cache->stats, HIT, false, action);
      return true;
    }
  }

  int r_way = cache->lru_way[ind];
  cache_line_t *victim = &cache->lines[ind][r_way];
  bool write = false;
  if (victim->is_valid && victim->dirty_f) {
    write = true;
  }
  victim->tag = tag;
  victim->is_valid = true;
  victim->dirty_f = (action == STORE);
  cache->lru_way[ind] = (r_way + 1) % cache->assoc;
  update_stats(cache->stats, MISS, write, action);
  return false;
}