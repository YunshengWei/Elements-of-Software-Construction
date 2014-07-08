package sat;

import immutable.ImList;
import immutable.EmptyImList;
import sat.env.Bool;
import sat.env.Variable;
import sat.env.Environment;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.PosLiteral;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {
    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     * 
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.
     */
    public static Environment solve(Formula formula) {
        // TODO: implement this.
        return solve(formula.getClauses(), new Environment());
    }

    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     * 
     * @param clauses
     *            formula in conjunctive normal form
     * @param env
     *            assignment of some or all variables in clauses to true or
     *            false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     *         or null if no such environment exists.
     */
    private static Environment solve(ImList<Clause> clauses, Environment env) {
        // TODO: implement this.
        if (clauses.isEmpty()) {
            return env;
        }
        Clause minC = clauses.first();
        for (Clause c : clauses) {
            if (c.isEmpty()) {
                return null;
            }
            if (c.size() < minC.size()) {
                minC = c;
            }
        }
        
        Literal l = minC.chooseLiteral();
        Variable v = l.getVariable();
        Bool b;
        if (l instanceof PosLiteral) {
            b = Bool.TRUE;
        } else {
            b = Bool.FALSE;
        }
        if (minC.isUnit()) {
            return solve(substitute(clauses, l), env.put(v, b));
        } else {
            Environment e = solve(substitute(clauses, l), env.put(v, b));
            if (e == null) {
                return solve(substitute(clauses, l.getNegation()), env.put(v, b.not()));
            } else {
                return e;
            }
        }
    }

    /**
     * given a clause list and literal, produce a new list resulting from
     * setting that literal to true
     * 
     * @param clauses
     *            , a list of clauses
     * @param l
     *            , a literal to set to true
     * @return a new list of clauses resulting from setting l to true
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses,
            Literal l) {
        // TODO: implement this.
        ImList<Clause> newClauses = new EmptyImList<Clause>();
        for (Clause c: clauses) {
            Clause newClause = c.reduce(l);
            if (newClause != null) {
                newClauses = newClauses.add(newClause);
            }
        }
        return newClauses;
    }

}
