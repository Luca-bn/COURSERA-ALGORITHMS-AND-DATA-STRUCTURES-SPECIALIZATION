import java.util.*;
import java.io.*;

/*
	Task. You are given a rooted binary tree. Build and output its in-order, pre-order and post-order traversals.
	
	Input Format. The first line contains the number of vertices π. The vertices of the tree are numbered
	from 0 to π β 1. Vertex 0 is the root.
	The next π lines contain information about vertices 0, 1, ..., πβ1 in order. Each of these lines contains
	three integers πππ¦π, ππππ‘π and πππβπ‘π β πππ¦π is the key of the π-th vertex, ππππ‘π is the index of the left
	child of the π-th vertex, and πππβπ‘π is the index of the right child of the π-th vertex. If π doesnβt have
	left or right child (or both), the corresponding ππππ‘π or πππβπ‘π (or both) will be equal to β1.
	
	Constraints. 1 β€ π β€ 10^5; 0 β€ πππ¦π β€ 10^9; β1 β€ ππππ‘π, πππβπ‘π β€ π β 1. It is guaranteed that the input
	represents a valid binary tree. In particular, if ππππ‘π ΜΈ= β1 and πππβπ‘π ΜΈ= β1, then ππππ‘π ΜΈ= πππβπ‘π. Also,
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
