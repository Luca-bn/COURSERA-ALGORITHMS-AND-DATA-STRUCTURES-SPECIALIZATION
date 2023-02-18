import java.util.Scanner;

/*
    Problem Description
    Task. Compute the last digit of (𝐹0)^2 + (𝐹1)^2 + · · · + (𝐹𝑛)^2.

    Input Format. Integer 𝑛.

    Constraints. 0 ≤ 𝑛 ≤ 1014.
    
    Output Format. The last digit of (𝐹0)^2 + (𝐹1)^2 + · · · + (𝐹𝑛)^2.
*/

// Good job! (Max time used: 0.07/1.50, max memory used: 29143040/2147483648.)
public class LastDigitOfFibonacciPow {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long input = scanner.nextLong();
        scanner.close();

        System.out.println(getResult(input));
    }

    private static Long getResult(Long input) {

        if (input < 3)
            return input;

        long firstToFind = input % 60l;
        long secondToFind = (input + 1) % 60l;

        Long[] sequence = new Long[3];
        Long firstVal = null;
        Long secondVal = null;

        for (int i = 0; i <= 60; i++) {

            if (firstVal != null && secondVal != null)
                break;

            if (i == 0) {
                sequence[0] = 0l;
                sequence[2] = 0l;
            }
            if (i == 1) {
                sequence[1] = 1l;
                sequence[2] = 1l;
            }
            if (i == 2) {
                sequence[0] = 0l;
                sequence[1] = 1l;
                sequence[2] = 1l;
            }

            if (i >= 3) {
                sequence[0] = sequence[1];
                sequence[1] = sequence[2];
                sequence[2] = (sequence[0] + sequence[1]) % 10;
            }

            if (i == firstToFind) {
                firstVal = sequence[2];
            }

            if (i == secondToFind) {
                secondVal = sequence[2];
            }
        }

        return (firstVal * secondVal) % 10;
    }

}
