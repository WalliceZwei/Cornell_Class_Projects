package coding;

import java.util.Arrays;

public class Q11 {
    public static void main(String[] args) {
        int[] ans =
                findSymDifference(new int[]{9, 2, 3, 4, 5, 6, 7, 8, 1},
                        new int[]{7, 8, 9, 10, 11});
        System.out.println(Arrays.toString(ans));
    }

    /**
     * Returns an array containing the symmetric difference of the
     * arrays a1 and a2 (the order of the resulting array does not matter).
     * The symmetric difference of a1 and a2 contains all those elements which belong
     * to exactly ONE of a1 and a2.
     *
     * @param a1 - does not contain any duplicates
     * @param a2 - does not contain any duplicates
     * @return Array containing the symmetric difference of a1 and a2 in any order
     */
    public static int[] findSymDifference(int[] a1, int[] a2) {
        // create a "safe array" that has the length of a1 and a2, just in case the symmetric difference is that long
        int[] symDiffArray = new int[a1.length+a2.length];
        int a1Index = 0;
        int a2Index = 0;
        int symArrIndex = 0;
        // Pre-sort both arrays so that we can progressively compare numbers to see if we can add it because it is less
        // than the other value in the other array
        Arrays.sort(a1);
        Arrays.sort(a2);
        // Make sure the first pass doesn't have any of their indices exceed the lengths of either list
        while (a1Index < a1.length && a2Index < a2.length){
            // if the two numbers are equal, we don't add either to the symmetric array, and move on from both
            if (a1[a1Index] == a2[a2Index]){
                a1Index++;
                a2Index++;
            }
            // if a1's array entry is less than a2's array entry, then it keeps on adding more a1 array entries as unique
            // until they run out or a1's entry ceases to be less than a2's array entry
            else if(a1[a1Index] < a2[a2Index]){
                while(a1Index < a1.length && a1[a1Index] < a2[a2Index]){
                    symDiffArray[symArrIndex] = a1[a1Index];
                    symArrIndex++;
                    a1Index++;
                }
            }
            // same as above but reversed
            else{
                while(a2Index < a2.length && a2[a2Index] < a1[a1Index]){
                    symDiffArray[symArrIndex] = a2[a2Index];
                    symArrIndex++;
                    a2Index++;
                }
            }
        }
        // If a1 still has entries left, then they must be above any other entry in the other list by virtue of the previous statements
        // So, we just add the rest of the entries in a1
        if (a1Index < a1.length){
            while (a1Index < a1.length){
                symDiffArray[symArrIndex] = a1[a1Index];
                symArrIndex++;
                a1Index++;
            }
        }
        // same as above but reversed
        else if (a2Index < a2.length){
            while (a2Index < a2.length){
                symDiffArray[symArrIndex] = a2[a2Index];
                symArrIndex++;
                a2Index++;
            }
        }
        // TODO Implement this method.
        // Returns an the symmetric difference array, while truncating the length of the big initial array
        // To only include the real length of the symmetric difference array
        return Arrays.copyOfRange(symDiffArray,0,symArrIndex);
    }
}

