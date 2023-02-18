import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/*
    Problem Description
    Task. You are given a set of points on a line and a set of segments on a line. The goal is to compute, for
    each point, the number of segments that contain this point.

    Input Format. The first line contains two non-negative integers 𝑠 and 𝑝 defining the number of segments
    and the number of points on a line, respectively. The next 𝑠 lines contain two integers 𝑎𝑖, 𝑏𝑖 defining
    the 𝑖-th segment [𝑎𝑖, 𝑏𝑖]. The next line contains 𝑝 integers defining points 𝑥1, 𝑥2, . . . , 𝑥𝑝.

    Constraints. 1 ≤ 𝑠, 𝑝 ≤ 50000; −10^8 ≤ 𝑎𝑖 ≤ 𝑏𝑖 ≤ 10^8 for all 0 ≤ 𝑖 < 𝑠; −10^8 ≤ 𝑥𝑗 ≤ 10^8 for all 0 ≤ 𝑗 < 𝑝.
    
    Output Format. Output 𝑝 non-negative integers 𝑘0, 𝑘1, . . . , 𝑘𝑝−1 where 𝑘𝑖 is the number of segments which
    contain 𝑥𝑖. More formally,
    𝑘𝑖 = |{𝑗 : 𝑎𝑗 ≤ 𝑥𝑖 ≤ 𝑏𝑗}| .
*/

// Good job! (Max time used: 0.93/6.00, max memory used: 100597760/2147483648.)
public class PointsAndSegments {

    public static void main(String[] args) {
        FastScanner scanner = new FastScanner(System.in);
        int numberOfSegments = scanner.nextInt();
        int numberOfPoints = scanner.nextInt();

        List<Point> allPoints = new ArrayList<>();

        for (int i = 0; i < numberOfSegments; i++) {
            allPoints.add(new Point(scanner.nextInt(), PointType.START_RANGE));
            allPoints.add(new Point(scanner.nextInt(), PointType.END_RANGE));
        }

        UUID[] indexedPoints = new UUID[numberOfPoints];
        Map<UUID, Integer> pointsCount = new HashMap<>();
        for (int i = 0; i < numberOfPoints; i++) {
            Point p = new Point(scanner.nextInt(), PointType.POINT);
            allPoints.add(p);
            pointsCount.put(p.id, 0);
            indexedPoints[i] = p.id;
        }

        allPoints.sort(new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                int coordinateCheck = p1.coordinate.compareTo(p2.coordinate);
                return coordinateCheck != 0 ? coordinateCheck : p1.type.PRIORITY.compareTo(p2.type.PRIORITY);
            }
        });

        // for(Point p : allPoints)
        //     System.out.println(p);

        int rangeOpened = 0;
        for (Point point : allPoints) {
            switch (point.type) {
                case START_RANGE: {
                    rangeOpened++;
                    break;
                }
                case END_RANGE: {
                    rangeOpened --;
                    break;
                }
                case POINT: {
                    pointsCount.put(point.id, rangeOpened);
                    break;
                }
            }
        }

        for(UUID p : indexedPoints)
            System.out.print(pointsCount.get(p) + " ");

    }

    static class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        FastScanner(InputStream stream) {
            try {
                br = new BufferedReader(new InputStreamReader(stream));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }

    static class Point {
        final UUID id;
        final Integer coordinate;
        final PointType type;

        Point(Integer coordinate, PointType type) {
            this.id = UUID.randomUUID();
            this.coordinate = coordinate;
            this.type = type;
        }

        @Override
        public String toString() {
            return String.format("{id:%s, coordinate:%s, type:%s}", id, coordinate, type);
        }
    }

    static enum PointType {
        START_RANGE(1),
        POINT(2),
        END_RANGE(3);

        final Integer PRIORITY;
        private PointType(int priority) {
            PRIORITY = priority;
        }
    }

}
