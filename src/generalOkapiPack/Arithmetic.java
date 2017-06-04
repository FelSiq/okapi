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

	// Regular expression which detects two equals "+" or "-" operands in sequence
	private static final String sroMergeOperationsEqualsString = "(([-+])\\s*(?:\\2))";
	
	// Regular expression which detects two opposite "+" or "-" operands in sequence
	private static final String sroMergeOperationsDiffString = "((?:\\+\\s*-)|(?:-\\s*\\+))";

	//Special symbol adopted to solve a power operator glitch
	private static final String powerMarkerSymbol = "z";

	// String used to place a symbol marker before a (a)^b operation, in order to fix a signal bug
	private static final String srPreprocessPowerString = "(\\(([^()]+)\\)\\s*\\^)";

	// Get a subexpression inside a parenthesis, in order to solve it with high priority
	private static final String srGetParenthesisString = "\\(([^()]+)\\)";

	// Solve exponential subexpression
	private static final String sroPrimaryString = "((?:" + powerMarkerSymbol + "\\s*[+-]?)?\\s*[\\d.]+)\\s*(\\^)\\s*([+-]?\\s*[\\d.]+)";

	// Get multiplication and division subexpressions
	private static final String sroSecondaryString = "([+-]?\\s*[\\d.]+)\\s*([/*])\\s*([+-]?\\s*[\\d.]+)";

	// Get add and minus subexpressions
	private static final String sroTertiaryString = "([-+]?\\s*[\\d.]+)\\s*([+-])\\s*([+-]?\\s*[\\d.]+)";
	//---------------------------------------------
	// AUXILIARY VARIABLES SECTION
	// Keep precompiled regex patterns active, aiming optimization 
	private static Pattern srGetParenthesisPattern;
	private static Pattern srPreprocessPowerPattern;
	private static Pattern sroMergeOperationsEqualsStringPattern;
	private static Pattern sroMergeOperationsDiffStringPattern;
	private static Pattern sroPrimaryPattern;
	private static Pattern sroSecondaryPattern;
	private static Pattern sroTertiaryPattern;
	//---------------------------------------------

	/**
	* Class constructor. Precompiles all regex patterns of this class.
	*/
	public Arithmetic() {
		// Compile all regexes of this class
		try {
			Arithmetic.srGetParenthesisPattern = Pattern.compile(Arithmetic.srGetParenthesisString);
			Arithmetic.srPreprocessPowerPattern = Pattern.compile(Arithmetic.srPreprocessPowerString);
			Arithmetic.sroMergeOperationsEqualsStringPattern = Pattern.compile(Arithmetic.sroMergeOperationsEqualsString);
			Arithmetic.sroMergeOperationsDiffStringPattern = Pattern.compile(Arithmetic.sroMergeOperationsDiffString);
			Arithmetic.sroPrimaryPattern = Pattern.compile(Arithmetic.sroPrimaryString);
			Arithmetic.sroSecondaryPattern = Pattern.compile(Arithmetic.sroSecondaryString);
			Arithmetic.sroTertiaryPattern = Pattern.compile(Arithmetic.sroTertiaryString);
		} catch (PatternSyntaxException pse) {
			System.out.println("E: Incorrect regex pattern.");
		}
	}

	/**
	* Merge equals or opposite operators in a sequence (++, -+, -- and +-).
	* @Return Brand-new preprocessed arithmetic expression.
	* @Throws No exceptions.
	*/
	private String mergeOperators (String arithmeticExpression) {
		while(Arithmetic.sroMergeOperationsEqualsStringPattern.matcher(arithmeticExpression).find() || 
			Arithmetic.sroMergeOperationsDiffStringPattern.matcher(arithmeticExpression).find()){
			// Recalculates symbols equals or opposites in a sequence (like -- or +-)
			arithmeticExpression = arithmeticExpression.replaceAll(sroMergeOperationsEqualsString, "+");
			arithmeticExpression = arithmeticExpression.replaceAll(sroMergeOperationsDiffString, "-");
		}
		return arithmeticExpression;
	}

	/**
	* Remove all whitespaces, calls mergeOperators and insert special operators if needed.
	* @Return Brand-new preprocessed arithmetic expression.
	* @Throws No exceptions.
	*/
	private String expressionPreprocess (String arithmeticExpression) {
		// Add a initial and final parenthesis on the user arithmetic expression,
		// to remove any special cases (all expression without any parenthesis).
		arithmeticExpression = "(" + arithmeticExpression + ")";

		// Remove all whitespaces, because java parse can't handle they correctly. 
		arithmeticExpression = arithmeticExpression.replaceAll("\\s+", "");

		// Merge or equals or opposite operators in sequence (++, --, +-, -+)
		arithmeticExpression = mergeOperators(arithmeticExpression);

		// Preprocess power operation, in order to fix a signal bug
		Matcher auxMatcher = Arithmetic.srPreprocessPowerPattern.matcher(arithmeticExpression);
		if (auxMatcher.find())
			arithmeticExpression = arithmeticExpression.replaceAll(srPreprocessPowerString, powerMarkerSymbol + auxMatcher.group(1));
		
		// Returns brand-new preprocessed String.
		return arithmeticExpression;
	}


	/**
	* Given a arithmetic expression String, return its value, if possible.
	* @Throws IllegalArithmeticExpression, if the given arithmetic expression is unsuported by any means.
	*/
	public Double solve(String arithmeticExpression) throws IllegalArithmeticExpression {
		Double partialResult = 0.0;
		Double operandA, operandB;
		String operator, auxString;
		int i, j, k;

		// Preprocess given arithmetic expression.
		arithmeticExpression = this.expressionPreprocess(arithmeticExpression);

		// Matches all inner parenthesis (1)
		Matcher matchedSubexpressions = Arithmetic.srGetParenthesisPattern.matcher(arithmeticExpression);
		Matcher matcherOperations = null;

		try {
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
							operandA = Double.parseDouble(matcherOperations.group(1).replaceAll(powerMarkerSymbol, ""));
							//operandA = Double.parseDouble(matcherOperations.group(1));

							// Get subexpression B operand
							operandB = Double.parseDouble(matcherOperations.group(3).replaceAll(powerMarkerSymbol, ""));

							switch (k) {
								// Solve max priority operation (^)
								case 0: 
									partialResult = Math.pow(operandA, operandB);
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
					// Replace the current partial result into the arithmetic expression (this replaces a full parenthesis)
					arithmeticExpression = arithmeticExpression.replaceFirst(srGetParenthesisString, auxString);

					// Recalculates symbols equals or opposites in a sequence (like -- or +-)
					arithmeticExpression = this.mergeOperators(arithmeticExpression);
				}
				// Matches all inner parenthesis (2)
				matchedSubexpressions = Arithmetic.srGetParenthesisPattern.matcher(arithmeticExpression);
			}
			//Return result.
			return Double.parseDouble(arithmeticExpression);
		} catch (Exception e) {
			throw new IllegalArithmeticExpression();
		}
	}
}