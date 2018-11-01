package expression;

public interface Expression {
    Expression getLeft();

    Expression getRight();

    String getSymb();

    String normalString();

}