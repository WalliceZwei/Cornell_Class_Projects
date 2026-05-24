package coding;

import java.util.Arrays;

public class Q9 {

    public static void main(String[] args) {
        String[] strs = new String[]{"Hello", "and", "Goodbye", "world!"};
        char[] chars = new char[]{'e', 'f', 'g', '!'};
        System.out.println(Arrays.toString(filter(strs, chars)));
    }

    /**
     * Returns a new array containing all strings in strs that contain the corresponding character in chars
     * i.e. strs[i] is included in the result iff strs[i] contains chars[i]
     * The order of strings should remain the same.
     *
     * @param strs  Array of strings to be filtered
     * @param chars Array of characters for filtering
     *              Precondition - chars.length == strs.length
     * @return filtered array of strings
     */
    public static String[] filter(String[] strs, char[] chars) {
        int lengthOfStringArr = strs.length;
        String[] filteredArray = new String[lengthOfStringArr];
        int filteredArrayIndex = 0;
        // The first for loop goes over all the strings in the array of strings
        for(int x = 0; x<lengthOfStringArr;x++){
            // The second for loop goes over all characters in the string
            // to check the presence of the character, and if detected, will put that string in the array,
            // break and move onto the next string, and move the array pointer to the next index
            for(int y = 0; y < strs[x].length(); y++){
                if (strs[x].charAt(y) == chars[x]){
                    filteredArray[filteredArrayIndex] = strs[x];
                    filteredArrayIndex++;
                    break;
                }
            }
        }
        // This return statement prunes the array of all "default" values 
        // that might be left over when not all string contain the specified characters
        return Arrays.copyOfRange(filteredArray,0,filteredArrayIndex);
    }
}
