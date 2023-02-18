import java.io.*;
import java.util.*;

/*
    Task. You’re in the middle of writing your newspaper’s end-of-year economics summary, and you’ve decided
    that you want to show a number of charts to demonstrate how different stocks have performed over the
    course of the last year. You’ve already decided that you want to show the price of 𝑛 different stocks,
    all at the same 𝑘 points of the year.
    A simple chart of one stock’s price would draw lines between the points (0, 𝑝𝑟𝑖𝑐𝑒0), (1, 𝑝𝑟𝑖𝑐𝑒1), . . . , (𝑘−
    1, 𝑝𝑟𝑖𝑐𝑒𝑘−1), where 𝑝𝑟𝑖𝑐𝑒𝑖 is the price of the stock at the 𝑖-th point in time.
    In order to save space, you have invented the concept of an overlaid chart. An overlaid chart is the
    combination of one or more simple charts, and shows the prices of multiple stocks (simply drawing a
    line for each one). In order to avoid confusion between the stocks shown in a chart, the lines in an
    overlaid chart may not cross or touch.
    Given a list of 𝑛 stocks’ prices at each of 𝑘 time points, determine the minimum number of overlaid
    charts you need to show all of the stocks’ prices.
    
    Input Format. The first line of the input contains two integers 𝑛 and 𝑘 — the number of stocks and the
    number of points in the year which are common for all of them. Each of the next 𝑛 lines contains 𝑘
    integers. The 𝑖-th of those 𝑛 lines contains the prices of the 𝑖-th stock at the corresponding 𝑘 points
    in the year.
    
    Constraints. 1 ≤ 𝑛 ≤ 100; 1 ≤ 𝑘 ≤ 25. All the stock prices are between 0 and 1 000 000.
    
    Output Format. Output a single integer — the minimum number of overlaid charts to visualize all the
    stock price data you have.
*/

// Good job! (Max time used: 0.23/3.00, max memory used: 37363712/2147483648.)
public class StockCharts {
    private FastScanner in;
    private PrintWriter out;

    public static void main(String[] args) throws IOException {
        // new StockCharts().test();
        new StockCharts().solve();
    }

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        int[][] stockData = readData();
        int result = minCharts(stockData);
        writeResponse(result);
        out.close();
    }

    int[][] readData() throws IOException {
        int numStocks = in.nextInt();
        int numPoints = in.nextInt();
        int[][] stockData = new int[numStocks][numPoints];
        for (int i = 0; i < numStocks; ++i)
            for (int j = 0; j < numPoints; ++j)
                stockData[i][j] = in.nextInt();
        return stockData;
    }

    private int minCharts(int[][] stockData) {

        // creating residual graph with source and sink
        int[][] residualGraph = new int[(stockData.length * 2) + 2][];
        for (int i = 0; i < residualGraph.length; i++)
            residualGraph[i] = new int[residualGraph.length];
        for (int i = 1; i <= stockData.length; i++) {
            residualGraph[0][i] = 1;
            residualGraph[stockData.length + i][residualGraph.length - 1] = 1;
        }

        // for(int i = 0; i < residualGraph.length; i++) {
        // for(int j = 0; j < residualGraph.length; j++)
        // System.out.print(residualGraph[i][j] + " ");
        // System.out.println();
        // }

        // adding adjacencies
        for (int i = 0; i < stockData.length; i++) {
            for (int j = 0; j < stockData.length; j++) {
                if (i == j)
                    continue;
                if (compare(stockData[i], stockData[j]))
                    residualGraph[i + 1][stockData.length + j + 1] = 1;
            }
        }

        // performing max capacity algorithm
        int source = 0;
        int sink = residualGraph.length - 1;
        int[] parents = new int[residualGraph.length];
        Arrays.fill(parents, -1);
        int minCap = 0;
        while ((minCap = bfs(residualGraph, source, sink, parents)) != 0) {
            int u = sink;
            while (u != source) {
                int v = parents[u];
                residualGraph[u][v] += minCap;
                residualGraph[v][u] -= minCap;
                u = v;
            }
        }

        // System.out.println("======");
        // for(int i = 0; i < residualGraph.length; i++) {
        // for(int j = 0; j < residualGraph.length; j++)
        // System.out.print(residualGraph[i][j] + " ");
        // System.out.println();
        // }

        int charts = stockData.length;
        for (int i = 0; i < residualGraph.length; i++)
            if (residualGraph[residualGraph.length - 1][i] == 1)
                charts--;

        return charts;
    }

    private int bfs(int[][] adjMatrix, int source, int sink, int[] parents) {
        Arrays.fill(parents, -1);
        parents[source] = -2;
        PriorityQueue<Integer[]> queue = new PriorityQueue<>((a1, a2) -> a2[1].compareTo(a1[1]));
        queue.add(new Integer[] { source, Integer.MAX_VALUE });
        while (!queue.isEmpty()) {
            int u = queue.peek()[0];
            int capacity = queue.peek()[1];
            queue.poll();
            for (int av = 0; av < adjMatrix.length; av++) {
                if (u != av && parents[av] == -1 && adjMatrix[u][av] != 0) {
                    parents[av] = u;
                    int minCap = Math.min(capacity, adjMatrix[u][av]);
                    if (av == sink)
                        return minCap;
                    queue.add(new Integer[] { av, minCap });
                }
            }
        }
        return 0;
    }

    boolean compare(int[] stock1, int[] stock2) {
        for (int i = 0; i < stock1.length; ++i)
            if (stock1[i] >= stock2[i])
                return false;
        return true;
    }

    private void writeResponse(int result) {
        out.println(result);
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

    private void test() throws IOException {
        String filePath = "C:\\Users\\ut1257\\Desktop\\PERSONALI\\corsi\\Algoritmi e strutture dati\\advanced-algorithms-and-complexity\\week1\\stock_charts\\tests";
        boolean hasError = false;
        for (int i = 1; i <= 36; i++) {
            try (BufferedReader requestReader = new BufferedReader(
                    new FileReader(new File(filePath + "\\" + (i < 10 ? ("0" + i) : i))));
                    Scanner answerReader = new Scanner(new File(filePath + "\\" + (i < 10 ? ("0" + i) : i) + ".a"))) {

                StringTokenizer st = new StringTokenizer(requestReader.readLine());
                int numStocks = Integer.parseInt(st.nextToken());
                int numPoints = Integer.parseInt(st.nextToken());
                int[][] stockData = new int[numStocks][numPoints];
                for (int n = 0; n < numStocks; ++n) {
                    st = new StringTokenizer(requestReader.readLine());
                    for (int j = 0; j < numPoints; ++j)
                        stockData[n][j] = Integer.parseInt(st.nextToken());
                }
                int result = minCharts(stockData);
                int answer = answerReader.nextInt();
                if (result != answer) {
                    hasError = true;
                    System.out.println(i + ": KO -> "+ result + " " + answer);
                }
            }
        }
        if(!hasError)
            System.out.println("OK");
    }
}
