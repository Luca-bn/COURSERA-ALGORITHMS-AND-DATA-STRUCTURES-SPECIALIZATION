import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
import java.math.BigInteger;

/*
    Task. For an integer parameter 𝑘 and two strings 𝑡 = 𝑡0𝑡1 · · · 𝑡𝑚−1 and 𝑝 = 𝑝0𝑝1 · · · 𝑝𝑛−1, we say that
    𝑝 occurs in 𝑡 at position 𝑖 with at most 𝑘 mismatches if the strings 𝑝 and 𝑡[𝑖 : 𝑖 + 𝑝) = 𝑡𝑖𝑡𝑖+1 · · · 𝑡𝑖+𝑛−1
    differ in at most 𝑘 positions.

    Input Format. Every line of the input contains an integer 𝑘 and two strings 𝑡 and 𝑝 consisting of lower
    case Latin letters.

    Constraints. 0 ≤ 𝑘 ≤ 5, 1 ≤ |𝑡| ≤ 200 000, 1 ≤ |𝑝| ≤ min{|𝑡|, 100 000}. The total length of all 𝑡’s does not
    exceed 200 000, the total length of all 𝑝’s does not exceed 100 000.

    Output Format. For each triple (𝑘, 𝑡, 𝑝), find all positions 0 ≤ 𝑖1 < 𝑖2 < · · · < 𝑖𝑙 < |𝑡| where 𝑝 occurs in 𝑡
    with at most 𝑘 mismatches. Output 𝑙 and 𝑖1, 𝑖2, . . . , 𝑖𝑙.
*/

// Good job! (Max time used: 1.56/5.00, max memory used: 358567936/2147483648.)
public class matching_with_mismatches {

    static int m = 1000000007, x = 263;
    static BigInteger mBi = new BigInteger(String.valueOf(m)), xBi = new BigInteger(String.valueOf(x));
    static long[] h1, h2;
    static BigInteger[] powTable;

    public List<Integer> solve(int k, String text, String pattern) {
        ArrayList<Integer> pos = new ArrayList<>();
        preprocess(text, pattern);
        for(int i = 0; i < text.length() - pattern.length() + 1; i++)
            if(checkMatch(i, Math.floorDiv(pattern.length(), 2), pattern.length(), k))
                pos.add(i);
        return pos;
    }

    private void preprocess(String text, String pattern) {
        h1 = hashTable(text);
        h2 = hashTable(pattern);
        powTable = new BigInteger[text.length() + 1];
        powTable[0] = new BigInteger("1");
        for(int i = 1; i < powTable.length; i++) {
            powTable[i] = powTable[i - 1].multiply(xBi).mod(mBi);
        }
    }

    private long[] hashTable(String s) {
        long[] hashtable = new long[s.length() + 1];
        hashtable[0] = 0;
        for(int i = 1; i < hashtable.length; i++)
            hashtable[i] = mod((mod(hashtable[i - 1] * x, m) + s.charAt(i - 1)) , m);
        return hashtable;
    }

    private long mod(long a, long mod) {
        return ((a % mod) + mod) % mod;
    }

    private boolean checkMatch(int aStart, int length, int patternLength, int k) {
        LinkedList<Integer[]> stack = new LinkedList<>();
        stack.add(new Integer[]{aStart, 0, length, 1});
        stack.add(new Integer[]{aStart + length, length, patternLength - length, 1});

        int count = 0, temp = 2, C= 0;
        while(!stack.isEmpty()) {
            Integer[] current = stack.pollFirst();
            int a = current[0], b = current[1], L = current[2], n = current[3];
            
            long u1 = hashValue(h1, a, L);
            long v1 = hashValue(h2, b, L);

            if(temp != n)
                count = C;
            if(u1 != v1) {
                count += 1;
                if(L > 1) {
                    stack.add(new Integer[]{a, b, Math.floorDiv(L, 2), n+1});
                    stack.add(new Integer[]{a + Math.floorDiv(L, 2), b + Math.floorDiv(L, 2), L - Math.floorDiv(L, 2), n+1});
                } else
                    C += 1;
            }
            if(count > k)
                return false;
                temp = n;
        }
        return count <= k;
    }

    private long hashValue(long[] h, int start, int l) {
        return mod((h[start + l] - powTable[l].longValue() * h[start]), m);
    }

    public void run() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        in.lines().forEach(line -> {
            StringTokenizer tok = new StringTokenizer(line);
            int k = Integer.valueOf(tok.nextToken());
            String s = tok.nextToken();
            String t = tok.nextToken();
            List<Integer> ans = solve(k, s, t);
            out.format("%d ", ans.size());
            out.println(ans.stream()
                    .map(n -> String.valueOf(n))
                    .collect(Collectors.joining(" "))
            );
        });
        out.close();
    }

    static public void main(String[] args) {
        new matching_with_mismatches().run();
    }
}
