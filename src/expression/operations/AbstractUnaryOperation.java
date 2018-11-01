package expression.operations;

import expression.Expression;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractUnaryOperation implements Expression {
    private Expression expr;
    private String symb;
    private Integer cache = null;
    public List<String> proof;

    @Override
    public List<String> getProof() {
        return proof;
    }

    public Expression getLeft() {
        return expr;
    }

    public Expression getRight() {
        return expr;
    }

    public String getSymb() {
        return symb;
    }


    AbstractUnaryOperation(String symb, Expression expr) {
        this.expr = expr;
        this.symb = symb;
    }

    @Override
    public String toString() {
        return ("(" + symb + expr.toString() + ")");
    }

    @Override
    public String normalString() {
        return "(" + symb + expr.normalString() + ")";
    }

    @Override
    public int hashCode() {
        return cache != null ? cache : (cache = expr.hashCode() * 239017 + symb.hashCode() * 31);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractUnaryOperation that = (AbstractUnaryOperation) o;
        return Objects.equals(expr, that.expr) &&
                Objects.equals(symb, that.symb);
    }

    protected abstract char operation(char first);

    public char apply(Set<String> set) {return operation(expr.apply(set));}

}
