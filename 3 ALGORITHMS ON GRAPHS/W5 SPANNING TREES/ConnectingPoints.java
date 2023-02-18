import java.util.*;
import java.util.Scanner;

/*
    Task. Given 𝑛 points on a plane, connect them with segments of minimum total length such that there is a
    path between any two points. Recall that the length of a segment with endpoints (𝑥1, 𝑦1) and (𝑥2, 𝑦2)
    is equal to √︀[(𝑥1 − 𝑥2)^2 + (𝑦1 − 𝑦2)^2].

    Input Format. The first line contains the number 𝑛 of points. Each of the following 𝑛 lines defines a point
    (𝑥𝑖, 𝑦𝑖).

    Constraints. 1 ≤ 𝑛 ≤ 200; −10^3 ≤ 𝑥𝑖, 𝑦𝑖 ≤ 10^3 are integers. All points are pairwise different, no three
    points lie on the same line.

    Output Format. Output the minimum total length of segments. The absolute value of the difference
    between the answer of your program and the optimal value should be at most 10^−6. To ensure this,
    output your answer with at least seven digits after the decimal point (otherwise your answer, while
    being computed correctly, can turn out to be wrong because of rounding issues).
*/

// Good job! (Max time used: 0.12/3.00, max memory used: 30978048/2147483648.)
public class ConnectingPoints {

    private static double minimumDistance(List<Node> nodes) {
        double result = 0.;
        boolean[] visited = new boolean[nodes.size()];
        nodes.get(0).cost = 0d;

        PriorityQueue<Node> queue = new PriorityQueue<>(nodes);
        while(!queue.isEmpty()) {
            Node current = queue.poll();
            if(visited[current.id])
                continue;
            visited[current.id] = true;
            result += current.cost;
            for(Node n : nodes) {
                if(visited[n.id])
                    continue;
                if(n.cost > distance(n, current)) {
                    n.cost = distance(n, current);
                    n.parent = current.id;
                    queue.remove(n);
                    queue.add(n);
                }
            }
        }

        return result;
    }

    private static Double distance(Node a, Node b) {
        return Math.sqrt(Math.pow((a.x - b.x), 2) + Math.pow((a.y - b.y), 2));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            nodes.add(new Node(i, scanner.nextInt(), scanner.nextInt()));
        }
        scanner.close();
        System.out.printf("%.9f", minimumDistance(nodes));
    }
}

class Node implements Comparable<Node> {
    int parent = -1;
    int id;
    int x;
    int y;
    Double cost = Double.POSITIVE_INFINITY;
    
    Node(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(Node o) {
        return cost.compareTo(o.cost);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Node other = (Node) obj;
        if (id != other.id)
            return false;
        return true;
    }

}