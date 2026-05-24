package ast;

import parse.TokenCategory;
import parse.TokenType;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Term extends Expr {

    private TokenType op;
    private Expr left;
    private Expr right;

    /**
     * Creates an AST representation of a term left _ right
     * where '+' '-' 'x' '/' '%' are all valid expressions
     * @param left the left hand expression of this Term
     * @param op must be of TokenCategory.ADDOP or MULOP
     * @param right the right hand expression of this term
     */
    public Term(TokenType op, Expr left, Expr right) {
        assert op.category().equals(TokenCategory.ADDOP) || op.category().equals(TokenCategory.MULOP);
        value = op.toString();
        this.op = op;
        this.left = left;
        this.right = right;
        left.parent = this;
        right.parent = this;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Term)) return false;
        if(((Term)o).op != op) return false;
        if(!((Term)o).left.equals(left)) return false;
        if(!((Term)o).right.equals(right)) return false;
        return true;
    }

    @Override
    public Node clone() {
        assert classInv();
        return new Term(op, (Expr)left.clone(), (Expr)right.clone());
    }

    @Override
    public List<Node> getChildren() {
        return List.of(left,right);
    }

    @Override
    public boolean classInv() {
        boolean selfCheck = left!=null && right!=null &&
                (op.category().equals(TokenCategory.ADDOP) || op.category().equals(TokenCategory.MULOP)) &&
                value.equals(op.toString()) && (
                parent instanceof Expr ||
                parent instanceof RelationCondition ||
                parent instanceof Update ||
                parent instanceof Action);
        return selfCheck && left.classInv() && right.classInv();
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb){
        boolean lPrio = left instanceof Term &&
                op.category().equals(TokenCategory.MULOP) &&
                ((Term) left).op.category().equals(TokenCategory.ADDOP);

        boolean rPrio = false;
        if(right instanceof Term){
            TokenType rop = ((Term) right).op;
            if(op.category().equals(rop.category())){
                rPrio = true;
            }else if(op.category().equals(TokenCategory.MULOP)){
                if(rop.category().equals(TokenCategory.ADDOP)) rPrio = true;
            }
        }

        //doesn't have space in front of string intentionally
        if(lPrio) sb.append('(');
        left.prettyPrint(sb);
        if(lPrio) sb.append(')');

        sb.append(' ');
        sb.append(value);
        sb.append(' ');

        if(rPrio) sb.append('(');
        right.prettyPrint(sb);
        if(rPrio) sb.append(')');
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

    /** Returns true if this term's operation is '*','/', or '%' */
    public boolean isMulop(){ return op.category().equals(TokenCategory.MULOP); }
}
