import java.util.*;

/*
    Task. Given 𝑛 points on a plane and an integer 𝑘, compute the largest possible value of 𝑑 such that the
    given points can be partitioned into 𝑘 non-empty subsets in such a way that the distance between any
    two points from different subsets is at least 𝑑.
    
    Input Format. The first line contains the number 𝑛 of points. Each of the following 𝑛 lines defines a point
    (𝑥𝑖, 𝑦𝑖). The last line contains the number 𝑘 of clusters.
    
    Constraints. 2 ≤ 𝑘 ≤ 𝑛 ≤ 200; −10^3 ≤ 𝑥𝑖, 𝑦𝑖 ≤ 10^3 are integers. All points are pairwise different.
    
    Output Format. Output the largest value of 𝑑. The absolute value of the difference between the answer of
    your program and the optimal value should be at most 10−6. To ensure this, output your answer with
    at least seven digits after the decimal point (otherwise your answer, while being computed correctly,
    can turn out to be wrong because of rounding issues).
*/

// Good job! (Max time used: 0.17/3.00, max memory used: 35926016/2147483648.)
public class Clustering {
    private static double clustering(List<Node> nodes, List<Edge> edges, int k) {
        Double d = Double.POSITIVE_INFINITY;
        Collections.sort(edges);

        DisjoinedSet set = new DisjoinedSet(nodes.size());
        int clusterCount = nodes.size();
        for(Edge e : edges) {
            if(set.find(e.from.id) == set.find(e.to.id))
                continue;
            
            if(clusterCount == k && d > e.w) {
                d = e.w;
                continue;
            }

            set.union(e.from.id, e.to.id);
            clusterCount--;
        }

        return d;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            nodes.add(new Node(i, x, y));
        }
        int k = scanner.nextInt();
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                if(i == j)
                    continue;
                edges.add(new Edge(nodes.get(i), nodes.get(j)));
            }
        }
        scanner.close();
        System.out.printf("%.9f", clustering(nodes, edges, k));
    }
}

class DisjoinedSet {

    int[] parents;
    int[] ranks;

    DisjoinedSet(int n) {
        this.parents = new int[n];
        this.ranks = new int[n];
        for(int i = 0; i < n; i++) {
            parents[i] = i;
            ranks[i] = 1;
        }
    }

    int find(int u) {
        if(parents[u] == u)
            return u;

        int r = find(parents[u]);
        parents[u] = r;

        return r;
    }

    void union(int u, int v) {
        int pu = find(u);
        int pv = find(v);

        if(pu == pv)
            return;

        if(ranks[pu] > ranks[pv])
            parents[pv] = pu;
        else {
            parents[pu] = pv;
            if(ranks[pu] == ranks[pv])
                ranks[pv] += 1;
        }
    }

}

class Node {
    int id;
    int x;
    int y;

    Node(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
}

class Edge implements Comparable<Edge> {
    Node from;
    Node to;
    Double w = Double.POSITIVE_INFINITY;

    Edge(Node from, Node to) {
        this.from = from;
        this.to = to;
        this.w = Math.sqrt(Math.pow((from.x - to.x), 2) + Math.pow((from.y - to.y), 2));
    }

    @Override
    public int compareTo(Edge o) {
        return w.compareTo(o.w);
    }
}