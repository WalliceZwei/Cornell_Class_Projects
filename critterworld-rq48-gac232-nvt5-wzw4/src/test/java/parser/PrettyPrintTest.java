package parser;

import ast.*;
import exceptions.SyntaxError;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import parse.*;

import java.io.StringReader;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrettyPrintTest {

    private static final HashMap<String,String> exprTestLibrary = new HashMap<>();
    private static final HashMap<String,String> conditionTestLibrary = new HashMap<>();
    private static final Parser parser = ParserFactory.getParser();

    static {
        exprTestLibrary.put("-(1 - 2) / (3 * 2)","-(1 - 2) / (3 * 2)");
        exprTestLibrary.put("(2 - 2) / 3 * 2","(2 - 2) / 3 * 2"); //ADDOP vs MULOP test
        exprTestLibrary.put("(3 - 2) * 3 / 2","(3 - 2) * 3 / 2");
        exprTestLibrary.put("4 - 2 / 3 * 2","4 - 2 / 3 * 2"); //ADDOP vs MULOP negative test
        exprTestLibrary.put("4 - (2 / 3) * 2","4 - 2 / 3 * 2"); //redundant parenthesis test

        //correct test cases
        exprTestLibrary.put("(9 - 9) * -(7 + 5)","(9 - 9) * -(7 + 5)");
        exprTestLibrary.put("82 mod (6 - 2)","82 mod (6 - 2)");
        exprTestLibrary.put("9 + 8 * 8","9 + 8 * 8");
        exprTestLibrary.put("-(4 + 1) * (4 + 1)","-(4 + 1) * (4 + 1)");
        exprTestLibrary.put("7 + 6 * 32","7 + 6 * 32");
        exprTestLibrary.put("1 * (3 / 3)","1 * (3 / 3)");
        exprTestLibrary.put("30 / 6 + 92","30 / 6 + 92");
        exprTestLibrary.put("-(9 - (9 * 7) + 5)","-(9 - 9 * 7 + 5)");
        exprTestLibrary.put("9 - 9 * (7 / 5)","9 - 9 * (7 / 5)");

        //remove parenthesis test cases
        exprTestLibrary.put("(9 - (9 * 7) + 5)","9 - 9 * 7 + 5");
        exprTestLibrary.put("9 - (9 * 7) / 5","9 - 9 * 7 / 5");
        exprTestLibrary.put("(82 mod 6) - 2","82 mod 6 - 2");
        exprTestLibrary.put("(9 + 8 * 8)","9 + 8 * 8");
        exprTestLibrary.put("((9) + 8 * 8)","9 + 8 * 8");
        exprTestLibrary.put("(4 + 1 * 4 + 1)","4 + 1 * 4 + 1");
        exprTestLibrary.put("7 - (6 * 32)","7 - 6 * 32");
        exprTestLibrary.put("(1 / 3) mod 3","1 / 3 mod 3");
        exprTestLibrary.put("30 / (6 * 92)","30 / (6 * 92)");

        // New Test Cases
        exprTestLibrary.put("(1 * 3) / 3","1 * 3 / 3");
        exprTestLibrary.put("(9 mod 4) / 2", "9 mod 4 / 2");
        exprTestLibrary.put("9 / (4 mod 2)", "9 / (4 mod 2)");
        exprTestLibrary.put("2 - -5","2 - -5");
        exprTestLibrary.put("2 + -5","2 + -5");
        exprTestLibrary.put("90 mod (4 - -2)", "90 mod (4 - -2)");

        //Condition Tests
        //remove bracket test cases
        conditionTestLibrary.put("{1 = 1 and 1 = 1} or 1 = 1","1 = 1 and 1 = 1 or 1 = 1");
        conditionTestLibrary.put("{1 = 1 and 1 = 1 and 1 = 1} or 1 = 1","1 = 1 and 1 = 1 and 1 = 1 or 1 = 1");
        conditionTestLibrary.put("{1 = 1 or 1 = 1} or 1 = 1","1 = 1 or 1 = 1 or 1 = 1");
        conditionTestLibrary.put("{1 = 1 and 1 = 1} and 1 = 1","1 = 1 and 1 = 1 and 1 = 1");
        conditionTestLibrary.put("{1 = 1 and 1 = 1 and 1 = 1 or 1 = 1}","1 = 1 and 1 = 1 and 1 = 1 or 1 = 1");

        //keep bracket test cases
        conditionTestLibrary.put("1 = 1 and {1 = 1 or 1 = 1}","1 = 1 and {1 = 1 or 1 = 1}");
        conditionTestLibrary.put("1 = 1 and {1 = 1 and 1 = 1}","1 = 1 and {1 = 1 and 1 = 1}");
        conditionTestLibrary.put("1 = 1 and {1 = 1 and 1 = 1 or 1 = 1}","1 = 1 and {1 = 1 and 1 = 1 or 1 = 1}");
        conditionTestLibrary.put("1 = 1 and {1 = 1 or 1 = 1 and 1 = 1}","1 = 1 and {1 = 1 or 1 = 1 and 1 = 1}");
        conditionTestLibrary.put("{1 = 1 or 1 = 1} and 1 = 1","{1 = 1 or 1 = 1} and 1 = 1");
    }

    public static void main(String[] args) {prettyPrintRandomAST();}

    public static void prettyPrintRandomAST(){
        ProgramImpl root = RandomASTGenerator.generateRandomAST();
        System.out.println(root);
    }

    public static void printExpr(){
        Term term = new Term(TokenType.DIV,
                new Term(TokenType.MINUS,new Factor(1),new Factor(2)),
                new Term(TokenType.MUL,new Factor(3),new Factor(2)));
        System.out.println(term);
    }

    @Test
    void testExprPrint() throws SyntaxError {
        for(String input : exprTestLibrary.keySet()){
            Expr expr = ParserImpl.parseExpression(new Tokenizer(new StringReader(input)));
            String output = expr.toString();
            String answer = exprTestLibrary.get(input);
            assertEquals(answer,output);
        }
    }

    @Test
    void testConditionPrint() throws SyntaxError {
        for(String input : conditionTestLibrary.keySet()){
            Condition condition = ParserImpl.parseCondition(new Tokenizer(new StringReader(input)));
            String output = condition.toString();
            String answer = conditionTestLibrary.get(input);
            assertEquals(answer,output.trim());
        }
    }

    @RepeatedTest(1000)
    void testParsablePrint() throws SyntaxError {
        Program root = RandomASTGenerator.generateRandomAST(100,10);
        Program parsedRoot = parser.parse(new StringReader(root.toString()));
        assertEquals(parsedRoot,root);
    }
}
