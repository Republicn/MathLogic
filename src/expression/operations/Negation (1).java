package expression.operations;

import expression.Expression;

public class Negation extends AbstractUnaryOperation {

    Expression expr;
    char symb;

    public Negation(Expression expression) {
        super("!", expression);
    }


}
