import java.util.Arrays;
import java.util.Scanner;

/*
    Problem Description
    Task. The goal in this problem is to count the number of inversions of a given sequence.

    Input Format. The first line contains an integer 𝑛, the next one contains a sequence of integers
    𝑎0, 𝑎1, . . . , 𝑎𝑛−1.

    Constraints. 1 ≤ 𝑛 ≤ 10^5, 1 ≤ 𝑎𝑖 ≤ 10^9 for all 0 ≤ 𝑖 < 𝑛.
    
    Output Format. Output the number of inversions in the sequence.
*/

// Good job! (Max time used: 0.25/1.50, max memory used: 59879424/2147483648.)
public class NumberOfInversions {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int size = scanner.nextInt();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = scanner.nextInt();
        }
        scanner.close();

        int inversions = mergeSortAndCount(array, 0, size - 1);

        // for(int i = 0; i < array.length; i++)
        //     System.out.print(array[i] + " ");
        // System.out.println();
        System.out.println(inversions);
    }

    private static int mergeSortAndCount(int[] array, int low, int high) {

        if (low >= high)
            return 0;

        int inversions = 0;
        int mid = (high + low) / 2;

        inversions += mergeSortAndCount(array, low, mid);
        inversions += mergeSortAndCount(array, mid + 1, high);

        inversions += mergeAndCount(array, low, mid, high);

        return inversions;
    }

    private static int mergeAndCount(int[] array, int low, int mid, int high) {

        // [|=low mid=|=mid+1 high=|]

        int inversions = 0;
        int[] leftArray = Arrays.copyOfRange(array, low, mid + 1);
        int[] rightArray = Arrays.copyOfRange(array, mid + 1, high + 1);

        int arrayIndex = low, leftIndex = 0, rightIndex = 0;

        // [ 1 4 9] [ 3 4 8 ]
        // (1, 3), (1, 4), (1, 8), (4, 3), (4, 4), (4, 8)+, (9, 3)+, (9, 4)+, (9, 8)+
        // aspected result = 4
        
        // 1; 3 => 1 x
        // 4; 3 => 3 v -> have to swap 3 with 9 and 4 (+2)
        // 4; 4 => 4 x
        // 9; 4 => 4 v -> swap 4 with 9 (+1)
        // 9; 8 => 8 v -> swap 8 with 9 (+1)
        while (arrayIndex <= high) {
            if (leftIndex < leftArray.length && rightIndex < rightArray.length) {
                if (leftArray[leftIndex] <= rightArray[rightIndex]) {
                    array[arrayIndex++] = leftArray[leftIndex++];
                } else {
                    array[arrayIndex++] = rightArray[rightIndex++];
                    inversions += (leftArray.length - leftIndex);
                }
            } else if (leftIndex < leftArray.length) {
                array[arrayIndex++] = leftArray[leftIndex++];
            } else if (rightIndex < rightArray.length) {
                array[arrayIndex++] = rightArray[rightIndex++];
            }
        }

        return inversions;
    }

}
