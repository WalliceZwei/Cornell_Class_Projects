package a6;

/**
 * A priority queue of items of type E with a priority P
 * that supports efficiently changing the priority of items.
 * <p>
 * Duplicate and null items are not allowed.
 *
 * @param <E> The type of item stored in the priority queue
 * @param <P> The type of priorities for items
 */
interface AdjustablePriorityQueue<E, P> {
    /**
     * Effect: returns the number of items stored in this priority queue.
     * <p>
     * Runs in O(1) time.
     */
    int size();

    /**
     * Effect: inserts a new item into the queue with a designated priority.
     * Requires: Queue does not contain this item.
     * <p>
     * Runs in expected O(log n) time, where n is the size of this priority queue.
     *
     * @param item     new item to add
     * @param priority priority of the new item
     */
    void insert(E item, P priority);

    /**
     * Effect: returns the item from the queue with the highest priority.
     * Requires: The queue contains at least one item.
     * <p>
     * Runs in O(1) time.
     */

    E peek();

    /**
     * Effect: Removes and returns the item from the queue with the highest priority.
     * Requires: The queue contains at least one item.
     * <p>
     * Runs in expected O(log n) time, where n is the size of this priority queue.
     */
    E poll();

    /**
     * Increases the priority of an item already present in the
     * priority queue, updating its priority in-place.
     * Requires: Queue contains this item with a priority at most as high as {@code priority}
     * <p>
     * Runs in expected O(log n) time, where n is the size of this priority queue.
     *
     * @param item     existing item whose priority should be changed
     * @param priority new priority of the item
     */
    void increasePriority(E item, P priority);
}
