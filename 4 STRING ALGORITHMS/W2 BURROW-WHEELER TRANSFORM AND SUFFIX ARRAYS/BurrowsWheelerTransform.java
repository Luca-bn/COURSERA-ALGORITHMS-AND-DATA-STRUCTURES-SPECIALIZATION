import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/*
    Task. Construct the Burrows–Wheeler transform of a string.

    Input Format. A string Text ending with a “$” symbol.

    Constraints. 1 ≤ |Text| ≤ 1 000; except for the last symbol, Text contains symbols A, C, G, T only.
    
    Output Format. BWT(Text).
*/

// Good job! (Max time used: 0.08/0.75, max memory used: 36093952/2147483648.)
public class BurrowsWheelerTransform {

    public static void main(String[] args) throws IOException {

        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        BurrowsWheelTransformer bwt = new BurrowsWheelTransformer(text);
        // bwt.printMatrix();
        System.out.println(bwt.transformedText());
    }

    static class BurrowsWheelTransformer {

        String[] matrix;

        BurrowsWheelTransformer(String text) {
            this.matrix = new String[text.length()];
            for(int i = 0; i < text.length(); i++) {
                if(i == 0)
                    matrix[i] = text.charAt(text.length() - 1) + text.substring(0, text.length() - 1);
                else matrix[i] = matrix[i - 1].charAt(text.length() - 1) + matrix[i - 1].substring(0, text.length() - 1);
            }
            Arrays.sort(matrix);
        }

        void printMatrix() {
            for(int i = 0; i < matrix.length; i++) {
                for(char c : matrix[i].toCharArray())
                    System.out.print(c + ", ");
                System.out.println();
            }
        }

        public char[] transformedText() {
            char[] result = new char[matrix.length];
            for(int i = 0; i < matrix.length; i++)
                result[i] = matrix[i].charAt(matrix.length - 1);
            return result;
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