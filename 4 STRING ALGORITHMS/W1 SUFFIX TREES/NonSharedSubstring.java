import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/*
	Input Format. Strings Text1 and Text2.

	Constraints. 1 ≤ |Text1|, |Text2| ≤ 2 000; strings have equal length (|Text1| = |Text2|), are not equal
	(Text1 ̸= Text2), and contain symbols A, C, G, T only.
	
	Output Format. The shortest (non-empty) substring of Text1 that does not appear in Text2. (Multiple
	solutions may exist, in which case you may return any one.)
*/

// Good job! (Max time used: 3.57/5.00, max memory used: 393580544/4294967296.)
public class NonSharedSubstring {

	public static void main(String[] args) throws IOException {
		FastScanner scanner = new FastScanner();
		String s1 = scanner.next();
		String s2 = scanner.next();

		GeneralizedSuffixTree gst = new GeneralizedSuffixTree(s1, s2);
		// gst.printAllNodes();
		// System.out.println(gst.getAllUncommonSubstringOfText("$0"));
		System.out.println(gst.getShortestUncommonSubstringOfText("$0"));
	}

	static class GeneralizedSuffixTree {
		private static final Integer UNDEFINED_INDEX = -1;
		private static final String TEXT_END = "$";
		Map<String, String> textsMap;
		SuffixTreeNode root;

		GeneralizedSuffixTree(String... texts) {
			this.textsMap = new HashMap<>();
			this.root = new SuffixTreeNode(null, "");
			
			for (int i = 0; i < texts.length; i++) {
				String textKey = TEXT_END + i;
				this.root.indexes.put(textKey, UNDEFINED_INDEX);
				String text = texts[i];
				textsMap.put(textKey, text);
				for (Integer j = 0; j < text.length(); j++) {
					addNode(text.substring(j), textKey, j);
				}
			}
		}

		private void addNode(String text, String textKey, Integer i) {
			SuffixTreeNode parent = findParent(text, textKey);
			String cleanedText = text.substring(parent.textLengthTillHere - parent.label.length());
			String commonPrefix = findCommonPrefix(parent.label, cleanedText);

			SuffixTreeNode child;
			if (commonPrefix.isEmpty()) {
				child = new SuffixTreeNode(parent, cleanedText);
				child.indexes.put(textKey, i);
				parent.children.add(child);
			} else {
				// 1. already exists a node for this text
				if (parent.label.length() == commonPrefix.length() && commonPrefix.length() == cleanedText.length()) {
					parent.indexes.put(textKey, i);
					updateParentsIndex(parent, textKey);
					return;
				}

				// does not exist a node for this text
				if (parent.label.length() != commonPrefix.length()) {
					splitNode(parent, commonPrefix, textKey);
				}
				if (parent.label.length() == commonPrefix.length() && commonPrefix.length() == cleanedText.length()) {
					parent.indexes.put(textKey, i);
					updateParentsIndex(parent, textKey);
					return;
				}
				parent.indexes.put(textKey, UNDEFINED_INDEX);

				child = new SuffixTreeNode(parent, cleanedText.substring(commonPrefix.length()));
				child.indexes.put(textKey, i);
				parent.children.add(child);
			}
			updateParentsIndex(child, textKey);
		}

		private void updateParentsIndex(SuffixTreeNode child, String textKey) {
			child = child.parent;
			while(child != null) {
				if(child.indexes.get(textKey) == null)
					child.indexes.put(textKey, UNDEFINED_INDEX);
				child = child.parent;
			}
		}

		private void splitNode(SuffixTreeNode parent, String commonPrefix, String textKey) {
			String newChildText = parent.label.substring(commonPrefix.length());
			parent.label = commonPrefix;
			parent.textLengthTillHere = parent.parent == null ? 0
					: parent.parent.textLengthTillHere + parent.label.length();

			SuffixTreeNode newChild = new SuffixTreeNode(parent, newChildText);
			newChild.indexes.putAll(parent.indexes);
			for(SuffixTreeNode child : parent.children) {
				child.parent = newChild;
				newChild.children.add(child);
			}

			for (Entry<String, Integer> entry : parent.indexes.entrySet())
				parent.indexes.put(entry.getKey(), UNDEFINED_INDEX);
			parent.children = new ArrayList<>();
			parent.children.add(newChild);
		}

