package parser;

import ast.*;
import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for the RandomASTGenerator */
public class RandomASTGeneratorTest {

    /** Tests the parseExpr function of ParserImpl */
    @RepeatedTest(100)
    void testParseExpr() {
        Expr expr = RandomASTGenerator.generateExprNode(100,100);
        assertTrue(expr.classInv());
    }

    /** Tests the parseFactor function of ParserImpl */
    @RepeatedTest(100)
    void testParseFactor() {
        Factor factor = RandomASTGenerator.generateFactorNode(100,100);
        assertTrue(factor.classInv());
    }

    /** Tests the parseAction function of ParserImpl */
    @RepeatedTest(100)
    void testParseAction() {
        Action action = RandomASTGenerator.generateActionNode(100,100);
        assertTrue(action.classInv());
    }

    /** Tests the parseUpdate function of ParserImpl */
    @RepeatedTest(100)
    void testParseUpdate() {
        Update update = RandomASTGenerator.generateUpdateNode(100,100);
        assertTrue(update.classInv());
    }

    /** Tests the parseCondition function of ParserImpl */
    @RepeatedTest(100)
    void testParseCondition() {
        Condition condition = RandomASTGenerator.generateConditionNode(100,100);
        assertTrue(condition.classInv());
    }

    /** Tests the parseRule function of ParserImpl */
    @RepeatedTest(10000)
    void testParseRule() {
        Rule rule = RandomASTGenerator.generateRuleNode(100,100);
        assertTrue(rule.classInv());
    }

    /** Tests the parseProgram function of ParserImpl */
    @RepeatedTest(10000)
    void testParseProgram() {
        Program program = RandomASTGenerator.generateRandomAST(100,100);
        assertTrue(program.classInv());
    }
}
