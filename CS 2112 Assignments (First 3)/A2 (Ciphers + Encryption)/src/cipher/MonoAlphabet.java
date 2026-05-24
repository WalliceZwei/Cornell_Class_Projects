package cipher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.LinkedHashSet;

public class MonoAlphabet extends AbstractCipher{

    //Class Invariants: newAlphabet is a permutation of the alphabet (26 letters) with no duplicates
    // newAlphabet and decoder are inverses of each other (switching the places of the letters from index to value, and parameterizing them into numbers for decoder)
    // output is always lowercase alphabetical characters with whitespace


    protected ArrayList<Character> newAlphabet = new ArrayList<>(Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'));
    protected int[] decoder = new int[alphabet.length];


    // Class Invariant: Cipher alphabet (a-z)


    /**
     * Empty initializer meant for inheritance
     */
    public MonoAlphabet(){}

    /**
     * Initializes a monoaphabetic cipher
     *
     * @param newalphabet cipher that the normal a-z alphabet gets mapped to
     * Enforces Precondition: Characters must be unique, and be in the a-z alphabet
     * @throws IOException thrown when an invalid "alphabet" is input
     */

    // Turns input string lowercase and a-z, ensures string is an alphabet of (26) letters
    // Makes sure alphabet is "unique" with no duplicates through a set, and throws exceptions for any non-compliance
    // Uses an array that remembers what the encrypted letters map to in normal alphabetic letters
    // Uses a list for the new alphabet because of an inherited class (random), but also to easily encryot messages

    public MonoAlphabet(String newalphabet) throws IOException{
        newalphabet = newalphabet.toLowerCase();
        String alphanuevo = newalphabet.replaceAll("[^a-z]", "");
        Set<Character> h = new LinkedHashSet<>();

        if (newalphabet.length() != alphanuevo.length() || newalphabet.length() != 26){
            throw new IOException("Input a correct alphabet with no spaces or other characters, 26 letters a-z");
        }
        else{
            for(int x = 0; x<26;x++){
                h.add(newalphabet.charAt(x));
            }
            if(h.size() != 26){
                throw new IOException("Input an alphabet a-z with unique characters");
            }
        }
        this.alphabetString = newalphabet;
        for(int x = 0; x<alphabet.length;x++){
            newAlphabet.set(x,newalphabet.charAt(x));
            decoder[(int)newalphabet.charAt(x)-(int)'a'] = x;
        }
    }

    /**
     * Encrypts a message using a new alphabet
     * @param plaintext The plaintext to be encrypted
     * Enforces precondition: A plaintext string composed of only lowercase alphabetic characters (a-z) and whitespace
     * @return encrypted plaintext using the alphabet
     */

    // Makes to be encrypted string lowercase and only full of whitespace and alphabetical characters, and doesn't encrypt whitespace
    // Encrypts all alphabetic characters by assigning them there new alphabetic conjugate (cipher) as determined by the user
    public String encrypt(String plaintext){
        plaintext = plaintext.toLowerCase();
        plaintext = plaintext.replaceAll("[^a-z\\s]", "");
        String randoText = "";
        for(int k = 0; k<plaintext.length();k++){
            if ((int)plaintext.charAt(k) <= (int)'z' && (int)plaintext.charAt(k) >= (int)'a') {
                randoText += newAlphabet.get((int) plaintext.charAt(k) - (int) 'a');
            }
            else{
                randoText+=plaintext.charAt(k);
            }
        }
        return randoText;
    }

    /**
     * precondition: ciphertext must only contain characters in the alphabet or whitespace
     * Decrypts ciphertext using an alphabetic scramble
     * @param ciphertext The ciphertext to decrypt
     * Precondition: A ciphertext string composed of only lowercase alphabetic characters (a-z) and whitespace
     * @return decrypted plaintext message
     */

    // Decrypts the ciphertext character by character, using the fact that if the character isn't alphabetical it is whitespace
    // Leverages the use of the decoder array to find the correct plaintext from ciphertext

    public String decrypt(String ciphertext){
        String randoText = "";
        for(int k = 0; k<ciphertext.length();k++) {
            if ((int) ciphertext.charAt(k) <= (int) 'z' && (int) ciphertext.charAt(k) >= (int) 'a'){
                randoText += alphabet[decoder[(int) ciphertext.charAt(k) - (int) 'a']];
            }
            else{
                randoText+=ciphertext.charAt(k);
            }
        }
        return randoText;
    }
}
