import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/*
    Task. Construct a suffix tree from the suffix array and LCP array of a string.

    Input Format. The first line contains a string Text ending with a “$” symbol, the second line contains
    SuffixArray(Text) as a list of |Text| integers separated by spaces, the last line contains LCP(Text) as
    a list of |Text| − 1 integers separated by spaces.

    Constraints. 1 ≤ |Text(Text)| ≤ 2 · 10^5; except for the last symbol, Text contains symbols A, C, G, T only.

    Output Format. The output format in this problem differs from the output format in the problem “Suffix
    Tree” from the Programming Assignment 2 and is somewhat tricky. It is because this problem is
    harder: the input string can be longer, so it would take too long to output all the edge labels directly
    and compare them with the correct ones, as their combined length can be Θ(|Text|2), which is too
    much when the Text can be as long as 200 000 characters.
    Output the 𝑇𝑒𝑥𝑡 from the input on the first line. Then output all the edges of the suffix tree in a
    specific order (see below), each on its own line. Output each edge as a pair of integers (start, end),
    where start is the position in Text corresponding to the start of the edge label substring in the Text
    and end is the position right after the end of the edge label in the Text. Note that start must be a
    valid position in the Text, that is, 0 ≤ 𝑠𝑡𝑎𝑟𝑡 ≤ |Text| − 1, and end must be between 1 and |Text|
    inclusive. Substring Text[start..end−1] must be equal to the edge label of the corresponding edge. For
    example, if Text = “ACACAA$” and the edge label is “CA”, you can output this edge either as (1, 3)
    corresponding to Text[1..2] = “CA” or as (3, 5) corresponding to Text[3..4] = “CA” — both variants
    will be accepted.
    The order of the edges is important here — if you output all the correct edges in the wrong order, your
    solution will not be accepted. However, you don’t need to construct this order yourself if you
    write in C++, Java or Python3, because it is implemented for you in the starter files.
    Output all the edges in the order of sorted suffixes: first, take the leaf of the suffix tree corresponding
    to the smallest suffix of Text and output all the edges on the path from the root to this leaf. Then
    take the leaf corresponding to the second smallest suffix of Text and output all the edges on the path
    from the root to this leaf except for those edges which were printed before. Then take the leaf
    corresponding to the third smallest suffix, fourth smallest suffix and so on. Print each edge only once
    — as a part of the path corresponding to the smallest suffix of Text where this edge appears. This way,
    you will only output 𝑂(|Text|) integers. See the examples below for clarification.
*/

// Good job! (Max time used: 6.72/20.00, max memory used: 368431104/2147483648.)
public class SuffixTreeFromArray {

    public static void main(String[] args) throws Exception {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        int[] suffixArray = new int[text.length()];
        for (int i = 0; i < suffixArray.length; i++)
            suffixArray[i] = scanner.nextint();
        int[] lcpArray = new int[text.length() - 1];
        for (int i = 0; i < lcpArray.length; i++)
            lcpArray[i] = scanner.nextint();
        SuffixTreeNode suffixTree = buildSuffixTree(text, suffixArray, lcpArray);
        System.out.println(text);
        LinkedList<SuffixTreeNode> stack = new LinkedList<>();
        for (Entry<Character, SuffixTreeNode> entry : suffixTree.children.entrySet())
            stack.addFirst(entry.getValue());
        SuffixTreeNode current;
        while (!stack.isEmpty()) {
            current = stack.pollFirst();
            System.out.println(current);
            for (Entry<Character, SuffixTreeNode> entry : current.children.entrySet())
                stack.addFirst(entry.getValue());
        }

    }

    private static SuffixTreeNode buildSuffixTree(String text, int[] suffixArray, int[] lcpArray) {
        SuffixTreeNode root = new SuffixTreeNode().stringDepth(0).edgeStart(-1).edgeEnd(-1);
        int lcpPrev = 0;
        SuffixTreeNode currentNode = root;
        for (int i = 0; i < text.length(); i++) {
            int suffix = suffixArray[i];
            while (currentNode.stringDepth > lcpPrev)
                currentNode = currentNode.parent;
            if (currentNode.stringDepth == lcpPrev)
                currentNode = createNewLeaf(currentNode, text, suffix);
            else {
                int edgeStart = suffixArray[i - 1] + currentNode.stringDepth;
                int offset = lcpPrev - currentNode.stringDepth;
                SuffixTreeNode midNode = breakEdge(currentNode, text, edgeStart, offset);
                currentNode = createNewLeaf(midNode, text, suffix);
            }
            if (i < text.length() - 1)
                lcpPrev = lcpArray[i];
        }
        return root;
    }

    private static SuffixTreeNode createNewLeaf(SuffixTreeNode parent,
            String text, int suffix) {
        SuffixTreeNode leaf = new SuffixTreeNode().parent(parent).stringDepth(text.length() - suffix)
                .edgeStart(suffix + parent.stringDepth).edgeEnd(text.length() - 1);
        parent.addChild(text.charAt(leaf.edgeStart), leaf);
        return leaf;
    }

    private static SuffixTreeNode breakEdge(SuffixTreeNode node,
            String text, int start, int offset) {
        Character startChar = text.charAt(start);
        Character midChar = text.charAt(start + offset);
        SuffixTreeNode midNode = new SuffixTreeNode().parent(node).stringDepth(node.stringDepth + offset)
                .edgeStart(start).edgeEnd(start + offset - 1);
        midNode.addChild(midChar, node.children.get(startChar));
        SuffixTreeNode child = node.children.get(startChar);
        child.parent(midNode).edgeStart(child.edgeStart + offset);
        node.addChild(startChar, midNode);
        return midNode;
    }

    static class SuffixTreeNode {
        SuffixTreeNode parent;
        SortedMap<Character, SuffixTreeNode> children = new TreeMap<>(Collections.reverseOrder());
        Integer stringDepth;
        Integer edgeStart;
        Integer edgeEnd;

        SuffixTreeNode parent(SuffixTreeNode parent) {
            this.parent = parent;
            return this;
        }

        SuffixTreeNode addChild(Character key, SuffixTreeNode child) {
            this.children.put(key, child);
            return this;
        }

        SuffixTreeNode stringDepth(Integer stringDepth) {
            this.stringDepth = stringDepth;
            return this;
        }

        SuffixTreeNode edgeStart(Integer edgeStart) {
            this.edgeStart = edgeStart;
            return this;
        }

        SuffixTreeNode edgeEnd(Integer edgeEnd) {
            this.edgeEnd = edgeEnd;
            return this;
        }

        @Override
        public String toString() {
            return edgeStart + " " + (edgeEnd + 1);
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

        int nextint() throws IOException {
            return Integer.parseInt(next());
        }
    }

}
