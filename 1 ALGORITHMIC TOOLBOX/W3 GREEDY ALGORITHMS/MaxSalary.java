import java.util.*;

/*
    Problem Description
    Task. Compose the largest number out of a set of integers.

    Input Format. The first line of the input contains an integer 𝑛. The second line contains integers
    𝑎1, 𝑎2, . . . , 𝑎𝑛.

    Constraints. 1 ≤ 𝑛 ≤ 100; 1 ≤ 𝑎𝑖 ≤ 10^3 for all 1 ≤ 𝑖 ≤ 𝑛.
    
    Output Format. Output the largest number that can be composed out of 𝑎1, 𝑎2, . . . , 𝑎𝑛.
*/

// Good job! (Max time used: 0.09/1.50, max memory used: 32247808/2147483648.)
public class MaxSalary {
    
    public static void main(String[] args) {
        
        Scanner reader = new Scanner(System.in);
        StringTokenizer st = new StringTokenizer(reader.nextLine());
        Integer numberOfElements = Integer.valueOf(st.nextToken());
        st = new StringTokenizer(reader.nextLine());
        List<Integer> elements = new ArrayList<>(numberOfElements);
        while(st.hasMoreTokens()) {
            elements.add(Integer.valueOf(st.nextToken()));
        }
        reader.close();

        System.out.println(getMaximumNumber(elements));
    }

    private static String getMaximumNumber(List<Integer> elements) {

        String result = "";

        while(elements.size() > 0) {
            Integer maxDigit = null;
            for(Integer digit : elements) {
                if(maxDigit == null) {
                    maxDigit = digit;
                    continue;
                }
                maxDigit = getMaxValue(maxDigit, digit);
            }
            result += maxDigit;
            elements.remove(maxDigit);
        }

        return result;
    }

    private static Integer getMaxValue(Integer maxDigit, Integer digit) {
        
        Integer firstValue = Integer.valueOf(maxDigit + "" + digit);
        Integer secondValue = Integer.valueOf(digit + "" + maxDigit);

        return firstValue > secondValue ? maxDigit : digit;
    }

}
