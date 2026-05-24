package cipher;

import java.math.BigInteger;
import java.lang.String;
import java.io.IOException;
import java.io.EOFException;
import java.security.SecureRandom;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class RSA implements Cipher{

    private BigInteger privateKey, publicKey, modulus;

    // Class Invariants: modulus is a composite number made of two primes, with bitlength between 1017 and 1023
    // public key is a fixed prime, and private key is the modular inverse of the fixed prime under euler's totient (p-1)(q-1)
    // Information about the String is broken up into chunks (126) of hexadecimal (representing bytes) padded with zeroes
    // Byte Code is in chunks of 128, to be read back into and decrypted
    // Conforms to the UTF-8 Standard of Character Encoding


    /**
     * Creates a new RSA Key, with the private key, public key, and modulus
     *  Small chance of failure
     */

    // Generates a new RSA Key with a fixed prime (2^16+1) as public key
    // Generates a modulus and euler's totient of those two randomly generated primes (p-1)(q-1) with enough bitlength
    // Finds the modular inverse of the public key with euler's totient as the modulus, makes that the private key

    public void createRSAKey(){
        SecureRandom randomSource = new SecureRandom();
        BigInteger randprimep, randprimeq, newModulus, newPublicKey, newPrivateKey;
        newPublicKey = new BigInteger("65537");
        while(true) {
            randprimep = new BigInteger(512, 20, randomSource);
            randprimeq = new BigInteger(512, 20, randomSource);
            newModulus = randprimep.multiply(randprimeq);
            if (newModulus.bitLength()>=1017 && newModulus.bitLength() <= 1023){
                break;
            }
        }
        BigInteger uno = new BigInteger("1");
        BigInteger eulerTotient = randprimep.subtract(uno).multiply(randprimeq.subtract(uno));
        newPrivateKey = newPublicKey.modInverse(eulerTotient);

        this.modulus = newModulus;
        this.publicKey = newPublicKey;
        this.privateKey = newPrivateKey;
    }


    /**
     * Initializes RSA with given public, private, and modulus
     *
     * @param inputPublicKey Some Prime
     * @param inputModulus product of two primes
     * @param inputPrivateKey Inverse of the Public Key % Euler's totient of Modulus
     *
     * Precondition: Assumes that the public, private, and modulus satisfy RSA's class invariants
     */

    public RSA(BigInteger inputPublicKey, BigInteger inputModulus, BigInteger inputPrivateKey){
        this.privateKey = inputPrivateKey;
        this.publicKey = inputPublicKey;
        this.modulus = inputModulus;
    }

    /**
     * Initializes a new and random RSA Key
     */
    public RSA(){
        createRSAKey();
    }

    public String encrypt(String plaintext) {
        return null;
    }

    // Takes in a byte array, turns each byte into a hex string of length 2, and stitches them all together to form a number
    // This number is then modified with the RSA Algorithm formula for encryption/decryption
    // if the resulting hex string is odd length, that means the first 0 was ommitted, and needs to be added back

    private String bytetohexcryption(byte[] byteArr, BigInteger keyType){
        String hexString = "";
        for(byte b: byteArr){
            hexString += String.format("%02x", b&0xFF);
        }
        BigInteger byteNumber = new BigInteger(hexString,16);
        String cryptString = byteNumber.modPow(keyType,this.modulus).toString(16);
        if(cryptString.length()%2 == 1) {cryptString = "0"+cryptString;}

        return cryptString;
    }

    // input a byte array of the words you want

    private String encrypt(byte[] bytearr){
        return bytetohexcryption(bytearr,this.publicKey);
    }

    public String decrypt(String s){
        return null;
    }





    // gets the decrypted hex string via RSA Algorithm, and then turns each hex substring of length 2 to a byte, to turn into a byte array
    // then, it passes that byte array to turn into a string using utf8 charset
    // Finds the length of the chunk in the first hexadecimal pair, and weeds out anything longer than 126 bytes (meaning there was some corruption)
    // Tries to weed out any invalid utf8 character byte arrays, and weeds out any where the header length is shorter than the actual bytes

    private String decrypt (byte[] encryptedMessage) throws IOException{
        String decryptedHexString = bytetohexcryption(encryptedMessage,this.privateKey);
        int decryptedHexStringLength = Integer.parseInt(decryptedHexString.substring(0,2),16);
        byte[] convertedByteArray = new byte[126];
        int pointer = 2;
        String decryptedString;
        if (decryptedHexStringLength>126){
            throw new IOException("Invalid Chunk Size, too large");
        }

        for(int x = 0; x<decryptedHexStringLength;x++){
            convertedByteArray[x]=(byte) Integer.parseInt(decryptedHexString.substring(pointer,pointer+2), 16);
            pointer+=2;
        }
        convertedByteArray = Arrays.copyOfRange(convertedByteArray,0,decryptedHexStringLength);
        try{
            decryptedString = new String(convertedByteArray,StandardCharsets.UTF_8);
        }
        catch(Exception exc){
            throw new IOException("Invalid Characters");
        }
        if (pointer < 252 && (byte) Integer.parseInt(decryptedHexString.substring(pointer,pointer+2), 16) != 0){
            throw new IOException("Invalid/Jumbled Byte Input");
        }
        return decryptedString;
    }

    /**
     * Encrypts UTF-8 Characters from an inputstream into byte code to export to a outputstream
     * @param in The InputStream the UTF-8 characters are on
     * @param out The OutputStream to send the bytecode
     * @throws IOException Invalid Input/Output File, or Invalid Chunking Procedures
     * @throws EOFException No more chunks left to read
     *
     * Satifies Class Invariants
     */

    // takes the encrypted string, and turns those hex values into byte value, while ensuring that it is 128 bytes
    // by padding it with lead zeroes with variable offset, then iterates over chunks to encrypt


    public void encrypt(InputStream in, OutputStream out) throws IOException{
        try(BufferedInputStream inners = new BufferedInputStream(in)) {
            try(BufferedOutputStream outers = new BufferedOutputStream(out)) {
                Chunks reader = new Chunks(inners);
                byte[] inputByteArray = new byte[127];
                reader.chunkSize = 126;
                while(true){
                    try{
                        reader.nextChunk(inputByteArray);
                        String hexstr = encrypt(inputByteArray);
                        int offset = (256 - hexstr.length()) / 2;
                        for (int x = 0; x < offset; x++) {
                            outers.write((byte) 0);
                        }
                        for (int x = 0; x < hexstr.length() / 2; x++) {
                            byte h = (byte) Integer.parseInt(hexstr.substring(2 * x, 2 * (x + 1)), 16);
                            outers.write(h);
                        }
                        outers.flush();
                        inputByteArray = new byte[127];
                    }
                    // End of line signified by EOF, error signified by IO Exceptions
                    catch(EOFException exc){
                        break;
                    }
                    catch(IOException exc){
                        throw new IOException(exc.getMessage(),exc);
                    }
                }
            }
            catch(IOException exception){
                throw new IOException("Error in Output File", exception);
            }
        }
        catch(IOException exception){
            throw new IOException("Error in Input File", exception);
        }
    }

    /**
     * Decrypts byte code from an inputstream into UTF-8 Characters to export to a outputstream
     *
     * @param in The InputStream the byte code is in
     * @param out The OutputStream to send the UTF-8 characters
     * @throws IOException Invalid Input/Output File
     *
     * Satifies Class Invariants
     */

    // takes the decrypted string, then turns it into a byte array to write out to a file
    // note that it iterates over the chunks to do so, and takes the 1 to 128th index because 0 index is length

    public void decrypt(InputStream in, OutputStream out) throws IOException {
        try(BufferedInputStream inners = new BufferedInputStream(in)) {
            try(BufferedOutputStream outers = new BufferedOutputStream(out)) {
                Chunks reader = new Chunks(inners);
                byte[] inputByteArray = new byte[129];
                reader.chunkSize = 128;
                while(true){
                    try{
                        reader.nextChunk(inputByteArray);
                        inputByteArray = Arrays.copyOfRange(inputByteArray, 1, 129);
                        outers.write(decrypt(inputByteArray).getBytes());
                        outers.flush();
                        inputByteArray = new byte[129];
                    }
                    //Catches EOFexception to stop decryption as soon as possible because end of line
                    catch(EOFException exc){
                        break;
                    }
                    // This means something actually went wrong
                    catch (Exception exc){
                        throw new IOException(exc.getMessage(),exc);
                    }
                }
            }
            catch (IOException exception){
                throw new IOException("Error in Output File",exception);
            }
        }
        catch(IOException exception){
            throw new IOException("Error in Input File", exception);
        }
    }

    /**
     * Writes out RSA Key (public key, private key, and moduli) to a file
     * @param out The OutputStream to write the cipher key to
     * @throws IOException Couldn't save key to file (will try to create a new file first)
     */

    public void save (OutputStream out) throws IOException {
        try (BufferedOutputStream outers = new BufferedOutputStream(out)){
            String fileMessage = "RSA" + "\n" + this.privateKey + "\n" + this.publicKey + "\n" + this.modulus + "\n";
            outers.write(fileMessage.getBytes());
            outers.flush();
        }
        catch (IOException e){
            throw new IOException("Error Saving RSA Key to Output File",e);
        }
    }
}