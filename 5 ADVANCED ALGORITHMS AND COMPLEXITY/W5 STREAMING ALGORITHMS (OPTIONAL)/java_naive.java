import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/*
    Input Format. The first line contains the number of children 𝑁, the second line contains the threshold 𝑡.
    This is followed by list 𝐴: 𝑁 lines specifying the number of good things each child did during the
    year. For each 𝑖 = 1, . . . ,𝑁 the corresponding line contains the tuple (𝑘𝑖, 𝑔𝑘𝑖 (separated by a space).
    The next 𝑁 lines specify list 𝐵: the number of bad things each child did during the year. For each
    𝑖 = 1, . . . ,𝑁 the corresponding line contains the tuple (𝑘𝑖, 𝑏𝑘𝑖 ). The next line contains the number of
    queries 𝑞. The last line contains 𝑞 children ids.

    Constraints. All the number are integers. 1 ≤ 𝑁 ≤ 5·10^5; 0 ≤ 𝑘𝑖 ≤ (2^31)−1; 0 ≤ 𝑏𝑗 , 𝑔𝑗 ≤ (2^31)−1, 1 ≤ 𝑞 ≤ 10.
    It is guaranteed that the lists 𝐴 and 𝐵 contain the same children ids (though not necessarily in the
    same order), and that each id appears exactly once in 𝐴 and exactly once in 𝐵. All the query ids are
    present in 𝐴 and 𝐵.
    
    Output Format. For each query id, output 1 if this child is nice, and 0 if the child is naughty. Separate
    these 𝑞 bits by spaces.
*/

// Good job! (Max time used: 7.57/40.00, max memory used: 594690048/1258291200.)
public class java_naive {

    /* PASSED BUT I THINK THAT THE ALGORITHM IS TOTALY WRONG.. */

    private static final int P = (int) Math.pow(2, 61) - 1;
    private static int buckets = 300_000;
    private static int hashFunctions = 80;

    public static void main(String[] args) throws Exception {
        // test();
        run();
    }

    private static void test() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(".\\test-cases.txt")))) {
            Random r = new Random();
            int maxN = 500000;
            int maxId = Integer.MAX_VALUE;
            int maxQueries = 10;
            boolean failed = false;
            int testCase = 1;

            while (!failed) {
                System.out.println("start test case: " + testCase);
                writer.write(String.format("===== test case: %s ======", testCase++));
                writer.newLine();
                int nChilds = /* r.nextInt(maxN - 1) + 1 */500_000;
                int t = r.nextInt(100 - 1) + 1;
                writer.write(nChilds + "\n" + t);
                writer.newLine();

                List<Integer> ids = new ArrayList<>();
                Map<Integer, Integer> M = new HashMap<>();

                long[][] c = new long[hashFunctions][buckets];

                // first list
                for (int i = 0; i < nChilds; i++) {
                    Integer id = r.nextInt(maxId - 1) + 1;
                    while (M.get(id) != null)
                        id = r.nextInt(maxId - 1) + 1;
                    Integer value = r.nextInt(maxN - 1) + 1;
                    M.put(id, value);
                    update(c, id, value);
                    ids.add(id);
                    writer.write(id + " " + value);
                    writer.newLine();
                }
                writer.flush();

                // saving random ids to query
                Set<Integer> idsToQuery = new HashSet<>();
                while (idsToQuery.size() < maxQueries)
                    idsToQuery.add(ids.get(r.nextInt(ids.size())));

                // second list
                while (!ids.isEmpty()) {
                    Integer id = ids.remove(r.nextInt(ids.size()));
                    Integer value = r.nextInt(maxN - 1) + 1;
                    M.put(id, M.get(id) - value);
                    update(c, id, -value);
                    writer.write(id + " " + value);
                    writer.newLine();
                }

                writer.write("10\n" + idsToQuery.stream().map(id -> id.toString()).collect(Collectors.joining(" ")));
                writer.newLine();
                writer.flush();

                String naiveSolution = "N: ";
                String algoSolution = "A: ";
                for (Integer id : idsToQuery) {
                    naiveSolution += M.get(id) < t ? "0 " : "1 ";
                    algoSolution += estimate(c, id) < t ? "0 " : "1 ";
                }
                writer.write(naiveSolution);
                writer.newLine();
                writer.write(algoSolution);
                writer.newLine();
                writer.flush();

                if (!naiveSolution.substring(3).equals(algoSolution.substring(3))) {
                    System.out.println("KO test n: " + (testCase - 1));
                    System.out.println(naiveSolution);
                    System.out.println(algoSolution);
                    failed = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void run() throws FileNotFoundException {
        // Scanner scanner = new Scanner(new File(".\\test-cases.txt"));
        // scanner.nextLine();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int t = scanner.nextInt();

        long[][] c = new long[hashFunctions][buckets];

        for (int i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int value = scanner.nextInt();
            update(c, id, value);
        }

        for (int i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int value = scanner.nextInt();
            update(c, id, -value);
        }

        int queries_n = scanner.nextInt();
        for (int i = 0; i < queries_n; ++i) {
            int q = scanner.nextInt();
            if (estimate(c, q) < t)
                System.out.print("0");
            else
                System.out.print("1");
            System.out.print(" ");
        }
        scanner.close();
    }

    private static void update(long[][] c, int id, int value) {
        for (int r = 0; r < c.length; r++) {
            int hash = hash(id, r, c[0].length);
            c[r][hash] = c[r][hash] + value;
        }
    }

    private static Integer estimate(long[][] c, int id) {
        Long currentValue = null;
        Integer lastCount = 0;
        Map<Long, Integer> valuesMap = new HashMap<>();
        for (int r = 0; r < c.length; r++) {
            long value = c[r][hash(id, r, c[0].length)];
            Integer count = valuesMap.get(value);
            if(count == null)
                count = 0;
            valuesMap.put(value, ++count);
            if(count > lastCount) {
                currentValue = value;
                lastCount = count;
            }
        }
        return currentValue.intValue();
    }

    private static int hash(int id, int r, int m) {
        Long l = Long.valueOf(id);
        l = l * (r + 1);
        l += ((r * 31));
        l = l % P;
        return l.intValue() % m;
    }

    private static int hashSign(int id, int r) {
        return (r * id) % 2 == 0 ? -1 : 1;
    }
}
