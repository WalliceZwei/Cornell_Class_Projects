package ast;

import parse.TokenCategory;
import parse.TokenType;

import java.util.List;

/** An AST node that represents the relation condition: '<' '<=' '=' '>=' '>' '!=' */
public class RelationCondition extends Condition {

    private TokenType op;
    private Expr left;
    private Expr right;

    /**
     * Create an AST representation of {@code lest op right}.
     *
     * @param left the left hand expression of this condition statement
     * @param op must be of TokenCategory.RELOP
     * @param right the right hand expression of this condition statement
     */
    public RelationCondition(TokenType op, Expr left, Expr right) {
        assert op.category().equals(TokenCategory.RELOP);
        value = op.toString();
        this.op = op;
        this.left = left;
        this.right = right;
        left.parent = this;
        right.parent = this;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof RelationCondition)) return false;
        if(((RelationCondition)o).op != op) return false;
        if(!((RelationCondition)o).left.equals(left)) return false;
        if(!((RelationCondition)o).right.equals(right)) return false;
        return true;
    }

    @Override
    public Node clone() {
        assert classInv();
        return new RelationCondition(op,(Expr) left.clone(),(Expr) right.clone());
    }

    @Override
    public List<Node> getChildren() {
        return List.of(left,right);
    }

    @Override
    public boolean classInv() {
        boolean selfCheck = op.category().equals(TokenCategory.RELOP) &&
                left != null && right != null &&
                value.equals(op.toString()) &&
                (parent instanceof BinaryCondition || parent instanceof Rule);
        return selfCheck && left.classInv() && right.classInv();
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb){
        //doesn't have space in front of string intentionally
        left.prettyPrint(sb);
        sb.append(' ');
        sb.append(value);
        sb.append(' ');
        right.prettyPrint(sb);
        return sb;
    }

    @Override
    public boolean setChildren(List<Node> children) {
        if(children.size()!=2) return false;
        if(!(children.get(0) instanceof Expr) || !(children.get(1) instanceof Expr)) return false;
        left = (Expr) children.get(0);
        right = (Expr) children.get(1);
        left.parent = this;
        right.parent = this;
        return true;
    }
}
