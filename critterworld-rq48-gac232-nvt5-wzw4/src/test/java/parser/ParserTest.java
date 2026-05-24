package parser;

import ast.Program;

import java.io.*;
import java.util.ArrayList;

import exceptions.SyntaxError;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import parse.*;

import static org.junit.jupiter.api.Assertions.*;

/** This class contains tests for the Critter parser. */
public class ParserTest {

    private static final Parser parser = ParserFactory.getParser();

    /** Checks that a valid critter program is not {@code null} when parsed. */
    @Test
    public void testProgramIsNotNull() {
        InputStream in = ClassLoader.getSystemResourceAsStream("A4files/draw_critter.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
        try {
            Program prog = parser.parse(r);
            assertNotNull(prog, "A valid critter program should not be null.");
        } catch (SyntaxError e) {
            e.printStackTrace();
        }
    }

    @Test
    void testComment() throws SyntaxError {
        ParserFactory.getParser().parse(new StringReader("1 = 1 --> wait; // first comment\n// second comment"));
    }

    /** Checks that parserImpl can parse all files in resource/files/... */
    @Test
    public void testParseFiles() throws SyntaxError{
        String[] fileNames = new String[]{"example-rules","mutated_critter_1","mutated_critter_2","mutated_critter_3","mutated_critter_4","mutated_critter_5","mutated_critter_6","unmutated_critter"};
        for (String fileName : fileNames) {
            InputStream in = ClassLoader.getSystemResourceAsStream("A4files/" + fileName + ".txt");
            Reader r = new BufferedReader(new InputStreamReader(in));
            Parser parser = ParserFactory.getParser();
            Program prog = parser.parse(r);
            assertTrue(prog.classInv());
        }
    }

    /** Tests the ParserImpl for successful parsing
     * using the RandomASTGenerator class and pretty printing
     */
    @RepeatedTest(1000)
    void testParseRandomAST() throws SyntaxError {
        Parser parser = ParserFactory.getParser();
        Program randomAST = RandomASTGenerator.generateRandomAST();
        Program parsedAST = parser.parse(new StringReader(randomAST.toString()));
        Program parsedASTSecond = parser.parse(new StringReader(parsedAST.toString()));
        assertEquals(parsedAST, parsedASTSecond);
    }

    /** Tests the parserImpl for unsuccessful parsing
     * using RnadomASTGenerator and prettyPrinting and altering the
     * pretty-printed string to create a syntax error.
     * Has a very low probability that this test fail if {@link #swapTokens(String)} returns a valid AST string
     */
    @RepeatedTest(10000)
    void testParseRandomASTSyntaxError() throws SyntaxError {
        Parser parser = ParserFactory.getParser();
        Program randomAST = RandomASTGenerator.generateRandomAST();
        String gibberish = swapTokens(randomAST.toString());
        assertThrows(SyntaxError.class,()-> {
            try {
                parser.parse(new StringReader(gibberish));
            }catch(AssertionError ignored){
                throw new SyntaxError(-1,"assertion error");
            }
        });
    }

    /**
     * Uses {@link Tokenizer} to convert String into array of Tokens, then swap
     * around 10 randomly selected tokens so when the tokens are converted back to a String,
     * it has a very low probability of being a valid AST String.
     */
    private String swapTokens(String str){
        ArrayList<Token> tokens = new ArrayList<>();
        Tokenizer t = new Tokenizer(new StringReader(str));
        while (tokens.isEmpty() || tokens.getLast().getType()!=TokenType.EOF){
            tokens.add(t.next());
        }
        final int iteration = 10; //# times to iterate token
        for(int i=0;i<iteration;i++){
            //swaps token randomly
            int randIndex1 = (int)(Math.random()*tokens.size()),randIndex2 = (int)(Math.random()*tokens.size());
            Token temp = tokens.get(randIndex1);
            tokens.set(randIndex1, tokens.get(randIndex2));
            tokens.set(randIndex2, temp);
        }

        StringBuilder sb = new StringBuilder();
        for(Token token : tokens) {
            sb.append(token.toString());
            sb.append(' ');
        }

        return sb.toString();
    }
}
