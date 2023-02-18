import java.io.*;
import java.util.*;

/*
    Task. The marketing department of your big company has many subdepartments which control advertising
    on TV, radio, web search, contextual advertising, mobile advertising, etc. Each of them has prepared
    their advertising campaign plan, and of course you don’t have enough budget to cover all of their
    proposals. You don’t have enough time to go thoroughly through each subdepartment’s proposals and
    cut them, because you need to set the budget for the next year tomorrow. You decide that you will
    either approve or decline each of the proposals as a whole.
    There is a bunch of constraints you face. For example, your total advertising budget is limited. Also,
    you have some contracts with advertising agencies for some of the advertisement types that oblige
    you to spend at least some fixed budget on that kind of advertising, or you’ll see huge penalties, so
    you’d better spend it. Also, there are different company policies that can be of the form that you
    spend at least 10% of your total advertising spend on mobile advertising to promote yourself in this
    new channel, or that you spend at least $1M a month on TV advertisement, so that people always
    remember your brand. All of these constraints can be rewritten as an Integer Linear Programming: for
    each subdepartment 𝑖, denote by 𝑥𝑖 boolean variable that corresponds to whether you will accept or
    decline the proposal of that subdepartment. Then each constraint can be written as a linear inequality.
    For example, Σ︀ (from 𝑖=1 to 𝑛) spend𝑖 · 𝑥𝑖 ≤ TotalBudget is the inequality to ensure your total budget 
    is enough to accept all the selected proposals. 
    And Σ︀(from 𝑖=1 to 𝑛) spend𝑖 · 𝑥𝑖 ≤ 10 · mobile corresponds to the fact that mobile
    advertisement budget is at least 10% of the total spending.
    You will be given the final Integer Linear Programming problem in the input, and you will need to
    reduce it to SAT. It is guaranteed that there will be at most 3 different variables with
    non-zero coefficients in each inequality of this Integer Linear Programming problem.
    
    Input Format. The first line contains two integers 𝑛 and 𝑚 — the number of inequalities and the number
    of variables. The next 𝑛 lines contain the description of 𝑛×𝑚 matrix 𝐴 with coefficients of inequalities
    (each of the 𝑛 lines contains 𝑚 integers, and at most 3 of them are non-zero), and the last line contains
    the description of the vector 𝑏 (𝑛 integers) for the system of inequalities 𝐴𝑥 ≤ 𝑏. You need to determine
    whether there exists a binary vector 𝑥 satisfying all those inequalities.

    Constraints. 1 ≤ 𝑛,𝑚 ≤ 500; −100 ≤ 𝐴𝑖𝑗 ≤ 100; −1 000 000 ≤ 𝑏𝑖 ≤ 1 000 000.
    
    Output Format. You need to output a boolean formula in the CNF form in a specific format. If it is
    possible to accept some of the proposals and decline all the others while satisfying all the constraints,
    the formula must be satisfiable. Otherwise, the formula must be unsatisfiable. The number of variables
    in the formula must not exceed 3000, and the number of clauses must not exceed 5000.
    On the first line, output integers 𝐶 and 𝑉 — the number of clauses in the formula and the number of
    variables respectively. On each of the next 𝐶 lines, output a description of a single clause. Each clause
    has a form (𝑥4 𝑂𝑅 𝑥1 𝑂𝑅 𝑥8). For a clause with 𝑘 terms (in the example, 𝑘 = 3 for 𝑥4, 𝑥1 and 𝑥8), output
    first those 𝑘 terms and then number 0 in the end (in the example, output “4 − 1 8 0”). Output each
    term as integer number. Output variables 𝑥1, 𝑥2, . . . , 𝑥𝑉 as numbers 1, 2, . . . , 𝑉 respectively. Output
    negations of variables 𝑥1, 𝑥2, . . . , 𝑥𝑉 as numbers −1,−2, . . . ,−𝑉 respectively. Each number other than
    the last one in each line must be a non-zero integer between −𝑉 and 𝑉 where 𝑉 is the total number
    of variables specified in the first line of the output. Ensure that 1 ≤ 𝐶 ≤ 5000 and 1 ≤ 𝑉 ≤ 3000.
    See the examples below for further clarification of the output format.
    If there are many different formulas that satisfy the requirements above, you can output any one of
    them.
*/

