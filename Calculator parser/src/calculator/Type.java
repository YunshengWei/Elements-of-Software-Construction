package calculator;

/*
 * Type should include:
 * +, -, *, /,
 * pt, in,
 * integer, decimal,
 * (, ).
 */

/**
 * Token type.
 */
enum Type {
    EOF, // end of file
    PLUS, // +
    MINUS, // -
    TIMES, // *
    DIVIDE, // /
    OPEN_PAREN, // (
    CLOSE_PAREN, // )
    SCALAR, // number
    POINTS, // pt
    INCHES, // in
}