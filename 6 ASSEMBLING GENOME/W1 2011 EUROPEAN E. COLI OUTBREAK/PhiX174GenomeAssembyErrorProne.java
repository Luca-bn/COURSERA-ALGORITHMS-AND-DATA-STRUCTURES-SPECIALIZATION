import java.util.*;
import java.util.Map.Entry;
import java.io.*;

/*
    Task. Given a list of error-prone reads, perform the task of Genome Assembly and return the circular
    genome from which they came.
    
    Dataset. Each of 1 618 lines of the input contains a single read. The reads are given to you in alphabetical
    order because their true order is hidden from you. Each read is 100 nucleotides long and contains a
    single sequencing error (i.e., one mismatch per read) in order to simulate the 1% error rate of Illumina
    sequencing machines. Note that you are not given the 100-mer composition of the genome (i.e., some
    100-mers may be missing).
    
    Output. Output the assembled genome on a single line.
*/

// Good job! (Max time used: 2.70/4.50, max memory used: 174592000/2147483648.)
public class PhiX174GenomeAssembyErrorProne {

    private static final int NUMBER_OF_READS = 1618;

    public static void main(String[] args) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            String[] reads = new String[NUMBER_OF_READS];
            for (int i = 0; i < NUMBER_OF_READS; i++)
                reads[i] = in.readLine();
            System.out.println(assemblyGenomeFromReads(reads));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String assemblyGenomeFromReads(String[] reads) {
        List<Node> graph = buildGraphFromReads(reads); // creates graph from reads
        removeMismatchesFromGraph(graph); // removes mismatches from nodes in graph
        return buildGenomeFromGraph(graph); // build resulting genome
    }

    private static List<Node> buildGraphFromReads(String[] reads) {
        // resulting graph where each node contains one read and the reference to next
        // and previous one
        List<Node> graph = new ArrayList<>();
        // used to tracking when a read is already used or not
        boolean[] used = new boolean[reads.length];
        used[0] = true;

        // starting loop from first read
        Node lastNode = new Node(0, reads[0]);
        while (graph.size() < reads.length - 1) {
            graph.add(lastNode);
            Overlap maxOverlap = new Overlap();
            int nextReadIndex = -1;
            // for each node searching next one with longest overlap (from not already
            // visited reads)
            for (int i = 0; i < reads.length; i++) {
                if (used[i])
                    continue;
                Overlap overlap = computeOverlap(reads[lastNode.readIndex], reads[i]);
                if (overlap.length > maxOverlap.length) {
                    nextReadIndex = i;
                    maxOverlap = overlap;
                }
            }
            // updating previous node
            used[nextReadIndex] = true;
            lastNode.nextOverlap = maxOverlap;
            lastNode.nextNodeIndex = graph.size();
            // creating next node
            Node nextNode = new Node(nextReadIndex, reads[nextReadIndex]);
            nextNode.prevOverlap = maxOverlap;
            nextNode.prevNodeIndex = graph.size() - 1;
            // updating next node to use
            lastNode = nextNode;
        }
        // attaching last node to first one (genome is circular)
        Overlap overlap = computeOverlap(reads[lastNode.readIndex], reads[0]);
        // updating first node in graph
        Node firstNode = graph.get(0);
        firstNode.prevNodeIndex = graph.size();
        firstNode.prevOverlap = overlap;
        // updating and inserting last node
        lastNode.nextOverlap = overlap;
        lastNode.nextNodeIndex = 0;
        graph.add(lastNode);
        return graph;
    }

    private static Overlap computeOverlap(String r1, String r2) {
        Overlap result = new Overlap();
        for (int i = 0; i < r1.length(); i++) {
            // without variables (using directly result.mismatches list instead) got time
            // limit exceeded
            int mismatchCount = 0;
            int mismatch1 = -1;
            int mismatch2 = -1;
            for (int j = 0; j < r1.length() - i; j++) {
                if (r1.charAt(i + j) != r2.charAt(j)) {
                    mismatchCount++;
                    if (mismatchCount == 1)
                        mismatch1 = j;
                    else if (mismatchCount == 2)
                        mismatch2 = j;
                    else
                        break;
                }
            }
            if (mismatchCount <= 2) {
                result.length = r1.length() - i;
                if (mismatch1 != -1)
                    result.mismatches.add(mismatch1);
                if (mismatch2 != -1)
                    result.mismatches.add(mismatch2);
                return result;
            }
        }
        return result;
    }

