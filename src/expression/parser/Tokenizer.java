package expression.parser;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private List<Token> tokens;
    private int curr = -1;

    public Tokenizer(String str) {
        tok(str);
    }

    public Token next() {
        return tokens.get(++curr);
    }

    public Token prev() {
        return tokens.get(--curr);
    }

    public boolean hasNext() {
        return curr < tokens.size() - 1;
    }

    public Token curr() {
        return tokens.get(curr);
    }

    private void tok(String str) {
        tokens = new ArrayList<>();
        for (int i = 0; i < str.length(); i++) {
            if (!(Character.isWhitespace(str.charAt(i)))) {
                switch (str.charAt(i)) {
                    case '(':
                        tokens.add(new Token(TokenType.LBR, "("));
                        break;

                    case ')':
                        tokens.add(new Token(TokenType.RBR, ")"));
                        break;

                    case '&':
                        tokens.add(new Token(TokenType.AND, "&"));
                        break;

                    case '|':
                        tokens.add(new Token(TokenType.OR, "|"));
                        break;

                    case '!':
                        tokens.add(new Token(TokenType.NEGATION, "!"));
                        break;

                    case '-':
                        tokens.add(new Token(TokenType.IMPL, "->"));
                        i++;
                        break;

                    default:
                        int j = i;
                        while (j < str.length() && (Character.isLetterOrDigit(str.charAt(j)))) {
                            j++;
                        }
                        tokens.add(new Token(TokenType.VARIABLE, str.substring(i, j)));
                        i = j - 1;
                }
            }
        }
    }
}
