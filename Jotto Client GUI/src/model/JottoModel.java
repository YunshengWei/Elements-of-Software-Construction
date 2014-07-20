package model;

import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * TODO Write the specification for JottoModel
 */
public class JottoModel {
    
    private final String partialURL = "http://courses.csail.mit.edu/6.005/jotto.py";
    private int puzzleNumber;
    
    public JottoModel(int puzzleNumber) {
        this.puzzleNumber = puzzleNumber;
    }
    
    public void setPuzzleNumber(int puzzleNumber) {
        this.puzzleNumber = puzzleNumber;
    }
    
    public int getPuzzleNumber() {
        return puzzleNumber;
    }
    
    /**
     * TODO Write the specification for makeGuess
     */
    public String makeGuess(String guess) {
        // TODO Problem 1
        String response = null;
        try {
            URL url = new URL(String.format("%s?puzzle=%s&guess=%s", partialURL, puzzleNumber, guess));
            try (
                    BufferedReader in = new BufferedReader(
                    new InputStreamReader(url.openStream()))
                    ) {
                response = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return response;
    }
}