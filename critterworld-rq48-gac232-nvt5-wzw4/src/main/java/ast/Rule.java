package ast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** An AST node representation of a Rule in a critter program. */
public class Rule extends AbstractNode {

    private Condition condition;
    private Action action;
    private List<Update> updates;

    /**
     * Creates an AST representation of condition --> update[0~n], action
     * 
     * @param condition A {@link Condition} class that, if true, will execute updates and action
     * @param action A {@link Action} that executes after updates
     * @param updates A list of {@link Update} classes that executes in sequence before action
     */
    public Rule(Condition condition, Action action, List<Update> updates) {
        //updates.isEmpty ==> (implies) action is not null
        assert !(updates.isEmpty()) || (action!=null) : "updates and action cannot be both empty";
        value = "-->";
        this.condition = condition;
        this.action = action;
        this.updates = new ArrayList<>(updates);
        condition.parent = this;
        if(action!=null) action.parent = this;
        for(Update u : updates) u.parent = this;
    }

    @Override
    public boolean equals(Object o){
        // checks that the object has the correct type, and all the objects are state equal
        if(!(o instanceof Rule)) return false;
        if(!((Rule)o).condition.equals(condition)) return false;
        if(!Objects.equals(((Rule)o).action,action)) return false;
        if(((Rule)o).updates.size()!=updates.size()) return false;
        for(int i=0;i<updates.size();i++) if(!updates.get(i).equals(((Rule)o).updates.get(i))) return false;
        return true;
    }

    @Override
    public Node clone() {
        assert classInv();
        ArrayList<Update> clonedUpdates = new ArrayList<>(updates.size());
        for(Update u : updates) clonedUpdates.add((Update)u.clone());
        return new Rule((Condition)condition.clone(),(action == null ? null : (Action)action.clone()),clonedUpdates);
    }

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.RULE;
    }

    @Override
    public List<Node> getChildren() {
        if(updates.isEmpty()) return List.of(condition,action);
        boolean hasAction = action!=null;
        List<Node> temp = new ArrayList<>(updates.size()+(hasAction ? 2 : 1));
        temp.add(condition);
        temp.addAll(updates);
        if(hasAction) temp.add(action);
        return List.copyOf(temp);
    }

    @Override
    public boolean classInv() {
        boolean selfCheck = updates!=null &&
                (!updates.isEmpty() || (action != null)) &&
                condition != null && value.equals("-->") &&
                parent instanceof ProgramImpl; //checks self
        if (!selfCheck) return false;
        for(Update u : updates) if(!u.classInv()) return false; //checks update children nodes
        if((action!=null && !action.classInv()) || !condition.classInv()) return false; //check all other children nodes
        return true;
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb){
        //doesn't have space in front of string, has a \n at the end of string
        condition.prettyPrint(sb);
        sb.append(' ');
        sb.append(value);
        for(Update u : updates) u.prettyPrint(sb);
        if(action != null) {
            sb.append(' ');
            action.prettyPrint(sb);
        }
        sb.append(";\n");
        return sb;
    }

    @Override
    public boolean setChildren(List<Node> children) {
        if(children.size()<2) return false;
        if(!(children.getFirst() instanceof Condition)) return false;
        for(Node n : children.subList(1,children.size()-1)) if(!(n instanceof Update)) return false;
        if(!(children.getLast() instanceof Action) && !(children.getLast() instanceof Update)) return false;

        condition = (Condition)children.getFirst();
        condition.parent = this;

        if(children.getLast() instanceof Action){
            action = (Action)children.getLast();
            action.parent = this;
        }else{
            action = null;
        }

        updates.clear();
        for(Node n : children.subList(1,children.size()-1)) {
            updates.add((Update)n);
            ((Update) n).parent = this;
        }

        if(children.getLast() instanceof Update){
            updates.add((Update)children.getLast());
            ((Update) children.getLast()).parent = this;
        }
        return true;
    }
}
