package parse;

import ast.Factor;
import ast.*;
import exceptions.SyntaxError;

import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/** A Parser implementation. */
public class ParserImpl implements Parser {

    @Override
    public Program parse(Reader r) throws SyntaxError {
        Tokenizer t = new Tokenizer(r);
        return parseProgram(t);
    }

    /**
     * Parses a program from the stream of tokens provided by the Tokenizer, consuming tokens
     * representing the program. All following methods with a name {@code parseX} have the same spec
     * except that they parse syntactic form X.
     *
     * @return the created AST
     * @throws SyntaxError if input tokens have invalid syntax
     */
    //Calls parseRule
    public static ProgramImpl parseProgram(Tokenizer t) throws SyntaxError {
        List<Rule> rules = new LinkedList<>();
        while (t.hasNext()) {
            rules.add(parseRule(t));
        }
        if(rules.isEmpty()) throw new SyntaxError(t.lineNumber(),"Cannot parse Program with no rules");
        return new ProgramImpl(rules);
    }

    //Calls parseCondition, parseUpdate, and parseAction
    public static Rule parseRule(Tokenizer t) throws SyntaxError {
        Condition c = parseCondition(t);
        consume(t,TokenType.ARR);
        ArrayList<Update> updates = new ArrayList<>();
        Action action = null;
        while (!t.peek().getType().equals(TokenType.SEMICOLON)) {
            if(t.peek().getType().equals(TokenType.MEM) || t.peek().isMemSugar()){
                updates.add(parseUpdate(t));
            }else{
                action = parseAction(t);//todo throw syntax error if more than 1 action
            }
        }
        consume(t,TokenType.SEMICOLON);
        return new Rule(c,action,updates);
    }

    //Calls parseExpression
    public static Condition parseCondition(Tokenizer t) throws SyntaxError {
        Condition c = parseConjunction(t);
        while (t.peek().getType().equals(TokenType.OR)) {
            consume(t,TokenType.OR);
            c = new BinaryCondition(TokenType.OR, c, parseConjunction(t));
        }
        return c;
    }

    //Added Function
    //Calls parseRelation
    public static Condition parseConjunction(Tokenizer t) throws SyntaxError {
        Condition e = parseRelation(t);
        while (t.peek().getType().equals(TokenType.AND)){
            consume(t,TokenType.AND);
            e = new BinaryCondition(TokenType.AND,e,parseRelation(t));
        }
        return e;
    }

    //Added Function
    public static Condition parseRelation(Tokenizer t) throws SyntaxError {
        if(t.peek().getType().equals(TokenType.LBRACE)){
            consume(t,TokenType.LBRACE);
            Condition c = parseCondition(t);
            consume(t,TokenType.RBRACE);
            return c;
        }else {
            Expr left = parseExpression(t);
            TokenType op = consume(t,TokenCategory.RELOP).getType();
            Expr right = parseExpression(t);

            return new RelationCondition(op,left,right);
        }
    }

    //Calls parseTerm and parseFactor
    public static Expr parseExpression(Tokenizer t) throws SyntaxError {
        Expr e = parseTerm(t);
        while (t.peek().isAddOp()) {
            TokenType op = consume(t,TokenCategory.ADDOP).getType();
            e = new Term(op, e, parseTerm(t));
        }
        return e;
    }

    //Calls parseFactor
    public static Expr parseTerm(Tokenizer t) throws SyntaxError {
        Expr e = parseFactor(t);
        while (t.peek().isMulOp()){
            TokenType op = consume(t,TokenCategory.MULOP).getType();
            e = new Term(op,e,parseFactor(t));
        }
        return e;
    }

