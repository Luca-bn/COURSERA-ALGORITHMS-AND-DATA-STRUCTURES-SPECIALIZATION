import java.util.Scanner;

/*
    Problem Description
    Task. Given three sequences 𝐴 = (𝑎1, 𝑎2, . . . , 𝑎𝑛), 𝐵 = (𝑏1, 𝑏2, . . . , 𝑏𝑚), and 𝐶 = (𝑐1, 𝑐2, . . . , 𝑐𝑙), find the
    length of their longest common subsequence, i.e., the largest non-negative integer 𝑝 such that there
    exist indices 1 ≤ 𝑖1 < 𝑖2 < · · · < 𝑖𝑝 ≤ 𝑛, 1 ≤ 𝑗1 < 𝑗2 < · · · < 𝑗𝑝 ≤ 𝑚, 1 ≤ 𝑘1 < 𝑘2 < · · · < 𝑘𝑝 ≤ 𝑙 such
    that 𝑎𝑖1 = 𝑏𝑗1 = 𝑐𝑘1 , . . . , 𝑎𝑖𝑝 = 𝑏𝑗𝑝 = 𝑐𝑘𝑝

    Input Format. First line: 𝑛. Second line: 𝑎1, 𝑎2, . . . , 𝑎𝑛. Third line: 𝑚. Fourth line: 𝑏1, 𝑏2, . . . , 𝑏𝑚. Fifth line:
    𝑙. Sixth line: 𝑐1, 𝑐2, . . . , 𝑐𝑙.

    Constraints. 1 ≤ 𝑛, 𝑚, 𝑙 ≤ 100; −10^9 < 𝑎𝑖, 𝑏𝑖, 𝑐𝑖 < 10^9.

    Output Format. Output 𝑝.
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
