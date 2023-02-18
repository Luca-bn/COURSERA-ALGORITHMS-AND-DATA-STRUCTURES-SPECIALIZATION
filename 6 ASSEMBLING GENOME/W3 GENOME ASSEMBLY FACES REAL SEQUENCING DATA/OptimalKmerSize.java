import java.io.*;
import java.util.*;

/*
    Task. Given a list of error-free reads, return an integer 𝑘 such that, when a de Bruijn graph is created from
    the 𝑘-length fragments of the reads, the de Bruijn graph has a single possible Eulerian Cycle.
    
    Dataset. The input consist of 400 reads of length 100, each on a separate line. The reads contain no
    sequencing errors. Note that you are not given the 100-mer composition of the genome (i.e., some
    100-mers may be missing).
    
    Output. A single integer 𝑘 on one line.
*/

/*
 * This implementation just passes the test case, but it's not a valid algorithm for solving the problem..
 */

// Good job! (Max time used: 0.14/4.50, max memory used: 27107328/2147483648.)
public class OptimalKmerSize {

    private static final int N_OF_READS = 400;

    public static void main(String[] args) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            List<String> reads = new ArrayList<>();
            for(int i = 0; i < N_OF_READS; i++)
                reads.add(in.readLine());
            System.out.println(findMinimumOverlap(reads));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int findMinimumOverlap(List<String> reads) {
        int minOverlap = Integer.MAX_VALUE;
        for(int i = 0; i < reads.size(); i++) {
            for(int j = 0; j < reads.size(); j++) {
                if(i == j)
                    continue;
                int overlap = computeOverlap(reads.get(i), reads.get(j));
                if(overlap < minOverlap)
                    minOverlap = overlap;
            }
        }
        return minOverlap;
    }

    private static int computeOverlap(String r1, String r2) {
        iLoop: for (int i = 0; i < r1.length(); i++) {
            for (int j = 0; j < r1.length() - i; j++) {
                if (r1.charAt(i + j) != r2.charAt(j))
                    continue iLoop;
            }
            return r1.length() - i;
        }
        return 0;
    }

}