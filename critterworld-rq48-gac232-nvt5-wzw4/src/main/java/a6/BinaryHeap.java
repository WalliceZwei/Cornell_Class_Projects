package a6;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/** A binary heap of items with priorities.  */
public class BinaryHeap<E, P> implements AdjustablePriorityQueue<E, P> {
    private final Comparator<P> comparator;

    /** The array representation of this BinaryHeap.
     * <br>Higher priority are at the front of the list, lower priority are at the end.
     */
    private final ArrayList<HeapNode<E, P>> array;

    private final HashMap<E, Integer> nodeToIndex;

    /** Create a binary heap in which the items are ordered according to {@code
     * comparator}. The heap implements a min-queue: smaller values according
     * to the comparator are higher priorities.
     */
    public BinaryHeap(Comparator<P> comparator) {
        this.comparator = comparator;
        array = new ArrayList<>();
        nodeToIndex = new HashMap<>();
    }

    //O(1) - size() of ArrayList is O(1) time
    @Override
    public int size() {
        return array.size();
    }

    //O(lgn) - insert item to end of array list in O(1) time and then bubbling up takes at most O(lgn)
    @Override
    public void insert(E item, P priority) {
        //dont insert if its null,
        // also shouldn't insert if its duplicate but that's a precondition so no need to check
        if (item == null || priority == null) return;
        assert classInv();

        array.add(new HeapNode<>(item, priority));
        nodeToIndex.put(array.getLast().element, array.size() - 1);
        bubbleUp(array.size() - 1);
        assert classInv();
    }

    //O(1) - gets the first item in the array list
    @Override
    public E peek() {
        return array.getFirst().element;
    }

    //O(lgn) - removes the first element in the arrayList and replace with the last in O(1) time
            // and then bubbling down takes at most O(lgn)
    @Override
    public E poll() {
        if (array.size() == 1) {
            nodeToIndex.remove(array.getFirst().element);
            return array.removeFirst().element;
        }
        assert classInv();

        HeapNode<E, P> highestPriority = array.getFirst();
        array.set(0, array.getLast());
        array.removeLast();

        nodeToIndex.replace(array.getFirst().element, 0);
        nodeToIndex.remove(highestPriority.element);

        bubbleDown(0);

        assert classInv();

        return highestPriority.element;
    }

    //O(lgn) - O(1) to find element in arrayList with hash map and then bubbling up takes at most O(lgn)
    @Override
    public void increasePriority(E item, P priority) {
        // precondition states item is already present somewhere in array
        if (item == null || priority == null) return;
        assert classInv();
        int index = nodeToIndex.get(item);

        array.get(index).priority = priority;
        bubbleUp(index);
        assert classInv();
    }

    public boolean isEmpty() {
        return array.isEmpty();
    }

    /** Follows the bubble up procedure at {@code index} to re-establish the order invariant */
    private void bubbleUp(int index) {
        int parentIndex = Math.floorDiv(index - 1, 2);
        while (index != 0 && comparator.compare(array.get(index).priority, array.get(parentIndex).priority) > 0) {
            //swap with parent
            nodeToIndex.replace(array.get(parentIndex).element, index);
            nodeToIndex.replace(array.get(index).element, parentIndex);

            HeapNode<E, P> temp = array.get(index);
            array.set(index, array.get(parentIndex));
            array.set(parentIndex, temp);

            index = parentIndex;
            parentIndex = Math.floorDiv(index - 1, 2);
        }
    }

    /** Follows the bubble down procedure at {@code index} to re-establish the order invariant */
    private void bubbleDown(int index) {
        while (true) {
            int leftIndex = 2 * index + 1, rightIndex = 2 * index + 2;
            boolean leftValid = leftIndex < array.size() && comparator.compare(array.get(leftIndex).priority, array.get(index).priority) > 0,
                    rightValid = rightIndex < array.size() && comparator.compare(array.get(rightIndex).priority, array.get(index).priority) > 0;

            //left is valid and right either doesn't exist or has lower priority than right
            if (leftValid && (!rightValid || comparator.compare(array.get(leftIndex).priority, array.get(rightIndex).priority) >= 0)) {
                //swap with left
                nodeToIndex.replace(array.get(leftIndex).element, index);
                nodeToIndex.replace(array.get(index).element, leftIndex);

                HeapNode<E, P> temp = array.get(index);
                array.set(index, array.get(leftIndex));
                array.set(leftIndex, temp);

                index = leftIndex;
                //right is valid and left has lower priority than right (left must exist by the heap invariant)
            } else if (rightValid && (comparator.compare(array.get(rightIndex).priority, array.get(leftIndex).priority) > 0)) {
                //swap with right
                nodeToIndex.replace(array.get(rightIndex).element, index);
                nodeToIndex.replace(array.get(index).element, rightIndex);

                HeapNode<E, P> temp = array.get(index);
                array.set(index, array.get(rightIndex));
                array.set(rightIndex, temp);

                index = rightIndex;
            } else {
                //neither left nor right child exists or has higher priority than parent
                break;
            }
        }
    }

    public boolean classInv() {
        //Shape Invariant & Order Invariant
        //Shape invariant is automatically maintained by array representation
        for (int i = 0; i < array.size(); i++) {
            HeapNode<E, P> node = array.get(i);
            int parentIndex = Math.floorDiv(i - 1, 2);
            if (parentIndex >= 0) {
                HeapNode<E, P> nodeParent = array.get(parentIndex);

                // parent's priority can't be lower than node's priority
                if (comparator.compare(nodeParent.priority, node.priority) < 0)
                    return false;
            }

            int leftIndex = 2 * i + 1, rightIndex = 2 * i + 2;
            if (leftIndex < array.size()) {
                HeapNode<E, P> left = array.get(leftIndex);

                // child's priority can't be higher than node's priority
                if (comparator.compare(left.priority, node.priority) > 0)
                    return false;
            }
            if (rightIndex < array.size()) {
                HeapNode<E, P> right = array.get(rightIndex);

                // child's priority can't be higher than node's priority
                if (comparator.compare(right.priority, node.priority) > 0)
                    return false;
            }
        }

        //checks if nodeToIndex matches up with array
        if (nodeToIndex.size() != array.size())
            return false;
        for (int i = 0; i < array.size(); i++) {
            if (!nodeToIndex.containsKey(array.get(i).element) || nodeToIndex.get(array.get(i).element) != i)
                return false;
        }

        return true;
    }

    private static class HeapNode<E, P> {
        private final E element;
        private P priority;

        private HeapNode(E element, P priority) {
            this.element = element;
            this.priority = priority;
        }

        @Override
        public String toString() {
            return element + " | " + priority;
        }
    }
}
