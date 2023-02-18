import java.io.*;
import java.util.*;

/*
    Task. Given a network with lower bounds and capacities on edges, find a circulation if it exists.
    
    Input Format. The first line contains integers 𝑛 and 𝑚 — the number of vertices and the number of edges,
    respectively. Each of the following 𝑚 lines specifies an edge in the format “u v l c”: the edge (𝑢, 𝑣)
    has a lower bound 𝑙 and a capacity 𝑐. (As usual, we assume that the vertices of the network are
    {1, 2, . . . , 𝑛}.) The network does not contain self-loops, but may contain parallel edges.
    
    Constraints. 2 ≤ 𝑛 ≤ 40; 1 ≤ 𝑚 ≤ 1 600; 𝑢 ̸= 𝑣; 0 ≤ 𝑙 ≤ 𝑐 ≤ 50.
    
    Output Format. If there exists a circulation, output YES in the first line. In each of the next 𝑚 lines output
    the value of the flow along an edge (assuming the same order of edges as in the input). If there is no
    circulation, output NO.
*/

/*
 * Used starter file from Course 5-week1 (Evacuation problem) and added just some logic to handle lower bound.
 * Basically same implementation of other problem with following differences:
 * - capacity of each edge becomes capacity - lowerBound;
 * - after creation of each node and edge, creating 2 new nodes s and t;
 * - for each node v, create new edge from s to v having capacity = sum of all lowerbounds of incoming nodes to v;
 * - for each node v, create new edge from v to t having capacity = sum of all lowerbounds of outcoming nodes from v;
 * - performing maxFlow algorithm from s to t (same logic of previous problem);
 *      if maxFlow = sum of all lowerbounds => there is solution, otherwise => no solution.
 * - for output step, for each edge taken in input (store in same way the order of input edges), print its flow + its lowerbound.
*/

// Good job! (Max time used: 0.26/4.50, max memory used: 35061760/4294967296.)
@SuppressWarnings("unchecked")
public class FindCirculationInNetwork {

