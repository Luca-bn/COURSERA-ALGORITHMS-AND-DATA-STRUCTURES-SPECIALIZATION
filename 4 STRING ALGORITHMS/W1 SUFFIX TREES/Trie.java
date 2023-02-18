import java.io.*;
import java.util.*;

/*
    Task. Construct a trie from a collection of patterns.

    Input Format. An integer 𝑛 and a collection of strings Patterns = {𝑝1, . . . , 𝑝𝑛} (each string is given on a
    separate line).
    
    Constraints. 1 ≤ 𝑛 ≤ 100; 1 ≤ |𝑝𝑖| ≤ 100 for all 1 ≤ 𝑖 ≤ 𝑛; 𝑝𝑖’s contain only symbols A, C, G, T; no 𝑝𝑖 is
    a prefix of 𝑝𝑗 for all 1 ≤ 𝑖 ̸= 𝑗 ≤ 𝑛.
    
    Output Format. The adjacency list corresponding to Trie(Patterns), in the following format. If
    Trie(Patterns) has 𝑛 nodes, first label the root with 0 and then label the remaining nodes with the
    integers 1 through 𝑛−1 in any order you like. Each edge of the adjacency list of Trie(Patterns) will be
    encoded by a triple: the first two members of the triple must be the integers 𝑖, 𝑗 labeling the initial and
    terminal nodes of the edge, respectively; the third member of the triple must be the symbol 𝑐 labeling
    the edge; output each such triple in the format u->v:c (with no spaces) on a separate line.
*/

// Good job! (Max time used: 0.27/2.00, max memory used: 69869568/2147483648.)
public class Trie {

    static public void main(String[] args) throws IOException {
        FastScanner scanner = new FastScanner();
        int patternsCount = scanner.nextInt();
        String[] patterns = new String[patternsCount];
        for (int i = 0; i < patternsCount; ++i) {
            patterns[i] = scanner.next();
        }
        TrieDataStructure trie = new TrieDataStructure();
        trie.addPatterns(patterns);
        trie.printTrie();
    }

    static class TrieDataStructure {
        TrieNode root;
        int insertionIndex = 0;

        TrieDataStructure() {
            this.root = new TrieNode();
            this.root.index = insertionIndex++;
            this.root.children = new HashMap<>();
        }

        void addPatterns(String[] patterns) {
            for (String pattern : patterns)
                addPattern(pattern);
        }

        void addPattern(String pattern) {
            TrieNode currentNode = root;
            for (char c : pattern.toCharArray()) {
                if (currentNode.children.get(c) != null) {
                    currentNode = currentNode.children.get(c);
                } else {
                    TrieNode node = new TrieNode();
                    node.label = c;
                    node.index = insertionIndex++;
                    node.parentIndex = currentNode.index;
                    node.children = new HashMap<>();
                    currentNode.children.put(c, node);
                    currentNode = node;
                }
            }
        }

        void printTrie() {
            for (TrieNode node : root.children.values()) {
                node.print();
            }
        }
    }

    static class TrieNode {
        Character label;
        Integer parentIndex;
        Integer index;
        Map<Character, TrieNode> children;

        void print() {
            System.out.println(parentIndex + "->" + index + ":" + label);
            if (children != null)
                for (TrieNode node : children.values()) {
                    node.print();
                }
        }
    }

    static class FastScanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        FastScanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}
