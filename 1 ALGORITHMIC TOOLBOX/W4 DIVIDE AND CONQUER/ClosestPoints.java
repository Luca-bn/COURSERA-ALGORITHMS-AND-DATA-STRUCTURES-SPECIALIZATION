import java.io.*;
import java.util.*;

/*
    Problem Description
    Task. Given 𝑛 points on a plane, find the smallest distance between a pair of two (different) points. Recall
    that the distance between points (𝑥1, 𝑦1) and (𝑥2, 𝑦2) is equal to
    √︀[(𝑥1 − 𝑥2)^2 + (𝑦1 − 𝑦2)^2].

    Input Format. The first line contains the number 𝑛 of points. Each of the following 𝑛 lines defines a point
    (𝑥𝑖, 𝑦𝑖).

    Constraints. 2 ≤ 𝑛 ≤ 10^5; −10^9 ≤ 𝑥𝑖, 𝑦𝑖 ≤ 10^9 are integers.
    
    Output Format. Output the minimum distance. The absolute value of the difference between the answer
    of your program and the optimal value should be at most 10^−3. To ensure this, output your answer
    with at least four digits after the decimal point (otherwise your answer, while being computed correctly,
    can turn out to be wrong because of rounding issues).
*/

// Good job! (Max time used: 1.05/3.00, max memory used: 147275776/2147483648.)
public class ClosestPoints {

    public static void main(String[] args) throws Exception {
        reader = new BufferedReader(new InputStreamReader(System.in));
        writer = new PrintWriter(System.out);
        int n = nextInt();
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            points.add(new Point(nextLong(), nextLong()));
        }
        points.sort((p1, p2) -> p1.x.compareTo(p2.x));
        System.out.print(minimalDistance(points, 0, n - 1));
        writer.close();
    }

    static double minimalDistance(List<Point> points, int from, int to) {
        // base case (2 or 3 points) => use brute force
        if (to - from == 1)
            return dist(points.get(0), points.get(1));
        if (to - from == 2)
            return Math.min(Math.min(
                    dist(points.get(0), points.get(1)),
                    dist(points.get(0), points.get(2))),
                    dist(points.get(1), points.get(2)));

        // recursively split array in half, and find minimum distance in that spaces
        int mid = (from + to) / 2;
        double d1 = minimalDistance(points, from, mid);
        double d2 = minimalDistance(points, mid + 1, to);

        double d = Math.min(d1, d2);

        // check for points in the center of the two halfs
        List<Point> pointsInTheMiddle = getSortedPointsInTheMiddle(points, from, mid, to, d);
        for (int i = 0; i < pointsInTheMiddle.size(); i++) {
            Point current = pointsInTheMiddle.get(i);
            for (int j = i + 1; j < pointsInTheMiddle.size(); j++) {
                Point next = pointsInTheMiddle.get(j);
                if ((j - i) == 6 || Math.abs(next.y - current.y) > d)
                    break;
                d = Math.min(d, dist(current, next));
            }
        }

        return d;
    }

    /* RETURNS A LIST WITH ALL ELEMENTS SORTED BY Y COORDINATE WHICH HAVE MAXIMUM DISTANCE FROM MIDDLE LINE == D */
    static List<Point> getSortedPointsInTheMiddle(List<Point> points, int from, int mid, int to, Double d) {
        Long leftCoordinate = points.get(mid).x;
        Long rightCoordinate = points.get(mid + 1).x;
        List<Point> pointsInTheMiddle = new ArrayList<>();
        for (int i = mid; i >= from; i--) {
            Point point = points.get(i);
            if (point.x + d < leftCoordinate)
                break;
            else
                pointsInTheMiddle.add(point);
        }
        for (int i = mid + 1; i <= to; i++) {
            Point point = points.get(i);
            if (point.x - d > rightCoordinate)
                break;
            else
                pointsInTheMiddle.add(point);
        }
        pointsInTheMiddle.sort((p1, p2) -> p1.compareTo(p2));
        return pointsInTheMiddle;
    }

    static Double dist(Point a, Point b) {
        return Math.sqrt(Math.pow((a.x - b.x), 2) + Math.pow((a.y - b.y), 2));
    }

    static class Point implements Comparable<Point> {
        Long x, y;

        public Point(Long x, Long y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Point o) {
            return o.y == y ? Long.signum(x - o.x) : Long.signum(y - o.y);
        }
    }

    /* UTILITY FOR READING INPUT DATA */
    static BufferedReader reader;
    static PrintWriter writer;
    static StringTokenizer tok = new StringTokenizer("");

    static String next() {
        while (!tok.hasMoreTokens()) {
            String w = null;
            try {
                w = reader.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (w == null)
                return null;
            tok = new StringTokenizer(w);
        }
        return tok.nextToken();
    }

    static Long nextLong() {
        return Long.parseLong(next());
    }

    static int nextInt() {
        return Integer.parseInt(next());
    }
}
