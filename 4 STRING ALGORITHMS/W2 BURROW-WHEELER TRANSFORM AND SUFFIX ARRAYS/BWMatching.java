import java.io.*;
import java.util.*;

/*
    Task. Implement BetterBWMatching algorithm.

    Input Format. A string BWT(Text), followed by an integer 𝑛 and a collection of 𝑛 strings Patterns =
    {𝑝1, . . . , 𝑝𝑛} (on one line separated by spaces).
    
    Constraints. 1 ≤ |BWT(Text)| ≤ 10^6; except for the one $ symbol, BWT(Text) contains symbols A, C,
    G, T only; 1 ≤ 𝑛 ≤ 5 000; for all 1 ≤ 𝑖 ≤ 𝑛, 𝑝𝑖 is a string over A, C, G, T; 1 ≤ |𝑝𝑖| ≤ 1 000.
    
    Output Format. A list of integers, where the 𝑖-th integer corresponds to the number of substring matches
    of the 𝑖-th member of Patterns in Text.
*/

// Good job! (Max time used: 2.44/6.00, max memory used: 428343296/2147483648.)
public class BWMatching {

    public static void main(String[] args) throws Exception {
        // stressTest();
        run();
    }

    static void run() throws Exception {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        int patterns = scanner.nextInt();
        BurrowsWheelTransformer bwt = new BurrowsWheelTransformer();
        bwt.setBwtText(text.toCharArray());
        for (int i = 0; i < patterns; i++) {
            System.out.print(bwt.countOccurrenciesOfPattern(scanner.next()));
            System.out.print(" ");
        }
    }

    static class BurrowsWheelTransformer {

        char[][] matrix;
        char[] bwtText;
        BWTHelper helper;

        public char[] compressText(String text) throws Exception {
            return compressText(text.toCharArray());
        }

        public char[] compressText(char[] text) throws Exception {
            this.matrix = new char[text.length][];
            
            // initializing first row of the matrix
            matrix[0] = new char[text.length];
            matrix[0][0] = text[text.length - 1];
            for(int i = 0; i < text.length - 1; i++)
                matrix[0][i] = text[i];

            // filling other rows
            for (int i = 1; i < text.length; i++) {
                matrix[i] = new char[text.length];
                matrix[i][0] = matrix[i - 1][text.length - 1];
                for(int j = 0; j < text.length - 1; j++)
                    matrix[i][j] = matrix[i - 1][j];
            }
            Arrays.sort(matrix);
            setBwtText(transformedText());
            return this.bwtText;
        }

        public char[] transformedText() {
            char[] result = new char[matrix.length];
            for (int i = 0; i < matrix.length; i++)
                result[i] = matrix[i][matrix.length - 1];
            return result;
        }

        public char[] decompressText() throws Exception {
            char[] result = new char[bwtText.length];

            int lastIndex = helper.firstColumn[0].lastColIndex;
            for (int i = 0; i < bwtText.length; i++) {
                BWTElement current = helper.firstColumn[lastIndex];
                lastIndex = current.lastColIndex;
                result[i] = current.currentColSymbol;
            }
            return result;
        }

        public int countOccurrenciesOfPattern(String pattern) throws Exception {

            if (pattern.length() > bwtText.length)
                return 0;

            int top = 0, bottom = bwtText.length - 1;
            int patternIndex = pattern.length() - 1;
            while (top <= bottom) {
                if (patternIndex >= 0) {
                    char symbol = pattern.charAt(patternIndex--);
                    int[] lastColOccurrencies = 
                        helper.findFirstAndLastOccurrencyInRange(symbol, top, bottom);
                    if (lastColOccurrencies == null)
                        return 0;
                    top = lastColOccurrencies[0];
                    bottom = lastColOccurrencies[1];
                } else
                    return bottom - top + 1;
            }

            return bottom - top + 1;
        }

        public void setBwtText(char[] bwtText) throws Exception {
            this.bwtText = bwtText;
            this.helper = new BWTHelper();
            this.helper.preprocess(this.bwtText);
        }
    }

