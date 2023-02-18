import java.util.ArrayList;
import java.util.Scanner;

/*
    Task. Given an undirected graph and two distinct vertices 𝑢 and 𝑣, check if there is a path between 𝑢 and 𝑣.
    
    Input Format. An undirected graph with 𝑛 vertices and 𝑚 edges. The next line contains two vertices 𝑢
    and 𝑣 of the graph.
    
    Constraints. 2 ≤ 𝑛 ≤ 10^3; 1 ≤ 𝑚 ≤ 10^3; 1 ≤ 𝑢, 𝑣 ≤ 𝑛; 𝑢 ̸= 𝑣.
    
    Output Format. Output 1 if there is a path between 𝑢 and 𝑣 and 0 otherwise.
*/

// Good job! (Max time used: 0.11/1.50, max memory used: 33157120/2147483648.)
public class FindExitFromMaze {

    private static boolean[] visited;
    private static int reach(ArrayList<Integer>[] adj, int x, int y) {
        visited = new boolean[adj.length];
        explore(adj, x);
        return visited[y] ? 1 : 0;
    }

    private static void explore(ArrayList<Integer>[] adj, int x) {
        visited[x] = true;
        for(int edge : adj[x])
            if(!visited[edge])
                explore(adj, edge);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = new ArrayList[n];
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
        System.out.println(reach(adj, x, y));
        scanner.close();
    }
}

