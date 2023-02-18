import java.util.Scanner;

/*
    Problem Description
    Task. Given two integers 𝑎 and 𝑏, find their least common multiple.

    Input Format. The two integers 𝑎 and 𝑏 are given in the same line separated by space.

    Constraints. 1 ≤ 𝑎, 𝑏 ≤ 10^7.

    Output Format. Output the least common multiple of 𝑎 and 𝑏.
 */

// Good job! (Max time used: 0.07/1.50, max memory used: 29192192/2147483648.)
public class LCM {

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

        return (a * b) / numerator;
    }

}
