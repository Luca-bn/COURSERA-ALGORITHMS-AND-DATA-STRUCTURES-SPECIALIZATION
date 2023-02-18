import java.util.*;
import java.io.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/*
    Task. Given a list of error-prone reads, perform the task of Genome Assembly using de Bruijn graphs and
    return the circular genome from which they came. Break the reads into fragments of length 𝑘 = 15
    before constructing the de Bruijn graph, remove tips, and handle bubbles.
    
    Dataset. Each line of the input contains a single read. The reads are given to you in alphabetical order
    because their true order is hidden from you. Each read is 100 nucleotides long and contains a single
    sequencing error (i.e., one mismatch per read) in order to simulate the 1% error rate of Illumina
    sequencing machines. Note that you are not given the 100-mer composition of the genome (i.e., some
    100-mers may be missing).
    
    Output. Output the assembled genome on a single line.
*/

// Good job! (Max time used: 0.49/15.00, max memory used: 57769984/2147483648.)
public class PhiX174AssemblerFromErrorProneWithDeBruijn {

    private static final boolean DEBUG = false;
    private static final PrintStream OUT = System.out;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        DeBruijnGraphHelper helper = new DeBruijnGraphHelper();
        while (scanner.hasNext()) {
            String read = scanner.next();
            if (read == null || read.length() < 3) {
                break;
            }
            helper.createKmersFromRead(read);
        }
        scanner.close();
        helper.buildGraph();
        while (!helper.makeGraphBalanced());
        
