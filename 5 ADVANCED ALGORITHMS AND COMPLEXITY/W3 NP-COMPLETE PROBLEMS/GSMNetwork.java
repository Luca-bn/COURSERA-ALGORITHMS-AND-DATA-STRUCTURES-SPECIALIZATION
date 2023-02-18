import java.io.*;
import java.util.Locale;
import java.util.StringTokenizer;

/*
    Task. GSM network is a type of infrastructure used for communication via mobile phones. It includes
    transmitting towers scattered around the area which operate in different frequencies. Typically there is
    one tower in the center of each hexagon called “cell” on the grid above — hence the name “cell phone”.
    A cell phone looks for towers in the neighborhood and decides which one to use based on strength
    of signal and other properties. For a phone to distinguish among a few closest towers, the frequencies
    of the neighboring towers must be different. You are working on a plan of GSM network for mobile,
    and you have a restriction that you’ve only got 3 different frequencies from the government which you
    can use in your towers. You know which pairs of the towers are neighbors, and for all such pairs the
    towers in the pair must use different frequencies. You need to determine whether it is possible to assign
    frequencies to towers and satisfy these restrictions.
    This is equivalent to a classical graph coloring problem: in other words, you are given a graph, and
    you need to color its vertices into 3 different colors, so that any two vertices connected by an edge
    need to be of different colors. Colors correspond to frequencies, vertices correspond to cells, and edges
    connect neighboring cells. Graph coloring is an NP-complete problem, so we don’t currently know an
    efficient solution to it, and you need to reduce it to an instance of SAT problem which, although it is
    NP-complete, can often be solved efficiently in practice using special programs called SAT-solvers.
    
    Input Format. The first line of the input contains integers 𝑛 and 𝑚 — the number of vertices and edges in
    the graph. The vertices are numbered from 1 through 𝑛. Each of the next 𝑚 lines contains two integers
    𝑢 and 𝑣 — the numbers of vertices connected by an edge. It is guaranteed that a vertex cannot be
    connected to itself by an edge.
    
    Constraints. 2 ≤ 𝑛 ≤ 500; 1 ≤ 𝑚 ≤ 1000; 1 ≤ 𝑢, 𝑣 ≤ 𝑛; 𝑢 ̸= 𝑣.
    
    Output Format. You need to output a boolean formula in the conjunctive normal form (CNF) in a specific
    format. If it is possible to color the vertices of the input graph in 3 colors such that any two vertices
    connected by an edge are of different colors, the formula must be satisfiable. Otherwise, the formula
    must be unsatisfiable. The number of variables in the formula must be at least 1 and at most 3000.
    The number of clauses must be at least 1 and at most 5000.
    On the first line, output integers 𝐶 and 𝑉 — the number of clauses in the formula and the number of
    variables respectively. On each of the next 𝐶 lines, output a description of a single clause. Each clause
    has a form (𝑥4 𝑂𝑅 𝑥1 𝑂𝑅 𝑥8). For a clause with 𝑘 terms (in the example, 𝑘 = 3 for 𝑥4, 𝑥1 and 𝑥8), output
    first those 𝑘 terms and then number 0 in the end (in the example, output “4 − 1 8 0”). Output each
    term as integer number. Output variables 𝑥1, 𝑥2, . . . , 𝑥𝑉 as numbers 1, 2, . . . , 𝑉 respectively. Output
    negations of variables 𝑥1, 𝑥2, . . . , 𝑥𝑉 as numbers −1,−2, . . . ,−𝑉 respectively. Each number other than
    the last one in each line must be a non-zero integer between −𝑉 and 𝑉 where 𝑉 is the total number
    of variables specified in the first line of the output. Ensure that 1 ≤ 𝐶 ≤ 5000 and 1 ≤ 𝑉 ≤ 3000.
    See the examples below for further clarification of the output format.
    If there are many different formulas that satisfy the requirements above, you can output any one of
    them.
*/

