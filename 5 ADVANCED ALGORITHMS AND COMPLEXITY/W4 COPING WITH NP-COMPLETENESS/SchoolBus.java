import java.io.*;
import java.util.*;

/*
    Task. A school bus needs to start from the depot early in the morning, pick up all the children from their
    homes in some order, get them all to school and return to the depot. You know the time it takes to
    get from depot to each home, from each home to each other home, from each home to the school and
    from the school to the depot. You want to define the order in which to visit children’s homes so as to
    minimize the total time spent on the route.
    This is an instance of a classical NP-complete problem called Traveling Salesman Problem. Given a
    graph with weighted edges, you need to find the shortest cycle visiting each vertex exactly once. Vertices
    correspond to homes, the school and the depot. Edges weights correspond to the time to get from one
    vertex to another one. Some vertices may not be connected by an edge in the general case.
    
    Input Format. The first line contains two integers 𝑛 and 𝑚 — the number of vertices and the number of
    edges in the graph. The vertices are numbered from 1 through 𝑛. Each of the next 𝑚 lines contains
    three integers 𝑢, 𝑣 and 𝑡 representing an edge of the graph. This edge connects vertices 𝑢 and 𝑣, and
    it takes time 𝑡 to get from 𝑢 to 𝑣. The edges are bidirectional: you can go both from 𝑢 to 𝑣 and from
    𝑣 to 𝑢 in time 𝑡 using this edge. No edge connects a vertex to itself. No two vertices are connected by
    more than one edge.

    Constraints. 2 ≤ 𝑛 ≤ 17; 1 ≤ 𝑚 ≤ 𝑛(𝑛−1) / 2 ; 1 ≤ 𝑢, 𝑣 ≤ 𝑛; 𝑢 ̸= 𝑣; 1 ≤ 𝑡 ≤ 10^9.

    Output Format. If it is possible to start in some vertex, visit each other vertex exactly once in some order
    going by edges of the graph and return to the starting vertex, output two lines. On the first line, output
    the minimum possible time to go through such circular route visiting all vertices exactly once (apart
    from the first vertex which is visited twice — in the beginning and in the end). On the second line,
    output the order in which you should visit the vertices to get the minimum possible time on the route.
    That is, output the numbers from 1 through 𝑛 in the order corresponding to visiting the vertices. Don’t
    output the starting vertex second time. However, account for the time to get from the last vertex back
    to the starting vertex. If there are several solutions, output any one of them. If there is no such circular
    route, output just −1 on a single line. Note that for 𝑛 = 2 it is considered a correct circular route to
    go from one vertex to another by an edge and then return back by the same edge.
*/

// Good job! (Max time used: 0.30/1.50, max memory used: 59338752/2147483648.)

public class SchoolBus {
    private static FastScanner in;

    public static void main(String[] args) {
        new Thread(null, new Runnable() {
            public void run() {
                try {
                    in = new FastScanner();
                    // printAnswer(tsp(readData(), 0));
                    TravellingSalesmanProblemSolver tspSolver = new TravellingSalesmanProblemSolver(readData());
                    Double tourCost = tspSolver.getTourCost();
                    if(Double.POSITIVE_INFINITY == tourCost) {
                        System.out.println("-1");
                        return;
                    }
                    System.out.println(tourCost.intValue());
                    System.out.println(tspSolver.getTour());
                } catch (Exception exception) {
                    System.err.print("Error during reading: " + exception.toString());
                }
            }
        }, "1", 1 << 26).start();
    }

    private static double[][] readData() throws IOException {
        int n = in.nextInt();
        int m = in.nextInt();
        double[][] distances = new double[n][n];

        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j) {
                distances[i][j] = Double.POSITIVE_INFINITY;
            }

        for (int i = 0; i < m; ++i) {
            int u = in.nextInt() - 1;
            int v = in.nextInt() - 1;
            int weight = in.nextInt();
            distances[u][v] = Double.valueOf(weight);
            distances[v][u] = distances[u][v];
        }
        return distances;
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

    static class TravellingSalesmanProblemSolver {
        private final int N, start;
        private final double[][] distance;
        private List<Integer> tour = new ArrayList<>();
        private double minTourCost = Double.POSITIVE_INFINITY;
        private boolean ranSolver = false;

        TravellingSalesmanProblemSolver(double[][] distance) {
            this.distance = distance;
            this.N = distance.length;
            this.start = 0;
        }

        // Returns the optimal tour for the traveling salesman problem.
        public String getTour() {
            if (!ranSolver)
                solve();
            StringBuilder path = new StringBuilder();
            for(int x = 0; x < tour.size() - 1; x++)
                path.append(tour.get(x) + 1).append(" ");
            return path.toString();
        }

        // Returns the minimal tour cost.
        public double getTourCost() {
            if (!ranSolver)
                solve();
            return minTourCost;
        }

        // Solves the traveling salesman problem and caches solution.
        public void solve() {

            if (ranSolver)
                return;

            final int END_STATE = (1 << N) - 1;
            Double[][] memo = new Double[N][1 << N];

            // Add all outgoing edges from the starting node to memo table.
            for (int end = 0; end < N; end++) {
                if (end == start)
                    continue;
                memo[end][(1 << start) | (1 << end)] = distance[start][end];
            }

            List<List<Integer>> combinations = combinations(N);
            for (int r = 3; r <= N; r++) {
                for (int subset : combinations.get(r)) {
                    if (notIn(start, subset))
                        continue;
                    for (int next = 0; next < N; next++) {
                        if (next == start || notIn(next, subset))
                            continue;
                        int subsetWithoutNext = subset ^ (1 << next);
                        double minDist = Double.POSITIVE_INFINITY;
                        for (int end = 0; end < N; end++) {
                            if (end == start || end == next || notIn(end, subset))
                                continue;
                            double newDistance = memo[end][subsetWithoutNext] + distance[end][next];
                            if (newDistance < minDist) {
                                minDist = newDistance;
                            }
                        }
                        memo[next][subset] = minDist;
                    }
                }
            }

            // Connect tour back to starting node and minimize cost.
            for (int i = 0; i < N; i++) {
                if (i == start)
                    continue;
                double tourCost = memo[i][END_STATE] + distance[i][start];
                if (tourCost < minTourCost) {
                    minTourCost = tourCost;
                }
            }

            int lastIndex = start;
            int state = END_STATE;
            tour.add(start);

            // Reconstruct TSP path from memo table.
            for (int i = 1; i < N; i++) {

                int index = -1;
                for (int j = 0; j < N; j++) {
                    if (j == start || notIn(j, state))
                        continue;
                    if (index == -1)
                        index = j;
                    double prevDist = memo[index][state] + distance[index][lastIndex];
                    double newDist = memo[j][state] + distance[j][lastIndex];
                    if (newDist < prevDist) {
                        index = j;
                    }
                }

                tour.add(index);
                state = state ^ (1 << index);
                lastIndex = index;
            }

            tour.add(start);
            Collections.reverse(tour);

            ranSolver = true;
        }

        private static boolean notIn(int elem, int subset) {
            return ((1 << elem) & subset) == 0;
        }

        private static List<List<Integer>> combinations(int N) {
            List<List<Integer>> combinations = new ArrayList<>();
            for (int i = 0; i < N + 1; i++)
                combinations.add(new ArrayList<>());
            for (int i = 0; i < (1 << N); i++) {
                int set = 0;
                for (int j = 0; j < N; j++)
                    if (((1 << j) & i) != 0)
                        set = (1 << j) | set;
                combinations.get(Integer.bitCount(set)).add(set);
            }
            return combinations;
        }
    }

}
