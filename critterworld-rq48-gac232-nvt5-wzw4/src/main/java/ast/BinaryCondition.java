package ast;

import parse.TokenType;
import java.util.List;

/** A node that represents a binary Boolean condition: 'and' or 'or' in a critter program.*/
public class BinaryCondition extends Condition {

    private TokenType op;
    private Condition left;
    private Condition right;

    /**
     * Create an AST representation of {@code left op right}.
     *
     * @param op the operation, must be either TokenType.AND or TokenType.OR
     * @param left the left hand side of this condition statement
     * @param right the right hand side of this condition statement
     */
    public BinaryCondition(TokenType op, Condition left, Condition right) {
        assert op.equals(TokenType.AND) || op.equals(TokenType.OR);
        value = op.toString();
        this.op = op;
        this.left = left;
        this.right = right;
        left.parent = this;
        right.parent = this;
    }

    @Override
    public boolean classInv() {
        boolean selfCheck = (op.equals(TokenType.AND) || op.equals(TokenType.OR)) &&
                left != null && right != null &&
                value.equals(op.toString()) &&
                (parent instanceof BinaryCondition || parent instanceof Rule);
        return selfCheck && left.classInv() && right.classInv();
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof BinaryCondition)) return false;
        if(((BinaryCondition)o).op != op) return false;
        if(!((BinaryCondition)o).left.equals(left)) return false;
        if(!((BinaryCondition)o).right.equals(right)) return false;
        return true;
    }

    @Override
    public AbstractNode clone() {
        assert classInv();
        return new BinaryCondition(op,(Condition)left.clone(),(Condition)right.clone());
    }

    @Override
    public List<Node> getChildren() {
        return List.of(left,right);
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb){
        boolean lPrio = left instanceof BinaryCondition &&
                op.equals(TokenType.AND) && ((BinaryCondition) left).op.equals(TokenType.OR);
        boolean rPrio = right instanceof BinaryCondition &&
                (op.equals(TokenType.AND) && ((BinaryCondition) right).op.equals(TokenType.OR) ||
                        op.equals(((BinaryCondition) right).op));

        if(lPrio) sb.append('{');
        left.prettyPrint(sb);
        if(lPrio) sb.append('}');

        sb.append(' ');
        sb.append(value);
        sb.append(' ');

        if(rPrio) sb.append('{');
        right.prettyPrint(sb);
        if(rPrio) sb.append('}');
        return sb;
    }

    @Override
    public boolean setChildren(List<Node> children) {
        if(children.size()!=2) return false;
        if(!(children.get(0) instanceof Condition) || !(children.get(1) instanceof Condition)) return false;
        left = (Condition) children.get(0);
        right = (Condition) children.get(1);
        left.parent = this;
        right.parent = this;
        return true;
    }
}
