import java.util.*;
import java.io.*;

/*
    Task. Construct the suffix tree of a string.

    Input Format. A string Text ending with a “$” symbol.
    
    Constraints. 1 ≤ |Text| ≤ 5 000; except for the last symbol, Text contains symbols A, C, G, T only.
    
    Output Format. The strings labeling the edges of SuffixTree(Text) in any order.
*/

// Good job! (Max time used: 0.73/3.00, max memory used: 324554752/2147483648.)
public class SuffixTree {

    public static void main(String[] args) throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        SuffixTreeDataStructure st = new SuffixTreeDataStructure(text);
        st.print();
    }

    static class SuffixTreeDataStructure {
        private static final int UNDEFINED_INDEX = -1;
        SuffixTreeNode root;

        SuffixTreeDataStructure(String text) {
            this.root = new SuffixTreeNode("", UNDEFINED_INDEX);

            for (int i = 0; i < text.length(); i++)
                addNode(text.substring(i), i);
        }

        private void addNode(String text, int i) {
            // i'm using a string builder so when i call findParent it will automatically
            // refresh the text reference
            StringBuilder sb = new StringBuilder(text);
            SuffixTreeNode parent = findParent(sb);
            String commonPrefix = findCommonPrefix(parent.label, sb.toString());
            if (commonPrefix.isEmpty()) {
                // create new node to add to the found parent
                parent.children.add(new SuffixTreeNode(text, i));
            } else {
                // if the label of the parent is not the common prefix, then i'm going to split
                // it into 2 nodes
                if (parent.label.length() != commonPrefix.length()) {
                    SuffixTreeNode newChild = new SuffixTreeNode(parent.label.substring(commonPrefix.length()),
                            parent.index);
                    newChild.children.addAll(parent.children);
                    parent.children = new ArrayList<>();
                    parent.children.add(newChild);
                    parent.label = commonPrefix;
                }

                // the index of the parent become undefind (its indexes are contained inside the
                // children)
                parent.index = UNDEFINED_INDEX;

                // adding the new child to the parent
                parent.children.add(new SuffixTreeNode(sb.substring(commonPrefix.length()), i));
            }
        }

        private String findCommonPrefix(String text, String substring) {
            int minLength = Math.min(text.length(), substring.length());
            for (int i = 0; i < minLength; i++) {
                if (text.charAt(i) != substring.charAt(i))
                    return substring.substring(0, i);
            }
            return text.substring(0, minLength);
        }

        /* with this method the algorithm become really too slow (from Max time used: 0.61 to 6.47!) */
        // private String findCommonPrefix(String text, String substring) {
        //     String commonPrefix = "";
        //     int minLength = Math.min(text.length(), substring.length());
        //     for (int i = 0; i < minLength; i++) {
        //         char c = text.charAt(i);
        //         if (c != substring.charAt(i))
        //             break;
        //         commonPrefix += c;
        //     }
        //     return commonPrefix;
        // }

        private SuffixTreeNode findParent(StringBuilder sb) {

            SuffixTreeNode lastNode = root;
            boolean stop = false;
            while (!stop) {
                if (lastNode.children.isEmpty())
                    return lastNode;

                stop = true;
                // checking for each child if one of them can be a potential parent
                if(lastNode.label.equals(findCommonPrefix(lastNode.label, sb.toString()))) {
                    for (SuffixTreeNode child : lastNode.children) {
                        // found a potential parent
                        if (sb.length() > lastNode.label.length() &&
                                child.label.charAt(0) == sb.charAt(lastNode.label.length())) {
                            // removing from the input text the current parent label
                            sb.delete(0, lastNode.label.length());
                            lastNode = child;
                            stop = false;
                            break;
                        }
                    }
                }
            }
            return lastNode;
        }

        void print() {
            for (SuffixTreeNode child : root.children)
                child.print();
        }

    }

    static class SuffixTreeNode {
        int index;
        String label;
        List<SuffixTreeNode> children;

        SuffixTreeNode(String text, int index) {
            this.index = index;
            this.label = text;
            this.children = new ArrayList<>();
        }

        public void print() {
            System.out.println(label);
            for (SuffixTreeNode child : children)
                child.print();
        }

    }

    // just an utility scanner
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