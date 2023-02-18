import java.io.*;
import java.util.*;

/*
    Task. You want to optimize your diet: that is, make sure that your diet satisfies all the recommendations
    of nutrition experts, but you also get maximum pleasure from your food and drinks. For each dish
    and drink you know all the nutrition facts, cost of one item, and an estimation of how much you like
    it. Your budget is limited, of course. The recommendations are of the form “total amount of calories
    consumed each day should be at least 1000” or “the amount of water you drink in liters should be at
    least twice the amount of food you eat in kilograms”, and so on. You optimize the total pleasure which
    is the sum of pleasure you get from consuming each particular dish or drink, and that is proportional
    to the amount amount𝑖 of that dish or drink consumed.
    The budget restriction and the nutrition recommendations can be converted into a system of linear
    inequalities like
    Σ︀ (from 𝑖=1 to 𝑚) [cost𝑖 · amount𝑖 ≤ Budget, amount𝑖 ≥ 1000 and amount𝑖 − 2 · amount𝑗 ≥ 0], where
    amount𝑖 is the amount of 𝑖-th dish or drink consumed, cost𝑖 is the cost of one item of 𝑖-th dish or
    drink, and 𝐵𝑢𝑑𝑔𝑒𝑡 is your total budget for the diet. Of course, you can only eat a non-negative amount
    amount𝑖 of 𝑖-th item, so amount𝑖 ≥ 0. The goal to maximize total pleasure is reduced to the linear
    objective
    Σ︀ (from 𝑖=1 to 𝑚) [amount𝑖 · pleasure𝑖] → max where pleasure𝑖 is the pleasure you get after consuming one
    unit of 𝑖-th dish or drink (some dishes like fish oil you don’t like at all, so pleasure𝑖 can be negative).
    Combined, all this is a linear programming problem which you need to solve now.
    
    Input Format. The first line of the input contains integers 𝑛 and 𝑚 — the number of restrictions on your
    diet and the number of all available dishes and drinks respectively. The next 𝑛 + 1 lines contain the
    coefficients of the linear inequalities in the standard form 𝐴𝑥 ≤ 𝑏, where 𝑥 = amount is the vector of
    length 𝑚 with amounts of each ingredient, 𝐴 is the 𝑛 × 𝑚 matrix with coefficients of inequalities and
    𝑏 is the vector with the right-hand side of each inequality. Specifically, 𝑖-th of the next 𝑛 lines contains
    𝑚 integers 𝐴𝑖1,𝐴𝑖2, . . . ,𝐴𝑖𝑚, and the next line after those 𝑛 contains 𝑛 integers 𝑏1, 𝑏2, . . . , 𝑏𝑛. These
    lines describe 𝑛 inequalities of the form 𝐴𝑖1 · amount1 +𝐴𝑖2 · amount2 +· · ·+𝐴𝑖𝑚 · amount𝑚 ≤ 𝑏𝑖. The
    last line of the input contains 𝑚 integers — the pleasure for consuming one item of each dish and drink
    pleasure1, pleasure2, . . . , pleasure𝑚.
    
    Constraints. 1 ≤ 𝑛,𝑚 ≤ 8; −100 ≤ 𝐴𝑖𝑗 ≤ 100; −1 000 000 ≤ 𝑏𝑖 ≤ 1 000 000; −100 ≤ cost𝑖 ≤ 100.
    
    Output Format. If there is no diet that satisfies all the restrictions, output “No solution” (without quotes).
    If you can get as much pleasure as you want despite all the restrictions, output “Infinity” (without
    quotes). If the maximum possible total pleasure is bounded, output two lines. On the first line, output
    “Bounded solution” (without quotes). On the second line, output 𝑚 real numbers — the optimal amounts
    for each dish and drink. Output all the numbers with at least 15 digits after the decimal point.
    The amounts you output will be inserted into the inequalities, and all the inequalities will be checked.
    An inequality 𝐿 ≤ 𝑅 will be considered satisfied if actually 𝐿 ≤ 𝑅 + 10^−3. The total pleasure of your
    solution will be calculated and compared with the optimal value. Your output will be accepted if all
    the inequalities are satisfied and the total pleasure of your solution differs from the optimal value by
    at most 10^−3. We ask you to output at least 15 digits after the decimal point, although
    we will check the answer with precision of only 10^−3. This is because in the process of
    checking the inequalities we will multiply your answers with coefficients from the matrix
    𝐴 and with the coefficients of the vector pleasure, and those coefficients can be pretty
    large, and computations with real numbers on a computer are not always precise. This
    way, the more digits after the decimal point you output for each amount — the less likely
    it is that your answer will be rejected because of precision issues.
*/

