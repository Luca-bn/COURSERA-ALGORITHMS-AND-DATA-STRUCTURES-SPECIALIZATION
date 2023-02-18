import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/*
    Task. Find all occurrences of a pattern in a string.

    Input Format. Strings Pattern and Genome.

    Constraints. 1 ≤ |Pattern| ≤ 10^6; 1 ≤ |Genome| ≤ 10^6; both strings are over A, C, G, T.

    Output Format. All starting positions in Genome where Pattern appears as a substring (using 0-based
    indexing as usual).
*/

// Good job! (Max time used: 2.29/12.00, max memory used: 199057408/2147483648.)
public class KnuthMorrisPratt {
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

    // Find all the occurrences of the pattern in the text and return
    // a list of all positions in the text (starting from 0) where
    // the pattern starts in the text.
    public List<Integer> findPattern(String pattern, String text) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        String wholeText = pattern + "$" + text;
        int[] prefixes = computePrefix(wholeText.toCharArray());
        for(int i = pattern.length() + 1; i < wholeText.length(); i++)
            if(prefixes[i] == pattern.length())
                result.add(i - (2 * pattern.length()));
        return result;
    }

    public int[] computePrefix(char[] text) {
        int[] prefixes = new int[text.length];
        int border = 0;
        for (int i = 1; i < text.length; i++) {
            while (border > 0 && text[i] != text[border])
                border = prefixes[border - 1];
            if (text[i] == text[border])
                border = border + 1;
            else
                border = 0;
            prefixes[i] = border;
        }
        return prefixes;
    }

    static public void main(String[] args) throws IOException {
        new KnuthMorrisPratt().run();
    }

    public void print(List<Integer> x) {
        for (int a : x) {
            System.out.print(a + " ");
        }
        System.out.println();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String pattern = scanner.next();
        String text = scanner.next();
        List<Integer> positions = findPattern(pattern, text);
        print(positions);
    }
}