    static class BWTElement {
        Integer firstColIndex;
        Integer lastColIndex;
        Character oppositeColSymbol;
        Character currentColSymbol;

        @Override
        public String toString() {
            return String.format("{ firstColIndex: %s, lastColIndex: %s, oppositeColSymbol: %s, currentColSymbol: %s }",
                    firstColIndex, lastColIndex, oppositeColSymbol, currentColSymbol);
        }
    }

    static class BWTHelper {

        // number of accepted symbols (used to sort faster)
        // private static final int K = 27; // this is for all chars + $
        private static final int K = 5; // this is for A C G T and $
        List<List<BWTElement>> groupedSortedElements = new ArrayList<>(K);
        {
            // inizializing lists with empty values
            for (int i = 0; i < K; i++) {
                groupedSortedElements.add(new ArrayList<>());
            }
        }

        BWTElement[] firstColumn;
        BWTElement[] lastColumn;

        Integer[] firstOccurrencyInLastColumn = new Integer[K];
        Integer[] firstOccurrencyInFirstColumn = new Integer[K];

        int[][] firstColumnCount = new int[K][];
        int[][] lastColumnCount = new int[K][];

        void preprocess(String bwtText) throws Exception {
            preprocess(bwtText.toCharArray());
        }

        void preprocess(char[] bwtText) throws Exception {

            // initializing count
            for (int i = 0; i < K; i++) {
                firstColumnCount[i] = new int[bwtText.length + 1];
                lastColumnCount[i] = new int[bwtText.length + 1];
            }

            // create lastColumn mapping
            lastColumn = new BWTElement[bwtText.length];
            for (int i = 0; i < bwtText.length; i++) {
                // creating wrapper element for every symbol of the string with the information
                // of the lastColumn properties
                BWTElement el = new BWTElement();
                el.lastColIndex = i;
                el.currentColSymbol = bwtText[i];
                lastColumn[i] = el;

                int index = getCharPosition(el.currentColSymbol);
                // saving the wrapper in this list to sort the elements faster in the next step
                groupedSortedElements.get(index).add(el);
                // saving informations of firstOccurrency of this symbol and its count for
                // lastColumn
                if (firstOccurrencyInLastColumn[index] == null)
                    firstOccurrencyInLastColumn[index] = i;
                for (int k = 0; k < K; k++)
                    lastColumnCount[k][i + 1] = lastColumnCount[k][i];
                lastColumnCount[index][i + 1] = lastColumnCount[index][i] + 1;
            }

            // sorting last column (creating firstColumn) in more or less O(N + K) and
            // adding missing mappings
            firstColumn = new BWTElement[lastColumn.length];
            int index = 0;
            for (List<BWTElement> list : groupedSortedElements) {
                for (BWTElement p : list) {
                    // adding for each wrapper its informations related to the first column value
                    firstColumn[index] = p;
                    lastColumn[p.lastColIndex].firstColIndex = index;
                    lastColumn[index].oppositeColSymbol = p.currentColSymbol;

                    // saving informations of firstOccurrency of this symbol and its count for
                    // firstColumn
                    int charIndex = getCharPosition(p.currentColSymbol);
                    if (firstOccurrencyInFirstColumn[charIndex] == null)
                        firstOccurrencyInFirstColumn[charIndex] = index;
                    for (int k = 0; k < K; k++)
                        firstColumnCount[k][index + 1] = firstColumnCount[k][index];
                    firstColumnCount[charIndex][index + 1] = firstColumnCount[charIndex][index] + 1;
                    index++;
                }
            }

            /*
             * at this point, firstColumn and lastColumn will contains the same objects
             * (sorted in different ways) and each of them will contains the the symbol
             * in the current column and the symbol at this position in the other one.
             * It will olso contains the the index of currentColSymbol in 
             * firstColumn and LastColumn
             * 
             * for example, if bwtText(=lastColumn) = "ATT$AA" => firstColumn = "$AAATT"
             * firstColumn[0] = 
             * { 
             *      firstColIndex: 0, (in firstColumn elements are sorted by this index)
             *      lastColIndex: 3 (position of $ in lastColumn), 
             *      currentColSymbol: $ (this symbol is in position 0 for firstColumn and in position 3 for lastColumn)
             *      oppositeColSymbol: A, (symbol in lastColumn at position 0)
             * }
             * lastColumn[0] = 
             * {    
             *      firstColIndex: 1 (position of A in firstColumn), 
             *      lastColIndex: 0, (in lastColumn elements are sorted by this index)
             *      currentColSymbol: A (this symbol is in position 0 for lastColumn and in position 1 for firstColumn)
             *      oppositeColSymbol: $, (symbol in firstColumn at position 0)
             * }
             */
        }

