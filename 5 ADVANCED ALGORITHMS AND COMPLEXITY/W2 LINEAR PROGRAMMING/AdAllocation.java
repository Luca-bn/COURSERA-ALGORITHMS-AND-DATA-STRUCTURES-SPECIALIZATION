import java.io.*;
import java.util.*;

/*
    Task. You have ๐ clients, they are advertisers, and each of them wants to show their ads to some number of
    internet users specified in the contract (or more) next month. Your online advertising network has ๐
    placements overall on all the sites connected to the network. You know how many users each advertiser
    wants to reach, how many users will see each of the ๐ ad placements next month, and how much
    each advertiser is willing to pay for one user who sees their ad through each particular ad placement
    (different placements can be on different sites attracting different types of users, and each advertiser
    is more interested in the visitors of some sites than the others). You can show different ads of different
    advertisers in the same ad placement throughout the next month or show always the same ad of the
    same advertiser, but the total number of users that will see some ad in that placement is estimated
    and fixed. You want to maximize your total revenue which is the sum of amounts each advertiser will
    pay you for all the users who have seen their ads.
    If we denote by ๐ฅ๐๐ the number of users who have seen an ad of advertiser ๐ in the ad placement ๐,
    then all the restrictions can be written as linear equalities and inequalities in ๐ฅ๐๐ . For example, if the
    total number of users that will see ad placement ๐ is ๐๐ , then we add an equality
    ฮฃ๏ธ (from ๐=1 to ๐) [๐ฅ๐๐ = ๐๐] . If the ๐-th advertiser wants to show the ad to at least ๐๐ users, we add an inequality
    ฮฃ๏ธ (from ๐=1 to ๐) [๐ฅ๐๐ โฅ ๐๐] โ ฮฃ๏ธ(from ๐=1 to ๐) [(โ1) ยท ๐ฅ๐๐ โค โ๐๐]. 
    Of course, each ๐ฅ๐๐ is non-negative: ๐ฅ๐๐ โฅ 0. If advertiser ๐ wishes to pay ๐๐๐ cents
    for each user who sees her advertisement through ad placement ๐, then the goal to maximize the total
    revenue is given by linear objective
    ฮฃ๏ธ(from ๐=1 to n) ฮฃ๏ธ (from ๐=1 to ๐) ๐๐๐๐ฅ๐๐ โ max. This leads to a linear programming problem
    which you need to solve. This time it will contain more variables and inequalities, because the number
    of advertisers and the number of different ad placements can be large.
    
    Input Format. You are given the ad allocation problem reduced to a linear programming problem of the
    form ๐ด๐ฅ โค ๐, ๐ฅ โฅ 0,
    ฮฃ๏ธ (from ๐=1 to ๐) ๐๐๐ฅ๐ โ max, where ๐ด is a matrix ๐ ร ๐, ๐ is a vector of length ๐, ๐ is a vector
    of length ๐ and ๐ฅ is the unknown vector of length ๐.
    The first line of the input contains integers ๐ and ๐ โ the number of inequalities in the system and the
    number of variables respectively. The next ๐ + 1 lines contain the coefficients of the linear inequalities
    in the standard form ๐ด๐ฅ โค ๐. Specifically, ๐-th of the next ๐ lines contains ๐ integers ๐ด๐1,๐ด๐2, . . . ,๐ด๐๐,
    and the next line after those ๐ contains ๐ integers ๐1, ๐2, . . . , ๐๐. These lines describe ๐ inequalities of
    the form ๐ด๐1 ยท ๐ฅ1 + ๐ด๐2 ยท ๐ฅ2 + ยท ยท ยท + ๐ด๐๐ ยท ๐ฅ๐ โค ๐๐. The last line of the input contains ๐ integers โ the
    coefficients ๐๐ of the objective
    ฮฃ๏ธ (from ๐=1 to ๐) ๐๐๐ฅ๐ โ max.
    
    Constraints. 1 โค ๐,๐ โค 100; โ100 โค ๐ด๐๐ โค 100; โ1 000 000 โค ๐๐ โค 1 000 000; โ100 โค ๐๐ โค 100.

    Output Format. If there is no allocation that satisfies all the requirements, output โNo solutionโ (without
    quotes). If you can get as much revenue as you want despite all the requirements, output โInfinityโ
    (without quotes). If the maximum possible revenue is bounded, output two lines. On the first line,
    output โBounded solutionโ (without quotes). On the second line, output ๐ real numbers โ the optimal
    values of the vector ๐ฅ (recall that ๐ฅ = ๐ฅ๐๐ is how many users will see the ad of advertiser ๐ through
    the placement ๐, but we changed the numbering of variables to ๐ฅ1, ๐ฅ2, . . . , ๐ฅ๐). Output all the numbers
    with at least 15 digits after the decimal point. Your solution will be accepted if all the inequalities
    are satisfied and the answer has absolute error of at most 10^โ3. See the previous problem output
    format description for the explanation of what this means and why do we ask to output
    at least 15 digits after the decimal point.
*/

