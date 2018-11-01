import lattices.Lattices;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) throws IOException {
        while (true) {
            run();
        }
    }

    static Random rnd = new Random(432);

    static void run() throws IOException {
        PrintWriter printWriter = new PrintWriter("input.txt");
        int n = 3 + rnd.nextInt(4);
        printWriter.println(n);
        for (int i = 0; i < n; i++) {
            for (int j = 1; j <= n; j++) {
                if (rnd.nextDouble() < 0.2 && j != i + 1) {
                    printWriter.print(j + " ");
                }
            }
            printWriter.println(i + 1);
        }
        printWriter.close();
        new Lattices().run();
        Scanner sc = new Scanner(new File("output.txt"));
        String s = sc.nextLine();

        if (s.contains("Булева")) { //s.equals("Булева алгебра")) {
            System.out.println(n);
            //System.exit(0);
        }
        sc.close();
    }
}
