package expression;

import expression.parser.ExpressionParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Kripke {

    private static void run() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("output.txt"))) {
            String s = reader.readLine();
            s = s.trim();
            s = s.replaceAll("\\p{javaWhitespace}+", "");
            int cnt = 0;
            ExpressionParser parser = new ExpressionParser();
        }
    }

    public static void main(String[] args) throws IOException {
        new Kripke().run();
    }
}
