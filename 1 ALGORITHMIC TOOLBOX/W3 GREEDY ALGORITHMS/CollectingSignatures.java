import java.util.*;
import java.util.stream.Collectors;

/*
    Problem Description
    Task. Given a set of 𝑛 segments {[𝑎0, 𝑏0], [𝑎1, 𝑏1], . . . , [𝑎𝑛−1, 𝑏𝑛−1]} with integer coordinates on a line, find
    the minimum number 𝑚 of points such that each segment contains at least one point. That is, find a
    set of integers 𝑋 of the minimum size such that for any segment [𝑎𝑖, 𝑏𝑖] there is a point 𝑥 ∈ 𝑋 such
    that 𝑎𝑖 ≤ 𝑥 ≤ 𝑏𝑖.

    Input Format. The first line of the input contains the number 𝑛 of segments. Each of the following 𝑛 lines
    contains two integers 𝑎𝑖 and 𝑏𝑖 (separated by a space) defining the coordinates of endpoints of the 𝑖-th
    segment.

    Constraints. 1 ≤ 𝑛 ≤ 100; 0 ≤ 𝑎𝑖 ≤ 𝑏𝑖 ≤ 10^9 for all 0 ≤ 𝑖 < 𝑛.
    
    Output Format. Output the minimum number 𝑚 of points on the first line and the integer coordinates
    of 𝑚 points (separated by spaces) on the second line. You can output the points in any order. If there
    are many such sets of points, you can output any set. (It is not difficult to see that there always exist
    a set of points of the minimum size such that all the coordinates of the points are integers.)
*/

// Good job! (Max time used: 0.17/1.50, max memory used: 35504128/2147483648.)
public class CollectingSignatures {
    public static void main(String[] args) {

        Scanner reader = new Scanner(System.in);
        StringTokenizer st = new StringTokenizer(reader.nextLine());
        Integer numberOfSegments = Integer.valueOf(st.nextToken());
        List<Segment> segments = new ArrayList<>(numberOfSegments);

        for (int i = 0; i < numberOfSegments; i++) {
            st = new StringTokenizer(reader.nextLine());
            segments.add(new Segment(Long.valueOf(st.nextToken()), Long.valueOf(st.nextToken())));
        }
        reader.close();

        printSolutions(segments);

    }

    private static void printSolutions(List<Segment> segments) {

        segments.sort((s1, s2) -> s1.finish.compareTo(s2.finish));

        List<Long> coordinates = new ArrayList<>();
        Long currentCoordinate = null;
        while (!segments.isEmpty()) {
            currentCoordinate = segments.get(0).finish;
            coordinates.add(currentCoordinate);

            // for each coordinate, removing all segments which contain it
            for (int i = segments.size() - 1; i >= 0; i--) {
                if (segments.get(i).containsCordinate(currentCoordinate))
                    segments.remove(i);
            }
        }

        System.out.println(coordinates.size());
        System.out.println(coordinates.stream().map(l -> String.valueOf(l)).collect(Collectors.joining(" ")));
    }

    static class Segment {
        Long start;
        Long finish;
        Long length;

        Segment(Long start, Long finish) {
            this.start = start;
            this.finish = finish;
            this.length = finish - start;
        }

        boolean containsCordinate(Long c) {
            return start <= c && c <= finish;
        }
    }
}
