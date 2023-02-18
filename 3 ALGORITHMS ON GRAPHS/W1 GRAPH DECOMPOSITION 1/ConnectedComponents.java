import java.util.ArrayList;
import java.util.Scanner;

/*
    Task. Given an undirected graph with 𝑛 vertices and 𝑚 edges, compute the number of connected components
    in it.

    Input Format. A graph is given in the standard format.
    
    Constraints. 1 ≤ 𝑛 ≤ 10^3, 0 ≤ 𝑚 ≤ 10^3.
    
    Output Format. Output the number of connected components.
*/

// Good job! (Max time used: 0.12/1.50, max memory used: 33333248/2147483648.)
public class ConnectedComponents {
    private static int numberOfComponents(ArrayList<Integer>[] adj) {
        int result = 0;
        DFS(adj);
        for(int cc : ccs)
            result = Math.max(cc, result);
        return result;
    }


    private static int[] ccs;
    private static boolean[] visited;
    
    private static void DFS(ArrayList<Integer>[] adj) {
        ccs = new int[adj.length];
        visited = new boolean[adj.length];

        int cc = 1;
        for(int v = 0; v < adj.length; v++) {
            if(!visited[v]) {
                explore(adj, v, cc);
                cc++;
            }
        }
    }

    private static void explore(ArrayList<Integer>[] adj, int x, int cc) {
        visited[x] = true;
        ccs[x] = cc;
        for(int edge : adj[x])
            if(!visited[edge])
                explore(adj, edge, cc);
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
        System.out.println(numberOfComponents(adj));
    }
}

