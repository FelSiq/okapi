//---------------------------------------------
//IMPORT SECTION 
// 0. Basic stuff (general stuff)
import generalOkapiPack.*;
import plotsOkapiPack.*;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.Collections;
import java.util.Set;
import java.io.File;

// 1. For regex (interpreting the user input)
import java.util.regex.PatternSyntaxException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

// 2. For reflection (calling the right input method)
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.Class;
import java.awt.Color;

// 3. For swing thread
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

//---------------------------------------------	
// To prevent compiler bothering about something the programer knows what is going on
@SuppressWarnings("unchecked")

/**
* Class used for Interpreting the user input, and doing the
* right action after it, whatever if input is or not a valid one.
*/

public abstract class Interpreter {
	//---------------------------------------------
	// CONSTANT SECTION

	// Hold all this class declared method names, to match the user input
	private static final Method[] INTERPRETER_METHODS = Interpreter.class.getDeclaredMethods();
	
	// Tell how many commands are available, on this program version, to recognition
	private static final int COMMAND_NUMBER = (Interpreter.INTERPRETER_METHODS.length - 1);

	// Get field labels of the InterpreterParameters class.
	private static final Field[] PARAM_FIELD_NAMES = InterpreterParameters.class.getDeclaredFields();
	
	// Permited characters inside a non-arithmetic command (both for parameters and commands)
	private static final String PERMITED_CHARACTERS = "-+_\\.,;=\\w\\{\\}\\(\\)\\[\\]";

	// Possible suported ways of parameter atributtion. Get flexible!
	private static final String PARAM_ATTRIBUTION = "(?:=+|is|equals?)";

	// This is the default regex to match user input
	private static final String DEFAULT_REGEX_STRING = "\\b([" + PERMITED_CHARACTERS + "]+)(?:\\s+([" + PERMITED_CHARACTERS + "]+)\\s*" + PARAM_ATTRIBUTION + "\\s*([" + PERMITED_CHARACTERS + "]+))*\\s*\\b";

	// Regex used to parameter parsing
	private static final String PARAM_PARSING_STR = "([" + PERMITED_CHARACTERS + "]+)\\s*" + PARAM_ATTRIBUTION + "\\s*([" + PERMITED_CHARACTERS + "]+)";

	// If this regex matches, then the given command is not a arithmetic expression
	private static final String NEGATED_ARITHMETIC_REGEX_STR = "[^-+*/^()0-9\\s.,]+";

	// Checks if there is at least on redefinition of a string parameter from a user input
	private static final String CHECK_INPUT_REDUNDANCY_STR = "\\b([" + PERMITED_CHARACTERS + "]+)\\s*" + PARAM_ATTRIBUTION + ".+(?:\\1)(?=\\s*" + PARAM_ATTRIBUTION + ")\\b";

	// Get obtain plotting limits 
	private static final String PLOT_REGEX_LIMITS = "[\\(\\[{]\\s*([+-]?\\s*(?:\\d+[,.]?|\\d*[,.]\\d+))\\s*[,;:]\\s*([+-]?\\s*(?:\\d+[,.]?|\\d*[,.]\\d+))\\s*[\\)\\]}]";  

	// Dependencies (requested parameters) of "matrix" command
	private static final String[] MATRIX_OP_DEPENDENCIES = {"operation", "a", "b"};

	// Dependencies (requested parameters) of "table" command
	private static final String[] TABLE_OP_DEPENDENCIES = {"operation", "name"};

	// Subdependencies (obligatory just for "create" operation) parameters of "create" operation, in "table" command 
	private static final String[] TABLE_OP_SUBDEPENDENCIES = {"rownum", "colnum"};

	// Dependencies (requested parameters) of "plot" command
	private static final String[] PLOT_OP_DEPENDENCIES = {"type", "table"};

