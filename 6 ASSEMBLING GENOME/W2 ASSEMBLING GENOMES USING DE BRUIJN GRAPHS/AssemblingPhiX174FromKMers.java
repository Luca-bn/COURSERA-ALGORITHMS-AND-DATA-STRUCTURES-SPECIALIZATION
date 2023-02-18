import java.util.*;
import java.io.*;

/*
    Task. Let the “k-mer composition” of a string Text be defined as the list of every k-mer in Text (in any
    order). For example, the 3-mer composition of the circular string ACGTA is [ACG, CGT, GTA, TAA,
    AAC]. Given the k-mer composition of some unknown string, perform the task of Genome Assembly
    and return the circular genome from which the k-mers came. In other words, return a string whose
    k-mer composition is equal to the given list of k-mers.
    
    Dataset. Each of the 5396 lines of the input contains a single k-mer. The k-mers are given to you in alpha-
    betical order because their true order is hidden from you. Each k-mer is 10 nucleotides long.
    
    Output. Output the assembled genome on a single line.
*/

// Good job! (Max time used: 0.16/4.50, max memory used: 38359040/2147483648.)
public class AssemblingPhiX174FromKMers {
    
    private static final int INPUT_K_MERS = 5396;

    public static void main(String[] args) {
        new Thread(null, new Runnable() {
            public void run() {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
                    Map<String, Vertex> kmersMap = new HashMap<>();
                    int id = 0;
                    for(int i = 0; i < INPUT_K_MERS; i++) {
                        String kmer = in.readLine();
                        String firstKmer = kmer.substring(0, kmer.length() - 1);
                        String secondKmer = kmer.substring(1);

                        Vertex firstVertex = kmersMap.getOrDefault(firstKmer, new Vertex(id++, firstKmer));
                        Vertex secondVertex = kmersMap.getOrDefault(secondKmer, new Vertex(id++, secondKmer));

                        firstVertex.addEdge(secondVertex);

                        kmersMap.put(firstKmer, firstVertex);
                        kmersMap.put(secondKmer, secondVertex);
                    }
                    LinkedList<Vertex> cycle = findEulerianCycle(kmersMap.values().iterator().next());
                    if(cycle.peekFirst().id == cycle.peekLast().id)
                        cycle.removeLast();
                    for(Vertex v : cycle)
                        System.out.print(v.text.charAt(v.text.length() - 1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "1", 1 << 26).start();
    }

    private static LinkedList<Vertex> findEulerianCycle(Vertex startingVertex) {
        LinkedList<Vertex> cycle = new LinkedList<>();
        dfs(startingVertex, cycle);
        return cycle;
    }

    private static void dfs(Vertex v, LinkedList<Vertex> cycle) {
        while(v.outEdgesCount > 0) {
            Vertex next = v.outEdges.get(--v.outEdgesCount);
            dfs(next, cycle);
        }
        cycle.addFirst(v);
    }
    
    static class Vertex {
        int id;
        String text;
        List<Vertex> outEdges = new ArrayList<>();
        int outEdgesCount = 0;

        public Vertex(int id, String text) {
            this.id = id;
            this.text = text;
        }

        public void addEdge(Vertex to) {
            this.outEdgesCount++;
            this.outEdges.add(to);
        }
    }
}
