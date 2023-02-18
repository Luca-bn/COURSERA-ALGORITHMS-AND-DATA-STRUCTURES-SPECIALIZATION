import java.util.*;
import java.io.*;

/*
    Problem Description
    Task. You are given a binary tree with integers as its keys. You need to test whether it is a correct binary
    search tree. The definition of the binary search tree is the following: for any node of the tree, if its
    key is 𝑥, then for any node in its left subtree its key must be strictly less than 𝑥, and for any node in
    its right subtree its key must be strictly greater than 𝑥. In other words, smaller elements are to the
    left, and bigger elements are to the right. You need to check whether the given binary tree structure
    satisfies this condition. You are guaranteed that the input contains a valid binary tree. That is, it is a
    tree, and each node has at most two children.

    Input Format. The first line contains the number of vertices 𝑛. The vertices of the tree are numbered
    from 0 to 𝑛 − 1. Vertex 0 is the root.
    The next 𝑛 lines contain information about vertices 0, 1, ..., 𝑛−1 in order. Each of these lines contains
    three integers 𝑘𝑒𝑦𝑖, 𝑙𝑒𝑓𝑡𝑖 and 𝑟𝑖𝑔ℎ𝑡𝑖 — 𝑘𝑒𝑦𝑖 is the key of the 𝑖-th vertex, 𝑙𝑒𝑓𝑡𝑖 is the index of the left
    child of the 𝑖-th vertex, and 𝑟𝑖𝑔ℎ𝑡𝑖 is the index of the right child of the 𝑖-th vertex. If 𝑖 doesn’t have
    left or right child (or both), the corresponding 𝑙𝑒𝑓𝑡𝑖 or 𝑟𝑖𝑔ℎ𝑡𝑖 (or both) will be equal to −1.
    
    Constraints. 0 ≤ 𝑛 ≤ 105; −231 < 𝑘𝑒𝑦𝑖 < 231 − 1; −1 ≤ 𝑙𝑒𝑓𝑡𝑖, 𝑟𝑖𝑔ℎ𝑡𝑖 ≤ 𝑛 − 1. It is guaranteed that the
    input represents a valid binary tree. In particular, if 𝑙𝑒𝑓𝑡𝑖 ̸= −1 and 𝑟𝑖𝑔ℎ𝑡𝑖 ̸= −1, then 𝑙𝑒𝑓𝑡𝑖 ̸= 𝑟𝑖𝑔ℎ𝑡𝑖.
    Also, a vertex cannot be a child of two different vertices. Also, each vertex is a descendant of the root
    vertex. All keys in the input will be different.
    
    Output Format. If the given binary tree is a correct binary search tree (see the definition in the problem
    description), output one word “CORRECT” (without quotes). Otherwise, output one word “INCORRECT”
    (without quotes).
*/

// Good job! (Max time used: 0.37/3.00, max memory used: 71454720/2147483648.)
public class is_bst {
    class FastScanner {
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

    public class IsBST {
        class Node {
            int key;
            int left;
            int right;

            int max = Integer.MIN_VALUE;
            int min = Integer.MAX_VALUE;

            Node(int key, int left, int right) {
                this.left = left;
                this.right = right;
                this.key = key;
            }
        }

        int nodes;
        Node[] tree;

        void read() throws IOException {
            FastScanner in = new FastScanner();
            nodes = in.nextInt();
            tree = new Node[nodes];
            for (int i = 0; i < nodes; i++) {
                tree[i] = new Node(in.nextInt(), in.nextInt(), in.nextInt());
            }
        }

        boolean isBinarySearchTree() {
            if (tree.length == 0)
                return true;
            return isBinarySearchSubtree(0);
        }

        private boolean isBinarySearchSubtree(int i) {
            Node current = tree[i];
            current.max = current.key;
            current.min = current.key;

            // base case (leaf node is valid BST)
            if (current.left == -1 && current.right == -1)
                return true;

            // check for leftside
            if (current.left != -1 && !isBinarySearchSubtree(current.left))
                return false;

            // check for rightside
            if (current.right != -1 && !isBinarySearchSubtree(current.right))
                return false;

            // check if left key is < then current one and right key is > then current one
            if((current.left != -1 && current.key < tree[current.left].key)
                || (current.right != -1 && current.key > tree[current.right].key))
                return false;

            // after key check, verify for min and max values in subtree
            // current key must be > then min and max key in left side
            if(current.left != -1) {
                Node left = tree[current.left];
                if(left.min > current.key || left.max > current.key)
                    return false;
                current.min = Math.min(current.min, left.min);
                current.max = Math.max(current.max, left.max);
            }

            // current key must be < then min and max key in right side
            if(current.right != -1) {
                Node right = tree[current.right];
                if(right.min < current.key || right.max < current.key)
                    return false;
                current.min = Math.min(current.min, right.min);
                current.max = Math.max(current.max, right.max);
            }

            return true;
        }
    }

    static public void main(String[] args) throws IOException {
        new Thread(null, new Runnable() {
            public void run() {
                try {
                    new is_bst().run();
                } catch (IOException e) {
                }
            }
        }, "1", 1 << 26).start();
    }

    public void run() throws IOException {
        IsBST tree = new IsBST();
        tree.read();
        if (tree.isBinarySearchTree()) {
            System.out.println("CORRECT");
        } else {
            System.out.println("INCORRECT");
        }
    }
}
