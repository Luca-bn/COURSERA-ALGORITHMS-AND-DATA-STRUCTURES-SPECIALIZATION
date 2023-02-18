import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/*
    Problem Description
    
    Input Format. The first line contains an integer 𝑛. The second line contains integers 𝑣1, 𝑣2, . . . , 𝑣𝑛 separated
    by spaces.
    
    Constraints. 1 ≤ 𝑛 ≤ 20, 1 ≤ 𝑣𝑖 ≤ 30 for all 𝑖.
    
    Output Format. Output 1, if it possible to partition 𝑣1, 𝑣2, . . . , 𝑣𝑛 into three subsets with equal sums, and
    0 otherwise.
*/

// Good job! (Max time used: 0.14/3.00, max memory used: 33251328/536870912.)
public class PartitioningSouvenirs {

    public static void main(String[] args) {

        FastScanner scanner = new FastScanner(System.in);
        int n = scanner.nextInt();
        int[] items = new int[n];
        
        for (int i = 0; i < n; i++)
            items[i] = scanner.nextInt();

        System.out.println(triPartitionWithEqualSum(items) ? "1" : "0");
    }

    private static boolean triPartitionWithEqualSum(int[] nums) {
        int sum = Arrays.stream(nums).sum();
        if(nums.length < 3 || sum % 3 != 0)
            return false;

        return subsetSum(nums, 0, sum / 3, sum / 3, sum / 3);
    }

    private static boolean subsetSum(int[] nums, int itemIndex, int remainingA, int remainingB, int remainingC) {

        // base case, all subsets are full
        if(remainingA == 0 && remainingB == 0 && remainingC == 0)
            return true;

        // there are no items left, but some of subsets is not full
        if(itemIndex >= nums.length)
            return false;

        // true if current item has been used in subset A
        boolean A = false;
        if(remainingA - nums[itemIndex] >= 0) {
            A = subsetSum(nums, itemIndex + 1, remainingA - nums[itemIndex], remainingB, remainingC);
        }

        // true if current item has been used in subset B
        boolean B = false;
        if(!A && remainingB - nums[itemIndex] >= 0) {
            B = subsetSum(nums, itemIndex + 1, remainingA, remainingB - nums[itemIndex], remainingC);
        }

        // true if current item has been used in subset C
        boolean C = false;
        if(!A && !B && remainingC - nums[itemIndex] >= 0) {
            B = subsetSum(nums, itemIndex + 1, remainingA, remainingB, remainingC - nums[itemIndex]);
        }

        // if current item has been used in one of the 3 subsets
        return A || B || C;
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