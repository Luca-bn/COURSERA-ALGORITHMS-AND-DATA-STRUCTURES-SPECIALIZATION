import java.util.*;

/*
    Problem Description
    Task. The goal of this code problem is to implement an algorithm for the fractional knapsack problem.
    
    Input Format. The first line of the input contains the number π of items and the capacity π of a knapsack.
    The next π lines define the values and weights of the items. The π-th line contains integers π£π and π€πβthe
    value and the weight of π-th item, respectively.

    Constraints. 1 β€ π β€ 10^3, 0 β€ π β€ 2 Β· 10^6; 0 β€ π£π β€ 2 Β· 10^6, 0 < π€π β€ 2 Β· 10^6 for all 1 β€ π β€ π. All the
    numbers are integers.

    Output Format. Output the maximal value of fractions of items that fit into the knapsack. The absolute
    value of the difference between the answer of your program and the optimal value should be at most
    10^β3. To ensure this, output your answer with at least four digits after the decimal point (otherwise
    your answer, while being computed correctly, can turn out to be wrong because of rounding issues).
 */

// Good job! (Max time used: 0.18/1.50, max memory used: 38187008/2684354560.)
public class MaximumValueOfTheLoot {

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        StringTokenizer st = new StringTokenizer(reader.nextLine());

        Integer numberOfItems = Integer.valueOf(st.nextToken());
        Integer maxWeight = Integer.valueOf(st.nextToken());
        List<Item> items = new ArrayList<>();

        for (int i = 0; i < numberOfItems; i++) {
            st = new StringTokenizer(reader.nextLine());
            items.add(new Item(Integer.valueOf(st.nextToken()), Integer.valueOf(st.nextToken())));
        }

        reader.close();

        System.out.printf("%.4f", maximumValueOfTheLoot(maxWeight, items));
    }

    private static double maximumValueOfTheLoot(Integer maxWeight, List<Item> items) {

        double result = 0d;

        // first of all i'm sorting items by price for weight in desc order
        items.sort((o1, o2) -> o2.priceForUnit.compareTo(o1.priceForUnit));

        // now I always take the max weight of biggest ones first
        Integer availableWeightAfterItem = null;
        for (Item item : items) {
            if (maxWeight == 0)
                break;

            availableWeightAfterItem = maxWeight - item.weight;
            if (availableWeightAfterItem >= 0) {
                // there is more space in the bag (or after this item the bag will be full)
                // => so I can take the maximum quantity for this item
                result += item.price;
                maxWeight -= item.weight;
            } else {
                // for this item the quantity is greather then the bag capacity
                // => so I have to take a fraction of it
                result += maxWeight * item.priceForUnit;
                maxWeight = 0;
            }
        }

        return result;
    }

    static class Item {

        Integer weight;
        Integer price;
        Double priceForUnit;

        public Item(Integer price, Integer weight) {
            this.weight = weight;
            this.price = price;
            this.priceForUnit = (0d + price) / weight;
        }

    }

}