// Good job! (Max time used: 0.21/4.00, max memory used: 38854656/2147483648.)
public class Diet {

    BufferedReader br;
    PrintWriter out;
    StringTokenizer st;
    boolean eof;

    int solveDietProblem(int n, int m, double A[][], double[] b, double[] c, double[] x) {
        Arrays.fill(x, 1);

        return 0;
    }

    void solve() throws Exception {
        int n = nextInt();
        int m = nextInt();
        double[][] A = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                A[i][j] = nextInt();
            }
        }
        double[] b = new double[n];
        for (int i = 0; i < n; i++) {
            b[i] = nextInt();
        }
        double[] c = new double[m];
        for (int i = 0; i < m; i++) {
            c[i] = nextInt();
        }
        TwoPhasesSimplexMethod solver = new TwoPhasesSimplexMethod(m, n,
                TwoPhasesSimplexMethod.SimplexOperation.MAXIMIZE);
        solver.debug = false;
        for (int i = 0; i < n; i++) {
            TwoPhasesSimplexMethod.SimplexInequality in = new TwoPhasesSimplexMethod.SimplexInequality(m,
                    TwoPhasesSimplexMethod.InequalityOperation.LESS_THAN_OR_EQUAL_TO);
            for (int j = 0; j < m; j++) {
                in.setCoefficientValue(j, A[i][j]);
            }
            in.setBetaValue(b[i]);
            solver.addConstraint(in);
        }
        TwoPhasesSimplexMethod.SimplexInequality objectiveFunction = new TwoPhasesSimplexMethod.SimplexInequality(m,
                TwoPhasesSimplexMethod.InequalityOperation.EQUALS_TO);
        for (int i = 0; i < m; i++)
            objectiveFunction.setCoefficientValue(i, c[i]);
        solver.objectiveFunction = objectiveFunction;

        TwoPhasesSimplexMethod.SimplexResult result = solver.solve();
        out.println(result.resultType);
        if (result.solution != null)
            for (int i = 0; i < m; i++)
                out.print(String.format("%.18f ", result.solution[i]).replace(",", "."));
    }

    Diet() throws Exception {
        br = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(System.out);
        solve();
        out.close();
    }

    public static void main(String[] args) throws Exception {
        // test();
        new Diet();
    }

    String nextToken() {
        while (st == null || !st.hasMoreTokens()) {
            try {
                st = new StringTokenizer(br.readLine());
            } catch (Exception e) {
                eof = true;
                return null;
            }
        }
        return st.nextToken();
    }

    int nextInt() throws IOException {
        return Integer.parseInt(nextToken());
    }

}

class TwoPhasesSimplexMethod {

    private static final double EPS = 0.00000000006;
    private final int originalNumberOfVariables;
    boolean debug = true;
    int numberOfVariables;
    int numberOfConstraints;
    List<SimplexBasicVariable> basicVariables = new ArrayList<>();
    List<SimplexInequality> constraints = new ArrayList<>();
    List<Integer> removedVariables = new ArrayList<>();
    SimplexInequality objectiveFunction;
    SimplexOperation operationToPerform;
    int slackVariables = 0, artificialVariables = 0;
    double[][] tableau;
    String[] variablesIds;

    TwoPhasesSimplexMethod(int numberOfVariables, int numberOfConstraints, SimplexOperation operationToPerform) {
        this.originalNumberOfVariables = numberOfVariables;
        this.numberOfVariables = numberOfVariables;
        this.numberOfConstraints = numberOfConstraints;
        this.operationToPerform = operationToPerform;
        for (int i = 0; i <= numberOfConstraints; i++)
            basicVariables.add(new SimplexBasicVariable());
    }

