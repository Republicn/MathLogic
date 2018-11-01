package expression.operations;

import expression.Expression;

public class Implication extends AbstractBinaryOperation {

    public Implication(Expression left, Expression right) {
        super("->", left, right);
    }

}
