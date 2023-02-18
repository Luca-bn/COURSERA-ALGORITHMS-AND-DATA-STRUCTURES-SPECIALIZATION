import java.util.Scanner;

/*
    Problem Description
    Task. Given two integers 𝑎 and 𝑏, find their greatest common divisor.

    Input Format. The two integers 𝑎, 𝑏 are given in the same line separated by space.

    Constraints. 1 ≤ 𝑎, 𝑏 ≤ 2 · 10^9.

    Output Format. Output GCD(𝑎, 𝑏).
 */

// Good job! (Max time used: 0.07/1.50, max memory used: 29200384/2147483648.)
public class GCD {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long a = scanner.nextLong();
        long b = scanner.nextLong();
        scanner.close();

        System.out.println(getGreatherCommonDivisor(a, b));
    }

    private static long getGreatherCommonDivisor(long a, long b) {

        long numerator = -1;
        long denominator = -1;
        long remaining = -1;
        if(a > b) {
            numerator = a;
            denominator = b;
        } else {
            numerator = b;
            denominator = a;
        }
        
        while (denominator != 0) {
            remaining = numerator % denominator;
            numerator = denominator;
            denominator = remaining;
        }

        return numerator;
    }

}
