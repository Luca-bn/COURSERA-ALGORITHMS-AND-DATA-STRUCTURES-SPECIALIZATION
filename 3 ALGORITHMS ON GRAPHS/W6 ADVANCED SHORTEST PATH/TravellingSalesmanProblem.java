import java.util.*;
import java.io.*;

/*
    Task. Compute the length of the shortest path starting in the depot, visiting each store at least once and
    returning to the depot.

    Input Format. You will be given the input for this problem in two parts. The first part contains the
    description of a road network, the second part contains the queries. You have a separate time limit for
    preprocessing the graph. Under this time limit, you need to read the graph and preprocess it. After
    you’ve preprocessed the graph, you need to output the string “Ready” (without quotes) and flush the
    output buffer (the starter files for C++, Java and Python3 do that for you; if you use another language,
    you will have to find out how to do this). Only after you output the string “Ready” you will be given
    the queries. You will have a time limit for the querying part, and under this time limit you will need
    to input all the queries and output the results for each of the quires.
    The first line of the road network description contains two integers 𝑛 and 𝑚 — the number of nodes
    and edges in the network, respectively. The nodes are numbered from 1 to 𝑛. Each of the following
    𝑚 lines contains three integers 𝑢, 𝑣 and 𝑙 describing a directed edge (𝑢, 𝑣) of length 𝑙 from the node
    number 𝑢 to the node number 𝑣.
    The first line of the queries description contains an integer 𝑞 — the number of queries for computing
    the distance. Each of the following 𝑞 lines starts with the integer 𝑘 — the number of points the truck
    must visit, including all the stores and the depot. There are 𝑘 more integers on the same line. The first
    of them is the number of the node corresponding to the depot location. The next 𝑘−1 integers are the
    numbers of the nodes corresponding to the store locations.
    
    Constraints. 1 ≤ 𝑛 ≤ 110 000; 1 ≤ 𝑚 ≤ 250 000; 1 ≤ 𝑢, 𝑣 ≤ 𝑛; 1 ≤ 𝑙 ≤ 100 000; 1 ≤ 𝑞 ≤ 100; 1 ≤ 𝑘 ≤ 20.
    It is guaranteed that all the answers are less than 1 000 000 000. For Python2, Python3, Ruby
    and Javascript, 1 ≤ 𝑛 ≤ 11 000, 1 ≤ 𝑚 ≤ 25 000, 1 ≤ 𝑘 ≤ 20.
    
    Output Format. After you’ve read the description of the road network and done your preprocessing,
    output one string “Ready” (without quotes) on a separate line and flush the output buffer. Then read
    the queries, and for each query, output one integer on a separate line. If there is no path starting in 
    depot, visiting each store at least once and returning to the depot, output −1. Otherwise, output the
    length of the shortest path starting at a depot, visiting each store at least once and returning to the
    depot.
*/

// Good job! (Max time used: 16.16/40.00, max preprocess time used: 6.51/50.00, max memory used: 0/8589934592.)
public class TravellingSalesmanProblem {

    public static final long INF = Long.MAX_VALUE / 4;

    public static void main(String args[]) throws IOException {
        FastScanner in = new FastScanner(System.in);
        int n = in.nextInt(); // number of vertices in the graph.
        int m = in.nextInt(); // number of edges in the graph.

        Node[] graph = new Node[n];

        // initialize the graph.
        for (int i = 0; i < n; i++) {
            graph[i] = new Node(i);
        }

        // get edges
        for (int i = 0; i < m; i++) {
            int x, y;
            long c;
            x = in.nextInt() - 1;
            y = in.nextInt() - 1;
            c = in.nextInt();

            graph[x].addOutEdge(new Edge(y, c));
            graph[y].addInEdge(new Edge(x, c));
        }

        // preprocessing stage.
        new PreProcess().processing(graph);
        System.out.println("Ready");

        int queryId = 0;
        int q = in.nextInt();
        // acutal distance computation stage.
        BidirectionalDijkstra bd = new BidirectionalDijkstra();
        for(int i = 0; i < q; i++) {
            int k = in.nextInt();
            int[] tourStops = new int[k];
            for(int j = 0; j < k; j++)
                tourStops[j] = in.nextInt() - 1;

            Long[][] distances = new Long[k][k];
            for(int x = 0; x < k; x++) {
                for(int y = 0; y < k; y++)
                    distances[x][y] = (x == y) ? 0 : bd.computeDist(graph, tourStops[x], tourStops[y], queryId++);
            }
            TravellingSalesmanProblemSolver solver = new TravellingSalesmanProblemSolver(distances);
            solver.solve();
            System.out.println(solver.getTourCost());
        }
    }

