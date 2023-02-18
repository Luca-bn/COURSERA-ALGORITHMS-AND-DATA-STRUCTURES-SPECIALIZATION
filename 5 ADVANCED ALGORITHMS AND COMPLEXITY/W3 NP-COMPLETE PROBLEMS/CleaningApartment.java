import java.io.*;
import java.util.*;

/*
    Task. You’ve just had a huge party in your parents’ house, and they are returning tomorrow. You need
    to not only clean the apartment, but leave no trace of the party. To do that, you need to clean all
    the rooms in some order. After finishing a thorough cleaning of some room, you cannot return to it
    anymore: you are afraid you’ll ruin everything accidentally and will have to start over. So, you need to
    move from room to room, visit each room exactly once and clean it. You can only move from a room
    to the neighboring rooms. You want to determine whether this is possible at all.
    This can be reduced to a classic Hamiltonian Path problem: given a graph, determine whether there is
    a route visiting each vertex exactly once. Rooms are vertices of the graph, and neighboring rooms are
    connected by edges. Hamiltonian Path problem is NP-complete, so we don’t know an efficient algorithm
    to solve it. You need to reduce it to SAT, so that it can be solved efficiently by a SAT-solver.
    
    Input Format. The first line contains two integers 𝑛 and 𝑚 — the number of rooms and the number of
    corridors connecting the rooms respectively. Each of the next 𝑚 lines contains two integers 𝑢 and 𝑣
    describing the corridor going from room 𝑢 to room 𝑣. The corridors are two-way, that is, you can go
    both from 𝑢 to 𝑣 and from 𝑣 to 𝑢. No two corridors have a common part, that is, every corridor only
    allows you to go from one room to one other room. Of course, no corridor connects a room to itself.
    Note that a corridor from 𝑢 to 𝑣 can be listed several times, and there can be listed both a corridor
    from 𝑢 to 𝑣 and a corridor from 𝑣 to 𝑢.
    
    Constraints. 1 ≤ 𝑛 ≤ 30; 0 ≤ 𝑚 ≤ 𝑛(𝑛−1) / 2 ; 1 ≤ 𝑢, 𝑣 ≤ 𝑛.
    
    Output Format. You need to output a boolean formula in the CNF form in a specific format. If it is
    possible to go through all the rooms and visit each one exactly once to clean it, the formula must be
    satisfiable. Otherwise, the formula must be unsatisfiable. The sum of the numbers of variables used in
    each clause of the formula must not exceed 120 000.
    On the first line, output integers 𝐶 and 𝑉 — the number of clauses in the formula and the number of
    variables respectively. On each of the next 𝐶 lines, output a description of a single clause. Each clause
    has a form (𝑥4 𝑂𝑅 𝑥1 𝑂𝑅 𝑥8). For a clause with 𝑘 terms (in the example, 𝑘 = 3 for 𝑥4, 𝑥1 and 𝑥8), output
    first those 𝑘 terms and then number 0 in the end (in the example, output “4 − 1 8 0”). Output each
    term as integer number. Output variables 𝑥1, 𝑥2, . . . , 𝑥𝑉 as numbers 1, 2, . . . , 𝑉 respectively. Output
    negations of variables 𝑥1, 𝑥2, . . . , 𝑥𝑉 as numbers −1,−2, . . . ,−𝑉 respectively. Each number other than
    the last one in each line must be a non-zero integer between −𝑉 and 𝑉 where 𝑉 is the total number
    of variables specified in the first line of the output. Ensure that the total number of non-zero integers
    in the 𝐶 lines describing the clauses is at most 120 000.
    See the examples below for further clarification of the output format.
    If there are many different formulas that satisfy the requirements above, you can output any one of
    them.
*/

// Good job! (Max time used: 0.70/3.00, max memory used: 94777344/2147483648.)
public class CleaningApartment {
    private final InputReader reader;
    private final OutputWriter writer;

