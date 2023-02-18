import java.util.*;
import java.io.*;
import java.math.BigInteger;

/*
	Problem Description
	Input Format. The first line contains a string 𝑠 consisting of small Latin letters. The second line contains
	the number of queries 𝑞. Each of the next 𝑞 lines specifies a query by three integers 𝑎, 𝑏, and 𝑙.
	
	Constraints. 1 ≤ |𝑠| ≤ 500 000. 1 ≤ 𝑞 ≤ 100 000. 0 ≤ 𝑎, 𝑏 ≤ |𝑠| − 𝑙 (hence the indices 𝑎 and 𝑏 are 0-based).
	
	Output Format. For each query, output “Yes” if 𝑠𝑎𝑠𝑎+1. . .𝑠𝑎+𝑙−1 = 𝑠𝑏𝑠𝑏+1. . .𝑠𝑏+𝑙−1 are equal, and “No”
	otherwise.
*/

// Good job! (Max time used: 0.88/2.00, max memory used: 336297984/2147483648.)
public class substring_equality {

	static public void main(String[] args) throws Exception {
		// new substring_equality().stressTestHuge();
		// new substring_equality().test();
		// new substring_equality().testExactlyCase();
		new substring_equality().run();
	}

	public class Solver {
		private long x = 263, m1 = 1000000007, m2 = 1000000009;
		private long[] h1;
		private long[] h2;
		private long[] powTable1;
		private long[] powTable2;
		private String s;

		public Solver(String s) {
			this.s = s;
			preprocessHashes();
		}

		private void preprocessHashes() {
			h1 = hashTable(m1, x);
			h2 = hashTable(m2, x);
			powTable1 = new long[s.length() + 1];
			powTable2 = new long[s.length() + 1];
			BigInteger bigX = new BigInteger(String.valueOf(x));
			BigInteger bigM1 = new BigInteger(String.valueOf(m1));
			BigInteger bigM2 = new BigInteger(String.valueOf(m2));
			powTable1[0] = powTable2[0] = 1;
			for(int i = 1; i < powTable1.length; i++) {
				powTable1[i] = new BigInteger(String.valueOf(powTable1[i - 1])).multiply(bigX).mod(bigM1).longValue();
				powTable2[i] = new BigInteger(String.valueOf(powTable2[i - 1])).multiply(bigX).mod(bigM2).longValue();
			}
		}

		private long[] hashTable(long prime, long x) {
			long[] hashTable = new long[s.length() + 1];
			hashTable[0] = 0;
			for(int i = 1; i < s.length() + 1; i++)
				hashTable[i] = mod(mod(hashTable[i - 1] * x, prime) + mod(s.charAt(i - 1), prime), prime);
			return hashTable;
		}

		private long hashValue(long[] hashTable, long[] powTable, long prime, long x, int start, int length) {
			return mod(hashTable[start  + length] - powTable[length] * hashTable[start], prime);
		}

		public boolean ask(int a, int b, int l) throws Exception {
			long ha1 = hashValue(h1, powTable1, m1, x, a, l);
			long ha2 = hashValue(h2, powTable2, m2, x, a, l);
			long hb1 = hashValue(h1, powTable1, m1, x, b, l);
			long hb2 = hashValue(h2, powTable2, m2, x, b, l);
			return ha1 == hb1 && hb2 == ha2;
		}

		private long mod(long a, long mod) {
			return ((a % mod) + mod) % mod;
		}
	}

	// huge stress test, generate random strings and calls "textExactlyCase" for every one of them
	private void stressTestHuge() throws Exception {
		char[] chars = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
		'K', 'L', 'M', 'N', 'O', 'P', 'Q',
		'R', 'S', 'T', 'U', 'W', 'X', 'Y', 'Z',
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
		'p', 'q', 'r', 's', 't', 'u',
		'w', 'x', 'y', 'z' };
		// char[] chars = new char[] { 'A', 'B' };
		int maxLength = 10_000;
		Random r = new Random();

