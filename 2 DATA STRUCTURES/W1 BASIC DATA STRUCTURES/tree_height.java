import java.io.*;
import java.util.*;

/*
	Problem Description
	Task. You are given a description of a rooted tree. Your task is to compute and output its height. Recall
	that the height of a (rooted) tree is the maximum depth of a node, or the maximum distance from a
	leaf to the root. You are given an arbitrary tree, not necessarily a binary tree.

	Input Format. The first line contains the number of nodes π. The second line contains π integer numbers
	from β1 to π β 1 β parents of nodes. If the π-th one of them (0 β€ π β€ π β 1) is β1, node π is the root,
	otherwise itβs 0-based index of the parent of π-th node. It is guaranteed that there is exactly one root.
	It is guaranteed that the input represents a tree.

	Constraints. 1 β€ π β€ 10^5.

	Output Format. If the code in π uses brackets correctly, output βSuccess" (without the quotes). Otherwise,
	output the 1-based index of the first unmatched closing bracket, and if there are no unmatched closing
	brackets, output the 1-based index of the first unmatched opening bracket.
*/

// Good job! (Max time used: 0.25/6.00, max memory used: 56918016/2147483648.)
public class tree_height {
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

	public class TreeHeight {
		int n;
		Node[] nodes;
		Node root;

		void read() throws IOException {
			FastScanner in = new FastScanner();
			n = in.nextInt();
			nodes = new Node[n];
			for(int i = 0; i < n; i++)
				nodes[i] = new Node(i);
			for (int i = 0; i < n; i++) {
				int parent = in.nextInt();
				if(parent == -1)
					root = nodes[i];
				else
					nodes[parent].children.add(nodes[i]);
			}
		}

		int computeHeight() {
			return root.computeHeight();
		}
	}

	static public void main(String[] args) throws IOException {
		new Thread(null, new Runnable() {
			public void run() {
				try {
					new tree_height().run();
				} catch (IOException e) {
				}
			}
		}, "1", 1 << 26).start();
	}

	public void run() throws IOException {
		TreeHeight tree = new TreeHeight();
		tree.read();
		System.out.println(tree.computeHeight());
	}

	static class Node {
		int id;
		int depth = 1;
		List<Node> children = new ArrayList<>();

		Node(int id) {
			this.id = id;
		}

		int computeHeight() {
			int height = 1;
			Stack<Node> stack = new Stack<>();
			for(Node child : children) {
				child.depth = depth + 1;
				stack.add(child);
			}
			while(!stack.isEmpty()) {
				Node current = stack.pop();
				for(Node child : current.children) {
					child.depth = current.depth + 1;
					stack.add(child);
				}
				height = Math.max(height, current.depth);
			}
			return height;
		}
	}
}