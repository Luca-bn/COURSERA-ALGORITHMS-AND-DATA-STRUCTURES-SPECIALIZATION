import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/*
    Task. Compute a topological ordering of a given directed acyclic graph (DAG) with 𝑛 vertices and 𝑚 edges.

    Input Format. A graph is given in the standard format.

    Constraints. 1 ≤ 𝑛 ≤ 10^5, 0 ≤ 𝑚 ≤ 10^5. The given graph is guaranteed to be acyclic.

    Output Format. Output any topological ordering of its vertices. (Many DAGs have more than just one
    topological ordering. You may output any of them.)
*/

// Good job! (Max time used: 0.87/3.00, max memory used: 210952192/2147483648.)
public class Toposort {

    private static boolean[] visited = null;
    private static int[] pre = null;
    private static int[] post = null;
    private static LinkedList<Integer> order = new LinkedList<>();

    private static List<Integer> toposort(ArrayList<Integer>[] adj) {
        visited = new boolean[adj.length];
        pre = new int[adj.length];
        post = new int[adj.length];
        
        DFS(adj);
        
        return order;
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
        order.addFirst(i);
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
        List<Integer> order = toposort(adj);
        for (int x : order) {
            System.out.print((x + 1) + " ");
        }
    }
}

