import java.math.BigDecimal;
import java.util.Scanner;

/*
    Problem Description
    Task. Given an integer ๐, find the last digit of the sum ๐น0 + ๐น1 + ยท ยท ยท + ๐น๐.

    Input Format. The input consists of a single integer ๐.

    Constraints. 0 โค ๐ โค 10^14.

    Output Format. Output the last digit of ๐น0 + ๐น1 + ยท ยท ยท + ๐น๐.
 */

// Good job! (Max time used: 0.07/1.50, max memory used: 29331456/2147483648.)
public class LastDigitOfFibonacciSum {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long input = scanner.nextLong();
        scanner.close();

        System.out.println(getResult(input));
    }

    private static Integer getResult(Long input) {

        Integer result = -1;

        if (input <= 1)
            return Long.valueOf(input).intValue();

        Long indexToFind = new BigDecimal(input).remainder(new BigDecimal(60)).add(new BigDecimal(2)).longValue();

        Integer[] sequence = new Integer[3];
        sequence[0] = 0;
        sequence[1] = 1;
        sequence[2] = 1;

        for (int i = 3; i <= indexToFind; i++) {
            sequence[0] = sequence[1];
            sequence[1] = sequence[2];
            sequence[2] = (sequence[1] + sequence[0]) % 10;
        }

        result = sequence[2];

        return (result == 0) ? 9 : result - 1;
    }

}
