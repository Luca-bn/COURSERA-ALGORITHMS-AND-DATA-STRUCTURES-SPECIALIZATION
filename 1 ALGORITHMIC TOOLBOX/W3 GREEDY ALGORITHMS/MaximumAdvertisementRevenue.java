import java.math.BigDecimal;
import java.util.*;

/*
    Problem Description
    Task. Given two sequences 𝑎1, 𝑎2, . . . , 𝑎𝑛 (𝑎𝑖 is the profit per click of the 𝑖-th ad) and 𝑏1, 𝑏2, . . . , 𝑏𝑛 (𝑏𝑖 is
    the average number of clicks per day of the 𝑖-th slot), we need to partition them into 𝑛 pairs (𝑎𝑖, 𝑏𝑗)
    such that the sum of their products is maximized.

    Input Format. The first line contains an integer 𝑛, the second one contains a sequence of integers
    𝑎1, 𝑎2, . . . , 𝑎𝑛, the third one contains a sequence of integers 𝑏1, 𝑏2, . . . , 𝑏𝑛.

    Constraints. 1 ≤ 𝑛 ≤ 10^3; −10^5 ≤ 𝑎𝑖, 𝑏𝑖 ≤ 10^5 for all 1 ≤ 𝑖 ≤ 𝑛.
    
    Output Format. Output the maximum value of
    Σ︀(from i=1 to 𝑛) 𝑎𝑖*𝑐𝑖, where 𝑐1, 𝑐2, . . . , 𝑐𝑛 is a permutation of 𝑏1, 𝑏2, . . . , 𝑏𝑛.
*/

// Good job! (Max time used: 0.19/1.50, max memory used: 35708928/2147483648.)
public class MaximumAdvertisementRevenue {

    public static void main(String[] args) {

        Scanner reader = new Scanner(System.in);

        StringTokenizer st = new StringTokenizer(reader.nextLine());
        Integer numberOfAds = Integer.valueOf(st.nextToken());

        st = new StringTokenizer(reader.nextLine());
        List<BigDecimal> firstSequence = new ArrayList<>(numberOfAds);
        for (int i = 0; i < numberOfAds; i++) {
            firstSequence.add(new BigDecimal(st.nextToken()));
        }

        st = new StringTokenizer(reader.nextLine());
        List<BigDecimal> secondSequence = new ArrayList<>(numberOfAds);
        for (int i = 0; i < numberOfAds; i++) {
            secondSequence.add(new BigDecimal(st.nextToken()));
        }

        System.out.println(maximumRevenue(numberOfAds, firstSequence, secondSequence));

        reader.close();

    }

    private static BigDecimal maximumRevenue(Integer numberOfAds, List<BigDecimal> firstSequence,
            List<BigDecimal> secondSequence) {

        BigDecimal result = BigDecimal.ZERO;

        firstSequence.sort((o1, o2) -> o2.compareTo(o1));
        secondSequence.sort((o1, o2) -> o2.compareTo(o1));

        for (int i = 0; i < numberOfAds; i++)
            result = result.add(firstSequence.get(i).multiply(secondSequence.get(i)));

        return result;
    }

}
