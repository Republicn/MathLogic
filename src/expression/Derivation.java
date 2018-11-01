package expression;

import expression.operations.AbstractUnaryOperation;
import expression.parser.ExpressionParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static expression.Deduction.deduction;
import static java.util.stream.Collectors.toList;

public class Derivation {

    private List<String> mainProof = new LinkedList<>();
    private String mainSt;
    Expression[] vars = new Expression[7];
    List<String>[] hm = new List[200];

    private static List<String> replace(List<String> list, String whatChange1, String repA, String whatChange2, String repB) {
        return list.stream().map(str -> str.replace(whatChange1, repA).replace(whatChange2, repB)).collect(toList());
    }

    public static List<String> UterranceA(String repA, String repB) {
        return replace(Arrays.asList(
                //"a,b|-a&b",
                "a->b->a&b",
                "a",
                "b->a&b",
                "b",
                "a&b"), "a", repA, "b", repB);
    }

    public static List<String> UterranceB(String repA, String repB) {
        return replace(Arrays.asList(
                //"!a,b|-!(a&b)",
                "(a&b->a)->(a&b->!a)->!(a&b)",
                "a&b->a",
                "(a&b->!a)->!(a&b)",
                "!a->a&b->!a",
                "!a",
                "a&b->!a",
                "!(a&b)"), "a", repA, "b", repB);
    }

    public static List<String> UterranceC(String repA, String repB) {
        return replace(Arrays.asList(
                //"a,!b|-!(a&b)",
                "(a&b->b)->(a&b->!b)->!(a&b)",
                "a&b->b",
                "(a&b->!b)->!(a&b)",
                "!b->a&b->!b",
                "!b",
                "a&b->!b",
                "!(a&b)"), "a", repA, "b", repB);
    }

    public static List<String> UterranceD(String repA, String repB) {
        return replace(Arrays.asList(
                //"!a,!b|-!(a&b)",
                "(a&b->a)->(a&b->!a)->!(a&b)",
                "a&b->a",
                "(a&b->!a)->!(a&b)",
                "!a->a&b->!a",
                "!a",
                "a&b->!a",
                "!(a&b)"), "a", repA, "b", repB);
    }

    public static List<String> UterranceEandG(String repA, String repB) {
        return replace(Arrays.asList(
                //"a,?|-a|?",
                "a->(a|?)",
                "a",
                "a|?"), "a", repA, "?", repB);
    }

    public static List<String> UterranceF(String repA, String repB) {
        return replace(Arrays.asList(
                //"!a,b|-a|b",
                "b->(a|b)",
                "b",
                "a|b"), "a", repA, "b", repB);
    }

    public static List<String> UterranceH(String repA, String repB) {
        return replace(Arrays.asList(
                // "!a,!b|-!(a|b)",
                "(a|b->(a))->(a|b->!(a))->!(a|b)",
                "(a->a)->(b->a)->(a|b->a)",
                "a->a->a",
                "(a->a->a)->(a->(a->a)->a)->(a->a)",
                "(a->(a->a)->a)->(a->a)",
                "a->(a->a)->a",
                "a->a",
                "(b->a)->(a|b->a)",
                "(b->!a->b)->b->(b->!a->b)",
                "(b->!a->b)",
                "b->(b->!a->b)",
                "b->b->b",
                "(b->b->b)->(b->(b->b)->b)->(b->b)",
                "(b->(b->b)->b)->(b->b)",
                "b->(b->b)->b",
                "b->b",
                "(b->b)->(b->b->(!a->b))->(b->(!a->b))",
                "(b->b->!a->b)->(b->!a->b)",
                "b->!a->b",
                "(!b->!a->!b)->b->(!b->!a->!b)",
                "!b->!a->!b",
                "b->!b->!a->!b",
                "!b->b->!b",
                "!b",
                "b->!b",
                "(b->!b)->(b->!b->!a->!b)->(b->!a->!b)",
                "(b->!b->!a->!b)->(b->!a->!b)",
                "b->!a->!b",
                "((!a->b)->(!a->!b)->!!a)->b->((!a->b)->(!a->!b)->!!a)",
                "(!a->b)->(!a->!b)->!!a",
                "b->((!a->b)->(!a->!b)->!!a)",
                "(b->(!a->b))->(b->(!a->b)->((!a->!b)->!!a))->(b->((!a->!b)->!!a))",
                "((b->((!a->b)->((!a->!b)->!!a)))->(b->((!a->!b)->!!a)))",
                "b->((!a->!b)->!!a)",
                "(b->(!a->!b))->(b->(!a->!b)->!!a)->(b->!!a)",
                "((b->((!a->!b)->!!a))->(b->!!a))",
                "b->!!a",
                "(!!a->a)->b->(!!a->a)",
                "!!a->a",
                "b->!!a->a",
                "(b->!!a)->(b->!!a->a)->(b->a)",
                "((b->!!a->a)->(b->a))",
                "b->a",
                "(a|b)->a",
                "((a|b)->!a)->!(a|b)",
                "!a->(a|b)->!a",
                "!a",
                "(a|b)->!a",
                "!(a|b)"
        ), "a", repA, "b", repB);
    }

