import java.util.Scanner;

/* 
    Sum of Two Digits Problem
    Compute the sum of two single digit numbers.
    Input: Two single digit numbers.
    Output: The sum of these numbers
 */
public class AplusB {
    public static void main(String[] args) {
            try (Scanner reader = new Scanner(System.in)) {
                Integer a = reader.nextInt();
                Integer b = reader.nextInt();
                System.out.println(a + b);
            } catch (Exception e) {
                System.err.println("Invalid input parameters");
            }
    }
}