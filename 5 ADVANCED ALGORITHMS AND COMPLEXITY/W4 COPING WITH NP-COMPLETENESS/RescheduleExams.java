import java.util.*;
import java.io.*;

/*
    Task. The new secretary at your Computer Science Department has prepared a schedule of exams for CS-
    101: each student was assigned to his own exam date. However, it’s a disaster: not only some pairs
    of students known to be close friends may have been assigned the same date, but also NONE of the
    students can actually come to the exam at the day they were assigned (there was a misunderstanding
    between the secretary who asked to specify available dates and the students who understood they
    needed to select the date at which they cannot come). There are three different dates the professors
    are available for these exams, and these dates cannot be changed. The only thing that can be changed
    is the assignment of students to the dates of exams. You know for sure that each student can’t come at
    the currently scheduled date, but also each student definitely can come at any of the two other possible
    dates. Also, you must make sure that no two known close friends are assigned to the same exam date.
    You need to determine whether it is possible or not, and if yes, suggest a specific assignment of the
    students to the dates.
    This problem can be reduced to a graph problem called 3-recoloring. You are given a graph, and each
    vertex is colored in one of the 3 possible colors. You need to assign another color to each vertex in
    such a way that no two vertices connected by and edge are assigned the same color. Here, possible
    colors correspond to the possible exam dates, vertices correspond to students, colors of the vertices
    correspond to the assignment of students to the exam dates, and edges correspond to the pairs of close
    friends.
    
    Input Format. The first line contains two integers 𝑛 and 𝑚 — the number of vertices and the number of
    edges of the graph. The vertices are numbered from 1 through 𝑛. The next line contains a string of
    length 𝑛 consisting only of letters 𝑅, 𝐺 and 𝐵 representing the current color assignments. For each
    position 𝑖 (1-based) in the string, if it is 𝑅, then the vertex 𝑖 is colored red; if it’s 𝐺, the vertex 𝑖 is
    colored green; if it’s 𝐵, the vertex 𝑖 is colored blue. These are the current color assignments, and each
    of them must be changed. Each of the next 𝑚 lines contains two integers 𝑢 and 𝑣 — vertices 𝑢 and
    𝑣 are connected by an edge (it is possible that 𝑢 = 𝑣).
    
    Constraints. 1 ≤ 𝑛 ≤ 1 000; 0 ≤ 𝑚 ≤ 20 000; 1 ≤ 𝑢, 𝑣 ≤ 𝑛.
    
    Output Format. If it is impossible to reassign the students to the dates of exams in such a way that no two
    friends are going to pass the exam the same day, and each student’s assigned date has changed, output
    just one word “Impossible” (without quotes). Otherwise, output one string consisting of 𝑛 characters
    𝑅, 𝐺 and 𝐵 representing the new coloring of the vertices. Note that the color of each vertex must be
    different from the initial color of this vertex. The vertices connected by an edge must have different
    colors.
*/

// Good job! (Max time used: 0.70/3.00, max memory used: 99753984/2147483648.)
public class RescheduleExams {

    class Edge {
        int u, v;

        public Edge(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }

