import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/*
    Problem Description
    Task. Find the maximum value of an arithmetic expression by specifying the order of applying its arithmetic
    operations using additional parentheses.
    
    Input Format. The only line of the input contains a string 𝑠 of length 2𝑛 + 1 for some 𝑛, with symbols
    𝑠0, 𝑠1, . . . , 𝑠2𝑛. Each symbol at an even position of 𝑠 is a digit (that is, an integer from 0 to 9) while
    each symbol at an odd position is one of three operations from {+,-,*}.
    
    Constraints. 0 ≤ 𝑛 ≤ 14 (hence the string contains at most 29 symbols).
    
    Output Format. Output the maximum possible value of the given arithmetic expression among different
    orders of applying arithmetic operations.
 */

// Good job! (Max time used: 0.08/1.50, max memory used: 29351936/2147483648.)
public class MaximumValueOfArithmeticExpression {

    private static final List<String> OPERATORS = Arrays.asList("+", "-", "*");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String exp = scanner.next();
        List<String> operators = new ArrayList<>();
        List<Long> operands = new ArrayList<>();
        for (String c : exp.split("")) {
            if (OPERATORS.contains(c))
                operators.add(c);
            else
                operands.add(Long.valueOf(c));
        }
        scanner.close();
        System.out.println(getMaximValue(operands, operators));
    }

    private static Long getMaximValue(List<Long> operands, List<String> operators) {

        int n = operands.size();
        Long[][] m = new Long[n][n];
        Long[][] M = new Long[n][n];

        for (int i = 0; i < operands.size(); i++) {
            m[i][i] = operands.get(i);
            M[i][i] = operands.get(i);
        }

        for (int s = 1; s < n; s++) {
            for (int i = 0; i < n - s; i++) {
                int j = i + s;
                long[] minMax = minAndMax(M, m, i, j, operators);
                m[i][j] = minMax[0];
                M[i][j] = minMax[1];
            }
        }

        return M[0][n - 1];
    }

    private static long[] minAndMax(Long[][] M, Long[][] m, int i, int j, List<String> operators) {
        Long minVal = Long.MAX_VALUE;
        Long maxVal = Long.MIN_VALUE;

        for (int k = i; k < j; k++) {
            long a = calc(M[i][k], M[k + 1][j], operators.get(k));
            long b = calc(M[i][k], m[k + 1][j], operators.get(k));
            long c = calc(m[i][k], M[k + 1][j], operators.get(k));
            long d = calc(m[i][k], m[k + 1][j], operators.get(k));
            minVal = Collections.min(Arrays.asList(minVal, a, b, c, d));
            maxVal = Collections.max(Arrays.asList(maxVal, a, b, c, d));
        }

        return new long[] { minVal, maxVal };
    }

    private static Long calc(long a, long b, String op) {
        if (op.equals("+")) {
            return a + b;
        } else if (op.equals("-")) {
            return a - b;
        } else if (op.equals("*")) {
            return a * b;
        } else {
            assert false;
            return 0l;
        }
    }
}