		private SuffixTreeNode findParent(String text, String textKey) {

			SuffixTreeNode lastNode = root;
			boolean stop = false;
			while (!stop) {
				if (lastNode.children.isEmpty())
					return lastNode;

				stop = true;
				// checking for each child if one of them can be a potential parent
				if (lastNode.label.equals(findCommonPrefix(lastNode.label, text.toString()))) {
					for (SuffixTreeNode child : lastNode.children) {
						// found a potential parent
						if (text.length() > lastNode.label.length() &&
								child.label.charAt(0) == text.charAt(lastNode.label.length())) {
							// removing from the input text the current parent label
							text = text.substring(lastNode.label.length());
							lastNode = child;
							stop = false;
							break;
						}
					}
				}
			}
			return lastNode;
		}

		private String findCommonPrefix(String text, String substring) {
			int minLength = Math.min(text.length(), substring.length());
			for (int i = 0; i < minLength; i++) {
				if (text.charAt(i) != substring.charAt(i))
					return substring.substring(0, i);
			}
			return text.substring(0, minLength);
		}

		public String getShortestUncommonSubstringOfText(String textKey) {
			String uncommonText = textsMap.get(textKey);
			LinkedList<SuffixTreeNode> toProcess = new LinkedList<>();
			toProcess.addAll(root.children);
			while(!toProcess.isEmpty()) {
				SuffixTreeNode current = toProcess.pollFirst();
				toProcess.addAll(current.children);
				if(current.indexes.size() == textsMap.size() || current.indexes.get(textKey) == null)
					continue;
				String path = current.reconstructPath();
				int pathWithoutLabel = path.length() - current.label.length();
				for(int i = 1; i <= current.label.length(); i++) {
					if(pathWithoutLabel + i > uncommonText.length())
						break;
					uncommonText = path.substring(0, pathWithoutLabel + i);
				}
			}
			return uncommonText;
		}

		public List<String> getAllUncommonSubstringOfText(String textKey) {
			List<String> uncommonTexts = new ArrayList<>();
			LinkedList<SuffixTreeNode> toProcess = new LinkedList<>();
			toProcess.addAll(root.children);
			while(!toProcess.isEmpty()) {
				SuffixTreeNode current = toProcess.pollFirst();
				toProcess.addAll(current.children);
				if(current.indexes.size() == textsMap.size() || current.indexes.get(textKey) == null)
					continue;
				String path = current.reconstructPath();
				int pathWithoutLabel = path.length() - current.label.length();
				for(int i = 1; i <= current.label.length(); i++) {
					uncommonTexts.add(path.substring(0, pathWithoutLabel + i));
				}
			}
			return uncommonTexts;
		}

		public void printAllNodes() {
			LinkedList<SuffixTreeNode> toProcess = new LinkedList<>();
			toProcess.addAll(root.children);
			while(!toProcess.isEmpty()) {
				SuffixTreeNode current = toProcess.pollFirst();
				System.out.println(current);
				toProcess.addAll(current.children);
			}
		}
	}

	static class SuffixTreeNode {
		String label;
		SuffixTreeNode parent;
		Map<String, Integer> indexes;
		Integer textLengthTillHere;
		List<SuffixTreeNode> children;

		SuffixTreeNode(SuffixTreeNode parent, String label) {
			this.parent = parent;
			this.label = label;
			this.textLengthTillHere = (parent == null ? 0 : parent.textLengthTillHere) + label.length();
			this.indexes = new HashMap<>();
			this.children = new ArrayList<>();
		}

		public String reconstructPath() {
			if (parent == null)
				return label;
			return parent.reconstructPath() + label;
		}

		@Override
		public String toString() {
			return String.format("%s(%s), %s",reconstructPath(), label, indexes.keySet());
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
