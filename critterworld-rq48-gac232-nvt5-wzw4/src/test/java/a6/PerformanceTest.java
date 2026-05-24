package a6;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Random;

public class PerformanceTest {

    public static void main(String[] args) {
        String directory = ".src/test/java/a6/performance";
        int[] trialSizes = {100000, 500000, 1000000};
        String[] trialNames = {"100k", "500k", "1m"};
        Random random = new Random();

        for (int i = 0; i < trialSizes.length; i++) {
            for (int c = 1; c <= 3; c++) {
                testInsertTime(trialSizes[i], directory + "heap_insert_" + trialNames[i] + "_" + c + ".csv", random);
                testPeekTime(trialSizes[i], directory + "heap_peek_" + trialNames[i] + "_" + c + ".csv", random);
                testPollTime(trialSizes[i], directory + "heap_poll_" + trialNames[i] + "_" + c + ".csv", random);
                testIncreasePriorityTime(trialSizes[i], directory + "heap_increase_priority_" + trialNames[i] + "_" + c + ".csv", random);
            }
        }
    }

    // Test insertion time (O(log n))
    public static void testInsertTime(int numElements, String fileName, Random random) {
        BinaryHeap<Integer, Integer> heap = new BinaryHeap<>(Integer::compareTo);

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.append("Number of Elements,NanoTime\n");

            for (int i = 1; i <= numElements; i++) {
                int key = random.nextInt();
                long startTime = System.nanoTime();
                heap.insert(key, key);
                long endTime = System.nanoTime();
                writer.append(i + "," + (endTime - startTime) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Test peek time (O(1))
    public static void testPeekTime(int numElements, String fileName, Random random) {
        BinaryHeap<Integer, Integer> heap = new BinaryHeap<>(Integer::compareTo);

        for (int i = 1; i <= numElements; i++) {
            heap.insert(random.nextInt(), random.nextInt());
        }

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.append("Number of Elements,NanoTime\n");

            for (int i = 1; i <= numElements; i++) {
                long startTime = System.nanoTime();
                heap.peek();
                long endTime = System.nanoTime();
                writer.append(i + "," + (endTime - startTime) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Test poll time (O(log n))
    public static void testPollTime(int numElements, String fileName, Random random) {
        BinaryHeap<Integer, Integer> heap = new BinaryHeap<>(Integer::compareTo);

        for (int i = 1; i <= numElements; i++) {
            heap.insert(random.nextInt(), random.nextInt());
        }

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.append("Number of Elements,NanoTime\n");

            for (int i = numElements; i > 0; i--) {
                long startTime = System.nanoTime();
                heap.poll();
                long endTime = System.nanoTime();
                writer.append((numElements - i + 1) + "," + (endTime - startTime) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Test increasePriority time (O(log n))
    public static void testIncreasePriorityTime(int numElements, String fileName, Random random) {
        BinaryHeap<Integer, Integer> heap = new BinaryHeap<>(Integer::compareTo);

        for (int i = 0; i < numElements; i++) {
            heap.insert(i, random.nextInt());
        }

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.append("Number of Elements,NanoTime\n");

            for (int i = 0; i < numElements; i++) {
                int key = random.nextInt(numElements);
                int newPriority = random.nextInt();
                long startTime = System.nanoTime();
                heap.increasePriority(key, newPriority);
                long endTime = System.nanoTime();
                writer.append((i + 1) + "," + (endTime - startTime) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
