import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/*
    Problem Description
    Task. Given an integer 𝑛, compute the minimum number of operations needed to obtain the number 𝑛
    starting from the number 1.

    Input Format. The input consists of a single integer 1 ≤ 𝑛 ≤ 10^6.
    
    Output Format. In the first line, output the minimum number 𝑘 of operations needed to get 𝑛 from 1.
    In the second line output a sequence of intermediate numbers. That is, the second line should contain
    positive integers 𝑎0, 𝑎2, . . . , 𝑎𝑘−1 such that 𝑎0 = 1, 𝑎𝑘−1 = 𝑛 and for all 0 ≤ 𝑖 < 𝑘 − 1, 𝑎𝑖+1 is equal to
    either 𝑎𝑖 + 1, 2𝑎𝑖, or 3𝑎𝑖. If there are many such sequences, output any one of them.
*/

// Good job! (Max time used: 0.08/2.25, max memory used: 29724672/2147483648.)
public class PrimitiveCalculator {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Integer n = scanner.nextInt();
        List<Integer> operations = findBestPath(n);
        System.out.println(operations.size() - 1);
        for(int i : operations)
            System.out.print(i + " ");
        scanner.close();

    }

    private static List<Integer> findBestPath(int n) {
        List<Integer> sequence = new ArrayList<>();

        int[] arr = new int[n + 1];
    
        for (int i = 1; i < arr.length; i++) {
            arr[i] = arr[i - 1] + 1;
            if (i % 2 == 0) arr[i] = Math.min(1 + arr[i / 2], arr[i]);
            if (i % 3 == 0) arr[i] = Math.min(1 + arr[i / 3], arr[i]);
    
        }
    
        for (int i = n; i > 1; ) {
            sequence.add(i);
            if (arr[i - 1] == arr[i] - 1)
                i = i - 1;
            else if (i % 2 == 0 && (arr[i / 2] == arr[i] - 1))
                i = i / 2;
            else if (i % 3 == 0 && (arr[i / 3] == arr[i] - 1))
                i = i / 3;
        }
        sequence.add(1);
    
        Collections.reverse(sequence);
        return sequence;
    }

}
