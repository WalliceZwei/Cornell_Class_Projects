package cipher;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.String;
import java.util.Scanner;
/** A place to put some inherited code? */
public abstract class AbstractCipher implements Cipher {

    // Class Invariants: alphabetString is a valid permutation of letters of the alphabet
    // cipherType represents the type of cipher
    // alphabet must stay as an alphabet from a-z in that order

    protected char[] alphabet = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    protected String alphabetString = "";
    protected String cipherType = "Mono";

    protected int negativemodulosaver(int x){
        return ((x%alphabet.length) + alphabet.length)%alphabet.length;
    }

    /**
     * Uses InputStream and encrypts the string for each line to output to OutputStream
     *
     * @param in The InputStream the plaintext is on
     * @param out The OutputStream to send the ciphertext to
     * @throws IOException Invalid Input/Output File
     */

    // Reads input files line by line, encrypts, and writes to output, while remembering to flush output

    public void encrypt(InputStream in, OutputStream out) throws IOException {
        try(Scanner scans = new Scanner(in)) {
            try(BufferedOutputStream outers = new BufferedOutputStream(out)) {
                while (scans.hasNextLine()) {
                    outers.write(encrypt(scans.nextLine()).getBytes());
                    outers.flush();
                }
            }
            catch(IOException exception){
                throw new IOException("Error in Output File",exception);
            }
        }
        catch(IOException exception){
            throw new IOException("Error in processing Input File",exception);
        }
    }

    /**
     * Uses InputStream and decrypts the string for each line to output to OutputStream
     *
     * @param in The InputStream the cipher is on
     * @param out The OutputStream to send the plaintext to
     * @throws IOException Invalid Input/Output File
     */

    // Reads input files line by line, decrypts, and writes to output, while remembering to flush output

    public void decrypt(InputStream in, OutputStream out) throws IOException {
        try(Scanner scans = new Scanner(in)) {
            try(BufferedOutputStream outers = new BufferedOutputStream(out)) {
                while (scans.hasNextLine()) {
                    outers.write(decrypt(scans.nextLine()).getBytes());
                    outers.flush();
                }
            }
            catch(IOException exception){
                throw new IOException("Error in processing Output File", exception);
            }
        }
        catch(IOException exception){
            throw new IOException("Error in processing Input File",exception);
        }
    }

    /**
     * Writes out String Key (either Vigenere or MonoAlphabetic) to a file
     * @param out The OutputStream to write the cipher key to
     * @throws IOException Invalid output file (will try to create one first)
     */

    public void save(OutputStream out) throws IOException{
        try (BufferedOutputStream outers = new BufferedOutputStream(out)){
            String example = this.cipherType + "\n" + this.alphabetString + "\n";
            outers.write(example.getBytes());
            outers.flush();
        }
        catch(IOException exception){
            throw new IOException("Error Saving " + this.cipherType + " Key", exception);
        }
    }
}