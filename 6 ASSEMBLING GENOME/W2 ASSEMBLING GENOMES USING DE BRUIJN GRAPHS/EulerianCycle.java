import java.util.*;
import java.io.*;

/*
    Task. Given a directed graph, find an Eulerian cycle in the graph or report that none exists.
    
    Input Format. The first line contains integers n and m — the number of vertices and the number of edges,
    respectively. Each of the following m lines specifies an edge in the format “u v”. (As usual, we assume
    that the vertices of the graph are f1; 2; . . . ; ng.) The graph may contain self-loops (that is, edges of
    the form (v; v)) and parallel edges (that is, several copies of the same edge). It is guaranteed that the
    graph is strongly connected.

    Constraints. 1 <= n <= 10^4; n <= m <= 10^5; 1 <= u; v <= n.
    
    Output Format. If the graph has no Eulerian cycle, output 0. Otherwise output 1 in the first line and a
    sequence v1; v2; . . . ; vm of vertices in the second line. This sequence should traverse an Eulerian cycle
    in the graph: (v1; v2); (v2; v3); . . . ; (vm-1; vm); (vm; v1) should all be edges of the graph and each edge of
    the graph should appear in this sequence exactly once. As usual, the graph may contain many Eulerian
    cycles (in particular, each Eulerian cycle may be traversed starting from any of its vertices). You may
    output any one of them.
*/

// Good job! (Max time used: 0.23/1.50, max memory used: 56270848/2147483648.)
public class EulerianCycle {

    public static void main(String[] args) throws IOException {
        FastScanner in = new FastScanner();
        int n = in.nextInt(), m = in.nextInt();
        EulerianCycleSolver solver = new EulerianCycleSolver(n, m);
        for (int i = 0; i < m; i++)
            solver.addEdge(in.nextInt() - 1, in.nextInt() - 1);
        LinkedList<Vertex> eulerianCycle = solver.findEulerianCycle();
        printSolution(eulerianCycle);
        in.close();
    }

    private static void printSolution(LinkedList<Vertex> eulerianCycle) {
        // if there is no path, or there exists a path which visits each edge exactly once, but it's not a cycle => 0
        if (eulerianCycle == null || eulerianCycle.isEmpty() || eulerianCycle.peekFirst().id != eulerianCycle.peekLast().id)
            System.out.println("0");
        else {
            // otherwise, printing path but without repeating starting node at the end
            System.out.println("1");
            if(eulerianCycle.peekFirst().id == eulerianCycle.peekLast().id)
                eulerianCycle.removeLast();
            for (Vertex v : eulerianCycle) {
                System.out.print((v.id + 1) + " ");
            }
        }
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
            to.inEdgesCount++;
        }

        
        public LinkedList<Vertex> findEulerianCycle() {
            LinkedList<Vertex> cycle = new LinkedList<>();

            // find random vertex and ensure that each vertex has same number of incoming and outcoming edges
            Vertex starting = findStartingVertex();
            if(starting == null)
                return cycle;

            dfs(starting, cycle);
            
            return cycle;
        }

        private void dfs(Vertex v, LinkedList<Vertex> cycle) {
            while(v.outEdgesCount > 0) {
                Vertex next = v.outEdges.get(--v.outEdgesCount);
                dfs(next, cycle);
            }
            cycle.addFirst(v);
        }

        private Vertex findStartingVertex() {
            Vertex startingPoint = null;
            for(Vertex v : graph) {
                if(v.outEdgesCount != v.inEdgesCount)
                    return null;
                if(v.outEdgesCount > 0)
                    startingPoint = v;
            }
            return startingPoint;
        }

    }

    static class Vertex {
        final int id;
        int outEdgesCount = 0;
        int inEdgesCount = 0;
        List<Vertex> outEdges = new ArrayList<>();

        public Vertex(int id) {
            this.id = id;
        }

    }

    static class FastScanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        FastScanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }

        void close() throws IOException {
            this.in.close();
        }
    }

}
