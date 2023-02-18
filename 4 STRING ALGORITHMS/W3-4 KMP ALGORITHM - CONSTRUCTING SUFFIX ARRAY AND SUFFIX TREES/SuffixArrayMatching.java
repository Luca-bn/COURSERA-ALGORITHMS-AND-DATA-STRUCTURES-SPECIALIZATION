import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/*
    Task. Find all occurrences of a given collection of patterns in a string.

    Input Format. The first line contains a string Text. The second line specifies an integer 𝑛. The last line
    gives a collection of 𝑛 strings Patterns = {𝑝1, . . . , 𝑝𝑛} separated by spaces.

    Constraints. All strings contain symbols A, C, G, T only; 1 ≤ |Text| ≤ 10^5; 1 ≤ 𝑛 ≤ 10^4; Σ︀ (from 𝑖=1 to 𝑛) |𝑝𝑖| ≤ 105.

    Output Format. All starting positions (in any order) in Text where a pattern appears as a substring (using
    0-based indexing as usual). If several patterns occur at the same position of the Text, still output this
    position only once.
*/

// Good job! (Max time used: 0.88/4.00, max memory used: 314470400/2147483648.)
public class SuffixArrayMatching {
    class fastscanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        fastscanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextint() throws IOException {
            return Integer.parseInt(next());
        }
    }

    public int[] computeSuffixArray(String text) throws Exception {
        int[] order = sortCharacters(text);
        int[] clazz = computeCharClasses(text, order);
        int L = 1;
        while (L < text.length()) {
            order = sortDoubled(text, L, order, clazz);
            clazz = updateClasses(order, clazz, L);
            L = 2 * L;
        }

        return order;
    }

    private int[] sortCharacters(String text) throws Exception {
        int[] order = new int[text.length()];
        List<List<Integer>> groupedSortedCharIndexes = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            groupedSortedCharIndexes.add(new ArrayList<>());
        for (int i = 0; i < text.length(); i++) {
            int charIndex = getCharIndexInAlphabeth(text.charAt(i));
            groupedSortedCharIndexes.get(charIndex).add(i);
        }
        int index = 0;
        for (List<Integer> groupedIndexes : groupedSortedCharIndexes)
            for (int i : groupedIndexes)
                order[index++] = i;
        return order;
    }

    private int[] computeCharClasses(String text, int[] order) {
        int[] clazz = new int[text.length()];
        clazz[order[0]] = 0;
        int lastCount = 0;
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(order[i]) == text.charAt(order[i - 1]))
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
        for (int i = 0; i < text.length(); i++)
            count[clazz[i]] = count[clazz[i]] + 1;
        for (int i = 1; i < text.length(); i++)
            count[i] = count[i] + count[i - 1];
        for (int i = text.length() - 1; i >= 0; i--) {
            int start = (order[i] - l + text.length()) % text.length();
            int cl = clazz[start];
            count[cl] = count[cl] - 1;
            newOrder[count[cl]] = start;
        }
        return newOrder;
    }

    private int[] updateClasses(int[] order, int[] clazz, int l) {
        int n = order.length;
        int[] newClazz = new int[n];
        newClazz[order[0]] = 0;
        for (int i = 1; i < n; i++) {
            int cur = order[i], prev = order[i - 1];
            int mid = cur + l, midPrev = (prev + l) % n;
            if (clazz[cur] != clazz[prev] || clazz[mid] != clazz[midPrev])
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

    public List<Integer> findOccurrences(String pattern, String text, int[] suffixArray) {
        List<Integer> result = new ArrayList<>();

        int start = -1, end = -1;
        int top = 0, bottom = suffixArray.length - 1;
        while (top <= bottom) {
            int mid = top + (bottom - top) / 2;
            int compare = compareTexts(pattern, text, suffixArray[mid]);
            if (compare == 0 && (mid == 0 || compareTexts(pattern, text, suffixArray[mid - 1]) != 0)) {
                start = mid;
                break;
            }
            else if (compare >= 0)
                bottom = mid - 1;
            else 
                top = mid + 1;
        }
        if(start == -1)
            return result;
        top = 0;
        bottom = suffixArray.length - 1;
        while (top <= bottom) {
            int mid = top + (bottom - top) / 2;
            int compare = compareTexts(pattern, text, suffixArray[mid]);
            if (compare == 0 && (mid == suffixArray.length - 1 || compareTexts(pattern, text, suffixArray[mid + 1]) != 0)) {
                end = mid;
                break;
            }
            else if (compare > 0)
                bottom = mid - 1;
            else
                top = mid + 1;
        }

        if (start != -1)
            for (int i = start; i <= end; i++)
                if(i != 0)
                    result.add(suffixArray[i]);

        return result;
    }

    // return 0 if suffix starts with pattern, 1 if suffix > pattern, -1 if suffix <
    // pattern
    private int compareTexts(String pattern, String text, int suffixIndex) {
        for (int i = 0; i < pattern.length(); i++) {
            char patternChar = pattern.charAt(i);
            char textChar = text.charAt(suffixIndex + i);
            if(suffixIndex + i > text.length())
                return - 1;
            if (patternChar > textChar)
                return -1;
            else if (patternChar < textChar)
                return 1;
        }
        return 0;
    }

    static public void main(String[] args) throws Exception {
        // new SuffixArrayMatching().stressTest();
        new SuffixArrayMatching().run();
    }

    private void stressTest() throws Exception {
        Random r = new Random();
        List<Character> symbols = Arrays.asList('A', 'C', 'G', 'T');
        int maxTextLength = 10000, maxPatterns = 1000, maxPatternsLength = 10000;
        boolean stop = false;
        while(!stop) {
            StringBuilder textBuilder = new StringBuilder();
            int L = r.nextInt(maxTextLength + 1);
            for(int i = 0 ; i < L; i++)
                textBuilder.append(symbols.get(r.nextInt(4)));
            List<String> patterns = new ArrayList<>();
            int N = r.nextInt(maxPatterns + 1);
            for(int i = 0; i < N; i++) {
                StringBuilder pattern = new StringBuilder();
                int PI = r.nextInt(maxPatternsLength + 1);
                for(int j = 0; j < PI; j++)
                    pattern.append(symbols.get(r.nextInt(4)));
                patterns.add(pattern.toString());
            }
            String text = textBuilder.toString() + "$";
            for(String pattern : patterns) {
                Set<Integer> naiveOccurrencies = findOccurrencesNaive(text, pattern);
                Set<Integer> occurrencies = findOccurrencesWithAlgo(text, pattern);
                if(naiveOccurrencies.size() != occurrencies.size() && !occurrencies.containsAll(naiveOccurrencies)) {
                    writeTestCase(text, pattern, naiveOccurrencies, occurrencies);
                    stop = true;
                    break;
                }
            }
            System.out.print("OK ");
        }
    }

    private void writeTestCase(String text, String pattern, Set<Integer> naiveOccurrencies,
            Set<Integer> occurrencies) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\Users\\ut1257\\Desktop\\PERSONALI\\corsi\\test.txt")))) {
            writer.write(text);
            writer.newLine();
            writer.flush();
            writer.write("PATTERN: " + pattern);
            writer.newLine();
            writer.flush();
            writer.write("NAIVE RESULT:");
            writer.newLine();
            writer.write(naiveOccurrencies.stream().map(i -> String.valueOf(i)).collect(Collectors.joining(", ")));
            writer.newLine();
            writer.flush();
            writer.write("ALGO RESULT:");
            writer.newLine();
            writer.write(occurrencies.stream().map(i -> String.valueOf(i)).collect(Collectors.joining(", ")));
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Set<Integer> findOccurrencesWithAlgo(String text, String pattern) throws Exception {
        Set<Integer> result = new HashSet<>();
        int[] suffixArray = computeSuffixArray(text);
        result.addAll(findOccurrences(pattern, text, suffixArray));
        return result;
    }

    private Set<Integer> findOccurrencesNaive(String text, String pattern) {
        Set<Integer> result = new HashSet<>();
        for(int i = 0; i < text.length() - pattern.length() - 1; i++) {
            boolean match = true;
            for(int j = 0; j < pattern.length(); j++) {
                if(pattern.charAt(j) != text.charAt(i + j)) {
                    match = false;
                    break;
                }
            }
            if(match)
                result.add(i);
        }
        return result;
    }

    public void print(boolean[] x) {
        for (int i = 0; i < x.length; ++i) {
            if (x[i]) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
    }

    public void run() throws Exception {
        fastscanner scanner = new fastscanner();
        String text = scanner.next() + "$";
        int[] suffixArray = computeSuffixArray(text);
        int patternCount = scanner.nextint();
        boolean[] occurs = new boolean[text.length()];
        for (int patternIndex = 0; patternIndex < patternCount; ++patternIndex) {
            String pattern = scanner.next();
            List<Integer> occurrences = findOccurrences(pattern, text, suffixArray);
            for (int x : occurrences) {
                occurs[x] = true;
            }
        }
        print(occurs);
    }
}
