package controller;

import Simulator.Critter;
import Simulator.Tiles;
import ast.Program;
import exceptions.SyntaxError;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import parse.Parser;
import parse.ParserFactory;
import parser.RandomASTGenerator;
import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InterpreterTest {

    /**
     * Uses {@link parser.RandomASTGenerator} to test that the returned index is a valid index of that AST, or -1<br>
     * If returned index is not -1 and valid, check if the condition is true and that the rule contains an action
     */
    @RepeatedTest(1000)
    void testValidRule(){
        Program randomAST = RandomASTGenerator.generateRandomAST();
        Tiles tiles = new Tiles(1,1,false);
        Critter critter = new Critter(randomAST);
        Critter.ASTInterpreter interpreter = critter.new ASTInterpreter(tiles);
        tiles.place(critter,0,0);

        int index = interpreter.interpret();
        assertTrue(index >= -1 && index < randomAST.getChildren().size());
    }


    //mem = new int[]{7,1,1,1,500,1,0};

    //Tests that conditions are correctly interpreted
    @Test
    void testInterpretCondition() throws SyntaxError {
        Reader r = new BufferedReader(new StringReader("mem[0] >= 7 and mem[1] = 1 --> mate;"));
        Parser parser = ParserFactory.getParser();
        Program prog = parser.parse(r);

        Tiles tiles = new Tiles(1,1,false);
        Critter critter = new Critter(prog);
        Critter.ASTInterpreter interpreter = critter.new ASTInterpreter(tiles);

        int index = interpreter.interpret();
        assertEquals(0, index);
    }

    //Test that expressions are correctly interpreted
    @Test
    void testInterpretExpr() throws SyntaxError {
        Reader r = new BufferedReader(new StringReader("mem[0] * 10 = 70 or mem[1] + 4 = 3 --> wait;"));
        Parser parser = ParserFactory.getParser();
        Program prog = parser.parse(r);

        Tiles tiles = new Tiles(1,1,false);
        Critter critter = new Critter(prog);
        Critter.ASTInterpreter interpreter = critter.new ASTInterpreter(tiles);

        int index = interpreter.interpret();
        assertEquals(0, index);
    }

    //Tests that Interpreter is returning index of first true condition with an action
    @Test
    void testInterpret() throws SyntaxError {
        InputStream in = ClassLoader.getSystemResourceAsStream("A5files/Interpreter_program.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));

        Parser parser = ParserFactory.getParser();
        Program prog = parser.parse(r);

        Tiles tiles = new Tiles(1,1,false);
        Critter critter = new Critter(prog);
        Critter.ASTInterpreter interpreter = critter.new ASTInterpreter(tiles);
        tiles.place(critter,0,0);

        int index = interpreter.interpret();
        assertEquals(2, index);
    }




}
