package coding;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Q10 {
    /**
     * Returns a BufferedReader that reads the input from a file.
     * Throws a {@code FileNotFoundException} if the file does not exist.
     *
     * @param filename The name of the file to read
     */
    public static BufferedReader openFile(String filename)
            throws FileNotFoundException {
        // A BufferedReader is an implementation of the Reader interface that
        // provides the efficient reading of input in bigger chunks from an
        // underlying Reader.  It then delivers the input character by
        // character.  Without BufferedReader, our application would interact
        // with the file system for each character read, which would typically
        // be much slower.
        return new BufferedReader(new FileReader(filename));
    }

    public static void main(String[] args) {
        // Note: args contains the command-line arguments to the program.
        // These can be passed on the command line or, in IntelliJ,
        // by going to Edit Configurations and setting the Program arguments.
        if (args.length != 1) {
            System.err.println("Usage: java coding.Q10 <filename>");
            return;
        }
        // A Scanner is a simple text scanner which can parse primitive types
        // and strings.  System.in is application's 'standard input', which is
        // normally the console input, but can be redirected to read from
        // other sources such as files.
        Scanner s = new Scanner(System.in);
        // Don't change this message for grading reasons
        System.out.print("Enter lines to skip: ");

        int linesSkipped;

        // User enters how many lines they want to skip, and records that amount as long as it is a non-negative integer
        // Asks user to input again if they don't input a non-negative integer
        while(true) {
            try {
                linesSkipped = Integer.parseInt(s.nextLine());
                if (linesSkipped >= 0) {
                    break;
                }
                else{
                    System.out.print("Enter lines to skip: ");
                }
            } catch (Exception exc) {
                System.out.print("Enter lines to skip: ");
            }
        }
        // This is a try-with-resources statement, a feature introduced in
        // Java 7.  Here, f is a resource that will be closed automatically
        // when the statement finishes executing (either normally or
        // abnormally).
        try (BufferedReader f = openFile(args[0])) {
            // Discards the first n lines the user requested
            // if they ever request to get rid of the same or more lines than the file has, then it outputs a blank
            for(int x = 0; x<linesSkipped; x++){
                if (f.readLine() == null){
                    System.out.print("");
                    break;
                }
            }
            // Basically reads the file, and adds the input backwards into a string while indenting to achieve "reversing"
            // Does this until it reaches the end of the file (null readLine)
            String backwardsOutput = "";
            while(true){
                String currentLine = f.readLine();
                if(currentLine == null){
                    break;
                }
                else{
                    backwardsOutput = currentLine+"\n"+backwardsOutput;
                }
            }
            backwardsOutput = backwardsOutput.trim();
            System.out.println(backwardsOutput);
            // TODO output lines in file backwards after skipping
            // a number of lines specified by user.
            // Hint: Use the provided scanner for user input, and
            // use the readLine() method in class BufferedReader
            // to read lines from the file.
        } catch (FileNotFoundException exc) {
            System.err.println("File not found: " + args[0]);
        } catch (IOException exc) {
            System.err.println("IO exception: " + exc.getMessage());
        }
    }
}