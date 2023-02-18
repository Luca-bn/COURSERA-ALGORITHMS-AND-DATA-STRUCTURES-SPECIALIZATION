import java.io.*;
import java.util.*;

/*
    Task. Compute the distance between several pairs of nodes in the network.
    
    Input Format. The first line contains two integers 𝑛 and 𝑚 — the number of nodes and edges in the
    network, respectively. The nodes are numbered from 1 to 𝑛. Each of the following 𝑛 lines contains the
    coordinates 𝑥 and 𝑦 of the corresponding node. Each of the following 𝑚 lines contains three integers
    𝑢, 𝑣 and 𝑙 describing a directed edge (𝑢, 𝑣) of length 𝑙 from the node number 𝑢 to the node number 𝑣.
    It is guaranteed that 𝑙 ≥ √︀[(𝑥(𝑢) − 𝑥(𝑣))^2 + (𝑦(𝑢) − 𝑦(𝑣))^2] 
    where (𝑥(𝑢), 𝑦(𝑢)) are the coordinates of 𝑢 and (𝑥(𝑣), 𝑦(𝑣)) are the coordinates of 𝑣. 
    The next line contains an integer 𝑞 — the number of queries
    for computing the distance. Each of the following 𝑞 lines contains two integers 𝑢 and 𝑣 — the numbers
    of the two nodes to compute the distance from 𝑢 to 𝑣.
    
    Constraints. 1 ≤ 𝑛 ≤ 110 000; 1 ≤ 𝑚 ≤ 250 000; −10^9 ≤ 𝑥, 𝑦 ≤ 10^9; 1 ≤ 𝑢, 𝑣 ≤ 𝑛; 0 ≤ 𝑙 ≤ 100 000;
    1 ≤ 𝑞 ≤ 10 000. For Python2, Python3, Ruby and Javascript, 1 ≤ 𝑛 ≤ 11 000, 1 ≤ 𝑚 ≤ 30 000.
    
    Output Format. For each query, output one integer. If there is no path from 𝑢 to 𝑣, output −1. Otherwise,
    output the distance from 𝑢 to 𝑣.
*/

// Good job! (Max time used: 59.35/100.00, max memory used: 409812992/2147483648.)
public class DistWithCoords {

