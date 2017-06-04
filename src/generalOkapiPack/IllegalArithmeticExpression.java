package generalOkapiPack;

/**
* Throwable made for a given unsuported arithmetic expression by user input.
*/
public class IllegalArithmeticExpression extends Exception {
	public IllegalArithmeticExpression() {
		super("E: invalid arithmetic expression.");
	}
}