// Good job! (Max time used: 0.74/1.50, max memory used: 62201856/2147483648.)
public class AdAllocation {

    public static void main(String[] args) {
        boolean debug = false;
        Scanner scanner = new Scanner(System.in);

        int numberOfConstraints = scanner.nextInt(), numberOfVariables = scanner.nextInt();
        double[][] A = new double[numberOfConstraints][];
        double[] b = new double[numberOfConstraints];
        double[] c = new double[numberOfVariables];
        for (int i = 0; i < numberOfConstraints; i++) {
            A[i] = new double[numberOfVariables];
            for (int j = 0; j < numberOfVariables; j++) {
                A[i][j] = scanner.nextInt();
            }
        }

        for (int i = 0; i < numberOfConstraints; i++) {
            b[i] = scanner.nextInt();
        }

        for (int i = 0; i < numberOfVariables; i++) {
            c[i] = scanner.nextInt();
        }
        scanner.close();

        TwoPhaseSimplex lp;
        try {
            lp = new TwoPhaseSimplex(A, b, c);
            lp.debug = debug;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (debug)
                e.printStackTrace();
            return;
        }
        // System.out.println("value = " + lp.value());
        double[] x = lp.primal();
        System.out.println("Bounded solution");
        for (int i = 0; i < x.length; i++)
            System.out.print(String.format("%.15f ", x[i]).replace(",", "."));
    }
}

class TwoPhaseSimplex {
    public boolean debug = false;
    private static PrintStream StdOut;
    private static final double EPSILON = 0.000001;

    private double[][] tableaux; // tableaux
    // row m = objective function
    // row m+1 = artificial objective function
    // column n to n+m-1 = slack variables
    // column n+m to n+m+m-1 = artificial variables

    private int numberOfConstraints; // number of constraints
    private int numberOfVariables; // number of original variables

    private int numberOfArtificialVariables;

    private int[] basis; // basis[i] = basic variable corresponding to row i

