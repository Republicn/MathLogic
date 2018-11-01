package expression;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public interface Expression {
    Expression getLeft();

    Expression getRight();

    String getSymb();

    String normalString();

    char apply(Set<String> set);

    List<String> getProof();
}