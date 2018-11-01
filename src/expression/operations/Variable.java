package expression.operations;

import expression.Expression;

import java.util.*;

public class Variable implements Expression {
    private String symb;
    public List<String> proof;

    @Override
    public List<String> getProof() {
        return proof;
    }

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
    public char apply(Set<String> set) {
        if (set.contains(symb)) {
            proof = Collections.singletonList(symb);
            return '1';
        }
        proof = Collections.singletonList("!" + symb);
        return '0';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return Objects.equals(symb, variable.symb);
    }
}
