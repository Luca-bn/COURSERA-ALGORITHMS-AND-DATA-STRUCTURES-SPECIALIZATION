import java.util.*;
import java.io.*;

/*
    Task. Compute the distance between several pairs of nodes in the network.
    
    Input Format. The first line contains two integers 𝑛 and 𝑚 — the number of nodes and edges in the
    network, respectively. The nodes are numbered from 1 to 𝑛. Each of the following 𝑚 lines contains
    three integers 𝑢, 𝑣 and 𝑙 describing a directed edge (𝑢, 𝑣) of length 𝑙 from the node number 𝑢to the
    node number 𝑣. (Note that some social networks are represented by directed graphs while some other
    correspond naturally to undirected graphs. For example, Twitter is a directed graph (with a directed
    edge (𝑢, 𝑣) meaning that 𝑢 follows 𝑣), while Facebook is an undirected graph (where an undirected
    edge {𝑢, 𝑣} means that 𝑢 and 𝑣 are friends). In this problem, we work with directed graphs only for a
    simple reason. It is easy to turn an undirected graph into a directed one: just replace each undirected
    edge {𝑢, 𝑣} with a pair of directed edges (𝑢, 𝑣) and (𝑣, 𝑢).)
    The next line contains an integer 𝑞 — the number of queries for computing the distance. Each of the
    following 𝑞 lines contains two integers 𝑢 and 𝑣 — the numbers of the two nodes to compute the distance
    from 𝑢 to 𝑣.
    
    Constraints. 1 ≤ 𝑛 ≤ 1 000 000; 1 ≤ 𝑚 ≤ 6 000 000; 1 ≤ 𝑢, 𝑣 ≤ 𝑛; 1 ≤ 𝑙 ≤ 1 000; 1 ≤ 𝑞 ≤ 1 000. For
    Python2, Python3, Ruby and Javascript, 1 ≤ 𝑚 ≤ 2 000 000.
    
    Output Format. For each query, output one integer on a separate line. If there is no path from 𝑢 to 𝑣,
    output −1. Otherwise, output the distance from 𝑢 to 𝑣.
*/

// Good job! (Max time used: 22.79/125.00, max memory used: 831561728/8589934592.)
public class FriendSuggestion {

    private static final Long INFINITE = Long.MAX_VALUE / 4;

    public static void main(String[] args) {
        FastScanner scanner = new FastScanner(System.in);
        // 1 < nodes < 1000000
        int numberOfNodes = scanner.nextInt();
        // 1 < edges < 6000000
        int numberOfEdges = scanner.nextInt();

        // initializing adjacency list (and reversed graph)
        List<Node> graph = new ArrayList<>();
        List<Node> reversedGraph = new ArrayList<>();
        for (int i = 0; i < numberOfNodes; i++) {
            graph.add(new Node(i));
            reversedGraph.add(new Node(i));
        }

        // reading all edges
        for (int i = 0; i < numberOfEdges; i++) {
            int from = scanner.nextInt(), to = scanner.nextInt();
            int cost = scanner.nextInt();
            graph.get(from - 1).addEdge(graph.get(to - 1), Long.valueOf(cost));
            reversedGraph.get(to - 1).addEdge(reversedGraph.get(from - 1), Long.valueOf(cost));
        }

        // finding shortest distance between 2 given nodes
        int numberOfQueries = scanner.nextInt();
        for (int i = 0; i < numberOfQueries; i++)
            System.out
                    .println(bidirectionalDijkstra(graph, reversedGraph, scanner.nextInt() - 1, scanner.nextInt() - 1));
    }

    private static Long bidirectionalDijkstra(List<Node> graph, List<Node> reversedGraph, int from, int to) {

        // resetting graphs
        initializeGraps(graph, reversedGraph, from, to);

        // PriorityQueue<Node> queue = new PriorityQueue<>(graph);
        // PriorityQueue<Node> queueR = new PriorityQueue<>(reversedGraph);
        UpdatablePriorityQueue queue = new UpdatablePriorityQueue(graph, from);
        UpdatablePriorityQueue queueR = new UpdatablePriorityQueue(reversedGraph, to);

        List<Node> processed = new ArrayList<>();
        List<Node> processedR = new ArrayList<>();

        while (true) {
            Node v = queue.poll();
            process(v, queue, processed);
            if (reversedGraph.get(v.id).visited)
                return shortestPath(from, to, graph, reversedGraph, processed, processedR);
            Node vR = queueR.poll();
            process(vR, queueR, processedR);
            if (graph.get(vR.id).visited)
                return shortestPath(from, to, graph, reversedGraph, processed, processedR);
        }

    }

    private static void initializeGraps(List<Node> graph, List<Node> reversedGraph, int from, int to) {
        for (int i = 0; i < graph.size(); i++) {
            Node n = graph.get(i);
            n.distance = INFINITE;
            n.prev = null;
            n.visited = false;
            Node nR = reversedGraph.get(i);
            nR.distance = INFINITE;
            nR.prev = null;
            nR.visited = false;
        }
        graph.get(from).distance = 0l;
        reversedGraph.get(to).distance = 0l;
    }

