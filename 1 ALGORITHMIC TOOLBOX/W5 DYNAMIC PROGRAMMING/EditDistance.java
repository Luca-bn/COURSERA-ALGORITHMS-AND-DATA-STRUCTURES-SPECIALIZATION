import java.util.Scanner;

/*
    Problem Description
    Task. The goal of this problem is to implement the algorithm for computing the edit distance between two
    strings.

    Input Format. Each of the two lines of the input contains a string consisting of lower case latin letters.

    Constraints. The length of both strings is at least 1 and at most 100.
    
    Output Format. Output the edit distance between the given two strings.
*/

// Good job! (Max time used: 0.07/1.50, max memory used: 29171712/2147483648.)
public class EditDistance {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        char[] firstWord = scanner.nextLine().toCharArray();
        char[] secondWord = scanner.nextLine().toCharArray();
        scanner.close();

        System.out.println(editDistance(firstWord, secondWord));
    }

    private static Integer editDistance(char[] firstWord, char[] secondWord) {

        Integer[][] matrix = new Integer[firstWord.length + 1][secondWord.length + 1];
        matrix[0][0] = 0;
        for(int i = 1; i <= firstWord.length; i ++)
            matrix[i][0] = i;
        for(int i = 1; i <= secondWord.length; i ++)
            matrix[0][i] = i;

        for(int i = 1; i <= firstWord.length; i++) {
            for(int j = 1; j <= secondWord.length; j++) {
                if(firstWord[i - 1] == secondWord[j - 1]) {
                    matrix[i][j] = matrix[i - 1][j - 1];
                } else {
                    int insert = matrix[i][j - 1] + 1;
                    int delete = matrix[i - 1][j] + 1;
                    int mismatch = matrix[i - 1][j - 1] + 1;
                    matrix[i][j] = Math.min(Math.min(insert, delete), mismatch);
                }
            }
        }

        return matrix[firstWord.length][secondWord.length];
    }

}
