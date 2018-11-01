package expression.parser;

import expression.Expression;
import expression.operations.*;

public class ExpressionParser {
    private Tokenizer tokens;

    public Expression parse(String expression) {
        tokens = new Tokenizer(expression);
        return sh();
    }

    private Expression sh() {
        Expression sh = expr();
        while (tokens.hasNext()) {
            Token operation = tokens.next();
            switch (operation.Type()) {
                case IMPL:
                    sh = new Implication(sh, sh());
                    break;

                default:
                    tokens.prev();
                    return sh;
            }
        }
        return sh;
    }

    private Expression expr() {
        Expression expr = term();
        while (tokens.hasNext()) {
            Token operation = tokens.next();
            switch (operation.Type()) {
                case OR:
                    expr = new Disjunction(expr, term());
                    break;

                default:
                    tokens.prev();
                    return expr;
            }
        }
        return expr;
    }

    private Expression term() {
        Expression term = prim();
        while (tokens.hasNext()) {
            Token operation = tokens.next();
            switch (operation.Type()) {
                case AND:
                    term = new Conjunction(term, prim());
                    break;

                default:
                    tokens.prev();
                    return term;
            }
        }
        return term;
    }

    private Expression prim() {
        Expression prim = null;
        Token token = tokens.next();

        switch (token.Type()) {
            case NEGATION:
                prim = new Negation(prim());
                break;

            case VARIABLE:
                prim = new Variable(token.Value());
                break;

            case LBR:
                prim = sh();
                tokens.next();
                break;
        }
        return prim;
    }
}
