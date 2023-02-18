import java.math.BigDecimal;
import java.util.Scanner;

/*
    Problem Description
    Task. Given two non-negative integers 𝑚 and 𝑛, where 𝑚 ≤ 𝑛, find the last digit of the sum 𝐹𝑚 + 𝐹𝑚+1 +
    · · · + 𝐹𝑛.

    Input Format. The input consists of two non-negative integers 𝑚 and 𝑛 separated by a space.

    Constraints. 0 ≤ 𝑚 ≤ 𝑛 ≤ 10^14.

    Output Format. Output the last digit of 𝐹𝑚 + 𝐹𝑚+1 + · · · + 𝐹𝑛.
 */

// Good job! (Max time used: 0.08/1.50, max memory used: 29396992/2147483648.)
public class LastDigitOfFibonacciSumAgain {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long from = scanner.nextLong();
        long to = scanner.nextLong();
        scanner.close();

        System.out.println(getResult(from, to));
    }

    private static Integer getResult(Long from, Long to) {

        Integer indexToFindFrom = new BigDecimal(from).remainder(new BigDecimal(60)).add(BigDecimal.ONE).intValue();
        Integer indexToFindTo = new BigDecimal(to).remainder(new BigDecimal(60)).add(new BigDecimal(2)).intValue();

        Integer[] sequence = new Integer[3];
        sequence[0] = 0;
        sequence[1] = 1;
        sequence[2] = 1;

        Integer fromValue = null;
        if (from < 3) {
            if (from == 0 || from == 1)
                fromValue = 1;
            else
                fromValue = 2;
        }
        Integer toValue = (to < 3) ? to.intValue() + 1 : null;

        for (int i = 3; i <= (indexToFindFrom + indexToFindTo); i++) {
            if (fromValue != null && toValue != null)
                break;

            sequence[0] = sequence[1];
            sequence[1] = sequence[2];
            sequence[2] = (sequence[1] + sequence[0]) % 10;

            if (indexToFindFrom.equals(i) && fromValue == null)
                fromValue = sequence[2];

            if (indexToFindTo.equals(i) && toValue == null)
                toValue = sequence[2];
        }

        return toValue < fromValue ? (toValue + 10) - fromValue : toValue - fromValue;
    }

}
