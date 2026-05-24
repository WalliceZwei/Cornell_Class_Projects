package ast;

import parse.TokenCategory;
import parse.TokenType;

import java.util.List;

/** An AST node representing an Update in a critter program */
public class Update extends AbstractNode {

    private Expr memExpr;
    private Expr expr;

    /**
     * Creates an AST representation of mem[memExpr] := expr
     *
     * @param memExpr The number associated with the index of mem that is being changed
     * @param expr The number that will be set in an index of mem
     */
    public Update(Expr memExpr, Expr expr){
        value = ":=";
        this.memExpr = memExpr;
        this.expr = expr;
        memExpr.parent = this;
        expr.parent = this;
    }

    /**
     * Creates an AST representation of (Abbreviated mem) = expr
     *
     * @param mem The TokenType associated with the index of mem that is being changed
     * @param expr The number that will be set in an index of mem
     */
    public Update(TokenType mem, Expr expr){
        assert mem.category().equals(TokenCategory.MEMSUGAR);
        value = ":=";
        this.memExpr = new Factor(TokenType.memsugarToValue(mem));
        this.expr = expr;
        memExpr.parent = this;
        expr.parent = this;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Update)) return false;
        if(!((Update)o).memExpr.equals(memExpr)) return false;
        if(!((Update)o).expr.equals(expr)) return false;
        return true;
    }

    @Override
    public Node clone() {
        assert classInv();
        return new Update((Expr)memExpr.clone(),(Expr)expr.clone());
    }

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.UPDATE;
    }

    @Override
    public List<Node> getChildren() {
        return List.of(memExpr,expr);
    }

    @Override
    public boolean classInv() {
        boolean checkSelf = memExpr!=null && expr!=null && value.equals(":=") && parent instanceof Rule;
        return checkSelf && memExpr.classInv() && expr.classInv();
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb){
        sb.append(" mem[");
        memExpr.prettyPrint(sb);
        sb.append("] := ");
        expr.prettyPrint(sb);
        return sb;
    }

    @Override
    public boolean setChildren(List<Node> children) {
        if(children.size()!=2) return false;
        if(!(children.get(0) instanceof Expr) || !(children.get(1) instanceof Expr)) return false;
        memExpr = (Expr) children.get(0);
        expr = (Expr) children.get(1);
        memExpr.parent = this;
        expr.parent = this;
        return true;
    }
}
