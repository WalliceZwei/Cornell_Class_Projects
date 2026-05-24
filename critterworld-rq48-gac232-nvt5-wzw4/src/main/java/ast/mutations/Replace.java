package ast.mutations;

import ast.*;
import ast.Action;
import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;

import java.util.ArrayList;
import java.util.List;

/**
 * A Replace mutation in a critter program
 * that replaces a specified node in an AST with a randomly selected subtree.
 */
public class Replace implements Mutation {
    @Override
    public boolean equals(Mutation m) {
        return m instanceof Replace;
    }

    @Override
    public Maybe<Program> apply(Program program, Node node) {
        if(!canApply(node)) return Maybe.none();
        if(program == node) return Maybe.some(program); //only valid solution is replace root node with itself

        AbstractNode parent;
        try{parent = (AbstractNode) ((AbstractNode) node).getParent().get();}
        catch (NoMaybeValue e) {throw new RuntimeException(e);}

        int nodeIndex = parent.getChildren().indexOf(node);
        List<Node> validNodes;
        if(parent instanceof ProgramImpl){
            validNodes = parent.getChildren();
        }else if (parent instanceof Rule){
            if(node instanceof Condition) validNodes = ((AbstractNode)program).findNodesOfPredicate((n)->(n instanceof Condition));
            else validNodes = ((AbstractNode)program).findNodesOfPredicate((n)->(n.getClass().equals(node.getClass())));
        }else if (parent instanceof BinaryCondition) {
            validNodes = ((AbstractNode)program).findNodesOfPredicate((n) -> (n instanceof Condition));
        }else if (parent instanceof RelationCondition || parent instanceof Update ||
                parent instanceof Action || parent instanceof Term || parent instanceof Factor){
            validNodes = ((AbstractNode)program).findNodesOfPredicate((n) -> (n instanceof Expr));
        }else{
            return Maybe.some(program);
        }
        List<Node> children = new ArrayList<>(parent.getChildren());
        Node randomlyChosen = validNodes.get((int)(Math.random()*validNodes.size()));
        children.set(nodeIndex, randomlyChosen.clone());
        parent.setChildren(children);

        return Maybe.some(program);
    }

    @Override
    public boolean canApply(Node n) {
        //The node and its descendants are replaced with a randomly selected subtree of the right kind.
        //There always exists a subtree for any node in a well-formed AST (the subtree of the node itself)
        return true;
    }
}
