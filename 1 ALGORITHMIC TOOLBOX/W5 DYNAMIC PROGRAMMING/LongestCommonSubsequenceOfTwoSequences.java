import java.util.Scanner;

/*
    Problem Description
    Task. Given two sequences 𝐴 = (𝑎1, 𝑎2, . . . , 𝑎𝑛) and 𝐵 = (𝑏1, 𝑏2, . . . , 𝑏𝑚), find the length of their longest
    common subsequence, i.e., the largest non-negative integer 𝑝 such that there exist indices 1 ≤ 𝑖1 <
    𝑖2 < · · · < 𝑖𝑝 ≤ 𝑛 and 1 ≤ 𝑗1 < 𝑗2 < · · · < 𝑗𝑝 ≤ 𝑚, such that 𝑎𝑖1 = 𝑏𝑗1 , . . . , 𝑎𝑖𝑝 = 𝑏𝑗𝑝 .

    Input Format. First line: 𝑛. Second line: 𝑎1, 𝑎2, . . . , 𝑎𝑛. Third line: 𝑚. Fourth line: 𝑏1, 𝑏2, . . . , 𝑏𝑚.

    Constraints. 1 ≤ 𝑛,𝑚 ≤ 100; −10^9 < 𝑎𝑖, 𝑏𝑖 < 10^9.
    
    Output Format. Output 𝑝.
*/

// Good job! (Max time used: 0.08/1.50, max memory used: 29388800/2147483648.)
public class LongestCommonSubsequenceOfTwoSequences {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Integer n = scanner.nextInt();
        Integer[] firstSequence = new Integer[n];
        for (int i = 0; i < n; i++)
            firstSequence[i] = scanner.nextInt();
        n = scanner.nextInt();
        Integer[] secondSequence = new Integer[n];
        for (int i = 0; i < n; i++)
            secondSequence[i] = scanner.nextInt();
        scanner.close();

        System.out.println(getLongestCommonSubsequence(firstSequence, secondSequence));
    }

    private static Integer getLongestCommonSubsequence(Integer[] firstSequence, Integer[] secondSequence) {

        int[][] matrix = new int[firstSequence.length + 1][secondSequence.length + 1];
        for (int j = 0; j < firstSequence.length; j++)
            matrix[j][0] = 0;
        for (int j = 0; j < secondSequence.length; j++)
            matrix[0][j] = 0;

        for(int i = 1; i <= firstSequence.length; i++) {

            for(int j = 1; j <= secondSequence.length; j++) {
                if(firstSequence[i - 1].equals(secondSequence[j - 1])) {
                    matrix[i][j] = matrix[i - 1][j - 1] + 1;
                } else {
                    matrix[i][j] = Math.max(matrix[i - 1][j], matrix[i][j - 1]);
                }
            }

        }

        return matrix[firstSequence.length][secondSequence.length];

    }
    
}
