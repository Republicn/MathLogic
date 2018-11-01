package expression.operations;

import expression.Expression;

import java.util.Objects;

public abstract class AbstractBinaryOperation implements Expression {
    private Expression left, right;
    private String symb;
    private Integer cache = null;

    public Expression getRight() {
        return right;
    }

    public String getSymb() {
        return symb;
    }

    public Expression getLeft() {

        return left;
    }

    AbstractBinaryOperation(String symb, Expression left, Expression right) {
        this.left = left;
        this.right = right;
        this.symb = symb;
    }

    @Override
    public String toString() {
        return ("(" + symb + "," + left.toString() + "," + right.toString() + ")");
    }

    @Override
    public String normalString() {
        return "(" + left.normalString() + symb + right.normalString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractBinaryOperation that = (AbstractBinaryOperation) o;
        return Objects.equals(left, that.left) &&
                Objects.equals(right, that.right) &&
                Objects.equals(symb, that.symb);
    }


    @Override
    public int hashCode() {
        return cache != null ? cache : (cache = left.hashCode() * 239017 + right.hashCode() * 17239 + symb.hashCode() * 31);
    }


}
