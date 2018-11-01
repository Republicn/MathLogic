package expression;

import expression.parser.ExpressionParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import static expression.OutputCheck.*;

public class Deduction {

    private PrintWriter writer;

    private void same(Expression expr) {
        String str1 = expr.normalString();
        String str2 = "(" + str1 + "->" + str1 + ")";
        writer.println(str1 + "->" + str2);
        writer.println("(" + str1 + "->" + str2 + ")" + "->" + "(" + str1 + "->(" + str2 + "->" + str1 + "))" +
                "->" + str2);
        writer.println("(" + str1 + "->(" + str2 + "->" + str1 + "))" + "->" + str2);
        writer.println("(" + str1 + "->(" + str2 + "->" + str1 + "))");
    }

    private boolean any(Expression left, Expression right) {
        return check1(left, right) || check2(left, right) || check3(left, right) ||
                check4(left, right) || check5(left, right) || check6(left, right) ||
                check7(left, right) || check8(left, right) || check9(left, right) ||
                check10(left, right);
    }

    private void run() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));

        writer = new PrintWriter("output.txt");
        String s = reader.readLine();
        s = s.trim();
        s = s.replaceAll("\\p{javaWhitespace}+", "");
        String[] tmp = s.split("\\|-");
        String[] hypo = new String[0];
        if (tmp.length > 0) {
            hypo = tmp[0].split(",");
        }
        HashMap<Expression, Integer> hyps = new HashMap<>();
        Expression mainHyp = null;
        for (int i = 1; i <= hypo.length; i++) {
            if (hypo[i - 1].length() > 0) {
                Expression expr = new ExpressionParser().parse(hypo[i - 1]);
                if (i == hypo.length) {
                    mainHyp = expr;
                } else {
                    hyps.put(expr, i);
                    if (i != 1) {
                        writer.print(",");
                    }
                    writer.print(hypo[i - 1]);
                }
            }
        }
        //Expression proof = new ExpressionParser().parse(  hypo[hypo.length - 1] + "->" + tmp[1]);
        Expression proof = new ExpressionParser().parse("(" + hypo[hypo.length - 1] + ")->" + tmp[1]);
        writer.println("|-" + proof.normalString());

        HashMap<Expression, Integer> hm = new HashMap<>();
        int cnt = 0;
        ArrayList<Expression> expressions = new ArrayList<>();
        expressions.add(null);
        HashMap<Expression, ArrayList<Integer>> mp = new HashMap<>();
        s = reader.readLine();
        while (s != null) {
            s = s.trim();
            s = s.replaceAll("\\p{javaWhitespace}+", "");
            if (s.length() == 0) {
                s = reader.readLine();
                continue;
            }
            cnt++;
            Expression expr = new ExpressionParser().parse(s);
            Expression exprL = expr.getLeft();
            Expression exprR = expr.getRight();
            if (expr.getSymb().equals("->")) {
                ArrayList<Integer> temp = mp.getOrDefault(exprR, new ArrayList<>());
                temp.add(cnt);
                mp.putIfAbsent(exprR, temp);
            }
            hm.put(expr, cnt);
            expressions.add(expr);
            Integer res = hyps.get(expr);
            boolean sch = expr.getSymb().equals("->");
            String exprNorm = expr.normalString();
            assert mainHyp != null;
            String mainHypNorm = mainHyp.normalString();
            if (res != null || (sch && any(exprL, exprR))) {
                writer.println(exprNorm + "->(" + mainHypNorm + "->" + exprNorm + ")");
                writer.println(exprNorm);
            } else if (mainHyp.equals(expr)) {
                same(expr);
            } else {
                int res1;
                for (int res2 : mp.getOrDefault(expr, new ArrayList<>())) {
                    if ((res1 = hm.getOrDefault(expressions.get(res2).getLeft(), -1)) != -1 && res1 != cnt) {
                        String resL = expressions.get(res2).getLeft().normalString();
                        String rightPart = "((" + mainHypNorm + "->(" + resL + "->" + exprNorm + "))" + "->(" +
                                mainHypNorm + "->" + exprNorm + "))";
                        writer.println("(" + mainHypNorm + "->" + resL + ")->" + rightPart);
                        writer.println(rightPart);
                        break;
                    }
                }
            }
            writer.println(mainHypNorm + "->" + exprNorm);
            s = reader.readLine();
        }
        writer.close();
    }

    public static void main(String[] args) throws IOException {
        new Deduction().run();
    }
}
