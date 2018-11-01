package expression.operations;

import expression.Derivation;
import expression.Expression;

public class Negation extends AbstractUnaryOperation {

    Expression expr;
    char symb;

    public Negation(Expression expression) {
        super("!", expression);
    }


    @Override
    protected char operation(char first) {
        if (first == '1') {
            proof = Derivation.UterranceN(getLeft().normalString(), getRight().normalString());
            return '0';
        }
        proof = getLeft().getProof();
        return '1';
    }
}
