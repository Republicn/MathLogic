package expression.operations;

import expression.Expression;

public class Disjunction extends AbstractBinaryOperation {

    public Disjunction(Expression left, Expression right) {
        super("|", left, right);
    }
}