    public static List<String> UterranceIandK(String repA, String repB) {
        return replace(Arrays.asList(
                //"?,b|-?->b",
                "b->?->b",
                "b",
                "?->b"), "?", repA, "b", repB);
    }

    public static List<String> UterranceJ(String repA, String repB) {
        return replace(Arrays.asList(
                //"a,!b|-!(a->b)",
                "((a->b)->b)->((a->b)->!b)->!(a->b)",
                "((a->b)->a)->((a->b)->a->b)->((a->b)->b)",
                "a->(a->b)->a",
                "a",
                "(a->b)->a",
                "((a->b)->a->b)->((a->b)->b)",
                "((a->b)->(a->b)->(a->b))->((a->b)->((a->b)->(a->b))->(a->b))->((a->b)->(a->b))",
                "(a->b)->(a->b)->(a->b)",
                "((a->b)->((a->b)->(a->b))->(a->b))->((a->b)->(a->b))",
                "(a->b)->((a->b)->(a->b))->(a->b)",
                "(a->b)->(a->b)",
                "(a->b)->b",
                "((a->b)->!b)->!(a->b)",
                "!b->(a->b)->!b",
                "!b",
                "(a->b)->!b",
                "!(a->b)"), "a", repA, "b", repB);
    }

    public static List<String> UterranceL(String repA, String repB) {
        return replace(Arrays.asList(
                //"!a,!b|-a->b",
                "(a->!b->a)->a->(a->!b->a)",
                "(a->!b->a)",
                "a->(a->!b->a)",
                "a->a->a",
                "(a->a->a)->(a->(a->a)->a)->(a->a)",
                "(a->(a->a)->a)->(a->a)",
                "a->(a->a)->a",
                "a->a",
                "(a->a)->(a->a->(!b->a))->(a->(!b->a))",
                "(a->a->!b->a)->(a->!b->a)",
                "a->!b->a",
                "(!a->!b->!a)->a->(!a->!b->!a)",
                "!a->!b->!a",
                "a->!a->!b->!a",
                "!a->a->!a",
                "!a",
                "a->!a",
                "(a->!a)->(a->!a->!b->!a)->(a->!b->!a)",
                "(a->!a->!b->!a)->(a->!b->!a)",
                "a->!b->!a",
                "((!b->a)->(!b->!a)->!!b)->a->((!b->a)->(!b->!a)->!!b)",
                "(!b->a)->(!b->!a)->!!b",
                "a->((!b->a)->(!b->!a)->!!b)",
                "(a->(!b->a))->(a->(!b->a)->((!b->!a)->!!b))->(a->((!b->!a)->!!b))",
                "((a->((!b->a)->((!b->!a)->!!b)))->(a->((!b->!a)->!!b)))",
                "a->((!b->!a)->!!b)",
                "(a->(!b->!a))->(a->(!b->!a)->!!b)->(a->!!b)",
                "((a->((!b->!a)->!!b))->(a->!!b))",
                "a->!!b",
                "(!!b->b)->a->(!!b->b)",
                "!!b->b",
                "a->!!b->b",
                "(a->!!b)->(a->!!b->b)->(a->b)",
                "((a->!!b->b)->(a->b))",
                "a->b"), "a", repA, "b", repB);
    }

    public static List<String> UterranceM(String repA, String repB) {
        return replace(Collections.singletonList(
                // "!a|-!a"
                "!a"), "a", repA, "b", repB);
    }