    // sets up the simplex tableaux
    public TwoPhaseSimplex(double[][] constraints, double[] constraintsValues, double[] objectiveFunction)
            throws Exception {
        // StdOut = new PrintStream(new File(".\\log.log"));
        StdOut = System.out;
        numberOfConstraints = constraintsValues.length;
        numberOfVariables = objectiveFunction.length;

        // counting artificial variables (in this case all constraints are <=, so artificial variables will be added only if right-side of inequality is less then 0 because they will be multiplied by -1)
        for (int i = 0; i < constraintsValues.length; i++)
            if (constraintsValues[i] < 0)
                numberOfArtificialVariables++;

        // initializing tableaux (x=input variables, s=slack variables, a=artificial variables, B=right-side of inequality)
        //    |x1, ... xn  |s1, ... sn | a1, .... an,     |B   |
        // c1 |cX1,... cXn |cS1,...cSn |cA1, .... cAn,    |c1B |<= c=constraint
        // ...  ........................................
        // cn |........... |.......... |..............    |cnB |
        // of |ofX1.. ofXn |ofS1..ofSn |ofA1, .... ofAn,  |ofB |<= of=objective function
        // aof|aofX1..aofXn|aofS1 aofSn|aofA1, .... aofAn,|aofB|<= aof=artificial objective function
        tableaux = new double[numberOfConstraints + 2][numberOfVariables + numberOfConstraints
                + numberOfArtificialVariables + 1];
        // adding variables values (x)
        for (int i = 0; i < numberOfConstraints; i++)
            for (int j = 0; j < numberOfVariables; j++)
                tableaux[i][j] = constraints[i][j];
        
                // adding slack variables (s)
        for (int i = 0; i < numberOfConstraints; i++)
            tableaux[i][numberOfVariables + i] = 1.0;
        
        // adding inequality rith side (B)
        for (int i = 0; i < numberOfConstraints; i++)
            tableaux[i][numberOfVariables + numberOfConstraints + numberOfArtificialVariables] = constraintsValues[i];
            
        // adding objective function (of)
        for (int j = 0; j < numberOfVariables; j++)
            tableaux[numberOfConstraints][j] = objectiveFunction[j];

        // if negative RHS, multiply by -1
        int artificialVariableCount = 0;
        for (int i = 0; i < constraintsValues.length; i++) {
            if (constraintsValues[i] < 0) {
                for (int j = 0; j < tableaux[i].length; j++) {
                    // if (tableaux[i][j] == 0)
                    //     continue;
                    tableaux[i][j] = (-tableaux[i][j]);
                }
                // setting artifical variable (after multiply by -1, <= become >= so we need artificial variable)
                tableaux[i][numberOfVariables + numberOfConstraints + artificialVariableCount++] = 1;
            }
        }

        // setting artificial objective function (all 0 and -1 for all artificial variables)
        for (int i = 0; i < numberOfArtificialVariables; i++)
            tableaux[numberOfConstraints + 1][numberOfVariables + numberOfConstraints + i] = (-1.0);

        if (debug) {
            StdOut.println("before removing artificial variables from artificial objective function");
            show();
        }

        // removing all artificial variables (setting their value to 0) in artificial objective function
        for (int column = numberOfVariables + numberOfConstraints; column < numberOfVariables + numberOfConstraints
                + numberOfArtificialVariables; column++)
            for (int row = 0; row < numberOfConstraints; row++)
                if (tableaux[row][column] == 1) {
                    pivot(row, column);
                    break;
                }

        // at this point all basic variables are the artificial variables
        basis = new int[numberOfConstraints];
        for (int i = 0; i < numberOfConstraints; i++)
            basis[i] = numberOfVariables + numberOfConstraints + i;

        if (debug) {
            StdOut.println("before phase I");
            show();
        }

        // performing phase one
        phase1();

        if (debug) {
            StdOut.println("before phase II");
            show();
        }

        // performing phase two
        phase2();

        if (debug) {
            StdOut.println("after phase II");
            show();
        }

        // check optimality conditions
        assert check(constraints, constraintsValues, objectiveFunction);
    }

    // run phase I simplex algorithm to find basic feasible solution
    private void phase1() {
        while (true) {

            // find entering column q
            int q = bland1();
            if (q == -1)
                break; // optimal

            // find leaving row p
            int p = minRatioRule(q);
            assert p != -1 : "Entering column = " + q;

            // pivot
            pivot(p, q);

            // update basis
            basis[p] = q;
            // show();
        }
        // if after phase one the value of the objective function is > 0 (in this case EPSILON) there aro no feasible solutions
        if (tableaux[numberOfConstraints + 1][numberOfVariables + numberOfConstraints
                + numberOfArtificialVariables] > EPSILON)
            throw new ArithmeticException("No solution");
    }

