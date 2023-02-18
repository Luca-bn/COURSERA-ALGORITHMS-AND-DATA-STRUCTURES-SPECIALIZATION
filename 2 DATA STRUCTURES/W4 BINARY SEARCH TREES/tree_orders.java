import java.util.*;
import java.io.*;

/*
	Task. You are given a rooted binary tree. Build and output its in-order, pre-order and post-order traversals.
	
	Input Format. The first line contains the number of vertices 𝑛. The vertices of the tree are numbered
	from 0 to 𝑛 − 1. Vertex 0 is the root.
	The next 𝑛 lines contain information about vertices 0, 1, ..., 𝑛−1 in order. Each of these lines contains
	three integers 𝑘𝑒𝑦𝑖, 𝑙𝑒𝑓𝑡𝑖 and 𝑟𝑖𝑔ℎ𝑡𝑖 — 𝑘𝑒𝑦𝑖 is the key of the 𝑖-th vertex, 𝑙𝑒𝑓𝑡𝑖 is the index of the left
	child of the 𝑖-th vertex, and 𝑟𝑖𝑔ℎ𝑡𝑖 is the index of the right child of the 𝑖-th vertex. If 𝑖 doesn’t have
	left or right child (or both), the corresponding 𝑙𝑒𝑓𝑡𝑖 or 𝑟𝑖𝑔ℎ𝑡𝑖 (or both) will be equal to −1.
	
	Constraints. 1 ≤ 𝑛 ≤ 10^5; 0 ≤ 𝑘𝑒𝑦𝑖 ≤ 10^9; −1 ≤ 𝑙𝑒𝑓𝑡𝑖, 𝑟𝑖𝑔ℎ𝑡𝑖 ≤ 𝑛 − 1. It is guaranteed that the input
	represents a valid binary tree. In particular, if 𝑙𝑒𝑓𝑡𝑖 ̸= −1 and 𝑟𝑖𝑔ℎ𝑡𝑖 ̸= −1, then 𝑙𝑒𝑓𝑡𝑖 ̸= 𝑟𝑖𝑔ℎ𝑡𝑖. Also,
	a vertex cannot be a child of two different vertices. Also, each vertex is a descendant of the root vertex.
	
	Output Format. Print three lines. The first line should contain the keys of the vertices in the in-order
	traversal of the tree. The second line should contain the keys of the vertices in the pre-order traversal
	of the tree. The third line should contain the keys of the vertices in the post-order traversal of the tree.
*/

// Good job! (Max time used: 0.94/12.00, max memory used: 153346048/2147483648.)
public class tree_orders {
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

	public class TreeOrders {
		int n;
		int[] key, left, right;

		void read() throws IOException {
			FastScanner in = new FastScanner();
			n = in.nextInt();
			key = new int[n];
			left = new int[n];
			right = new int[n];
			for (int i = 0; i < n; i++) {
				key[i] = in.nextInt();
				left[i] = in.nextInt();
				right[i] = in.nextInt();
			}
		}

		List<Integer> inOrder() {
			ArrayList<Integer> result = new ArrayList<Integer>();
			inOrderSubtree(result, 0);
			return result;
		}

		private void inOrderSubtree(List<Integer> result, int index) {
			if (left[index] != -1)
				inOrderSubtree(result, left[index]);
			result.add(key[index]);
			if (right[index] != -1)
				inOrderSubtree(result, right[index]);
		}

		List<Integer> preOrder() {
			ArrayList<Integer> result = new ArrayList<Integer>();
			preOrderSubtree(result, 0);
			return result;
		}

		private void preOrderSubtree(List<Integer> result, int index) {
			result.add(key[index]);
			if (left[index] != -1)
				preOrderSubtree(result, left[index]);
			if (right[index] != -1)
				preOrderSubtree(result, right[index]);
		}

		List<Integer> postOrder() {
			ArrayList<Integer> result = new ArrayList<Integer>();
			postOrderSubtree(result, 0);
			return result;
		}

		private void postOrderSubtree(List<Integer> result, int index) {
			if (left[index] != -1)
				postOrderSubtree(result, left[index]);
			if (right[index] != -1)
				postOrderSubtree(result, right[index]);
			result.add(key[index]);
		}
	}

	static public void main(String[] args) throws IOException {
		new Thread(null, new Runnable() {
			public void run() {
				try {
					new tree_orders().run();
				} catch (IOException e) {
				}
			}
		}, "1", 1 << 26).start();
	}

	public void print(List<Integer> x) {
		for (Integer a : x) {
			System.out.print(a + " ");
		}
		System.out.println();
	}

	public void run() throws IOException {
		TreeOrders tree = new TreeOrders();
		tree.read();
		print(tree.inOrder());
		print(tree.preOrder());
		print(tree.postOrder());
	}
}