    // all functions dealing with preprocessing in this class.
    static class PreProcess {

        int queryId = 0;
        // queue for importance parameter.
        PriorityQueue<Node> PQImp = new PriorityQueue<>((n1, n2) -> Long.compare(n1.importance, n2.importance)); 
        // queue for distance parameter.
        PriorityQueue<Node> queue; 

        // main function of this class.
        public void processing(Node[] graph) {
            for (int i = 0; i < graph.length; i++) {
                graph[i].updateImportance();
                PQImp.add(graph[i]);
            }
            preProcess(graph);
        }

        // function that will pre-process the graph.
        private void preProcess(Node[] graph) {
            int extractNum = 0; // stores the number of vertices that are contracted.

            while (PQImp.size() != 0) {
                Node vertex = PQImp.poll();
                vertex.updateImportance();

                // if the vertex's recomputed importance is still minimum then contract it.
                if (PQImp.size() != 0 && vertex.importance > PQImp.peek().importance) {
                    PQImp.add(vertex);
                    vertex.preprocessInsertions++;
                    continue;
                }

                vertex.orderPos = extractNum++;

                // contraction part.
                contractNode(graph, vertex, extractNum - 1);
            }
        }

        // function to contract the node.
        private void contractNode(Node[] graph, Node vertex, int contractId) {
            vertex.contracted = true;

            long inMax = 0; // stores the max distance out of uncontracted inVertices of the given vertex.
            long outMax = 0; // stores the max distance out of uncontracted outVertices of the given vertex.

            for(int i = 0; i < Math.max(vertex.inEdges.size(), vertex.outEdges.size()); i++) {
                if(i < vertex.inEdges.size()) {
                    Edge edge = vertex.inEdges.get(i);
                    if (!graph[edge.linkedNode].contracted)
                        inMax = Math.max(inMax, edge.cost);
                }
                if(i < vertex.outEdges.size()) {
                    Edge edge = vertex.outEdges.get(i);
                    if (!graph[edge.linkedNode].contracted)
                        outMax = Math.max(outMax, edge.cost);
                }
            }
            long max = inMax + outMax; // total max distance.

            for (Edge inEdge : vertex.inEdges) {
                int inVertex = inEdge.linkedNode;
                
                if (graph[inVertex].contracted)
                    continue;
                graph[inVertex].delNeighbors++;

                // finds the shortest distances from the inVertex to all the outVertices.
                dijkstra(graph, inVertex, max, contractId); 

                // this code adds shortcuts.
                for (Edge outEdge : vertex.outEdges) {
                    Node neighbor = graph[outEdge.linkedNode];
                    if (neighbor.contracted)
                        continue;
                    neighbor.delNeighbors++;
                    if (neighbor.contractId != contractId || neighbor.sourceId != queryId
                            || neighbor.queryDist > inEdge.cost + outEdge.cost) {
                        graph[inVertex].outEdges.add(new Edge(outEdge.linkedNode, inEdge.cost + outEdge.cost));
                        neighbor.inEdges.add(new Edge(inVertex, inEdge.cost + outEdge.cost));
                    }
                }
                queryId++;
            }
        }

        // dijkstra function implemented.
        private void dijkstra(Node[] graph, int source, long maxcost, int contractId) {
            queue = new PriorityQueue<>((n1, n2) -> Long.compare(n1.queryDist, n2.queryDist));

            Node s = graph[source];
            s.queryDist = 0;
            s.contractId = contractId;
            s.sourceId = queryId;
            s.inForwQueue = true;
            queue.add(s);

            while (queue.size() != 0) {
                Node vertex = queue.poll();
                vertex.inForwQueue = false;
                if (vertex.queryDist > maxcost)
                    return;
                relaxEdges(graph, vertex, contractId, queue, queryId);
            }
        }