		int testCase = 1;
		String text = null;
		String currentQuery = null;
		while (true) {
			System.out.println("Start test case: " + testCase);
			int length = r.nextInt(maxLength - 1) + 1;
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < length; i++)
				sb.append(chars[r.nextInt(chars.length)]);
			text = sb.toString();
			try {
				testExactlyCase(text);
			} catch (Exception e) {
				currentQuery = e.getMessage().substring(6);
				e.printStackTrace();
				break;
			}
			testCase++;
		}

		// writing test case
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(".\\test.txt")))) {
			writer.write(text);
			writer.newLine();
			writer.write("1");
			writer.newLine();
			writer.write(currentQuery);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// verify if for a given text, the algorithm is correct for every a, b and l
	private void testExactlyCase(String toTest) throws Exception {
		Solver solver = new Solver(toTest);
		for(int a = 0; a < toTest.length(); a++) {
			System.out.println("remainging: " + (toTest.length() - a));
			for(int b = a; b < toTest.length(); b++) {
				for(int l = toTest.length() - Math.max(a, b); l > 0; l--) {
					if(toTest.substring(a, a + l).equals(toTest.substring(b, b + l)) != solver.ask(a, b, l))
						throw new Exception("KO on " + a + " " + b + " " + l);
				}
			}
		}
	}

	// generate some random tests
	public void test() throws Exception {
		char[] chars = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
		'K', 'L', 'M', 'N', 'O', 'P', 'Q',
		'R', 'S', 'T', 'U', 'W', 'X', 'Y', 'Z',
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
		'p', 'q', 'r', 's', 't', 'u',
		'w', 'x', 'y', 'z' };
		// char[] chars = new char[] { 'A', 'B' };
		int maxLength = 500_000, maxQ = 100_000;
		Random r = new Random();
		boolean fail = false;

		int testCase = 1;
		String text = null;
		String currentQuery = null;
		while (!fail) {
			System.out.println("Start test case: " + testCase);
			int length = r.nextInt(maxLength - 1) + 1;
			int q = r.nextInt(maxQ - 1) + 1;
			// building string
			StringBuilder sb = new StringBuilder();
			while (sb.length() < length)
				sb.append(chars[r.nextInt(chars.length)]);
			text = sb.toString();
			// creating queries
			int trueCount = 0;
			int maxL = 0;
			Solver solver = new Solver(text);
			for (int i = 1; i <= q; i++) {
				int a = r.nextInt(length), b = r.nextInt(length), l = r.nextInt(Math.min(length - Math.max(a, b), 5));
				maxL = Math.max(maxL, l);
				currentQuery = a + " " + b + " " + l;
				boolean areEquals = text.substring(a, a + l).equals(text.substring(b, b + l));
				boolean result = solver.ask(a, b, l);
				if (areEquals)
					trueCount++;
				if (areEquals != result) {
					fail = true;
					System.out.println("Failed on test case: " + testCase);
					System.out.println(String.format("expected value: %s, returned value: %s", areEquals, result));
					break;
				}
			}
			System.out.println(String.format("Text length: %s, queries: %s, max l: %s, No cases: %s, Yes cases: %s", length, q, maxL,
					q - trueCount, trueCount));
			testCase++;
		}

		// writing test case
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(".\\test.txt")))) {
			writer.write(text);
			writer.newLine();
			writer.write("1");
			writer.newLine();
			writer.write(currentQuery);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() throws Exception {
		FastScanner in = new FastScanner();
		PrintWriter out = new PrintWriter(System.out);
		String s = in.next();
		int q = in.nextInt();
		Solver solver = new Solver(s);
		for (int i = 0; i < q; i++) {
			int a = in.nextInt();
			int b = in.nextInt();
			int l = in.nextInt();
			boolean sol = solver.ask(a, b, l);
			out.println(sol ? "Yes" : "No");
		}
		out.close();
	}

	class FastScanner {
		StringTokenizer tok = new StringTokenizer("");
		BufferedReader in;

		FastScanner() throws FileNotFoundException {
			// in = new BufferedReader(new FileReader(new File(".\\test.txt")));
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
