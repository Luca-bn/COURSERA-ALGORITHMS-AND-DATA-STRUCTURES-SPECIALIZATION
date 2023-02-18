import java.util.Scanner;

/*
    Problem Description
    Task. The goal in this problem is to find the minimum number of coins needed to change the input value
    (an integer) into coins with denominations 1, 5, and 10.

    Input Format. The input consists of a single integer 𝑚.

    Constraints. 1 ≤ 𝑚 ≤ (10)^3.
    
    Output Format. Output the minimum number of coins with denominations 1, 5, 10 that changes 𝑚.
*/

// Good job! (Max time used: 0.08/1.50, max memory used: 29200384/2147483648.)
public class MoneyChange {

    private static final Integer[] DENOMINATIONS = { 10, 5, 1 };

    public static void main(String[] args) {

        Scanner reader = new Scanner(System.in);
        Integer money = reader.nextInt();

        System.out.println(minimumNumberOfCoins(money));
        reader.close();

    }

    private static Integer minimumNumberOfCoins(Integer money) {

        Integer coins = 0;
        Integer currentCoinsToUse = -1;

        for (Integer denomination : DENOMINATIONS) {
            if (money == 0)
                break;
            currentCoinsToUse = money / denomination;
            if (currentCoinsToUse > 0) {
                coins += currentCoinsToUse;
                money -= currentCoinsToUse * denomination;
            }
            currentCoinsToUse = -1;
        }

        return coins;
    }
}
