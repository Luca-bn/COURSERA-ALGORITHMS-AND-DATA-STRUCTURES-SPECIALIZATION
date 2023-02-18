import java.io.*;
import java.util.*;

/*
    Problem Description
    Task. In this task your goal is to implement a hash table with lists chaining. You are already given the
    number of buckets 𝑚 and the hash function. It is a polynomial hash function
        h(S) = ( Σ︁ from i=0 to |S|-1 of S[i]x^i mod p) mod m
    where 𝑆[𝑖] is the ASCII code of the 𝑖-th symbol of 𝑆, 𝑝 = 1 000 000 007 and 𝑥 = 263. Your program
    should support the following kinds of queries:
    ∙ add string — insert string into the table. If there is already such string in the hash table, then
    just ignore the query.
    ∙ del string — remove string from the table. If there is no such string in the hash table, then
    just ignore the query.
    ∙ find string — output “yes" or “no" (without quotes) depending on whether the table contains
    string or not.
    ∙ check 𝑖 — output the content of the 𝑖-th list in the table. Use spaces to separate the elements of
    the list. If 𝑖-th list is empty, output a blank line.
    When inserting a new string into a hash chain, you must insert it in the beginning of the chain.
    
    Input Format. There is a single integer 𝑚 in the first line — the number of buckets you should have. The
    next line contains the number of queries 𝑁. It’s followed by 𝑁 lines, each of them contains one query
    in the format described above.
    
    Constraints. 1 ≤ 𝑁 ≤ 10^5; 𝑁/5 ≤ 𝑚 ≤ 𝑁. All the strings consist of latin letters. Each of them is non-empty
    and has length at most 15.

    Output Format. Print the result of each of the find and check queries, one result per line, in the same
    order as these queries are given in the input.
*/

// Good job! (Max time used: 0.28/5.00, max memory used: 64823296/2147483648.)
public class HashChains {

    private FastScanner in;
    private PrintWriter out;
    // store all strings in one list
    private LinkedList<String>[] elems;
    // for hash function
    private int bucketCount;
    private int prime = 1000000007;
    private int multiplier = 263;

    public static void main(String[] args) throws IOException {
        new HashChains().processQueries();
    }

    private int hashFunc(String s) {
        long hash = 0;
        for (int i = s.length() - 1; i >= 0; --i)
            hash = (hash * multiplier + s.charAt(i)) % prime;
        return (int)hash % bucketCount;
    }

    private Query readQuery() throws IOException {
        String type = in.next();
        if (!type.equals("check")) {
            String s = in.next();
            return new Query(type, s);
        } else {
            int ind = in.nextInt();
            return new Query(type, ind);
        }
    }

    private void writeSearchResult(boolean wasFound) {
        out.println(wasFound ? "yes" : "no");
    }

    private void processQuery(Query query) {
        Integer hash = null;
        LinkedList<String> chain = null;
        switch (query.type) {
            case "add":
                hash = hashFunc(query.s);
                chain = elems[hash];
                if(chain != null && chain.contains(query.s))
                    return;
                if(chain == null)
                    chain = new LinkedList<>();
                chain.addFirst(query.s);
                elems[hash] = chain;
                break;
            case "del":
                hash = hashFunc(query.s);
                chain = elems[hash];
                if(chain == null)
                    return;
                chain.remove(query.s);
                break;
            case "find":
                hash = hashFunc(query.s);
                chain = elems[hash];
                writeSearchResult(chain != null && chain.contains(query.s));
                break;
            case "check":
                chain = elems[query.ind];
                if(chain == null) {
                    out.println();
                    return;
                }
                boolean isFirst = true;
                for(String s : chain) {
                    if(isFirst) {
                        isFirst = false;
                        out.print(s);
                    }
                    else
                        out.print(" " + s);
                }
                out.println();
                break;
            default:
                throw new RuntimeException("Unknown query: " + query.type);
        }
    }

    public void processQueries() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        bucketCount = in.nextInt();
        elems = new LinkedList[bucketCount];
        int queryCount = in.nextInt();
        for (int i = 0; i < queryCount; ++i) {
            processQuery(readQuery());
        }
        out.close();
    }

    static class Query {
        String type;
        String s;
        int ind;

        public Query(String type, String s) {
            this.type = type;
            this.s = s;
        }

        public Query(String type, int ind) {
            this.type = type;
            this.ind = ind;
        }
    }

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}
