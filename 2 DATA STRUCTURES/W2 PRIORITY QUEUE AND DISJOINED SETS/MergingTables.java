import java.io.*;
import java.util.*;

/*
    Problem Description
    Task. There are π tables stored in some database. The tables are numbered from 1 to π. All tables share
    the same set of columns. Each table contains either several rows with real data or a symbolic link to
    another table. Initially, all tables contain data, and π-th table has ππ rows. You need to perform π of
    the following operations:
    1. Consider table number πππ π‘ππππ‘ππππ. Traverse the path of symbolic links to get to the data. That is,
    while πππ π‘ππππ‘ππππ contains a symbolic link instead of real data do
    πππ π‘ππππ‘ππππ β symlink(πππ π‘ππππ‘ππππ)
    2. Consider the table number π ππ’ππππ and traverse the path of symbolic links from it in the same
    manner as for πππ π‘ππππ‘ππππ.
    3. Now, πππ π‘ππππ‘ππππ and π ππ’ππππ are the numbers of two tables with real data. If πππ π‘ππππ‘ππππ ΜΈ=
    π ππ’ππππ, copy all the rows from table π ππ’ππππ to table πππ π‘ππππ‘ππππ, then clear the table π ππ’ππππ
    and instead of real data put a symbolic link to πππ π‘ππππ‘ππππ into it.
    4. Print the maximum size among all π tables (recall that size is the number of rows in the table).
    If the table contains only a symbolic link, its size is considered to be 0.
    See examples and explanations for further clarifications.
    
    Input Format. The first line of the input contains two integers π and π β the number of tables in the
    database and the number of merge queries to perform, respectively.
    The second line of the input contains π integers ππ β the number of rows in the π-th table.
    Then follow π lines describing merge queries. Each of them contains two integers πππ π‘ππππ‘ππππ and
    π ππ’ππππ β the numbers of the tables to merge.

    Constraints. 1 β€ π,π β€ 100 000; 0 β€ ππ β€ 10 000; 1 β€ πππ π‘ππππ‘ππππ, π ππ’ππππ β€ π.

    Output Format. For each query print a line containing a single integer β the maximum of the sizes of all
    tables (in terms of the number of rows) after the corresponding operation.
*/

// Good job! (Max time used: 1.00/14.00, max memory used: 185470976/2147483648.)
public class MergingTables {
    private final InputReader reader;
    private final OutputWriter writer;

    public MergingTables(InputReader reader, OutputWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public static void main(String[] args) {
        InputReader reader = new InputReader(System.in);
        OutputWriter writer = new OutputWriter(System.out);
        new MergingTables(reader, writer).run();
        writer.writer.flush();
    }

    class Table {
        Table parent;
        int rank;
        int numberOfRows;

        Table(int numberOfRows) {
            this.numberOfRows = numberOfRows;
            rank = 0;
            parent = this;
        }
        Table getParent() {
            if(parent != this)
                parent = parent.getParent();
            return parent;
        }
    }

    int maximumNumberOfRows = -1;

    void merge(Table destination, Table source) {
        // merge two components here
        // use rank heuristic
        // update maximumNumberOfRows
        Table realDestination = destination.getParent();
        Table realSource = source.getParent();
        if (realDestination == realSource) {
            return;
        }
        if(realDestination.rank > realSource.rank) {
            realSource.parent = realDestination;
            realDestination.numberOfRows = realDestination.numberOfRows + realSource.numberOfRows;
            maximumNumberOfRows = Math.max(maximumNumberOfRows, realDestination.numberOfRows);
            realSource.numberOfRows = 0;
        } else {
            realDestination.parent = realSource;
            realSource.numberOfRows = realSource.numberOfRows + realDestination.numberOfRows;
            realDestination.numberOfRows = 0;
            maximumNumberOfRows = Math.max(maximumNumberOfRows, realSource.numberOfRows);
            if(realSource.rank == realDestination.rank) {
                realSource.rank = realSource.rank+1;
            }
        }
    }

    public void run() {
        int n = reader.nextInt();
        int m = reader.nextInt();
        Table[] tables = new Table[n];
        for (int i = 0; i < n; i++) {
            int numberOfRows = reader.nextInt();
            tables[i] = new Table(numberOfRows);
            maximumNumberOfRows = Math.max(maximumNumberOfRows, numberOfRows);
        }
        for (int i = 0; i < m; i++) {
            int destination = reader.nextInt() - 1;
            int source = reader.nextInt() - 1;
            merge(tables[destination], tables[source]);
            writer.printf("%d\n", maximumNumberOfRows);
        }
    }


    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public double nextDouble() {
            return Double.parseDouble(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    static class OutputWriter {
        public PrintWriter writer;

        OutputWriter(OutputStream stream) {
            writer = new PrintWriter(stream);
        }

        public void printf(String format, Object... args) {
            writer.print(String.format(Locale.ENGLISH, format, args));
        }
    }
}