    private static void removeMismatchesFromGraph(List<Node> graph) {
        /*
         * trying to remove mismatches by iterating graph 2 times:
         * - first iteration, removes mismatches between node1 and node2 if their adjacent
         *   nodes cover that mismatch
         * 
         * - in second iteration, tries to remove remaining mismatches using most
         *   recurrent char for that mismatch (if possible) or using the value of an
         *   already updated adjacent node
         */
        removeMismatchesFirstIteration(graph);
        removeMismatchesSecondIteration(graph);
        // at this point there could still be some mismatches, but this implementation
        // is enaugh to pass the test
    }

    private static void removeMismatchesFirstIteration(List<Node> graph) {
        // first iteration, checking if can fix mismatches with prev and next nodes
        for (Node n1 : graph) {
            // skipping nodes without mismatches
            if (n1.nextOverlap.mismatches.isEmpty())
                continue;
            // prev -> n1 |--mismatch--| n2 -> next
            // checking if mismatch between n1 and n2 is covered from prev and next
            Node prev = graph.get(n1.prevNodeIndex);
            Node n2 = graph.get(n1.nextNodeIndex);
            Node next = graph.get(n2.nextNodeIndex);

            // using listIterator for deleting while looping
            ListIterator<Integer> mismatchIterator = n1.nextOverlap.mismatches.listIterator();
            while (mismatchIterator.hasNext()) {
                // mismatch is the index of the different character in the 2 reads (0-based on
                // their overlap)
                Integer mismatch = mismatchIterator.next();
                int mismatchIndexInPrevNode = prev.text.length() - n1.prevOverlap.length
                        + (n1.text.length() - n1.nextOverlap.length) + mismatch;
                int mismatchIndexInNextNode = n2.nextOverlap.length - (n2.text.length() - mismatch);

                // first checking, verifying if prev and next covers the error overlap (best
                // case)
                if ((mismatchIndexInPrevNode < prev.text.length() && mismatchIndexInPrevNode >= 0)
                        && (mismatchIndexInNextNode >= 0 && mismatchIndexInNextNode < next.text.length())
                        && prev.text.charAt(mismatchIndexInPrevNode) == next.text.charAt(mismatchIndexInNextNode)) {
                    // prev and next node cover this mismatch and have the same value => update
                    // mismatch
                    n1.setCharAt(n1.text.length() - n1.nextOverlap.length + mismatch,
                            prev.text.charAt(mismatchIndexInPrevNode));
                    n2.setCharAt(mismatch, prev.text.charAt(mismatchIndexInPrevNode));
                    mismatchIterator.remove();
                    // this should also automatically remove mismatches from
                    // n2.prevOverlap.mismatches (because they are same object)
                }
            }
        }
    }

