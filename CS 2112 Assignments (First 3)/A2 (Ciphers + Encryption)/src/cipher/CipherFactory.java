package cipher;

import java.math.BigInteger;
import java.io.*;

/** Factory class for creating cipher objects. */
public class CipherFactory {

    /**
     * Returns: a monoalphabetic substitution cipher with the English alphabet mapped to the
     * provided alphabet.<br>
     * Requires: {@code encrAlph} contains exactly one occurrence of each English letter and nothing
     * more. No requirement is made on case.
     *
     * @param encrAlph the encrypted alphabet
     */
    public Cipher getMonoCipher(String encrAlph) throws IOException{
        try {
            return new MonoAlphabet(encrAlph);
        }// TODO implement
        catch(IOException exc){
            throw new IOException("Invalid input for MonoAlphabet: " + exc.getMessage(), exc);
        }
    }

    /**
     * Returns a new Caesar cipher with the given shift parameter.
     *
     * @param shift the cipher's shift parameter
     */
    public Cipher getCaesarCipher(int shift) {
        return new Caesar(shift);
    }

    /**
     * Returns a Vigenere cipher (with multiple shifts).
     *
     * @param key the cipher's shift parameters. Note that a is a shift of 1.
     */
    public Cipher getVigenereCipher(String key) {
        try {
            return new Vigenere(key); // TODO implement
        }
        catch(IllegalArgumentException exc){
            throw new IllegalArgumentException(exc.getMessage(),exc);
        }
    }

    /** Returns a new monoalphabetic substitution cipher with a randomly generated mapping. */
    public Cipher getRandomSubstitutionCipher() {
        return new Rando(); // TODO implement
    }

    /** Returns a new RSA cipher with a randomly generated keys. */
    public Cipher getRSACipher() {
        return new RSA(); // TODO implement
    }

    /**
     * Returns a new RSA cipher with given key.
     *
     * @param e encryption key
     * @param n modulus
     * @param d decryption key
     */
    public Cipher getRSACipher(BigInteger e, BigInteger n, BigInteger d) {
        return new RSA(e,n,d); // TODO implement
    }
}