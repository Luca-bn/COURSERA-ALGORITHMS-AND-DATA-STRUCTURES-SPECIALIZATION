import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/*
    Problem Description
    Task. Find the first occurence of an integer in the given sorted sequence of integers (possibly with duplicates).
    
    Input Format. The first two lines of the input contain an integer 𝑛 and a sequence 𝑎0 ≤ 𝑎1 ≤ · · · ≤ 𝑎𝑛−1
    of 𝑛 positive integers in non-decreasing order. The next two lines contain an integer 𝑘 and 𝑘 positive
    integers 𝑏0, 𝑏1, . . . , 𝑏𝑘−1.

    Constraints. 1 ≤ 𝑘 ≤ 10^5; 1 ≤ 𝑛 ≤ 3 · 10^4; 1 ≤ 𝑎𝑖 ≤ 10^9 for all 0 ≤ 𝑖 < 𝑛; 1 ≤ 𝑏𝑗 ≤ 10^9 for all 0 ≤ 𝑗 < 𝑘;

    Output Format. For all 𝑖 from 0 to 𝑘 −1, output an index 0 ≤ 𝑗 ≤ 𝑛−1 of the first occurrence of 𝑏𝑖 (i.e.,
    𝑎𝑗 = 𝑏𝑖) or −1 if there is no such index.
*/

// Good job! (Max time used: 0.50/2.00, max memory used: 58220544/536870912.)
public class BinarySearchWithDuplicates {

    public static void main(String[] args) {
        
        FastScanner scanner = new FastScanner(System.in);

        // reading array values
        int arraySize = scanner.nextInt();
        int[] array = new int[arraySize];
        for(int i = 0; i < arraySize; i++) {
            array[i] = scanner.nextInt();
        }

        // reading keys to find
        Map<Integer, Integer> processedValues = new HashMap<>();
        int keysSize = scanner.nextInt();
        while(--keysSize >= 0) {
            Integer toSearch = scanner.nextInt();
            Integer index = processedValues.get(toSearch);
            if(index == null) {
                index = findIndex(array, toSearch, 0, arraySize - 1);
                processedValues.put(toSearch, index);
            }
            System.out.print(index + " ");
        }
    }

    private static Integer findIndex(int[] array, int toFind, int fromIndex, int toIndex) {

        while(fromIndex <= toIndex) {
            int mid = (fromIndex + toIndex) / 2;
            int midValue = array[mid];

            if(midValue == toFind) {
                while(mid > 0) {
                    if(array[mid - 1] < midValue)
                        return mid;
                    mid--;
                }
                return mid;
            }

            if(midValue > toFind)
                toIndex = mid - 1;
            else
                fromIndex = mid + 1;
        }
        
        return -1;
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