import java.io.*;
import java.util.*;

/*
  Problem Description
  Task. The first step of the HeapSort algorithm is to create a heap from the array you want to sort. By the
  way, did you know that algorithms based on Heaps are widely used for external sort, when you need
  to sort huge files that donβt fit into memory of a computer?
  Your task is to implement this first step and convert a given array of integers into a heap. You will
  do that by applying a certain number of swaps to the array. Swap is an operation which exchanges
  elements ππ and ππ of the array π for some π and π. You will need to convert the array into a heap using
  only π(π) swaps, as was described in the lectures. Note that you will need to use a min-heap instead
  of a max-heap in this problem.
  
  Input Format. The first line of the input contains single integer π. The next line contains π space-separated
  integers ππ.

  Constraints. 1 β€ π β€ 100 000; 0 β€ π, π β€ π β 1; 0 β€ π0, π1, . . . , ππβ1 β€ 10^9. All ππ are distinct.
  
  Output Format. The first line of the output should contain single integer π β the total number of swaps.
  π must satisfy conditions 0 β€ π β€ 4π. The next π lines should contain the swap operations used
  to convert the array π into a heap. Each swap is described by a pair of integers π, π β the 0-based
  indices of the elements to be swapped. After applying all the swaps in the specified order the array
  must become a heap, that is, for each π where 0 β€ π β€ π β 1 the following conditions must be true:
  1. If 2π + 1 β€ π β 1, then ππ < π2π+1.
  2. If 2π + 2 β€ π β 1, then ππ < π2π+2.
  Note that all the elements of the input array are distinct. Note that any sequence of swaps that has
  length at most 4π and after which your initial array becomes a correct heap will be graded as correct.
*/

// Good job! (Max time used: 0.30/3.00, max memory used: 68149248/2147483648.)
public class BuildHeap {
  private List<Integer> data;
  private List<Swap> swaps;

  private FastScanner in;
  private PrintWriter out;

  public static void main(String[] args) throws IOException {
    new BuildHeap().solve();
  }

  private void readData() throws IOException {
    int n = in.nextInt();
    data = new ArrayList<>();
    for (int i = 0; i < n; ++i) {
      data.add(in.nextInt());
    }
  }

  private void writeResponse() {
    out.println(swaps.size());
    for (Swap swap : swaps) {
      out.println(swap.index1 + " " + swap.index2);
    }
  }

  private void generateSwaps() {

    MinHeap heap = new MinHeap();
    heap.build(data);
    swaps = heap.getSwaps();
  }

  class MinHeap {

    private List<Swap> swaps = new ArrayList<>();
    public List<Integer> heap = new ArrayList<>();

    public void build(List<Integer> data) {
      this.heap = data;
      for (int i = data.size() - 1; i >= 0; i--) {
        shiftDown(i);
      }
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

    public Integer parent(int i) {
      if (i == 0)
        return null;
      return heap.get((i - 1) / 2);
    }

    public Integer leftChild(int i) {
      if (((i * 2) + 1) >= heap.size())
        return null;
      return heap.get((i * 2) + 1);
    }

    public Integer rightChild(int i) {
      if (((i * 2) + 2) >= heap.size())
        return null;
      return heap.get((i * 2) + 2);
    }

    private void shiftUp(int i) {
      Integer parent = null;
      Integer parentIndex = null;
      while ((parent = parent(i)) != null) {
        parentIndex = parentindex(i);
        if (parent > heap.get(i)) {
          swap(i, parentIndex);
        }
        i = parentIndex;
      }
    }

    public List<Swap> getSwaps() {
      return swaps;
    }

    private void shiftDown(int i) {
      int node = heap.get(i);
      Integer leftChild = leftChild(i);
      Integer rightChild = rightChild(i);

      int indexToSwap = -1;
      if (leftChild != null && rightChild != null) {
        if (leftChild > rightChild)
          indexToSwap = rightChildIndex(i);
        else
          indexToSwap = leftChildIndex(i);
      } else if (leftChild != null)
        indexToSwap = leftChildIndex(i);

      if (indexToSwap != -1 && node > heap.get(indexToSwap)) {
        swap(i, indexToSwap);
        shiftDown(indexToSwap);
      }
    }

    private void swap(int index1, int index2) {
      int tmp = heap.get(index1);
      heap.set(index1, heap.get(index2));
      heap.set(index2, tmp);
      swaps.add(new Swap(index1, index2));
    }

    public void remove(int i) {
      heap.set(i, Integer.MAX_VALUE);
      shiftUp(i);
      extractMax();
    }

    public int extractMax() {
      int result = heap.get(0);
      heap.set(0, heap.get(heap.size() - 1));
      heap.remove(heap.size() - 1);
      shiftDown(0);
      return result;
    }

    public void insert(int n) {
      heap.add(n);
      shiftUp(heap.size() - 1);
    }

  }

  public void solve() throws IOException {
    in = new FastScanner();
    out = new PrintWriter(new BufferedOutputStream(System.out));
    readData();
    generateSwaps();
    writeResponse();
    out.close();
  }

  static class Swap {
    int index1;
    int index2;

    public Swap(int index1, int index2) {
      if (index1 < index2) {
        this.index1 = index1;
        this.index2 = index2;
      } else {
        this.index1 = index2;
        this.index2 = index1;
      }
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
