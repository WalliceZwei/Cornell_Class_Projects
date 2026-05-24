package cipher;

public class Vigenere extends AbstractCipher {
    private char[] key;

    // Class invariant: the Vigenere Cipher is represented by a constant key, an is a nonempty string where the letters represent shifts (a=1, b=2 ...)
    // This key is cylically used to encode/decode strings,

    /**
     * @param inputcipher A cylically repeating string that shifts based on the character present
     *                    (a = 1, b = 2, ...)
     * Requires: A string of purely alphabetic characters, or else it will be enforced
     * @throws IllegalArgumentException when an invalid alphabet is put in
     */

    // Satisfies preconditions, and also stores the Vigenere key into an array for easier usage & its cyclic nature

    public Vigenere(String inputcipher) throws IllegalArgumentException{
        inputcipher = inputcipher.toLowerCase();
        inputcipher = inputcipher.replaceAll("[^a-z]","");
        if(inputcipher.isEmpty()){
            throw new IllegalArgumentException("Vigenere Cipher String must be non-empty after removing non-alphabetic characters");
        }
        this.cipherType = "Vigenere";
        this.alphabetString = inputcipher.toLowerCase();
        this.key = inputcipher.toCharArray();
    }

    /**
     * Encrypts plaintext using the Vigenere Cipher
     *
     * @param plaintext The plaintext to be encrypted
     * Enforced precondition: plaintext must be alphabetic or whitespace, and lowercase
     * @return encrypted text with a Vigenere cipher applied
     */

    // index of original character in alphabet = text.charAt(n) - (int)'a'
    // index of "adjustment" that vigenere makes = (int) key[n%key.length] - (int)'a' + 1
    // plus or minus these two to get the encryption and decryption

    // Checks for each character if it is alphabetic, and if it isn't, the character isn't encrypted (whitespace)
    // Uses the Vigenere conversion factor to encrypt

    public String encrypt(String plaintext){
        plaintext = plaintext.toLowerCase();
        plaintext = plaintext.replaceAll("[^a-z\\s]", "");
        String compiledText = "";
        for(int n = 0; n<plaintext.length(); n++){
            if ((int)plaintext.charAt(n) <= (int)'z' && (int)plaintext.charAt(n) >= (int)'a'){
                int x = (int) plaintext.charAt(n) + (int) key[n%key.length] - 2*(int)'a' + 1;
                compiledText += (char) (negativemodulosaver(x) + (int)'a');
            }
            else{
                compiledText+=plaintext.charAt(n);
            }
        }
        return compiledText;
    }

    /**
     * Decrypts ciphertext using the Vigenere Cipher
     *
     * @param ciphertext The ciphertext to be encrypted
     * Enforced Precondition: ciphertext must be alphabetic or whitespace, and lowercase
     * @return decrypted Vigenere text to plaintext
     */

    // Inverse of encryption, iterates through each ciphertext character, and if not alphabetical, it is whitespace, so it is added on
    // If it alphabetical, then it is converted back to plaintext using the Vigenere conversion factor, while accounting for 1-indexing of the alphabet

    public String decrypt(String ciphertext){
        ciphertext = ciphertext.toLowerCase();
        ciphertext = ciphertext.replaceAll("[^a-z\\s]", "");
        String compiledText = "";
        for(int n = 0; n<ciphertext.length(); n++){
            // decrypt each character in the ciphertext
            if ((int)ciphertext.charAt(n) <= (int)'z' && (int)ciphertext.charAt(n) >= (int)'a') {
                int x = (int) ciphertext.charAt(n) - (int) key[n % key.length] - 1;
                compiledText += (char) (negativemodulosaver(x) + (int) 'a');
            }
            else{
                compiledText+=ciphertext.charAt(n);
            }
        }
        return compiledText;
    }
}