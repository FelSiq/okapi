package generalOkapiPack;
// ------------------------------------------
// IMPORT SECTION
import java.util.ArrayList;
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
			return this.userTable.get(x).get(y);
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			System.out.println("E: can't get (" + x + ", " + y + ") element of this table!");
		}
		return null;
	}

	/**
	* 
	*/
	public OkapiTable(List<List<T>> newUserTable) {
		this.userTable = newUserTable;
		this.rowName = new ArrayList<String>();
		this.colName = new ArrayList<String>();
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
			if (this.getColNum() > index)
				return this.colName.get(index);
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			//Do nothing, because can happens.
		}
		return null;
	}

	/**
	* 
	*/ 
	public String getRowName(int index) {
		try {
			if (this.getRowNum() > index)
				return this.rowName.get(index);
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			//Do nothing, because can happens.
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
	public void addColName(String newColName) {
		try {
			this.colName.add(newColName);
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			System.out.println("E: Can't add column name!");
		}
	}

	/**
	* 
	*/ 
	public void addRowName(String newRowName) {
		try {
			this.rowName.add(newRowName);
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			System.out.println("E: Can't add row name!");
		}
	}

	/**
	* 
	*/ 
	public void remColName(int index) {
		try {
			this.colName.remove(index);
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			System.out.println("E: Can't remove column name!");
		}
	}

	/**
	* 
	*/ 
	public void remRowName(int index) {
		try {
			this.rowName.remove(index);
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			System.out.println("E: Can't remove row name!");
		}
	}

	/**
	* 
	*/ 
	public void setColName(int index, String newColName) {
		try {
			this.colName.set(index, newColName);
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			System.out.println("E: Can't set column name!");
		}
	}

	/**
	* 
	*/ 
	public void setRowName(int index, String newRowName) {
		try {
			this.rowName.set(index, newRowName);
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			System.out.println("E: Can't set row name!");
		}
	}
}