    private static void removeMismatchesSecondIteration(List<Node> graph) {
        // second iteration, trying to fix still existing mismatches by using any of the
        // already fixed nodes
        for (Node n1 : graph) {
            // skipping nodes without mismatches
            if (n1.nextOverlap.mismatches.isEmpty())
                continue;
            // prev -> n1 |--mismatch--| n2 -> next
            // checking if mismatch between n1 and n2 is covered from prev and next
            Node prev = graph.get(n1.prevNodeIndex);
            Node n2 = graph.get(n1.nextNodeIndex);
            Node next = graph.get(n2.nextNodeIndex);

            // using listIterator for deleting while looping
            ListIterator<Integer> mismatchIterator = n1.nextOverlap.mismatches.listIterator();
            while (mismatchIterator.hasNext()) {
                // mismatch is the index of the different character in the 2 reads (0-based on
                // their overlap)
                Integer mismatch = mismatchIterator.next(); // this is also the mismatch index in n2
                int mismatchIndexInN1 = n1.text.length() - n1.nextOverlap.length + mismatch;
                int mismatchIndexInPrevNode = prev.text.length() - n1.prevOverlap.length
                        + mismatchIndexInN1;
                int mismatchIndexInNextNode = n2.nextOverlap.length - (n2.text.length() - mismatch);

                // trying to use most recurrent character in that position
                Map<Character, Integer> charCounts = new HashMap<>();
                charCounts.put('A', 0);
                charCounts.put('C', 0);
                charCounts.put('G', 0);
                charCounts.put('T', 0);
                charCounts.put(n1.text.charAt(mismatchIndexInN1),
                        charCounts.get(n1.text.charAt(mismatchIndexInN1)) + 1);
                charCounts.put(n2.text.charAt(mismatch), charCounts.get(n2.text.charAt(mismatch)) + 1);
                if (mismatchIndexInPrevNode >= 0 && mismatchIndexInPrevNode < prev.text.length())
                    charCounts.put(prev.text.charAt(mismatchIndexInPrevNode),
                            charCounts.get(prev.text.charAt(mismatchIndexInPrevNode)) + 1);
                if (mismatchIndexInNextNode >= 0 && mismatchIndexInNextNode < next.text.length())
                    charCounts.put(next.text.charAt(mismatchIndexInNextNode),
                            charCounts.get(next.text.charAt(mismatchIndexInNextNode)) + 1);

                // checking if there is at least one character with more than one occurrence
                int charCount = 1;
                Character charToUse = null;
                for (Entry<Character, Integer> entry : charCounts.entrySet())
                    if (entry.getValue() > charCount) {
                        charToUse = entry.getKey();
                        break;
                    }

                // if found a character with more than one occurrence, use that and continue
                if (charToUse != null) {
                    n1.setCharAt(mismatchIndexInN1, charToUse);
                    n2.setCharAt(mismatch, charToUse);
                    mismatchIterator.remove();
                    continue;
                }

                // still not found after checking most recurrent character
                // so trying to use the character of one of the other nodes
                // (taking one which doesn't have other errors in next overlap)
                if (n2.nextOverlap.mismatches.isEmpty()) {
                    // if n2 has no error in next patterns using its value to update mismatch
                    n1.setCharAt(mismatchIndexInN1, n2.text.charAt(mismatch));
                    mismatchIterator.remove();
                } else if (prev.nextOverlap.mismatches.isEmpty()
                        && (mismatchIndexInPrevNode < prev.text.length() && mismatchIndexInPrevNode >= 0)) {
                    // else if prev node has not errors in next overlap, using its value to update
                    // mismatch
                    n1.setCharAt(mismatchIndexInN1, prev.text.charAt(mismatchIndexInPrevNode));
                    n2.setCharAt(mismatch, prev.text.charAt(mismatchIndexInPrevNode));
                    mismatchIterator.remove();
                } else if (next.nextOverlap.mismatches.isEmpty()
                        && (mismatchIndexInNextNode >= 0 && mismatchIndexInNextNode < next.text.length())) {
                    // else if next node has not errors in next overlap, using its value to update
                    // mismatch
                    n1.setCharAt(mismatchIndexInN1, next.text.charAt(mismatchIndexInNextNode));
                    n2.setCharAt(mismatch, next.text.charAt(mismatchIndexInNextNode));
                    mismatchIterator.remove();
                }
            }
        }
    }

    private static String buildGenomeFromGraph(List<Node> graph) {
        StringBuilder genome = new StringBuilder();
        for (Node n : graph) {
            // for each node, appending the read without next overlap part
            genome.append(n.text.substring(0, n.text.length() - n.nextOverlap.length));
        }
        return genome.toString();
    }

    static class Node {
        String text; // read
        int readIndex; // index in reads array
        Overlap nextOverlap; // overlap with next node
        Overlap prevOverlap; // overlap with previous node
        int nextNodeIndex; // index of next node in graph
        int prevNodeIndex; // index of previous node in graph

        public Node(int index, String read) {
            this.readIndex = index;
            this.text = read;
        }

        public void setCharAt(int pos, char ch) {
            text = text.substring(0, pos) + ch + text.substring(pos + 1);
        }
    }

    static class Overlap {
        int length; // overlap length
        List<Integer> mismatches = new ArrayList<>(); // indexes of mismatches into the overlap (0-based)
    }

}