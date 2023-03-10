import java.util.Scanner;

/*
    Problem Description
    Task. Given three sequences ๐ด = (๐1, ๐2, . . . , ๐๐), ๐ต = (๐1, ๐2, . . . , ๐๐), and ๐ถ = (๐1, ๐2, . . . , ๐๐), find the
    length of their longest common subsequence, i.e., the largest non-negative integer ๐ such that there
    exist indices 1 โค ๐1 < ๐2 < ยท ยท ยท < ๐๐ โค ๐, 1 โค ๐1 < ๐2 < ยท ยท ยท < ๐๐ โค ๐, 1 โค ๐1 < ๐2 < ยท ยท ยท < ๐๐ โค ๐ such
    that ๐๐1 = ๐๐1 = ๐๐1 , . . . , ๐๐๐ = ๐๐๐ = ๐๐๐

    Input Format. First line: ๐. Second line: ๐1, ๐2, . . . , ๐๐. Third line: ๐. Fourth line: ๐1, ๐2, . . . , ๐๐. Fifth line:
    ๐. Sixth line: ๐1, ๐2, . . . , ๐๐.

    Constraints. 1 โค ๐, ๐, ๐ โค 100; โ10^9 < ๐๐, ๐๐, ๐๐ < 10^9.

    Output Format. Output ๐.
*/

// Good job! (Max time used: 0.19/1.50, max memory used: 91963392/2147483648.)
public class LongestCommonSubsequenceOfThreeSequences {

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
        n = scanner.nextInt();
        Integer[] thirdSequence = new Integer[n];
        for (int i = 0; i < n; i++)
            thirdSequence[i] = scanner.nextInt();
        scanner.close();

        System.out.println(getLongestCommonSubsequence(firstSequence, secondSequence, thirdSequence));
    }

    private static Integer getLongestCommonSubsequence(Integer[] firstSequence, Integer[] secondSequence,
            Integer[] thirdSequence) {

        int[][][] matrix = new int[firstSequence.length + 1][secondSequence.length + 1][thirdSequence.length + 1];
        for (int i = 0; i <= firstSequence.length; i++)
            matrix[i][0][0] = 0;
        for (int i = 0; i <= secondSequence.length; i++)
            matrix[0][i][0] = 0;
        for (int i = 0; i <= thirdSequence.length; i++)
            matrix[0][0][i] = 0;

        for (int i = 1; i <= firstSequence.length; i++) {

            for (int j = 1; j <= secondSequence.length; j++) {

                for (int x = 1; x <= thirdSequence.length; x++) {

                    if (firstSequence[i - 1].equals(secondSequence[j - 1])
                            && firstSequence[i - 1].equals(thirdSequence[x - 1])) {
                        matrix[i][j][x] = matrix[i - 1][j - 1][x - 1] + 1;
                    } else {
                        // probably here I should check only for some of this values
                        matrix[i][j][x] = max(matrix[i - 1][j][x], matrix[i][j - 1][x], matrix[i][j][x - 1],
                                matrix[i - 1][j - 1][x], matrix[i - 1][j][x - 1], matrix[i][j - 1][x - 1]);
                    }
                }
            }

        }

        return matrix[firstSequence.length][secondSequence.length][thirdSequence.length];

    }

    private static int max(int... n) {

        Integer max = Integer.MIN_VALUE;
        for (int i : n)
            if (i > max)
                max = i;
        return max;
    }
}
