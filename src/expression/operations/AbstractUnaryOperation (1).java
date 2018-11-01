package expression.operations;

import expression.Expression;

import java.util.Objects;

public abstract class AbstractUnaryOperation implements Expression {
    private Expression expr;
    private String symb;
    private Integer cache = null;

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
}
