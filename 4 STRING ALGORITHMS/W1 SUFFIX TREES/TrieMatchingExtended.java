import java.io.*;
import java.util.*;

/*
	Input Format. The first line of the input contains a string Text, the second line contains an integer 𝑛,
	each of the following 𝑛 lines contains a pattern from Patterns = {𝑝1, . . . , 𝑝𝑛}.

	Constraints. 1 ≤ |Text| ≤ 10 000; 1 ≤ 𝑛 ≤ 5 000; 1 ≤ |𝑝𝑖| ≤ 100 for all 1 ≤ 𝑖 ≤ 𝑛; all strings contain only
	symbols A, C, G, T; it can be the case that 𝑝𝑖 is a prefix of 𝑝𝑗 for some 𝑖, 𝑗.
	
	Output Format. All starting positions in Text where a string from Patterns appears as a substring in
	increasing order (assuming that Text is a 0-based array of symbols). If more than one pattern
	appears starting at position 𝑖, output 𝑖 once.
*/

// Good job! (Max time used: 1.32/3.00, max memory used: 487727104/2147483648.)
public class TrieMatchingExtended {
	public static void main(String[] args) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String text = in.readLine();
			int n = Integer.parseInt(in.readLine());
			List<String> patterns = new ArrayList<String>();
			for (int i = 0; i < n; i++) {
				patterns.add(in.readLine());
			}

			TrieDataStructure trie = new TrieDataStructure();
			trie.addPatterns(patterns);
			List<Integer> ans = trie.matches(text);

			for (int j = 0; j < ans.size(); j++) {
				System.out.print("" + ans.get(j));
				System.out.print(j + 1 < ans.size() ? " " : "\n");
			}
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	static class TrieDataStructure {
        TrieNode root;
        int insertionIndex = 0;

        TrieDataStructure() {
            this.root = new TrieNode();
            this.root.index = insertionIndex++;
			this.root.patternSoFar = "";
            this.root.children = new HashMap<>();
        }

        void addPatterns(Iterable<String> patterns) {
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
					node.patternSoFar = currentNode.patternSoFar + c;
                    node.children = new HashMap<>();
                    currentNode.children.put(c, node);
                    currentNode = node;
                }
            }
			currentNode.isEndOfPattern = true;
        }

		List<Integer> matches(String text) {
			int originalTextSize = text.length();
			List<Integer> matches = new ArrayList<>();
			while(!text.isEmpty()) {
				String match = prefixMatching(text);
				if(match != null && !match.isEmpty()) {
					matches.add(originalTextSize - text.length());
				}
				text = text.substring(1);
			}
			return matches;
		}

        private String prefixMatching(String text) {
			int textIndex = 0;
			Character symbol = text.charAt(textIndex++);
			TrieNode v = this.root;
			while(true) {
				if(v.children == null || v.children.isEmpty() || v.isEndOfPattern) {
					// match found
					return v.patternSoFar;
				} 
				if(v.children.get(symbol) != null) {
					v = v.children.get(symbol);
					if(textIndex >= text.length()) {
						if(v.children == null || v.children.isEmpty() || v.isEndOfPattern)
							return v.patternSoFar;
						else
							return null;
					}
					symbol = text.charAt(textIndex++);
				} else {
					// no match
					return null;
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
		String patternSoFar;
        Character label;
        Integer parentIndex;
        Integer index;
		boolean isEndOfPattern = false;
        Map<Character, TrieNode> children;

        void print() {
            System.out.println(parentIndex + "->" + index + ":" + label);
            if (children != null)
                for (TrieNode node : children.values()) {
                    node.print();
                }
        }
    }

}
