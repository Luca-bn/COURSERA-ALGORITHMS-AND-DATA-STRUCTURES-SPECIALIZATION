import java.io.*;
import java.util.*;

/*
    Problem Description
    Task. In this problem your goal is to implement the Rabin–Karp’s algorithm for searching the given pattern
    in the given text.

    Input Format. There are two strings in the input: the pattern 𝑃 and the text 𝑇.

    Constraints. 1 ≤ |𝑃| ≤ |𝑇| ≤ 5 · 10^5. The total length of all occurrences of 𝑃 in 𝑇 doesn’t exceed 108. The
    pattern and the text contain only latin letters.

    Output Format. Print all the positions of the occurrences of 𝑃 in 𝑇 in the ascending order. Use 0-based
    indexing of positions in the the text 𝑇.
*/

// Good job! (Max time used: 3.78/4.50, max memory used: 330268672/2147483648.)
public class HashSubstring {

    private static FastScanner in;
    private static PrintWriter out;
    private static int x = 1;

    public static void main(String[] args) throws Exception {
        run();
    }

    private static void run() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        printOccurrences(rabinKarp(readInput()));
        out.close();
    }

    private static Data readInput() throws IOException {
        String pattern = in.next();
        String text = in.next();
        return new Data(pattern, text);
    }

    private static void printOccurrences(List<Integer> ans) throws IOException {
        for (Integer cur : ans) {
            out.print(cur);
            out.print(" ");
        }
    }

    private static List<Integer> rabinKarp(Data input) {
        String text = input.text, pattern = input.pattern;
        List<Integer> occurrences = new ArrayList<>();
        long[] hashes = precomputeHashes(text, input.pattern.length());
        long hashToFind = hash(pattern);
        for(int i = 0; i < hashes.length; i++)
            if(hashes[i] == hashToFind && areEquals(text.substring(i, i + pattern.length()), pattern))
                occurrences.add(i);
        return occurrences;
    }

    private static long[] precomputeHashes(String text, int patternLength) {
        long[] hashes = new long[text.length() - patternLength + 1];
        hashes[0] = hash(text.substring(0, patternLength));
        for(int i = 1; i < hashes.length; i++) {
            long prevHash = hashes[i - 1];
            long charToRemove = text.charAt(i - 1) * (long) Math.pow(x, patternLength - 1);
            int charToAdd = text.charAt(i + patternLength - 1);
            hashes[i] = ((prevHash - charToRemove) * x) + charToAdd;
        }
        return hashes;
    }

    private static long hash(String s) {
        long hash = 0;
        for(int i = 0; i < s.length(); i++)
            hash += s.charAt(i) * (Math.pow(x, s.length() - i - 1));
        return hash;
    }

    private static boolean areEquals(String substring, String toFind) {
        if (substring.length() != toFind.length())
            return false;
        for (int i = 0; i < substring.length(); i++)
            if (substring.charAt(i) != toFind.charAt(i))
                return false;
        return true;
    }

    static class Data {
        String pattern;
        String text;

        public Data(String pattern, String text) {
            this.pattern = pattern;
            this.text = text;
        }
    }

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}
