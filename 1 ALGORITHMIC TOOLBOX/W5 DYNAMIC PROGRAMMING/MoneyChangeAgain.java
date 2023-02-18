import java.util.*;

/*
    Problem Description

    Input Format. Integer money.

    Output Format. The minimum number of coins with denominations 1, 3, 4 that changes money.
    
    Constraints. 1 ≤ money ≤ 10^3.
*/

// Good job! (Max time used: 0.07/1.50, max memory used: 29184000/2147483648.)
public class MoneyChangeAgain {

    private static final Integer[] COINS = { 1, 3, 4 };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Integer moneyToChange = scanner.nextInt();
        scanner.close();

        System.out.println(changeMoney(moneyToChange));
    }

    private static Integer changeMoney(Integer moneyToChange) {

        int[] memo = new int[moneyToChange < 5 ? 5 : moneyToChange + 1];
        memo[0] = 0;
        memo[1] = 1;
        memo[2] = 2;
        memo[3] = 1;
        memo[4] = 1;

        for(int i = 5; i <= moneyToChange; i++) {
            int minVal = Integer.MAX_VALUE;
            for(int coin : COINS) {
                minVal = Math.min(minVal, (memo[i - coin] + 1));
            }
            memo[i] = minVal;
        }

        return memo[moneyToChange];
    }

}