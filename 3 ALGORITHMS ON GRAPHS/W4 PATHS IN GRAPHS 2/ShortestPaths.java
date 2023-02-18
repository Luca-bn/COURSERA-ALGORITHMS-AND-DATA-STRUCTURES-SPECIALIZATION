import java.util.*;

/*
    Task. Given an directed graph with possibly negative edge weights and with 𝑛 vertices and 𝑚 edges as well
    as its vertex 𝑠, compute the length of shortest paths from 𝑠 to all other vertices of the graph.
    
    Input Format. A graph is given in the standard format.

    Constraints. 1 ≤ 𝑛 ≤ 10^3, 0 ≤ 𝑚 ≤ 10^4, 1 ≤ 𝑠 ≤ 𝑛, edge weights are integers of absolute value at most
    10^9.

    Output Format. For all vertices 𝑖 from 1 to 𝑛 output the following on a separate line:
    ∙ “*”, if there is no path from 𝑠 to 𝑢;
    ∙ “-”, if there is a path from 𝑠 to 𝑢, but there is no shortest path from 𝑠 to 𝑢 (that is, the distance
    from 𝑠 to 𝑢 is −∞);
    ∙ the length of a shortest path otherwise.
*/

// Good job! (Max time used: 0.62/3.00, max memory used: 58408960/2147483648.)
public class ShortestPaths {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[n];
        ArrayList<Integer>[] cost = (ArrayList<Integer>[]) new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y, w;
            x = scanner.nextInt();
            y = scanner.nextInt();
            w = scanner.nextInt();
            adj[x - 1].add(y - 1);
            cost[x - 1].add(w);
        }
        int s = scanner.nextInt() - 1;
        long[] distance = new long[n];
        int[] reachable = new int[n];
        int[] shortest = new int[n];
        for (int i = 0; i < n; i++) {
            distance[i] = Integer.MAX_VALUE;
            reachable[i] = 0;
            shortest[i] = 1;
        }
        shortestPaths(adj, cost, s, distance, reachable, shortest);
        for (int i = 0; i < n; i++) {
            if (reachable[i] == 0) {
                System.out.println('*');
            } else if (shortest[i] == 0) {
                System.out.println('-');
            } else {
                System.out.println(distance[i]);
            }
        }
        scanner.close();
    }

    private static void shortestPaths(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost, int s,
            long[] distance, int[] reachable, int[] shortest) {
        defineReachability(adj, s, reachable);
        distance[s] = 0;
        defineNegativeCycle(adj, cost, reachable, shortest, distance);
    }

    private static void defineReachability(ArrayList<Integer>[] adj, int s, int[] reachable) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(s);
        long[] distTo = count(adj, queue);

        for (int i = 0; i < distTo.length; i++) {
            if (distTo[i] != -1) {
                reachable[i] = 1;
            }
        }
    }

    private static long[] count(ArrayList<Integer>[] adj, Queue<Integer> queue) {

        int n = adj.length;
        long[] distTo = new long[n];
        for (int v = 0; v < adj.length; v++) {
            distTo[v] = -1;
        }

        distTo[queue.peek()] = 0;

        while (!queue.isEmpty()) {
            int v = queue.remove();
            for (int w : adj[v]) {
                if (distTo[w] == -1) {
                    queue.add(w);
                    distTo[w] = distTo[v] + 1;
                }
            }
        }
        return distTo;
    }

    private static void defineNegativeCycle(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost,
            int[] reachable, int[] shortest, long[] distance) {
        int n = adj.length;

        for (int v = 0; v < n - 1; v++) {
            processRelax(adj, cost, distance, reachable);
        }

        long[] copy = Arrays.copyOf(distance, distance.length);
        processRelax(adj, cost, distance, reachable);

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < copy.length; i++) {
            if (copy[i] != distance[i] && reachable[i] == 1) {
                queue.add(i);
            }
        }

        if (!queue.isEmpty()) {
            long[] distTo = count(adj, queue);
            for (int i = 0; i < distTo.length; i++) {
                if (distTo[i] != -1) {
                    shortest[i] = 0;
                }
            }
        }
    }

    private static void processRelax(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost, long[] distTo,
            int[] reachable) {
        for (int v = 0; v < adj.length; v++) {
            if (reachable[v] == 1) {
                for (int i = 0; i < adj[v].size(); i++) {
                    int w = adj[v].get(i);
                    if (distTo[w] > distTo[v] + cost[v].get(i)) {
                        distTo[w] = distTo[v] + cost[v].get(i);
                    }
                }
            }
        }
    }
}