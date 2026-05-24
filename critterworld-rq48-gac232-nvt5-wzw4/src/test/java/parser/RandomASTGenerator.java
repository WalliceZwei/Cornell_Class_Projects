package parser;

import ast.*;
import parse.TokenCategory;
import parse.TokenType;
import java.util.ArrayList;
import java.util.List;

public class RandomASTGenerator {

    /**
     * The maximum number of children you can have for a given node.
     */
    private static int maxWidth = 10;

    /**
     * The number of nodes in the randomly generated tree.
     */
    private static int numberNodes = 10;

    /**
     * Generates a random AST.
     *
     * @param numNodes the number of nodes you want the AST to contain.
     * @param maxChildren the maximum number of direct children you want any given node in the tree to have.
     * @return a ProgramImpl
     */
    public static ProgramImpl generateRandomAST(int numNodes, int maxChildren) {
        maxWidth = maxChildren;
        numberNodes = numNodes;
        return generateProgramNode();
    }

    public static ProgramImpl generateRandomAST() {
        maxWidth = 100;
        numberNodes = 100;
        return generateProgramNode();
    }

    private static ProgramImpl generateProgramNode() {
        int ruleCount = (int) (Math.random() * (maxWidth-1))+1;

        List<Rule> rules = new ArrayList<>();
        for (int i = 0; i < ruleCount; i++) {
            rules.add(generateRuleNode());
        }
        return new ProgramImpl(rules);
    }

    private static Rule generateRuleNode() {
        numberNodes--;
        Condition condition = generateConditionNode();
        int numChild = Math.max(1,(int) (Math.random() * maxWidth/4));
        Action action = null;

        if (Math.random() < 0.9) {
            action = generateActionNode();
            numChild--;
        }

        List<Update> updates = new ArrayList<>();
        for (int i = 0; i < numChild; i++) {
            updates.add(generateUpdateNode());
        }

        return new Rule(condition, action, updates);
    }

    private static Condition generateConditionNode() {
        if (Math.random() < 0.5 && numberNodes-- > 0) {
            Condition left = generateConditionNode();
            Condition right = generateConditionNode();
            TokenType operator = Math.random() > 0.5 ? TokenType.AND : TokenType.OR;
            return new BinaryCondition(operator, left, right);
        } else {
            Expr left = generateExprNode();
            Expr right = generateExprNode();
            TokenType operator = getRandomRelOp();
            return new RelationCondition(operator, left, right);
        }
    }

    private static Factor generateFactorNode() {
        if (numberNodes-- > 0) {
            double ranNum = Math.random();
            if (ranNum <= 0.25) return new Factor((int) (100 * Math.random()));
            else if (ranNum <= 0.5) return new Factor(generateExprNode());
            else if (ranNum <= 0.75) return new Factor(getRandomMemSugarToken());
            else {
                TokenType token = new TokenType[]{
                        TokenType.MEM,
                        TokenType.NEARBY,
                        TokenType.AHEAD,
                        TokenType.RANDOM,
                        TokenType.SMELL,
                        TokenType.PING
                }[(int) (Math.random() * 5)];
                return new Factor(token, (token.equals(TokenType.SMELL) || token.equals(TokenType.PING)) ? null : generateExprNode());
            }
        }
        return new Factor((int)(100 * Math.random()));
    }

    private static Update generateUpdateNode() {
        numberNodes--;
        Expr expr1 = generateExprNode();
        Expr expr2 = generateExprNode();
        return new Update(expr1, expr2);
    }

    private static Action generateActionNode() {
        TokenType actionType = getRandomActionToken();
        Expr actionExpr = null;

        if (actionType.equals(TokenType.SERVE)) {
            if(numberNodes-- > 0) actionExpr = generateExprNode();
            else while (actionType.equals(TokenType.SERVE)) actionType = getRandomActionToken();
        }

        return new Action(actionType, actionExpr);
    }

    private static Expr generateExprNode() {
        TokenType operator = Math.random() > 0.5 ?
                new TokenType[]{TokenType.PLUS,TokenType.MINUS}[(int)(Math.random()*2)] :
                new TokenType[]{TokenType.MUL,TokenType.DIV,TokenType.MOD}[(int)(Math.random()*3)];

        if(numberNodes--<0) return new Term(operator,generateFactorNode(),generateFactorNode());

        Expr left = Math.random() > 0.3 ? generateFactorNode() : generateExprNode();
        Expr right = Math.random() > 0.3 ? generateFactorNode() : generateExprNode();
        return new Term(operator, left, right);
    }

    private static TokenType getRandomRelOp() {
        TokenType[] operators = {
                TokenType.LT,
                TokenType.LE,
                TokenType.EQ,
                TokenType.GE,
                TokenType.GT,
                TokenType.NE
        };
        return operators[(int) (Math.random() * operators.length)];
    }

    private static TokenType getRandomMemSugarToken() {
        return new TokenType[]{
                TokenType.ABV_MEMSIZE,
                TokenType.ABV_DEFENSE,
                TokenType.ABV_OFFENSE,
                TokenType.ABV_SIZE,
                TokenType.ABV_ENERGY,
                TokenType.ABV_PASS,
                TokenType.ABV_POSTURE,
        }[(int) (Math.random() * 7)];
    }

    private static TokenType getRandomActionToken() {
        return new TokenType[]{
                TokenType.FORWARD,
                TokenType.BACKWARD,
                TokenType.LEFT,
                TokenType.RIGHT,
                TokenType.EAT,
                TokenType.ATTACK,
                TokenType.GROW,
                TokenType.BUD,
                TokenType.MATE,
                TokenType.SERVE,
                TokenType.WAIT,
        }[(int) (Math.random() * 11)];
    }

    // Public Functions
    public static Rule generateRuleNode(int numNodes, int maxChildren) {
        maxWidth = maxChildren;
        numberNodes = numNodes;
        Rule child = generateRuleNode();
        ProgramImpl parent = new ProgramImpl(List.of(child));
        child.setParent(parent);
        return child;
    }

    public static Condition generateConditionNode(int numNodes, int maxChildren) {
        maxWidth = maxChildren;
        numberNodes = numNodes;
        Condition child = generateConditionNode();
        BinaryCondition parent = new BinaryCondition(TokenType.AND,child,child);
        child.setParent(parent);
        return child;
    }

    public static Factor generateFactorNode(int numNodes, int maxChildren) {
        maxWidth = maxChildren;
        numberNodes = numNodes;
        Factor parent = new Factor(3);
        Factor child = generateFactorNode();
        child.setParent(parent);
        return child;
    }

    public static Update generateUpdateNode(int numNodes, int maxChildren) {
        maxWidth = maxChildren;
        numberNodes = numNodes;
        Update child = generateUpdateNode();
        Rule parent = new Rule(new RelationCondition(TokenType.LT,new Factor(3),new Factor(4))
                ,null,List.of(child));
        return child;
    }

    public static Action generateActionNode(int numNodes, int maxChildren) {
        maxWidth = maxChildren;
        numberNodes = numNodes;
        Action child = generateActionNode();
        Rule parent = new Rule(new RelationCondition(TokenType.LT,new Factor(1),new Factor(1)),
                child,
                List.of());
        child.setParent(parent);
        return child;
    }
    public static Expr generateExprNode(int numNodes, int maxChildren) {
        maxWidth = maxChildren;
        numberNodes = numNodes;
        Expr child = generateExprNode();
        Term parent = new Term(TokenType.PLUS,child,child);
        child.setParent(parent);
        return child;
    }
}
