import java.io.*;
import java.util.*;

/*
    Problem Description
    Task. Implement a data structure that stores a set 𝑆 of integers with the following allowed operations:
    ∙ add(𝑖) — add integer 𝑖 into the set 𝑆 (if it was there already, the set doesn’t change).
    ∙ del(𝑖) — remove integer 𝑖 from the set 𝑆 (if there was no such element, nothing happens).
    ∙ find(𝑖) — check whether 𝑖 is in the set 𝑆 or not.
    ∙ sum(𝑙, 𝑟) — output the sum of all elements 𝑣 in 𝑆 such that 𝑙 ≤ 𝑣 ≤ 𝑟.

    Input Format. Initially the set 𝑆 is empty. The first line contains 𝑛 — the number of operations. The next
    𝑛 lines contain operations. Each operation is one of the following:
    ∙ “+ i" — which means add some integer (not 𝑖, see below) to 𝑆,
    ∙ “- i" — which means del some integer (not 𝑖, see below)from 𝑆,
    ∙ “? i" — which means find some integer (not 𝑖, see below)in 𝑆,
    ∙ “s l r" — which means compute the sum of all elements of 𝑆 within some range of values (not
    from 𝑙 to 𝑟, see below).
    However, to make sure that your solution can work in an online fashion, each request will actually
    depend on the result of the last sum request. Denote 𝑀 = 1 000 000 001. At any moment, let 𝑥 be
    the result of the last sum operation, or just 0 if there were no sum operations before. Then
    ∙ “+ i" means add((𝑖 + 𝑥) mod 𝑀),
    ∙ “- i" means del((𝑖 + 𝑥) mod 𝑀),
    ∙ “? i" means find((𝑖 + 𝑥) mod 𝑀),
    ∙ “s l r" means sum((𝑙 + 𝑥) mod 𝑀, (𝑟 + 𝑥) mod 𝑀).

    Constraints. 1 ≤ 𝑛 ≤ 100 000; 0 ≤ 𝑖 ≤ 10^9.

    Output Format. For each find request, just output “Found" or “Not found" (without quotes; note that the
    first letter is capital) depending on whether (𝑖+𝑥) mod 𝑀 is in 𝑆 or not. For each sum query, output
    the sum of all the values 𝑣 in 𝑆 such that ((𝑙+𝑥) mod 𝑀) ≤ 𝑣 ≤ ((𝑟+𝑥) mod 𝑀) (it is guaranteed that
    in all the tests ((𝑙 + 𝑥) mod 𝑀) ≤ ((𝑟 + 𝑥) mod 𝑀)), where 𝑥 is the result of the last sum operation
    or 0 if there was no previous sum operation.
*/

// Good job! (Max time used: 0.46/4.00, max memory used: 76173312/2147483648.)
public class SetRangeSum {

    BufferedReader br;
    PrintWriter out;
    StringTokenizer st;
    boolean eof;

    // Splay tree implementation

    // Vertex of a splay tree
    class Vertex {
        int key;
        // Sum of all the keys in the subtree - remember to update
        // it after each operation that changes the tree.
        long sum;
        Vertex left;
        Vertex right;
        Vertex parent;

        Vertex(int key, long sum, Vertex left, Vertex right, Vertex parent) {
            this.key = key;
            this.sum = sum;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }
    }

    void update(Vertex v) {
        if (v == null)
            return;
        v.sum = v.key + (v.left != null ? v.left.sum : 0) + (v.right != null ? v.right.sum : 0);
        if (v.left != null) {
            v.left.parent = v;
        }
        if (v.right != null) {
            v.right.parent = v;
        }
    }

    void smallRotation(Vertex v) {
        Vertex parent = v.parent;
        if (parent == null) {
            return;
        }
        Vertex grandparent = v.parent.parent;
        if (parent.left == v) {
            Vertex m = v.right;
            v.right = parent;
            parent.left = m;
        } else {
            Vertex m = v.left;
            v.left = parent;
            parent.right = m;
        }
        update(parent);
        update(v);
        v.parent = grandparent;
        if (grandparent != null) {
            if (grandparent.left == parent) {
                grandparent.left = v;
            } else {
                grandparent.right = v;
            }
        }
    }

    void bigRotation(Vertex v) {
        if (v.parent.left == v && v.parent.parent.left == v.parent) {
            // Zig-zig
            smallRotation(v.parent);
            smallRotation(v);
        } else if (v.parent.right == v && v.parent.parent.right == v.parent) {
            // Zig-zig
            smallRotation(v.parent);
            smallRotation(v);
        } else {
            // Zig-zag
            smallRotation(v);
            smallRotation(v);
        }
    }

