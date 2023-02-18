import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/*
    Task. Compute the number of strongly connected components of a given directed graph with 𝑛 vertices and
    𝑚 edges.

    Input Format. A graph is given in the standard format.
    
    Constraints. 1 ≤ 𝑛 ≤ 10^4, 0 ≤ 𝑚 ≤ 10^4.
    
    Output Format. Output the number of strongly connected components.
*/

// Good job! (Max time used: 0.23/1.50, max memory used: 52436992/2147483648.)
public class StronglyConnected {
    
    private static boolean[] visited = null;
    private static int[] pre = null;
    private static int[] post = null;
    private static int[] ccs = null;
    private static LinkedList<Integer> order = new LinkedList<>();

    private static int numberOfStronglyConnectedComponents(ArrayList<Integer>[] adj) {
        visited = new boolean[adj.length];
        pre = new int[adj.length];
        post = new int[adj.length];
        ccs = new int[adj.length];

        return SCCs(adj);
    }

    private static int SCCs(ArrayList<Integer>[] adj) {
        DFS(reverseGraph(adj));
        visited = new boolean[adj.length];
        AtomicInteger clock = new AtomicInteger(1);
        AtomicInteger cc = new AtomicInteger(1);
        int scc = 0;
        
        List<Integer> sorted = new ArrayList<>(order);
        for(Integer v : sorted) {
            if(!visited[v]) {
                explore(adj, v, cc, clock);
                scc++;
            }
        }
        return scc;
    }

    private static void DFS(List<Integer>[] adj) {

        AtomicInteger clock = new AtomicInteger(1);
        AtomicInteger cc = new AtomicInteger(1);
        for(int i = 0; i < adj.length; i++)
            if(!visited[i])
                explore(adj, i, cc, clock);

    }

    private static void explore(List<Integer>[] adj, int i, AtomicInteger cc, AtomicInteger clock) {
        visited[i] = true;
        pre[i] = clock.get();
        ccs[i] = cc.get();

        for(Integer edge : adj[i]) {
            if(!visited[edge]) {
                clock.incrementAndGet();
                explore(adj, edge, cc, clock);
            }
        }
        
        cc.getAndIncrement();
        post[i] = clock.incrementAndGet();
        order.addFirst(i);
    }

    private static ArrayList<Integer>[] reverseGraph(ArrayList<Integer>[] adj) {

        ArrayList<Integer>[] reversed = new ArrayList[adj.length];
        for(int i = 0; i < reversed.length; i++)
            reversed[i] = new ArrayList<>();
        for(int i = 0; i < adj.length; i++) {
            for(int edge : adj[i]) {
                reversed[edge].add(i);
            }
        }
        return reversed;
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
        System.out.println(numberOfStronglyConnectedComponents(adj));
    }
}

