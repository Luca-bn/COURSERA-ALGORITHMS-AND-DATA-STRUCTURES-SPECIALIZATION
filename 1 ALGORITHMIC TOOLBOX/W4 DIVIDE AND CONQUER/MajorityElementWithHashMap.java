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
public class MajorityElementWithHashMap {

    public static void main(String[] args) {
        
        FastScanner scanner = new FastScanner(System.in);
        Integer currentMax = 0;
        Map<Integer, Integer> processedValues = new HashMap<>();

        // reading array values
        int elements = scanner.nextInt();
        int mid = elements / 2;
        for(int i = 0; i < elements; i++) {
            if(currentMax + (elements - i) < mid) {
                System.out.println(0);
                return;
            }
            int currentElement = scanner.nextInt();
            Integer elementCount = processedValues.get(currentElement);
            if(elementCount == null)
                elementCount = 0;
            elementCount++;
            if(elementCount > currentMax)
                currentMax = elementCount;
            if(currentMax > mid) {
                System.out.println(1);
                return;
            }
            processedValues.put(currentElement, elementCount);
        }

        System.out.println(currentMax > mid ? 1 : 0);
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