// Good job! (Max time used: 0.22/1.50, max memory used: 41922560/4294967296.)
public class GSMNetwork {
    private final InputReader reader;
    private final OutputWriter writer;

    public GSMNetwork(InputReader reader, OutputWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public static void main(String[] args) {
        InputReader reader = new InputReader(System.in);
        OutputWriter writer = new OutputWriter(System.out);
        new GSMNetwork(reader, writer).run();
        writer.writer.flush();
    }

    class Edge {
        int from;
        int to;
    }

    class ConvertGSMNetworkProblemToSat {
        int numOfColors = 3;
        int numVertices;
        Edge[] edges;

        ConvertGSMNetworkProblemToSat(int n, int m) {
            numVertices = n;
            edges = new Edge[m];
            for (int i = 0; i < m; ++i) {
                edges[i] = new Edge();
            }
        }

        void printEquisatisfiableSatFormula() {

            /*
             * I have 3 variables for each vertex (each variable is true if and only if that
             * vertex has that color)
             * for example, for vertex 1, i have 3 variables x1 (true if vertex 1 is red),
             * x2 (true if vertex 1 is green), x3 (true if vertex 1 is blue);
             * for vertex 2 i have x4 (v2 is red), x5 (v2 is green), x6 (v2 is blue), and so on
             * for each vertex.
             * 
             * So, for each vertex I have to add 4 clauses (each && is a different clause):
             * (xi1 || xi2 || xi3) && (!xi1 || !xi2) && (!xi1 || !xi3) && !(!xi2 || !xi3).
             * This clauses means that each vertex can have at most one single color (for
             * this numOfClauses = x + (numVertices * 4)).
             * 
             * After that, for each edge I have to add 3 clauses:
             * (!xi1 || !xp1) && (!xi2 || !xp2) && (!xi3 || !xp3).
             * which means that for each edge, the 2 nodes cannot have the same color.
             * 
             * for this numOfClauses = (edges.length * numOfColors) + y;
             */
            int numOfVariables = numVertices * numOfColors;
            int numOfClauses = (edges.length * numOfColors) + (numVertices * 4);

            // printing number of clauses and variables
            writer.printf(numOfClauses + " " + numOfVariables + "\n");
            // printing clauses related to vertexes's color
            for (int i = 0; i < numVertices; i++)
                printAtLeastOneColorTrueForNodeClauses(i);

            // printing clauses related to different color for adjacent vertexes
            for (Edge edge : edges)
                printDifferentColorForAdjacentNodesClauses(edge);

        }

        private void printAtLeastOneColorTrueForNodeClauses(int vertex) {
            // vertex must have color 1 or color 2 or color 3 (red || green || blue)
            for (int i = vertex * numOfColors; i < (vertex * numOfColors) + numOfColors; i++)
                writer.printf((i + 1) + " ");
            writer.printf("0\n");
            // excluding cases where vertex has 2 colors at same time (!red || !green) &&
            // (!red || !blue) && (!green || !blue)
            writer.printf("-" + ((vertex * numOfColors) + 1) + " -" + ((vertex * numOfColors) + 2) + " 0\n");
            writer.printf("-" + ((vertex * numOfColors) + 1) + " -" + ((vertex * numOfColors) + 3) + " 0\n");
            writer.printf("-" + ((vertex * numOfColors) + 2) + " -" + ((vertex * numOfColors) + 3) + " 0\n");
        }

        private void printDifferentColorForAdjacentNodesClauses(Edge edge) {
            // !vertex P color || !vertex I color
            for (int i = 0; i < numOfColors; i++) {
                // writer.printf("== edge: "+edge.from + "->" +edge.to + " ==\n");
                writer.printf("-" + (((edge.from - 1) * numOfColors) + i + 1) + " -"
                        + (((edge.to - 1) * numOfColors) + i + 1) + " 0\n");
            }
        }
    }

    public void run() {
        int n = reader.nextInt();
        int m = reader.nextInt();

        ConvertGSMNetworkProblemToSat converter = new ConvertGSMNetworkProblemToSat(n, m);
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
