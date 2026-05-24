package ast;


import parse.TokenCategory;
import parse.TokenType;

import java.util.List;
import java.util.Objects;

/** An AST node that represents a Factor in a critter program.*/
public class Factor extends Expr {

    /** Represents the (potential) child of this node<br>
     * Is null when this node has no children */
    private Expr expr;

    /** Represents the type of Factor this node is. (i.e. has child? stores num? inverse? etc.) */
    private NumberType numType;

    /** The different kinds of NumberTypes a Factor can store */
    public enum NumberType {
        /** Has no child node, value is the number {@code num} */
        num,
        /** Has a child node (expr) whose value is the negative sign - */
        inv,
        /** Has a child node (expr) except when value is smell or ping, in which case has no child node.
         * Value of expression is mem, nearby, smell, ping, etc */
        args
    }

    /**
     * An AST representation of a positive integer number {@code num}
     * @param num must be greater than or equal to zero.
     */
    public Factor(int num){
        assert num >= 0;
        value = Integer.toString(num);
        expr = null;
        numType = NumberType.num;
    }

    /**
     * An AST representation of a number in the form of mem[expr], nearby[expr], ahead[expr], random[expr],
     * or smell depending on parameter type
     *
     * @param type the type of the number, must be mem or of TokenCategory.SENSOR
     * @param expr the expression, must be null when {@code type} is smell or ping
     */
    public Factor(TokenType type, Expr expr) {
        assert type.equals(TokenType.MEM) || type.category().equals(TokenCategory.SENSOR);
        this.expr = expr;
        if(!type.equals(TokenType.SMELL) && !type.equals(TokenType.PING)) expr.parent = this;
        value = type.toString();
        numType = NumberType.args;
    }

    /**
     * An AST representation of a number somewhere in mem array
     *
     * @param mem must be TokenCategory of MEMSUGAR
     */
    public Factor(TokenType mem){
        assert mem.category().equals(TokenCategory.MEMSUGAR);
        numType = NumberType.args;
        value = "mem";
        expr = new Factor(TokenType.memsugarToValue(mem));
        expr.parent = this;
    }

    /**
     * An AST representation of a number in the form of -expr
     * @param expr
     */
    public Factor(Expr expr){
        numType = NumberType.inv;
        value = "-";
        this.expr = expr;
        expr.parent = this;
    }

    @Override
    public boolean setChildren(List<Node> children) {
        if(children.size()!=1) return false;
        if(children.get(0) instanceof Expr && (numType==NumberType.inv || numType == NumberType.args))
            expr = (Expr) children.get(0);
        else return false;
        expr.parent = this;
        return true;
    }

    public NumberType getNumType(){ return numType; }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Factor)) return false;
        if(((Factor)o).numType != numType) return false;
        if(!Objects.equals(((Factor)o).expr,expr)) return false;
        if (!((Factor)o).value.equals(value)) return false;
        return true;
    }

    @Override
    public Node clone() {
        assert classInv();
        switch (numType) {
            case num -> {
                return new Factor(Integer.parseInt(value));
            }
            case inv -> {
                return new Factor((Expr) expr.clone());
            }
            case args -> {
                return new Factor(TokenType.getTypeFromString(value), expr == null ? null : (Expr) expr.clone());
            }
            default ->
                throw new RuntimeException("Unknown number type: " + numType);
        }
    }

    @Override
    public List<Node> getChildren() {
        if(numType.equals(NumberType.num) || value.equals("smell") || value.equals("ping")) return List.of();
        return List.of(expr);
    }

    @Override
    public boolean classInv() {
        boolean selfCheck;
        switch (numType) {
            case num -> { //value must be a parsable int
                boolean checkVal;
                try{
                    Integer.parseInt(value);
                    checkVal = true;
                }catch(NumberFormatException e){
                    checkVal = false;
                }
                selfCheck = checkVal && expr == null;
            }
            case inv -> selfCheck = expr != null  && value.equals("-");
            case args -> {
                TokenType tt = TokenType.getTypeFromString(value);
                selfCheck = (tt.equals(TokenType.MEM) || tt.category().equals(TokenCategory.SENSOR)) &&
                        ((tt.equals(TokenType.SMELL) || tt.equals(TokenType.PING)) == (expr == null));
            }
            default -> selfCheck = false;
        }
        selfCheck = selfCheck && (
                        parent instanceof Expr ||
                        parent instanceof RelationCondition ||
                        parent instanceof Update ||
                        parent instanceof Action);
        //checks valid parents
        return selfCheck && (expr == null || expr.classInv());
    }

    @Override
    public StringBuilder prettyPrint(StringBuilder sb){
        //doesn't have space in front of string intentionally
        switch (numType) {
            case num -> {
                sb.append(value);
            }
            case inv -> {
                sb.append("-");
                boolean isTerm = expr instanceof Term;
                if(isTerm)sb.append('(');
                expr.prettyPrint(sb);
                if(isTerm)sb.append(')');
            }
            case args -> {
                sb.append(value);
                if(!value.equals("smell") && !value.equals("ping")){
                    sb.append('[');
                    expr.prettyPrint(sb);
                    sb.append(']');
                }
            }
        }
        return sb;
    }
}
