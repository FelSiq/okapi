//---------------------------------------------
//IMPORT SECTION 
//0. Basic stuff (general stuff)
import java.util.Comparator;
import java.util.Scanner;
import java.util.Arrays;
import generalOkapiPack.*;
import plotsOkapiPack.*;

//1. For regex (interpreting the user input)
import java.util.regex.PatternSyntaxException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

//2. For reflection (calling the right input method)
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
//---------------------------------------------

/**
* Class used for Interpreting the user input, and doing the
* right action after it, whatever if input is or not a valid one.
*/

public class Interpreter {
	//---------------------------------------------
	// VARIABLE SECTION
	// Tell if exit command is or is not been called.
	private static boolean endOfProgram = false;

	// Standard user input getter
	private static final Scanner inputScanner = new Scanner (System.in);

	// Hold all this class declared method names, to match the user input
	private static final Method [] interpreterMethods = Interpreter.class.getDeclaredMethods();
	
	// Tell how many commands are available, on this program version, to recognition
	private static final int commandQuantity = (Interpreter.interpreterMethods.length - 1);
	
	// This is the default regex to match user input
	private static final String defaultRegexString = "\\b(\\w+)(?:(?:\\s+(\\w+)\\s*=\\s*(\\w+))?)*\\s*\\b";

	// If this regex matches, then the given command is not a arithmetic expression
	private static final String negatedArithmeticRegexString = "[^-+*/^()0-9\\s.]+";

	// Checks if there is at least on redefinition of a string parameter from a user input
	private static final String checkInputRedundancyString = "\\b(\\w+)\\s*=.+(?:\\1)(?=\\s*=)\\b";

	// These variables holds the regex compiled pattern, used to process user input
	private Pattern mainRegexPattern = null; 
	private Pattern negatedArithmeticRegexPattern = null; 
	private Pattern checkInputRedundancyPattern = null; 

	// Instantiation of arithmetic expression solver
	private static final Arithmetic arithmeticSolver = new Arithmetic();

	//---------------------------------------------
	//CLASS CONSTRUCTOR
	public Interpreter() {
		try {
			//If this regex have a match, then the input string has at least two equal words  
			this.checkInputRedundancyPattern = Pattern.compile(Interpreter.checkInputRedundancyString);
			//Compile negated arithmetic expression. If this pattern has at least match, then the
			//current user input can not be a arithmetic expression.
			this.negatedArithmeticRegexPattern = Pattern.compile(Interpreter.negatedArithmeticRegexString);

			//Compile general regex, used to match both main command and its parameters 
			this.mainRegexPattern = Pattern.compile(Interpreter.defaultRegexString);
			
			//Sort the method list of Interpreter Class, by name, in order to use binary searchs
			Arrays.sort(interpreterMethods, 
				new Comparator <Method>() {
					public int compare(Method a, Method b) {
						return a.getName().compareTo(b.getName());
					}
				});
		} catch (PatternSyntaxException pse) {
			System.out.println("E: Incorrect regex pattern.");
		}
	}
	//---------------------------------------------
	//TERTIARY METHODS SECTION (GETTERS/SETTERS)
	/**
	* Check if "exit" command was given by user.
	* @Throws No exceptions.
	* @Return True if "exit" command was given already. False otherwise.
	*/
	public boolean programEnds() {
		return this.endOfProgram;
	}

	//---------------------------------------------
	//SECONDARY METHODS SECTION

	/**
	* Display a default error message, if current command is a invalid one.
	* @Throws No exceptions.
	* @Return Always null.
	*/
	private Boolean callInvalidMethod() {
		System.out.println("E: this command is invalid!");
		return null;
	} 

	/**
	* Set flag "endOfProgram" to true. Further interpretations of this flag should be handled by another class.
	* @Throws No exceptions.
	* @Return Always true.
	*/
	private Boolean exit() {
		System.out.println("System: program will now exit.");
		this.endOfProgram = true;
		return true;
	}

	/**
	* Main function for plotting. Uses given plotArguments to identify the correct plot asked by program user.
	* @Throws No exceptions.
	* @Return null, if a plot argument is invalid. True otherwise.
	*/
	private Boolean magplot(Matcher plotArguments) {
		//This is the main plot method call. 
		try {
			return true;
		} catch (NullPointerException npe) {
			System.out.println("E: invalid parameters for plotting.");
		}
		return null;
	}

	//---------------------------------------------
	//PRIMARY METHOD SECTION

	/**
	* Get a user input from STDIN, interpret it, and make the correct action.
	* @Throws No exceptions.
	* @Return True, if command was correctly recognized. False otherwise.
	*/
	public boolean getInput() {
		try {
			//Get user next input from STDIN.
			String userInput = inputScanner.nextLine();

			//First, verify if user input is not a arithmetic expression.
			//If this regex has at least a match, then the user input
			//can not be a valid arithmetic expression and, then, check if
			//it is a valid method name.
			Matcher regexArithmeticNotMatched = this.negatedArithmeticRegexPattern.matcher(userInput);

			if (regexArithmeticNotMatched.find()) {
				//It is not a arithmetic expression.

				// Now, check user's next input line and
				// get the result of the default regex match, if any.
				Matcher regexTextMatched = this.mainRegexPattern.matcher(userInput);

				// Verify if user input made sense, according to the default regex pattern
				if (regexTextMatched.find()) {
					//Identify the correct regex command to be called
					Method methodToBeCalled = null;
					for (Method methodAux : this.interpreterMethods){
						if (methodAux.getName().equals(regexTextMatched.group(1).toLowerCase())) {
							methodToBeCalled = methodAux;
							break;
						}
					}

					//Last but not least, check for forbidden input parameter redefinition
					Matcher redefinitionCheck = checkInputRedundancyPattern.matcher(userInput);
					if (redefinitionCheck.find()) {
						System.out.println("E: found redefinition of input parameter \"" + 
							redefinitionCheck.group(1) + "\"! abort.");
						return false;
					}

					//Try to call the identified method, if any, and return true if success.
					return ((regexTextMatched.group(2) != null ? 
						methodToBeCalled.invoke(this, regexTextMatched) : 
						methodToBeCalled.invoke(this)) != null);
				}
			} else {
				//It is a arithmetic expression, call a method to solve it and then display the result.
				System.out.println(this.arithmeticSolver.solve(userInput));
			}
		} catch (IllegalStateException ise) {
			System.out.println("E: main input Scanner was closed.");
		} catch (IllegalAccessException iae) {
			System.out.println("E: invalid access to method.");
		} catch (InvocationTargetException ite) {
			System.out.println("E: can't invoke selected method.");
		} catch (IllegalArgumentException iae) {
			System.out.println("E: missing or excessive parameters for this function!");
		} catch (NullPointerException npe) {
			this.callInvalidMethod();
		} catch (IllegalArithmeticExpression iae) {
			System.out.println(iae.getMessage());
		}

		//Return false, by default, if no regex match happens.
		return false;
	} 

	/**
	* For test purpose. Should not exists on final version.
	*/
	public static void main(String[] args) {
		//for (int i = 0; i < Interpreter.interpreterMethods.length; i++)
		//	System.out.println(Interpreter.interpreterMethods[i].getName().toString());

		Interpreter myInt = new Interpreter ();
		while(!myInt.programEnds()) 
			myInt.getInput();
	}
}