    private static FastScanner in;
    private static int vertex_count;
    private static int edge_count;
    private static int[] outs, ins;
    private static int lowerBoundSum = 0;
    private static List<Integer> edgesIds = new ArrayList<>();
    private static List<Integer> edgesIdsR = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        in = new FastScanner();
        FlowGraph graph = readGraph();
        int maxFlow = maxFlow(graph, graph.size() - 2, graph.size() - 1);
        if(maxFlow != lowerBoundSum)
            System.out.println("NO");
        else {
            System.out.println("YES");
            for(int i = 0; i < edge_count; i++) {
                Edge edge = graph.edges.get(edgesIds.get(i));
                System.out.println(edge.flow + edge.lowerBound);
                // System.out.println("F: " + graph.edges.get(edgesIds.get(i)));
                // System.out.println("R: " + graph.edges.get(edgesIdsR.get(i)));
                // System.out.println();
            }
        }
    }

    private static int maxFlow(FlowGraph graph, int from, int to) {
        int flow = 0;
        // compute initial path
        List<Integer> path = getPath(graph, from, to);

        // while there is a path...
        while (path.size() > 0) {
            // calculate the min residual flow through the current path
            int minResidual = Integer.MAX_VALUE;

            for (Integer id_edge : path) {
                Edge e = graph.getEdge(id_edge);

                int residual = e.capacity - e.flow;
                if (minResidual > residual) {
                    minResidual = residual;
                }
            }

            // update the graph with the min residual flow
            for (Integer id_edge : path) {
                graph.addFlow(id_edge, minResidual);
            }

            // recalculate the path
            path = getPath(graph, from, to);
        }

        // calculate the maxflow
        for (Integer id_edge : graph.getIds(from)) {
            Edge e = graph.getEdge(id_edge);
            flow += e.flow;
        }

        return flow;
    }

    private static List<Integer> getPath(FlowGraph graph, int from, int to) {
        // initialize the result (a backward sequence of edges to traverse from "from"
        // to "to")
        List<Integer> result = new ArrayList<>();

        // initialize the array of visited nodes
        List<Integer> visited = new ArrayList<>();
        // initialize the map of parent nodes
        Map<Integer, Integer> parents = new HashMap<>();

        // initialize the node queue
        Queue<Integer> q = new LinkedList<>();
        q.add(from);

        boolean isPathFound = false;

        // begin traversing a graph
        while (!q.isEmpty()) {
            // get the first node in the queue
            Integer u = q.remove();

            // get the nodes adjacent to the first node in the queue
            List<Integer> u_adj = graph.getIds(u);

            // for every node v adjacent to u...
            for (Integer v : u_adj) {
                Edge v_edge = graph.getEdge(v);

                int residualFlow = v_edge.capacity - v_edge.flow;
                if ((residualFlow > 0) && !visited.contains(v_edge.to)) {
                    q.add(v_edge.to);
                    visited.add(v_edge.to);
                    parents.put(v_edge.to, v);

                    // we can break early if the path is found
                    if (v_edge.to == to) {
                        isPathFound = true;
                        break;
                    }
                }
            }

            // we can break early if the path is found
            if (isPathFound) {
                break;
            }
        }

        if (visited.contains(to)) {
            // build the path as a backward sequence of edges
            while (to != from) {
                Integer tmpStart = parents.get(to);
                result.add(tmpStart);
                Edge e = graph.getEdge(tmpStart);
                to = e.from;
            }
        }

        return result;
    }

    static FlowGraph readGraph() throws IOException {
        vertex_count = in.nextInt();
        edge_count = in.nextInt();
        FlowGraph graph = new FlowGraph(vertex_count + 2);
        ins = new int[vertex_count + 2];
        outs = new int[vertex_count + 2];

        for (int i = 0; i < edge_count; ++i) {
            int from = in.nextInt() - 1, to = in.nextInt() - 1, lowerBound = in.nextInt(), capacity = in.nextInt();
            graph.addEdge(from, to, lowerBound, capacity);
            outs[from] += lowerBound;
            ins[to] += lowerBound;
            lowerBoundSum += lowerBound;
        }
        int sIndex = vertex_count, tIndex = vertex_count + 1;
        for(int i = 0; i < vertex_count; i++) {
            graph.addEdge(sIndex, i, 0, ins[i]);
            graph.addEdge(i, tIndex, 0, outs[i]);
        }
        return graph;
    }

    /*
     * This class implements a bit unusual scheme to store the graph edges, in order
     * to retrieve the backward edge for a given edge quickly.
     */
    static class FlowGraph {
        /* List of all - forward and backward - edges */
        private List<Edge> edges;

        /* These adjacency lists store only indices of edges from the edges list */
        private List<Integer>[] graph;

        public FlowGraph(int n) {
            this.graph = (ArrayList<Integer>[]) new ArrayList[n];
            for (int i = 0; i < n; ++i)
                this.graph[i] = new ArrayList<>();
            this.edges = new ArrayList<>();
        }

        public void addEdge(int from, int to, int lowerBound, int capacity) {
            /*
             * Note that we first append a forward edge and then a backward edge,
             * so all forward edges are stored at even indices (starting from 0),
             * whereas backward edges are stored at odd indices.
             */
            Edge forwardEdge = new Edge(from, to, lowerBound, capacity);
            Edge backwardEdge = new Edge(to, from, lowerBound, 0);
            graph[from].add(edges.size());
            edges.add(forwardEdge);
            edgesIds.add(edges.size() - 1);
            graph[to].add(edges.size());
            edges.add(backwardEdge);
            edgesIdsR.add(edges.size() - 1);
        }

        public int size() {
            return graph.length;
        }

        public List<Integer> getIds(int from) {
            return graph[from];
        }

        public Edge getEdge(int id) {
            return edges.get(id);
        }

        public void addFlow(int id, int flow) {
            /*
             * To get a backward edge for a true forward edge (i.e id is even), we should
             * get id + 1
             * due to the described above scheme. On the other hand, when we have to get a
             * "backward"
             * edge for a backward edge (i.e. get a forward edge for backward - id is odd),
             * id - 1
             * should be taken.
             *
             * It turns out that id ^ 1 works for both cases. Think this through!
             */
            edges.get(id).flow += flow;
            edges.get(id ^ 1).flow -= flow;
        }
    }

    static class Edge {
        int from, to, maxCapacity, capacity, lowerBound, flow;

        public Edge(int from, int to, int lowerBound, int capacity) {
            this.from = from;
            this.to = to;
            this.maxCapacity = capacity;
            this.flow = 0;
            this.lowerBound = lowerBound;
            this.capacity = capacity - lowerBound;
        }

        @Override
        public String toString() {
            return String.format("[from: %s, to: %s, flow: %s,  maxCapacity: %s, capacity: %s, lowerBound: %s]", from + 1, to + 1, flow, maxCapacity, capacity, lowerBound);
        }
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
