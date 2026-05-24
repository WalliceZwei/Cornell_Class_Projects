package util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;



/**
 * This hash table uses linked lists for collision handling
 * and dynamically resizes. Keys are unique, and collisions
 * are handled with chaining.
 *
 * Class Invariants:
 * - Keys are unique.
 * - Objects with equal state produce the same hashCode.
 * - The table size dynamically increases when the load factor threshold is exceeded.
 */
public class HashTable<K, V> implements Map<K, V> {
    private LinkedList<K,V>[] hashtablearr;
    private final double loadFactor = 0.75;
    private int hashTableSize;
    private int numElements = 0;
    private int emptyBuckets;
    private K hashKey;
    private int collisions = 0;

    /**
     * Returns the number of empty buckets in the hash table.
     *
     * @return the number of empty buckets
     */
    public int getEmptyBuckets() {
        return emptyBuckets;
    }

    /**
     * Returns the number of collisions encountered during insertions.
     *
     * @return the number of collisions
     */
    public int getCollisions() {
        return collisions;
    }

    /**
     * Returns the current load factor of the hash table.
     *
     * @return the load factor
     */
    public double getLoadFactor() {
        return loadFactor;
    }

    /**
     * Constructs a new hash table with the specified initial capacity.
     *
     * @param tableSize the initial size of the hash table
     * @throws IllegalArgumentException if tableSize is less than 1
     */
    public HashTable(int tableSize) throws IllegalArgumentException{
        if (tableSize < 1){
            throw new IllegalArgumentException("Hash Table has to have size above 0");
        }
        this.hashTableSize = tableSize;
        this.emptyBuckets = tableSize;
        hashtablearr = (LinkedList<K,V>[]) new LinkedList[this.hashTableSize];
    }

    /**
     * Resizes the hash table by doubling its size and rehashing all elements.
     */
    private void resizing(){
        int oldHashTableSize = hashTableSize;
        hashTableSize <<=1;
        emptyBuckets = hashTableSize;
        LinkedList<K,V>[] hasharr = (LinkedList<K,V>[]) new LinkedList[hashTableSize];

        for(int x = 0; x<(oldHashTableSize);x++){
            if (hashtablearr[x] != null) {
                hashtablearr[x].resetTracker();
                for (int j = 0; j < hashtablearr[x].size(); j++) {
                    hashKey = hashtablearr[x].keyStepper();
                    int z = hashCode();
                    if (hasharr[z] == null) {
                        hasharr[z] = new LinkedList<>();
                        emptyBuckets--;
                    }
                    hashTableSize >>= 1;
                    hasharr[z].prepend(hashKey, get(hashKey));
                    hashTableSize <<= 1;
                }
            }
        }
        hashtablearr = hasharr;
    }

    /**
     * Returns the FNV1 hash code for the given key, if it is not null.
     *
     * @return the hash code of the key
     * @throws IllegalArgumentException if the key is null
     */
    @Override
    public int hashCode() throws IllegalArgumentException {
        if (hashKey!=null){
            int offset = 0x811c9dc5;
            int prime = 0x01000193;
            int hash = offset;
            String h = hashKey.toString();
            for(int x = 0; x<h.length();x++){
                hash^=h.charAt(x);
                hash *= prime;
            }
            return (hash% hashTableSize + hashTableSize)% hashTableSize;
        }
        throw new IllegalArgumentException("No hash key given to hash");
    }

    /**
     * Returns the number of key-value pairs in the hash table.
     *
     * @return the size of the hash table
     */
    @Override
    public int size() {
        return numElements;
    }

    /**
     * Checks if the hash table is empty.
     *
     * @return true if the hash table is empty
     */
    @Override
    public boolean isEmpty() {
        return numElements == 0;
    }

    /**
     * Checks if the hash table contains the specified key.
     *
     * @param key the key to check
     * @return true if the hash table contains the specified key
     */
    @Override
    public boolean containsKey(Object key) {
        hashKey = (K) key;
        if (hashtablearr[hashCode()] == null){return false;}
        return hashtablearr[hashCode()].containsKey(hashKey);
    }

    /**
     * Checks if the hash table contains the specified value.
     *
     * @param value the value to be checked
     * @return true if the hash table contains the specified value
     */
    @Override
    public boolean containsValue(Object value) {
        for(int x = 0; x < hashtablearr.length;x++){
            if (hashtablearr[x] != null) {
                if (hashtablearr[x].contains((V) value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns the value associated with the specified key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value associated with the key, or null if the key is not in the hashtable
     */
    @Override
    public V get(Object key) {
        hashKey = (K)key;
        if (hashtablearr[hashCode()] == null){return null;}
        return hashtablearr[hashCode()].getKeyValue(hashKey);
    }

    /**
     * Puts a key with a value into the hash table
     *
     * @param key the key to put
     * @param value the value to be associated with the key
     * @return the previous value associated with the key, or null if the key wasn't mapped before
     */
    @Override
    public V put(K key, V value) {
        if (containsKey(key)){
            return hashtablearr[hashCode()].changeKeyValue(key,value);
        }
        else {
            if (hashtablearr[hashCode()] == null) {
                hashtablearr[hashCode()] = new LinkedList<>();
                emptyBuckets--;
            }
            else{
                collisions++;
            }
            hashtablearr[hashCode()].prepend(key,value);
            numElements++;
            if (numElements >= loadFactor * hashTableSize){
                resizing();
            }
        }
        return null;
    }

    /**
     * Deletes key and value from hashtable
     *
     * @param key the key to remove
     * @return the previous value of the key, or null if the key was not in the table before
     */
    @Override
    public V remove(Object key) {
        if (containsKey(key)){
            numElements--;
            return hashtablearr[hashCode()].remove((K)key);
        }
        return null;
    }

    /**
     * Copies all mappings from a map to the hash table.
     *
     * @param m the map
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for(Object key : m.keySet().toArray()){
            put((K)key,m.get((K)key));
        }
    }

    /**
     * Removes all mappings from the hash table.
     */
    @Override
    public void clear() {
        numElements = 0;
        hashtablearr = (LinkedList<K,V>[]) new LinkedList[this.hashTableSize];
        emptyBuckets = this.hashTableSize;
    }

    /**
     * @return a new keyset object with the hash table keys
     */
    @Override
    public Set<K> keySet() {
        return new KeySetClass<>();
    }

    /*
     * You are not required to implement the values() or entrySet() operations.
     */

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }



    private class KeySetClass<K> implements Set <K>{

        // Class Invariants: all keys in the hash table are mapped to the keyset
        // All keys must be unique

        public int size(){
            return HashTable.this.size();
        }
        public boolean isEmpty(){
            return HashTable.this.size() == 0;
        }
        public boolean contains(Object xi){
            return containsKey(xi);
        }

        public Object[] toArray() {
            int tracker = 0;
            Object[] keySet = new Object[numElements];
            for(int x = 0; x<(hashTableSize); x++){
                if (hashtablearr[x]!=null){
                    hashtablearr[x].resetTracker();
                }
            }
            for(int x = 0; x<(hashTableSize); x++){ // halve the sizehashtable here to accoutn for original size
                // account for the fact that key might have been erased
                if (hashtablearr[x] != null) {
                    for (int j = 0; j < hashtablearr[x].size(); j++) {
                        hashKey = hashtablearr[x].keyStepper();
                        keySet[tracker] = hashKey;
                        tracker++;
                    }
                }
            }
            return keySet;
        }

        @Override
        public Iterator<K> iterator(){
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean add(K k) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(Collection<? extends K> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }
    }

}