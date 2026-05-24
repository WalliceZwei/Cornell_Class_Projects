package cipher;
import java.security.SecureRandom;
import java.util.Collections;

public class Rando extends MonoAlphabet{
    private SecureRandom randomAlphabetShuffler = new SecureRandom();



    /**
     * Initializes a random alphabet permutation as a cipher
     * Maps each letter to a random new letter based on its position
     */

    // Uses a list to shuffle the alphabet randomly, then records the shuffled alphabet to a string.
    // Also, it updates an array such that the index is the letter used in the random cipher, and the contents is the actual letter
    // This makes it easier to reverse an encrypted random cipher

    public Rando(){
        Collections.shuffle(newAlphabet, randomAlphabetShuffler);
        for(int j = 0; j < alphabet.length; j++){
            this.alphabetString += newAlphabet.get(j);
            this.decoder[(int)newAlphabet.get(j)-(int)'a'] = j;
        }
    }
}