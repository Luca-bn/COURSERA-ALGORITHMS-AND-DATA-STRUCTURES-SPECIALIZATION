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
public class LongestCommonIncreasingSubsequenceOfTwoSequences {

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

        int[] LICS = new int[secondSequence.length];
        for (int j = 0; j < LICS.length; j++)
            LICS[j] = 0;

        for(int i = 0; i < firstSequence.length; i++) {
            int current = 0;
            for(int j = 0; j < secondSequence.length; j++) {
                if(firstSequence[i].equals(secondSequence[j])) {
                    current = LICS[j] + 1;
                    LICS[j] = current;
                }

                if(firstSequence[i] < secondSequence[j]) {
                    LICS[j] = Math.max(current, LICS[j]);
                }
            }

        }


        int result = 0;
        for(int i : LICS)
            result = i > result ? i : result;

        return result;

    }

}