    // run simplex algorithm starting from initial basic feasible solution
    private void phase2() {
        while (true) {

            // find entering column q
            int q = bland2();
            if (q == -1)
                break; // optimal

            // find leaving row p
            int p = minRatioRule(q);
            // if can't find new entering variable and the objective function is not optimized, the solution is unbounded
            if (p == -1)
                throw new ArithmeticException("Infinity");

            // pivot
            pivot(p, q);

            // update basis
            basis[p] = q;
        }
    }

    // lowest index of a non-basic column with a positive cost - using artificial
    // objective function
    private int bland1() {
        for (int j = 0; j < numberOfVariables + numberOfConstraints; j++)
            if (tableaux[numberOfConstraints + 1][j] > EPSILON)
                return j;
        return -1; // optimal
    }

    // lowest index of a non-basic column with a positive cost
    private int bland2() {
        for (int j = 0; j < numberOfVariables + numberOfConstraints; j++)
            if (tableaux[numberOfConstraints][j] > EPSILON)
                return j;
        return -1; // optimal
    }

    // find row p using min ratio rule (-1 if no such row)
    private int minRatioRule(int q) {
        int p = -1;
        double lastRatio = Double.POSITIVE_INFINITY;
        for (int i = 0; i < numberOfConstraints; i++) {
            // if (a[i][q] <= 0) continue;
            if (tableaux[i][q] <= EPSILON)
                continue;
            double ratio = (tableaux[i][numberOfVariables + numberOfConstraints + numberOfArtificialVariables] / tableaux[i][q]);
            if (p == -1) {
                p = i;
                lastRatio = ratio;
            }
            else if (ratio < lastRatio) {
                p = i;
                lastRatio = ratio;
            }
        }
        return p;
    }

    // pivot on entry (p, q) using Gauss-Jordan elimination
    private void pivot(int row, int column) {

        // everything but row p and column q
        for (int i = 0; i <= numberOfConstraints + 1; i++)
            if (i != row)
                for (int j = 0; j <= numberOfVariables + numberOfConstraints + numberOfArtificialVariables; j++)
                    if (j != column)
                        tableaux[i][j] -= tableaux[row][j] * tableaux[i][column] / tableaux[row][column];

        // zero out column q
        for (int i = 0; i <= numberOfConstraints + 1; i++)
            if (i != row)
                tableaux[i][column] = 0.0;

        // scale row p
        for (int j = 0; j <= numberOfVariables + numberOfConstraints + numberOfArtificialVariables; j++)
            if (j != column)
                tableaux[row][j] /= tableaux[row][column];
        tableaux[row][column] = 1.0;
    }

    // return optimal objective value
    public double value() {
        return -tableaux[numberOfConstraints][numberOfVariables + numberOfConstraints + numberOfArtificialVariables];
    }

    // return primal solution vector (the value of the 'x' variables)
    public double[] primal() {
        double[] x = new double[numberOfVariables];
        for (int i = 0; i < numberOfConstraints; i++)
            if (basis[i] < numberOfVariables)
                x[basis[i]] = tableaux[i][numberOfVariables + numberOfConstraints + numberOfArtificialVariables];
        return x;
    }

    // return dual solution vector (the value of the slack variables)
    public double[] dual() {
        double[] y = new double[numberOfConstraints];
        for (int i = 0; i < numberOfConstraints; i++)
            y[i] = -tableaux[numberOfConstraints][numberOfVariables + i];
        return y;
    }

    // is the solution primal feasible?
    private boolean isPrimalFeasible(double[][] constraints, double[] constraintsValues) {
        double[] x = primal();

        // check that x >= 0
        for (int j = 0; j < x.length; j++) {
            if (x[j] < 0.0) {
                if (debug)
                    StdOut.println("x[" + j + "] = " + x[j] + " is negative");
                return false;
            }
        }

        // check that Ax <= b
        for (int i = 0; i < numberOfConstraints; i++) {
            double sum = 0.0;
            for (int j = 0; j < numberOfVariables; j++) {
                sum += constraints[i][j] * x[j];
            }
            if (sum > constraintsValues[i] + EPSILON) {
                if (debug) {
                    StdOut.println("not primal feasible");
                    StdOut.println("b[" + i + "] = " + constraintsValues[i] + ", sum = " + sum);
                }
                return false;
            }
        }
        return true;
    }

