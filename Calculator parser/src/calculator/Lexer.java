package calculator;

import calculator.Type;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Calculator lexical analyzer.
 */
public class Lexer {
    private final String s;
    private int i = 0;
    private final Matcher matcher;
    
    // Regex matching the next token.
    private static final Pattern TOKEN_REGEX
        = Pattern.compile(
                "(\\+)"
                + "|(-)"
                + "|(\\*)"
                + "|(/)"
                + "|(pt)"
                + "|(in)"
                + "|([\\d]+(\\.[\\d]*)?" // 1.2 or 1 or 1.
                + "|\\.[\\d]+)"           // .5
                + "|(\\()"
                + "|(\\))"
          );
    
    // The token types for each of the parenthesized patterns in TOKEN_REGEX.
    private static final Type[] TOKEN_TYPES
        = {
            Type.EOF,
            Type.PLUS,
            Type.MINUS,
            Type.TIMES,
            Type.DIVIDE,
            Type.POINTS,
            Type.INCHES,
            Type.SCALAR,
            Type.SCALAR,
            Type.OPEN_PAREN,
            Type.CLOSE_PAREN
          };

	/**
	 * Token in the stream.
	 */
	public static class Token {
		final Type type;
		final String text;

		Token(Type type, String text) {
			this.type = type;
			this.text = text;
		}

		Token(Type type) {
			this(type, null);
		}
	}

	@SuppressWarnings("serial")
	static class TokenMismatchException extends Exception {
	    TokenMismatchException(String msg) {
	        super(msg);
	    }
	}
	
	/**
	 * Lexer is used to convert the user input
	 * String into a set of tokens that parser will
	 * be able to recognize. 
	 * 
	 * @param input: the user input String 
	 */
	public Lexer(String input) {
	    this.s = input;
		this.matcher = TOKEN_REGEX.matcher(s);
	}
	
	/** 
	 * return the next Token in the input String.
	 * If reach the end of input String, return Type.EOF.
	 * 
	 * @return the next Token in the input String.
	 * @throws TokenMismatchException
	 */
	public Token next() throws TokenMismatchException {
	    if (i >= s.length())
	        return new Token(Type.EOF);
	    
	    if (!matcher.find(i)) {
	        throw new TokenMismatchException("token mismatch at " + s.substring(i));
	    }
	    
	    String value = matcher.group(0);
	    i = matcher.end();
	    
	    for (int i = 1; i <= matcher.groupCount(); i++) {
	        if (matcher.group(i) != null) {
	            return new Token(TOKEN_TYPES[i], value);
	        }
	    }
	    
	    throw new AssertionError("shouldn't get here");
	}
	
	// Unit test
	public static void main(String[] args) throws TokenMismatchException{
	    Lexer lex = new Lexer("( 2 in+ (5.)pt) / .6 in");
	    for (Token tok = lex.next(); tok.type != Type.EOF; tok = lex.next()) {
	        System.out.println(tok.type);
	        System.out.println(tok.text);
	    }
	}
}
