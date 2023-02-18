import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/*
    Problem Description

    Input Format. The first line contains an integer 𝑛, the second line contains 𝑛 integers 𝑎1, . . . , 𝑎𝑛 separated
    by spaces, the third line contains an integer 𝑚.

    Constraints. 1 ≤ 𝑛 ≤ 10^5, 1 ≤ 𝑚 ≤ 𝑛, 0 ≤ 𝑎𝑖 ≤ 10^5 for all 1 ≤ 𝑖 ≤ 𝑛.
    
    Output Format. Output max{𝑎𝑖, . . . , 𝑎𝑖+𝑚−1} for every 1 ≤ 𝑖 ≤ 𝑛 − 𝑚 + 1.
*/

// Good job! (Max time used: 0.47/1.50, max memory used: 64454656/2147483648.)
public class MaximumInSlidingWindow {
    
    public static void main(String[] args) {
        FastScanner scanner = new FastScanner(System.in);
        int n = scanner.nextInt();
        int[] sequence = new int[n];
        for(int i = 0; i < n; i++) {
            sequence[i] = scanner.nextInt();
        }
        int k = scanner.nextInt();

        List<Integer> output = new ArrayList<>();
        Deque<Integer> q = new LinkedList<>();
        int left = 0;
        for(int right = 0; right < n; right++) {

            while(!q.isEmpty() && sequence[q.peekLast()] < sequence[right])
                q.removeLast();
            q.addLast(right);

            if(left > q.peekFirst())
                q.removeFirst();

            if(right+1 >= k) {
                output.add(sequence[q.peekFirst()]);
                left++;
            }
        }

        for(int i : output)
            System.out.print(i + " ");
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

        String nextLine() {
            String line = null;
            try {
                line = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return line;
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