    public static List<String> UterranceN(String repA, String repB) {
        return replace(Arrays.asList(
                //"a|-!!a",
                "(!a->a)->(!a->!a)->!!a",
                "a->!a->a",
                "a",
                "!a->a",
                "(!a->!a)->!!a",
                "(!a->!a->!a)->(!a->(!a->!a)->!a)->(!a->!a)",
                "!a->!a->!a",
                "(!a->(!a->!a)->!a)->(!a->!a)",
                "!a->(!a->!a)->!a",
                "!a->!a",
                "!!a"), "a", repA, "b", repB);
    }

    private void dfs(Expression expr, List<String> curProof) {
        if (expr.getLeft() == null) {
            return;
        }
        if (expr instanceof AbstractUnaryOperation) {
            dfs(expr.getLeft(), curProof);
            curProof.addAll(expr.getLeft().getProof());
        } else {
            dfs(expr.getLeft(), curProof);
            curProof.addAll(expr.getLeft().getProof());
            dfs(expr.getRight(), curProof);
            curProof.addAll(expr.getRight().getProof());
        }
        curProof.addAll(expr.getProof());
    }

    private List<String> contrPosition(String var) {
        return replace(Arrays.asList(
                "(((a->b)->((a->(!b))->(!a)))->((!b)->((a->b)->((a->(!b))->(!a)))))->((a->b)->(((a->b)->((a->(!b))->(!a)))->((!b)->((a->b)->((a->(!b))->(!a))))))",
                "(((a->b)->((a->(!b))->(!a)))->((!b)->((a->b)->((a->(!b))->(!a)))))",
                "(a->b)->(((a->b)->((a->(!b))->(!a)))->((!b)->((a->b)->((a->(!b))->(!a)))))",
                "((a->b)->((a->(!b))->(!a)))->((a->b)->((a->b)->((a->(!b))->(!a))))",
                "((a->b)->((a->(!b))->(!a)))",
                "(a->b)->((a->b)->((a->(!b))->(!a)))",
                "((a->b)->((a->b)->((a->(!b))->(!a))))->(((a->b)->(((a->b)->((a->(!b))->(!a)))->((!b)->((a->b)->((a->(!b))->(!a))))))->((a->b)->((!b)->((a->b)->((a->(!b))->(!a))))))",
                "(((a->b)->(((a->b)->((a->(!b))->(!a)))->((!b)->((a->b)->((a->(!b))->(!a))))))->((a->b)->((!b)->((a->b)->((a->(!b))->(!a))))))",
                "(a->b)->((!b)->((a->b)->((a->(!b))->(!a))))",
                "((a->b)->((!b)->(a->b)))->((a->b)->((a->b)->((!b)->(a->b))))",
                "((a->b)->((!b)->(a->b)))",
                "(a->b)->((a->b)->((!b)->(a->b)))",
                "(a->b)->((a->b)->(a->b))",
                "((a->b)->((a->b)->(a->b)))->((a->b)->(((a->b)->(a->b))->(a->b)))->((a->b)->(a->b))",
                "((a->b)->(((a->b)->(a->b))->(a->b)))->((a->b)->(a->b))",
                "((a->b)->(((a->b)->(a->b))->(a->b)))",
                "(a->b)->(a->b)",
                "((a->b)->(a->b))->(((a->b)->((a->b)->((!b)->(a->b))))->((a->b)->((!b)->(a->b))))",
                "(((a->b)->((a->b)->((!b)->(a->b))))->((a->b)->((!b)->(a->b))))",
                "(a->b)->((!b)->(a->b))",
                "(((!b)->(a->b))->(((!b)->((a->b)->((a->(!b))->(!a))))->((!b)->((a->(!b))->(!a)))))->((a->b)->(((!b)->(a->b))->(((!b)->((a->b)->((a->(!b))->(!a))))->((!b)->((a->(!b))->(!a))))))",
                "(((!b)->(a->b))->(((!b)->((a->b)->((a->(!b))->(!a))))->((!b)->((a->(!b))->(!a)))))",
                "(a->b)->(((!b)->(a->b))->(((!b)->((a->b)->((a->(!b))->(!a))))->((!b)->((a->(!b))->(!a)))))",
                "((a->b)->((!b)->(a->b)))->(((a->b)->(((!b)->(a->b))->(((!b)->((a->b)->((a->(!b))->(!a))))->((!b)->((a->(!b))->(!a))))))->((a->b)->(((!b)->((a->b)->((a->(!b))->(!a))))->((!b)->((a->(!b))->(!a))))))",
                "(((a->b)->(((!b)->(a->b))->(((!b)->((a->b)->((a->(!b))->(!a))))->((!b)->((a->(!b))->(!a))))))->((a->b)->(((!b)->((a->b)->((a->(!b))->(!a))))->((!b)->((a->(!b))->(!a))))))",
                "(a->b)->(((!b)->((a->b)->((a->(!b))->(!a))))->((!b)->((a->(!b))->(!a))))",
                "((a->b)->((!b)->((a->b)->((a->(!b))->(!a)))))->(((a->b)->(((!b)->((a->b)->((a->(!b))->(!a))))->((!b)->((a->(!b))->(!a)))))->((a->b)->((!b)->((a->(!b))->(!a)))))",
                "(((a->b)->(((!b)->((a->b)->((a->(!b))->(!a))))->((!b)->((a->(!b))->(!a)))))->((a->b)->((!b)->((a->(!b))->(!a)))))",
                "(a->b)->((!b)->((a->(!b))->(!a)))",
                "(((!b)->(a->(!b)))->((!b)->((!b)->(a->(!b)))))->((a->b)->(((!b)->(a->(!b)))->((!b)->((!b)->(a->(!b))))))",
                "(((!b)->(a->(!b)))->((!b)->((!b)->(a->(!b)))))",
                "(a->b)->(((!b)->(a->(!b)))->((!b)->((!b)->(a->(!b)))))",
                "((!b)->(a->(!b)))->((a->b)->((!b)->(a->(!b))))",
                "((!b)->(a->(!b)))",
                "(a->b)->((!b)->(a->(!b)))",
                "((a->b)->((!b)->(a->(!b))))->(((a->b)->(((!b)->(a->(!b)))->((!b)->((!b)->(a->(!b))))))->((a->b)->((!b)->((!b)->(a->(!b))))))",
                "(((a->b)->(((!b)->(a->(!b)))->((!b)->((!b)->(a->(!b))))))->((a->b)->((!b)->((!b)->(a->(!b))))))",
                "(a->b)->((!b)->((!b)->(a->(!b))))",
                "((!b)->((!b)->(!b)))->((a->b)->((!b)->((!b)->(!b))))",
                "((!b)->((!b)->(!b)))",
                "(a->b)->((!b)->((!b)->(!b)))",
                "(((!b)->((!b)->(!b)))->(((!b)->(((!b)->(!b))->(!b)))->((!b)->(!b))))->((a->b)->(((!b)->((!b)->(!b)))->(((!b)->(((!b)->(!b))->(!b)))->((!b)->(!b)))))",
                "(((!b)->((!b)->(!b)))->(((!b)->(((!b)->(!b))->(!b)))->((!b)->(!b))))",
                "(a->b)->(((!b)->((!b)->(!b)))->(((!b)->(((!b)->(!b))->(!b)))->((!b)->(!b))))",
                "((a->b)->((!b)->((!b)->(!b))))->(((a->b)->(((!b)->((!b)->(!b)))->(((!b)->(((!b)->(!b))->(!b)))->((!b)->(!b)))))->((a->b)->(((!b)->(((!b)->(!b))->(!b)))->((!b)->(!b)))))",
                "(((a->b)->(((!b)->((!b)->(!b)))->(((!b)->(((!b)->(!b))->(!b)))->((!b)->(!b)))))->((a->b)->(((!b)->(((!b)->(!b))->(!b)))->((!b)->(!b)))))",
                "(a->b)->(((!b)->(((!b)->(!b))->(!b)))->((!b)->(!b)))",
                "((!b)->(((!b)->(!b))->(!b)))->((a->b)->((!b)->(((!b)->(!b))->(!b))))",
                "((!b)->(((!b)->(!b))->(!b)))",
                "(a->b)->((!b)->(((!b)->(!b))->(!b)))",
                "((a->b)->((!b)->(((!b)->(!b))->(!b))))->(((a->b)->(((!b)->(((!b)->(!b))->(!b)))->((!b)->(!b))))->((a->b)->((!b)->(!b))))",
                "(((a->b)->(((!b)->(((!b)->(!b))->(!b)))->((!b)->(!b))))->((a->b)->((!b)->(!b))))",
                "(a->b)->((!b)->(!b))",
                "(((!b)->(!b))->(((!b)->((!b)->(a->(!b))))->((!b)->(a->(!b)))))->((a->b)->(((!b)->(!b))->(((!b)->((!b)->(a->(!b))))->((!b)->(a->(!b))))))",
                "(((!b)->(!b))->(((!b)->((!b)->(a->(!b))))->((!b)->(a->(!b)))))",
                "(a->b)->(((!b)->(!b))->(((!b)->((!b)->(a->(!b))))->((!b)->(a->(!b)))))",
                "((a->b)->((!b)->(!b)))->(((a->b)->(((!b)->(!b))->(((!b)->((!b)->(a->(!b))))->((!b)->(a->(!b))))))->((a->b)->(((!b)->((!b)->(a->(!b))))->((!b)->(a->(!b))))))",
                "(((a->b)->(((!b)->(!b))->(((!b)->((!b)->(a->(!b))))->((!b)->(a->(!b))))))->((a->b)->(((!b)->((!b)->(a->(!b))))->((!b)->(a->(!b))))))",
                "(a->b)->(((!b)->((!b)->(a->(!b))))->((!b)->(a->(!b))))",
                "((!b)->(a->(!b)))->((a->b)->((!b)->(a->(!b))))",
                "((!b)->(a->(!b)))",
                "(a->b)->((!b)->(a->(!b)))",
                "(((!b)->(a->(!b)))->(((!b)->((a->(!b))->(!a)))->((!b)->(!a))))->((a->b)->(((!b)->(a->(!b)))->(((!b)->((a->(!b))->(!a)))->((!b)->(!a)))))",
                "(((!b)->(a->(!b)))->(((!b)->((a->(!b))->(!a)))->((!b)->(!a))))",
                "(a->b)->(((!b)->(a->(!b)))->(((!b)->((a->(!b))->(!a)))->((!b)->(!a))))",
                "((a->b)->((!b)->(a->(!b))))->(((a->b)->(((!b)->(a->(!b)))->(((!b)->((a->(!b))->(!a)))->((!b)->(!a)))))->((a->b)->(((!b)->((a->(!b))->(!a)))->((!b)->(!a)))))",
                "(((a->b)->(((!b)->(a->(!b)))->(((!b)->((a->(!b))->(!a)))->((!b)->(!a)))))->((a->b)->(((!b)->((a->(!b))->(!a)))->((!b)->(!a)))))",
                "(a->b)->(((!b)->((a->(!b))->(!a)))->((!b)->(!a)))",
                "((a->b)->((!b)->((a->(!b))->(!a))))->(((a->b)->(((!b)->((a->(!b))->(!a)))->((!b)->(!a))))->((a->b)->((!b)->(!a))))",
                "(((a->b)->(((!b)->((a->(!b))->(!a)))->((!b)->(!a))))->((a->b)->((!b)->(!a))))",
                "(a->b)->((!b)->(!a))"
        ), "a", var, "b", mainSt);
    }

