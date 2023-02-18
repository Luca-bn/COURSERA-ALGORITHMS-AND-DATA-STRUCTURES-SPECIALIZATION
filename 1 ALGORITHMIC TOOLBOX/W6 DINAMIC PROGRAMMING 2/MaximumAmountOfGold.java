import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/*
    Problem Description
    Task. Given 𝑛 gold bars, find the maximum weight of gold that fits into a bag of capacity 𝑊.

    Input Format. The first line of the input contains the capacity 𝑊 of a knapsack and the number 𝑛 of bars
    of gold. The next line contains 𝑛 integers 𝑤0,𝑤1, . . . ,𝑤𝑛−1 defining the weights of the bars of gold.
    
    Constraints. 1 ≤ 𝑊 ≤ 10^4; 1 ≤ 𝑛 ≤ 300; 0 ≤ 𝑤0, . . . ,𝑤𝑛−1 ≤ 10^5.
    
    Output Format. Output the maximum weight of gold that fits into a knapsack of capacity 𝑊.
*/

// Good job! (Max time used: 0.07/3.00, max memory used: 39518208/2147483648.)
public class MaximumAmountOfGold {

    public static void main(String[] args) {

        FastScanner scanner = new FastScanner(System.in);
        int W = scanner.nextInt();
        int n = scanner.nextInt();
        int[] bars = new int[n];
        for (int i = 0; i < n; i++)
            bars[i] = scanner.nextInt();

        System.out.println(findMaximumAmountOfGold(W, bars));
    }

    private static int findMaximumAmountOfGold(int W, int[] bars) {

        // initializing table
        int[][] table = new int[bars.length + 1][W + 1];
        for (int i = 0; i <= bars.length; i++)
            table[i][0] = 0;
        for (int i = 0; i <= W; i++)
            table[0][i] = 0;

        // building table
        for (int currentBar = 1; currentBar <= bars.length; currentBar++) {
            int currentBarW = bars[currentBar - 1];

            for (int currentW = 1; currentW <= W; currentW++) {
                // the weight of the bar is too heavy for current index of total Weight
                if (currentBarW > currentW)
                    table[currentBar][currentW] = table[currentBar - 1][currentW];
                // checking if add or not current bar at actual index of total Wight
                else
                    table[currentBar][currentW] = Math.max(
                         // if this is max, is better to add
                        table[currentBar - 1][currentW - currentBarW] + currentBarW,
                         // else if this one is max, is better to don't add
                        table[currentBar - 1][currentW]);
            }
        }

        // returning maximum value
        for (int i = 0; i <= bars.length; i++) {
            System.out.println();
            for (int j = 0; j <= W; j++) {
                System.out.print(table[i][j] + " ");
            }
        }
        return table[bars.length][W];
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

/*
 * W = 10, bars = { 1, 4, 8 }
 * 
 * 0  1  2  3  4  5  6  7  8  9  10
 * 0  0  0  0  0  0  0  0  0  0  0
 * 1  0  1  1  1  1  1  1  1  1  1 
 * 4  0  1  1  1  5  5  5  5  5  5 
 * 8  0  1  1  1  1  1  1  8  9  9 
 * 
 */