package expression.operations;

import expression.Derivation;
import expression.Expression;

public class Disjunction extends AbstractBinaryOperation {

    public Disjunction(Expression left, Expression right) {
        super("|", left, right);
    }

    @Override
    protected char operation(char first, char second) {
        if ((first == '0') && (second == '0')) {
            proof = Derivation.UterranceH(getLeft().normalString(), getRight().normalString());
            return '0';
        } else if (first == '1') {
            proof = Derivation.UterranceEandG(getLeft().normalString(), getRight().normalString());
        } else {
            proof = Derivation.UterranceF(getLeft().normalString(), getRight().normalString());
        }
        return '1';
    }
}
