import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/*
    Task. Check whether a given directed graph with 𝑛 vertices and 𝑚 edges contains a cycle.

    Input Format. A graph is given in the standard format.
    
    Constraints. 1 ≤ 𝑛 ≤ 10^3, 0 ≤ 𝑚 ≤ 10^3.
    
    Output Format. Output 1 if the graph contains a cycle and 0 otherwise.
*/

// Good job! (Max time used: 0.20/1.50, max memory used: 33476608/2147483648.)
public class Acyclicity {

    private static boolean[] visited = null;
    private static int[] pre = null;
    private static int[] post = null;

    private static int acyclic(ArrayList<Integer>[] adj) {
        visited = new boolean[adj.length];
        pre = new int[adj.length];
        post = new int[adj.length];

        DFS(adj);
        
        for(int v = 0; v < adj.length; v++) {
            List<Integer> edges = adj[v];
            for(Integer edge : edges)
                if(post[v] < post[edge])
                    return 1;
        }
        return 0;
    }

    private static void DFS(ArrayList<Integer>[] adj) {

        AtomicInteger clock = new AtomicInteger(1);
        for(int i = 0; i < adj.length; i++)
            if(!visited[i])
                explore(adj, i, clock);

    }
    
    private static void explore(ArrayList<Integer>[] adj, int i, AtomicInteger clock) {
        visited[i] = true;
        pre[i] = clock.get();
        for(Integer edge : adj[i]) {
            if(!visited[edge]) {
                clock.incrementAndGet();
                explore(adj, edge, clock);
            }
        }
        post[i] = clock.incrementAndGet();
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
        }
        System.out.println(acyclic(adj));
    }
}

