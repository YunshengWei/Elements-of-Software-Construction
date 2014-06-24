package calculator;

import org.junit.Assert;
import org.junit.Test;

public class CalculatorTest {

	@Test
	public void testMultiUnitCalculator() throws Lexer.TokenMismatchException{
	    MultiUnitCalculator calculator = new MultiUnitCalculator();
	    Assert.assertTrue(approxEquals(calculator.evaluate("3+2.4"), "5.4", true));
	    Assert.assertTrue(approxEquals(calculator.evaluate("3 + 2.4"), "5.4", true));
	    Assert.assertTrue(approxEquals(calculator.evaluate("1 - 2.4"), "-1.4", true));
	    Assert.assertTrue(approxEquals(calculator.evaluate("(3 + 4)*2.4"), "16.8", true));
	    Assert.assertTrue(approxEquals(calculator.evaluate("3 + 2.4in"), "5.4in", true));
	    Assert.assertTrue(approxEquals(calculator.evaluate("3pt * 2.4in"), "518.4pt", true));
	    Assert.assertTrue(approxEquals(calculator.evaluate("3in * 2.4"), "7.2in", true));
	    Assert.assertTrue(approxEquals(calculator.evaluate("4pt+(3in*2.4)"), "522.4pt", true));
	    Assert.assertTrue(approxEquals(calculator.evaluate("4pt+((3in*2.4))"), "522.4pt", true));
	    Assert.assertTrue(approxEquals(calculator.evaluate("(3 + 2.4) in"), "5.4in", true));
	    Assert.assertTrue(approxEquals(calculator.evaluate("(3in * 2.4) pt"), "518.4pt", true));
	    Assert.assertTrue(approxEquals(calculator.evaluate("2.4pt / 3"), "0.8pt", true));
	    Assert.assertTrue(approxEquals(calculator.evaluate("2.4pt / 3pt"), "0.8", true));
	    Assert.assertTrue(approxEquals(calculator.evaluate("2.4in / 3pt"), "57.6", true));
	    Assert.assertTrue(approxEquals(calculator.evaluate(".5pt * 3."), "1.5pt", true));
	}

	boolean approxEquals(String expr1, String expr2, boolean compareUnits) {
		return new Value(expr1).approxEquals(new Value(expr2), compareUnits);
	}

	static class Value {
		static float delta = 0.001f;
 
		enum Unit {
			POINT, INCH, SCALAR
		}

		Unit unit;
		// in points if a length
		float value;

		Value(String value) {
			value = value.trim();
			if (value.endsWith("pt")) {
				unit = Unit.POINT;
				this.value = Float.parseFloat(value.substring(0,
						value.length() - 2).trim());
			} else if (value.endsWith("in")) {
				unit = Unit.INCH;
				this.value = 72 * Float.parseFloat(value.substring(0,
						value.length() - 2).trim());
			} else {
				unit = Unit.SCALAR;
				this.value = Float.parseFloat(value);
			}
		}

		boolean approxEquals(Value that, boolean compareUnits) {
			return (this.unit == that.unit || !compareUnits)
					&& Math.abs(this.value - that.value) < delta;
		}
	}
}
