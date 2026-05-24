package ast.mutations;

import ast.*;
import cms.util.maybe.Maybe;

import java.util.ArrayList;
import java.util.List;

/**
 * A Swap mutation in a critter program,
 * that swaps the order of two children nodes in an AST.
 */
public class Swap implements Mutation {
    @Override
    public boolean equals(Mutation m) {
        return m instanceof Swap;
    }

    @Override
    public Maybe<Program> apply(Program program, Node node) {
        if (!canApply(node)) return Maybe.none();

        List<Node> children = new ArrayList<>(node.getChildren());
        int swap1,swap2;
        if (node instanceof Rule) {
            //state the bounds of Update
            int startIndex = 1;
            int endIndex = children.getLast() instanceof Action ? children.size() - 1 : children.size();

            //randomly select 2 valid unique indices
            swap1 = (int)(Math.random() * (endIndex-startIndex)) + startIndex;
            do swap2 = (int)(Math.random() * (endIndex-startIndex)) + startIndex;
            while (swap1==swap2);
        }else if(node instanceof Program) { //Program: swap 2 rules randomly
            //randomly select 2 unique indices
            swap1 = (int)(Math.random() * children.size());
            do swap2 = (int)(Math.random() * children.size());
            while (swap1==swap2);
        }else{ //Condition or Term, guaranteed 2 children
            swap1 = 0;
            swap2 = 1;
        }
        //randomly swap 2 Update children
        Node temp = children.get(swap1);
        children.set(swap1, children.get(swap2));
        children.set(swap2, temp);

        ((AbstractNode) node).setChildren(children);
        return Maybe.some(program);
    }

    @Override
    public boolean canApply(Node n) {
        //Swaps 2 children nodes of same type
        //consider edge cases - (can't swap condition & action within Rule class)
        return (n instanceof Condition) ||
                (n instanceof Term) ||
                (n instanceof Program && n.getChildren().size() > 1) ||
                (n instanceof Rule && n.getChildren().size() >
                        (n.getChildren().getLast() instanceof Action ? 3 : 2));
    }
}
