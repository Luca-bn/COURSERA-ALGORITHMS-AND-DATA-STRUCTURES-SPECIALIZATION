import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
    Problem Description
    Task. Given two integers 𝑛 and 𝑚, outpt 𝐹𝑛 mod 𝑚 (that is, the remainder of 𝐹𝑛 when divided by 𝑚).

    Input Format. The input consists of two integers 𝑛 and 𝑚 given on the same line (separated by a space).

    Constraints. 1 ≤ 𝑛 ≤ 1014, 2 ≤ 𝑚 ≤ 10^3.
    
    Output Format. Output 𝐹𝑛 mod 𝑚.
 */

// Good job! (Max time used: 0.07/1.50, max memory used: 29245440/2147483648.)
public class FibonacciAgain {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long a = scanner.nextLong();
        long b = scanner.nextLong();
        scanner.close();

        System.out.println(getResult(a, b));
    }

    private static Long getResult(Long a, Long b) {

        if (a <= 1)
            return 1 % b;

        List<Long> sequence = new ArrayList<>();
        sequence.add(0l);
        sequence.add(1l);

        for (int i = 2; true; i++) {
            if (i > a) {
                // if I've found the n-fibonacci-value, then I've finished
                return sequence.get(sequence.size() - 1);
            }

            sequence.add((sequence.get(i - 2) + sequence.get(i - 1)) % b);

            if(sequence.get(sequence.size() - 1).equals(1l) && sequence.get(sequence.size() - 2).equals(0l)) {
                // I'm in new period
                int index = Long.valueOf(a % (i - 1)).intValue();
                return sequence.get(index);
            }
        }
    }

}
