package expression.operations;

import expression.Expression;

import java.util.Objects;

public class Variable implements Expression {
    private String symb;

    public Variable(String sym) {
        this.symb = sym;
    }


    @Override
    public String toString() {
        return symb;
    }

    @Override
    public Expression getLeft() {
        return null;
    }

    @Override
    public Expression getRight() {
        return null;
    }

    @Override
    public String getSymb() {
        return symb;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String normalString() {
        return symb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return Objects.equals(symb, variable.symb);
    }
}
