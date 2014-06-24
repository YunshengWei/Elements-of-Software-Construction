package calculator;

import calculator.Lexer;
import calculator.Lexer.Token;
import calculator.Type;

import java.util.Stack;
import java.util.ArrayList;

/*
 * Grammar:
 *   EXP :: = EXP OPERATOR EXP
 *   EXP :: = ( EXP )
 *   EXP :: = LENGTH
 *   EXP :: = LENGTH OPERATOR LENGTH
 *   LENGTH :: = SCALAR UNIT
 *   LENGTH :: = SCALAR
 *   
 *   OPERATOR includes +, -, *, /
 */

/**
 * Calculator parser. All values are measured in pt.
 */
class Parser {
    @SuppressWarnings("serial")
    static class ParserException extends RuntimeException {
        ParserException(String msg) {
            super(msg);
        }
    }

    /**
     * Type of values.
     */
    private enum ValueType {
        POINTS, INCHES, SCALAR
    };

    /**
     * Internal value is always in points.
     */
    public class Value {
        final double value;
        final ValueType type;

        Value(double value, ValueType type) {
            this.value = value;
            this.type = type;
        }

        @Override
        public String toString() {
            switch (type) {
            case INCHES:
                return value / PT_PER_IN + " in";
            case POINTS:
                return value + " pt";
            default:
                return "" + value;
            }
        }
    }

    /**
     * operate on value1 and value2 according to operator.
     * 
     * @param value1
     *            : the first operand, value1.value measured in pt.
     * @param value2
     *            : the second operand, value2.value measured in pt.
     * @param operator
     *            : the operator type, should be one of +, -, *, /
     * @return Value representing the result of the operation. Value.type is the
     *         same to value1.type if any.
     */
    private Value operate(Value value1, Value value2, Type operator) {
        Value result;
        ValueType resultType;
        if (value1.type == ValueType.SCALAR) {
            resultType = value2.type;
        } else {
            resultType = value1.type;
        }
        switch (operator) {
        case PLUS:
            if (value1.type == ValueType.SCALAR && value2.type == ValueType.INCHES) {
                value1 = new Value(value1.value * PT_PER_IN, value1.type);
            } else if (value2.type == ValueType.SCALAR && value1.type == ValueType.INCHES) {
                value2 = new Value(value2.value * PT_PER_IN, value2.type);
            }
            result = new Value(value1.value + value2.value, resultType);
            break;
        case MINUS:
            if (value1.type == ValueType.SCALAR && value2.type == ValueType.INCHES) {
                value1 = new Value(value1.value * PT_PER_IN, value1.type);
            } else if (value2.type == ValueType.SCALAR && value1.type == ValueType.INCHES) {
                value2 = new Value(value2.value * PT_PER_IN, value2.type);
            }
            result = new Value(value1.value - value2.value, resultType);
            break;
        case TIMES:
            result = new Value(value1.value * value2.value, resultType);
            break;
        case DIVIDE:
            if (value1.type != ValueType.SCALAR
                    && value2.type != ValueType.SCALAR) {
                resultType = ValueType.SCALAR;
            }
            result = new Value(value1.value / value2.value, resultType);
            break;
        default:
            throw new ParserException("Operator can not be recognized.");
        }
        return result;
    }

    private static final double PT_PER_IN = 72;
    private final Lexer lexer;

    /**
     * Parser is used to parse the grammar of user input String.
     * 
     * @param lexer
     *            : constructed on user input String
     */
    Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    /**
     * Evaluate the arithmetic expression.
     * 
     * @return Value representing the value and unit of the evaluated result
     */
    public Value evaluate() throws Lexer.TokenMismatchException {
        // Infix to Postfix
        ArrayList<Token> al = new ArrayList<Token>();
        Stack<Token> st = new Stack<Token>();
        while (true) {
            Token tk = lexer.next();
            if (tk.type == Type.SCALAR || tk.type == Type.POINTS
                    || tk.type == Type.INCHES) {
                al.add(tk);
            } else if (tk.type == Type.EOF) {
                while (!st.empty()) {
                    Token t = st.pop();
                    if (t.type != Type.OPEN_PAREN && t.type != Type.CLOSE_PAREN) {
                        al.add(t);
                    }
                }
                break;
            } else if (tk.type == Type.CLOSE_PAREN) {
                Token t = st.pop();
                while (t.type != Type.OPEN_PAREN) {
                    al.add(t);
                    t = st.pop();
                }
            } else {
                st.push(tk);
            }
        }
        
        // Evaluate Postfix
        Stack<Value> stv = new Stack<Value>();
        for (Token tk : al) {
            if (tk.type == Type.SCALAR) {
                stv.push(new Value(Double.parseDouble(tk.text), ValueType.SCALAR));
            } else if (tk.type == Type.POINTS) {
                stv.push(new Value(stv.pop().value, ValueType.POINTS));
            } else if (tk.type == Type.INCHES) {
                stv.push(new Value(stv.pop().value * PT_PER_IN, ValueType.INCHES));
            } else {
                Value value2 = stv.pop();
                Value value1 = stv.pop();
                stv.push(operate(value1, value2, tk.type));
            }
        }
        return stv.pop();
    }
    
    public static void main(String[] args) throws Lexer.TokenMismatchException {
        String s = "4pt+((3in*2.4))";
        Lexer lex = new Lexer(s);
        Parser parser = new Parser(lex);
        System.out.println(parser.evaluate());
    }
}