    // Makes splay of the given vertex and returns the new root.
    Vertex splay(Vertex v) {
        if (v == null)
            return null;
        while (v.parent != null) {
            if (v.parent.parent == null) {
                smallRotation(v);
                break;
            }
            bigRotation(v);
        }
        return v;
    }

    class VertexPair {
        Vertex left;
        Vertex right;

        VertexPair() {
        }

        VertexPair(Vertex left, Vertex right) {
            this.left = left;
            this.right = right;
        }
    }

    VertexPair find(Vertex root, int key) {
        Vertex v = root;
        Vertex last = root;
        Vertex next = null;
        while (v != null) {
            if (v.key >= key && (next == null || v.key < next.key)) {
                next = v;
            }
            last = v;
            if (v.key == key) {
                break;
            }
            if (v.key < key) {
                v = v.right;
            } else {
                v = v.left;
            }
        }
        root = splay(last);
        return new VertexPair(next, root);
    }

    VertexPair split(Vertex root, int key) {
        VertexPair result = new VertexPair();
        VertexPair findAndRoot = find(root, key);
        root = findAndRoot.right;
        result.right = findAndRoot.left;
        if (result.right == null) {
            result.left = root;
            return result;
        }
        result.right = splay(result.right);
        result.left = result.right.left;
        result.right.left = null;
        if (result.left != null) {
            result.left.parent = null;
        }
        update(result.left);
        update(result.right);
        return result;
    }

    Vertex merge(Vertex left, Vertex right) {
        if (left == null)
            return right;
        if (right == null)
            return left;
        while (right.left != null) {
            right = right.left;
        }
        right = splay(right);
        right.left = left;
        update(right);
        return right;
    }

    Vertex root = null;

    void insert(int x) {
        Vertex left = null;
        Vertex right = null;
        Vertex new_vertex = null;
        VertexPair leftRight = split(root, x);
        left = leftRight.left;
        right = leftRight.right;
        if (right == null || right.key != x) {
            new_vertex = new Vertex(x, x, null, null, null);
        }
        root = merge(merge(left, new_vertex), right);
    }

    void erase(int x) {
        VertexPair leftMiddle = split(root, x);
        Vertex left = leftMiddle.left;
        Vertex middle = leftMiddle.right;
        VertexPair middleRight = split(middle, x + 1);
        middle = middleRight.left;
        Vertex right = middleRight.right;
        if (middle == null || middle.key != x) {
            root = merge(merge(left, middle), right);
        } else {
            middle = null;
            root = merge(left, right);
        }
    }

    boolean find(int x) {
        Vertex left = null;
        Vertex right = null;
        VertexPair leftRight = split(root, x);
        left = leftRight.left;
        right = leftRight.right;
        if (right == null || right.key != x) {
            root = merge(left, right);
            return false;
        } else {
            root = merge(left, right);
            return true;
        }
    }

    long sum(int from, int to) {
        // System.out.println("From "+from + " to " + to);
        VertexPair leftMiddle = split(root, from);
        Vertex left = leftMiddle.left;
        Vertex middle = leftMiddle.right;
        VertexPair middleRight = split(middle, to + 1);
        middle = middleRight.left;
        Vertex right = middleRight.right;
        long ans = 0;
        // Complete the implementation of sum
        // TODO
        if (middle != null)
            ans = middle.sum;
        middle = merge(middle, right);
        root = merge(left, middle);
        return ans;
    }

    public static final int MODULO = 1000000001;

    void solve() throws IOException {
        int n = nextInt();
        int last_sum_result = 0;
        for (int i = 0; i < n; i++) {
            char type = nextChar();
            switch (type) {
                case '+': {
                    int x = nextInt();
                    insert((x + last_sum_result) % MODULO);
                }
                    break;
                case '-': {
                    int x = nextInt();
                    erase((x + last_sum_result) % MODULO);
                }
                    break;
                case '?': {
                    int x = nextInt();
                    out.println(find((x + last_sum_result) % MODULO) ? "Found" : "Not found");
                }
                    break;
                case 's': {
                    int l = nextInt();
                    int r = nextInt();
                    long res = sum((l + last_sum_result) % MODULO, (r + last_sum_result) % MODULO);
                    out.println(res);
                    last_sum_result = (int) (res % MODULO);
                }
            }
        }
    }

