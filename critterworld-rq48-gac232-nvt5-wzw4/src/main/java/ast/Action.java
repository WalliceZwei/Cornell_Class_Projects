package ast;

import Simulator.CritterAction;
import org.w3c.dom.css.CSSImportRule;
import parse.TokenCategory;
import parse.TokenType;

import java.util.FormattableFlags;
import java.util.List;
import java.util.Objects;

/**
 * An AST Action node that represents the actions: <i>wait, forward, backward, left, right, eat, attack,
 * grow, bud, mate, serve [expr]</i> in a critter program.
 */
public class Action extends AbstractNode {

    private TokenType type;
    private Expr expr;

    /**
     * Creates an Action AST node.
     *
     * @param type the Action type, must be of category {@code TokenCategory.ACTION}
     * @param expr the Action expression, must be null except when type is {@code TokenType.SERVE}
     */
    public Action(TokenType type, Expr expr){
        assert type.category().equals(TokenCategory.ACTION); // checks type precondition
        assert type.equals(TokenType.SERVE) == (expr != null); // checks expr precondition
        value = type.toString();
        this.type = type;
        this.expr = expr;
        if(type.equals(TokenType.SERVE)) expr.parent = this;
    }

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.ACTION;
    }

    @Override
    public List<Node> getChildren() {
        return (expr == null) ? List.of() : List.of(expr);
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Action)) return false;
        if(((Action)o).type !=type) return false;
        return Objects.equals(((Action) o).expr, expr);
    }

    @Override
    public Node clone() {
        assert classInv();
        return new Action(type,(expr == null ? null : (Expr) expr.clone()));
    }

    @Override
    public boolean classInv() {
        boolean checkSelf = type!=null &&
                (type.equals(TokenType.SERVE) == (expr != null)) && // (type = SERVE) <==> (expr is not null)
                value.equals(type.toString()) &&
                parent instanceof Rule;
        return checkSelf && (expr == null || expr.classInv());
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb){
        if(type.equals(TokenType.SERVE)){
            sb.append(value);
            sb.append('[');
            expr.prettyPrint(sb);
            sb.append(']');
        }else{
            sb.append(value);
        }
        return sb;
    }

    @Override
    public boolean setChildren(List<Node> children) {
        if(children.size()!=1 || !(children.get(0) instanceof Expr)) return false;
        expr = (Expr) children.get(0);
        expr.parent = this;
        return true;
    }
}
