package ast;

import java.util.List;
import java.util.ArrayList;
import cms.util.maybe.Maybe;
import java.lang.String;
import java.util.function.Predicate;

/** Base abstract class for all AST nodes in a critter program (AST). */
public abstract class AbstractNode implements Node {

    protected String value = "";
    protected AbstractNode parent = null;

    @Override
    public String toString() {
        return prettyPrint(new StringBuilder()).toString();
    }

    @Override
    public int size() {
        int sum = 1;
        for (Node n : getChildren()) sum += n.size();
        return sum;
    }

    @Override
    public Node nodeAt(int index) {
        if(index < 0) throw new IndexOutOfBoundsException();
        ArrayList<Node> Queue = new ArrayList<>();
        Queue.add(this);
        while (!Queue.isEmpty()) {
            Node h = Queue.removeFirst();
            if (index-- <= 0) return h;
            Queue.addAll(h.getChildren());
        }
        throw new IndexOutOfBoundsException(); // throws exception when precondition is not satisfied
    }

    /**
     * Searches the subtree of this {@code Node} and returns a list of {@code Node}
     * that satisfies the given predicate class.<br>
     * If no such node exists, returns an empty list
     *
     * @param predicate
     * @return
     */
    public List<Node> findNodesOfPredicate(Predicate<Node> predicate) {
        ArrayList<Node> validNodes = new ArrayList<>();
        ArrayList<Node> Q = new ArrayList<>(getChildren());
        while (!Q.isEmpty()) {
            Node h = Q.removeFirst();
            if (predicate.test(h)) {
                validNodes.add(h);
            }
            Q.addAll(h.getChildren());
        }
        return validNodes;
    }

    /**
     * Returns the parent of this {@code Node}, or {@Maybe.none} if this {@code Node} is the root.
     *
     * @return the parent of this {@code Node}, or {@Maybe.none} if this {@code Node} is the root.
     * <p>
     * This method does not need to be implemented and may be removed from the interface.
     */
    public Maybe<Node> getParent() {
        if (parent == null) {
            return Maybe.none();
        }
        return Maybe.some(parent);
    }

    @Override
    public abstract Node clone();

    public void setParent(AbstractNode parent) {
        this.parent = parent;
    }

    public String getValue(){return value;}

    /**
     * Sets the children of this {@code Node} in the same order as getChildren().
     * If {@code children} is invalid for this current {@code Node}, nothing changes
     *
     * @param children A list of the children this {@code Node}.
     * @return true if children is successfully changed, false otherwise
     */
    public abstract boolean setChildren(List<Node> children);
}
