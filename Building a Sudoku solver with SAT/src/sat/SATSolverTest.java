package sat;

import static org.junit.Assert.*;

import org.junit.Test;

import sat.formula.Clause;
import sat.formula.Literal;
import sat.formula.PosLiteral;
import sat.formula.Formula;
import sat.env.Environment;
import sat.env.Variable;
import sat.env.Bool;

public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();

    // make sure assertions are turned on!  
    // we don't want to run test cases without assertions too.
    // see the handout to find out how to turn them on.
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false;
    }

    // TODO: put your test cases here
    @Test
    public void testSolve1() {
        Formula f = new Formula();
        Clause c = new Clause();
        c = c.add(a);
        c = c.add(nb);
        f = f.addClause(c);
        c = new Clause();
        c = c.add(a);
        c = c.add(b);
        f = f.addClause(c);
        Environment e = SATSolver.solve(f);
        assertTrue(e.get(new Variable("a")) == Bool.TRUE);
        assertTrue(e.get(new Variable("b")) == Bool.TRUE);
    }
    
    @Test
    public void testSolve2() {
        Formula f = new Formula();
        f = f.addClause(new Clause(a));
        f = f.addClause(new Clause(b));
        f = f.addClause(new Clause(a));
        f = f.addClause(new Clause(nb));
        Environment e = SATSolver.solve(f);
        assertTrue(e == null);
    }
    
    @Test
    public void testSolve3() {
        Formula f = new Formula();
        f = f.addClause(new Clause(a));
        f = f.addClause(new Clause(b));
        f = f.addClause(new Clause(nb).add(c));
        Environment e = SATSolver.solve(f);
        assertTrue(e.get(new Variable("a")) == Bool.TRUE);
        assertTrue(e.get(new Variable("b")) == Bool.TRUE);
        assertTrue(e.get(new Variable("c")) == Bool.TRUE);
    }

    
}