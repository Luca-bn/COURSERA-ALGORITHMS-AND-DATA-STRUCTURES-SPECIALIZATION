import java.util.*;
import java.io.*;

/*
    Task. Construct the suffix array of a string.

    Input Format. A string Text ending with a “$” symbol.
    
    Constraints. 1 ≤ |Text| ≤ 2 · 10^5; except for the last symbol, Text contains symbols A, C, G, T only.
    
    Output Format. SuffixArray(Text), that is, the list of starting positions of sorted suffixes separated by
    spaces.
*/

// Good job! (Max time used: 0.91/4.00, max memory used: 126423040/2147483648.)
public class SuffixArrayLong {
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

    public class Suffix implements Comparable {
        String suffix;
        int start;

        Suffix(String suffix, int start) {
            this.suffix = suffix;
            this.start = start;
        }

        @Override
        public int compareTo(Object o) {
            Suffix other = (Suffix) o;
            return suffix.compareTo(other.suffix);
        }
    }

    // Build suffix array of the string text and
    // return an int[] result of the same length as the text
    // such that the value result[i] is the index (0-based)
    // in text where the i-th lexicographically smallest
    // suffix of text starts.
    public int[] computeSuffixArray(String text) throws Exception {
        int[] order = sortCharacters(text);
        int[] clazz = computeCharClasses(text, order);
        int L = 1;
        while (L < text.length()) {
            order = sortDoubled(text, L, order, clazz);
            clazz = uldateClasses(order, clazz, L);
            L = 2 * L;
        }

        return order;
    }

    private int[] sortCharacters(String text) throws Exception {
        int[] order = new int[text.length()];
        List<List<Integer>> groupedSortedCharIndexes = new ArrayList<>();
        for(int i = 0; i < 5; i++)
            groupedSortedCharIndexes.add(new ArrayList<>());
        for (int i = 0; i < text.length(); i++) {
            int charIndex = getCharIndexInAlphabeth(text.charAt(i));
            groupedSortedCharIndexes.get(charIndex).add(i);
        }
        int index = 0;
        for(List<Integer> groupedIndexes : groupedSortedCharIndexes)
            for(int i : groupedIndexes)
                order[index++] = i;
        return order;
    }

    private int[] computeCharClasses(String text, int[] order) {
        int[] clazz = new int[text.length()];
        clazz[order[0]] = 0;
        int lastCount = 0;
        for(int i = 1; i < text.length(); i++) {
            if(text.charAt(order[i]) == text.charAt(order[i - 1]))
                clazz[order[i]] = lastCount;
            else 
                clazz[order[i]] = ++lastCount;
        }
        return clazz;
    }


    // TODO: RIVEDERE COME FUNZIONANO QUESTI 2 METODI
    private int[] sortDoubled(String text, int l, int[] order, int[] clazz) {
        int[] count = new int[text.length()];
        int[] newOrder = new int[text.length()];
        for(int i = 0; i < text.length(); i++)
            count[clazz[i]] = count[clazz[i]] + 1;
        for(int i = 1; i < text.length(); i++)
            count[i] = count[i] + count[i - 1];
        for(int i = text.length() - 1; i >= 0; i--) {
            int start = (order[i] - l + text.length()) % text.length();
            int cl = clazz[start];
            count[cl] = count[cl] - 1;
            newOrder[count[cl]] = start;
        }
        return newOrder;
    }

    private int[] uldateClasses(int[] order, int[] clazz, int l) {
        int n = order.length;
        int[] newClazz = new int[n];
        newClazz[order[0]] = 0;
        for(int i = 1; i < n; i++) {
            int cur = order[i], prev = order[i - 1];
            int mid = cur + l, midPrev = (prev + l) % n;
            if(clazz[cur] != clazz[prev] || clazz[mid] != clazz[midPrev]) 
                newClazz[cur] = newClazz[prev] + 1;
            else 
                newClazz[cur] = newClazz[prev];
        }
        return newClazz;
    }

    int getCharIndexInAlphabeth(char c) throws Exception {
        switch (c) {
            case '$':
                return 0;
            case 'A':
                return 1;
            case 'C':
                return 2;
            case 'G':
                return 3;
            case 'T':
                return 4;
            default:
                throw new Exception();
        }
    }

    static public void main(String[] args) throws Exception {
        new SuffixArrayLong().run();
    }

    public void print(int[] x) {
        for (int a : x) {
            System.out.print(a + " ");
        }
        System.out.println();
    }

    public void run() throws Exception {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        int[] suffix_array = computeSuffixArray(text);
        print(suffix_array);
    }
}
