package ast.mutations;

import ast.*;
import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;
import parse.TokenType;

import java.util.*;

/**
 * A Transform mutation in a critter program,
 * changes a specified to a random newly created node of some kind, but its children remain the same.
 */
public class Transform implements Mutation {
    @Override
    public boolean equals(Mutation m) {
        return m instanceof Transform;
    }

    @Override
    public Maybe<Program> apply(Program program, Node node) {
        if (!canApply(node)) return Maybe.none();

        Node parent;
        try {parent = ((AbstractNode) node).getParent().get();
        } catch (NoMaybeValue e) {throw new RuntimeException(e);}

        List<Node> nodeChildren = new ArrayList<>(node.getChildren());
        AbstractNode newNode;

        switch (node) {
            case Action action -> {
                if(nodeChildren.size()==1) return Maybe.some(program); //Action type = SERVE. Cannnot transform to anything but itself

                TokenType randomToken = new TokenType[]{TokenType.WAIT, TokenType.FORWARD,
                        TokenType.BACKWARD, TokenType.LEFT, TokenType.RIGHT, TokenType.EAT,
                        TokenType.ATTACK, TokenType.GROW, TokenType.BUD, TokenType.MATE}[(int)(Math.random() * 10)];

                //generate new Action with null expr since Action type != SERVE
                newNode = new Action(randomToken,null);
            }
            case RelationCondition relationCondition -> {
                TokenType randomToken = new TokenType[]{TokenType.LT,
                        TokenType.LE, TokenType.EQ, TokenType.GE,
                        TokenType.GT, TokenType.NE}[(int)(Math.random() * 6)];

                newNode = new RelationCondition(randomToken, (Expr) nodeChildren.get(0), (Expr) nodeChildren.get(1));
            }
            case BinaryCondition binaryCondition -> {
                TokenType randomToken = new TokenType[]{TokenType.AND, TokenType.OR}[(int)(Math.random() * 2)];

                newNode = new BinaryCondition(randomToken, (Condition) nodeChildren.get(0), (Condition) nodeChildren.get(1));
            }
            case Term term -> {
                TokenType randomToken = new TokenType[]{TokenType.PLUS,
                        TokenType.MINUS,TokenType.MUL,TokenType.DIV,
                        TokenType.MOD}[(int)(Math.random() * 5)];

                newNode = new Term(randomToken, (Expr) nodeChildren.get(0), (Expr) nodeChildren.get(1));
            }
            default -> { // Factor
                if(((Factor)node).getNumType().equals(Factor.NumberType.num))
                    newNode = new Factor(Math.abs(Integer.MAX_VALUE/new Random().nextInt()));
                else if(nodeChildren.size()==1) {
                    TokenType randomToken = new TokenType[]{TokenType.MEM,TokenType.NEARBY
                            ,TokenType.AHEAD,TokenType.RANDOM}[(int)(Math.random() * 4)];
                    newNode = new Factor(randomToken, (Expr) nodeChildren.getFirst());
                }else{
                    return Maybe.some(program);
                }
            }
        }

        List<Node> children = new ArrayList<>(parent.getChildren());

        int nodeIndex = -1;
        for(int i=0;i<children.size();i++) if (children.get(i) == node) {
            nodeIndex = i;
            break;
        }
        children.set(nodeIndex,newNode);//replace parent's reference to node with a reference to newNode
        ((AbstractNode) parent).setChildren(children);

        //disconnect node and its parent :(
        ((AbstractNode) node).setParent(null);
        return Maybe.some(program);
    }

    @Override
    public boolean canApply(Node n) {
        //The node is replaced with a random, newly created node of the same kind
        return (n instanceof Action) ||
                (n instanceof BinaryCondition) ||
                (n instanceof RelationCondition) ||
                (n instanceof Factor) ||
                (n instanceof Term);
    }
}