    private List<String> withoutAssumptions(int curVar, int cnt, int mask) {
        if (curVar == cnt) {
            return hm[mask];
        }
        List<String> proof1 = withoutAssumptions(curVar + 1, cnt, mask);
        List<String> proof2 = withoutAssumptions(curVar + 1, cnt, mask | (1 << curVar));
        List<String> tmpHyps = new ArrayList<>();
        for (int i = 0; i < curVar; i++) {
            tmpHyps.add((((mask & (1 << i)) > 0) ? "" : "!") + vars[i].normalString());
        }
        List<String> proof = new ArrayList<>();
        proof.addAll(deduction(proof1, "!" + vars[curVar].normalString(), tmpHyps));
        proof.addAll(deduction(proof2, vars[curVar].normalString(), tmpHyps));
        proof.addAll(contrPosition(vars[curVar].normalString()));
        proof.addAll(contrPosition("!" + vars[curVar].normalString()));
        proof.addAll(replace(Arrays.asList(
                "(a->b)->(!b->!a)",
                "(!a->b)->(!b->!!a)",
                "!b->!a",
                "!b->!!a",
                "(!b->!a)->(!b->!!a)->!!b",
                "(!b->!!a)->!!b",
                "!!b",
                "!!b->b",
                "b"
        ), "a", vars[curVar].normalString(), "b", mainSt));
        return proof;
    }