    SimplexResult solve() {
        variablesIds = new String[numberOfVariables + slackVariables + artificialVariables + 1];
        int index = 0;
        for (int i = 1; i <= numberOfVariables; i++)
            variablesIds[index++] = "x" + i;
        for (int i = 1; i <= slackVariables; i++)
            variablesIds[index++] = "s" + i;
        for (int i = 1; i <= artificialVariables; i++)
            variablesIds[index++] = "a" + i;
        variablesIds[variablesIds.length - 1] = " Z";
        try {
            phaseOne();
        } catch (Exception e) {
            return new SimplexResult(SimplexResultType.UNBOUNDED);
        }
        // case 1
        if (tableau[numberOfConstraints][tableau[numberOfConstraints].length - 1] > 0 + EPS) {
            if (debug)
                System.out.println("== value of Z > 0 after phase one => Unfeasable solution ==");
            return new SimplexResult(SimplexResultType.UNDEFINED);
        }

        // tracking basic variables after phase one and checking if there are artificial
        // variables
        boolean containsArtificialVariables = false;
        List<SimplexBasicVariable> phaseOneBV = new ArrayList<>();
        List<Integer> artificialBasicVariables = new ArrayList<>();
        for (SimplexBasicVariable bv : basicVariables) {
            SimplexBasicVariable clone = new SimplexBasicVariable();
            clone.column = bv.column;
            clone.row = bv.row;
            clone.id = bv.id;
            clone.value = bv.value;
            phaseOneBV.add(clone);
            if (bv.id.contains("a")) {
                containsArtificialVariables = true;
                artificialBasicVariables.add(bv.column);
            }
        }
        int zIndex = numberOfVariables + slackVariables + artificialVariables;
        for (int i = numberOfVariables + slackVariables; i < zIndex; i++)
            if (!artificialBasicVariables.contains(i)) {
                removedVariables.add(i);
                artificialVariables--;
            }
        if (containsArtificialVariables) {
            for (int i = 0; i < numberOfVariables; i++)
                if (tableau[tableau.length - 1][i] < 0) {
                    removedVariables.add(i);
                    numberOfVariables--;
                }
        }
        removeVariablesFromTableau(containsArtificialVariables);

        if (debug) {
            System.out.println("== updated tableau for phase 2 ==");
            printTableau();
        }
        List<SimplexBasicVariable> delta = null;
        // updating tableau in a way thath basic variables are the same as that ones in
        // phase one
        while (!(delta = findDifferentBasicVariables(basicVariables, phaseOneBV)).isEmpty()) {
            if (debug) {
                System.out.println(String.format("== delta variables from phase one: %s ==", delta));
            }
            SimplexBasicVariable bv = delta.get(0);
            for (int row = 0; row < tableau.length; row++) {
                if (tableau[row][bv.column] < 1 + EPS && tableau[row][bv.column] > 1 - EPS) {
                    double objFunctionValue = tableau[tableau.length - 1][bv.column];
                    for (int column = 0; column < tableau[0].length; column++) {
                        tableau[tableau.length - 1][column] = tableau[tableau.length - 1][column]
                                + (-1d * objFunctionValue * tableau[row][column]);
                        if (Math.abs(tableau[tableau.length - 1][column]) < EPS)
                            tableau[tableau.length - 1][column] = 0;
                    }
                    break;
                }
            }
            updateBasicVariables();
            if (debug) {
                System.out.println(String.format("== updated objective function for basic variable %s ==", bv.id));
                printTableau();
            }
        }
        // checking if after updating basic variables, the tableau is optimized
        while (!checkOptimality(operationToPerform)) {
            SimplexPivot pivotElement = findPivotElement(operationToPerform);
            if (pivotElement.row == -1 || pivotElement.row == -1) {
                if (debug)
                    System.out.println(
                            "== objective function not optimized, but no pivot element can be found => UNBOUNDED Solution ==");
                return new SimplexResult(SimplexResultType.UNBOUNDED);
            }
            String exitingBV = basicVariables.get(pivotElement.row).id;
            multiplyRowByValue(pivotElement.row, 1d / pivotElement.value);
            pivotTable(pivotElement);
            updateBasicVariables();
            if (debug) {
                System.out.println(
                        String.format("== (phase two) updated tableau for pivot %s - exiting %s entering %s ==",
                                pivotElement, exitingBV, basicVariables.get(pivotElement.row).id));
                printTableau();
            }
        }

        SimplexResult result = new SimplexResult(SimplexResultType.BOUNDED);
        result.solution = new double[originalNumberOfVariables];
        for (int i = 1; i <= originalNumberOfVariables; i++) {
            final String id = "x" + i;
            SimplexBasicVariable bv = basicVariables.stream().filter(v -> v.id.equals(id)).findFirst().orElse(null);
            if (bv != null)
                result.solution[i - 1] = bv.value;
        }
        SimplexBasicVariable bv = basicVariables.stream().filter(v -> v.id.equals(" Z")).findFirst().orElse(null);
        if (bv != null)
            result.value = bv.value;
        return result;
    }