    private static void process(Node v, UpdatablePriorityQueue queue, List<Node> processed) {
        for (Edge e : v.edges)
            relax(queue, e);
        v.visited = true;
        processed.add(v);
    }

    private static void relax(UpdatablePriorityQueue queue, Edge e) {
        if (e.to.distance > e.from.distance + e.cost) {
            e.to.prev = e.from;
            queue.update(e.to.indexInQueue, e.from.distance + e.cost);
            // updating node in queue
            // e.to.distance = e.from.distance + e.cost;
            // queue.remove(e.to);
            // queue.add(e.to);
        }
    }

    private static Long shortestPath(int from, int to, List<Node> graph,
            List<Node> reversedGraph, List<Node> processed, List<Node> processedR) {

        // retriving shortest distance
        Long distance = INFINITE;
        Integer best = null;
        for (Node n : processed) {
            if (n.distance + reversedGraph.get(n.id).distance < distance) {
                best = n.id;
                distance = n.distance + reversedGraph.get(n.id).distance;
            }
        }
        for (Node n : processedR) {
            if (n.distance + graph.get(n.id).distance < distance) {
                best = n.id;
                distance = n.distance + graph.get(n.id).distance;
            }
        }
        if (best == null)
            return -1l;

        // reconstructing path
        LinkedList<Node> path = new LinkedList<>();
        Node last = graph.get(best).prev;
        while (last != null) {
            path.addFirst(last);
            last = last.prev;
        }
        path.addLast(graph.get(best));
        last = reversedGraph.get(best).prev;
        while (last != null) {
            path.addLast(last);
            last = last.prev;
        }

        if (path.isEmpty())
            return 0l;

        if (path.getFirst().id != from || path.getLast().id != to)
            return -1l;

        // System.out.println(path);
        return distance;
    }
    
    static class Node implements Comparable<Node> {
    
        int id;
        int indexInQueue;
        boolean visited;
        Long distance;
        Node prev;
        List<Edge> edges = new ArrayList<>();
    
        Node(int id) {
            this.id = id;
        }
    
        void addEdge(Node to, Long cost) {
            this.edges.add(new Edge(this, to, cost));
        }
    
        @Override
        public int compareTo(Node o) {
            return distance.compareTo(o.distance);
        }
    
        @Override
        public String toString() {
            return "Node [id=" + (id + 1) + ", distance=" + distance + "]";
        }
    
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + id;
            return result;
        }
    
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Node other = (Node) obj;
            if (id != other.id)
                return false;
            return true;
        }
    }
    
    static class Edge {
        Node from;
        Node to;
        Long cost;
    
        Edge(Node from, Node to, Long cost) {
            this.from = from;
            this.to = to;
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
    }
    
    static class UpdatablePriorityQueue {
    
        Node[] data;
        int size;
    
        UpdatablePriorityQueue(List<Node> graph, int from) {
            this.size = graph.size();
            data = new Node[size];
            int startFromIndex = 1;
            for (Node n : graph) {
                if (n.id == from) {
                    n.indexInQueue = 0;
                    data[0] = n;
                } else {
                    n.indexInQueue = startFromIndex;
                    data[startFromIndex++] = n;
                }
            }
        }
    
        int parent(int n) {
            if(n == 0)
                return -1;
            return (n - 1) / 2;
        }
    
        int left(int n) {
            int left = (n * 2) + 1;
            return left >= size ? -1 : left;
        }
    
        int right(int n) {
            int right = (n * 2) + 2;
            return right >= size ? -1 : right;
        }
    
        Node poll() {
            Node min = data[0];
            data[0] = data[size - 1];
            data[0].indexInQueue = 0;
            size = size - 1;
            shiftDown(0);
            return min;
        }
        
        void shiftUp(int n) {
            while(parent(n) != -1 && data[parent(n)].distance > data[n].distance) {
                swap(n, parent(n));
                n = parent(n);
            }
        }
    
        void shiftDown(int n) {
            int indexToSwap = n;
            if(left(n) != -1 && data[left(n)].distance < data[n].distance)
                indexToSwap = left(n);
            if(right(n) != -1 && data[right(n)].distance < data[indexToSwap].distance)
                indexToSwap = right(n);
            if(indexToSwap != n) {
                swap(indexToSwap, n);
                shiftDown(indexToSwap);
            }
        }
    
        void update(int n, long p) {
            long old = data[n].distance;
            if(old == p)
                return;
            data[n].distance = p;
            if(old > p)
                shiftUp(n);
            else
                shiftDown(n);
        }
    
        void swap(int a, int b) {
            Node temp = data[a];
            data[a] = data[b];
            data[b] = temp;
            data[a].indexInQueue = a;
            data[b].indexInQueue = b;
        }
    }
}