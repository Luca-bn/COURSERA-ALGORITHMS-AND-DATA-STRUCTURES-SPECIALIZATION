import java.util.*;
import java.util.Map.Entry;
import java.io.*;
import java.math.BigInteger;

/*
    Problem Description
    Input Format. Every line of the input contains two strings 𝑠 and 𝑡 consisting of lower case Latin letters.

    Constraints. The total length of all 𝑠’s as well as the total length of all 𝑡’s does not exceed 100 000.
    
    Output Format. For each pair of strings 𝑠 and 𝑡𝑖, find its longest common substring and specify it by
    outputting three integers: its starting position in 𝑠, its starting position in 𝑡 (both 0-based), and its
    length. More formally, output integers 0 ≤ 𝑖 < |𝑠|, 0 ≤ 𝑗 < |𝑡|, and 𝑙 ≥ 0 such that 𝑠𝑖𝑠𝑖+1 · · · 𝑠𝑖+𝑙−1 =
    𝑡𝑗 𝑡𝑗+1 · · · 𝑡𝑗+𝑙−1 and 𝑙 is maximal. (As usual, if there are many such triples with maximal 𝑙, output any
    of them.)
*/

// Good job! (Max time used: 4.85/5.00, max memory used: 522145792/2147483648.)
public class common_substring {

    static int p1 = 1000000007, p2 = 1000000009, x = 339;
    static BigInteger p1Bi = new BigInteger(String.valueOf(p1)), p2Bi = new BigInteger(String.valueOf(p2)),
            xBi = new BigInteger(String.valueOf(x));

    static public void main(String[] args) throws Exception {
        new common_substring().run();
    }

    public void run() throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        in.lines().forEach(line -> {
            StringTokenizer tok = new StringTokenizer(line);
            String s = tok.nextToken();
            String t = tok.nextToken();
            Answer ans = s.length() > t.length() ? maxLength(s, t, 0, Math.min(s.length(), t.length()), 0, 0, 0)
                    : maxLength(t, s, 0, Math.min(s.length(), t.length()), 0, 0, 0);
            if(s.length() <= t.length())
                System.out.println(String.format("%d %d %d", ans.j, ans.i, ans.len));
            else
                System.out.println(String.format("%d %d %d", ans.i, ans.j, ans.len));
        });
    }

    private Answer maxLength(String stringA, String stringB, int low, int high, int maxLength, int aStart, int bStart) {

        if (low > high)
            return new Answer(aStart, bStart, maxLength);

        int mid = (low + high) / 2;

        long[] aHash1 = hashTable(stringA, mid, p1, p1Bi);
        long[] aHash2 = hashTable(stringA, mid, p2, p2Bi);
        Map<Long, Integer> bHash1 = hashMap(stringB, mid, p1, p1Bi);
        Map<Long, Integer> bHash2 = hashMap(stringB, mid, p2, p2Bi);

        Map<Integer, Integer> matches1 = searchSubstring(aHash1, bHash1);
        Map<Integer, Integer> matches2 = searchSubstring(aHash2, bHash2);

        if (!matches1.isEmpty() && !matches2.isEmpty()) {
            for (Entry<Integer, Integer> entry : matches1.entrySet()) {
                int a = entry.getKey(), b = entry.getValue();
                if (matches2.get(a) != null) {
                    maxLength = mid;
                    aStart = a;
                    bStart = b;
                    aHash1 = null;
                    aHash2 = null;
                    bHash1 = null;
                    bHash2 = null;
                    matches1 = null;
                    matches2 = null;
                    return maxLength(stringA, stringB, mid + 1, high, maxLength, aStart, bStart);
                }
            }
        }
        return maxLength(stringA, stringB, low, mid - 1, maxLength, aStart, bStart);
    }

    private Map<Integer, Integer> searchSubstring(long[] hashTable, Map<Long, Integer> hashMap) {
        Map<Integer, Integer> matches = new HashMap<>();
        for (int i = 0; i < hashTable.length; i++) {
            Integer bStart = hashMap.get(hashTable[i]);
            if (bStart != null) {
                matches.put(i, bStart);
            }
        }
        return matches;
    }

    private Map<Long, Integer> hashMap(String s, int length, int p, BigInteger pBi) {
        Map<Long, Integer> h = new HashMap<>();
        String substring = s.substring(s.length() - length);
        long last = polyHash(substring, p);
        h.put(last, s.length() - length);
        long y = xBi.pow(length).mod(pBi).longValue();
        for (int i = s.length() - length - 1; i >= 0; i--) {
            long current = mod((mod(x * last, p) + s.charAt(i) - (y * s.charAt(i + length))), p);
            h.put(current, i);
            last = current;
        }
        return h;
    }

    private long[] hashTable(String s, int length, int p, BigInteger pBi) {
        long[] h = new long[s.length() - length + 1];
        String substring = s.substring(s.length() - length);
        h[s.length() - length] = polyHash(substring, p);
        long y = xBi.pow(length).mod(pBi).longValue();
        for (int i = s.length() - length - 1; i >= 0; i--)
            h[i] = mod((mod(x * h[i + 1], p) + s.charAt(i) - (y * s.charAt(i + length))), p);
        return h;
    }

    private long polyHash(String s, int p) {
        long hash = 0;
        for (int i = s.length() - 1; i >= 0; i--)
            hash = mod((mod(hash * x, p) + s.charAt(i)), p);
        return hash;
    }

    private long mod(long a, long mod) {
        return ((a % mod) + mod) % mod;
    }

    public class Answer {
        int i, j, len;

        Answer(int i, int j, int len) {
            this.i = i;
            this.j = j;
            this.len = len;
        }
    }
}
