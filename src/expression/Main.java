package expression;

import expression.parser.ExpressionParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
        try (PrintWriter writer = new PrintWriter("output.txt")) {
            String s = reader.readLine();

            s = reader.readLine();
            while (s != null) {
                Expression expr = new ExpressionParser().parse(s);
                //if (expr.getSymb().equals("->") && expr.getRight().getSymb().equals("->") && expr.getLeft().hashCode() == expr.getRight().getRight().hashCode()) {
                //  writer.println(s + " (Сх. акс. 1)");
                //} else {
                writer.println(expr);
                //}
                s = reader.readLine();
            }
            //writer.println(new ExpressionParser().parse(reader.readLine()));
        }
    }
}
