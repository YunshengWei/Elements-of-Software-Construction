package sudoku;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.Assert;

import sudoku.Sudoku.ParseException;


public class SudokuTest {
    

    // make sure assertions are turned on!  
    // we don't want to run test cases without assertions too.
    // see the handout to find out how to turn them on.
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false;
    }

    // TODO: put your test cases here
    @Test
    public void testFromFile() throws IOException, ParseException {
        String filename = "samples" + File.separator + "sudoku_4x4.txt";
        Sudoku sudoku = Sudoku.fromFile(2, filename);
        Assert.assertTrue(sudoku.toString().equals(".234\n341.\n214.\n.321\n"));
    }
}