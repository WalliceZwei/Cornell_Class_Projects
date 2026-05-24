package coding;

@SuppressWarnings("ALL")
public class Q13 {

    public static void main(String[] args) {
        // these are not palindromes
        test(1, !isPalindrome("abc"));
        test(2, !isPalindrome("aslkjdhflakjsd"));
        test(3, !isPalindrome("palindrome? not"));

        // These are palindromes
        test(4, isPalindrome("aBcbA"));
        test(5, isPalindrome("a Man a Plan a Canal Panama"));
        test(6, isPalindrome("racecar"));
        test(7, isPalindrome("x"));
        test(8, isPalindrome("!@#$%^&*&^%$#@!"));
        test(9, isPalindrome(""));
    }

    /**
     * Returns true if s is a palindrome. Ignores case and whitespace
     *
     * @param s: string to be tested
     * @return true if s is palindrome
     */
    public static boolean isPalindrome(String s) {
        // make lowercase and strip whitespace
        s = s.toLowerCase().replaceAll("\\s", "");
        // checks if the reversed string is equal to the input, rather than using ==
        // == only really works on primitive types, .equals() just checks if they have the same contents
        // for Strings, reverse(s) and s point to two different objects of the same contents
        return s.equals(reverse(s));
    }

    /**
     * reverses a string
     *
     * @param s
     * @return reversed string
     */
    public static String reverse(String s) {
        return new StringBuilder(s).reverse().toString();
    }

    /**
     * prints to the console results of tests
     *
     * @param i test number
     * @param passed boolean
     */
    public static void test(int i, boolean passed) {
        if (passed) {
            System.out.println("Test " + i + " passed");
        } else {
            System.out.println("Test " + i + " failed");
        }
    }
}
