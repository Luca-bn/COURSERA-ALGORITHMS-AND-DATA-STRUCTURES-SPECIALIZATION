import java.io.*;
import java.util.*;

/*
	Problem Description
	Task. You are given a string 𝑆 and you have to process 𝑛 queries. Each query is described by three integers
	𝑖, 𝑗, 𝑘 and means to cut substring 𝑆[𝑖..𝑗] (𝑖 and 𝑗 are 0-based) from the string and then insert it after the
	𝑘-th symbol of the remaining string (if the symbols are numbered from 1). If 𝑘 = 0, 𝑆[𝑖..𝑗] is inserted
	in the beginning. See the examples for further clarification.
	
	Input Format. The first line contains the initial string 𝑆.
	The second line contains the number of queries 𝑞.
	Next 𝑞 lines contain triples of integers 𝑖, 𝑗, 𝑘.
	
	Constraints. 𝑆 contains only lowercase english letters. 1 ≤ |𝑆| ≤ 300 000; 1 ≤ 𝑞 ≤ 100 000; 0 ≤ 𝑖 ≤ 𝑗 ≤
	𝑛 − 1; 0 ≤ 𝑘 ≤ 𝑛 − (𝑗 − 𝑖 + 1).
	
	Output Format. Output the string after all 𝑞 queries.
*/

// Good job! (Max time used: 0.73/6.00, max memory used: 91258880/2147483648.)
class RopeProblem {

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

	void update(Vertex v) {
		if (v == null)
			return;
		v.size = 1 + (v.left != null ? v.left.size : 0) + (v.right != null ? v.right.size : 0);
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

	static class VertexPair {
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
		if (key > root.size) {
			return null;
		}
		Vertex v = root;
		Vertex last = root;
		Vertex next = null;
		while (v != null) {
			last = v;
			int lSum = 0;
			if (v.left != null) {
				lSum = v.left.size;
			}
			if (key == lSum + 1) {
				next = v;
				break;
			} else if (lSum < key) {
				v = v.right;
				key -= lSum + 1;
			} else {
				v = v.left;
			}
		}
		root = splay(last);
		return new VertexPair(next, root);
	}

	VertexPair split(Vertex root, int key) {
		if (root == null) {
			return new VertexPair(null, null);
		}
		VertexPair result = new VertexPair();
		VertexPair findAndRoot = find(root, key);
		if (findAndRoot != null) {
			root = findAndRoot.right;
			result.right = findAndRoot.left;
		}
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

	public static void main(String[] args) throws IOException {
		new RopeProblem().run();
	}

	public void run() throws IOException {
		FastScanner in = new FastScanner();
		PrintWriter out = new PrintWriter(System.out);
		Rope rope = new Rope(in.next());
		for (int q = in.nextInt(); q > 0; q--) {
			int i = in.nextInt();
			int j = in.nextInt();
			int k = in.nextInt();
			rope.process(i, j, k);
		}
		out.println(rope.result());
		out.close();
	}

	class Rope {
		Vertex root;

		Rope(String s) {
			root = constructVertex(s);
		}

		void process(int i, int j, int k) {
			VertexPair leftMid = split(root, i + 1);
			VertexPair midRight = split(leftMid.right, j - i + 2);
			Vertex mid = midRight.left;
			root = merge(leftMid.left, midRight.right);
			VertexPair leftRight = split(root, k + 1);
			root = merge(leftRight.left, mid);
			root = merge(root, leftRight.right);
		}

		String result() {
			Vertex node = root;
			Stack<Vertex> stack = new Stack<>();
			StringBuilder sb = new StringBuilder("");
			while (node != null) {
				stack.push(node);
				node = node.left;
			}
			while (!stack.empty()) {
				node = stack.pop();
				sb.append(node.c);
				if (node.right != null) {
					node = node.right;

					while (node != null) {
						stack.push(node);
						node = node.left;
					}
				}
			}
			return sb.toString();
		}
	}

	private static Vertex constructVertex(String s) {
		Vertex root = null;
		Vertex prev = null;
		int n = s.length();
		for (int i = 0; i < n; i++) {
			Vertex v = new Vertex(n - i, s.charAt(i), null, null, prev);
			if (prev == null) {
				root = v;
			} else {
				prev.right = v;
			}
			prev = v;
		}
		return root;
	}

	static class Vertex {
		char c;
		int size;
		Vertex left;
		Vertex right;
		Vertex parent;

		Vertex(int size, char c, Vertex left, Vertex right, Vertex parent) {
			this.size = size;
			this.c = c;
			this.left = left;
			this.right = right;
			this.parent = parent;
		}
	}
}