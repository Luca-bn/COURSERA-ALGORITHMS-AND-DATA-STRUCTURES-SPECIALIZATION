import java.io.*;
import java.util.*;

/*
    Task. VLSI or Very Large-Scale Integration is a process of creating an integrated circuit by combining
    thousands of transistors on a single chip. You want to design a single layer of an integrated circuit.
    You know exactly what modules will be used in this layer, and which of them should be connected by
    wires. The wires will be all on the same layer, but they cannot intersect with each other. Also, each
    wire can only be bent once, in one of two directions β to the left or to the right. If you connect two
    modules with a wire, selecting the direction of bending uniquely defines the position of the wire. Of
    course, some positions of some pairs of wires lead to intersection of the wires, which is forbidden. You
    need to determine a position for each wire in such a way that no wires intersect.
    This problem can be reduced to 2-SAT problem β a special case of the SAT problem in which each
    clause contains exactly 2 variables. For each wire π, denote by π₯π a binary variable which takes value 1
    if the wire is bent to the right and 0 if the wire is bent to the left. For each π, π₯π must be either 0 or 1.
    Also, some pairs of wires intersect in some positions. For example, it could be so that if wire 1 is bent
    to the left and wire 2 is bent to the right, then they intersect. We want to write down a formula which
    is satisfied only if no wires intersect. In this case, we will add the clause (π₯1 ππ π₯2) to the formula
    which ensures that either π₯1 (the first wire is bent to the right) is true or π₯2 (the second wire is bent
    to the left) is true, and so the particular crossing when wire 1 is bent to the left AND wire 2 is bent to
    the right doesnβt happen whenever the formula is satisfied. We will add such a clause for each pair of
    wires and each pair of their positions if they intersect when put in those positions. Of course, if some
    pair of wires intersects in any pair of possible positions, we wonβt be able to design a circuit. Your task
    is to determine whether it is possible, and if yes, determine the direction of bending for each of the
    wires.

    Input Format. The input represents a 2-CNF formula. The first line contains two integers π and πΆ β
    the number of variables and the number of clauses respectively. Each of the next πΆ lines contains two
    non-zero integers π and π representing a clause in the CNF form. If π > 0, it represents π₯π, otherwise
    if π < 0, it represents π₯βπ, and the same goes for π. For example, a line β2 3β represents a clause
    (π₯2 ππ π₯3), line β1 -4β represents (π₯1 ππ π₯4), line β-1 -3β represents (π₯1 ππ π₯3), and line β0 2β
    cannot happen, because π and π must be non-zero.

    Constraints. 1 β€ π,πΆ β€ 1 000 000; βπ β€ π, π β€ π ; π, π ΜΈ= 0.
    
    Output Format. If the 2-CNF formula in the input is unsatisfiable, output just the word βUNSATISFIABLEβ
    (without quotes, using capital letters). If the 2-CNF formula in the input is satisfiable, output
    the word βSATISFIABLEβ (without quotes, using capital letters) on the first line and the corresponding
    assignment of variables on the second line. For each π₯π in order from π₯1 to π₯π , output π if π₯π = 1 or
    βπ if π₯π = 0. For example, if a formula is satisfied by assignment π₯1 = 0, π₯2 = 1, π₯3 = 0,
    output β-1 2 -3β on the second line (without quotes). If there are several possible assignments satisfying
    the input formula, output any one of them.
*/

// Good job! (Max time used: 1.30/18.00, max memory used: 165564416/4294967296.)
public class CircuitDesign {
    private final InputReader reader;
    private final OutputWriter writer;

