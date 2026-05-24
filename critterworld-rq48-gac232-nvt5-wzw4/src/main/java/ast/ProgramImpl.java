package ast;

import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;

import java.util.ArrayList;
import java.util.List;

/**
 * An AST data structure representing a critter program.
 */
public class ProgramImpl extends AbstractNode implements Program {

    /**
     * A list of rules of length > 0
     */
    private List<Rule> rules;

    /**
     * Creates the head (root) of the AST that contains a certain amount of Rule objects
     *
     * @param rules A list of {@link Rule} classes containing at least one Rule class
     */
    public ProgramImpl(List<Rule> rules) {
        assert !rules.isEmpty();
        this.rules = rules;
        for (Rule r : rules) r.parent = this;
        value = "";
    }

    @Override
    public Program mutate() {
        //chooses random mutation
        Mutation mutation;
        int indexOfMutation = -1;

        //chooses random valid node for mutation
        int size = size();
        do {
            indexOfMutation = (int) (Math.random() * size);
            ArrayList<Mutation> mutations = new ArrayList<>(List.of(
                    MutationFactory.getTransform(),
                    MutationFactory.getSwap(),
                    MutationFactory.getRemove(),
                    MutationFactory.getReplace(),
                    MutationFactory.getInsert(),
                    MutationFactory.getDuplicate()));
            do {
                int randomIndex = (int) (Math.random() * mutations.size());
                mutation = mutations.get(randomIndex);
                mutations.remove(randomIndex);
            } while (!mutations.isEmpty() && !mutation.canApply(nodeAt(indexOfMutation)));
        } while (!mutation.canApply(nodeAt(indexOfMutation)));

        Maybe<Program> result = mutate(indexOfMutation, mutation);

        if (result.isEmpty()) System.err.println(mutation);
        assert result.isPresent();
        try {
            return result.get();
        } catch (NoMaybeValue e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Maybe<Program> mutate(int index, Mutation m) {
        Program newProgram = (Program) clone();
        return m.apply(newProgram, newProgram.nodeAt(index));
    }

    @Override
    public Maybe<Node> findNodeOfType(NodeCategory type) {
        ArrayList<Node> Q = new ArrayList<>();
        Q.add(this);
        while (!Q.isEmpty()) {
            Node h = Q.removeFirst();
            if (h.getCategory() == type) {
                return Maybe.some(h);
            }
            for (Node n : h.getChildren()) {
                Q.add(n);
            }
        }
        return Maybe.none();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ProgramImpl)) return false;
        if (((ProgramImpl) o).rules.size() != rules.size()) return false;
        for (int i = 0; i < rules.size(); i++) if (!rules.get(i).equals(((ProgramImpl) o).rules.get(i))) return false;
        return true;
    }

    @Override
    public Node clone() {
        ArrayList<Rule> clonedRules = new ArrayList<>(rules.size());
        for (Rule r : rules) clonedRules.add((Rule) r.clone());
        return new ProgramImpl(clonedRules);
    }

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.PROGRAM;
    }

    @Override
    public List<Node> getChildren() {
        return List.copyOf(rules);
    }

    @Override
    public boolean classInv() {
        boolean selfCheck = rules != null && !rules.isEmpty() && value.isEmpty() && parent == null;
        if (!selfCheck) return false;
        for (Rule r : rules)
            if (!r.classInv()) return false;
        return true;
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb) {
        //todo test tostring. 0 = 0 --> mem[10] := 5 mem[11] := 6 mem[12] := 7 wait;
        //todo find index 1
        for (Rule r : rules) r.prettyPrint(sb);
        return sb;
    }

    @Override
    public boolean setChildren(List<Node> children) {
        if (children.isEmpty()) return false;
        for (Node n : children) if (!(n instanceof Rule)) return false;
        rules.clear();
        for (Node n : children) {
            rules.add(((Rule) n));
            ((Rule) n).parent = this;
        }
        return true;
    }
}
