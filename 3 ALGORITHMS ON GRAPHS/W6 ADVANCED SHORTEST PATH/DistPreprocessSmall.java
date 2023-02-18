import java.util.*;

/*
    Task. Compute the distance between several pairs of nodes in the network.
    
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
    the distance. Each of the following 𝑞 lines contains two integers 𝑢 and 𝑣 — the numbers of the two
    nodes to compute the distance from 𝑢 to 𝑣.
    
    Constraints. 1 ≤ 𝑛 ≤ 110 000; 1 ≤ 𝑚 ≤ 250 000; 1 ≤ 𝑢, 𝑣 ≤ 𝑛; 1 ≤ 𝑙 ≤ 200 000; 1 ≤ 𝑞 ≤ 10 000. It is
    guaranteed that the correct distances are less than 1 000 000 000. For Python2, Python3, Ruby
    and Javascript, 1 ≤ 𝑛 ≤ 11 000, 1 ≤ 𝑚 ≤ 25 000, 1 ≤ 𝑞 ≤ 1 000.
    
    Output Format. After you’ve read the description of the road network and done your preprocessing,
    output one string “Ready” (without quotes) on a separate line and flush the output buffer. Then read
    the queries, and for each query, output one integer on a separate line. If there is no path from 𝑢 to 𝑣,
    output −1. Otherwise, output the distance from 𝑢 to 𝑣.
*/

// Good job! (Max time used: 5.39/9.00, max preprocess time used: 22.23/45.00, max memory used: 0/8589934592.)
public class DistPreprocessSmall {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
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
            Long c;
            x = in.nextInt() - 1;
            y = in.nextInt() - 1;
            c = in.nextLong();

