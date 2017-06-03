package generalOkapiPack;
//---------------------------------------------
//IMPORT SECTION 
//1. For regex (interpreting and solving the arithmetic expression)
import java.util.regex.PatternSyntaxException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
//---------------------------------------------

/**
* This class should solve every suported arithmetic expression.
*/
public class Arithmetic extends GeneralOkapi {
	//---------------------------------------------
	// CONSTANT SECTION

	// Get a subexpression inside a parenthesis, in order to solve it with high priority
	private static final String srGetParenthesisString = "\\(([^()]+)\\)";

	// Solve exponential subexpression
	private static final String sroPrimaryString = "((?:\\(\\s*[+-]?\\s*[\\d.]+\\s*\\))|(?:[\\d.]+))\\s*(\\^)\\s*([+-]?\\s*[\\d.]+)";

	// Get multiplication and division subexpressions
	private static final String sroSecondaryString = "((?:[+-]\\s*)?\\s*[\\d.]+)\\s*([/*])\\s*((?:[+-]\\s*)?\\s*[\\d.]+)";

	// Get add and minus subexpressions
	private static final String sroTertiaryString = "((?:[-+]\\s*)?\\s*[\\d.]+)\\s*([+-])\\s*((?:[+-]\\s*)?\\s*[\\d.]+)";
	//---------------------------------------------
	// AUXILIARY VARIABLES SECTION
	// Keep precompiled regex patterns active
	private static Pattern srGetParenthesisPattern;
	private static Pattern sroPrimaryPattern;
	private static Pattern sroSecondaryPattern;
	private static Pattern sroTertiaryPattern;
	//---------------------------------------------

	/**
	* Class constructor. Precompiles all regex patterns of this class.
	*/
	public Arithmetic() {
		try {
			Arithmetic.srGetParenthesisPattern = Pattern.compile(Arithmetic.srGetParenthesisString);
			Arithmetic.sroPrimaryPattern = Pattern.compile(Arithmetic.sroPrimaryString);
			Arithmetic.sroSecondaryPattern = Pattern.compile(Arithmetic.sroSecondaryString);
			Arithmetic.sroTertiaryPattern = Pattern.compile(Arithmetic.sroTertiaryString);
		} catch (PatternSyntaxException pse) {
			System.out.println("E: Incorrect regex pattern.");
		}
	}

	public Double solve(String arithmeticExpression) {
		Double partialResult = 0.0;
		Double operandA, operandB;
		String operator, auxString;
		int i, j, k;

		// Add a initial and final parenthesis on the user arithmetic expression,
		// to remove any special cases (all expression without any parenthesis).
		arithmeticExpression = "(" + arithmeticExpression + ")";

		// Matches all inner parenthesis (1)
		Matcher matchedSubexpressions = Arithmetic.srGetParenthesisPattern.matcher(arithmeticExpression);
		Matcher matcherOperations = null;

		while (matchedSubexpressions.find()) {
			for (i = 1; i < (matchedSubexpressions.groupCount() + 1); i++) {
				auxString = matchedSubexpressions.group(i);

				for (k = 0; k < 3; k++) {
					switch (k) {
						// Solve subexpression first operations (^)
						case 0: matcherOperations = sroPrimaryPattern.matcher(auxString); break;
						
						// Solve subexpression second operations (* and /)
						case 1: matcherOperations = sroSecondaryPattern.matcher(auxString); break;

						// Solve subexpression third operations (+ and -)
						default: matcherOperations = sroTertiaryPattern.matcher(auxString); break;
					}

					while (matcherOperations.find()) {

						// Get subexpression operator
						operator = matcherOperations.group(2);

						// Get subexpression A operand
						operandA = Double.parseDouble(matcherOperations.group(1));

						// Get subexpression B operand
						operandB = Double.parseDouble(matcherOperations.group(3));

						switch (k) {
							// Solve max priority operation (^)
							case 0: 
								partialResult = Math.pow(
									Double.parseDouble(matcherOperations.group(1)), 
									Double.parseDouble(matcherOperations.group(3)));
								auxString = auxString.replaceFirst(Arithmetic.sroPrimaryString, partialResult.toString());
								matcherOperations = Arithmetic.sroPrimaryPattern.matcher(auxString);
								break;

							// Solve medium priority operation (* and /)
							case 1: 
								partialResult = ((operator.equals("*")) ? (operandA * operandB) : (operandA / operandB));
								auxString = auxString.replaceFirst(Arithmetic.sroSecondaryString, partialResult.toString());
								matcherOperations = Arithmetic.sroSecondaryPattern.matcher(auxString);
								break;

							// Solve low priority operation (+ and -)
							default: 
								partialResult = ((operator.equals("+")) ? (operandA + operandB) : (operandA - operandB));
								auxString = auxString.replaceFirst(Arithmetic.sroTertiaryString, partialResult.toString());
								matcherOperations = Arithmetic.sroTertiaryPattern.matcher(auxString);
								break;
						}
					}
				}
				//Replace the current partial result into the arithmetic expression (this replaces a full parenthesis)
				arithmeticExpression = arithmeticExpression.replaceFirst(srGetParenthesisString, auxString);
			}
			// Matches all inner parenthesis (2)
			matchedSubexpressions = Arithmetic.srGetParenthesisPattern.matcher(arithmeticExpression);
		}
		//result = arithmeticExpression;
		return Double.parseDouble(arithmeticExpression);
	}
}