import java.util.Scanner;
import java.util.StringTokenizer;

/* 
    Maximum Pairwise Product Problem
    Find the maximum product of two distinct numbers
    in a sequence of non-negative integers.

    Input: A sequence of non-negative
    integers.

    Output: The maximum value that
    can be obtained by multiplying
    two different elements from the sequence 
*/

//Good job! (Max time used: 0.34/1.50, max memory used: 69668864/2147483648.)
public class MaximumPairwiseProduct {
    public static void main(String[] args) {

        // read data
        Long[] array = getInputData();

        // find max product
        Long result = findMaximumPairwiseProduct(array);

        // print output
        System.out.println(result);

    }

    private static Long[] getInputData() {
        Scanner reader = new Scanner(System.in);
        Integer arraySize = reader.nextInt();
        reader.nextLine();
        Long[] array = new Long[arraySize];

        StringTokenizer st = new StringTokenizer(reader.nextLine());
        int index = 0;
        while (st.hasMoreTokens()) {
            array[index++] = Long.parseLong(st.nextToken());
        }

        reader.close();
        return array;
    }

    private static Long findMaximumPairwiseProduct(Long[] array) {
        if (array == null || array.length < 2)
            return -1l;

        // at beginnig first two max are always first two elements in the array
        Long firstMax = array[0];
        Long secondMax = array[1];

        for (int i = 2; i < array.length; i++) {
            Long currentValue = array[i];

            if (currentValue > firstMax) {
                // before replace fistMax, check if it is greather then second one
                // - if it is, then replace second one with first one
                if (firstMax > secondMax)
                    secondMax = firstMax;

                // now i can replace first one with new value
                firstMax = currentValue;
                continue;
            }

            if (currentValue > secondMax)
                secondMax = currentValue;

        }

        return firstMax * secondMax;
    }
}