        // function to relax outgoing edges.
        private void relaxEdges(Node[] graph, Node vertex, int contractId, PriorityQueue<Node> queue, int sourceId) {

            for (Edge edge : vertex.outEdges) {
                Node neighbor = graph[edge.linkedNode];
                if (neighbor.contracted) {
                    continue;
                }
                if (!isUpdated(vertex, neighbor))
                    neighbor.clean();
                
                if (neighbor.queryDist > vertex.queryDist + edge.cost) {
                    neighbor.queryDist = vertex.queryDist + edge.cost;
                    neighbor.contractId = contractId;
                    neighbor.sourceId = sourceId;

                    if (neighbor.inForwQueue)
                        queue.remove(neighbor);
                    queue.add(neighbor);
                    neighbor.inForwQueue = true;
                }
            }
        }

        // compare the ids whether id of source to target is same if not then consider
        // the target vertex distance=infinity.
        private boolean isUpdated(Node source, Node target) {
            return source.contractId == target.contractId && source.sourceId == target.sourceId;
        }
    }

    // class for bidirectional dijstra search.
    static class BidirectionalDijkstra {
        PriorityQueue<Node> forwQ;
        PriorityQueue<Node> revQ;

        // main function that will compute distances.
        public long computeDist(Node[] graph, int source, int target, int queryID) {
            Node s = graph[source];
            s.cleanForw();
            s.queryDist = 0;
            s.forwqueryId = queryID;

            Node t = graph[target];
            t.cleanBackw();
            t.revDistance = 0;
            t.revqueryId = queryID;

            forwQ = new PriorityQueue<>((n1, n2) -> Long.compare(n1.queryDist, n2.queryDist));
            revQ = new PriorityQueue<>((n1, n2) -> Long.compare(n1.revDistance, n2.revDistance));
            forwQ.add(s);
            revQ.add(t);

            long estimate = INF;

            while (forwQ.size() != 0 || revQ.size() != 0) {
                if (forwQ.size() != 0) {
                    Node vertex1 = forwQ.poll();
                    if (vertex1.queryDist <= estimate) {
                        relaxEdges(graph, vertex1.id, queryID, true);
                    }
                    if (vertex1.revqueryId == queryID && vertex1.revProcessed) {
                        if (vertex1.queryDist + vertex1.revDistance < estimate) {
                            estimate = vertex1.queryDist + vertex1.revDistance;
                        }
                    }
                }

                if (revQ.size() != 0) {
                    Node vertex2 = revQ.poll();
                    if (vertex2.revDistance <= estimate) {
                        relaxEdges(graph, vertex2.id, queryID, false);
                    }
                    if (vertex2.forwqueryId == queryID && vertex2.forwProcessed) {
                        if (vertex2.revDistance + vertex2.queryDist < estimate) {
                            estimate = vertex2.queryDist + vertex2.revDistance;
                        }
                    }
                }
            }

            return estimate;
        }

        // function to relax edges.(according to the direction forward or backward)
        private void relaxEdges(Node[] graph, int vertex, int queryId, boolean forword) {
            if (forword) {
                Node current = graph[vertex];
                List<Edge> outEdges = current.outEdges;
                current.forwProcessed = true;
                current.forwqueryId = queryId;
                current.inForwQueue = false;

                for (Edge edge : outEdges) {
                    long cost = edge.cost;
                    Node neighbor = graph[edge.linkedNode];
                    if (current.orderPos < neighbor.orderPos) {
                        if (current.forwqueryId != neighbor.forwqueryId)
                            neighbor.cleanForw();
                        if (neighbor.queryDist > current.queryDist + cost) {
                            neighbor.forwqueryId = current.forwqueryId;
                            neighbor.queryDist = current.queryDist + cost;

                            if (neighbor.inForwQueue)
                                forwQ.remove(neighbor);
                            forwQ.add(neighbor);
                            neighbor.inForwQueue = true;
                        }
                    }
                }
            } else {
                Node current = graph[vertex];
                List<Edge> inEdges = current.inEdges;
                current.revProcessed = true;
                current.revqueryId = queryId;
                current.inRevQueue = false;

                for (Edge edge : inEdges) {
                    Node neighbor = graph[edge.linkedNode];
                    long cost = edge.cost;

                    if (current.orderPos < neighbor.orderPos) {
                        if (current.revqueryId != neighbor.revqueryId)
                            neighbor.cleanBackw();
                        if (neighbor.revDistance > current.revDistance + cost) {
                            neighbor.revqueryId = current.revqueryId;
                            neighbor.revDistance = current.revDistance + cost;

                            if (neighbor.inRevQueue)
                                revQ.remove(neighbor);
                            revQ.add(neighbor);
                            neighbor.inRevQueue = true;
                        }
                    }
                }
            }
        }
    }

