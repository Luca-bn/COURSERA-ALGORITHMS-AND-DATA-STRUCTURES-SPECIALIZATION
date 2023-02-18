import java.io.*;
import java.util.*;

/*
    Task. The airline offers a bunch of flights and has a set of crews that can work on those flights. However,
    the flights are starting in different cities and at different times, so only some of the crews are able to
    work on a particular flight. You are given the pairs of flights and associated crews that can work on
    those flights. You need to assign crews to as many flights as possible and output all the assignments.
    
    Input Format. The first line of the input contains integers 𝑛 and 𝑚 — the number of flights and the number
    of crews respectively. Each of the next 𝑛 lines contains 𝑚 binary integers (0 or 1). If the 𝑗-th integer
    in the 𝑖-th line is 1, then the crew number 𝑗 can work on the flight number 𝑖, and if it is 0, then it
    cannot.

    Constraints. 1 ≤ 𝑛,𝑚 ≤ 100.
    
    Output Format. Output 𝑛 integers — for each flight, output the 1-based index of the crew assigned to
    this flight. If no crew is assigned to a flight, output −1 as the index of the crew corresponding to it.
    All the positive indices in the output must be between 1 and 𝑚, and they must be pairwise different,
    but you can output any number of −1’s. If there are several assignments with the maximum possible
    number of flights having a crew assigned, output any of them.
*/

// Good job! (Max time used: 0.30/1.50, max memory used: 42545152/2147483648.)
public class AirlineCrews {
    private FastScanner in;
    private PrintWriter out;

    public static void main(String[] args) throws IOException {
        new AirlineCrews().solve();
    }

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        boolean[][] bipartiteGraph = readData();
        int[] matching = findMatching(bipartiteGraph);
        writeResponse(matching);
        out.close();
    }

    boolean[][] readData() throws IOException {
        int numLeft = in.nextInt();
        int numRight = in.nextInt();
        boolean[][] adjMatrix = new boolean[numLeft][numRight];
        for (int i = 0; i < numLeft; ++i)
            for (int j = 0; j < numRight; ++j)
                adjMatrix[i][j] = (in.nextInt() == 1);
        return adjMatrix;
    }

    private int[] findMatching(boolean[][] bipartiteGraph) {
        int leftNums = bipartiteGraph.length;
        int rightNums = bipartiteGraph[0].length;
        int[] matching = new int[leftNums];
        Arrays.fill(matching, -1);

        // creating adjacency matrix with source and target
        int[][] residualGraph = new int[leftNums + rightNums + 2][];
        for (int i = 0; i < residualGraph.length; i++)
            residualGraph[i] = new int[residualGraph.length];
        for (int i = 1; i <= leftNums; i++)
            residualGraph[0][i] = 1;
        for (int i = residualGraph.length - 2; i > leftNums; i--)
            residualGraph[i][residualGraph.length - 1] = 1;
        for (int i = 0; i < leftNums; i++)
            for (int j = 0; j < rightNums; j++)
                if (bipartiteGraph[i][j])
                    residualGraph[i + 1][leftNums + 1 + j] = 1;

        // performing max capacity algorithm
        int source = 0;
        int sink = residualGraph.length - 1;
        int[] parents = new int[residualGraph.length];
        Arrays.fill(parents, -1);
        int minCap = 0;
        while ((minCap = bfs(residualGraph, source, sink, parents)) != 0) {
            int u = sink;
            while (u != source) {
                int v = parents[u];
                residualGraph[u][v] += minCap;
                residualGraph[v][u] -= minCap;
                u = v;
            }
        }

        // reconstructing matching
        // starting from target, going back to right nodes, than to left ones
        for(int i = leftNums + 1; i < residualGraph.length - 1; i++) {
            if(residualGraph[residualGraph.length - 1][i] == 1) {
                for(int j = 1; j < leftNums + 1; j++) {
                    if(residualGraph[i][j] == 1) {
                        matching[j - 1] = i - (leftNums + 1);
                    }
                }
            }
        }

        return matching;
    }

    private int bfs(int[][] adjMatrix, int source, int sink, int[] parents) {
        Arrays.fill(parents, -1);
        parents[source] = -2;
        PriorityQueue<Integer[]> queue = new PriorityQueue<>((a1, a2) -> a2[1].compareTo(a1[1]));
        queue.add(new Integer[] { source, Integer.MAX_VALUE });
        while (!queue.isEmpty()) {
            int u = queue.peek()[0];
            int capacity = queue.peek()[1];
            queue.poll();
            for (int av = 0; av < adjMatrix.length; av++) {
                if (u != av && parents[av] == -1 && adjMatrix[u][av] != 0) {
                    parents[av] = u;
                    int minCap = Math.min(capacity, adjMatrix[u][av]);
                    if (av == sink)
                        return minCap;
                    queue.add(new Integer[] { av, minCap });
                }
            }
        }
        return 0;
    }

    private void writeResponse(int[] matching) {
        for (int i = 0; i < matching.length; ++i) {
            if (i > 0) {
                out.print(" ");
            }
            if (matching[i] == -1) {
                out.print("-1");
            } else {
                out.print(matching[i] + 1);
            }
        }
        out.println();
    }

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}
