import java.util.*;
import java.io.*;

/*
    Task. Given a list of error-free reads, perform the task of Genome Assembly and return the circular genome
    from which they came.
    
    Dataset. Each of 1 618 lines of the input contains a single read, that is, a string over {A, C, G, T}. The
    reads are given to you in alphabetical order because their true order is hidden from you. Each read
    is 100 nucleotides long and contains no sequencing errors. Note that you are not given the 100-mer
    composition of the genome, i.e., some 100-mers may be missing.
    
    Output. Output the assembled genome on a single line.
*/

// Good job! (Max time used: 3.52/4.50, max memory used: 390438912/2147483648.)
public class PhiX174GenomeAssembyErrorFree {

    public static void main(String[] args) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            List<String> reads = new ArrayList<>();
            for(int i = 0; i < 1618; i++) {
                reads.add(in.readLine());
            }
            System.out.println(buildGenomeFromReads(reads));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String buildGenomeFromReads(List<String> reads) {

        StringBuilder genomeBuilder = new StringBuilder();
        String firstPattern = reads.remove(0);
        genomeBuilder.append(firstPattern);
        while(!reads.isEmpty()) {
            int nextPatternIndex = -1;
            int matchLength = -1;
            String nextPattern = null;
            for(int i = 0; i < reads.size(); i++) {
                String read = reads.get(i);
                for(int j = 0; j < firstPattern.length(); j++) {
                    if(read.startsWith(firstPattern.substring(j))) {
                        int overlapLength = firstPattern.length() - j;
                        if(overlapLength > matchLength) {
                            nextPattern = read;
                            matchLength = overlapLength;
                            nextPatternIndex = i;
                        }
                        break;
                    }
                }
            }
            genomeBuilder.append(nextPattern.substring(matchLength));
            reads.remove(nextPatternIndex);
            firstPattern = nextPattern;
        }
        String genome = genomeBuilder.toString();
        for(int i = 0; i < firstPattern.length(); i++) {
            if(genome.startsWith(firstPattern.substring(i))) {
                int commonChars = firstPattern.length() - i;
                genome = genome.substring(0, genome.length() - commonChars);
                break;
            }
        }
        return genome;
    }
}