    void assignNewColors(int n, Edge[] edges, char[] colors) {

        /*
         * CONSTRAINTS:
         * 1) each node can not be the same color of its initial state.
         * 
         * 2) each node should be exactly one of another two colors differ from its
         * original.
         * 
         * 3) each node can not be of the same color of its adjacent node.
         */

        // each variable rapresent a specific color for that node
        // x1 = 1 Red, x2 = 1 Blue, x3 = 1 Green, x4 = 2 Red...
        int nColors = 3;
        int variables = n * nColors;
        List<TwoSatClause> clauses = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            // R = (i * nColors) - 2; G = (i * nColors) - 1; B = (i * nColors)
            if (colors[i - 1] == 'R') {
                // excluding red
                // (x = B || x = G) && (x != B || x != G)
                clauses.add(new TwoSatClause(blue(i, nColors), green(i, nColors)));
                clauses.add(new TwoSatClause(-blue(i, nColors), -green(i, nColors)));
                // (x != R || x = G) && (x != R || x = B)
                clauses.add(new TwoSatClause(-red(i, nColors), green(i, nColors)));
                clauses.add(new TwoSatClause(-red(i, nColors), blue(i, nColors)));
            }
            else if (colors[i - 1] == 'G') {
                // excluding green
                // (x = B || x = R) && (x != B || x != R)
                clauses.add(new TwoSatClause(blue(i, nColors), red(i, nColors)));
                clauses.add(new TwoSatClause(-blue(i, nColors), -(red(i, nColors))));
                // (x != G || x = R) && (x != G || x = B)
                clauses.add(new TwoSatClause(-green(i, nColors), red(i, nColors)));
                clauses.add(new TwoSatClause(-green(i, nColors), blue(i, nColors)));
            }
            else {
                // excluding blue
                // (x = R || x = G) && (x != R || x != G)
                clauses.add(new TwoSatClause(red(i, nColors), green(i, nColors)));
                clauses.add(new TwoSatClause(-red(i, nColors), -green(i, nColors)));
                // (x != B || x = G) && (x != B || x = R)
                clauses.add(new TwoSatClause(-blue(i, nColors), green(i, nColors)));
                clauses.add(new TwoSatClause(-blue(i, nColors), red(i, nColors)));
            }
        }

        for(Edge edge : edges) {
            // for each edge, the 2 nodes cannot have same color
            // (x!=R or y!=R) & (x!=B or y!=B) & (x!=G or y!= G)
            clauses.add(new TwoSatClause(-red(edge.u, nColors), -red(edge.v, nColors)));
            clauses.add(new TwoSatClause(-blue(edge.u, nColors), -blue(edge.v, nColors)));
            clauses.add(new TwoSatClause(-green(edge.u, nColors), -green(edge.v, nColors)));
        }

        TwoSATSolver solver = new TwoSATSolver(variables);
        if(!solver.solve(clauses)) {
            System.out.println("Impossible");
            return;
        }
        char[] col = new char[]{'R', 'G', 'B'};
        for(int i = 0; i < solver.assignment.length; i++) {
            if(solver.assignment[i])
                System.out.print(col[i % 3]);
        }
        System.out.print("\n");
    }

    static int red(int i, int nColors) {
        return (i * nColors) - 2;
    }

    static int green(int i, int nColors) {
        return (i * nColors) - 1;
    }

    static int blue(int i, int nColors) {
        return  (i * nColors);
    }

    static class TwoSATSolver {

        boolean debug = false;
        final int UNVISITED = -1;
        boolean[] used;
        int[] scc;
        List<Integer> order;
        int numberOfVariables;
        List<List<Integer>> adjList;
        List<List<Integer>> adjListR;
        boolean[] assignment;

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

        boolean solve(List<TwoSatClause> clauses) {
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

            assignment = new boolean[numberOfVariables];
            for (int i = 0; i < numberOfVariables; i++) {
                // if variable with its negation belong to same scc, the 2-SAT is UNSATISFIABLE
                if (scc[i] == scc[i + numberOfVariables]) {
                    return false;
                }
                // if scc of variable is > then its negation, taking 1 as its value, 0 otherwise
                assignment[i] = scc[i] > scc[getOppositeIndexInAdjList(i)];
            }
            return true;
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

        TwoSatClause(int a, int b) {
            firstVar = a;
            secondVar = b;
        }
    }

    void run() {
        Scanner scanner = new Scanner(System.in);
        PrintWriter writer = new PrintWriter(System.out);

        int n = scanner.nextInt();
        int m = scanner.nextInt();
        scanner.nextLine();

        String colorsLine = scanner.nextLine();
        char[] colors = colorsLine.toCharArray();

        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            edges[i] = new Edge(u, v);
        }

        assignNewColors(n, edges, colors);
        writer.close();
    }

    public static void main(String[] args) throws FileNotFoundException {
        new RescheduleExams().run();
    }
}
