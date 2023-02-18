import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/*
    Problem Description
    Task. The goal in this code problem is to check whether an input sequence contains a majority element.
    Input Format. The first line contains an integer 𝑛, the next one contains a sequence of 𝑛 non-negative
    integers 𝑎0, 𝑎1, . . . , 𝑎𝑛−1.

    Constraints. 1 ≤ 𝑛 ≤ 10^5; 0 ≤ 𝑎𝑖 ≤ 10^9 for all 0 ≤ 𝑖 < 𝑛.
    
    Output Format. Output 1 if the sequence contains an element that appears strictly more than 𝑛/2 times,
    and 0 otherwise.
*/

// Good job! (Max time used: 0.17/1.50, max memory used: 42684416/2147483648.)
public class MajorityElement {

    public static void main(String[] args) {

        FastScanner scanner = new FastScanner(System.in);

        int n = scanner.nextInt();
        int mid = n / 2;
        int[] elements = new int[n];
        for (int i = 0; i < n; i++) {
            elements[i] = scanner.nextInt();
        }

        int majority = findMajority(elements, 0, n - 1);
        int majorityCount = countMajority(elements, majority, 0, n - 1);
        System.out.println(majorityCount >= mid + 1 ? 1 : 0);
    }

    private static int findMajority(int[] elements, int from, int to) {
        if (from == to)
            return elements[from];

        int mid = (from + to) / 2;

        int leftMajority = findMajority(elements, from, mid);
        int rightMajority = findMajority(elements, mid + 1, to);

        if (leftMajority == rightMajority)
            return leftMajority;

        if (countMajority(elements, leftMajority, from, mid) > countMajority(elements, rightMajority, mid + 1, to))
            return leftMajority;
        else
            return rightMajority;
    }

    private static int countMajority(int[] elements, int majority, int from, int to) {
        int count = 0;
        for (int i = from; i <= to; i++) {
            if (elements[i] == majority)
                count++;
        }
        return count;
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
}