    static final Long INFINITE = Long.MAX_VALUE / 4;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt(), m = scanner.nextInt();
        List<Node> graph = new ArrayList<>();
        List<Node> graphR = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int x = scanner.nextInt(), y = scanner.nextInt();
            graph.add(new Node(i, x, y));
            graphR.add(new Node(i, x, y));
        }
        for (int i = 0; i < m; i++) {
            int from = scanner.nextInt() - 1, to = scanner.nextInt() - 1, cost = scanner.nextInt();
            graph.get(from).edges.add(new Edge(graph.get(to), cost));
            graphR.get(to).edges.add(new Edge(graphR.get(from), cost));
        }
        int q = scanner.nextInt();
        List<Long> results = new ArrayList<>();
        for (int i = 0; i < q; i++) {
            // results.add(Bidirectional_A_Star_Algorithm(graph, graphR, scanner.nextInt()
            // -1, scanner.nextInt() -1));
            results.add(A_Star_Algorithm(graph, scanner.nextInt() - 1, scanner.nextInt() - 1));
        }
        for (Long r : results)
            System.out.println(r);
        scanner.close();
    }

    // bidirectional A* algorithm implementation (TOO SLOW)
    private static long Bidirectional_A_Star_Algorithm(List<Node> graph, List<Node> graphR, Integer from, Integer to) {

        if (from == to)
            return 0;

        for (int i = 0; i < graph.size(); i++) {
            Node n = graph.get(i);
            n.distance = INFINITE;
            n.visited = false;
            n.prev = null;
            n.priority = null;
            n.heuristic = null;
            Node nR = graphR.get(i);
            nR.distance = INFINITE;
            nR.visited = false;
            nR.prev = null;
            nR.priority = null;
            nR.heuristic = null;
        }
        Node start = graph.get(from);
        Node target = graph.get(to);
        start.distance = 0l;
        start.heuristic = heuristicDistance(start, target);
        start.priority = start.heuristic;
        Node startR = graphR.get(to);
        Node targetR = graphR.get(from);
        startR.distance = 0l;
        startR.heuristic = heuristicDistance(startR, targetR);
        startR.priority = startR.heuristic;

        Queue<Node> queue = new PriorityQueue<>((Node n1, Node n2) -> n1.priority.compareTo(n2.priority));
        Queue<Node> queueR = new PriorityQueue<>((Node n1, Node n2) -> n1.priority.compareTo(n2.priority));
        queue.add(start);
        queueR.add(startR);
        Set<Node> processed = new HashSet<>();
        Set<Node> processedR = new HashSet<>();
        Long mu = INFINITE;
        while (!queue.isEmpty() && !queueR.isEmpty()) {
            Node current = queue.poll();
            if (current.priority >= mu)
                break;
            if (!current.visited) {
                for (Edge edge : current.edges) {
                    if (edge.to.distance > current.distance + edge.weight) {
                        edge.to.heuristic = bidirectionalHeuristicDistance(edge.to, start, target);
                        edge.to.distance = current.distance + edge.weight;
                        edge.to.priority = edge.to.heuristic + edge.to.distance;
                        edge.to.prev = current;
                        queue.remove(edge.to);
                        queue.add(edge.to);
                    }
                }
                current.visited = true;
                processed.add(current);
            }
            if (graphR.get(current.id).visited && mu > current.distance + graphR.get(current.id).distance) {
                mu = current.distance + graphR.get(current.id).distance;
            }
            Node currentR = queueR.poll();
            if (currentR.priority >= mu)
                break;
            if (!currentR.visited) {
                for (Edge edge : currentR.edges) {
                    if (edge.to.distance > currentR.distance + edge.weight) {
                        edge.to.heuristic = bidirectionalHeuristicDistance(edge.to, startR, targetR);
                        edge.to.distance = currentR.distance + edge.weight;
                        edge.to.priority = edge.to.heuristic + edge.to.distance;
                        edge.to.prev = currentR;
                        queueR.remove(edge.to);
                        queueR.add(edge.to);
                    }
                }
                currentR.visited = true;
                processedR.add(currentR);
            }
            if (graph.get(currentR.id).visited && mu > currentR.distance + graph.get(currentR.id).distance) {
                mu = currentR.distance + graph.get(currentR.id).distance;
            }
        }

        return bidirectionalShortestDistance(graph, graphR, processed, processedR, from, to);
    }

    private static long bidirectionalShortestDistance(List<Node> graph, List<Node> graphR, Set<Node> processed,
            Set<Node> processedR, Integer from, Integer to) {
        Node best = null;
        Long distance = INFINITE;
        for (Node n : processed) {
            if (distance > n.distance + graphR.get(n.id).distance) {
                distance = n.distance + graphR.get(n.id).distance;
                best = n;
            }
        }
        for (Node n : processedR) {
            if (distance > n.distance + graph.get(n.id).distance) {
                distance = n.distance + graph.get(n.id).distance;
                best = n;
            }
        }
        if (best == null)
            return -1;

        LinkedList<Node> path = new LinkedList<>();
        Node last = graph.get(best.id).prev;
        while (last != null) {
            path.addFirst(last);
            last = last.prev;
        }
        path.add(best);
        last = graphR.get(best.id).prev;
        while (last != null) {
            path.addLast(last);
            last = last.prev;
        }
        if (path.get(0).id != from || path.getLast().id != to)
            return -1;
        return distance;
    }

    private static Long bidirectionalHeuristicDistance(Node n, Node from, Node to) {
        return (heuristicDistance(from, n) + heuristicDistance(n, to)) / 2;
    }

    // Monodirectional A* algorithm implementation
    private static long A_Star_Algorithm(List<Node> graph, Integer from, Integer to) {

        if (from == to)
            return 0;

        for (Node n : graph) {
            n.distance = INFINITE;
            n.visited = false;
            n.prev = null;
            n.priority = null;
            n.heuristic = null;
        }
        Node start = graph.get(from);
        Node target = graph.get(to);
        start.distance = 0l;
        start.heuristic = heuristicDistance(start, target);
        start.priority = start.heuristic;

        Queue<Node> queue = new PriorityQueue<>((Node n1, Node n2) -> n1.priority.compareTo(n2.priority));
        queue.add(start);
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (current.id == to)
                return shortestDistance(graph, from, to);
            if (current.visited)
                continue;
            for (Edge edge : current.edges) {
                if (edge.to.visited)
                    continue;
                if (edge.to.distance > current.distance + edge.weight) {
                    edge.to.heuristic = heuristicDistance(edge.to, target);
                    edge.to.distance = current.distance + edge.weight;
                    edge.to.priority = edge.to.heuristic + edge.to.distance;
                    edge.to.prev = current;
                    queue.remove(edge.to);
                    queue.add(edge.to);
                }
            }
            current.visited = true;
        }

        return -1;
    }

    private static long shortestDistance(List<Node> graph, Integer from, Integer to) {
        LinkedList<Node> path = new LinkedList<>();
        Node last = graph.get(to);
        while (last != null) {
            path.addFirst(last);
            last = last.prev;
        }
        if (path.getFirst().id != from || path.getLast().id != to)
            return -1;
        return graph.get(to).distance;
    }

    private static Long heuristicDistance(Node from, Node to) {
        return Double.valueOf(Math.sqrt(Math.pow((from.x - to.x), 2) + Math.pow((from.y - to.y), 2))).longValue();
    }

    // utilities classes
    static class Node {
        final int id, x, y;
        boolean visited;
        Long distance, heuristic, priority;
        Node prev;
        List<Edge> edges = new ArrayList<>();

        Node(int id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }
    }

    static class Edge {
        final Node to;
        final Integer weight;

        Edge(Node to, Integer weight) {
            this.to = to;
            this.weight = weight;
        }
    }
}