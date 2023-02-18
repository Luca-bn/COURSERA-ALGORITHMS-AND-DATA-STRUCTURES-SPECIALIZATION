import java.io.*;
import java.util.*;

/*
    Task. Reconstruct a string from its Burrows–Wheeler transform.

    Input Format. A string Transform with a single “$” sign.
    
    Constraints. 1 ≤ |Transform| ≤ 1 000 000; except for the last symbol, Text contains symbols A, C, G, T
    only.
    
    Output Format. The string Text such that BWT(Text) = Transform. (There exists a unique such string.)
*/

// Good job! (Max time used: 0.66/3.00, max memory used: 106618880/2147483648.)
public class InverseBWT {
    public static void main(String[] args) throws Exception {

        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        BurrowsWheelTransformer bwt = new BurrowsWheelTransformer();
        // bwt.printMatrix();
        System.out.println(bwt.reconstructText(text));
    }

    static class BurrowsWheelTransformer {

        char[][] matrix;

        BurrowsWheelTransformer() {
        }

        public char[] reconstructText(String text) throws Exception {

            char[] result = new char[text.length()];
            Pair[] pairs = new Pair[text.length()];
            for (int i = 0; i < text.length(); i++)
                pairs[i] = new Pair(text.charAt(i), i);
            PairSorter sorter = new PairSorter(pairs);
            pairs = sorter.result;

            int lastIndex = pairs[0].i;
            for(int i = 0; i < text.length(); i++) {
                Pair current = pairs[lastIndex];
                lastIndex = current.i;
                result[i] = current.c;
            }
            return result;
        }
    }

    static class Pair {
        Character c;
        Integer i;

        Pair(Character c, Integer i) {
            this.c = c;
            this.i = i;
        }

        @Override
        public String toString() {
            return "Pair [c=" + c + ", i=" + i + "]";
        }
    }

    // sort the pair array in O(n)
    static class PairSorter {

        Pair[] result;
        List[] alphabethLists = new List[27];

        PairSorter(Pair[] pairs) throws Exception {
            for (Pair p : pairs) {
                int index = getCharPosition(p.c);
                if (alphabethLists[index] == null)
                    alphabethLists[index] = new ArrayList<>();
                alphabethLists[index].add(p);
            }
            result = new Pair[pairs.length];
            int index = 0;
            for (List<Pair> list : alphabethLists) {
                if (list != null)
                    for (Pair p : list)
                        result[index++] = p;
            }
        }

        int getCharPosition(char c) throws Exception {
            switch (c) {
                case '$':
                    return 0;
                case 'A':
                    return 1;
                case 'B':
                    return 2;
                case 'C':
                    return 3;
                case 'D':
                    return 4;
                case 'E':
                    return 5;
                case 'F':
                    return 6;
                case 'G':
                    return 7;
                case 'H':
                    return 8;
                case 'I':
                    return 9;
                case 'J':
                    return 10;
                case 'K':
                    return 11;
                case 'L':
                    return 12;
                case 'M':
                    return 13;
                case 'N':
                    return 14;
                case 'O':
                    return 15;
                case 'P':
                    return 16;
                case 'Q':
                    return 17;
                case 'R':
                    return 18;
                case 'S':
                    return 19;
                case 'T':
                    return 20;
                case 'U':
                    return 21;
                case 'W':
                    return 22;
                case 'X':
                    return 23;
                case 'Y':
                    return 24;
                case 'V':
                    return 25;
                case 'Z':
                    return 26;
                default:
                    throw new Exception();
            }
        }
    }

    static class FastScanner {
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
}
