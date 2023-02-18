import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/*
    Problem Description
    Task. The goal of this problem is to represent a given positive integer 𝑛 as a sum of as many pairwise
    distinct positive integers as possible. That is, to find the maximum 𝑘 such that 𝑛 can be written as
    𝑎1 + 𝑎2 + · · · + 𝑎𝑘 where 𝑎1, . . . , 𝑎𝑘 are positive integers and 𝑎𝑖 ̸= 𝑎𝑗 for all 1 ≤ 𝑖 < 𝑗 ≤ 𝑘.

    Input Format. The input consists of a single integer 𝑛.

    Constraints. 1 ≤ 𝑛 ≤ 10^9.
    
    Output Format. In the first line, output the maximum number 𝑘 such that 𝑛 can be represented as a sum
    of 𝑘 pairwise distinct positive integers. In the second line, output 𝑘 pairwise distinct positive integers
    that sum up to 𝑛 (if there are many such representations, output any of them).
*/

// Good job! (Max time used: 0.20/1.50, max memory used: 42602496/2147483648.)
public class MaximumNumberOfPrizes {
    
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        Long quantity = reader.nextLong();
        reader.close();

        List<Long> summands = findSummands(quantity);
        System.out.println(summands.size());
        System.out.println(summands.stream().map(l -> String.valueOf(l)).collect(Collectors.joining(" ")));
    }

    private static List<Long> findSummands(Long quantity) {

        if(quantity <= 2) 
            return Arrays.asList(quantity);

        List<Long> summands = new ArrayList<>();
        Long reminingQuantity = quantity;
        for(int i = 1; i < quantity; i++) {
            // I can add this value, only if after it, I can add another value bigger than this
            if(reminingQuantity - i <= i) { 
                summands.add(reminingQuantity);
                break;
            }

            summands.add(Long.valueOf(i));
            reminingQuantity -= i;
        }
        
        return summands;
    }
    
}
