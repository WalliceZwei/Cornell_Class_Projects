package ast.mutations;

import ast.*;
import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;

import java.util.ArrayList;
import java.util.List;

/**
 * A Remove mutation in a critter program
 * that removes or replaces a specified node in an AST, based on its type.
 */
public class Remove implements Mutation {
    @Override
    public boolean equals(Mutation m) {
        return m instanceof Remove;
    }

    @Override
    public Maybe<Program> apply(Program program, Node node) {
        if (!canApply(node)) return Maybe.none();

        AbstractNode parent;
        try {
            parent = (AbstractNode) ((AbstractNode) node).getParent().get();
        } catch (NoMaybeValue e) {
            throw new RuntimeException(e);
        }

        List<Node> children = new ArrayList<>(parent.getChildren());
        int nodeIndex = children.indexOf(node);

        if (node instanceof Rule || node instanceof Update || node instanceof Action) {
            children.remove(nodeIndex);
        } else if (node instanceof BinaryCondition) {
            List<Node> conditions = ((AbstractNode) node).findNodesOfPredicate((n) -> n instanceof Condition);
            children.set(nodeIndex, conditions.get((int) (Math.random() * conditions.size())));
        } else if (node instanceof Term) {
            List<Node> exprs = ((AbstractNode) node).findNodesOfPredicate((n) -> n instanceof Expr);
            children.set(nodeIndex, exprs.get((int) (Math.random() * exprs.size())));
        } else if (node instanceof Factor) {
            children.set(nodeIndex, node.getChildren().get(0));
        }

        parent.setChildren(children);
        return Maybe.some(program);
    }

    @Override
    public boolean canApply(Node n) {
        AbstractNode parent;

        //cannot remove the root
        if (n instanceof ProgramImpl) return false;

        try {
            parent = (AbstractNode) ((AbstractNode) n).getParent().get();
        } catch (NoMaybeValue e) {
            throw new RuntimeException(e);
        }

        switch (n) {
            case Rule rule -> { //cannot be replaced, but parent may not need replacement node
                return parent.getChildren().size() > 1;
            }
            case BinaryCondition binaryCondition -> { //parent always need replacement node
                return true; //children are always Condition
            }
            case RelationCondition relationCondition -> { //parent always need replacement node
                return false; //no Condition descendants
            }
            case Action action -> { //parent may need a replacement node
                return parent.getChildren().size() >= (parent.getChildren().getLast() instanceof Action ? 3 : 2); //no action descendants
            }
            case Update update -> { //parent may need a replacement node
                return parent.getChildren().size() > 2; //no update descendants
            }
            case Term term -> { //parent always need a replacement node
                return true; //children are always Expr
            }
            case Factor factor -> { //parent always need a replacement node
                return !n.getChildren().isEmpty(); //children could be factor or Expr
            }
            default -> {
                return false;
            }
        }
    }
}
