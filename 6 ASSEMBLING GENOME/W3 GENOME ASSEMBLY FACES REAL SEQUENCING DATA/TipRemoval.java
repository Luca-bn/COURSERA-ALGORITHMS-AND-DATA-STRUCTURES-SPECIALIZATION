import java.util.*;
import java.util.stream.Collectors;
import java.io.*;

/*
    Task. Given a list of error-prone reads, construct a de Bruijn graph from the 15-mers created from the reads
    and perform the task of tip removal on this de Bruijn graph.
    
    Dataset. The input consist of 400 reads of length 100, each on a separate line. The reads are given to you in
    alphabetical order because their true order is hidden from you. Each read is 100 nucleotides long and
    contains a single sequencing error (i.e., one mismatch per read) in order to simulate the 1% error rate
    of Illumina sequencing machines. Note that you are not given the 100-mer composition of the genome
    (i.e., some 100-mers may be missing).
    
    Output. A single integer (the number of edges removed during the Tip Removal process) on one line.
*/

// Good job! (Max time used: 0.55/30.00, max memory used: 158912512/2147483648.)
public class TipRemoval {

    private static final int N_OF_READS = 1618;
    private static final int K = 15;

    private static int vertexId = 0;
    private static final Map<Integer, Vertex> VERTEXES_MAP_R = new HashMap<>();
    private static final Map<String, Vertex> VERTEXES_MAP = new HashMap<>();
    private static final Map<Integer, List<Vertex>> STRONGLY_CONNECTED_COMPONENTS = new HashMap<>();

    public static void main(String[] args) {
        new Thread(null, new Runnable() {
            public void run() {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {

                    // READING INPUT
                    for (int i = 0; i < N_OF_READS; i++)
                        breakReadIntoKmers(in.readLine());

                    // FINDING STRONGLY CONNECTED COMPONENTS
                    findStronglyConnectedComponents();

                    // PRINTING THE SUM OF ALL STRONGLY CONNECTED COMPONENTS WITH ONLY ONE VERTEX
                    System.out.println(STRONGLY_CONNECTED_COMPONENTS.values().stream().filter(vs -> vs.size() == 1)
                            .mapToInt(vs -> vs.size()).sum());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "1", 1 << 26).start();
    }

    private static void breakReadIntoKmers(String read) {
        for (int i = 0; i <= read.length() - K; i++) {
            // kmer of size K
            String kmer = read.substring(i, i + K);

            // splitting kmer in 2 substring of size K-1
            String firstKey = kmer.substring(0, K - 1);
            String secondKey = kmer.substring(1);

            // creating edges between first k-1 mer and second k-1 mer
            Vertex v1 = VERTEXES_MAP.get(firstKey);
            if (v1 == null) {
                v1 = new Vertex(vertexId++, firstKey);
                VERTEXES_MAP.put(firstKey, v1);
            }
            Vertex v2 = VERTEXES_MAP.get(secondKey);
            if (v2 == null) {
                v2 = new Vertex(vertexId++, secondKey);
                VERTEXES_MAP.put(secondKey, v2);
            }
            v1.addEdge(v2);

            // create edge in reverse graph
            Vertex v1R = VERTEXES_MAP_R.get(v1.id);
            if (v1R == null) {
                v1R = new Vertex(v1.id, firstKey);
                VERTEXES_MAP_R.put(v1.id, v1R);
            }
            Vertex v2R = VERTEXES_MAP_R.get(v2.id);
            if (v2R == null) {
                v2R = new Vertex(v2.id, secondKey);
                VERTEXES_MAP_R.put(v2.id, v2R);
            }
            v2R.addEdge(v1R);
        }
    }

    private static void findStronglyConnectedComponents() {
        // making graph and reversed graph 2 lists sorted by vertex id
        List<Vertex> graph = VERTEXES_MAP.values().stream().sorted((v1, v2) -> Integer.compare(v1.id, v2.id))
                .collect(Collectors.toList());
        List<Vertex> graphR = VERTEXES_MAP_R.values().stream().sorted((v1, v2) -> Integer.compare(v1.id, v2.id))
                .collect(Collectors.toList());

        // first step, finding traversal order in graph
        boolean[] used = new boolean[graph.size()];
        LinkedList<Vertex> order = new LinkedList<>();
        dfsForTraversalOrder(graph.get(0), used, order);
        // ensure to check each node
        for (int i = 0; i < used.length; i++)
            if (!used[i])
                dfsForTraversalOrder(graph.get(i), used, order);

        // second step, finding scc in reversed graph
        int currentSsc = 0;
        used = new boolean[graphR.size()];
        while (!order.isEmpty()) {
            Vertex next = graphR.get(order.pollLast().id);
            if (used[next.id])
                continue;
            dfsForStronglyConnectedComponents(next, used, currentSsc++);
        }
    }

    // DFS for traversal order
    private static void dfsForTraversalOrder(Vertex vertex, boolean[] used, LinkedList<Vertex> order) {
        used[vertex.id] = true;
        for (Vertex v : vertex.outEdges) {
            if (!used[v.id])
                dfsForTraversalOrder(v, used, order);
        }
        order.add(vertex);
    }

    // DFS for strongly connected components
    private static void dfsForStronglyConnectedComponents(Vertex vertex, boolean[] used, int currentSsc) {
        used[vertex.id] = true;
        for (Vertex next : vertex.outEdges) {
            if (!used[next.id])
                dfsForStronglyConnectedComponents(next, used, currentSsc);
        }
        if (STRONGLY_CONNECTED_COMPONENTS.get(currentSsc) == null)
            STRONGLY_CONNECTED_COMPONENTS.put(currentSsc, new ArrayList<>());
        STRONGLY_CONNECTED_COMPONENTS.get(currentSsc).add(vertex);
    }

    static class Vertex {
        final int id;
        final String text;
        int selfConnectedEdges = 0;
        int outCount = 0;
        int inCount = 0;
        List<Vertex> outEdges = new ArrayList<>();

        public Vertex(int id, String text) {
            this.id = id;
            this.text = text;
        }

        public void addEdge(Vertex v) {
            if (id == v.id) {
                selfConnectedEdges++;
                return;
            }
            if (outEdges.stream().filter(w -> w.id == v.id).findFirst().isPresent())
                return; // avoiding to add already existing edge

            outEdges.add(v);
            outCount++;
            v.inCount++;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + id;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Vertex other = (Vertex) obj;
            if (id != other.id)
                return false;
            return true;
        }
    }
}
