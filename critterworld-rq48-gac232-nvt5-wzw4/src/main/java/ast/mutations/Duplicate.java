package ast.mutations;

import ast.*;
import cms.util.maybe.Maybe;

import java.util.ArrayList;
import java.util.List;

/** A Duplicate mutation in a critter program
 * that appends a randomly selected subtree of the right kind to a node's children.
 */
public class Duplicate implements Mutation {
    @Override
    public boolean equals(Mutation m) {
        return m instanceof Duplicate;
    }

    @Override
    public Maybe<Program> apply(Program program, Node node) {
        if(!canApply(node)) return Maybe.none();

        List<Node> children = new ArrayList<>(node.getChildren());
        if(node instanceof Rule){ //node is Rule, select random update from entire ruleset
            List<Node> validNodes = ((AbstractNode)program).findNodesOfPredicate(n -> n instanceof Update);
            AbstractNode duplicatedNode = (AbstractNode) validNodes.get((int)(Math.random() * validNodes.size())).clone();
            if(children.getLast() instanceof Action) children.add(children.size()-1,duplicatedNode);
            else children.add(duplicatedNode);
        }else{ //node is a ProgramImpl, select random rule
            children.add(node.getChildren().get((int)(Math.random() * node.getChildren().size())).clone());
        }

        ((AbstractNode)node).setChildren(children);

        return Maybe.some(program);
    }

    @Override
    public boolean canApply(Node n) {
        //For nodes with a variable number of children, a randomly selected subtree of the right type
        //is appended to the end of the list of children.
        return n instanceof ProgramImpl || (n instanceof Rule && n.getChildren().size() > (n.getChildren().getLast() instanceof Action ? 2 : 1));
    }
}
