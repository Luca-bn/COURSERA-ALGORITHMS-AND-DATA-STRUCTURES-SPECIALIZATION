import java.io.*;
import java.util.*;

/*
    Task. You’re planning a company party. You’d like to invite the coolest people, and you’ve assigned each
    one of them a fun factor — the more the fun factor, the cooler is the person. You want to maximize the
    total fun factor (sum of the fun factors of all the invited people). However, you can’t invite everyone,
    because if the direct boss of some invited person is also invited, it will be awkward. Find out what is
    the maximum possible total fun factor.

    Input Format. The first line contains an integer 𝑛 — the number of people in the company. The next line
    contains 𝑛 numbers 𝑓𝑖 — the fun factors of each of the 𝑛 people in the company. Each of the next 𝑛−1
    lines describes the subordination structure. Everyone but for the CEO of the company has exactly one
    direct boss. There are no cycles: nobody can be a boss of a boss of a ... of a boss of himself. So, the
    subordination structure is a regular tree. Each of the 𝑛 − 1 lines contains two integers 𝑢 and 𝑣, and
    you know that either 𝑢 is the boss of 𝑣 or vice versa (you don’t really need to know which one is the
    boss, but you can invite only one of them or none of them).
    
    Constraints. 1 ≤ 𝑛 ≤ 100 000; 1 ≤ 𝑓𝑖 ≤ 1 000; 1 ≤ 𝑢, 𝑣 ≤ 𝑛; 𝑢 ̸= 𝑣.
    
    Output Format. Output the maximum possible total fun factor of the party (the sum of fun factors of all
    the invited people).
*/

// Good job! (Max time used: 0.47/1.50, max memory used: 70148096/2147483648

class Vertex {
    Vertex() {
        this.weight = 0;
        this.children = new ArrayList<Integer>();
    }

    int weight;
    ArrayList<Integer> children;
}

class PlanParty {
    static int[] d;

    static Vertex[] ReadTree() throws IOException {
        InputStreamReader input_stream = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input_stream);
        StreamTokenizer tokenizer = new StreamTokenizer(reader);

        tokenizer.nextToken();
        int vertices_count = (int) tokenizer.nval;

        Vertex[] tree = new Vertex[vertices_count];

        for (int i = 0; i < vertices_count; ++i) {
            tree[i] = new Vertex();
            tokenizer.nextToken();
            tree[i].weight = (int) tokenizer.nval;
        }

        for (int i = 1; i < vertices_count; ++i) {
            tokenizer.nextToken();
            int from = (int) tokenizer.nval;
            tokenizer.nextToken();
            int to = (int) tokenizer.nval;
            tree[from - 1].children.add(to - 1);
            tree[to - 1].children.add(from - 1);
        }

        return tree;
    }

    static int dfs(Vertex[] tree, int vertex, int parent) {

        if(d[vertex] == -1) {
            if(tree[vertex].children.isEmpty())
                d[vertex] = tree[vertex].weight;
            else {
                int m1 = tree[vertex].weight;
                for(Integer child : tree[vertex].children)
                    if(child != parent)
                        for(Integer grandChild : tree[child].children)
                            if(grandChild != vertex)
                                m1 = m1 + dfs(tree, grandChild, child);
                int m0 = 0;
                for(Integer child : tree[vertex].children)
                    if(child != parent)
                        m0 = m0 + dfs(tree, child, vertex);
                d[vertex] = Math.max(m1, m0);
            }
        }
        return d[vertex];
    }

    static int MaxWeightIndependentTreeSubset(Vertex[] tree) {
        d = new int[tree.length];
        Arrays.fill(d, -1);
        int size = tree.length;
        if (size == 0)
            return 0;
        dfs(tree, 0, -1);
        return Arrays.stream(d).max().getAsInt();
    }

    public static void main(String[] args) throws IOException {
        new Thread(null, new Runnable() {
            public void run() {
                try {
                    new PlanParty().run();
                } catch (IOException e) {
                }
            }
        }, "1", 1 << 26).start();
    }

    public void run() throws IOException {
        Vertex[] tree = ReadTree();
        int weight = MaxWeightIndependentTreeSubset(tree);
        System.out.println(weight);
    }
}
