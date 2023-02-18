import java.io.*;
import java.util.*;

/*
    Task. Given a list of error-prone reads and two integers, 𝑘 and 𝑡, construct a de Bruijn graph from the
    𝑘-mers created from the reads and perform the task of bubble detection on this de Bruijn graph with
    a path length threshold of 𝑡.
    
    Dataset. The first line of the input contains two integers, 𝑘 and 𝑡, separated by a single space. Each
    subsequent line of the input contains a single read. The reads are given to you in alphabetical order
    because their true order is hidden from you. Each read is 100 nucleotides long and contains a single
    sequencing error (i.e., one mismatch per read) in order to simulate the 1% error rate of Illumina
    sequencing machines. Note that you are not given the 100-mer composition of the genome (i.e., some
    100-mers may be missing).
    
    Output. A single integer (the number of (𝑣,𝑤)-bubbles) on one line.
*/

// Good job! (Max time used: 1.83/30.00, max memory used: 351256576/2147483648.)
public class BubbleDetection {
    
    private static final int N_OF_READS = 1618;

    private static int K; // K-MER size
    private static int T; // bubble length threshold

    private static int vertexId = 0; // id of next vertex to create
    private static final Map<String, Vertex> VERTEXES_MAP = new HashMap<>(); // map used to store all vertexes
    // list containing each vertex with more than one outgoing edge
    private static final List<Vertex> VERTEXES_WITH_MORE_THAN_ONE_OUTGOING_EDGES = new ArrayList<>();
    // list containing ids of vertexes with more than one incoming edge
    private static final List<Integer> VERTEXES_WITH_MORE_THAN_ONE_INCOMING_EDGES = new ArrayList<>();
    // map that stores paths from vertexes with more than one outgoing edges and
    // vertexes with more than one incoming edges (with length <= T)
    private static final Map<PathKey, List<LinkedList<Integer>>> PATHS_MAP = new HashMap<>();

    public static void main(String[] args) {
        new Thread(null, new Runnable() {
            public void run() {
                try (Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)))) {
                    K = in.nextInt();
                    T = in.nextInt();

                    for(int i = 0; i < N_OF_READS; i++)
                        breakReadIntoKmers(in.next());

                    for (Vertex v : VERTEXES_WITH_MORE_THAN_ONE_OUTGOING_EDGES)
                        dfs(v, 0, new LinkedList<>());

                    int bubbles = 0;
                    for(List<LinkedList<Integer>> paths : PATHS_MAP.values()) {
                        if(paths.size() < 2)
                            continue;
                        bubbles += countDisjoinedPaths(paths);
                    }
                    System.out.println(bubbles);
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
            if(v1 == null) {
                v1 = new Vertex(vertexId++, firstKey);
                VERTEXES_MAP.put(firstKey, v1);
            }
            Vertex v2 = VERTEXES_MAP.get(secondKey);
            if(v2 == null) {
                v2 = new Vertex(vertexId++, secondKey);
                VERTEXES_MAP.put(secondKey, v2);
            }
            v1.addEdge(v2);
        }
    }

    private static void dfs(Vertex v, int depth, LinkedList<Integer> path) {
        if (depth > T)
            return;

        path.add(v.id);
        if (v.id != path.peekFirst() && VERTEXES_WITH_MORE_THAN_ONE_INCOMING_EDGES.contains(v.id)) {
            PathKey key = new PathKey(path.peekFirst(), v.id);
            if (PATHS_MAP.get(key) == null)
                PATHS_MAP.put(key, new ArrayList<>());
            PATHS_MAP.get(key).add(new LinkedList<>(path));
        }

        for (Vertex w : v.outEdges) {
            dfs(w, depth + 1, new LinkedList<>(path));
        }
    }

    private static int countDisjoinedPaths(List<LinkedList<Integer>> paths) {
        int disjoinedPaths = 0;

        ListIterator<LinkedList<Integer>> pathsIterator = paths.listIterator();
        while(pathsIterator.hasNext()) {
            // getting next path to compare
            LinkedList<Integer> currentPath = pathsIterator.next();
            // removing it from list to avoid compare it to itself (and avoit to compare it again with other paths)
            pathsIterator.remove();
            // for each remaining path, checking if they are disjoined or not
            for(LinkedList<Integer> nextPath : paths) {
                if(arePathDisjoined(currentPath, nextPath))
                    disjoinedPaths++;
            }
        }
        
        return disjoinedPaths;
    }

    private static boolean arePathDisjoined(LinkedList<Integer> currentPath, LinkedList<Integer> nextPath) {
        int sharedVertexes = 0;
        for(Integer id : currentPath)
            if(nextPath.contains(id))
                sharedVertexes++;
        return sharedVertexes <= 2;
    }

    static class Vertex {
        final int id;
        final String text;
        int outCount = 0;
        int inCount = 0;
        List<Vertex> outEdges = new ArrayList<>();

        public Vertex(int id, String text) {
            this.id = id;
            this.text = text;
        }

        public void addEdge(Vertex v) {
            if (id == v.id)
                return; // for this problem, excluding self connected edges
            if(outEdges.stream().filter(w -> w.id == v.id).findFirst().isPresent())
                return; // avoiding also to add already existing edge

            outEdges.add(v);
            outCount++;
            v.inCount++;

            // if this is the second edge outgoing from this node, storing it
            if (outCount == 2)
                VERTEXES_WITH_MORE_THAN_ONE_OUTGOING_EDGES.add(this);
            // if this is the second edge incoming for node v, storing it
            if (v.inCount == 2)
                VERTEXES_WITH_MORE_THAN_ONE_INCOMING_EDGES.add(v.id);
        }
    }

    static class PathKey {
        final int from;
        final int to;

        public PathKey(int from, int to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + from;
            result = prime * result + to;
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
            PathKey other = (PathKey) obj;
            if (from != other.from)
                return false;
            if (to != other.to)
                return false;
            return true;
        }
        
    }

}
