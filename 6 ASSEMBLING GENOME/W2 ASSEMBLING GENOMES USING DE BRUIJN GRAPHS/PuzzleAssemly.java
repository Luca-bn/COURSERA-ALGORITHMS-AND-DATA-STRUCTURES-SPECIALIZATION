import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.io.*;

/*
    Task. Let each square piece be defined by the four colors of its four edges, in the format (up,left,down,right).
    Let a “valid placement” be defined as a placement of n^2 square pieces onto an n-by-n grid such
    that all “outer edges” (i.e., edges that border no other square pieces), and only these edges,
    are black, and for all edges that touch an edge in another square piece, the two touching
    edges are the same color.
    You will be given 25 square pieces in the format described above, and you will need to return a “valid
    placement” of them onto a 5-by-5 grid. To simplify the problem, we guarantee that all of the square
    pieces are given to you in the correct orientation (i.e., you will not need to rotate any of the pieces to
    have them fit in a “valid placement”). For example, the square (green,black,red,blue) and the similar
    square (black,red,blue,green) are not equivalent in this problem.

    Dataset. Each line of the input contains a single square piece, in the format described above:
    (up,left,down,right). You will be given 25 such pieces in total (so 25 lines of input). Note that all
    “outer edges” (i.e., edges that border no other square pieces on the puzzle) are black, and none of the
    “inner edges” (i.e., edges not on the outside border of the puzzle) are black.
    
    Output. Output a “valid placement" of the inputted pieces in a 5-by-5 grid. Specifically, your output should
    be exactly 5 lines long (representing the 5 rows of the grid), and on each line of your output, you should
    output 5 square pieces in the exact format described, (up,left,down,right), separated by semicolons.
    There should be no space characters at all in your output.
*/

// Good job! (Max time used: 0.21/15.00, max memory used: 38199296/2147483648.)
public class PuzzleAssemly {

    private static final String BORDER_COLOR = "black";
    private static final int PUZZLE_SIZE = 5;
    private static final int NUMBER_OF_PIECES = PUZZLE_SIZE * PUZZLE_SIZE;

    public static void main(String[] args) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            List<Square> squares = readInput(in);
            makeGraph(squares);
            LinkedList<Edge> hemiltonianPath = findHemiltonianPath(squares);
            Square[][] puzzle = assemblyPuzzle(hemiltonianPath);
            printPuzzle(puzzle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // read the input and creates a list of square objects
    private static List<Square> readInput(BufferedReader in) throws IOException {
        List<Square> input = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_PIECES; i++)
            input.add(new Square(i, in.readLine()));
        return input;
    }

    // for each node creates edges which are squares with same color in opposite direction (probably could be improved)
    private static void makeGraph(List<Square> squares) {
        for (Square square : squares) {
            if (!BORDER_COLOR.equals(square.top)) {
                square.edges.addAll(squares.stream().filter(s -> s.bottom.equals(square.top) && s.id != square.id)
                        .map(s -> new Edge(Direction.TOP, s)).collect(Collectors.toList()));
            }
            if (!BORDER_COLOR.equals(square.left)) {
                square.edges.addAll(squares.stream().filter(s -> s.right.equals(square.left) && s.id != square.id)
                        .map(s -> new Edge(Direction.LEFT, s)).collect(Collectors.toList()));
            }
            if (!BORDER_COLOR.equals(square.bottom)) {
                square.edges.addAll(squares.stream().filter(s -> s.top.equals(square.bottom) && s.id != square.id)
                        .map(s -> new Edge(Direction.BOTTOM, s)).collect(Collectors.toList()));
            }
            if (!BORDER_COLOR.equals(square.right)) {
                square.edges.addAll(squares.stream().filter(s -> s.left.equals(square.right) && s.id != square.id)
                        .map(s -> new Edge(Direction.RIGHT, s)).collect(Collectors.toList()));
            }
        }
    }

    // finding hemiltonian path from graph
    private static LinkedList<Edge> findHemiltonianPath(List<Square> squares) throws Exception {
        LinkedList<Edge> path = new LinkedList<>();

        // starting traveling from top-left corner
        Square startingPoint = squares.stream()
                .filter(s -> BORDER_COLOR.equals(s.left) && BORDER_COLOR.equals(s.top)).findFirst().get();
        startingPoint.used = true;
        path.add(new Edge(null, startingPoint));
        if (!hamiltonianCycle(path, startingPoint))
            throw new Exception("No solution found");
        return path;
    }

    private static boolean hamiltonianCycle(LinkedList<Edge> path, Square v) {
        if (path.size() == PUZZLE_SIZE * PUZZLE_SIZE)
            return true;

        for (Edge edge : v.edges) {
            if (!edge.adjacent.used && checkValidMovementAndPacement(path, edge)) {
                edge.adjacent.used = true;
                path.add(edge);
                if (hamiltonianCycle(path, edge.adjacent))
                    return true;
                edge.adjacent.used = false;
                path.removeLast();
            }
        }
        return false;
    }