    private void removeVariablesFromTableau(boolean containsArtificialVariables) {
        if (!containsArtificialVariables) {
            // I have to remove ALL artificial variables
            for (int i = numberOfVariables + slackVariables; i < numberOfVariables + slackVariables
                    + artificialVariables; i++)
                removedVariables.add(i);
            artificialVariables = 0;
        }
        double[][] phaseTwoTableau = new double[tableau.length][];
        for (int i = 0; i < tableau.length; i++) {
            phaseTwoTableau[i] = new double[tableau[i].length - removedVariables.size()];
            int currentIndex = 0;
            for (int j = 0; j < tableau[i].length; j++) {
                if (removedVariables.contains(j))
                    continue;
                phaseTwoTableau[i][currentIndex++] = tableau[i][j];
            }
        }
        int skip = 0;
        double[] objectiveFunctionRow = new double[tableau[0].length - removedVariables.size()];
        for (int i = 0; i < objectiveFunction.coefficients.length; i++) {
            if (removedVariables.contains(i)) {
                skip++;
                continue;
            }
            objectiveFunctionRow[i - skip] = Math.abs(objectiveFunction.coefficients[i]) < EPS ? 0
                    : -1d * objectiveFunction.coefficients[i];
        }
        objectiveFunctionRow[objectiveFunctionRow.length - 2] = 1;
        phaseTwoTableau[tableau.length - 1] = objectiveFunctionRow;
        tableau = phaseTwoTableau;
        String[] newVariablesIds = new String[numberOfVariables + slackVariables + artificialVariables + 1];
        int index = 0;
        for (int i = 0; i < variablesIds.length; i++)
            if (!removedVariables.contains(i))
                newVariablesIds[index++] = variablesIds[i];
        variablesIds = newVariablesIds;
        updateBasicVariables();
    }

    private List<SimplexBasicVariable> findDifferentBasicVariables(List<SimplexBasicVariable> basicVariables,
            List<SimplexBasicVariable> phaseOneBV) {
        List<SimplexBasicVariable> delta = new ArrayList<>();
        for (SimplexBasicVariable bv : phaseOneBV) {
            SimplexBasicVariable match = basicVariables.stream().filter(b -> b.id.equals(bv.id)).findFirst()
                    .orElse(null);
            if (match == null) {
                for (int i = 0; i < variablesIds.length; i++)
                    if (variablesIds[i].equals(bv.id)) {
                        bv.column = i;
                        break;
                    }
                int index = -1;
                double value = Integer.MAX_VALUE;
                for (int i = 0; i < numberOfConstraints; i++) {
                    if (tableau[i][bv.column] == 0)
                        continue;
                    double ratio = tableau[i][tableau[i].length - 1] / tableau[i][bv.column];
                    if (ratio > 0 && ratio < value) {
                        value = ratio;
                        index = i;
                    }
                }
                bv.row = index;
                if (index != -1)
                    bv.value = tableau[bv.row][bv.column];
                delta.add(bv);
            }
        }
        return delta;
    }

    private void phaseOne() throws Exception {
        buildPhaseOneTableau();
        updateBasicVariables();
        if (debug) {
            System.out.println("== created tableau for phase1 ==");
            printTableau();
        }

        // removing artificial variables from objectiveFunction*
        for (int i = numberOfVariables + slackVariables; i < numberOfVariables + slackVariables
                + artificialVariables; i++) {
            int rowToSum = -1;
            for (int j = 0; j < tableau.length; j++)
                if (tableau[j][i] == 1) {
                    rowToSum = j;
                    break;
                }
            for (int j = 0; j < numberOfVariables + slackVariables + artificialVariables + 2; j++)
                tableau[numberOfConstraints][j] = tableau[numberOfConstraints][j] + tableau[rowToSum][j];
            updateBasicVariables();
            if (debug) {
                System.out.println(
                        String.format("== (phase one) removed artificial variable [%s] from objective function* ==",
                                "a" + ((i + 1) - (numberOfVariables + slackVariables))));
                printTableau();
            }
        }

        // optimizing objectiveFunction* (in phase one the goal is to MINIMIZE the
        // function*)
        while (!checkOptimality(SimplexOperation.MINIMIZE)) {
            SimplexPivot pivotElement = findPivotElement(SimplexOperation.MINIMIZE);
            if (pivotElement.column == -1 || pivotElement.row == -1
                    || pivotElement.column == basicVariables.get(pivotElement.row).column)
                throw new Exception();
            String exitingBV = basicVariables.get(pivotElement.row).id;
            multiplyRowByValue(pivotElement.row, 1 / pivotElement.value);
            pivotTable(pivotElement);
            updateBasicVariables();
            if (debug) {
                System.out.println(
                        String.format("== (phase one) updated tableau for pivot %s - exiting %s entering %s ==",
                                pivotElement, exitingBV, basicVariables.get(pivotElement.row).id));
                printTableau();
            }
        }
    }

