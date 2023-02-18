import java.io.*;
import java.util.*;

/*
    Task. You’re looking into a restaurant menu which shows for each dish the list of ingredients with amounts
    and the estimated total energy value in calories. You would like to find out the energy values of
    individual ingredients (then you will be able to estimate the total energy values of your favorite dishes).
    
    Input Format. The first line of the input contains an integer 𝑛 — the number of dishes in the menu, and
    it happens so that the number of different ingredients is the same. Each of the next 𝑛 lines contains
    description 𝑎1, 𝑎2, . . . , 𝑎𝑛,𝐸 of a single menu item. 𝑎𝑖 is the amount of 𝑖-th ingredient in the dish, and
    𝐸 is the estimated total energy value of the dish. If the ingredient is not used in the dish, the amount
    will be specified as 𝑎𝑖 = 0; beware that although the amount of any ingredient in any real
    menu would be positive, we will test that your algorithm works even for negative amounts
    𝑎𝑖 < 0.
    
    Constraints. 0 ≤ 𝑛 ≤ 20; −1000 ≤ 𝑎𝑖 ≤ 1000.
    
    Output Format. Output 𝑛 real numbers — for each ingredient, what is its energy value. These numbers
    can be non-integer, so output them with at least 3 digits after the decimal point.
    Your output for a particular test input will be accepted if all the numbers in the output are considered
    correct. The amounts and energy values are of course approximate, and the computations in real
    numbers on a computer are not always precise, so each of the numbers in your output will be considered
    correct if either absolute or relative error is less than 10−2. That is, if the correct number is 5.245000,
    and you output 5.235001, your number will be considered correct, but 5.225500 will not be accepted.
    Also, if the correct number is 1001, and you output 1000, your answer will be considered correct,
    because the relative error will be less than 10−2, but if the correct answer is 0.1, and you output 0.05,
    your answer will not be accepted, because in this case both the absolute error (0.05) and the relative
    error (0.5) are more than 10−2. Note that we ask you to output at least 3 digits after the
    decimal point, although we only require precision of 10−2, intentionally: if you output only
    2 digits after the decimal point, your answer can be rejected while being correct because
    of the rounding issues. The easiest way to avoid this mistake is to output at least 3 digits
    after the decimal point.
*/

// Good job! (Max time used: 0.21/1.50, max memory used: 40873984/2147483648.)
class EnergyValues {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();

        double[][] A = new double[n][n];
        double[] b = new double[n];

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                A[i][j] = in.nextDouble();
            }
            b[i] = in.nextDouble();
        }
        in.close();

        double[] solution = solve(A, b);
        for (int i = 0; i < solution.length; i++)
            System.out.print(String.format("%.6f ", solution[i]).replace(",", "."));
    }

    public static double[] solve(double[][] A, double[] B)
    {
        int N = B.length;
        for (int k = 0; k < N; k++) 
        {
            /** find pivot row **/
            int max = k;
            for (int i = k + 1; i < N; i++) 
                if (Math.abs(A[i][k]) > Math.abs(A[max][k])) 
                    max = i;
 
            /** swap row in A matrix **/    
            double[] temp = A[k]; 
            A[k] = A[max]; 
            A[max] = temp;
 
            /** swap corresponding values in constants matrix **/
            double t = B[k]; 
            B[k] = B[max]; 
            B[max] = t;
 
            /** pivot within A and B **/
            for (int i = k + 1; i < N; i++) 
            {
                double factor = A[i][k] / A[k][k];
                B[i] -= factor * B[k];
                for (int j = k; j < N; j++) 
                    A[i][j] -= factor * A[k][j];
            }
        }
 
        /** back substitution **/
        double[] solution = new double[N];
        for (int i = N - 1; i >= 0; i--) 
        {
            double sum = 0.0;
            for (int j = i + 1; j < N; j++) 
                sum += A[i][j] * solution[j];
            solution[i] = (B[i] - sum) / A[i][i];
        }        

        return solution;
    }
}