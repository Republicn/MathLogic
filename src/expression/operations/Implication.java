package expression.operations;

import expression.Derivation;
import expression.Expression;

public class Implication extends AbstractBinaryOperation {

    public Implication(Expression left, Expression right) {
        super("->", left, right);
    }


    @Override
    protected char operation(char first, char second) {
        if ((first == '1') && (second == '0')) {
            proof = Derivation.UterranceJ(getLeft().normalString(), getRight().normalString());
            return '0';
        } else if (second == '1') {
            proof = Derivation.UterranceIandK(getLeft().normalString(), getRight().normalString());
        } else {
            proof = Derivation.UterranceL(getLeft().normalString(), getRight().normalString());
        }
        return '1';
    }
}
