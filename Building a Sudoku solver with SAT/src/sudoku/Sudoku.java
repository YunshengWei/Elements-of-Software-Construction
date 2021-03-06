/**
 * Author: dnj, Hank Huang
 * Date: March 7, 2009
 * 6.005 Elements of Software Construction
 * (c) 2007-2009, MIT 6.005 Staff
 */
package sudoku;

import java.io.*;

import sat.env.Environment;
import sat.env.Variable;
import sat.env.Bool;
import sat.formula.Formula;
import sat.formula.Clause;
import sat.formula.PosLiteral;
import sat.formula.NegLiteral;

/**
 * Sudoku is an immutable abstract datatype representing instances of Sudoku.
 * Each object is a partially completed Sudoku puzzle.
 */
public class Sudoku {
    // dimension: standard puzzle has dim 3
    private final int dim;
    // number of rows and columns: standard puzzle has size 9
    private final int size;
    // known values: square[i][j] represents the square in the ith row and jth
    // column,
    // contains -1 if the digit is not present, else i>=0 to represent the digit
    // i+1
    // (digits are indexed from 0 and not 1 so that we can take the number k
    // from square[i][j] and
    // use it to index into occupies[i][j][k])
    private final int[][] square;
    // occupies [i,j,k] means that kth symbol occupies entry in row i, column j
    private final Variable[][][] occupies;

    // Rep invariant:
    // size == dim * dim
    // square.length == size
    // square[i].length == size for 0 <= i < dim
    // square[i][j] is in {-1, 0, 1, ..., size - 1} for 0 <= i, j < dim
    private void checkRep() {
        // TODO: implement this.
        // check whether assertions are turned on.
        // if they're not on, we want to avoid all the recursive
        // traversal that checkRep() would do.
        try {
            assert false;
        } catch (AssertionError e) {
            assert size == dim * dim : "Sudoku, Rep invariant: size == dim * dim";
            assert square.length == size : "Sudoku, Rep invariant: size == square.length";
            for (int i = 0; i < size; i++) {
                assert size == square[i].length : "Sudoku, Rep invariant: "
                    + "square[i].length == size for 0 <=i < dim";
                for (int j = 0; j < size; j++) {
                    assert square[i][j] >= -1 && square[i][j] < size :
                        "Sudoku, Rep invariant: square[i][j] is in "
                      + "{-1, 0, 1, ..., size - 1} for 0 <= i, j < dim";
                }
            }
        }
        
    }

