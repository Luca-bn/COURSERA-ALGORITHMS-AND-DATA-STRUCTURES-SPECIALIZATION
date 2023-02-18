import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/*
    Task. Given an directed graph with possibly negative edge weights and with 𝑛 vertices and 𝑚 edges, check
    whether it contains a cycle of negative weight.

    Input Format. A graph is given in the standard format.

    Constraints. 1 ≤ 𝑛 ≤ 10^3, 0 ≤ 𝑚 ≤ 10^4, edge weights are integers of absolute value at most 10^3.

    Output Format. Output 1 if the graph contains a cycle of negative weight and 0 otherwise.
*/

// Good job! (Max time used: 0.70/3.00, max memory used: 179482624/2147483648.)
public class NegativeCycle {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            int x, y;
            long w;
            x = scanner.nextInt();
            y = scanner.nextInt();
            w = scanner.nextInt();
            Edge edge = new Edge(x - 1, y - 1, w);
            edges.add(edge);
        }
        scanner.close();
        System.out.println(negativeCycle(n, edges));
    }

    private static int negativeCycle(int nodes, List<Edge> edges) {

        Long[] dist = new Long[nodes];
        Arrays.fill(dist, Long.valueOf(Integer.MAX_VALUE));
        dist[0] = 0l;

        for (int i = 0; i < nodes; i++)
            for (Edge edge : edges) {
                if (dist[edge.to] > dist[edge.from] + edge.cost)
                    dist[edge.to] = dist[edge.from] + edge.cost;
            }

        for (int i = 0; i < nodes; i++)
            for (Edge edge : edges) {
                if (dist[edge.to] > dist[edge.from] + edge.cost)
                    return 1;
            }

        return 0;
    }
}

class Edge {
    int from, to;
    Long cost;

    public Edge(int from, int to, Long cost) {
        this.from = from;
        this.to = to;
        this.cost = cost;
    }
}