import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/*
    Problem Description
    Task. The goal in this code problem is to implement the binary search algorithm.

    Input Format. The first two lines of the input contain an integer ๐ and a sequence ๐0 < ๐1 < ยท ยท ยท < ๐๐โ1
    of ๐ distinct positive integers in increasing order. The next two line contain an integer ๐ and ๐ positive
    integers ๐0, ๐1, . . . , ๐๐โ1.

    Constraints. 1 โค ๐ โค 10^5; 1 โค ๐ โค 3 ยท 10^4; 1 โค ๐๐ โค 10^9 for all 0 โค ๐ < ๐; 1 โค ๐๐ โค 10^9 for all 0 โค ๐ < ๐;
    
    Output Format. For all ๐ from 0 to ๐ โ 1, output an index 0 โค ๐ โค ๐ โ 1 such that ๐๐ = ๐๐ or โ1 if there
    is no such index.
*/

// Good job! (Max time used: 0.54/2.00, max memory used: 57491456/536870912.)
public class BinarySearch {

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

            if(midValue == toFind)
                return mid;

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