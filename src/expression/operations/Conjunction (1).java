package expression.operations;

import expression.Expression;

public class Conjunction extends AbstractBinaryOperation {

    public Conjunction(Expression left, Expression right) {
        super("&", left, right);
    }

}
