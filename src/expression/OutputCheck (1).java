package expression;

import expression.parser.ExpressionParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class OutputCheck {

    static boolean check1(Expression exprL, Expression exprR) {
        return exprR.getSymb().equals("->") &&
                exprL.hashCode() == exprR.getRight().hashCode();
    }

    static boolean check2(Expression exprL, Expression exprR) {
        return ((exprL.getSymb().equals(exprR.getSymb())) && (exprL.getSymb().equals("->")) &&
                (exprR.getLeft().getSymb().equals(exprR.getRight().getSymb())) &&
                (exprR.getLeft().getSymb().equals("->")) && (exprR.getLeft().getRight().getSymb().equals("->")) &&
                (exprL.getLeft().hashCode() == exprR.getLeft().getLeft().hashCode()) &&
                (exprL.getLeft().hashCode() == exprR.getRight().getLeft().hashCode()) &&
                (exprL.getRight().hashCode() == exprR.getLeft().getRight().getLeft().hashCode()) &&
                (exprR.getLeft().getRight().getRight().hashCode() == exprR.getRight().getRight().hashCode()));
    }


    static boolean check3(Expression exprL, Expression exprR) {
        return (exprR.getSymb().equals("->") && (exprR.getRight().getSymb().equals("&")) &&
                (exprR.getLeft().hashCode() == exprR.getRight().getRight().hashCode()) &&
                (exprL.hashCode() == exprR.getRight().getLeft().hashCode()));
    }

    static boolean check4(Expression exprL, Expression exprR) {
        return (exprL.getSymb().equals("&") && (exprL.getLeft().hashCode() == exprR.hashCode()));
    }


    static boolean check5(Expression exprL, Expression exprR) {
        return (exprL.getSymb().equals("&") && (exprL.getRight().hashCode() == exprR.hashCode()));
    }


    static boolean check6(Expression exprL, Expression exprR) {
        return (exprR.getSymb().equals("|") && (exprR.getLeft().hashCode() == exprL.hashCode()));
    }


    static boolean check7(Expression exprL, Expression exprR) {
        return (exprR.getSymb().equals("|") && (exprL.hashCode() == exprR.getRight().hashCode()));
    }

    static boolean check8(Expression exprL, Expression exprR) {
        return (exprL.getSymb().equals(exprR.getSymb()) && exprL.getSymb().equals("->") &&
                (exprR.getLeft().getSymb().equals(exprR.getRight().getSymb())) &&
                (exprR.getRight().getSymb().equals("->")) &&
                (exprR.getRight().getLeft().getSymb().equals("|")) &&
                (exprL.getLeft().hashCode() == exprR.getRight().getLeft().getLeft().hashCode()) &&
                (exprL.getRight().hashCode() == exprR.getLeft().getRight().hashCode()) &&
                (exprL.getRight().hashCode() == exprR.getRight().getRight().hashCode()) &&
                (exprR.getLeft().getLeft().hashCode() == exprR.getRight().getLeft().getRight().hashCode()));
    }

    static boolean check9(Expression exprL, Expression exprR) {
        return (exprL.getSymb().equals(exprR.getSymb()) && exprL.getSymb().equals("->") &&
                (exprR.getLeft()).getSymb().equals("->") && (exprR.getRight().getSymb().equals("!")) &&
                (exprR.getLeft().getRight().getSymb().equals("!")) &&
                (exprL.getLeft().hashCode() == exprR.getLeft().getLeft().hashCode()) &&
                (exprL.getLeft().hashCode() == exprR.getRight().getRight().hashCode()) &&
                (exprL.getRight().hashCode() == exprR.getLeft().getRight().getRight().hashCode()));
    }

    static boolean check10(Expression exprL, Expression exprR) {
        return (exprL.getSymb().equals("!") && (exprL.getLeft().getSymb().equals("!")) &&
                (exprL.getLeft().getLeft().hashCode() == exprR.hashCode()));
    }

    private void run() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("output.txt"));
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("output2.txt"))) {
            //try (writeWriter writer = new writeWriter("output.txt")) {
            String s = reader.readLine();
            s = s.trim();
            s = s.replaceAll("\\p{javaWhitespace}+", "");

            String[] tmp = s.split("\\|-");

            String[] hypo = new String[0];
            if (tmp.length > 0) {
                hypo = tmp[0].split(",");
            }

            HashMap<Expression, Integer> hyps = new HashMap<>();
            ExpressionParser parser = new ExpressionParser();
            for (int i = 0; i < hypo.length; i++) {
                if (hypo[i].length() > 0) {
                    hyps.put(parser.parse(hypo[i]), i + 1);
                }
            }
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
                Expression expr = parser.parse(s);
                Expression exprL = expr.getLeft();
                Expression exprR = expr.getRight();
                if (exprR != null && expr.getSymb().equals("->")) {
                    ArrayList<Integer> temp = mp.getOrDefault(exprR, new ArrayList<>());
                    temp.add(cnt);
                    mp.put(exprR, temp);
                }
                hm.put(expr, cnt);
                expressions.add(expr);
                writer.write("(" + cnt + ") " + s);

                Integer res = hyps.get(expr);
                boolean sch = expr.getSymb().equals("->");

                if (res != null) {
                    writer.write(" (Предп. " + res + ")");
                } else {
                    assert exprR != null;
                    if (sch && check1(exprL, exprR)) {
                        writer.write(" (Сх. акс. 1)");
                    } else if (sch && check2(exprL, exprR)) {
                        writer.write(" (Сх. акс. 2)");
                    } else if (sch && check3(exprL, exprR)) {
                        writer.write(" (Сх. акс. 3)");
                    } else if (sch && check4(exprL, exprR)) {
                        writer.write(" (Сх. акс. 4)");
                    } else if (sch && check5(exprL, exprR)) {
                        writer.write(" (Сх. акс. 5)");
                    } else if (sch && check6(exprL, exprR)) {
                        writer.write(" (Сх. акс. 6)");
                    } else if (sch && check7(exprL, exprR)) {
                        writer.write(" (Сх. акс. 7)");
                    } else if (sch && check8(exprL, exprR)) {
                        writer.write(" (Сх. акс. 8)");
                    } else if (sch && check9(exprL, exprR)) {
                        writer.write(" (Сх. акс. 9)");
                    } else if (sch && check10(exprL, exprR)) {
                        writer.write(" (Сх. акс. 10)");
                    } else {
                        boolean ok = false;
                        int res1;
                        for (int res2 : mp.getOrDefault(expr, new ArrayList<>())) {
                            if ((res1 = hm.getOrDefault(expressions.get(res2).getLeft(), -1)) != -1 && res1 != cnt) {
                                writer.write(" (M.P. " + res2 + ", " + res1 + ")");
                                ok = true;
                                break;
                            }
                        }
                        if (!ok) {
                            writer.write(" (Не доказано)");
                        }
                    }
                }
                s = reader.readLine();
                writer.write("\n");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new OutputCheck().run();
    }
}
