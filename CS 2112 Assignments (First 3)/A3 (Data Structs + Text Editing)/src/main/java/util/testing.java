package util;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class testing {
    public static void main(String[] args){

        int[] sampleSizes = {100, 1000, 10000, 100000, 1000000};
        int trials = 5;
        int wordLength = 10;
        int testRuns = 10;
        String fileName = "performance/performance.csv";
        System.out.println("waiting...");
        long start = System.nanoTime();
        while (System.nanoTime() - start < 10000000000.0) {}
        System.out.println("finished waiting.");

        for (int i = 0; i < testRuns + 1; i++) {
            File file = new File(fileName);
            File parentDir = file.getParentFile();
            if (file.exists()) {
                file.delete();
            }
            parentDir.mkdirs();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                writer.write("Table Size,Put Time (ns),Get Time (ns),Empty Buckets,Collisions\n");
                for (int sampleSize: sampleSizes) {
                    System.out.println("Beginning test case: " + String.valueOf(sampleSize));
                    writer.write(testCase(sampleSize, wordLength, trials));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    static String testCase(int sampleSize, int wordLength, int trials) {
        StringBuilder str = new StringBuilder();
        List<String> testCases;

        for (int trial = 0; trial < trials; trial++) {
            HashTable<String, String> hashtab = new HashTable<>(5*sampleSize);
            testCases = generateRandomEntries(sampleSize, wordLength);
            str.append(String.valueOf(sampleSize)).append(",");
            str.append(String.valueOf(trial)).append(",");

            // time put
            long putStartTime = System.nanoTime();
            for (int x = 0; x < sampleSize; x++) {
                hashtab.put(testCases.get(x), "test");
            }
            long putTime = System.nanoTime() - putStartTime;
            str.append(String.valueOf(putTime)).append(",");

            // time get
            long getStartTime = System.nanoTime();
            for (int x = 0; x < sampleSize; x++) {
                hashtab.get(testCases.get(x));
            }
            long getTime = System.nanoTime() - putStartTime;
            str.append(String.valueOf(getTime)).append(",");

            // num empty buckets
            int emptyBuckets = hashtab.getEmptyBuckets();
            str.append(String.valueOf(emptyBuckets)).append(",");

            // num collisions
            int collisions = hashtab.getCollisions();
            str.append(String.valueOf(collisions)).append("\n");
        }
        return str.toString();
    }

    static List<String> generateRandomEntries(int sampleSize, int wordLength) {
        List<String> testCases = new ArrayList<>();
        Random random = new Random();
        for (int sample = 0; sample < sampleSize; sample++) {
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < wordLength; i++) {
                str.append((char) ('a' + random.nextInt(0,26)));
            }
            testCases.add(str.toString());
        }
        return testCases;
    }
}

