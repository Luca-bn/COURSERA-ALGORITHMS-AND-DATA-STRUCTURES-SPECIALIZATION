import java.util.*;
import java.io.*;

/*
    Task. Compute the distance between several pairs of nodes in the network.

    Input Format. See the input format for the previous problem.

    Constraints. 1 ≤ 𝑛 ≤ 500 000; 1 ≤ 𝑚 ≤ 1 100 000; 1 ≤ 𝑢, 𝑣 ≤ 𝑛; 1 ≤ 𝑙 ≤ 200 000; 1 ≤ 𝑞 ≤ 10 000. It is
    guaranteed that the correct distances are less than 1 000 000 000. For Python2, Python3, Ruby
    and Javascript, 1 ≤ 𝑛 ≤ 11 000, 1 ≤ 𝑚 ≤ 25 000, 1 ≤ 𝑞 ≤ 1 000.
    
    Output Format. See the output format for the previous problem.
*/

/*
    There are some issues with this submission.. sometimes it works, some other times it doesn't..
    here 2 cases where it workded:

    current version of code:
    Good job! (Max time used: 18.92/20.00, max preprocess time used: 188.41/220.00, max memory used: 0/8589934592.)

    same versione but at line 139 this was the code: dijkstra(graph, inVertex, max, contractId); (dindn't multiply max by 1.7)
    Good job! (Max time used: 19.15/20.00, max preprocess time used: 59.56/220.00, max memory used: 0/8589934592.)
*/
public class DistPreprocessLarge {

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

        // acutal distance computation stage.
        BidirectionalDijkstra bd = new BidirectionalDijkstra();
        int t = in.nextInt();
        for (int i = 0; i < t; i++) {
            int u, v;
            u = in.nextInt() - 1;
            v = in.nextInt() - 1;
            System.out.println(bd.computeDist(graph, u, v, i));
        }
        in.close();
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
                dijkstra(graph, inVertex, (int) (max * 1.7), contractId); 

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

            if (estimate == INF) {
                return -1;
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
