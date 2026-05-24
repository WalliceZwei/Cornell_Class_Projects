package ast.mutations;


import ast.*;
import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;
import parse.TokenType;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** An Insert mutation in a critter program
 * that randomly inserts a specified node in an AST.
 */
public class Insert implements Mutation {
    @Override
    public boolean equals(Mutation m) {
        return m instanceof Insert;
    }

    @Override
    public Maybe<Program> apply(Program program, Node node) {
        if (!canApply(node)) return Maybe.none();
        // Only factor(expr num) or factor(factor) can have a random mutated node above it, all other factors
        // can't have a new node above it, because they must be from update, mem[expr], or engages in double nagation (--)

        // Must have a parent
        AbstractNode parent;
        try {
            parent = (AbstractNode) ((AbstractNode)node).getParent().get();
        } catch (NoMaybeValue ev) {
            throw new RuntimeException();
        }

        if (node instanceof Factor || node instanceof Term) {
            boolean makeTerm = Math.random()>0.5;
            if(makeTerm){
                //newNode is Term
                List<Node> validNodes = ((AbstractNode)program).findNodesOfPredicate(n -> n instanceof Expr);
                Node validNode = validNodes.get((int) (Math.random() * validNodes.size())).clone();

                TokenType mathOps = new TokenType[]{
                        TokenType.MINUS,
                        TokenType.PLUS,
                        TokenType.DIV,
                        TokenType.MOD,
                        TokenType.MUL}
                        [(int) (Math.random() * 5)];
                Term newNode = new Term(mathOps, (Expr) node.clone(), (Expr) validNode);
                List<Node> holder = new ArrayList<>(parent.getChildren());
                // modifies and adds new node into children list
                holder.set(holder.indexOf(node), newNode);
                parent.setChildren(holder);
            }else{
                //newNode is Factor
                TokenType args = new TokenType[]{
                        TokenType.MEM,
                        TokenType.NEARBY,
                        TokenType.AHEAD,
                        TokenType.RANDOM}
                        [(int) (Math.random() * 4)];

                Factor newNode = new Factor(args, (Expr) node);
                List<Node> holder = new ArrayList<>(parent.getChildren());
                // modifies and adds new node into children list
                holder.set(holder.indexOf(node), newNode);
                parent.setChildren(holder);
            }
        }
        // same as above
        else if (node instanceof Condition) {
            List<Node> validNodes = ((AbstractNode)program).findNodesOfPredicate(n -> n instanceof Condition);
            Node validNode = validNodes.get((int) (Math.random() * validNodes.size())).clone();

            TokenType andOr = Math.random()<0.5 ? TokenType.AND : TokenType.OR;
            Condition newNode = new BinaryCondition(andOr, (Condition) validNode, (Condition) node);
            List<Node> holder = new ArrayList<>(parent.getChildren());
            holder.set(holder.indexOf(node), newNode);
            parent.setChildren(holder);
        }
        return Maybe.some(program);
    }

    @Override
    public boolean canApply(Node n) {
        //A newly created node is inserted as the parent of the mutated node
        //node's parent must accept all valid classes of node's parent as child nodes
        try {
            n = ((AbstractNode) n).getParent().get();
            return n instanceof Term || n instanceof BinaryCondition || n instanceof Factor;
        } catch (NoMaybeValue e) {
            return false;
        }
    }
}