    /*
     * not so good this method, probably should just use a better approach to select edges for squares,
     * anyway good enaugh to pass test case
     */
    private static boolean checkValidMovementAndPacement(LinkedList<Edge> path, Edge edge) {
        AtomicInteger lastCol = new AtomicInteger(0), lastRow = new AtomicInteger(0);
        boolean[][] validity = buildValidityMatrix(path, lastRow, lastCol);
        switch (edge.direction) {
            case TOP:
                lastRow.decrementAndGet();
                break;
            case LEFT:
                lastCol.decrementAndGet();
                break;
            case BOTTOM:
                lastRow.incrementAndGet();
                break;
            case RIGHT:
                lastCol.incrementAndGet();
                break;
        }
        int row = lastRow.get(), col = lastCol.get();
        // out of bound case
        if (col < 0 || col >= PUZZLE_SIZE || row < 0 || row >= PUZZLE_SIZE)
            return false;
        // piece alread placede in this position case
        if (validity[lastRow.get()][lastCol.get()])
            return false;
        Square current = edge.adjacent;
        if (current.isAnyBorder()) {
            // border placed in wrong position case
            if (current.isTopBorder && row != 0)
                return false;
            if (current.isLeftBorder && col != 0)
                return false;
            if (current.isBottomBorder && row != PUZZLE_SIZE - 1)
                return false;
            if (current.isRightBorder && col != PUZZLE_SIZE - 1)
                return false;
        } else {
            // non border placed in border square
            if (row == 0 || row == PUZZLE_SIZE - 1 || col == 0 || col == PUZZLE_SIZE - 1)
                return false;
        }
        return true;
    }

    // create a matrix where each cordinate is true if that square is used in current path
    private static boolean[][] buildValidityMatrix(LinkedList<Edge> path, AtomicInteger lastRow,
            AtomicInteger lastCol) {
        boolean[][] validity = new boolean[PUZZLE_SIZE][PUZZLE_SIZE];
        for (Edge edge : path) {
            if (edge.direction == null)
                validity[lastRow.get()][lastCol.get()] = true;
            else {
                switch (edge.direction) {
                    case TOP:
                        lastRow.decrementAndGet();
                        break;
                    case LEFT:
                        lastCol.decrementAndGet();
                        break;
                    case BOTTOM:
                        lastRow.incrementAndGet();
                        break;
                    case RIGHT:
                        lastCol.incrementAndGet();
                        break;
                }
                validity[lastRow.get()][lastCol.get()] = true;
            }
        }
        return validity;
    }

    // reconstruct puzzle from edges used in hamiltonian path
    private static Square[][] assemblyPuzzle(LinkedList<Edge> hemiltonianPath) {
        Square[][] puzzle = new Square[PUZZLE_SIZE][PUZZLE_SIZE];

        int lastRow = 0, lastCol = 0;
        puzzle[lastRow][lastCol] = hemiltonianPath.removeFirst().adjacent;
        while (!hemiltonianPath.isEmpty()) {
            Edge next = hemiltonianPath.removeFirst();
            switch (next.direction) {
                case TOP:
                    lastRow--;
                    break;
                case LEFT:
                    lastCol--;
                    break;
                case BOTTOM:
                    lastRow++;
                    break;
                case RIGHT:
                    lastCol++;
                    break;
            }
            puzzle[lastRow][lastCol] = next.adjacent;
        }

        return puzzle;
    }

    private static void printPuzzle(Square[][] puzzle) {
        for (Square[] row : puzzle)
            System.out.println(Arrays.stream(row).map(Square::toString).collect(Collectors.joining(";")));
    }

    static class Square {

        // position in graph
        final int id;

        // colors
        String top;
        String left;
        String bottom;
        String right;

        // if is used in current path
        boolean used;
        // all edges (in this implementation i just added every square which has same
        // color in opposite direction)
        List<Edge> edges = new ArrayList<>();

        // used to check in a fast way if square is border or not
        boolean isTopBorder, isLeftBorder, isBottomBorder, isRightBorder;

        public Square(int id, String input) {
            this.id = id;
            String[] splittedInput = input.substring(1, input.length() - 1).split(",");
            top = splittedInput[0];
            if (BORDER_COLOR.equalsIgnoreCase(top))
                isTopBorder = true;
            left = splittedInput[1];
            if (BORDER_COLOR.equalsIgnoreCase(left))
                isLeftBorder = true;
            bottom = splittedInput[2];
            if (BORDER_COLOR.equalsIgnoreCase(bottom))
                isBottomBorder = true;
            right = splittedInput[3];
            if (BORDER_COLOR.equalsIgnoreCase(right))
                isRightBorder = true;
        }

        public boolean isAnyBorder() {
            return isTopBorder || isLeftBorder || isBottomBorder || isRightBorder;
        }

        @Override
        public String toString() {
            return String.format("(%s,%s,%s,%s)", top, left, bottom, right);
        }
    }

    static class Edge {
        Direction direction;
        Square adjacent;

        public Edge(Direction direction, Square adjacent) {
            this.direction = direction;
            this.adjacent = adjacent;
        }
    }

    static enum Direction {
        TOP,
        LEFT,
        BOTTOM,
        RIGHT;
    }

}
