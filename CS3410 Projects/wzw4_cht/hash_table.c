#include "hash_table.h"

cht *init_cht(const size_t num_buckets,
              size_t (*hash_function)(const int key,
                                      const size_t num_buckets)) {
  cht *ht = malloc(sizeof(cht));
  ht->hash_function = hash_function;
  ht->num_buckets = num_buckets;
  ht->buckets = malloc(sizeof(cht_bucket *) * num_buckets);
  for (int i = 0; i < num_buckets; i++) {
    ht->buckets[i] = malloc(sizeof(cht_bucket));
    ht->buckets[i]->head = NULL;
    ht->buckets[i]->bucket_lock = init_rwl();
  }
  return ht;
}

void cht_insert(cht *ht, const int key, const int value) {
  size_t ind = ht->hash_function(key, ht->num_buckets);
  cht_bucket *bucket = ht->buckets[ind];

  start_write(bucket->bucket_lock);
  cht_node *node = malloc(sizeof(cht_node));
  node->key = key;
  node->value = value;
  node->next = bucket->head;
  bucket->head = node;
  end_write(bucket->bucket_lock);
}

int cht_get(cht *ht, const int key) {
  size_t ind = ht->hash_function(key, ht->num_buckets);
  cht_bucket *bucket = ht->buckets[ind];

  start_read(bucket->bucket_lock);

  cht_node *cur = bucket->head;
  while (cur != NULL) {
    if (cur->key == key) {
      int value = cur->value;
      end_read(bucket->bucket_lock);
      return value;
    }
    cur = cur->next;
  }
  end_read(bucket->bucket_lock);
  return INT_MIN;
}

void cht_delete(cht *ht, const int key) {

  size_t ind = ht->hash_function(key, ht->num_buckets);
  cht_bucket *bucket = ht->buckets[ind];
  start_write(bucket->bucket_lock);
  cht_node *curr = bucket->head;
  cht_node *prev = NULL;
  while (curr != NULL) {
    if (curr->key == key) {
      if (prev == NULL) {
        bucket->head = curr->next;
      } else {
        prev->next = curr->next;
      }
      free(curr);
      break;
    }
    prev = curr;
    curr = curr->next;
  }
  end_write(bucket->bucket_lock);
}

cht_request_queue *init_cht_request_queue(void) {
  cht_request_queue *requests = malloc(sizeof(cht_request_queue));
  requests->head = NULL;
  requests->tail = NULL;
  return requests;
}

void enqueue_cht_request(cht_request_queue *requests, cht_request request) {
  cht_request_node *new_node = malloc(sizeof(cht_request_node));
  new_node->request = request;
  new_node->next = NULL;

  if (requests->tail == NULL) {
    requests->head = requests->tail = new_node;
  } else {
    requests->tail->next = new_node;
    requests->tail = new_node;
  }
}

cht_request dequeue_cht_request(cht_request_queue *requests) {
  if (requests->head == NULL) {
    fprintf(stderr,
            "Attempted to dequeue from empty queue.\nExiting\nCaller must "
            "check that queue is not empty.\n");
    exit(1);
  }

  cht_request_node *old_head = requests->head;
  cht_request request = old_head->request;

  requests->head = old_head->next;

  if (requests->head == NULL) {
    requests->tail = NULL;
  }

  free(old_head);
  return request;
}

void *thread_cht_requests(void *arg) {
  cht_thread_arg *t_arg = (cht_thread_arg *)arg;
  cht *ht = t_arg->ht;
  cht_request_queue *queue = t_arg->requests;
  int get_count = 0;
  cht_request_node *curr_node = queue->head;
  while (curr_node != NULL) {
    if (curr_node->request.op == CHT_GET) {
      get_count++;
    }
    curr_node = curr_node->next;
  }

  if (get_count > 0) {
    t_arg->results = malloc(sizeof(int) * get_count);
    t_arg->result_count = get_count;
  } else {
    t_arg->results = NULL;
    t_arg->result_count = 0;
  }

  int result_ind = 0;

  while (queue->head != NULL) {
    cht_request req = dequeue_cht_request(queue);

    switch (req.op) {
    case CHT_INSERT:
      cht_insert(ht, req.key, req.value);
      break;
    case CHT_DELETE:
      cht_delete(ht, req.key);
      break;
    case CHT_GET: {
      int val = cht_get(ht, req.key);
      if (t_arg->results != NULL) {
        t_arg->results[result_ind++] = val;
      }
      break;
    }
    default:
      fprintf(stderr, "Unknown CHT operation\n");
      break;
    }
  }
  barrier_wait(t_arg->global_barrier);
  return NULL;
}
