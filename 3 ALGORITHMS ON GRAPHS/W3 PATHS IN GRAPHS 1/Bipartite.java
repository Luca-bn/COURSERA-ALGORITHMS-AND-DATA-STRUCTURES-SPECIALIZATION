import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/*
    Task. Given an undirected graph with 𝑛 vertices and 𝑚 edges, check whether it is bipartite.

    Input Format. A graph is given in the standard format.

    Constraints. 1 ≤ 𝑛 ≤ 10^5, 0 ≤ 𝑚 ≤ 10^5.

    Output Format. Output 1 if the graph is bipartite and 0 otherwise.
*/

// Good job! (Max time used: 0.79/3.00, max memory used: 205496320/2147483648.)
public class Bipartite {

    private static Integer[] dist;
    private static Integer[] prev;
    private static boolean[] bipartite;

    private static int bipartite(ArrayList<Integer>[] adj) {
        BreadthFirstSearch(adj);
        for(int i = 0; i < adj.length; i++) {
            for(int edge : adj[i])
                if(bipartite[i] == bipartite[edge])
                    return 0;
        }
        return 1;
    }

    private static void BreadthFirstSearch(ArrayList<Integer>[] adj) {
        dist = new Integer[adj.length];
        prev = new Integer[adj.length];
        bipartite = new boolean[adj.length];

        Queue<Integer> queue = new LinkedList<>();
        for(int i = 0; i < adj.length; i++) {
            if(dist[i] == null) {
                dist[i] = 0;
                queue.add(i);
            }
            while(!queue.isEmpty()) {
                int v = queue.poll();
                for(int edge : adj[v]) {
                    if(dist[edge] == null) {
                        queue.add(edge);
                        dist[edge] = dist[v] + 1;
                        bipartite[edge] = !bipartite[v];
                        prev[edge] = v;
                    }
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
        System.out.println(bipartite(adj));
    }
}