    public CleaningApartment(InputReader reader, OutputWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public static void main(String[] args) {
        InputReader reader = new InputReader(System.in);
        OutputWriter writer = new OutputWriter(System.out);
        new CleaningApartment(reader, writer).run();
        writer.writer.flush();
    }

    class Edge {
        int from;
        int to;
    }

    class ConvertHampathToSat {
        int numVertices;
        Edge[] edges;

        ConvertHampathToSat(int n, int m) {
            numVertices = n;
            edges = new Edge[m];
            for (int i = 0; i < m; ++i) {
                edges[i] = new Edge();
            }
        }

        void printEquisatisfiableSatFormula() {
            /*
             * The Clauses of R(G) and Their Intended Meanings
             *
             * 1. Each node j must appear in the path.
             * • x1j ∨ x2j ∨ · · · ∨ xnj for each j.
             * 
             * 2. No node j appears twice in the path.
             * • ¬xij ∨ ¬xkj for all i, j, k with i != k.
             * 
             * 3. Every position i on the path must be occupied.
             * • xi1 ∨ xi2 ∨ · · · ∨ xin for each i.
             * 
             * 4. No two nodes j and k occupy the same position in the path.
             * • ¬xij ∨ ¬xik for all i, j, k with j != k.
             * 
             * 5. Nonadjacent nodes i and j cannot be adjacent in the path.
             * • ¬xki ∨ ¬xk+1,j for all (i, j) !∈ G and k = 1, 2, . . . , n − 1.
             */

            List<String> clauses = new ArrayList<>();

            // first point clauses (Each node j must appear in the path)
            // clauses.add("== first point clauses (Each node j must appear in the path) ==\n");
            for (int vertex = 0; vertex < numVertices; vertex++) {
                StringBuilder sb = new StringBuilder();
                for (int positionInPath = 0; positionInPath < numVertices; positionInPath++) {
                    sb.append(((vertex * numVertices) + (positionInPath + 1)));
                    sb.append(" ");
                }
                sb.append("0\n");
                clauses.add(sb.toString());
            }

            // second point clauses (No node j appears twice in the path)
            // clauses.add("== second point clauses (No node j appears twice in the path) ==\n");
            for (int vertex = 0; vertex < numVertices; vertex++) {
                for (int positionStart = 0; positionStart < numVertices - 1; positionStart++) {
                    for (int nextPosition = positionStart + 1; nextPosition < numVertices; nextPosition++) {
                        StringBuilder sb = new StringBuilder("-")
                                .append(((vertex * numVertices) + positionStart + 1))
                                .append(" -")
                                .append(((vertex * numVertices) + nextPosition + 1))
                                .append(" 0\n");
                        clauses.add(sb.toString());
                    }
                }
            }

            // third point clauses (Every position i on the path must be occupied)
            // clauses.add("== third point clauses (Every position i on the path must be occupied) ==\n");
            for (int positionInPath = 0; positionInPath < numVertices; positionInPath++) {
                StringBuilder sb = new StringBuilder();
                for (int vertex = 0; vertex < numVertices; vertex++) {
                    sb.append(((positionInPath) + (vertex * numVertices) + 1));
                    sb.append(" ");
                }
                sb.append("0\n");
                clauses.add(sb.toString());
            }

            // fourth point clauses (No two nodes j and k occupy the same position in the
            // path)
            // clauses.add("== fourth point clauses (No two nodes j and k occupy the same position in the path) ==\n");
            for (int positionInPath = 0; positionInPath < numVertices; positionInPath++) {
                for (int vertexStart = 0; vertexStart < numVertices - 1; vertexStart++) {
                    for (int nextVertex = vertexStart + 1; nextVertex < numVertices; nextVertex++) {
                        StringBuilder sb = new StringBuilder("-")
                                .append(((vertexStart * numVertices) + positionInPath + 1))
                                .append(" -")
                                .append(((nextVertex * numVertices) + positionInPath + 1))
                                .append(" 0\n");
                        clauses.add(sb.toString());
                    }
                }
            }

            // last point clauses (Nonadjacent nodes i and j cannot be adjacent in the path)

            // first i create an adjacency matrix to find non-adjacent nodes
            int[][] adjMatrix = new int[numVertices][];
            for (int i = 0; i < numVertices; i++)
                adjMatrix[i] = new int[numVertices];
            for (Edge edge : edges) {
                adjMatrix[edge.from - 1][edge.to - 1] = 1;
                // edges are bidirectional, so if there is an edge from a to b, there is also
                // from b to a
                adjMatrix[edge.to - 1][edge.from - 1] = 1;
            }
            // printMatrix(adjMatrix);

            // creating clauses for last point
            // clauses.add("== last point clauses (Nonadjacent nodes i and j cannot be adjacent in the path) ==\n");
            for (int currentNode = 0; currentNode < numVertices; currentNode++) {
                int[] edges = adjMatrix[currentNode];
                for (int edgeTo = 0; edgeTo < numVertices; edgeTo++) {
                    if (currentNode == edgeTo)
                        continue;
                    if (edges[edgeTo] == 0) {
                        // there is no edge between this 2 nodes, so create clauses
                        for(int position = 0; position < numVertices - 1; position++) {
                            StringBuilder sb = new StringBuilder("-")
                            .append(((currentNode * numVertices) + position + 1))
                            .append(" -")
                            .append(((edgeTo * numVertices) + position + 2))
                            .append(" 0\n");
                            clauses.add(sb.toString());
                        }
                    }
                }
            }

            // printing clauses
            writer.printf(clauses.size() + " " + (numVertices * numVertices) + "\n");
            for(String clause : clauses)
                writer.printf(clause);
        }

        private void printMatrix(int[][] adjMatrix) {
            System.out.print(" | ");
            for(int i = 1; i <= numVertices; i++)
                System.out.print(i + " |");
            System.out.println();
            for(int i = 1; i <= numVertices; i++) {
                System.out.print(i + "| ");
                for(int j = 1; j <= numVertices; j++) {
                    if(i == j)
                        System.out.print("- |");
                    else    
                        System.out.print(adjMatrix[i - 1][j - 1] + " |");
                }
                System.out.println();
            }
        }
    }

    public void run() {
        int n = reader.nextInt();
        int m = reader.nextInt();

        ConvertHampathToSat converter = new ConvertHampathToSat(n, m);
        for (int i = 0; i < m; ++i) {
            converter.edges[i].from = reader.nextInt();
            converter.edges[i].to = reader.nextInt();
        }

        converter.printEquisatisfiableSatFormula();
    }

    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public double nextDouble() {
            return Double.parseDouble(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    static class OutputWriter {
        public PrintWriter writer;

        OutputWriter(OutputStream stream) {
            writer = new PrintWriter(stream);
        }

        public void printf(String format, Object... args) {
            writer.print(String.format(Locale.ENGLISH, format, args));
        }
    }
}