    // is the solution dual feasible?
    private boolean isDualFeasible(double[][] constraints, double[] objectiveFunction) {
        double[] y = dual();

        // check that y >= 0
        for (int i = 0; i < y.length; i++) {
            if (y[i] < 0.0) {
                if (debug)
                    StdOut.println("y[" + i + "] = " + y[i] + " is negative");
                return false;
            }
        }

        // check that yA >= c
        for (int j = 0; j < numberOfVariables; j++) {
            double sum = 0.0;
            for (int i = 0; i < numberOfConstraints; i++) {
                sum += constraints[i][j] * y[i];
            }
            if (sum < objectiveFunction[j] - EPSILON) {
                if (debug) {
                    StdOut.println("not dual feasible");
                    StdOut.println("c[" + j + "] = " + objectiveFunction[j] + ", sum = " + sum);
                }
                return false;
            }
        }
        return true;
    }

    // check that optimal value = cx = yb
    private boolean isOptimal(double[] constraintsValues, double[] objectiveFunction) {
        double[] x = primal();
        double[] y = dual();
        double value = value();

        // check that value = cx = yb
        double value1 = 0.0;
        for (int j = 0; j < x.length; j++)
            value1 += objectiveFunction[j] * x[j];
        double value2 = 0.0;
        for (int i = 0; i < y.length; i++)
            value2 += y[i] * constraintsValues[i];
        if (Math.abs(value - value1) > EPSILON || Math.abs(value - value2) > EPSILON) {
            if (debug)
                StdOut.println("value = " + value + ", cx = " + value1 + ", yb = " + value2);
            return false;
        }

        return true;
    }

    private boolean check(double[][] constraints, double[] constraintsValues, double[] objectiveFunction) {
        return isPrimalFeasible(constraints, constraintsValues) && isDualFeasible(constraints, objectiveFunction) && isOptimal(constraintsValues, objectiveFunction);
    }

    // print tableaux
    public void show() {
        StdOut.println("m = " + numberOfConstraints);
        StdOut.println("n = " + numberOfVariables);
        for (int i = 0; i <= numberOfVariables + numberOfConstraints + numberOfArtificialVariables; i++) {
            StdOut.printf("%1$7s ",
                    i < numberOfVariables ? ("x" + (i + 1))
                            : i < numberOfVariables + numberOfConstraints ? ("s" + ((i - numberOfVariables) + 1))
                                    : i < numberOfVariables + numberOfConstraints + numberOfArtificialVariables
                                            ? ("a" + ((i - numberOfVariables - numberOfConstraints) + 1))
                                            : "B");
            if (i == numberOfVariables + numberOfConstraints - 1
                    || i == numberOfVariables + numberOfConstraints + numberOfArtificialVariables - 1)
                StdOut.print(" |");
        }

        StdOut.println();
        for (int i = 0; i <= numberOfConstraints + 1; i++) {
            for (int j = 0; j <= numberOfVariables + numberOfConstraints + numberOfArtificialVariables; j++) {
                StdOut.printf("%7.2f ", tableaux[i][j]);
                if (j == numberOfVariables + numberOfConstraints - 1
                        || j == numberOfVariables + numberOfConstraints + numberOfArtificialVariables - 1)
                    StdOut.print(" |");
            }
            StdOut.println();
        }
        if (basis != null) {
            StdOut.print("basis = ");
            for (int i = 0; i < numberOfConstraints; i++)
                StdOut.print(basis[i] + " ");
            StdOut.println();
        }
        StdOut.println();
    }
}
