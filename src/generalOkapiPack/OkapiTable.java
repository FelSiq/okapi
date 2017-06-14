package generalOkapiPack;
// ------------------------------------------
// IMPORT SECTION
import java.util.List;
// ------------------------------------------

/**
* 
*/
public class OkapiTable<T> {
	// ------------------------------------------
	// VARIABLE SECTION
	private List<List<T>> userTable;
	private List<String> rowName, colName;
	// ------------------------------------------
	// METHOD SECTION 

	/**
	*
	*/
	public List<List<T>> getUserTable() {
		return this.userTable;
	}

	/**
	* 
	*/
	public T getElement(int x, int y) {
		try {
			return this.userTable.get(y).get(x);
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	/**
	* 
	*/
	public OkapiTable(List<List<T>> newUserTable, List<String> newRowNames, List<String> newColNames) {
		this.userTable = newUserTable;
		this.rowName = newRowNames;
		this.colName = newColNames;
	}

	/**
	* 
	*/ 
	public String getColName(int index) {
		try {
			return this.colName.get(index);
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	/**
	* 
	*/ 
	public String getRowName(int index) {
		try {
			return this.rowName.get(index);
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	/**
	* 
	*/
	public int getColNum() {
		try {
			return this.userTable.get(0).size();
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			System.out.println(e.getMessage());
		}
		return 0;
	}

	/**
	* 
	*/
	public int getRowNum() {
		try {
			return this.userTable.size();
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		}
		return 0;
	}

	/**
	* 
	*/ 
	public void setColName(int index, String newColName) {
		try {
			this.colName.set(index, newColName);
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	* 
	*/ 
	public void setRowName(int index, String newRowName) {
		try {
			this.rowName.set(index, newRowName);
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			System.out.println(e.getMessage());
		}
	}
}