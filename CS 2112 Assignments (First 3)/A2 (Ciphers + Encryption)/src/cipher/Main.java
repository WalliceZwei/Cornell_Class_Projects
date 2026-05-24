package cipher;

import java.io.*;

import java.util.Scanner;
import java.math.BigInteger;

/**
 * Command line interface to allow users to interact with your ciphers.
 *
 * <p>We have provided some infrastructure to parse most of the arguments. It is your responsibility
 * to implement the appropriate actions according to the assignment specifications. You may choose
 * to "fill in the blanks" or rewrite this class.
 *
 * <p>Regardless of which option you choose, remember to minimize repetitive code. You are welcome
 * to add additional methods or alter the provided code to achieve this.
 */

public class Main {

    public static void main(String[] args) {

        //driver code to run three command line "statements"
        Main commandLineInterface = new Main();
        try{
            int pos = commandLineInterface.parseCipherType(args,0);
            try{
                int pos2 = commandLineInterface.parseCipherFunction(args,pos);
                try{
                    commandLineInterface.parseOutputOptions(args, pos2);
                }
                catch(IllegalArgumentException exc){
                    System.err.println(exc.getMessage());
                }
                catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
            catch(IllegalArgumentException exc){
                System.err.println(exc.getMessage());
            }
            catch (FileNotFoundException e) {
                System.err.println(e.getMessage());
            }

        }
        catch(IllegalArgumentException exc){
            System.err.println(exc.getMessage());
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Set up the cipher type based on the options found in args starting at position pos, and
     * return the index into args just past any cipher type options.
     */

    //CipherObject keeps track of the current cipher

    private Cipher CipherObject = null;

    // tries to get the correct ciphertype, if it just uses a string, then just use args[pos++] and input that into
    // the object in question. If it needs a file, then you have to check the file and make sure it has the right contents according to the save methods
    // then initialize it

    private int parseCipherType(String[] args, int pos) throws IllegalArgumentException, FileNotFoundException{

        // check if arguments are exhausted
        if (pos == args.length) return pos;

        String cmdFlag = args[pos++];
        CipherFactory reference = new CipherFactory();

        switch (cmdFlag) {
            case "--caesar":
                CipherObject = reference.getCaesarCipher(Integer.parseInt(args[pos++]));
                break;
            case "--random":
                CipherObject = reference.getRandomSubstitutionCipher();
                break;
            case "--monoLoad":
                try {
                    FileInputStream on = new FileInputStream(args[pos++]);
                    BufferedInputStream sh = new BufferedInputStream(on);
                    Scanner scans = new Scanner(sh);
                    String s = scans.nextLine().replaceAll("[^a-zA-Z]", "");
                    if (s.equals("Mono")){
                        try {
                            String h = scans.nextLine().replaceAll("\n","");
                            CipherObject = reference.getMonoCipher(h);
                        }
                        catch(IOException e){
                            throw new IllegalArgumentException(e.getMessage(),e);
                        }
                    }
                    else{
                        throw new IllegalArgumentException("Mono not present in file title");
                    }
                }
                catch (FileNotFoundException exc){
                    throw new FileNotFoundException("File Input Not Found");
                }

                // TODO load a monoalphabetic substitution cipher from a file
                break;
            case "--vigenere":
                try {
                    CipherObject = reference.getVigenereCipher(args[pos++]);
                }
                catch(IllegalArgumentException exc){
                    throw new IllegalArgumentException(exc.getMessage(),exc);
                }
                break;
            case "--vigenereLoad":
                try {
                    FileInputStream on = new FileInputStream(args[pos++]);
                    BufferedInputStream sh = new BufferedInputStream(on);
                    Scanner scans = new Scanner(sh);
                    String s = scans.nextLine().replaceAll("[^a-zA-Z]", "");
                    if (s.equals("Vigenere")){
                        CipherObject = reference.getVigenereCipher(scans.nextLine());
                    }
                }
                catch (FileNotFoundException exc){
                    throw new FileNotFoundException("File Input Not Found");
                }
                break;
            case "--rsa":
                CipherObject = reference.getRSACipher();
                break;
            case "--rsaLoad":
                try {
                    FileInputStream on = new FileInputStream(args[pos++]);
                    BufferedInputStream sh = new BufferedInputStream(on);
                    Scanner scans = new Scanner(sh);
                    String s = scans.nextLine().replaceAll("[^a-zA-Z]", "");
                    if (s.equals("RSA")){
                        BigInteger d = new BigInteger(scans.nextLine().replaceAll("\n",""));
                        BigInteger e = new BigInteger(scans.nextLine().replaceAll("\n",""));
                        BigInteger n = new BigInteger(scans.nextLine().replaceAll("\n",""));
                        CipherObject = reference.getRSACipher(e,n,d);
                    }
                }
                catch (FileNotFoundException exc){
                    throw new FileNotFoundException("File Input Not Found");
                }
                break;
            default:
                throw new IllegalArgumentException("No Cipher Type Inputted in Command Line");
        }
        return pos;
    }

    /**
     * Parse the operations to be performed by the program from the command-line arguments in args
     * starting at position pos. Return the index into args just past the parsed arguments.
     */


    private InputStream fis;
    private FileOutputStream outsfile;
    private BufferedOutputStream outsy;
    private int x = 0;
    private String str = "";


    //encrypts or decrypts the data, and uses various variables to stores this fact to prepare for
    //outputting it. If it is a string, then the decryption will already be done, but if it is not, then
    // the variable x will store what kind of encrypt it is with what type of output.
    // x = 0 is encrypt/decrypt string, x = 1 is encrypt file/bytearrayinputstream, and x = 2 is decrypt file


    //Exceptions are thrown whenever an error from a method has an exception, or when there is an error in finding something
    // Or when there is an error in incompatible arguments


    private int parseCipherFunction(String[] args, int pos) throws IllegalArgumentException, FileNotFoundException{
        // check if arguments are exhausted

        if (pos == args.length) return pos;

        switch (args[pos++]) {
            case "--em":
                if (CipherObject instanceof AbstractCipher){
                    str = CipherObject.encrypt(args[pos++]);
                }
                else {
                    x =1;
                    fis = new ByteArrayInputStream(args[pos++].getBytes());
                }
                // TODO encrypt the given string
                break;
            case "--ef":
                x=1;
                // TODO encrypt the contents of the given file
                try {
                    fis = new FileInputStream(args[pos++]);
                }
                catch (FileNotFoundException exc){
                    throw new FileNotFoundException("Input File Not Found");
                }
                break;
            case "--dm":
                if (CipherObject instanceof AbstractCipher){
                    String h = args[pos++];
                    str = CipherObject.decrypt(h);
                }
                else{
                    throw new IllegalArgumentException("Can't decrypt alphabetic text strings in RSA, needs byte code from a file");
                }
                // TODO decrypt the given string -- substitution ciphers only
                break;
            case "--df":
                x=2;
                try {
                    fis = new FileInputStream(args[pos++]);
                }
                catch (FileNotFoundException exc){
                    throw new FileNotFoundException("Input File Not Found");
                }
                // TODO decrypt the contents of the given file
                break;
            default:
                throw new IllegalArgumentException("No Cipher Function Inputted");
        }
        return pos;
    }

    /**
     * Parse options for output, starting within {@code args} at index {@code argPos}. Return the
     * index in args just past such options.
     */

    // Finally tries to output it, and can use as many options as you can with a while loop
    // uses the values of x in the previous function to go through with what inputs and outputs to use
    // throws exceptions if invalid arguments are given, like rsa byte code being printed out

    private int parseOutputOptions(String[] args, int pos) throws IllegalArgumentException,IOException {
        // check if arguments are exhausted
        if (pos == args.length) return pos;

        String cmdFlag;
        while (pos < args.length) {
            switch (cmdFlag = args[pos++]) {
                case "--print":
                    if(x==1 && CipherObject instanceof AbstractCipher){
                        try {
                            CipherObject.encrypt(fis, System.out);
                        }
                        catch(IOException e){
                            throw new IOException(e.getMessage(),e);
                        }
                    }
                    if(x==2){
                        try {
                            CipherObject.decrypt(fis, System.out);
                        }
                        catch(IOException e){
                            throw new IOException(e.getMessage(),e);
                        }
                    }

                    else if (CipherObject instanceof RSA){
                        throw new IllegalArgumentException("Can't print encrypted RSA byte code, use a substitution cipher for print, or use --out for RSA");
                    }

                    else {
                        System.out.println(str);
                    }
                    // TODO print result of applying the cipher to the console -- substitution
                    // ciphers only
                    break;
                case "--out":
                    String g = args[pos++];
                    try {
                        outsfile = new FileOutputStream(g);
                        outsy = new BufferedOutputStream(outsfile);
                        if(x==0){
                            try {
                                outsy.write(str.getBytes());
                                outsy.flush();
                            }
                            catch(IOException exc){
                                throw new IOException("Output File Not Found");
                            }
                        }
                        if(x==1){
                            try {
                                CipherObject.encrypt(fis, outsfile);
                            }
                            catch(Exception exc){
                                throw new IOException(exc.getMessage(),exc);
                            }
                        }
                        if(x==2){
                            try {
                                CipherObject.decrypt(fis, outsfile);
                            }
                            catch(IOException exc){
                                throw new IOException(exc.getMessage(),exc);
                            }
                        }
                    }
                    catch (IOException exc){
                        throw new IOException("Input File Not Found");
                    }

                    // TODO output result of applying the cipher to a file
                    break;
                case "--save":
                    try {
                        CipherObject.save(new FileOutputStream(args[pos++]));
                    }
                    catch(IOException exc){
                        throw new IOException(exc.getMessage(),exc);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("No Output Option Inputted");
            }
        }
        return pos;
    }
}