            graph[x].outEdges.add(new Edge(y, c));
            graph[y].inEdges.add(new Edge(x, c));
        }

        // preprocessing stage.
        PreProcess process = new PreProcess();
        process.processing(graph);
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
        PriorityQueue<Node> PQImp; // queue for importance parameter.
        PriorityQueue<Node> queue; // queue for distance parameter.

        // main function of this class.
        public void processing(Node[] graph) {
            PQImp = new PriorityQueue<>(graph.length, (n1, n2) -> Long.compare(n1.importance, n2.importance));
            for (int i = 0; i < graph.length; i++) {
               computeImportance(graph[i]);
                PQImp.add(graph[i]);
            }
            preProcess(graph);
        }

        // compute importance for individual vertex while processing.
        private void computeImportance(Node vertex) {
            vertex.edgeDiff = (vertex.inEdges.size() * vertex.outEdges.size()) - vertex.inEdges.size()
                    - vertex.outEdges.size();
            vertex.shortcutCover = vertex.inEdges.size() + vertex.outEdges.size();
            vertex.importance = vertex.edgeDiff * 14 + vertex.shortcutCover * 25 + vertex.delNeighbors * 10;
        }

        // function that will pre-process the graph.
        private void preProcess(Node[] graph) {
            int extractNum = 0; // stores the number of vertices that are contracted.

            while (PQImp.size() != 0) {
                Node vertex = (Node) PQImp.poll();
                computeImportance(vertex); // recompute importance before contracting the vertex.

                // if the vertex's recomputed importance is still minimum then contract it.
                if (PQImp.size() != 0 && vertex.importance > PQImp.peek().importance) {
                    PQImp.add(vertex);
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

            for (Edge edge : vertex.inEdges) {
                graph[edge.linkedNode].delNeighbors++;
                if (!graph[edge.linkedNode].contracted)
                    inMax = Math.max(inMax, edge.cost);
            }

            for (Edge edge : vertex.outEdges) {
                graph[edge.linkedNode].delNeighbors++;
                if (!graph[edge.linkedNode].contracted)
                    outMax = Math.max(outMax, edge.cost);
            }

            long max = inMax + outMax; // total max distance.

            for (Edge inEdge : vertex.inEdges) {
                int inVertex = inEdge.linkedNode;
                if (graph[inVertex].contracted) {
                    continue;
                }
                dijkstra(graph, inVertex, max, contractId); // finds the shortest distances from the inVertex to all
                                                            // the outVertices.
                // this code adds shortcuts.
                for (Edge outEdge : vertex.outEdges) {
                    Node neighbor = graph[outEdge.linkedNode];
                    if (neighbor.contracted)
                        continue;
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
            queue = new PriorityQueue<Node>(graph.length,
                    (n1, n2) -> Long.compare(n1.queryDist, n2.queryDist));

            graph[source].queryDist = 0;
            graph[source].contractId = contractId;
            graph[source].sourceId = queryId;
            graph[source].inForwQueue = true;
            queue.add(graph[source]);

            while (queue.size() != 0) {
                Node vertex = queue.poll();
                vertex.inForwQueue = false;
                if (vertex.queryDist > maxcost)
                    return;
                relaxEdges(graph, vertex.id, contractId, queue, queryId);
            }
        }

        // function to relax outgoing edges.
        private void relaxEdges(Node[] graph, int vertex, int contractId, PriorityQueue<Node> queue, int sourceId) {

            for (Edge edge : graph[vertex].outEdges) {
                Node neighbor = graph[edge.linkedNode];
                if (neighbor.contracted) {
                    continue;
                }
                if(!isUpdated(graph, vertex, neighbor.id)) {
                    neighbor.clean();
                }
                if (neighbor.queryDist > graph[vertex].queryDist + edge.cost) {
                    neighbor.queryDist = graph[vertex].queryDist + edge.cost;
                    neighbor.contractId = contractId;
                    neighbor.sourceId = sourceId;

                    if(neighbor.inForwQueue)
                        queue.remove(neighbor);
                    queue.add(neighbor);
                    neighbor.inForwQueue = true;
                }
            }
        }

        // compare the ids whether id of source to target is same if not then consider
        // the target vertex distance=infinity.
        private boolean isUpdated(Node[] graph, int source, int target) {
            if (graph[source].contractId != graph[target].contractId
                    || graph[source].sourceId != graph[target].sourceId) {
                return false;
            }
            return true;
        }
    }

    // class for bidirectional dijstra search.
    static class BidirectionalDijkstra {
        PriorityQueue<Node> forwQ;
        PriorityQueue<Node> revQ;

        // main function that will compute distances.
        public long computeDist(Node[] graph, int source, int target, int queryID) {
            graph[source].cleanForw();
            graph[source].queryDist = 0;
            graph[source].forwqueryId = queryID;

            graph[target].cleanBackw();
            graph[target].revDistance = 0;
            graph[target].revqueryId = queryID;

            forwQ = new PriorityQueue<Node>(graph.length,
                    (n1, n2) -> Long.compare(n1.queryDist, n2.queryDist));
            revQ = new PriorityQueue<Node>(graph.length,
                    (n1, n2) -> Long.compare(n1.revDistance, n2.revDistance));
            forwQ.add(graph[source]);
            revQ.add(graph[target]);

            long estimate = Long.MAX_VALUE;

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

            if (estimate == Long.MAX_VALUE) {
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
                        if(current.forwqueryId != neighbor.forwqueryId)
                            neighbor.cleanForw();
                        if (neighbor.queryDist > current.queryDist + cost) {
                            neighbor.forwqueryId = current.forwqueryId;
                            neighbor.queryDist = current.queryDist + cost;

                            if(neighbor.inForwQueue)
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
                        if(current.revqueryId != neighbor.revqueryId)
                            neighbor.cleanBackw();
                        if (neighbor.revDistance > current.revDistance + cost) {
                            neighbor.revqueryId = current.revqueryId;
                            neighbor.revDistance = current.revDistance + cost;

                            if(neighbor.inRevQueue)
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
        // node properties
        int id; // id of the vertex.
        List<Edge> inEdges;
        List<Edge> outEdges;
        
        // preprocessing properties
        int orderPos; // position of vertex in nodeOrderingQueue.
        boolean contracted; // to check if vertex is contracted
        
        int edgeDiff; // egdediff = sE - inE - outE. (sE=inE*outE , i.e number of shortcuts that we
        // may have to add.)
        long delNeighbors; // number of contracted neighbors.
        int shortcutCover; // number of shortcuts to be introduced if this vertex is contracted.
        
        long importance; // total importance = edgediff + shortcutcover + delneighbors.

        int contractId; // id for the vertex that is going to be contracted.
        int sourceId; // it contains the id of vertex for which we will apply dijkstra while
                      // contracting.
        
        // dijkstra properties
        int forwqueryId; // id for forward search.
        int revqueryId; // id for backward search.

        long queryDist; // for forward distance.
        long revDistance; // for backward distance.

        boolean forwProcessed; // is processed in forward search.
        boolean revProcessed; // is processed in backward search.
        
        // to updating priority in queue without calling remove first
        boolean inForwQueue;
        boolean inRevQueue;

        public void clean() {
            cleanForw();
            cleanBackw();
        }

        private void cleanForw() {
            queryDist = Integer.MAX_VALUE;
            forwProcessed = false;
            inForwQueue = false;
        }
        
        private void cleanBackw() {
            revDistance = Integer.MAX_VALUE;
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
    }

    static class Edge {
        Integer linkedNode;
        Long cost;

        public Edge(Integer node, Long cost) {
            this.linkedNode = node;
            this.cost = cost;
        }
    }

}