// Good job! (Max time used: 0.61/3.00, max memory used: 61292544/2147483648.)
public class BudgetAllocation {
    private final InputReader reader;
    private final OutputWriter writer;

    public BudgetAllocation(InputReader reader, OutputWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public static void main(String[] args) {
        InputReader reader = new InputReader(System.in);
        OutputWriter writer = new OutputWriter(System.out);
        new Thread(null, new Runnable() {
            public void run() {
                try {
                    new BudgetAllocation(reader, writer).run();
                    writer.writer.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "1", 1 << 26).start();
    }

    class ConvertILPToSat {
        int[][] A;
        int[] b;

        ConvertILPToSat(int n, int m) {
            A = new int[n][m];
            b = new int[n];
        }

        void printEquisatisfiableSatFormula() {
            List<String> clauses = new ArrayList<>();
            int count = 0;
            // for each inequality
            for(int i = 0; i < A.length; i++) {
                int[] row = A[i]; // current inequality
                long n = Arrays.stream(row).filter(x -> x != 0).count(); // counting non 0 coefficients (max 3)
                int[][] combinations = getPossibleCominations(n); // getting all possible binary combinations for non 0 coefficients

                // this should happen only if all coefficients in disequality are 0
                if(combinations == null) {
                    // if all coefficients are 0 and b < 0 => there is no solution
                    if(b[i] < 0) { 
                        writer.printf("2 1");
                        writer.printf("1 0\n-1 0\n");
                        return;
                    }
                    // else (0 + ... + 0 <= n with n >= 0, n = 0 is acceptable) skipping current inequality
                    continue;
                }

                // for each combination
                for(int[] combination : combinations) {
                    int combCount = 0;
                    int sum = 0;

                    // multiping non 0 coefficients for corresponding current binary combination
                    for(int x : row)
                        if(x != 0)
                            sum += (x * combination[combCount++]);

                    // if the inequality with current combination of binary values does not respect the original inequality (all inequalities are <=)
                    if(sum > b[i]) {
                        // adding a clause to be verified to SAT
                        String clauseStream = "";
                        boolean isClause = false;
                        combCount = 0;
                        for(int k = 0; k < row.length; k++)
                            if(row[k] != 0) {
                                clauseStream += (combination[combCount] != 0 ? "-" + (k + 1) : (k + 1)) + " ";
                                combCount++;
                                isClause = true;
                            }
                        if(isClause) {
                            clauseStream += "0\n";
                            clauses.add(clauseStream);
                            count++;
                        }
                    }
                }
            }
            // if no clause has been added, already know that this LP is feasable so printing a feasable CNF formula
            if(count == 0) {
                count++;
                clauses.add("1 -1 0\n");
            }
            writer.printf(count + " " + A[0].length + "\n");
            for(String clauseStream : clauses)
                writer.printf(clauseStream);
        }

        private int[][] getPossibleCominations(long n) {
            // 3 is the maximum value of coefficients != 0 in disequality
            if(n == 1)
                return new int[][] {{0}, {1}};
            if(n == 2)
                return new int[][] {
                    {0, 0}, 
                    {0, 1},
                    {1, 0}, 
                    {1, 1}
                };
            if(n == 3)
                return new int[][] {
                    {0, 0, 0}, 
                    {0, 0, 1},
                    {0, 1, 0}, 
                    {0, 1, 1},
                    {1, 0, 0}, 
                    {1, 0, 1},
                    {1, 1, 0}, 
                    {1, 1, 1}
                };
            return null;
        }
    }

    public void run() {
        int n = reader.nextInt();
        int m = reader.nextInt();

        ConvertILPToSat converter = new ConvertILPToSat(n, m);
        for (int i = 0; i < n; ++i) {
          for (int j = 0; j < m; ++j) {
            converter.A[i][j] = reader.nextInt();
          }
        }
        for (int i = 0; i < n; ++i) {
            converter.b[i] = reader.nextInt();
        }

        converter.printEquisatisfiableSatFormula();
    }

    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public double nextDouble() {
            return Double.parseDouble(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    static class OutputWriter {
        public PrintWriter writer;

        OutputWriter(OutputStream stream) {
            writer = new PrintWriter(stream);
        }

        public void printf(String format, Object... args) {
            writer.print(String.format(Locale.ENGLISH, format, args));
        }
    }
}
