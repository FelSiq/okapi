/**
* Give user a detailed information about a choosen available command.
*/
public class HelpSystem {
	private boolean matched;


	/**
	* Verify if this help system found the desired functionalitie.
	* @Return 
	*/
	public boolean state() {
		return this.matched;
	}

	public HelpSystem(String function) {
		this.matched = false; 
	}
}