        print(helper.findEulerianCycle());
    }

    private static void print(LinkedList<Vertex> cycle) {
        if(DEBUG)
            System.out.println();
        if(cycle.peekFirst().id == cycle.peekLast().id)
            cycle.removeLast();
        for(Vertex v : cycle)
            OUT.print(v.text.charAt(v.text.length() - 1));
    }

    static class DeBruijnGraphHelper {

        private int K = 20;
        private double averageOccurrency;
        private final Map<String, Integer> kmersOccurrenciesMap = new HashMap<>();
        private final Map<String, Vertex> vertexesMap = new HashMap<>();
        private final Map<Integer, Vertex> graph = new HashMap<>();

        public void createKmersFromRead(String read) {
            for (int i = 0; i <= read.length() - K; i++) {
                String kmer = read.substring(i, i + K);
                Integer kmerOccurrency = kmersOccurrenciesMap.getOrDefault(kmer, 0);
                kmersOccurrenciesMap.put(kmer, ++kmerOccurrency);
            }
        }

        public void buildGraph() {
            correctKmers();
            for (Entry<String, Integer> entry : kmersOccurrenciesMap.entrySet()) {
                String k1 = entry.getKey().substring(0, entry.getKey().length() - 1);
                String k2 = entry.getKey().substring(1);

                Vertex v1 = vertexesMap.get(k1);
                if (v1 == null) {
                    v1 = new Vertex(vertexesMap.size(), k1);
                    vertexesMap.put(k1, v1);
                    graph.put(v1.id, v1);
                }
                Vertex v2 = vertexesMap.get(k2);
                if (v2 == null) {
                    v2 = new Vertex(vertexesMap.size(), k2);
                    vertexesMap.put(k2, v2);
                    graph.put(v2.id, v2);
                }

                v1.outEdges.add(new Edge(v1.id, v2.id, entry.getValue()));
                v2.inEdges.add(new Edge(v2.id, v1.id, entry.getValue()));
            }
        }

        private void correctKmers() {
            // removing low coverage kmers (kmers that appears less than average occurrency
            // / 3)
            if (DEBUG) {
                OUT.println("\n********** Starting kmers correction: **********");
                printKmerStatistics();
            }
            int totalKmers = kmersOccurrenciesMap.size();
            int occurrenciesSum = 0;
            for (Integer occurrency : kmersOccurrenciesMap.values()) {
                occurrenciesSum += occurrency;
            }
            averageOccurrency = occurrenciesSum / totalKmers;
            List<String> keys = kmersOccurrenciesMap.entrySet().stream()
                    .filter(e -> e.getValue() <= averageOccurrency / 3).map(e -> e.getKey())
                    .collect(Collectors.toList());
            for (String key : keys)
                kmersOccurrenciesMap.remove(key);
            if (DEBUG) {
                OUT.println("Removed kmers: " + (totalKmers - kmersOccurrenciesMap.size()));
                OUT.println("********** End kmers correction **********\n");
            }
        }

        public boolean makeGraphBalanced() {
            if (DEBUG)
                OUT.println("\n********** Checking for balanced graph: **********");
            Set<Vertex> startVertexes = new HashSet<>();
            Set<Vertex> endVertexes = new HashSet<>();
            Set<Vertex> unbalancedOutVertexes = new HashSet<>();
            Set<Vertex> unbalancedInVertexes = new HashSet<>();
            // checking all vertexes which makes graph unbalanced
            Set<Integer> isolatedVertexes = new HashSet<>();
            for (Vertex v : graph.values()) {
                if (v.inEdges.isEmpty() && v.outEdges.isEmpty())
                    isolatedVertexes.add(v.id);
                if (v.inEdges.isEmpty() && !v.outEdges.isEmpty())
                    startVertexes.add(v);
                else if (!v.inEdges.isEmpty() && v.outEdges.isEmpty())
                    endVertexes.add(v);
                else if (v.inEdges.size() > v.outEdges.size())
                    unbalancedInVertexes.add(v);
                else if (v.inEdges.size() < v.outEdges.size())
                    unbalancedOutVertexes.add(v);
            }
            if (DEBUG) {
                printGraphStatistics(startVertexes, endVertexes, unbalancedOutVertexes, unbalancedInVertexes);
                OUT.println("Isolated vertexes found: " + isolatedVertexes.size());
                for (Integer vertex : isolatedVertexes)
                    graph.remove(vertex);
            }
            fix(startVertexes, endVertexes, unbalancedInVertexes, unbalancedOutVertexes);
            return ((startVertexes.isEmpty() && endVertexes.isEmpty())
                    || (startVertexes.size() == 1 && endVertexes.size() == 1))
                    && unbalancedInVertexes.isEmpty() && unbalancedOutVertexes.isEmpty();
        }

        private void fix(Set<Vertex> startVertexes, Set<Vertex> endVertexes, Set<Vertex> unbalancedInVertexes,
                Set<Vertex> unbalancedOutVertexes) {

            boolean bubblesSolved = false;
            Map<Integer, DFSResult> dfsResults = new HashMap<>();
            if (!unbalancedInVertexes.isEmpty()) {
                for (Vertex v : unbalancedInVertexes) {
                    DFSResult result = dfs(v, false);
                    if (result.bubbles != null && !result.bubbles.isEmpty()) {
                        bubblesSolved = solveBubbles(result) || bubblesSolved;
                    }
                    dfsResults.put(v.id, result);
                }
            }
            if (bubblesSolved)
                return;

            boolean inTipsSolved = false;
            for (DFSResult dfsResult : dfsResults.values()) {
                if (dfsResult.deadEndings != null && !dfsResult.deadEndings.isEmpty()) {
                    inTipsSolved = solveTips(dfsResult) || inTipsSolved;
                }
            }
            if (inTipsSolved)
                return;

            dfsResults = new HashMap<>();
            if (!unbalancedOutVertexes.isEmpty()) {
                for (Vertex v : unbalancedOutVertexes) {
                    DFSResult result = dfs(v, true);
                    if (result.bubbles != null && !result.bubbles.isEmpty()) {
                        bubblesSolved = solveBubbles(result) || bubblesSolved;
                    }
                    dfsResults.put(v.id, result);
                }
            }
            if (bubblesSolved)
                return;

            inTipsSolved = false;
            for (DFSResult dfsResult : dfsResults.values()) {
                if (dfsResult.deadEndings != null && !dfsResult.deadEndings.isEmpty()) {
                    inTipsSolved = solveTips(dfsResult) || inTipsSolved;
                }
            }
            if (inTipsSolved)
                return;

            if(DEBUG)
                OUT.println("Increasing K size, K = " + K * 2);
            K = K * 2;
        }

        private boolean solveBubbles(DFSResult result) {

            boolean bubbleSolved = false;
            Map<Integer, List<List<Vertex>>> paths = null;
            while (!(paths = reconstructPaths(result, result.bubbles)).isEmpty()) {
                Set<Integer> toRem = new HashSet<>();
                for (Entry<Integer, List<List<Vertex>>> entry : paths.entrySet())
                    if (entry.getValue().size() < 2)
                        toRem.add(entry.getKey());
                for(Integer id : toRem)
                    paths.remove(id);
                if (paths.isEmpty())
                    break;
                // trying to solve first bubbles with less paths
                Entry<Integer, List<List<Vertex>>> nextBubbleToSolve = paths.entrySet().stream()
                        .min((e1, e2) -> Integer.compare(e1.getValue().size(), e2.getValue().size())).orElse(null);
                if (nextBubbleToSolve != null) {
                    solveBubble(nextBubbleToSolve.getKey(), nextBubbleToSolve.getValue(), result.parents,
                            result.forword);
                    bubbleSolved = true;
                }
            }
            return bubbleSolved;
        }

        private void solveBubble(Integer bubbleKey, List<List<Vertex>> paths, Map<Integer, Set<Integer>> parents,
                boolean forword) {
            if (DEBUG) {
                OUT.println("\n********** Trying to solve bubble "+ (forword ? "forword" : "backword")+": **********");
                for (List<Vertex> path : paths) {
                    int index = 1;
                    for (Vertex v : path) {
                        OUT.print(v.id + " (" + v.text + ")");
                        if (index++ < path.size())
                            OUT.print(" -> ");
                    }
                    OUT.println();
                }
            }

            int pathIndex = -1;
            int pathCoverage = Integer.MIN_VALUE;
            for (int i = 0; i < paths.size(); i++) {
                List<Vertex> path = paths.get(i);
                int coverage = 0;
                for (int j = 0; j < path.size() - 1; j++) {
                    Vertex from = path.get(j);
                    Vertex to = path.get(j + 1);
                    List<Edge> edges = !forword ? from.outEdges : from.inEdges;
                    coverage += edges.stream().filter(e -> e.to == to.id).findFirst().get().cardinality;
                }
                coverage = coverage / path.size();
                if (DEBUG)
                    OUT.println(String.format("Path %s coverage: %s", i, coverage));
                if (coverage > pathCoverage) {
                    pathCoverage = coverage;
                    pathIndex = i;
                }
            }

            if (DEBUG)
                OUT.println("Path with highest coverage: " + pathIndex);

            for (int i = 0; i < paths.size(); i++) {
                if (i == pathIndex)
                    continue;
                List<Vertex> path = paths.get(i);
                for (int j = 0; j < path.size() - 1; j++) {
                    Vertex from = path.get(j);
                    Vertex to = path.get(j + 1);
                    List<Edge> fromEdges = !forword ? from.outEdges : from.inEdges;
                    List<Edge> toEdges = forword ? to.outEdges : to.inEdges;
                    fromEdges.removeIf(e -> e.to == to.id);
                    toEdges.removeIf(e -> e.to == from.id);
                    parents.getOrDefault(from.id, new HashSet<>()).removeIf(id -> id == to.id);
                    parents.getOrDefault(to.id, new HashSet<>()).removeIf(id -> id == from.id);
                }
            }

        }

        private boolean solveTips(DFSResult dfsResult) {
            if (DEBUG)
                OUT.println("\n********** Trying tips solving "+(dfsResult.forword ? "forword" : "backword") +" **********");
            boolean tipSolved = false;
            Map<Integer, List<List<Vertex>>> paths = reconstructPaths(dfsResult, dfsResult.deadEndings);
            for (Entry<Integer, List<List<Vertex>>> entry : paths.entrySet()) {
                if (entry.getValue().size() != 1)
                    continue;
                List<Vertex> path = entry.getValue().get(0);
                if (DEBUG) {
                    OUT.print("Tip to remove: ");
                    if (dfsResult.forword) {
                        Vertex v = path.get(0);
                        OUT.print(v.id+"("+v.text+")");
                        for(int i = 1; i < path.size() - 1; i++) {
                            v = path.get(i);
                            OUT.print(String.format(" -> %s(%s) ", v.id, v.text));
                        }
                        v = path.get(path.size() - 1);
                        OUT.print(String.format(" -> %s(%s) ", v.id, v.text));
                    }
                    else {
                        Vertex v = path.get(path.size() - 1);
                        OUT.print(v.id+"("+v.text+")");
                        for(int i = path.size() - 2; i > 0; i--) {
                            v = path.get(i);
                            OUT.print(String.format(" -> %s(%s) ", v.id, v.text));
                        }
                        v = path.get(0);
                        OUT.print(String.format(" -> %s(%s) ", v.id, v.text));
                    }
                    OUT.println();
                }
                if(DEBUG)
                    OUT.print("removed vertexes: ");
                for (int i = 0; i < path.size() - 1; i++) {
                    Vertex from = path.get(i);
                    Vertex to = path.get(i + 1);
                    List<Edge> fromEdges = !dfsResult.forword ? from.outEdges : from.inEdges;
                    List<Edge> toEdges = dfsResult.forword ? to.outEdges : to.inEdges;
                    boolean removed = fromEdges.removeIf(e -> e.to == to.id) && toEdges.removeIf(e -> e.to == from.id);
                    dfsResult.parents.getOrDefault(from.id, new HashSet<>()).removeIf(id -> id == to.id);
                    dfsResult.parents.getOrDefault(to.id, new HashSet<>()).removeIf(id -> id == from.id);
                    if(DEBUG) {
                        OUT.print(from.id + " ");
                    }
                    if(!removed || !toEdges.isEmpty()) {
                        break;
                    }
                }
                OUT.println();
                tipSolved = true;
            }
            return tipSolved;
        }

        private Map<Integer, List<List<Vertex>>> reconstructPaths(DFSResult result, List<Integer> errors) {
            Map<Integer, List<List<Vertex>>> paths = new HashMap<>();
            for (Integer error : errors) {
                paths.put(error, new ArrayList<>());
                for (Integer parent : result.parents.get(error)) {
                    List<Vertex> startingPath = new ArrayList<>();
                    startingPath.add(graph.get(error));
                    startingPath.add(graph.get(parent));
                    continuePath(result.startingPoint, paths.get(error), startingPath, result.parents);
                }
            }
            return paths;
        }

        private void continuePath(Integer searchVertex, List<List<Vertex>> bubblePaths, List<Vertex> currentPath,
                Map<Integer, Set<Integer>> parents) {
            Set<Integer> currParents = parents.get(currentPath.get(currentPath.size() - 1).id);
            if (currParents == null || currParents.isEmpty()) {
                if (currentPath.get(currentPath.size() - 1).id != searchVertex)
                    return; // this path is no more part of a bubble => not adding to bubblePaths
                // otherwise, adding current path to bubblepaths
                bubblePaths.add(currentPath);
                return;
            }
            for (Integer parent : currParents) {
                List<Vertex> path = new ArrayList<>(currentPath);
                path.add(graph.get(parent));
                continuePath(searchVertex, bubblePaths, path, parents);
            }
        }

        private DFSResult dfs(Vertex v, boolean forword) {
            Map<Integer, Set<Integer>> parents = new HashMap<>();
            List<Integer> deadEndingVertexes = new ArrayList<>();
            int coverage = 0;
            int visitedVertexes = 0;
            Stack<Vertex> stack = new Stack<>();
            Stack<Integer> depth = new Stack<>();
            stack.add(v);
            depth.add(0);
            while (!stack.isEmpty()) {
                Vertex current = stack.pop();
                int currentDepth = depth.pop();
                visitedVertexes++;
                if (currentDepth <= K) {
                    List<Edge> edges = forword ? current.outEdges : current.inEdges;
                    if (edges.isEmpty())
                        deadEndingVertexes.add(current.id);
                    for (Edge edge : edges) {
                        coverage += edge.cardinality;
                        Vertex next = graph.get(edge.to);
                        if (parents.get(next.id) == null)
                            parents.put(next.id, new HashSet<>());
                        parents.get(next.id).add(current.id);
                        stack.add(next);
                        depth.add(currentDepth + 1);
                    }
                }
            }
            return buildDfsResult(v, forword, parents, deadEndingVertexes, coverage, visitedVertexes);
        }

        private DFSResult buildDfsResult(Vertex startingPoint, boolean forword, Map<Integer, Set<Integer>> parents,
                List<Integer> deadEndingVertexes, int coverage, int visitedVertexes) {
            List<Integer> bubblesEnd = parents.entrySet().stream().filter(e -> e.getValue().size() > 1)
                    .map(e -> e.getKey()).collect(Collectors.toList());
            return DFSResult.instance().bubbles(bubblesEnd).coverage(coverage / visitedVertexes)
                    .deadEndigs(deadEndingVertexes).startingPoint(startingPoint.id).parents(parents).forword(forword);
        }

        public LinkedList<Vertex> findEulerianCycle() {
            LinkedList<Vertex> cycle = new LinkedList<>();
            Map<Integer, Integer> outs = new HashMap<>();

            Vertex starting = findStartingVertex(outs);

            dfs(starting, cycle, outs);

            return cycle;
        }

        private Vertex findStartingVertex(Map<Integer, Integer> outs) {
            boolean stop = false;
            Vertex starting = null;
            for(Vertex v : graph.values()) {
                outs.put(v.id, v.outEdges.size());
                if(!v.outEdges.isEmpty()) {
                    if(!stop)
                        starting = v;
                    stop = stop || v.inEdges.isEmpty();
                }
            }
            return starting;
        }

        private void dfs(Vertex v, LinkedList<Vertex> cycle, Map<Integer, Integer> outs) {
            while (outs.get(v.id) > 0) {
                outs.put(v.id, outs.get(v.id) - 1);
                Vertex next = graph.get(v.outEdges.get(outs.get(v.id)).to);
                dfs(next, cycle, outs);
            }
            cycle.addFirst(v);
        }

        private void printKmerStatistics() {
            int totalKmers = kmersOccurrenciesMap.size();
            int occurrenciesSum = 0;
            Map<Integer, Integer> occurrenciesCounts = new HashMap<>();
            for (Integer occurrency : kmersOccurrenciesMap.values()) {
                Integer count = occurrenciesCounts.getOrDefault(occurrency, 0);
                occurrenciesCounts.put(occurrency, ++count);
                occurrenciesSum += occurrency;
            }
            double averageOccurrency = occurrenciesSum / totalKmers;
            OUT.println();
            OUT.println(String.format("%53s", "").replaceAll(" ", "*"));
            OUT.printf("* %-23s : %-23s *%n", "total kmers", totalKmers);
            OUT.printf("* %-23s : %-23s *%n", "total coverege", occurrenciesSum);
            OUT.printf("* %-23s : %-23s *%n", "average coverege", averageOccurrency);
            OUT.printf("* %-23s : %-23s *%n", "with lower coverege", occurrenciesCounts.entrySet().stream()
                    .filter(e -> e.getKey() < averageOccurrency).mapToInt(e -> e.getValue()).sum());
            for (Entry<Integer, Integer> entry : occurrenciesCounts.entrySet())
                OUT.printf("* %-23s : %-23s *%n", "with " + entry.getKey() + " occurrency", entry.getValue());
            OUT.println(String.format("%53s", "").replaceAll(" ", "*"));
            OUT.println();
        }

        private void printGraphStatistics(Set<Vertex> starts, Set<Vertex> ends, Set<Vertex> out, Set<Vertex> ins) {
            OUT.println();
            OUT.println(String.format("%53s", "").replaceAll(" ", "*"));
            OUT.printf("* %-23s : %-23s *%n", "graph size", graph.size());
            OUT.printf("* %-23s : %-23s *%n", "starting vertexes", starts.size());
            OUT.printf("* %-23s : %-23s *%n", "ending vertexes", ends.size());
            OUT.printf("* %-23s : %-23s *%n", "out > in", out.size());
            OUT.printf("* %-23s : %-23s *%n", "in > out", ins.size());
            OUT.println(String.format("%53s", "").replaceAll(" ", "*"));
            OUT.println();
        }
    }

    static class Vertex {
        final int id;
        final String text;
        List<Edge> outEdges = new ArrayList<>();
        List<Edge> inEdges = new ArrayList<>();

        public Vertex(int id, String text) {
            this.id = id;
            this.text = text;
        }

        @Override
        public int hashCode() {
            return id;
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

    static class Edge {
        int from, to;
        int cardinality;

        public Edge(int from, int to, int cardinality) {
            this.from = from;
            this.to = to;
            this.cardinality = cardinality;
        }
    }

    static class DFSResult {

        int startingPoint;
        boolean forword;
        List<Integer> bubbles;
        List<Integer> deadEndings;
        int coverage;
        Map<Integer, Set<Integer>> parents;

        public static DFSResult instance() {
            return new DFSResult();
        }

        public DFSResult forword(boolean forword) {
            this.forword = forword;
            return this;
        }

        public DFSResult startingPoint(int v) {
            this.startingPoint = v;
            return this;
        }

        public DFSResult coverage(int c) {
            this.coverage = c;
            return this;
        }

        public DFSResult bubbles(List<Integer> b) {
            this.bubbles = b;
            return this;
        }

        public DFSResult deadEndigs(List<Integer> d) {
            this.deadEndings = d;
            return this;
        }

        public DFSResult parents(Map<Integer, Set<Integer>> p) {
            this.parents = p;
            return this;
        }
    }

}
