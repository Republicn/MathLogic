package expression.operations;

import expression.Expression;
import expression.Derivation;

public class Conjunction extends AbstractBinaryOperation {

    public Conjunction(Expression left, Expression right) {
        super("&", left, right);
    }

    @Override
    protected char operation(char first, char second) {
        if ((first == '1') && (second == '1')) {
            proof = Derivation.UterranceA(getLeft().normalString(), getRight().normalString());
            return '1';
        } else if ((first == '1') && (second == '0'))  {
            proof = Derivation.UterranceC(getLeft().normalString(), getRight().normalString());
        } else if (second == '1') {
            proof = Derivation.UterranceB(getLeft().normalString(), getRight().normalString());
        } else {
            proof = Derivation.UterranceD(getLeft().normalString(), getRight().normalString());
        }
        return '0';
    }

}
