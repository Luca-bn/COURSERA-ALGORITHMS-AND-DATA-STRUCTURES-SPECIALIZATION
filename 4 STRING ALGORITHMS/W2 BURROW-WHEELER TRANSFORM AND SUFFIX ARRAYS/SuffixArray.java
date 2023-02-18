import java.util.*;
import java.io.*;

/*
    Task. Construct the suffix array of a string.

    Input Format. A string Text ending with a “$” symbol.
    
    Constraints. 1 ≤ |Text| ≤ 10^4; except for the last symbol, Text contains symbols A, C, G, T only.
    
    Output Format. SuffixArray(Text), that is, the list of starting positions (0-based) of sorted suffixes separated
    by spaces.
*/

// Good job! (Max time used: 0.45/2.00, max memory used: 150872064/2147483648.)
public class SuffixArray {
    class FastScanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        FastScanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }

    public class Suffix {
        String suffix;
        int start;

        Suffix(String suffix, int start) {
            this.suffix = suffix;
            this.start = start;
        }
    }

    // Build suffix array of the string text and
    // return an int[] result of the same length as the text
    // such that the value result[i] is the index (0-based)
    // in text where the i-th lexicographically smallest
    // suffix of text starts.
    public int[] computeSuffixArray(String text) {
        
        int[] suffixArray = new int[text.length()];
        Suffix[] suffixes = new Suffix[text.length()];
        for(int i = 0; i < text.length(); i++) {
            suffixes[i] = new Suffix(text.substring(i), i);
        }
        Arrays.sort(suffixes, (s1, s2) -> s1.suffix.compareTo(s2.suffix));
        for(int i = 0; i < suffixArray.length; i++)
            suffixArray[i] = suffixes[i].start;

        return suffixArray;
    }

    static public void main(String[] args) throws IOException {
        new SuffixArray().run();
    }

    public void print(int[] x) {
        for (int a : x) {
            System.out.print(a + " ");
        }
        System.out.println();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        int[] SuffixArray = computeSuffixArray(text);
        print(SuffixArray);
    }
}