    public CircuitDesign(InputReader reader, OutputWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public static void main(String[] args) {
        new Thread(null, new Runnable() {
            public void run() {
                try {
                    InputReader reader = new InputReader(System.in);
                    OutputWriter writer = new OutputWriter(System.out);
                    new CircuitDesign(reader, writer).run();
                    writer.writer.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "1", 1 << 26).start();
    }

    public void run() {
        int n = reader.nextInt();
        int m = reader.nextInt();

        TwoSATSolver twoSatSolver = new TwoSATSolver(n);
        List<TwoSatClause> clauses = new ArrayList<>();
        for (int i = 0; i < m; ++i) {
            TwoSatClause clause = new TwoSatClause();
            clause.firstVar = reader.nextInt();
            clause.secondVar = reader.nextInt();
            clauses.add(clause);
        }

        twoSatSolver.solve(clauses);
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

    static class TwoSATSolver {

        boolean debug = true;
        final int UNVISITED = -1;
        boolean[] used;
        int[] scc;
        List<Integer> order;
        int numberOfVariables;
        List<List<Integer>> adjList;
        List<List<Integer>> adjListR;

        TwoSATSolver(int numberOfVariables) {
            this.numberOfVariables = numberOfVariables;
            this.adjList = new ArrayList<>();
            this.adjListR = new ArrayList<>();
            // the adjacency list will contains from 0 to n-1 the variables
            // and from n to 2n-1 their negation
            for (int i = 0; i < numberOfVariables * 2; i++) {
                adjList.add(new ArrayList<>());
                adjListR.add(new ArrayList<>());
            }
            this.order = new ArrayList<>();
            this.scc = new int[numberOfVariables * 2];
            this.used = new boolean[numberOfVariables * 2];
        }

        void solve(List<TwoSatClause> clauses) {
            int n = numberOfVariables * 2;
            // creating implication graph (and reverse one)
            createAdjacencyFromClauses(clauses);
            if (debug)
                printGraph();

            // find the order in which perform strongly connected components (dfs order)
            for (int i = 0; i < n; i++)
                if (!used[i])
                    dfs1(i);
            if (debug)
                printOrder();

            // getting strongly connected components in reverse graph using found order
            for (int i = 0; i < n; i++)
                scc[i] = -1;
            for (int i = n - 1, j = 0; i >= 0; i--) {
                int v = order.get(i);
                if (scc[v] == -1)
                    dfs2(v, j++);
            }
            if (debug) {
                printSccs();
                System.out.println();
            }

            boolean[] assignment = new boolean[numberOfVariables];
            for (int i = 0; i < numberOfVariables; i++) {
                // if variable with its negation belong to same scc, the 2-SAT is UNSATISFIABLE
                if (scc[i] == scc[i + numberOfVariables]) {
                    System.out.println("UNSATISFIABLE");
                    return;
                }
                // if scc of variable is > then its negation, taking 1 as its value, 0 otherwise
                assignment[i] = scc[i] > scc[getOppositeIndexInAdjList(i)];
            }

            // printing solution
            System.out.println("SATISFIABLE");
            for (int i = 1; i <= numberOfVariables; i++)
                System.out.print((assignment[i - 1] ? "" : "-") + i + " ");
            System.out.print("\n");
        }

        private void dfs1(int v) {
            used[v] = true;
            for (int u : adjList.get(v))
                if (!used[u])
                    dfs1(u);
            order.add(v);
        }

        private void dfs2(int v, int cl) {
            scc[v] = cl;
            for (int u : adjListR.get(v))
                if (scc[u] == -1)
                    dfs2(u, cl);
        }

        void createAdjacencyFromClauses(List<TwoSatClause> clauses) {
            for (TwoSatClause clause : clauses)
                createAdjacencyFromClause(clause);
        }

        void createAdjacencyFromClause(TwoSatClause clause) {
            // (x1 || x2) -> !x1 => x2 && !x2 => x1
            if (debug) {
                System.out.println(String.format("\n== creating adjacency from clause : (%s || %s) ==", clause.firstVar,
                        clause.secondVar));
                System.out.println(String.format("first adjacency: %s => %s",
                        getEffectiveIndexInAdjList(-clause.firstVar), getEffectiveIndexInAdjList(clause.secondVar)));
                System.out.println(String.format("second adjacency: %s => %s",
                        getEffectiveIndexInAdjList(-clause.secondVar), getEffectiveIndexInAdjList(clause.firstVar)));
            }
            adjList.get(getEffectiveIndexInAdjList(-clause.firstVar))
                    .add(getEffectiveIndexInAdjList(clause.secondVar));
            adjList.get(getEffectiveIndexInAdjList(-clause.secondVar))
                    .add(getEffectiveIndexInAdjList(clause.firstVar));
            adjListR.get(getEffectiveIndexInAdjList(clause.secondVar))
                    .add(getEffectiveIndexInAdjList(-clause.firstVar));
            adjListR.get(getEffectiveIndexInAdjList(clause.firstVar))
                    .add(getEffectiveIndexInAdjList(-clause.secondVar));
        }

        int getEffectiveIndexInAdjList(int variable) {
            if (variable < 0)
                return Math.abs(variable) + numberOfVariables - 1;
            return variable - 1;
        }

        int getOppositeIndexInAdjList(int index) {
            if (index >= numberOfVariables)
                return index - numberOfVariables;
            return index + numberOfVariables;
        }

        int getVariableIdFromIndex(int index) {
            return (index >= numberOfVariables) ? -((index - numberOfVariables) + 1) : (index + 1);
        }

        void printGraph() {
            System.out.println("\n== implication graph ==");
            for (int i = 0; i < adjList.size(); i++) {
                System.out.println("== variable " + getVariableIdFromIndex(i) + " ==");
                for (int j : adjList.get(i))
                    System.out.println(String.format("%s(%s) => %s(%s)", getVariableIdFromIndex(i), i,
                            getVariableIdFromIndex(j), j));
            }

            System.out.println("\n== reverse implication graph ==");
            for (int i = 0; i < adjListR.size(); i++) {
                System.out.println("== variable " + getVariableIdFromIndex(i) + " ==");
                for (int j : adjListR.get(i))
                    System.out.println(String.format("%s(%s) => %s(%s)", getVariableIdFromIndex(i), i,
                            getVariableIdFromIndex(j), j));
            }
        }

        void printSccs() {
            System.out.println("\n == Strongly connected components ==");
            for (int i = 0; i < scc.length; i++) {
                System.out.print(scc[i] + " ");
            }
            System.out.println();
        }

        private void printOrder() {
            System.out.println("\n == order ==");
            for (int i = 0; i < order.size(); i++)
                System.out.print(getVariableIdFromIndex(order.get(i)) + "(" + order.get(i) + ") ");
            System.out.println();
        }
    }

    static class TwoSatClause {
        int firstVar;
        int secondVar;
    }

}
