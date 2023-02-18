import java.util.*;
import java.io.*;

/*
    Task. Find a k-universal circular binary string.

    Input Format. An integer k.
    
    Constraints. 4 <= k <= 14.
    
    Output Format. A k-universal circular string. (If multiple answers exist, you may return any one.)
*/

// Good job! (Max time used: 0.87/1.50, max memory used: 96264192/2147483648.)
public class UniversalKmerBinaryString {

    static int K;

    public static void main(String[] args) {
        new Thread(null, new Runnable() {
            public void run() {
                try {
                    Scanner in = new Scanner(System.in);
                    K = in.nextInt();
                    in.close();

                    int numberOfVertices = (int) Math.pow(2, K - 1);

                    EulerianCycleSolver solver = new EulerianCycleSolver(numberOfVertices, numberOfVertices * 2);
                    for (int i = 0; i < numberOfVertices; i++) {
                        solver.addEdge(i, (i * 2) % numberOfVertices);
                        solver.addEdge(i, ((i * 2) % numberOfVertices) + 1);
                    }
                    LinkedList<Vertex> cycle = solver.findEulerianCycle();
                    for (Vertex v : cycle) {
                        System.out.print(v.binaryString.charAt(0));
                        // System.out.print(v.binaryString);
                        // System.out.print(" -> ");
                    }
                } catch (Exception e) {
                }
            }
        }, "1", 1 << 26).start();
    }

    static class EulerianCycleSolver {

        int numberOfVertices, numberOfEdges;
        int exploredEdges = 0;
        List<Vertex> graph = new ArrayList<>();

        public EulerianCycleSolver(int n, int m) {
            this.numberOfVertices = n;
            this.numberOfEdges = m;
            for (int i = 0; i < numberOfVertices; i++)
                graph.add(new Vertex(i));
        }

        public void addEdge(int v, int u) {
            Vertex from = graph.get(v);
            Vertex to = graph.get(u);
            from.outEdges.add(to);
            from.outEdgesCount++;
        }

        public LinkedList<Vertex> findEulerianCycle() {
            LinkedList<Vertex> cycle = new LinkedList<>();

            Vertex starting = graph.get(0);

            dfs(starting, cycle);

            return cycle;
        }

        private void dfs(Vertex v, LinkedList<Vertex> cycle) {
            while (v.outEdgesCount > 0) {
                Vertex next = v.outEdges.get(--v.outEdgesCount);
                dfs(next, cycle);
            }
            cycle.addFirst(v);
        }

    }

    static class Vertex {
        final int id;
        final String binaryString;
        List<Vertex> outEdges = new ArrayList<>();
        int outEdgesCount = 0;

        public Vertex(int id) {
            this.id = id;
            this.binaryString = String.format("%1$" + (K - 1) + "s", Integer.toBinaryString(id)).replaceAll(" ", "0");
        }
    }
}
