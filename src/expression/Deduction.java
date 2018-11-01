package expression;

import expression.parser.ExpressionParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static expression.OutputCheck.*;

public class Deduction {

    private static List<String> same(Expression expr) {
        String str1 = expr.normalString();
        String str2 = "(" + str1 + "->" + str1 + ")";
        List<String> temp = new ArrayList<>();
        temp.add(str1 + "->" + str2);
        temp.add("(" + str1 + "->" + str2 + ")" + "->" + "(" + str1 + "->(" + str2 + "->" + str1 + "))" +
                "->" + str2);
        temp.add("(" + str1 + "->(" + str2 + "->" + str1 + "))" + "->" + str2);
        temp.add("(" + str1 + "->(" + str2 + "->" + str1 + "))");
        return temp;
    }

    private static boolean any(Expression left, Expression right) {
        return check1(left, right) || check2(left, right) || check3(left, right) ||
                check4(left, right) || check5(left, right) || check6(left, right) ||
                check7(left, right) || check8(left, right) || check9(left, right) ||
                check10(left, right);
    }

    public static List<String> deduction(List<String> proof, String mainHyp, List<String> hypo) {
        ExpressionParser parser = new ExpressionParser();
        HashMap<Expression, Integer> hyps = new HashMap<>();
        for (int i = 0; i < hypo.size(); i++) {
            hyps.put(parser.parse(hypo.get(i)), i + 1);
        }
        return deduction(proof, parser.parse(mainHyp), hyps);
    }

    private static List<String> deduction(List<String> str, Expression mainHyp, HashMap<Expression, Integer> hyps) {
        HashMap<Expression, Integer> hm = new HashMap<>();
        ArrayList<Expression> expressions = new ArrayList<>();
        HashMap<Expression, ArrayList<Integer>> mp = new HashMap<>();

        List<String> ans = new ArrayList<>();
        int cnt = 0;
        expressions.add(null);
        for (String s : str) {
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
                ans.add(exprNorm + "->(" + mainHypNorm + "->" + exprNorm + ")");
                ans.add(exprNorm);
            } else if (mainHyp.equals(expr)) {
                ans.addAll(same(expr));
            } else {
                int res1;
                for (int res2 : mp.getOrDefault(expr, new ArrayList<>())) {
                    if ((res1 = hm.getOrDefault(expressions.get(res2).getLeft(), -1)) != -1 && res1 != cnt) {
                        String resL = expressions.get(res2).getLeft().normalString();
                        String rightPart = "((" + mainHypNorm + "->(" + resL + "->" + exprNorm + "))" + "->(" +
                                mainHypNorm + "->" + exprNorm + "))";
                        ans.add("(" + mainHypNorm + "->" + resL + ")->" + rightPart);
                        ans.add(rightPart);
                        break;
                    }
                }
            }
            ans.add(mainHypNorm + "->" + exprNorm);
        }
        return ans;
    }

    private void run() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
        PrintWriter writer = new PrintWriter("output.txt");
        String s = reader.readLine();
        s = s.trim();
        s = s.replaceAll("\\p{javaWhitespace}+", "");
        String[] tmp = s.split("\\|-");
        String[] hypo = new String[0];
        if (tmp.length > 0) {
            hypo = tmp[0].split(",");
        }
        //HashMap<Expression, Integer> hyps = new HashMap<>();
        String mainHyp = null;
        List<String> hyps = new ArrayList<>();
        for (int i = 1; i <= hypo.length; i++) {
            if (hypo[i - 1].length() > 0) {
                //Expression expr = new ExpressionParser().parse(hypo[i - 1]);
                if (i == hypo.length) {
                    mainHyp = hypo[i - 1];
                } else {
                    //hyps.put(expr, i);
                    hyps.add(hypo[i - 1]);
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

        s = reader.readLine();
        List<String> str = new ArrayList<>();
        while (s != null) {
            s = s.trim();
            s = s.replaceAll("\\p{javaWhitespace}+", "");
            if (s.length() == 0) {
                s = reader.readLine();
                continue;
            }
            str.add(s);
            s = reader.readLine();
        }
        List<String> ans = deduction(str, mainHyp, hyps);
        for (String ss : ans) {
            writer.println(ss);
        }
        writer.close();
    }

    public static void main(String[] args) throws IOException {
        new Deduction().run();
    }
}
