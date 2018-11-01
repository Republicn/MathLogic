package expression;

import expression.parser.ExpressionParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class IIV {

    private Expression mainStatement;
    private int numVars = 0;
    private Expression[] vars = new Expression[3];
    private final static int MAX_N = 5;
    private ArrayList<Integer>[] vertices = new ArrayList[MAX_N + 2];
    private Set<String>[] coertionVertices = new Set[MAX_N + 2];
    private Set<Integer> correctMasks;
    private int correctMask[] = new int[3];

    private ArrayList<String> brackets = new ArrayList<>();
    private char[] bracket;

    private void genBrackets(int len, int bal, int n) {
        if (len == n) {
            if (bal == 0) {
                StringBuilder tmp = new StringBuilder();
                for (char c : bracket) {
                    tmp.append(c);
                }
                brackets.add(tmp.toString());
            }
            return;
        }
        bracket[len] = '(';
        genBrackets(len + 1, bal + 1, n);
        if (bal > 0) {
            bracket[len] = ')';
            genBrackets(len + 1, bal - 1, n);
        }

    }

    private void corrMasks() {
        correctMasks = new TreeSet<>();
        for (int mask1 = 0; mask1 < (1 << MAX_N); mask1++) {
            boolean ok = true;
            for (int i = 0; i < MAX_N; i++) {
                if (((mask1 >> i) & 1) == 1) {
                    if (!checkCoercion(mask1, i + 1)) {
                        //correctMasks.add(mask1);
                        ok = false;
                        break;
                    }
                }
            }
            if (ok) {
                correctMasks.add(mask1);
            }
        }
    }

    private boolean checkCoercion(int mask, int curVertice) {
        if (((mask >> (curVertice - 1)) & 1) == 0) return false;
        boolean fl = true;
        for (int vert : vertices[curVertice]) {
            fl = fl & checkCoercion(mask, vert);
        }
        return fl;
    }

    private void Coercion(int mask, int var) {
        for (int i = 0; i < MAX_N; i++) {
            if (((mask >> i) & 1) == 1) {
                coertionVertices[i + 1].add(vars[var].normalString());
            }
        }
    }

    private boolean checkImpl(int vert, Expression curStLeft, Expression curStRight) {
        boolean fl = true;
        for (int ch : vertices[vert]) {
            if (!checkImpl(ch, curStLeft, curStRight)) {
                fl = false;
                break;
            }
            if (!(!finalCoercion(ch, curStLeft) || finalCoercion(ch, curStRight))) {
                fl = false;
                break;
            }
        }
        if (vertices[vert].size() != 0) return fl;
        return !finalCoercion(vert, curStLeft) || finalCoercion(vert, curStRight);
        //return ((!coertionVertices[vert].contains(curStLeft.normalString())) || coertionVertices[vert].contains(curStRight.normalString()));
    }

    private boolean checkNeg(int vert, Expression curSt) {
        boolean fl = true;
        for (int ch : vertices[vert]) {
            if (!checkNeg(ch, curSt)) {
                fl = false;
                break;
            }
            if (finalCoercion(ch, curSt)) {
                fl = false;
                break;
            }
        }
        if (vertices[vert].size() != 0) return fl;
        return !finalCoercion(vert, curSt);
        //return (!coertionVertices[vert].contains(curSt.normalString()));

    }

    private boolean finalCoercion(int vert, Expression curSt) {
        if (curSt == null) {
            return true;
        }
        if (curSt.getSymb().equals("&")) {
            if (finalCoercion(vert, curSt.getLeft()) && finalCoercion(vert, curSt.getRight())) {
                coertionVertices[vert].add(curSt.normalString());
                return true;
            } else return false;
        }
        if (curSt.getSymb().equals("|")) {
            if (finalCoercion(vert, curSt.getLeft()) || finalCoercion(vert, curSt.getRight())) {
                coertionVertices[vert].add(curSt.normalString());
                return true;
            } else return false;
        }
        if (curSt.getSymb().equals("!")) {
            if (checkNeg(vert, curSt.getLeft())) {
                coertionVertices[vert].add(curSt.normalString());
                return true;
            } else return false;
        }
        if (curSt.getSymb().equals("->")) {
            if (checkImpl(vert, curSt.getLeft(), curSt.getRight())) {
                coertionVertices[vert].add(curSt.normalString());
                return true;
            } else return false;
        }
        return coertionVertices[vert].contains(curSt.normalString());
    }

    private void dfs(int v, Set<Integer> child) {
        child.add(v);
        for (int u : vertices[v]) {
            dfs(u, child);
        }
    }

    private boolean equalsSet(Set<Integer> a, Set<Integer> b) {
        if (a.size() != b.size()) {
            return false;
        }
        for (int u : a) {
            if (!b.contains(u)) {
                return false;
            }
        }
        return true;
    }

    private void makeTopology(BufferedWriter writer) throws IOException {
        List<Set<Integer>> base = new ArrayList<>();
        base.add(new TreeSet<Integer>());
        for (int i = 1; i <= MAX_N + 1; i++) {
            Set<Integer> child = new TreeSet<>();
            dfs(i, child);
            base.add(child);
        }
        List<Set<Integer>> topology = new ArrayList<>();
        for (int mask = 0; mask < (1 << base.size()); mask++) {
            Set<Integer> s = new TreeSet<>();
            for (int i = 0; i < base.size(); i++) {
                if (((mask >> i) & 1) == 1) {
                    s.addAll(base.get(i));
                }
            }
            boolean ok = true;
            for (Set<Integer> tmp : topology) {
                if (equalsSet(tmp, s)) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                topology.add(s);
            }
        }
        printGraph(writer, topology);
    }

    private void printGraph(BufferedWriter writer, List<Set<Integer>> topology) throws IOException {
        int n = topology.size();
        boolean graph[][] = new boolean[n][n];
        writer.write(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                graph[i][j] = topology.get(j).containsAll(topology.get(i));
                if (graph[i][j]) {
                    writer.write((j + 1) + " ");
                }
            }
            writer.write("\n");
        }
        printVars(writer, topology, graph);
    }

    private void printVars(BufferedWriter writer, List<Set<Integer>> topology, boolean[][] graph) throws IOException {
        String ans = "";
        for (int v = 0; v < numVars; v++) {
            String var = vars[v].normalString();
            Set<Integer> verties = new TreeSet<>();
            for (int i = 0; i < MAX_N; i++) {
                if (((correctMask[v] >> i) & 1) == 1) {
                    verties.add(i + 1);
                }
            }
            //System.err.println(verties);
            int numTopology = -1;
            for (int i = 0; i < topology.size(); i++) {
                if (verties.containsAll(topology.get(i))) {

                    if (numTopology == -1 || topology.get(numTopology).size() < topology.get(i).size()) {
                        //System.err.println("new ver in: " + i + "===" + topology.get(i));

                        numTopology = i;
                    }
                }
            }
            if (!ans.equals("")) {
                ans = ',' + ans;
            }
            ans = var + "=" + (numTopology + 1) + ans;
        }
        writer.write(ans);
        //System.err.println("topology");
        for (Set<Integer> s : topology) {
            //System.err.println(s);
        }

    }

    private void makeFullKripke(BufferedWriter writer) throws IOException {
        corrMasks();
        for (int j : correctMasks) {
            correctMask[0] = j;
            if (numVars > 1) {
                for (int k : correctMasks) {
                    correctMask[1] = k;
                    if (numVars > 2) {
                        for (int l : correctMasks) {
                            correctMask[2] = l;
                            for (int i = 1; i <= MAX_N; i++) {
                                coertionVertices[i].clear();
                            }
                            Coercion(correctMask[0], 0);
                            Coercion(correctMask[1], 1);
                            Coercion(correctMask[2], 2);
                            if (!finalCoercion(1, mainStatement)) {
                                makeTopology(writer);
                                writer.close();
                                System.exit(0);
                            }
                        }
                    } else {
                        for (int i = 1; i <= MAX_N; i++) {
                            coertionVertices[i].clear();
                        }
                        Coercion(correctMask[0], 0);
                        Coercion(correctMask[1], 1);
                        if (!finalCoercion(1, mainStatement)) {
                            makeTopology(writer);
                            writer.close();
                            System.exit(0);
                        }
                    }
                }
            } else {
                for (int i = 1; i <= MAX_N; i++) {
                    coertionVertices[i].clear();
                }
                Coercion(correctMask[0], 0);
                if (!finalCoercion(1, mainStatement)) {
                    for (int l = 1; l <= MAX_N; l++) {
                        //System.err.print(l + " ");
                        for (String st : coertionVertices[l]) {
                            //System.err.print(st + " ");
                        }
                        //System.err.println("");
                    }
                    makeTopology(writer);
                    writer.close();
                    System.exit(0);
                }

            }
        }

    }

    private void createTrees(BufferedWriter writer) throws IOException {

        for (String curSeq : brackets) {
            int par = 0;
            int ver = 1;

            for (int j = 0; j < curSeq.length(); j++) {
                if (curSeq.charAt(j) == '(') {
                    par++;
                    vertices[par].add(++ver);

                } else {
                    par--;
                }
                //prev = bal;
            }
            //System.err.println(curSeq);
            makeFullKripke(writer);
            correctMasks.clear();
            for (int i = 1; i <= MAX_N; i++) {
                vertices[i].clear();
                coertionVertices[i].clear();
            }
        }
        writer.write("Формула общезначима");
    }

    private void run() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));

        bracket = new char[2 * MAX_N];

        genBrackets(0, 0, 2 * MAX_N);
        for (int i = 0; i <= MAX_N + 1; i++) {
            vertices[i] = new ArrayList<>();
            coertionVertices[i] = new TreeSet<>();
        }
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("output.txt"))) {
            String s = reader.readLine();
            s = s.trim();
            s = s.replaceAll("\\p{javaWhitespace}+", "");
            ExpressionParser parser = new ExpressionParser();
            int len = s.length();
            for (int i = 0; i < len; i++) {
                StringBuilder str = new StringBuilder();
                while (i < len && !Character.isLetterOrDigit(s.charAt(i))) i++;
                while (i < len && Character.isLetterOrDigit(s.charAt(i))) {
                    str.append(s.charAt(i));
                    i++;
                }
                if (str.length() == 0) {
                    break;
                }
                Expression parsed = parser.parse(str.toString());
                if (!(parsed.equals(vars[0]) || parsed.equals(vars[1]) || parsed.equals(vars[2]))) {
                    vars[numVars++] = parsed;
                }
            }
            String mainSt = "(" + s + ")";
            mainStatement = parser.parse(s);
            createTrees(writer);
        }
    }


    public static void main(String[] args) throws IOException {
        new IIV().run();
    }
}