    private void updateBasicVariables() {
        for (int i = 0; i < basicVariables.size(); i++) {
            SimplexBasicVariable bv = basicVariables.get(i);
            bv.column = -1;
            bv.row = -1;
            bv.id = "?";
            bv.value = Double.POSITIVE_INFINITY;
        }
        for (int column = 0; column < tableau[0].length - 1; column++) {
            int zeroCount = 0;
            int nonZeroIndex = -1;
            for (int row = 0; row < tableau.length; row++) {
                if (tableau[row][column] < 0 + EPS && tableau[row][column] > 0 - EPS)
                    zeroCount++;
                else if (tableau[row][column] > 0)
                    nonZeroIndex = row;
            }
            if (nonZeroIndex != -1 && zeroCount == tableau.length - 1) {
                SimplexBasicVariable bv = basicVariables.get(nonZeroIndex);
                bv.column = column;
                bv.row = nonZeroIndex;
                bv.value = tableau[nonZeroIndex][tableau[0].length - 1] / tableau[bv.row][bv.column];
                bv.id = variablesIds[bv.column];
            }
        }
    }

    private void pivotTable(SimplexPivot pivotElement) {
        /*
         * updating all values of other rows and columns in a way that all elements on
         * same column of pivot element become 0
         */
        for (int row = 0; row < tableau.length; row++) {
            if (row == pivotElement.row)
                continue;
            double currentRowPivot = tableau[row][pivotElement.column];
            for (int column = 0; column < tableau[0].length; column++) {
                tableau[row][column] = (-1d * currentRowPivot * tableau[pivotElement.row][column])
                        + tableau[row][column];
                // if (Math.abs(tableau[row][column]) < EPS)
                //     tableau[row][column] = 0;
            }
        }
    }

    private void multiplyRowByValue(int row, double value) {
        for (int i = 0; i < tableau[0].length; i++)
            tableau[row][i] = value * tableau[row][i];
    }

    private SimplexPivot findPivotElement(SimplexOperation op) {
        List<Integer> candidates = new ArrayList<>();
        int index = -1;
        if (SimplexOperation.MAXIMIZE.equals(op)) {
            double min = Integer.MAX_VALUE;
            for (int column = 0; column < numberOfVariables + slackVariables; column++) {
                if (tableau[tableau.length - 1][column] < 0) {
                    if (tableau[tableau.length - 1][column] < min) {
                        candidates = new ArrayList<>();
                        min = tableau[tableau.length - 1][column];
                        index = column;
                    } else if (tableau[tableau.length - 1][column] == min) {
                        candidates.add(index);
                        min = tableau[tableau.length - 1][column];
                        index = column;
                    }
                }
            }
        } else {
            double max = Integer.MIN_VALUE;
            for (int column = 0; column < numberOfVariables + slackVariables; column++) {
                if (tableau[tableau.length - 1][column] > 0) {
                    if (tableau[tableau.length - 1][column] > max) {
                        candidates = new ArrayList<>();
                        max = tableau[tableau.length - 1][column];
                        index = column;
                    } else if (tableau[tableau.length - 1][column] == max) {
                        candidates.add(index);
                        max = tableau[tableau.length - 1][column];
                        index = column;
                    }
                }
            }
        }
        if (index != -1)
            candidates.add(index);
        if (candidates.isEmpty())
            return new SimplexPivot();
        for (Integer column : candidates) {
            index = -1;
            double value = Integer.MAX_VALUE;
            for (Integer row = 0; row < tableau.length - 1; row++) {
                if (tableau[row][column] == 0) {
                    if(debug) {
                        System.out.println(String.format("== checking for variable %s, skipping variable %s for 0 value ==", variablesIds[column], basicVariables.get(row).id));
                    }
                    continue;
                }
                double ratio = tableau[row][tableau[row].length - 1] / tableau[row][column];
                if(debug) {
                    System.out.println(String.format("== checking for variable %s, ratio for %s : %.15f ==", variablesIds[column], basicVariables.get(row).id, ratio));
                }
                if (ratio >= 0 && ratio < value || (ratio == value && basicVariables.get(row).column < basicVariables.get(index).column)) {
                    value = ratio;
                    index = row;
                }
            }
            if (index != -1) {
                SimplexPivot pivot = new SimplexPivot();
                pivot.row = index;
                pivot.column = column;
                pivot.value = tableau[index][column];
                return pivot;
            }
        }
        return new SimplexPivot();
    }