    //Calls parseExpr
    public static Expr parseFactor(Tokenizer t) throws SyntaxError {
        Token token = t.peek();

        if (token.isSensor()) {
            //Process SENSOR TokenType
            consume(t, TokenCategory.SENSOR);
            if(token.getType().equals(TokenType.SMELL)) return new Factor(TokenType.SMELL,null);
            else if(token.getType().equals(TokenType.PING)) return new Factor(TokenType.PING,null);
            consume(t,TokenType.LBRACKET);
            Expr expr = parseExpression(t);
            consume(t,TokenType.RBRACKET);

            return new Factor(token.getType(),expr);
        } else if (token.isMemSugar()) {
            //Process MEMSUGAR TokenType
            consume(t, TokenCategory.MEMSUGAR);

            return new Factor(token.getType());
        } else {
            switch (token.getType()) {
                case NUM: {
                    //Process number
                    Token numToken = consume(t, TokenType.NUM);
                    assert numToken instanceof Token.NumToken;
                    int val = ((Token.NumToken) numToken).getValue();

                    return new Factor(val);
                }case MEM: {
                    //Process 'mem[expr]'
                    consume(t, TokenType.MEM);
                    consume(t, TokenType.LBRACKET);
                    Expr expr = parseExpression(t);
                    consume(t, TokenType.RBRACKET);

                    return new Factor(TokenType.MEM, expr);
                } case LPAREN: {
                    //Process '(expr)'
                    consume(t, TokenType.LPAREN);
                    Expr expr = parseExpression(t);
                    consume(t, TokenType.RPAREN);

                    return expr;
                }case MINUS: {
                    //Process '-factor'
                    consume(t, TokenType.MINUS);

                    return new Factor(parseFactor(t));
                }default:
                    throw new SyntaxError(t.lineNumber(),"Invalid Token of type " + token.getType());
            }
        }
    }

    //Added Function
    //Calls parseExpr
    public static Update parseUpdate(Tokenizer t) throws SyntaxError {
        //Parse mem[expr] := expr or MEMSUGAR := expr
        if (t.peek().isMemSugar()) {
            TokenType memSugar = t.peek().getType();
            consume(t,TokenCategory.MEMSUGAR);
            consume(t,TokenType.ASSIGN); //:=

            Expr expr = parseExpression(t); //expr
            return new Update(memSugar ,expr);
        } else {
            consume(t,TokenType.MEM); //mem
            consume(t,TokenType.LBRACKET); //[
            Expr memExpr = parseExpression(t); //expr
            consume(t,TokenType.RBRACKET); //]
            consume(t,TokenType.ASSIGN); //:=

            Expr expr = parseExpression(t); //expr
            return new Update(memExpr ,expr);
        }
    }

    //Added Function
    //Calls parseExpr
    public static Action parseAction(Tokenizer t) throws SyntaxError {
        TokenType tt = consume(t, TokenCategory.ACTION).getType();
        if(tt.equals(TokenType.SERVE)){
            consume(t,TokenType.LBRACKET);
            Expr expr = parseExpression(t);
            consume(t,TokenType.RBRACKET);

            return new Action(tt,expr);
        }else{
            return new Action(tt,null);
        }
    }

    /**
     * Consumes a token of the expected type.
     *
     * @throws SyntaxError if the wrong kind of token is encountered.
     */
    public static Token consume(Tokenizer t, TokenType tt) throws SyntaxError {
        return consume(t, (ott)->ott.equals(tt));
    }

    /**
     * Consumes a token of the expected category.
     *
     * @throws SyntaxError if the wrong kind of token is encountered.
     */
    public static Token consume(Tokenizer t, TokenCategory tt) throws SyntaxError {
        return consume(t,(ott)->ott.category().equals(tt));
    }

    /**
     * Consumes a token that satisfies the input predicate.
     *
     * @throws SyntaxError if the wrong kind of token is encountered.
     */
    private static Token consume(Tokenizer t, Predicate<TokenType> predicate) throws SyntaxError {
        Token token = t.peek();
        if (predicate.test(token.getType())) return t.next();
        else if (token.getType().equals(TokenType.EOF))
            throw new SyntaxError(t.lineNumber(), "consume called on end-of-file token");
        else throw new SyntaxError(t.lineNumber(), "consume called on unexpected token type '" + token + "'");
    }
}
