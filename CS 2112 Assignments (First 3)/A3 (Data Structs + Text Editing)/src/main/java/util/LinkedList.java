package util;

/**
 * A node in the linked list that holds a key-value pair.
 *
 * @param <A> the type of the key
 * @param <B> the type of the value
 */
class Node<A, B>{
    A key;
    B value;
    Node<A, B> next;

    /**
     * Constructs a new node with the given key, value, and next node reference.
     *
     * @param key the key associated with this node
     * @param value the value associated with this node
     * @param next the next node in the list
     */
    Node(A key, B value, Node<A, B> next){
        this.key = key;

        this.value = value;
        this.next = next;
    }
}

/**
 * Linked list that stores key-value pairs.
 * The list supports appending, prepending, and searching for keys and values.
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 */
public class LinkedList <K,V> {

    // Class Invariants: K,V are subclasses of Object, Null next node indicates the end of the list, Tail Object has a Null next node
    // data is contained in the key and value, next is the next "linked" node, which can't just be itself
    // null is the default value

    private int sizelist = 0;
    private Node<K, V> head = null;
    private Node<K, V> tail = null;
    private Node<K, V> tracker1 = head;

    /**
     * Constructs an empty linked list.
     */
    public LinkedList() {
    }

    /**
     * Returns whether the linked list is empty.
     *
     * @return {@code true} if the list is empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return sizelist == 0;
    }

    /**
     * Returns the size of the linked list.
     *
     * @return the number of elements in the list
     */
    public int size() {
        return sizelist;
    }

    /**
     * Appends a new node with the specified key and value to the end of the list.
     *
     * @param key the key of the new node
     * @param value the value of the new node
     */
    public void append(K key, V value) {
        Node<K, V> newob = new Node<>(key, value, null);
        if (isEmpty()) {
            this.head = newob;
        } else {
            this.tail.next = newob;
        }
        this.tail = newob;
        sizelist++;
    }

    /**
     * Resets the internal tracker used for stepping through the keys in the list.
     */
    public void resetTracker() {
        tracker1 = head;
    }

    /**
     * Returns the next key in the list and advances the internal tracker.
     *
     * @return the next key in the list
     */
    public K keyStepper() {
        K key = tracker1.key;
        tracker1 = tracker1.next;
        return key;
    }

    /**
     * Prepends a new node with the specified key and value to the start of the list.
     *
     * @param key the key of the new node
     * @param value the value of the new node
     */
    public void prepend(K key, V value) {
        Node<K, V> h = new Node<>(key, value, head);
        head = h;
        if (isEmpty()) {
            tail = h;
        }
        sizelist++;
    }

    /**
     * Checks if the linked list contains a node with the specified value.
     *
     * @param valueRef the value to search for
     * @return {@code true} if the list contains the value, {@code false} otherwise
     */
    public boolean contains(V valueRef) {
        if (isEmpty()) {
            return false;
        }
        Node<K, V> tracker = head;
        while (tracker != null) {
            if (tracker.value.equals(valueRef)) {
                return true;
            }
            tracker = tracker.next;
        }
        return false;
    }

    /**
     * Checks if the linked list contains a node with the specified key.
     *
     * @param keyRef the key to search for
     * @return {@code true} if the list contains the key, {@code false} otherwise
     */
    public boolean containsKey(K keyRef) {
        if (keyRef == null) {
            return false;
        }
        Node<K, V> tracker = head;
        while (tracker != null) {
            if (tracker.key.equals(keyRef)) {
                return true;
            }
            tracker = tracker.next;
        }
        return false;
    }

    /**
     * Returns the value associated with the specified key.
     *
     * @param keyRef the key to search for
     * @return the value associated with the key, or {@code null} if the key is not found
     */
    public V getKeyValue(K keyRef) {
        if (isEmpty()) {
            return null;
        }
        Node<K, V> tracker = head;
        while (tracker != null) {
            if (tracker.key.equals(keyRef)) {
                return tracker.value;
            }
            tracker = tracker.next;
        }
        return null;
    }

    /**
     * Changes the value associated with the specified key.
     *
     * @param keyRef the key whose value is to be updated
     * @param value the new value to associate with the key
     * @return the updated value, or {@code null} if the key is not found
     */
    public V changeKeyValue(K keyRef, V value) {
        if (isEmpty()) {
            return null;
        }
        Node<K, V> tracker = head;
        while (tracker != null) {
            if (tracker.key.equals(keyRef)) {
                tracker.value = value;
                return value;
            }
            tracker = tracker.next;
        }
        return null;
    }

    /**
     * Removes the first node with the specified key from the list.
     *
     * @param key the key of the node to remove
     * @return the value of the removed node, or {@code null} if the key is not found
     */
    public V remove(K key) {
        if (!isEmpty()) {
            Node<K, V> tracker = head;
            Node<K, V> behindtracker = null;
            while (tracker != null) {
                if (tracker.key.equals(key)) {
                    if (tracker == head) {
                        head = tracker.next;
                        if (sizelist == 1) {
                            tail = null;
                        }
                    } else if (tracker == tail) {
                        tail = behindtracker;
                        tail.next = null;
                    } else {
                        behindtracker.next = tracker.next;
                    }
                    sizelist--;
                    return tracker.value;
                }
                behindtracker = tracker;
                tracker = tracker.next;
            }
        }
        return null;
    }
}