import java.util.Scanner;

/*
    Problem Description
    Task. Given two sequences ๐ด = (๐1, ๐2, . . . , ๐๐) and ๐ต = (๐1, ๐2, . . . , ๐๐), find the length of their longest
    common subsequence, i.e., the largest non-negative integer ๐ such that there exist indices 1 โค ๐1 <
    ๐2 < ยท ยท ยท < ๐๐ โค ๐ and 1 โค ๐1 < ๐2 < ยท ยท ยท < ๐๐ โค ๐, such that ๐๐1 = ๐๐1 , . . . , ๐๐๐ = ๐๐๐ .

    Input Format. First line: ๐. Second line: ๐1, ๐2, . . . , ๐๐. Third line: ๐. Fourth line: ๐1, ๐2, . . . , ๐๐.

    Constraints. 1 โค ๐,๐ โค 100; โ10^9 < ๐๐, ๐๐ < 10^9.
    
    Output Format. Output ๐.
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