    /**
     * create an empty Sudoku puzzle of dimension dim.
     * 
     * @param dim
     *            size of one block of the puzzle. For example, new Sudoku(3)
     *            makes a standard Sudoku puzzle with a 9x9 grid.
     */
    public Sudoku(int dim) {
        // TODO: implement this.
        this.dim = dim;
        this.size = dim * dim;
        this.square = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                square[i][j] = -1;
            }
        }
        occupies = new Variable[size][size][size];
        for (Integer i = 0; i < size; i++) {
            for (Integer j = 0; j < size; j++) {
                for (Integer k = 0; k < size;k ++) {
                    occupies[i][j][k] = new Variable(i.toString() + j.toString() + k.toString());
                }
            }
        }
        checkRep();
    }

    /**
     * create Sudoku puzzle
     * 
     * @param square
     *            digits or blanks of the Sudoku grid. square[i][j] represents
     *            the square in the ith row and jth column, contains 0 for a
     *            blank, else i to represent the digit i. So { { 0, 0, 0, 1 }, {
     *            2, 3, 0, 4 }, { 0, 0, 0, 3 }, { 4, 1, 0, 2 } } represents the
     *            dimension-2 Sudoku grid:
     * 
     *            ...1 23.4 ...3 41.2
     * 
     * @param dim
     *            dimension of puzzle Requires that dim*dim == square.length ==
     *            square[i].length for 0<=i<dim.
     */
    public Sudoku(int dim, int[][] square) {
        // TODO: implement this.
        this(dim);
        for (int i = 0; i < square.length; i++) {
            for (int j = 0; j < square[i].length; j++) {
                if (square[i][j] == 0) {
                    this.square[i][j] = -1;
                } else {
                    this.square[i][j] = square[i][j] - 1;
                }
            }
        }
        checkRep();
    }

    /**
     * Reads in a file containing a Sudoku puzzle.
     * 
     * @param dim
     *            Dimension of puzzle. Requires: at most dim of 3, because
     *            otherwise need different file format
     * @param filename
     *            of file containing puzzle. The file should contain one line
     *            per row, with each square in the row represented by a digit,
     *            if known, and a period otherwise. With dimension dim, the file
     *            should contain dim*dim rows, and each row should contain
     *            dim*dim characters.
     * @return Sudoku object corresponding to file contents
     * @throws IOException
     *             if file reading encounters an error
     * @throws ParseException
     *             if file has error in its format
     */
    public static Sudoku fromFile(int dim, String filename) throws IOException,
            ParseException {
        // TODO: implement this.
        int size = dim * dim;
        int[][] square = new int[size][size];
        try (BufferedReader reader = 
                new BufferedReader(new FileReader(new File(filename)))) {
            try {
                for (int i = 0; i < size; i++) {
                    String line = reader.readLine();
                    for (int j = 0; j < size; j++) {
                        if (line.charAt(j) == '.') {
                            square[i][j] = 0;
                        } else {
                            square[i][j] = Integer.parseInt(line.substring(j, j + 1));
                        }
                    }
                }
            } catch (Exception ex) {
                throw new ParseException("Grammatical errors in " + filename);
            }
        }
        return new Sudoku(dim, square);
    }

    /**
     * Exception used for signaling grammatical errors in Sudoku puzzle files
     */
    @SuppressWarnings("serial")
    public static class ParseException extends Exception {
        public ParseException(String msg) {
            super(msg);
        }
    }

    /**
     * Produce readable string representation of this Sukoku grid, e.g. for a 4
     * x 4 sudoku problem: 12.4 3412 2.43 4321
     * 
     * @return a string corresponding to this grid
     */
    public String toString() {
        // TODO: implement this.
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (square[i][j] == -1) {
                    sb.append('.');
                } else {
                    sb.append(square[i][j] + 1);
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    /**
     * @return a SAT problem corresponding to the puzzle, using variables with
     *         names of the form occupies(i,j,k) to indicate that the kth symbol
     *         occupies the entry in row i, column j
     */
    public Formula getProblem() {
        // TODO: implement this.
        Formula formula = new Formula();
        // Solution must be consistent with the starting grid.
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (square[i][j] != -1) {
                    formula = formula.and(new Formula(occupies[i][j][square[i][j]]));
                }
            }
        }
        // At most one digit per square.
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k1 = 0; k1 < size; k1++) {
                    for (int k2 = k1 + 1; k2 < size; k2++) {
                        formula = formula.addClause(new Clause().add(
                                NegLiteral.make(occupies[i][j][k1])).add(
                                        NegLiteral.make(occupies[i][j][k2])));
                    }
                }
            }
        }
        // In each row, each digit must appear exactly once.
        // Add redundant constraints to speed up backtracking
        for (int i = 0; i < size; i++) {
            for (int k = 0; k < size; k++) {
                Clause clause = new Clause();
                for (int j = 0; j < size; j++) {
                    clause = clause.add(PosLiteral.make(occupies[i][j][k]));
                }
                formula = formula.addClause(clause);
                for (int j1 = 0; j1 < size; j1++) {
                    for (int j2 = j1 + 1; j2 < size; j2++) {
                        formula = formula.addClause(new Clause().add(
                                NegLiteral.make(occupies[i][j1][k])).add(
                                        NegLiteral.make(occupies[i][j2][k])));
                    }
                }
            }
        }
        // In each column, each digit must appear exactly once.
        // Add redundant constraints to speed up backtracking
        for (int j = 0; j < size; j++) {
            for (int k = 0; k < size; k++) {
                Clause clause = new Clause();
                for (int i = 0; i < size; i++) {
                    clause = clause.add(PosLiteral.make(occupies[i][j][k]));
                }
                formula = formula.addClause(clause);
                for (int i1 = 0; i1 < size; i1++) {
                    for (int i2 = i1 + 1; i2 < size; i2++) {
                        formula = formula.addClause(new Clause().add(
                                NegLiteral.make(occupies[i1][j][k])).add(
                                        NegLiteral.make(occupies[i2][j][k])));
                    }
                }
            }
        }
        // In each block, each digit must appear exactly once.
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                for (int k = 0; k < size; k++) {
                    Clause clause = new Clause();
                    for (int s = 0; s < dim; s++) {
                        for (int t = 0; t < dim; t++) {
                            clause = clause.add(PosLiteral.make(occupies[i*dim+s][j*dim+t][k]));
                        }
                    }
                    formula = formula.addClause(clause);
                }
            }
        }
        System.out.println(formula.getSize());
        return formula;
    }

    /**
     * Interpret the solved SAT problem as a filled-in grid.
     * 
     * @param e
     *            Assignment of variables to values that solves this puzzle.
     *            Requires that e came from a solution to this.getProblem().
     * @return a new Sudoku grid containing the solution to the puzzle, with no
     *         blank entries.
     */
    public Sudoku interpretSolution(Environment e) {
        // TODO: implement this.
        int square[][] = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    if (e.get(occupies[i][j][k]) == Bool.TRUE) {
                        square[i][j] = k + 1;
                        break;
                    }
                }
            }
        }
        return new Sudoku((int)(Math.sqrt((double)size)), square);
    }

}
