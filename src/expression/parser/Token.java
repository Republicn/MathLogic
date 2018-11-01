package expression.parser;

enum TokenType {
    NEGATION, VARIABLE, IMPL, AND, OR, LBR, RBR
}

public class Token {
    private TokenType type;
    private String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public TokenType Type() {
        return type;
    }

    public String Value() {
        return value;
    }
}
