import java.io.*;
import java.util.*;

/*
    Problem Description
    Task. To force the given implementation of the quick sort algorithm to efficiently process sequences with
    few unique elements, your goal is replace a 2-way partition with a 3-way partition. That is, your new
    partition procedure should partition the array into three parts: < 𝑥 part, = 𝑥 part, and > 𝑥 part.

    Input Format. The first line of the input contains an integer 𝑛. The next line contains a sequence of 𝑛
    integers 𝑎0, 𝑎1, . . . , 𝑎𝑛−1.

    Constraints. 1 ≤ 𝑛 ≤ 10^5; 1 ≤ 𝑎𝑖 ≤ 10^9 for all 0 ≤ 𝑖 < 𝑛.
    
    Output Format. Output this sequence sorted in non-decreasing order.
*/

// Good job! (Max time used: 0.32/1.50, max memory used: 46809088/2147483648.)
public class Randomized3PartitionsQuickSort {
    private static Random random = new Random();

    public static void main(String[] args) {
        FastScanner scanner = new FastScanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        randomizedQuickSort(a, 0, n - 1);
        for (int i = 0; i < n; i++) {
            System.out.print(a[i] + " ");
        }
    }

    private static void randomizedQuickSort(int[] toSort, int left, int right) {
        
        // if the first and last index are the same, the array is already sorted (because it has 1 element)
        if (left >= right)
            return;

        // find random index for pivot element (including first and last index)
        int randomIndex = random.nextInt(right - left + 1) + left;
        // move element at randomIndex to first position (have to use the first element as pivot)
        swap(toSort, left, randomIndex);

        // order the array in this way: 
        // [|<-left   < pivot   |<-m1   = pivot    m2->|    > pivot   right->|]
        // then return m1 and m2 as first and last index of second partition
        int[] m = partition3(toSort, left, right);

        // do the same process for first and third partition
        randomizedQuickSort(toSort, left, m[0] - 1);
        randomizedQuickSort(toSort, m[1] + 1, right);
    }

    private static int[] partition3(int[] toSort, int left, int right) {

        int pivot = toSort[left]; // select first elemenet as pivot
        int lessThen = left; // index of last element in first partition
        int greatherThan = right; // index of first element in third partition
        int index = left + 1; // index to check (skip first one because it is the pivot element)

        while (index <= greatherThan) {
            if (toSort[index] < pivot) {
                // if current one is < then pivot, move the element to the front
                // increment last index of first partition;
                // increment index to check;
                swap(toSort, lessThen, index);
                lessThen++;
                index++;
            } else if (toSort[index] > pivot) {
                // if current one is > then pivot, move it to the end
                // decrese last index of third partition
                // NB: don't increment index, so after swap, check again new value at current index
                swap(toSort, index, greatherThan);
                greatherThan--;
            } else {
                // if current element is = to pivot, just check next element
                // NB: don't need to swap, because already swap in other cases
                index++;
            }
        }

        // at this point, lessThen and greatherThan are no more the indexes of last element in
        // first partition and first element in last partition (because after last swaps 
        // they have been incremented / decremented by 1)
        // so at this point, lt and gt represent the index of first and last elements in partition 2:
        // [   partition1(< pivot)  |<- lt   partition2(= pivot)   gt->|   partition3(> pivot)   ]
        int[] partition2Indexes = { lessThen, greatherThan };
        return partition2Indexes;
    }

    private static void swap(int[] array, int left, int right) {
        int temp = array[left];
        array[left] = array[right];
        array[right] = temp;
    }

    static class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        FastScanner(InputStream stream) {
            try {
                br = new BufferedReader(new InputStreamReader(stream));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }
}
