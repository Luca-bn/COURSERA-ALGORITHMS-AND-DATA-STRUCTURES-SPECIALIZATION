import java.util.Scanner;

/* 
    Problem Description
    Task. Given an integer 𝑛, find the 𝑛th Fibonacci number 𝐹𝑛.

    Input Format. The input consists of a single integer 𝑛.

    Constraints. 0 ≤ 𝑛 ≤ 45.
    
    Output Format. Output 𝐹𝑛. 
*/

// Good job! (Max time used: 0.07/1.50, max memory used: 29208576/2147483648.)
public class Fibonacci {
    
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        Integer input = reader.nextInt();

        System.out.println(getFibonacciResult(input));

        reader.close();
    }

    private static Long getFibonacciResult(Integer input) {

        if(input <= 1)
            return Long.valueOf(input);

        Long[] fibonacciSequence = new Long[input + 1];
        fibonacciSequence[0] = 0l;
        fibonacciSequence[1] = 1l;

        for(int i = 2; i <= input; i++) {
            fibonacciSequence[i] = fibonacciSequence[i-1] + fibonacciSequence[i-2];
        }

        return fibonacciSequence[input];
    }

}