    // class for Vertex of a graph.
    static class Node {
        
        /* -- node properties -- */
        // id of the vertex.
        int id; 
        // incoming edges
        List<Edge> inEdges; 
        // outcoming edges
        List<Edge> outEdges; 

        /* -- preprocessing properties -- */
        // position of vertex in nodeOrderingQueue.
        int orderPos; 
        // to check if vertex is contracted
        boolean contracted; 
        // egdediff = sE - inE - outE. (sE=inE*outE , i.e number of shortcuts that we may have to add.)
        int edgeDiff; 
        // number of contracted neighbors.
        int delNeighbors; 
        // number of shortcuts to be introduced if this vertex is contracted.
        int shortcutCover; 
        // id for the vertex that is going to be contracted.
        int contractId; 
        // it contains the id of vertex for which we will apply dijkstra while contracting.
        int sourceId; 
        // the number of time that current node has been inserted in importance queue
        int preprocessInsertions;
        // total importance = edgediff + shortcutcover + delneighbors.
        long importance;

        /* -- dijkstra properties -- */
        // id for forward search.
        int forwqueryId;
        // id for backward search. 
        int revqueryId; 
        // for forward distance.
        long queryDist; 
        // for backward distance.
        long revDistance; 
        // is processed in forward search.
        boolean forwProcessed;
        // is processed in backward search. 
        boolean revProcessed; 
        // to updating priority in queue without calling remove first
        boolean inForwQueue;
        boolean inRevQueue;

        public void clean() {
            cleanForw();
            cleanBackw();
        }

        private void cleanForw() {
            queryDist = INF;
            forwProcessed = false;
            inForwQueue = false;
        }

        private void cleanBackw() {
            revDistance = INF;
            revProcessed = false;
            inRevQueue = false;
        }

        public Node(int vertexNum) {
            this.id = vertexNum;
            this.inEdges = new ArrayList<>();
            this.outEdges = new ArrayList<>();
            this.forwqueryId = -1;
            this.revqueryId = -1;
            clean();
        }

        public void addInEdge(Edge e) {
            this.inEdges.add(e);
        }

        public void addOutEdge(Edge e) {
            this.outEdges.add(e);
        }

        public void updateImportance() {
            importance = (((inEdges.size() * outEdges.size()) - inEdges.size() - outEdges.size()) * 14) 
                + ((inEdges.size() + outEdges.size()) * 25) 
                + (delNeighbors * 10);
        }
    }

    static class Edge {
        Integer linkedNode;
        Long cost;

        public Edge(Integer node, Long cost) {
            this.linkedNode = node;
            this.cost = cost;
        }
    }

    static class TravellingSalesmanProblemSolver {
        private final int N, start;
        private final Long[][] distance;
        private Long minTourCost = INF;
        private boolean ranSolver = false;

        TravellingSalesmanProblemSolver(Long[][] distance) {
            this.distance = distance;
            this.N = distance.length;
            this.start = 0;
        }

        // Returns the minimal tour cost.
        public Long getTourCost() {
            if (!ranSolver)
                solve();
            return minTourCost == INF ? -1 : minTourCost;
        }

        // Solves the traveling salesman problem and caches solution.
        public void solve() {

            if (ranSolver)
                return;

            final int END_STATE = (1 << N) - 1;
            Long[][] memo = new Long[N][1 << N];

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
                        Long minDist = INF;
                        for (int end = 0; end < N; end++) {
                            if (end == start || end == next || notIn(end, subset))
                                continue;
                            Long newDistance = memo[end][subsetWithoutNext] + distance[end][next];
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
                long tourCost = memo[i][END_STATE] + distance[i][start];
                if (tourCost < minTourCost) {
                    minTourCost = tourCost;
                }
            }

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

    static class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        FastScanner(InputStream stream) {
            try {
                br = new BufferedReader(new InputStreamReader(stream));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        void close() throws IOException {
            this.br.close();
        }
    }
}
