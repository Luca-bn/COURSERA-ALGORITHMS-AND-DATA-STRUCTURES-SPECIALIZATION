import java.util.*;
import java.io.*;

/*
    Problem Description

    Task. Implement a stack supporting the operations Push(), Pop(), and Max().

    Input Format. The first line of the input contains the number 𝑞 of queries. Each of the following 𝑞 lines
    specifies a query of one of the following formats: push v, pop, or max.

    Constraints. 1 ≤ 𝑞 ≤ 400 000, 0 ≤ 𝑣 ≤ 10^5.
    
    Output Format. For each max query, output (on a separate line) the maximum value of the stack.
*/

// Good job! (Max time used: 0.61/5.00, max memory used: 75415552/2147483648.)
public class StackWithMax {
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

    public void solve() throws IOException {
        FastScanner scanner = new FastScanner();
        int queries = scanner.nextInt();
        ConcreteStack stack = new ConcreteStack();

        for (int qi = 0; qi < queries; ++qi) {
            String operation = scanner.next();
            if ("push".equals(operation)) {
                int value = scanner.nextInt();
                stack.push(value);
            } else if ("pop".equals(operation)) {
                stack.pop();
            } else if ("max".equals(operation)) {
                System.out.println(stack.max());
            }
        }
    }

    static public void main(String[] args) throws IOException {
        new StackWithMax().solve();
    }
}

class ConcreteStack extends AbstractCollection<Integer> {

    private Stack<Integer> maximum = new Stack<>();
    private Stack<Integer> data = new Stack<>();

    @Override
    public Iterator<Integer> iterator() {
        return data.iterator();
    }

    @Override
    public int size() {
        return data.size();
    }

    public void push(Integer element) {
        if (maximum.isEmpty())
            maximum.add(element);
        else
            maximum.add(Math.max(maximum.peek(), element));
        data.add(element);
    }

    public void pop() {
        maximum.pop();
        data.pop();
    }

    public Integer max() {
        return maximum.peek();
    }

}