        private int[] findFirstAndLastOccurrencyInRange(char symbol, int top, int bottom) throws Exception {
            int symbolIndex = getCharPosition(symbol);
            if (firstOccurrencyInFirstColumn[symbolIndex] == null)
                return null;
            int topCount = lastColumnCount[symbolIndex][top];
            int bottomCount = lastColumnCount[symbolIndex][bottom + 1];

            return new int[] { firstOccurrencyInFirstColumn[symbolIndex] + topCount,
                firstOccurrencyInFirstColumn[symbolIndex] + bottomCount - 1 };
        }

        // in this case the only admitted chars are A, C, G, T and $
        int getCharPosition(char c) throws Exception {
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

        // this method allows all chars (in upper case), but need to set K = 27
        // int getCharPosition(char c) throws Exception {
        // switch (c) {
        // case '$':
        // return 0;
        // case 'A':
        // return 1;
        // case 'B':
        // return 2;
        // case 'C':
        // return 3;
        // case 'D':
        // return 4;
        // case 'E':
        // return 5;
        // case 'F':
        // return 6;
        // case 'G':
        // return 7;
        // case 'H':
        // return 8;
        // case 'I':
        // return 9;
        // case 'J':
        // return 10;
        // case 'K':
        // return 11;
        // case 'L':
        // return 12;
        // case 'M':
        // return 13;
        // case 'N':
        // return 14;
        // case 'O':
        // return 15;
        // case 'P':
        // return 16;
        // case 'Q':
        // return 17;
        // case 'R':
        // return 18;
        // case 'S':
        // return 19;
        // case 'T':
        // return 20;
        // case 'U':
        // return 21;
        // case 'W':
        // return 22;
        // case 'X':
        // return 23;
        // case 'Y':
        // return 24;
        // case 'V':
        // return 25;
        // case 'Z':
        // return 26;
        // default:
        // throw new Exception();
        // }
        // }
    }

    // test for max constraints scenario
    private static void stressTest() throws Exception {
        Random r = new Random();
        // possible chars
        List<Character> charsList = Arrays.asList('A', 'C', 'G', 'T');
        // constraints
        int l = 1000000, n = 5000, p = 1000;
        // creating random compressed text
        StringBuilder bwtTextBuilder = new StringBuilder();
        for (int i = 0; i <= l; i++)
            bwtTextBuilder.append(charsList.get(r.nextInt(charsList.size())));
        // putting $ char at random position
        int dollarPosition = r.nextInt(l);
        bwtTextBuilder.replace(dollarPosition, dollarPosition + 1, "$");
        String bwtText = bwtTextBuilder.toString();

        // finding original text for the random generated string
        BurrowsWheelTransformer bwt = new BurrowsWheelTransformer();
        bwt.setBwtText(bwtText.toCharArray());
        String decompressedTxt = new String(bwt.decompressText());

        // creating random patterns from decompressed text
        List<String> patterns = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int start = r.nextInt(l);
            while (start > l - p)
                start = r.nextInt(l);
            patterns.add(decompressedTxt.substring(start, start + p));
        }

        // performig test (expected output = all 1)
        Long start = System.currentTimeMillis();
        System.out.println("Start");
        bwt = new BurrowsWheelTransformer();
        bwt.setBwtText(bwtText.toCharArray());
        for (int i = 0; i < patterns.size(); i++) {
            System.out.print(bwt.countOccurrenciesOfPattern(patterns.get(i)) + " ");
        }
        System.out.println(String.format("\nFine in: %s ms", System.currentTimeMillis() - start));
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
