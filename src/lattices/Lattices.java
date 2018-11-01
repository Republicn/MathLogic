package lattices;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Lattices {

    private int[][] relations, adds, muls;
    private int numVertices;
    private Set<Integer>[] descendants;
    private int zero;
    private int one;

    private int add(int verticeOne, int verticeTwo) {
        if (adds[verticeOne][verticeTwo] != -1) {
            return adds[verticeOne][verticeTwo];
        }
        int ans = 0;
        Set<Integer> child = new HashSet<>();
        for (int i = 1; i <= numVertices; i++) {
            if (relations[verticeOne][i] == 1 && relations[verticeTwo][i] == 1) {
                child.add(i);
            }
        }
        for (int v : child) {
            boolean ok = true;
            for (int u = 1; u <= numVertices; u++) {
                if (child.contains(u) && u != v && relations[u][v] == 1) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                if (ans == 0) {
                    ans = v;
                } else {
                    return 0;
                }
            }
        }
        return adds[verticeOne][verticeTwo] = ans;
    }

    private int mul(int verticeOne, int verticeTwo) {
        if (muls[verticeOne][verticeTwo] != -1) {
            return muls[verticeOne][verticeTwo];
        }
        int ans = 0;
        Set<Integer> parent = new HashSet<>();
        for (int i = 1; i <= numVertices; i++) {
            if (relations[i][verticeOne] == 1 && relations[i][verticeTwo] == 1) {
                parent.add(i);
            }
        }
        for (int v : parent) {
            boolean ok = true;
            for (int u : descendants[v]) {
                if (parent.contains(u) && u != v) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                if (ans == 0) {
                    ans = v;
                } else {
                    return 0;
                }
            }
        }
        return muls[verticeOne][verticeTwo] = ans;
    }

    private boolean distr(int verticeOne, int verticeTwo, int verticeThree) {
        int resAdd = add(verticeTwo, verticeThree);
        int mul1 = mul(verticeOne, verticeTwo);
        int mul2 = mul(verticeOne, verticeThree);
        return (mul(verticeOne, resAdd) == add(mul1, mul2));
    }

    private int pseudoCompl(int verticeOne, int verticeTwo) {
        int ans = 0;
        Set<Integer> cand = new HashSet<>();
        for (int i = 1; i <= numVertices; i++) {
            int x = mul(verticeOne, i);
            if (relations[x][verticeTwo] == 1) {
                cand.add(i);
            }
        }
        for (int v : cand) {
            boolean ok = true;
            for (int u : descendants[v]) {
                if (cand.contains(u) && u != v) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                if (ans == 0) {
                    ans = v;
                } else {
                    return 0;
                }
            }
        }
        return ans;
    }

    private boolean boolAlg(int vertice) {
        int res = pseudoCompl(vertice, zero);
        assert add(vertice, res) != one || mul(vertice, res) == zero : "bool";
        return add(vertice, res) == one;// && mul(vertice, res) == zero;
    }

    public void run() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
        PrintWriter writer = new PrintWriter("output.txt");
        String ss = reader.readLine().trim();
        while (ss.equals("")) {
            ss = reader.readLine().trim();
        }
        numVertices = Integer.parseInt(ss);
        relations = new int[numVertices + 1][numVertices + 1];
        adds = new int[numVertices + 1][numVertices + 1];
        muls = new int[numVertices + 1][numVertices + 1];
        for (int i = 0; i <= numVertices; i++) {
            for (int j = 0; j <= numVertices; j++) {
                adds[i][j] = muls[i][j] = -1;
            }
        }
        descendants = new Set[numVertices + 1];
        TreeSet<Integer> allNums = new TreeSet<>();
        for (int i = 1; i <= numVertices; i++) {
            ss = reader.readLine().trim();
            while (ss.equals("")) {
                ss = reader.readLine().trim();
            }
            allNums.add(i);
            String[] tmp = ss.trim().replaceAll("\\p{javaWhitespace}+", " ").split(" ");
            for (String s : tmp) {
                relations[i][Integer.parseInt(s)] = 1;
            }
            relations[i][i] = 1;
        }
        Set<Integer> notZeros = new HashSet<>();
        Set<Integer> notOne = new HashSet<>();
        for (int i = 1; i <= numVertices; i++) {
            descendants[i] = new HashSet<>();
            for (int j = 1; j <= numVertices; j++) {
                if (relations[i][j] == 1 && i != j) {
                    descendants[i].add(j);
                    notZeros.add(j);
                    notOne.add(i);
                }
            }
        }
        allNums.removeAll(notZeros);
        if (allNums.size() == 1) {
            zero = allNums.first();
        }
        allNums.addAll(notZeros);
        allNums.removeAll(notOne);
        if (allNums.size() == 1) {
            one = allNums.first();
        }
        for (int k = 1; k <= numVertices; k++) {
            for (int i = 1; i <= numVertices; i++) {
                for (int j = 1; j <= numVertices; j++) {
                    if (relations[i][k] == 1 && relations[k][j] == 1) {
                        relations[i][j] = 1;
                    }
                }
            }
        }
        for (int i = 1; i <= numVertices; i++) {
            for (int j = 1; j <= numVertices; j++) {
                if (add(i, j) == 0) {
                    writer.write("Операция '+' не определена: " + i + "+" + j);
                    writer.close();
                    return;
                }
            }
        }
        for (int i = 1; i <= numVertices; i++) {
            for (int j = 1; j <= numVertices; j++) {
                if (mul(i, j) == 0) {
                    writer.write("Операция '*' не определена: " + i + "*" + j);
                    writer.close();
                    return;
                }
            }
        }
        for (int i = 1; i <= numVertices; i++) {
            for (int j = 1; j <= numVertices; j++) {
                assert muls[i][j] == muls[j][i] : "muls";
                assert adds[i][j] == adds[j][i] : "adds";
            }
        }
        for (int i = 1; i <= numVertices; i++) {
            for (int j = 1; j <= numVertices; j++) {
                for (int k = 1; k <= numVertices; k++) {
                    if (!distr(i, j, k)) {
                        writer.write("Нарушается дистрибутивность: " + i + "*(" + j + "+" + k + ")");
                        writer.close();
                        return;
                    }
                }
            }
        }
        for (int i = 1; i <= numVertices; i++) {
            for (int j = 1; j <= numVertices; j++) {
                if (pseudoCompl(i, j) == 0) {
                    writer.write("Операция '->' не определена: " + i + "->" + j);
                    writer.close();
                    return;
                }
            }
        }
        if (zero == 0 || one == 0) {
            for (; ; ) {

            }
        }
        for (int i = 1; i <= numVertices; i++) {
            if (!boolAlg(i)) {
                writer.write("Не булева алгебра: " + i + "+~" + i);
                writer.close();
                return;
            }
        }
        writer.write("Булева алгебра");
        writer.close();
    }

    public static void main(String[] args) throws IOException {
        new Lattices().run();
    }
}