    SetRangeSum() throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(System.out);
        solve();
        out.close();
    }

    public static void main(String[] args) throws IOException {
        new SetRangeSum();
    }

    String nextToken() {
        while (st == null || !st.hasMoreTokens()) {
            try {
                st = new StringTokenizer(br.readLine());
            } catch (Exception e) {
                eof = true;
                return null;
            }
        }
        return st.nextToken();
    }

    int nextInt() throws IOException {
        return Integer.parseInt(nextToken());
    }

    char nextChar() throws IOException {
        return nextToken().charAt(0);
    }

    // my implementation of splay tree (works fine, but not achived fast sum in range)
    static class SplayTree {

        private SplayTreeNode root;

        public void add(int nodeId) {
            // base case: nodeId is first node insert => it becomes root
            if (root == null) {
                root = new SplayTreeNode(nodeId);
                return;
            }

            // finding parent candidate to current node to add
            SplayTreeNode parent = findNodeOrParent(nodeId);

            // if parent id == nodeId this means that current node is already in tree =>
            // nothing else to do
            if (parent.id == nodeId)
                return;

            // adding current node to tree
            SplayTreeNode toAdd = new SplayTreeNode(nodeId);
            toAdd.parent = parent;
            if (toAdd.id < parent.id)
                parent.left = toAdd;
            else
                parent.right = toAdd;

            splay(toAdd);
        }

        public SplayTreeNode find(int id) {
            // if tree is empty return null
            if (root == null)
                return null;

            // starting from root searching current id
            SplayTreeNode last = root;
            while (last.id != id) {
                // if i can't continue to search, the node is missing
                if ((id < last.id && last.left == null) || (id > last.id && last.right == null))
                    return null;

                // if value is < than current node, searhing it on its left side
                if (id < last.id)
                    last = last.left;
                // otherwise checking on right side
                else
                    last = last.right;
            }
            // making found node the root
            splay(last);
            return last;
        }

        public void delete(int id) {
            // deleting in top-down approach
            SplayTreeNode toDelete = findNodeOrParent(id);
            // if node to delete is not present, nothing to do
            if (toDelete == null || toDelete.id != id)
                return;

            // promoting node to delete as root
            splay(toDelete);

            // if node to delete is the only one, remove it and finish
            if (root.isLeaf()) {
                root = null;
                return;
            }

            // splitting current tree in 2 subtrees
            SplayTreeNode leftSubtree = root.left;
            SplayTreeNode rightSubtree = root.right;

            // if there is only one subtree, it becomes the new splay tree
            if (leftSubtree == null) {
                root = rightSubtree;
                rightSubtree.parent = null;
                return;
            }
            if (rightSubtree == null) {
                root = leftSubtree;
                leftSubtree.parent = null;
                return;
            }

            // otherwise splitting current tree in 2 subtrees
            rightSubtree.parent = null;
            leftSubtree.parent = null;

            // new root becomes greather value in left subtree
            SplayTreeNode maxInLeft = leftSubtree;
            while (maxInLeft.right != null)
                maxInLeft = maxInLeft.right;
            // making it the root of its subtree
            splay(maxInLeft);

            // merging the 2 subtrees
            root = maxInLeft;
            root.right = rightSubtree;
            rightSubtree.parent = root;
        }

        public List<SplayTreeNode> rangeSearch(int l, int r) {
            List<SplayTreeNode> result = new ArrayList<>();
            rangeSearch(result, root, l, r);
            return result;
        }

        private void rangeSearch(List<SplayTreeNode> result, SplayTreeNode node, int l, int r) {
            if (node == null)
                return;

            if (node.id > l)
                rangeSearch(result, node.left, l, r);

            if (node.id >= l && node.id <= r)
                result.add(node);

            if (r > node.id)
                rangeSearch(result, node.right, l, r);
        }

        // with this method it is too slow to pass test case 69
        public long rangeSum(int l, int r) {
            if (l > r || root == null)
                return 0;
            return rangeSearch(l, r).stream().mapToLong(n -> n.id).sum();
        }

        private SplayTreeNode findNodeOrParent(int id) {
            if (root == null)
                return null;
            SplayTreeNode last = root;
            while (last.id != id) {
                if ((id < last.id && last.left == null) || (id > last.id && last.right == null))
                    return last;

                if (id < last.id)
                    last = last.left;
                else
                    last = last.right;
            }
            if (last.id == id)
                return last;
            return last.parent != null ? last.parent : last;
        }

        private void splay(SplayTreeNode node) {
            while (node.parent != null) {
                if (node.parent.parent != null)
                    bigRotation(node);
                else
                    smallRotation(node);
            }
            root = node;
        }

        private void bigRotation(SplayTreeNode node) {
            if ((node.id < node.parent.id && node.parent.id < node.parent.parent.id)
                    || (node.id > node.parent.id && node.parent.id > node.parent.parent.id)) {
                // ZIG-ZIG rotation
                smallRotation(node.parent);
                smallRotation(node);
            } else {
                // ZIG-ZAG rotation
                smallRotation(node);
                smallRotation(node);
            }
        }

        private void smallRotation(SplayTreeNode node) {
            if (node.id < node.parent.id)
                rightRotation(node);
            else
                leftRotation(node);
        }

        private void leftRotation(SplayTreeNode node) {
            // in this scenario node > parent (node is right child of the parent)

            SplayTreeNode parent = node.parent;
            parent.right = node.left; // <- parent's right child becomes node's left child
            if (parent.right != null)
                parent.right.parent = parent; // <- updating new parent's right child parent value

            // parent become child of current node
            node.left = parent; // <- node's left child becomes its parent
            node.parent = parent.parent; // <- the parent of current node becomes its grandfather
            parent.parent = node; // <- the parent of the parent becomes current node

        }

        private void rightRotation(SplayTreeNode node) {
            // in this scenario node < parent (node is the left child of the parent)

            SplayTreeNode parent = node.parent;
            parent.left = node.right; // <- parent's left child becomes node's right child
            if (parent.left != null)
                parent.left.parent = parent; // <- updating new parent's left child parent value

            // parent become child of current node
            node.right = parent; // <- node's right child becomes its parent
            node.parent = parent.parent; // <- the parent of current node becomes its grandfather
            parent.parent = node; // <- the parent of the parent becomse current node

        }

    }

    static class SplayTreeNode {
        int id;
        SplayTreeNode left;
        SplayTreeNode right;
        SplayTreeNode parent;

        public SplayTreeNode(int nodeId) {
            this.id = nodeId;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        @Override
        public String toString() {
            return String.valueOf(id);
        }
    }

    // method to test my implementations, it works fine but is too slow for summing
    void test() {
        Random r = new Random();
        char[] queryChars = new char[] { '+', '-', '?', 's' };

        int testCase = 1;
        int maxN = 100_000, maxI = 1_000_000_000;
        List<String> queries = null;
        testLoop: while (true) {
            int n = r.nextInt(maxN - 1) + 1;
            System.out.println(String.format("Start test case: %s, n: %s", testCase, n));
            Set<Integer> naive = new HashSet<>();
            SplayTree tree = new SplayTree();
            queries = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                char q = queryChars[r.nextInt(queryChars.length)];
                if (q == '+') {
                    Integer toAdd = null;
                    if (naive.size() > 0 && r.nextBoolean()) {
                        int randomIndex = r.nextInt(naive.size());
                        Iterator<Integer> iterator = naive.iterator();
                        while (randomIndex-- > 0)
                            iterator.next();
                        toAdd = iterator.next();
                    } else {
                        toAdd = r.nextInt(maxI);
                    }
                    naive.add(toAdd);
                    tree.add(toAdd);
                    queries.add(q + " " + toAdd);
                } else if (q == '-') {
                    Integer toRem = null;
                    if (naive.size() > 0 && r.nextBoolean()) {
                        int randomIndex = r.nextInt(naive.size());
                        Iterator<Integer> iterator = naive.iterator();
                        while (randomIndex-- > 0)
                            iterator.next();
                        toRem = iterator.next();
                    } else {
                        toRem = r.nextInt(maxI);
                    }
                    final int rem = toRem;
                    naive.removeIf(x -> x == rem);
                    tree.delete(rem);
                    queries.add(q + " " + rem);
                } else if (q == '?') {
                    Integer toSearch = null;
                    if (naive.size() > 0 && r.nextBoolean()) {
                        int randomIndex = r.nextInt(naive.size());
                        Iterator<Integer> iterator = naive.iterator();
                        while (randomIndex-- > 0)
                            iterator.next();
                        toSearch = iterator.next();
                    } else {
                        toSearch = r.nextInt(maxI);
                    }
                    final int search = toSearch;
                    Integer found = naive.stream().filter(x -> x == search).findFirst().orElse(null);
                    SplayTreeNode node = tree.find(search);
                    queries.add(q + " " + search);
                    if (found == null && node == null)
                        continue;
                    if (node == null || !found.equals(node.id)) {
                        System.out.println(String.format("Searching value: %s, expected value: %s, actual value: %s",
                                search, found, node));
                        break testLoop;
                    }
                } else {
                    final int a = r.nextInt(maxI), b = r.nextInt(maxI);
                    long sum = naive.stream().mapToLong(x -> x).filter(x -> x >= a && x <= b).sum();
                    long sum2 = tree.rangeSum(a, b);
                    queries.add(q + " " + a + " " + b);
                    if (sum != sum2) {
                        System.out.println(String.format(
                                "Summing values: %s - %s, expected value: %s, actual value: %s", a, b, sum, sum2));
                        break testLoop;
                    }
                }
            }
            testCase++;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(".\\test.txt")))) {
            writer.write(queries.size() + "");
            writer.newLine();
            for (String query : queries) {
                writer.write(query);
                writer.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}