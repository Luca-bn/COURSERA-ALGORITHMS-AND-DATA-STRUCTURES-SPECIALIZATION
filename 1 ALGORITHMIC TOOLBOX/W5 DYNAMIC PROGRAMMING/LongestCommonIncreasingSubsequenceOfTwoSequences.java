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
