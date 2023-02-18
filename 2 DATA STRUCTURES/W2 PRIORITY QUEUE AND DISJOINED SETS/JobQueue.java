import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/*
    Problem Description
    Task. You have a program which is parallelized and uses 𝑛 independent threads to process the given list of 𝑚
    jobs. Threads take jobs in the order they are given in the input. If there is a free thread, it immediately
    takes the next job from the list. If a thread has started processing a job, it doesn’t interrupt or stop
    until it finishes processing the job. If several threads try to take jobs from the list simultaneously, the
    thread with smaller index takes the job. For each job you know exactly how long will it take any thread
    to process this job, and this time is the same for all the threads. You need to determine for each job
    which thread will process it and when will it start processing.
    
    Input Format. The first line of the input contains integers 𝑛 and 𝑚.
    The second line contains 𝑚 integers 𝑡𝑖 — the times in seconds it takes any thread to process 𝑖-th job.
    The times are given in the same order as they are in the list from which threads take jobs.
    Threads are indexed starting from 0.

    Constraints. 1 ≤ 𝑛 ≤ 10^5; 1 ≤ 𝑚 ≤ 10^5; 0 ≤ 𝑡𝑖 ≤ 10^9.

    Output Format. Output exactly 𝑚 lines. 𝑖-th line (0-based index is used) should contain two spaceseparated
    integers — the 0-based index of the thread which will process the 𝑖-th job and the time
    in seconds when it will start processing that job.
*/

// Good job! (Max time used: 0.45/8.00, max memory used: 79433728/2147483648.)
public class JobQueue {
    private int numWorkers;
    private int[] jobs;

    private int[] assignedWorker;
    private long[] startTime;

    private FastScanner in;
    private PrintWriter out;

    public static void main(String[] args) throws IOException {
        new JobQueue().solve();
    }

    private void readData() throws IOException {
        numWorkers = in.nextInt();
        int m = in.nextInt();
        jobs = new int[m];
        assignedWorker = new int[m];
        startTime = new long[m];
        for (int i = 0; i < m; ++i) {
            jobs[i] = in.nextInt();
        }
    }

    private void writeResponse() {
        for (int i = 0; i < jobs.length; ++i) {
            out.println(assignedWorker[i] + " " + startTime[i]);
        }
    }

    private void assignJobs() {
        MinHeap heap = new MinHeap();
        heap.build(numWorkers);
        for (int i = 0; i < jobs.length; i++) {
            int job = jobs[i];
            Thread thread = heap.getMin();
            assignedWorker[i] = thread.index;
            startTime[i] = thread.endTime;
            heap.changePriority(0, thread.endTime + job);
        }
    }

    class MinHeap {

        public List<Thread> heap = new ArrayList<>();

        public void build(int threads) {
            for (int i = 0; i < threads; i++)
                heap.add(new Thread(i, 0));
            for (int i = threads - 1; i >= 0; i--)
                shiftDown(i);
        }

        public Integer parentindex(int i) {
            if (i == 0)
                return -1;
            return (i - 1) / 2;
        }

        public Integer leftChildIndex(int i) {
            if (i >= heap.size())
                return -1;
            return (i * 2) + 1;
        }

        public Integer rightChildIndex(int i) {
            if (i >= heap.size())
                return -1;
            return (i * 2) + 2;
        }

        public Thread parent(int i) {
            if (i == 0)
                return null;
            return heap.get((i - 1) / 2);
        }

        public Thread leftChild(int i) {
            if (((i * 2) + 1) >= heap.size())
                return null;
            return heap.get((i * 2) + 1);
        }

        public Thread rightChild(int i) {
            if (((i * 2) + 2) >= heap.size())
                return null;
            return heap.get((i * 2) + 2);
        }

        private void shiftUp(int i) {
            Thread parent = null;
            Integer parentIndex = null;
            while ((parent = parent(i)) != null) {
                parentIndex = parentindex(i);
                if (parent.endTime > heap.get(i).endTime
                        || (parent.endTime == heap.get(i).endTime && parent.index > heap.get(i).index)) {
                    swap(i, parentIndex);
                }
                i = parentIndex;
            }
        }

        private void shiftDown(int i) {
            Thread node = heap.get(i);
            Thread leftChild = leftChild(i);
            Thread rightChild = rightChild(i);

            int indexToSwap = -1;
            if (leftChild != null && rightChild != null) {
                if (leftChild.endTime > rightChild.endTime)
                    indexToSwap = rightChildIndex(i);
                else if (leftChild.endTime == rightChild.endTime) {
                    if (leftChild.index < rightChild.index)
                        indexToSwap = leftChildIndex(i);
                    else
                        indexToSwap = rightChildIndex(i);
                } else
                    indexToSwap = leftChildIndex(i);
            } else if (leftChild != null)
                indexToSwap = leftChildIndex(i);

            if (indexToSwap != -1 && (node.endTime > heap.get(indexToSwap).endTime
                    || (node.endTime == heap.get(indexToSwap).endTime && node.index > heap.get(indexToSwap).index))) {
                swap(i, indexToSwap);
                shiftDown(indexToSwap);
            }
        }

        private void swap(int index1, int index2) {
            Thread tmp = heap.get(index1);
            heap.set(index1, heap.get(index2));
            heap.set(index2, tmp);
        }

        public void remove(int i) {
            Thread t = heap.get(i);
            t.endTime = Integer.MIN_VALUE;
            heap.set(i, t);
            shiftUp(i);
            extractMin();
        }

        public Thread extractMin() {
            Thread result = heap.get(0);
            heap.set(0, heap.get(heap.size() - 1));
            heap.remove(heap.size() - 1);
            shiftDown(0);
            return result;
        }

        public Thread getMin() {
            return heap.get(0);
        }

        public void insert(Thread n) {
            heap.add(n);
            shiftUp(heap.size() - 1);
        }

        public void changePriority(int i, long p) {
            Thread t = heap.get(i);
            long old = t.endTime;
            t.endTime = p;
            if (p < old)
                shiftUp(i);
            else
                shiftDown(i);
        }
    }

    class Thread {
        int index;
        long endTime;

        Thread(int index, int endTime) {
            this.endTime = endTime;
            this.index = index;
        }
    }

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        readData();
        assignJobs();
        writeResponse();
        out.close();
    }

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() throws FileNotFoundException {
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