    public void run() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("output.txt"))) {
            String s = reader.readLine();
            s = s.trim();
            s = s.replaceAll("\\p{javaWhitespace}+", "");
            String header = s.replace("|=", "|-");
            mainProof.add(header);
            String[] tmp = s.split("\\|=");
            String[] hypo = new String[0];
            if (tmp.length > 0) {
                hypo = tmp[0].split(",");
            }
            Set<Expression> hypsAlwaysTrue = new HashSet<>();
            int cnt = 0;
            ExpressionParser parser = new ExpressionParser();

            for (int i = 0; i < hypo.length; i++) {
                if (hypo[i].length() > 0) {
                    Expression parsed = parser.parse(hypo[i]);
                    hypsAlwaysTrue.add(parsed);
                    tmp[1] = "(" + parsed.normalString() + ")->" + tmp[1];
                }
            }
            System.err.println(tmp[1]);
            int len = tmp[1].length();
            for (int i = 0; i < len; i++) {
                StringBuilder str = new StringBuilder();
                while (i < len && !Character.isLetterOrDigit(tmp[1].charAt(i))) i++;
                while (i < len && Character.isLetterOrDigit(tmp[1].charAt(i))) {
                    str.append(tmp[1].charAt(i));
                    if (tmp[1].charAt(i) == 'a' || tmp[1].charAt(i) == 'b' ) {
                        i--;
                    }
                    i++;
                }
                if (str.length() == 0) {
                    break;
                }
                Expression parsed = parser.parse(str.toString());
                if (!(parsed.equals(vars[0]) || parsed.equals(vars[1]) || parsed.equals(vars[2]) || parsed.equals(vars[3])
                        || parsed.equals(vars[4]) || parsed.equals(vars[5]) || parsed.equals(vars[6]))) {
                    vars[cnt++] = parsed;
                }
            }
            mainSt = "(" + tmp[1] + ")";
            Expression mainStatement = parser.parse(tmp[1]);
            int sets = (1 << cnt);
            int ch = 0;
            while (ch < sets) {
                List<String> curVars = new LinkedList<>();
                Set<String> varsNowTrue = new HashSet<>();
                int i = 0;
                int now = ch;
                while (now != 0) {
                    int curValueVar = now & 1;
                    if (curValueVar == 1) {
                        varsNowTrue.add(vars[i].normalString());
                        curVars.add(vars[i].normalString());
                    } else {
                        curVars.add("!" + vars[i].normalString());
                    }
                    i++;
                    now = now >> 1;
                }
                boolean fl = false;
                if (mainStatement.apply(varsNowTrue) == '0') {
                    StringBuilder str = new StringBuilder();
                    for (int ij = 0; ij < cnt; ij++) {
                        Expression var = vars[ij];
                        String varNormStr = var.normalString();
                        if (varsNowTrue.contains(var.normalString())) {
                            if (!fl) {
                                str.append("Высказывание ложно при ").append(varNormStr).append("=И, ");
                                fl = true;
                            } else {
                                str.append(varNormStr).append("=И, ");
                            }
                        } else if (!fl) {
                            str.append("Высказывание ложно при ").append(varNormStr).append("=Л, ");
                            fl = true;
                        } else {
                            str.append(varNormStr).append("=Л, ");
                        }
                    }
                    writer.write(str.toString().substring(0, str.length() - 2));
                    return;
                }
                List<String> curProof = new LinkedList<>();
                dfs(mainStatement, curProof);
                hm[ch] = curProof;
                ch++;
            }
            /*PrintWriter serr = new PrintWriter("debug");
            for (int i = 0; i < 1 << cnt; i++) {
                for (String ss : hm[i]) {
                    serr.println(ss);
                }
                serr.println();
            }
            serr.close();*/
            mainProof.addAll(withoutAssumptions(0, cnt, 0));
            for (int j = 0; j < hypsAlwaysTrue.size(); j++) {
                mainProof.add(mainStatement.getLeft().normalString());
                mainProof.add(mainStatement.getRight().normalString());
                mainStatement = mainStatement.getRight();
            }
            for (String str : mainProof) {
                writer.write(str + "\n");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        long curTime = System.currentTimeMillis();
        new Derivation().run();
        System.err.println(System.currentTimeMillis() - curTime);
    }
}
