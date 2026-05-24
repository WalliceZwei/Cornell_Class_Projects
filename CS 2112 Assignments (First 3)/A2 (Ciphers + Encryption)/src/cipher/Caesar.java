package cipher;

public class Caesar extends MonoAlphabet{
    private int shifters;

    // Class Invariant: Integer shifts must be made between 0-25
    // alphabetString/newAlphabet is the same as the normal alphabet, but cyclically shifted
    // decoder maps from ciphertext to plaintext using indices as ciphertext to values as plaintext


    /**
     * Initializes a Caesar Cipher
     *
     * @param shift Shifts and maps each letter to each other by moving them all over by the shift amount
     *              like a roledex
     *
     * Precondition: an integer from -maxint to +maxint
     */
    // Initializes the amount of shift, and alphabet under a caesar shift (cyclic shift)
    // accounts for the negative shifts, and turns the list inherited from MonoAlphabet to correspond to the shift
    // As well as using the mapping from the shifted letter to its original letter with decoder

    public Caesar(int shift){
        this.shifters = negativemodulosaver(shift);
        for(int i = 0; i < alphabet.length; i++){
            int shiftedamount = (i+this.shifters)%alphabet.length;
            this.alphabetString += alphabet[shiftedamount];
            this.newAlphabet.set(i, alphabet[shiftedamount]);
            this.decoder[shiftedamount] = i;
        }
    }
}