    private boolean checkOptimality(SimplexOperation operation) {
        for (int i = 0; i < numberOfVariables + slackVariables; i++)
            if (SimplexOperation.MINIMIZE.equals(operation)) {
                if (tableau[numberOfConstraints][i] > 0 + EPS)
                    return false;
            } else {
                if (tableau[numberOfConstraints][i] < 0 - EPS)
                    return false;
            }
        return true;
    }

    void buildPhaseOneTableau() {
        tableau = new double[numberOfConstraints + 1][];
        int artificialVariableIndex = numberOfVariables + slackVariables;
        for (int i = 0; i < numberOfConstraints; i++) {
            tableau[i] = new double[numberOfVariables + slackVariables + artificialVariables + 2];
            SimplexInequality constraint = constraints.get(i);
            for (int j = 0; j < constraint.coefficients.length; j++)
                tableau[i][j] = constraint.coefficients[j];
            switch (constraint.operation) {
                case LESS_THAN_OR_EQUAL_TO:
                    tableau[i][numberOfVariables + i] = 1;
                    break;
                case GREATHER_THAN_OR_EQUAL_TO:
                    tableau[i][numberOfVariables + i] = -1;
                    if (!constraint.containsActiveArtificialVariable)
                        tableau[i][artificialVariableIndex++] = 1;
                    break;
                case EQUALS_TO:
                    if (!constraint.containsActiveArtificialVariable)
                        tableau[i][artificialVariableIndex++] = 1;
                    break;
            }
            tableau[i][numberOfVariables + slackVariables + artificialVariables + 1] = constraint.beta;
        }
        tableau[numberOfConstraints] = new double[numberOfVariables + slackVariables + artificialVariables + 2];
        for (int i = 0; i < artificialVariables; i++)
            tableau[numberOfConstraints][numberOfVariables + slackVariables + i] = -1;
        tableau[numberOfConstraints][numberOfVariables + slackVariables + artificialVariables] = 1;
    }

    void addConstraint(SimplexInequality constraint) {
        if (constraint.beta < 0)
            multiplyInequalityByMinusOne(constraint);
        if (!InequalityOperation.LESS_THAN_OR_EQUAL_TO.equals(constraint.operation)
                && !constraint.containsActiveArtificialVariable)
            artificialVariables += 1;
        if (!InequalityOperation.EQUALS_TO.equals(constraint.operation))
            slackVariables += 1;
        constraints.add(constraint);
    }

    void multiplyInequalityByMinusOne(SimplexInequality in) {
        for (int i = 0; i < in.coefficients.length; i++)
            in.coefficients[i] = -1 * in.coefficients[i];
        in.beta = -1 * in.beta;
        in.operation = in.operation.reverse();
    }

    private void printTableau() {
        System.out.println();
        /* table header */
        for (int i = 0; i < variablesIds.length; i++) {
            System.out.print(String.format("|     %s     |", variablesIds[i]));
        }
        System.out.print("|      b     |");
        System.out.print(String.format(" BV    \n"));

        /* table content */
        for (int i = 0; i < tableau.length; i++) {
            for (int j = 0; j < tableau[0].length; j++) {
                String n = String.format("%.8f", tableau[i][j]);
                if (n.length() > 12)
                    n = n.substring(0, 11);
                while (n.length() < 12)
                    n = n + " ";
                System.out.print("|" + n + "|");
            }
            String bv = String.format("%s = %.15f", basicVariables.get(i).id, basicVariables.get(i).value);
            System.out.print(bv);
            System.out.println();
        }
        System.out.println();
    }

