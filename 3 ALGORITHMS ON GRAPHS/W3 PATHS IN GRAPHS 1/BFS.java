import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/*
    Task. Given an undirected graph with π vertices and π edges and two vertices π’ and π£, compute the length
    of a shortest path between π’ and π£ (that is, the minimum number of edges in a path from π’ to π£).

    Input Format. A graph is given in the standard format. The next line contains two vertices π’ and π£.

    Constraints. 2 β€ π β€ 10^5, 0 β€ π β€ 10^5, π’ ΜΈ= π£, 1 β€ π’, π£ β€ π.

    Output Format. Output the minimum number of edges in a path from π’ to π£, or β1 if there is no path.
*/

// Good job! (Max time used: 0.83/3.00, max memory used: 200740864/2147483648.)
public class BFS {

    private static Integer[] dist;
    private static Integer[] prev;

    private static int distance(ArrayList<Integer>[] adj, int s, int t) {
        BreadthFirstSearch(adj, s);
        return dist[t] != null ? dist[t] : -1;
    }

    private static void BreadthFirstSearch(ArrayList<Integer>[] adj, int from) {
        dist = new Integer[adj.length];
        prev = new Integer[adj.length];
        dist[from] = 0;

        Queue<Integer> queue = new LinkedList<>();
        queue.add(from);
        while(!queue.isEmpty()) {
            int v = queue.poll();
            for(int edge : adj[v]) {
                if(dist[edge] == null) {
                    queue.add(edge);
                    dist[edge] = dist[v] + 1;
                    prev[edge] = v;
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y;
            x = scanner.nextInt();
            y = scanner.nextInt();
            adj[x - 1].add(y - 1);
            adj[y - 1].add(x - 1);
        }
        int x = scanner.nextInt() - 1;
        int y = scanner.nextInt() - 1;
        System.out.println(distance(adj, x, y));
    }
}