	//---------------------------------------------
	// CONSTANT GLOBAL ACCESS SECTION
	// Standard user input getter
	private static final Scanner MAIN_INPUT_SCANNER = new Scanner(System.in);

	// Instantiation of arithmetic expression solver
	private static final Arithmetic ARITHMETIC_SOLVER = new Arithmetic();

	// Inner class, which keeps all the function parameters inside it
	private static final Interpreter.InterpreterParameters PARAM_KEEPER = new Interpreter.InterpreterParameters();

	// Colletion of all user created tables
	private static final TreeMap<String, OkapiTable<Double>> CREATED_TABLES = new TreeMap<String, OkapiTable<Double>>();

	// These variables holds the regex compiled pattern, used to process user input
	private static Pattern 
		REGPATTERN_NEGATED_ARITHMETIC,
		REGPATTERN_INPUT_REDUNDANCY,
		REGPATTERN_PARAM_PARSING,
		REGPATTERN_INTERPRET_INPUT,
		REGPATTERN_PLOT_LIMITS;
	//---------------------------------------------
	//VARIABLE SECTION

	// Tell if exit command is or is not been called.
	private static boolean endOfProgram = false;

	//---------------------------------------------
	// IINER CLASSES SECTION
	// Inner class, which hold up all Interpreter parameters
	private static class InterpreterParameters {
		//---------------------------------------------
		// Interpreter parameters set
		// Label legend:
		// 		-> Obligatory: every suboperation from this command need that parameter
		//		-> Subdependencie: If determined condition meet, that parameter turns obligatory
		//		-> unlabeled: always optional
		// Plot operation
		protected String 
			table, // Obligatory
			type, // Obligatory
			color,
			title,
			yint, // Number of intervals on the y-axis
			ylab, // Label of the y-axis
			ylim, // Numeric limit of the y-axis
			xint, // Number of intervals on the x-axis
			xlab, // Label of the x-axis
			xlim, // Numeric limit of the x-axis

		// Matrix operation
			a, // Obligatory - Fst operand of the matrix operation
			b, // Obligatory - Snd operand of the matrix operation
			r, // Name of the table created to hold resultand matrix 

		// Table and/or matrix operations
			operation, // Obligatory for both table and matrix
			name, // Obligatory for table
			file, // Input file for the table or output file for the resultant matrix.
			index, 
			rownum, // Subdependencie: operation=init
			colnum; // Subdependencie: operation=init
		//---------------------------------------------

