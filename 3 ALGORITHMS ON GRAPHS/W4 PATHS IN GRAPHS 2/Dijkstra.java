import java.util.*;

/*
    Task. Given an directed graph with positive edge weights and with 𝑛 vertices and 𝑚 edges as well as two
    vertices 𝑢 and 𝑣, compute the weight of a shortest path between 𝑢 and 𝑣 (that is, the minimum total
    weight of a path from 𝑢 to 𝑣).

    Input Format. A graph is given in the standard format. The next line contains two vertices 𝑢 and 𝑣.
    
    Constraints. 1 ≤ 𝑛 ≤ 10^4, 0 ≤ 𝑚 ≤ 10^5, 𝑢 ̸= 𝑣, 1 ≤ 𝑢, 𝑣 ≤ 𝑛, edge weights are non-negative integers not
    exceeding 10^8.
    
    Output Format. Output the minimum weight of a path from 𝑢 to 𝑣, or −1 if there is no path.
*/

// Good job! (Max time used: 1.01/3.00, max memory used: 257523712/2147483648.)
public class Dijkstra {

    public static void main(String arg[]) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        Map<Integer, Node> nodesMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            nodesMap.put(i, new Node(i));
        }
        for (int i = 0; i < m; i++) {
            int x, y;
            long w;
            x = scanner.nextInt();
            y = scanner.nextInt();
            w = scanner.nextInt();
            nodesMap.get(x - 1).addEdge(nodesMap.get(y - 1), w);
        }
        int x = scanner.nextInt() - 1;
        int y = scanner.nextInt() - 1;
        scanner.close();
        System.out.println(dijkstra(nodesMap, x, y));
    }

    private static Long dijkstra(Map<Integer, Node> nodesMap, int from, int to) {
        if(from == to)
            return 0l;

        if(nodesMap.get(from).edges.isEmpty())
            return -1l;

        nodesMap.get(from).distance = 0l;
        PriorityQueue<Node> queue = new PriorityQueue<>(nodesMap.values());

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if(current.distance == Long.MAX_VALUE)
                continue;
            for (Edge edge : current.edges) {
                Node next = edge.node;
                if (next.distance > current.distance + edge.cost) {
                    next.prev = current;
                    next.distance = current.distance + edge.cost;
                    // update element in queue
                    queue.remove(next);
                    queue.add(next);
                    // System.out.println(
                    //         String.format("Checking %s -> %s, distance: %s", current.index + 1, next.index + 1,
                    //                 next.distance));
                }
            }
        }

        Node target = nodesMap.get(to);
        while (target.prev != null)
            target = target.prev;
        if (target.index != from)
            return -1l;
        return nodesMap.get(to).distance;
    }

}

class Node implements Comparable<Node> {
    int index;
    Node prev;
    Long distance = Long.MAX_VALUE;
    List<Edge> edges = new ArrayList<>();

    Node(int index) {
        this.index = index;
    }

    void addEdge(Node n, Long cost) {
        edges.add(new Edge(n, cost));
    }

    @Override
    public int compareTo(Node o) {
        return distance.compareTo(o.distance);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + index;
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
        if (index != other.index)
            return false;
        return true;
    }

}

class Edge {
    Node node;
    Long cost;

    public Edge(Node n, Long cost) {
        this.node = n;
        this.cost = cost;
    }
}