    static class SimplexBasicVariable {
        String id;
        int row;
        int column;
        double value;

        @Override
        public String toString() {
            return id;
        }
    }

    static class SimplexPivot {
        int row = -1;
        int column = -1;
        double value;

        @Override
        public String toString() {
            return "SimplexPivot [row=" + row + ", column=" + column + ", value=" + value + "]";
        }
    }

    static class SimplexResult {
        double[] solution;
        SimplexResultType resultType;
        double value;

        SimplexResult(SimplexResultType resultType) {
            this.resultType = resultType;
        }
    }

    static class SimplexInequality {
        int variables;
        double[] coefficients;
        double beta;
        InequalityOperation operation;
        boolean containsActiveArtificialVariable = false;

        SimplexInequality(int n, InequalityOperation op) {
            this.variables = n;
            this.coefficients = new double[n];
            this.operation = op;
        }

        void setCoefficientValue(int index, double coefficient) {
            this.coefficients[index] = coefficient;
        }

        void setBetaValue(double value) {
            this.beta = value;
        }
    }

    static enum InequalityOperation {
        EQUALS_TO,
        LESS_THAN_OR_EQUAL_TO,
        GREATHER_THAN_OR_EQUAL_TO;

        public InequalityOperation reverse() {
            if (this.equals(EQUALS_TO))
                return EQUALS_TO;
            if (this.equals(GREATHER_THAN_OR_EQUAL_TO))
                return LESS_THAN_OR_EQUAL_TO;
            if (this.equals(LESS_THAN_OR_EQUAL_TO))
                return GREATHER_THAN_OR_EQUAL_TO;
            return null;
        }
    }

    static enum SimplexOperation {
        MAXIMIZE,
        MINIMIZE;
    }

    static enum SimplexResultType {
        BOUNDED("Bounded solution"),
        UNBOUNDED("Infinity"),
        UNDEFINED("No solution");

        private final String toString;

        SimplexResultType(String toString) {
            this.toString = toString;
        }

        @Override
        public String toString() {
            return toString;
        }
    }

    private static void test() {
        int numberOfVariables = 6, numberOfConstraints = 4;
        TwoPhasesSimplexMethod simplex = new TwoPhasesSimplexMethod(numberOfVariables, numberOfConstraints,
                TwoPhasesSimplexMethod.SimplexOperation.MAXIMIZE);
        double[][] matrix = new double[][] {
                { 1, -1, 0, 0, 2, 0, 0 },
                { -2, 1, 0, 0, -2, 0, 0 },
                { 1, 0, 1, 0, 1, -1, 3 },
                { 0, 1, 1, 1, 2, 1, 4 }
        };
        for (int i = 0; i < numberOfConstraints; i++) {
            TwoPhasesSimplexMethod.SimplexInequality in = new TwoPhasesSimplexMethod.SimplexInequality(
                    numberOfVariables, TwoPhasesSimplexMethod.InequalityOperation.EQUALS_TO);
            for (int j = 0; j < numberOfVariables; j++) {
                in.setCoefficientValue(j, matrix[i][j]);
                // in.containsActiveArtificialVariable = i == numberOfConstraints - 1;
            }
            in.setBetaValue(matrix[i][matrix[i].length - 1]);
            simplex.addConstraint(in);
        }
        TwoPhasesSimplexMethod.SimplexInequality of = new TwoPhasesSimplexMethod.SimplexInequality(numberOfVariables,
                TwoPhasesSimplexMethod.InequalityOperation.EQUALS_TO);
        of.setCoefficientValue(0, 40);
        of.setCoefficientValue(1, 10);
        of.setCoefficientValue(4, 7);
        of.setCoefficientValue(5, 14);
        simplex.objectiveFunction = of;

        TwoPhasesSimplexMethod.SimplexResult result = simplex.solve();
        System.out.println(result.resultType);
        if (result.solution != null)
            for (int i = 0; i < numberOfVariables; i++)
                System.out.print(String.format("%.18f ", result.solution[i]).replace(",", "."));
        System.out.println();
        System.out.println(result.value);
    }
}