		/**
		* Clear all fields (set to null) of this class
		*/
		public void clearParameters() {
			try {
				for (Field k : InterpreterParameters.class.getDeclaredFields())
					k.set(this, null);
			} catch (IllegalAccessException | NullPointerException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	/**
	* This class will hold up all Interpreter methods which ones can not be called directly from user command line
	*/
	private static abstract class InterpreterAuxiliaryMethods {
		/**
		* Removes every symbol that is not a blank space, letter, dot (.) or number on the given string, and
		* set all letters to its lower case form.
		* @Return New processed String.
		*/
		private static String toCanonical(String unprocessedUserInput) {
			try {
				return unprocessedUserInput.toLowerCase().replaceAll("[^" + Interpreter.PERMITED_CHARACTERS + "\\s]", "").replaceAll("\\s+", " ");
			} catch (NullPointerException e) {
				System.out.println(e.getMessage());
			}
			return null;
		}

		/**
		* Process all regexes parameters.
		* @Throws No exceptions.
		*/
		private static void processParameters(String userInput) {
			// Set up user parameter matching
			Matcher userCommandArgs = Interpreter.REGPATTERN_PARAM_PARSING.matcher(userInput);

			// Used to give user a warning
			Boolean invalidationFlag;

			// Clear all set on the previous command 
			Interpreter.PARAM_KEEPER.clearParameters();
			
			// Set all given arguments, if possible
			try {
				while (userCommandArgs.find()) {
					// Set validation flag to true
					invalidationFlag = true;

					// Search the correspondent field of the given parameter
					for (Field f : Interpreter.PARAM_FIELD_NAMES) {
						if (f.getName().equals(userCommandArgs.group(1))) {
							// Set correspondent field to the given value
							f.set(Interpreter.PARAM_KEEPER, userCommandArgs.group(2));

							// Set invalidation flag to false 
							invalidationFlag = false;

							// Found. Ends the loop.
							break;
						}
					}

					// If invalidationFlag is true, then give user a warning (invalid argument)
					if (invalidationFlag) {
						System.out.println("Warning: unknown given parameter (" + userCommandArgs.group(1) + ").");
					}
				}
			} catch (Exception e) {
				System.out.println("E: " + e.getMessage());
			}
		}

		/**
		* Check if wanted method of user input command has the necessary parameters, in order to correct funcionality.
		* For example, it is obligatory that "magplot" method has the "type" parameter or, otherwise, it does
		* not work. In this case, this function return false, and an warning was send to the user.
		* @Return Null, if method dependencies of user input command was fully satisfied. Field name, otherwise.
		* @Throws No exception.
		*/
		private static String checkDependencies(String[] fieldNames) {
			String notSatisfiedDependency = null;
			if (fieldNames != null) {
				try {
					// Give user a warning, if given parameter does not match any available 
					// field on the parameter keeper class.
					Boolean excessiveParameterFlag;

					// For each given parameter name
					for (String s : fieldNames) {
						// Set auxiliary flag to true
						excessiveParameterFlag = true;

						// For each field available on parameter keeper class
						for (Field f : Interpreter.PARAM_FIELD_NAMES) {
							// If there's a match, i.e, if we found the wanted paremeter
							if (f.getName().equals(s)) {
								// Turn excessive parameter flag off
								excessiveParameterFlag = false;
								// If if it was not set on the last given user input command.
								// In this case, the dependencies was not fully satisfied, and, 
								//therefore, return field Name.
								if (f.get(Interpreter.PARAM_KEEPER) == null) {
									notSatisfiedDependency = (notSatisfiedDependency != null ? notSatisfiedDependency : "") 
										+ ("\n\t> \"" + s + "\"");
								}

								// Found method. break loop.
								break;
							}
						}

						// Print a warning, if a unknown parameter was given by user
						if (excessiveParameterFlag) {
							System.out.println("Warning: unknown parameter (" + s +").");
						}
					}
				} catch (NullPointerException npe) {
					System.out.println("E: bad parameter checking dependencies.");
				} catch (IllegalAccessException iae) {
					System.out.println("E: bad access while checking dependencies.");
				}
			}

			// Return null by default, i.e, method dependencies was fully satisfies by last
			// user input command.
			return notSatisfiedDependency;
		}

		/**
		* Auxiliary method for plotting (set up user plot parameters).
		* @Throws No Exception.
		*/
		private static void setPlottingParameters() {
			// 1. Axes number of intervals
			try {
				if (Interpreter.PARAM_KEEPER.xint != null)
					GeneralPlot.setXInterval(Integer.parseInt(Interpreter.PARAM_KEEPER.xint)); // x-axis
				if (Interpreter.PARAM_KEEPER.yint != null)
					GeneralPlot.setYInterval(Integer.parseInt(Interpreter.PARAM_KEEPER.yint)); // y-axis

				// 2. Plot main title (if title is not given, use table name instead by default)
				GeneralPlot.setTitle(Interpreter.PARAM_KEEPER.title != null ? 
					Interpreter.PARAM_KEEPER.title : Interpreter.PARAM_KEEPER.table);

				// 3. Axes labels
				GeneralPlot.setXAxisLabel(Interpreter.PARAM_KEEPER.xlab); // x-axis
				GeneralPlot.setYAxisLabel(Interpreter.PARAM_KEEPER.ylab); // y-axis
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}  

		/**
		* Auxiliary method for plotting (set up user plot manual limits).
		* @Throws No Exception.
		*/
		private static void setPlottingManualLimits() {
			// Limits for x-axis
			try {
				if (Interpreter.PARAM_KEEPER.xlim != null) {
					Matcher plotLims = Interpreter.REGPATTERN_PLOT_LIMITS.matcher(Interpreter.PARAM_KEEPER.xlim.replaceAll("\\s+", " "));
					if (plotLims.find()) {
						GeneralPlot.setXLimits(
							Double.parseDouble(plotLims.group(1)), 
							Double.parseDouble(plotLims.group(2)));
					}
				}

				// Limits for y-axis
				if (Interpreter.PARAM_KEEPER.ylim != null) {
					Matcher plotLims = Interpreter.REGPATTERN_PLOT_LIMITS.matcher(Interpreter.PARAM_KEEPER.ylim.replaceAll("\\s+", " "));
					if (plotLims.find()) {
						GeneralPlot.setYLimits(
							Double.parseDouble(plotLims.group(1)), 
							Double.parseDouble(plotLims.group(2)));
					}
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

		/**
		* Display a default error message, if current command is a invalid one.
		* @Throws No exceptions.
		* @Return Always false.
		*/
		private static Boolean callInvalidMethod() {
			System.out.println("E: this command is invalid!");
			return false;
		} 
	}
	//---------------------------------------------
	//CLASS CONSTRUCTOR
	public static void compile() {
		try {
			// Regex used to parse user input parameters
			Interpreter.REGPATTERN_PARAM_PARSING = Pattern.compile(Interpreter.PARAM_PARSING_STR);

			// If this regex have a match, then the input string has at least two equal words  
			Interpreter.REGPATTERN_INPUT_REDUNDANCY = Pattern.compile(Interpreter.CHECK_INPUT_REDUNDANCY_STR);

			// Compile negated arithmetic expression. If this pattern has at least match, then the
			// current user input can not be a arithmetic expression.
			Interpreter.REGPATTERN_NEGATED_ARITHMETIC = Pattern.compile(Interpreter.NEGATED_ARITHMETIC_REGEX_STR);

			// Compile general regex, used to match both main command and its parameters 
			Interpreter.REGPATTERN_INTERPRET_INPUT = Pattern.compile(Interpreter.DEFAULT_REGEX_STRING);

			// Interpret the given plotting axes limits
			Interpreter.REGPATTERN_PLOT_LIMITS = Pattern.compile(Interpreter.PLOT_REGEX_LIMITS);
			
			// Sort the method list of Interpreter Class, by name, in order to use binary searchs
			Arrays.sort(INTERPRETER_METHODS, 
				new Comparator<Method>() {
					public int compare(Method a, Method b) {
						return a.getName().compareTo(b.getName());
					}
				});
		} catch (PatternSyntaxException pse) {
			System.out.println("E: Incorrect regex pattern.");
		}
	}

	//---------------------------------------------
	//SECONDARY METHODS SECTION

	/**
	* List all available tables on current program section.
	* @Throws No exceptions.
	* @Return Always true.
	*/
	private static Boolean list() {
		// Get the name of all created tables on this program section, if any
		Set<String> allTableNames = Interpreter.CREATED_TABLES.keySet();

		// Check if user already create at least one table
		if (!allTableNames.isEmpty()) {
			// Aesthetics
			System.out.println("User created tables:");
			
			// Print the name of all available tables
			for (String tableName : allTableNames) {
				// Recover the current table
				OkapiTable<Double> currentTable = Interpreter.CREATED_TABLES.get(tableName);
				// Print it's name and its dimensions, if not empty
				System.out.println("\t> " + tableName + " [" + currentTable.getRowNum() + ", " + currentTable.getColNum() + "]");
			}
		} else {
			// No data table found.
			System.out.println("No data table found. Use \"table operation = create name = mytable\".");
		}

		return true;
	}

	/**
	* Set flag "endOfProgram" to true. Further interpretations of this flag should be handled by another class.
	* @Throws No exceptions.
	* @Return Always true.
	*/
	private static Boolean exit() {
		System.out.println("System: program will now exit.");
		Interpreter.endOfProgram = true;
		return true;
	}


	/**
	* Main function for plotting.
	* @Throws No exceptions.
	* @Return false, if a plot argument is invalid. True otherwise.
	*/
	private static Boolean magplot() {
		// This is the main plot method call. 
		try {
			// Check if this method paremeter dependencies was satisfied.
			String notSatisfiedDependency = Interpreter.InterpreterAuxiliaryMethods.checkDependencies(Interpreter.PLOT_OP_DEPENDENCIES);
			if (notSatisfiedDependency != null) {
				System.out.println("Warning: function parameters not fully satisfied, missing:" + 
					notSatisfiedDependency);
				return false;
			}

			// Set up user plotting parameters
			Interpreter.InterpreterAuxiliaryMethods.setPlottingParameters();

			// Get table for plotting
			OkapiTable<Double> sourceTable = Interpreter.CREATED_TABLES.get(Interpreter.PARAM_KEEPER.table);

			// Check if given table exists
			if (sourceTable != null) {
				// User gave a valid table, proceed.

				// Adjust plotting limits automatically to the given table.
				GeneralPlot.adjustParametersToTable(sourceTable);

				// User may have a chance to impose it's own plotting limits.
				Interpreter.InterpreterAuxiliaryMethods.setPlottingManualLimits();

				// Obligatory parameters fully satisfied, try to call correct plot
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						// Get user asked table
						OkapiTable<Double> dataTable = Interpreter.CREATED_TABLES.get(Interpreter.PARAM_KEEPER.table);
						// Set plot color scheme default to white
						Color userColor = Color.WHITE;
						try {
							// Try to recover user asked color, if any
							if (Interpreter.PARAM_KEEPER.color != null)
								userColor = (Color) Color.class.getDeclaredField(Interpreter.PARAM_KEEPER.color).get(null);
						} catch (NoSuchFieldException | IllegalAccessException e) {
							// Error message (user typed a invalid color)
							System.out.println("W: invalid given color.");
						} finally {
							// Placeholder solution.
							switch(Interpreter.PARAM_KEEPER.type) {
								case "box": 
									PlotBox.setupPlotBox(dataTable, userColor);
									GeneralPlot myPlotBox = new PlotBox();
									break;
								case "pie": 
									PlotPie.setupPlotPie(dataTable, userColor);
									GeneralPlot myPlotPie = new PlotPie();
									break;
								case "bar": 
									PlotBar.setupPlotBar(dataTable, userColor);
									GeneralPlot myPlotBar = new PlotBar();
									break;
								case "dot": 
									PlotDot.setupPlotDot(dataTable, userColor);
									GeneralPlot myPlotDot = new PlotDot();
									break;
								case "line": 
									PlotLine.setupPlotLine(dataTable, userColor);
									GeneralPlot myPlotLine = new PlotLine();
									break;
								default: 
									System.out.println("E: invalid plotting type."); 
									Thread.currentThread().interrupt();
									break;
							}
						}
					}
				});

				return true;
			} else {
				// User gave a unknown table. Give a error message and abort plotting.
				System.out.println(
					"E: there's no table named \"" + 
					Interpreter.PARAM_KEEPER.table + 
					"\". Please \"create\" it first.");
			}
		} catch (NullPointerException | IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}

		//Return false by default
		return false;
	}

	/**
	* Works with all operations of table command.
	* @Throws No exception.
	* @Return False, if something went wrong within user command interpretation. True, otherwise.
	*/
	private static Boolean table() {
		try {
			// Check if this method paremeter dependencies was satisfied.
			String notSatisfiedDependency = Interpreter.InterpreterAuxiliaryMethods.checkDependencies(Interpreter.TABLE_OP_DEPENDENCIES);
			if (notSatisfiedDependency != null) {
				System.out.println("Warning: function parameters not fully satisfied, missing:" 
					+ notSatisfiedDependency);
				return false;
			}

			// "TABLE" command dependencies was satisfied, at this point, now work with the operation
			// Search the correct method to invoke
			Method correctMethod = null;
			for (Method k : TableManager.class.getDeclaredMethods()) {
				// Search for the correct method to be invoked
				if (Interpreter.InterpreterAuxiliaryMethods.toCanonical(k.getName()).equals(Interpreter.PARAM_KEEPER.operation)) {
					// Found correct method.
					correctMethod = k;
					// Don't need to search anymore, break loop.
					break;
				}
			}
			if (correctMethod == null) {
				System.out.println(
					"E: Invalid table operation \"" + 
					Interpreter.PARAM_KEEPER.operation 
					+ "\".");
				return false;
			}

			// Check if given source file was given
			File sourceFile = null;
			if (Interpreter.PARAM_KEEPER.file != null)
				sourceFile = new File(Interpreter.PARAM_KEEPER.file);

			// If operation is "create", then this is a special case.
			if (Interpreter.InterpreterAuxiliaryMethods.toCanonical(correctMethod.getName()).equals("create")) {
				String notSatisfiedSubdependency = Interpreter.InterpreterAuxiliaryMethods.checkDependencies(Interpreter.TABLE_OP_SUBDEPENDENCIES);
				if (notSatisfiedSubdependency != null) {
					System.out.println("Warning: function parameters not fully satisfied, missing:"	
						+ notSatisfiedSubdependency);
					return false;
				}

				// 2. Creates the new table with specified user parameters
				OkapiTable<Double> newTable = null;

				// Check if a source file was given by user
				if (sourceFile != null) {
					// Source file was given, use it
					newTable = TableManager.create(
						Integer.parseInt(Interpreter.PARAM_KEEPER.rownum), 
						Integer.parseInt(Interpreter.PARAM_KEEPER.colnum),
						sourceFile,
						null, null);
				} else {
					// No source file available
					newTable = TableManager.create(
						Integer.parseInt(Interpreter.PARAM_KEEPER.rownum), 
						Integer.parseInt(Interpreter.PARAM_KEEPER.colnum),
						null, null);
				}

				if (newTable != null) {
					// 3. Need to add the new table to the user table collection, with the specified name.
					Interpreter.CREATED_TABLES.put(Interpreter.PARAM_KEEPER.name, newTable);
				} else {
					// Error message
					System.out.println("E: can't create table.");
				}
			} else {
				// At this point, user command does not call "create" operation.
				// Then it's not a special case to be handled.
				
				// Get the user specified table
				OkapiTable<Double> selectedTable = Interpreter.CREATED_TABLES.get(Interpreter.PARAM_KEEPER.name);
				
				// Checks if specified table, to be worked on, exists				
				if (selectedTable != null) {
					if (sourceFile != null){
						// Source/Input File as extra argument
						correctMethod.invoke(null, selectedTable, sourceFile);
					} else if (Interpreter.PARAM_KEEPER.index != null) {
						// A integer/index as extra argument
						correctMethod.invoke(null, selectedTable, 
							Integer.parseInt(Interpreter.PARAM_KEEPER.index));
					} else {
						// Only table as argument (no extra arguments)
						correctMethod.invoke(null, selectedTable);
					}
				} else {
					// Error message, if user input command specified a inexistent table
					System.out.println("E: can't find any table named \"" + 
						Interpreter.PARAM_KEEPER.name + 
						"\". Please, \"create\" it first.");
				}
			}

			// Return true
			return true;
		} catch (NullPointerException | IllegalAccessException | InvocationTargetException e) {
			System.out.println(e.getMessage());
		}

		//Return false by default
		return false;
	}

	/**
	* This method should handle all matrix operations supported by this program.
	* @Return False, if user command does not fills up all necessary conditions for the operation. True, otherwise.
	* @Throws No exceptions.
	*/
	private static Boolean matrix() {
		try {
			// Check if this method paremeter dependencies was satisfied.
			String notSatisfiedDependency = Interpreter.InterpreterAuxiliaryMethods.checkDependencies(Interpreter.MATRIX_OP_DEPENDENCIES);
			if (notSatisfiedDependency != null) {
				System.out.println("Warning: function parameters not fully satisfied, missing:" + 
					notSatisfiedDependency);
				return false;
			}

			// Conditions meet. 

			// Search the correct method to invoke
			Method correctMethod = null;
			for (Method k : TableManager.class.getDeclaredMethods()) {
				// Search for the correct method to be invoked
				if (Interpreter.InterpreterAuxiliaryMethods.toCanonical(k.getName()).equals(Interpreter.PARAM_KEEPER.operation)) {
					// Found correct method.
					correctMethod = k;
					// Don't need to search anymore, break loop.
					break;
				}
			}
			if (correctMethod == null) {
				System.out.println(
					"E: Invalid matrix operation \"" + 
					Interpreter.PARAM_KEEPER.operation + 
					" \".");
				return false;
			}

			// Get the A operand
			OkapiTable<Double> matrixOperandA = Interpreter.CREATED_TABLES.get(Interpreter.PARAM_KEEPER.a);

			// Get the B operand
			OkapiTable<Double> matrixOperandB = Interpreter.CREATED_TABLES.get(Interpreter.PARAM_KEEPER.b);

			// Check if specified table, to be worked on, exists				
			if (matrixOperandA != null && matrixOperandB != null) {
				// Invoke the correct matrix operation with the given operands
				// Get the result with a dummy Object
				Object safetyDummy = correctMethod.invoke(null, matrixOperandA, matrixOperandB);

				// Check if dummy object is a list type
				OkapiTable<Double> matrixOperandR = (safetyDummy instanceof OkapiTable) ? ((OkapiTable<Double>) safetyDummy) : null;

				// Check if everything is fine with the resultant matrix
				if (matrixOperandR != null){
					// Use "r" parameter and "file" after this point, if possible.
					if (Interpreter.PARAM_KEEPER.file == null && Interpreter.PARAM_KEEPER.r == null) {
						// User does not gave neither output file neither new table name to store resultant matrix,
						// then just print it on the STDOUT.
						TableManager.print(matrixOperandR);
					} else {
						if (Interpreter.PARAM_KEEPER.r != null) {
							// User gives a table name to store result value, store the table
							Interpreter.CREATED_TABLES.put(Interpreter.PARAM_KEEPER.r, matrixOperandR);
						} 

						if (Interpreter.PARAM_KEEPER.file != null) {
							// User gives a output file to store this resultant matrix, create this file
							// and append it.
							// To be worked on.
						}
					}
				} else {
					// Can't create resultant matrix, show error message
					System.out.println("E: can't create resultant matrix.");
				}

			} else {
				// Error message, if user input command specified a inexistent table
				System.out.println("E: can't find any table named \"" + 
					(matrixOperandA == null ? Interpreter.PARAM_KEEPER.a : Interpreter.PARAM_KEEPER.b) + 
					"\". Please, \"create\" it first.");
			}

			// Return true
			return true;
		} catch (NullPointerException | IllegalAccessException | InvocationTargetException e) {
			System.out.println(e.getMessage());
		}

		//Return false by default
		return false;
	}

	//---------------------------------------------
	//PRIMARY METHOD SECTION

	/**
	* Get a user input from STDIN, interpret it, and make the correct action.
	* @Throws No exceptions.
	* @Return True, if command was correctly recognized. False otherwise.
	*/
	public static boolean getInput() {
		try {
			//Get user next input from STDIN.
			String userInput = MAIN_INPUT_SCANNER.nextLine();

			//First, verify if user input is not a arithmetic expression.
			//If this regex has at least a match, then the user input
			//can not be a valid arithmetic expression and, then, check if
			//it is a valid method name.
			Matcher regexArithmeticNotMatched = Interpreter.REGPATTERN_NEGATED_ARITHMETIC.matcher(userInput);

			if (regexArithmeticNotMatched.find()) {
				//It is not a arithmetic expression.

				// Then transform the given command to a canonical form
				userInput = Interpreter.InterpreterAuxiliaryMethods.toCanonical(userInput);

				// Now, check user's next input line and
				// get the result of the default regex match, if any.
				Matcher regexTextMatched = Interpreter.REGPATTERN_INTERPRET_INPUT.matcher(userInput);

				// Verify if user input made sense, according to the default regex pattern
				if (regexTextMatched.find()) {
					// Identify the correct regex command to be called
					Method methodToBeCalled = null;
					for (Method methodAux : Interpreter.INTERPRETER_METHODS){
						if (methodAux.getName().equals(regexTextMatched.group(1).toLowerCase())) {
							methodToBeCalled = methodAux;
							break;
						}
					}

					// Last but not least, check for forbidden input parameter redefinition
					Matcher redefinitionCheck = Interpreter.REGPATTERN_INPUT_REDUNDANCY.matcher(userInput);
					if (redefinitionCheck.find()) {
						System.out.println("E: found redefinition of input parameter \"" + 
							redefinitionCheck.group(1) + "\"! abort.");
						return false;
					}

					// Preprocess user given parameters, if needed
					if (regexTextMatched.group(2) != null)
						Interpreter.InterpreterAuxiliaryMethods.processParameters(userInput);

					// Try to call the identified method, if any, and return true if success.
					return (Boolean) methodToBeCalled.invoke(null);
				}
			} else if (!userInput.replaceAll("\\s+", "").equals("")){
				//It is a arithmetic expression, call a method to solve it and then display the result.
				System.out.println(Interpreter.ARITHMETIC_SOLVER.solve(userInput));
			}
		} catch (IllegalStateException | IllegalAccessException | InvocationTargetException | 
			IllegalArgumentException | IllegalArithmeticExpression e) {
			System.out.println(e.getMessage());
		} catch (NullPointerException npe) {
			Interpreter.InterpreterAuxiliaryMethods.callInvalidMethod();
		}

		//Return false, by default, if no regex match happens.
		return false;
	} 

	/**
	* Check if "exit" command was given by user.
	* @Throws No exceptions.
	* @Return True if "exit" command was given already. False otherwise.
	*/
	public static boolean programEnds() {
		return Interpreter.endOfProgram;
	}

	/**
	* Print off all available user commands.
	* @Throws No exceptions.
	* @Return Always true.
	*/
	public static Boolean commands() {
		System.out.println("Listing all available commands:");
		for (Method m : Interpreter.INTERPRETER_METHODS) {
			try {
				if (m.getReturnType().getName().equals("java.lang.Boolean"))
					System.out.println("\t> " + m.getName());
			} catch (NullPointerException e) {
				System.out.println(e.getMessage());
			}
		}
		return true;
	}

	/**
	* Call a help system.
	* @Throws No exceptions.
	* @Return True, if function was found on the help system. False, otherwise.
	*/
	public static Boolean help() {
		if (Interpreter.PARAM_KEEPER.operation != null) {
			HelpSystem helpInstance = new HelpSystem(Interpreter.PARAM_KEEPER.operation);
			return helpInstance.state();
		} else {
			System.out.println("E: Please specify the \"operation\" to access the help system." +
				" Type \"commands\" to show then all.");
		}
		return false;
	}
}