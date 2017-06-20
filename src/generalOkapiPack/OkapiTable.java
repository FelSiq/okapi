package generalOkapiPack;
// ------------------------------------------
// IMPORT SECTION
import java.util.ArrayList;
import java.util.List;
// ------------------------------------------

/**
* Basic data structure, used to keep values for plotting operations.
*/
public class OkapiTable<T> {
	// ------------------------------------------
	// VARIABLE SECTION
	private List<List<T>> userTable;
	private List<String> rowName, colName;
	// ------------------------------------------
	// CONSTRUCTOR SECTION

	public OkapiTable(List<List<T>> newUserTable) {
		this.userTable = newUserTable;
		this.rowName = new ArrayList<String>();
		this.colName = new ArrayList<String>();
	}

	public OkapiTable(List<List<T>> newUserTable, List<String> newRowNames, List<String> newColNames) {
		this.userTable = newUserTable;
		this.rowName = newRowNames;
		this.colName = newColNames;
	}

	// ------------------------------------------
	// METHOD SECTION 

	/**
	* Get the table of data values. 
	*/
	public List<List<T>> getUserTable() {
		return this.userTable;
	}

	/**
	* Get the element on the x row and y column of the content table.
	*/
	public T getElement(int x, int y) {
		try {
			return this.userTable.get(x).get(y);
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			System.out.println("E: can't GET (" + x + ", " + y + ") element of this table!");
		}
		return null;
	}

	/**
	* Get the element on the x row and y column of the content table.
	*/
	public void setElement(int x, int y, T value) {
		try {
			this.userTable.get(x).set(y, value);
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			System.out.println("E: can't SET (" + x + ", " + y + ") element of this table!");
		}
	}

	/**
	* Get the name of column with given index 
	*/ 
	public String getColName(int index) {
		try {
			if (this.colName.size() > index)
				return this.colName.get(index);
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			//Do nothing, because can happens.
		}
		return null;
	}

	/**
	* Get the name of row with given index 
	*/ 
	public String getRowName(int index) {
		try {
			if (this.rowName.size() > index)
				return this.rowName.get(index);
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			//Do nothing, because can happens.
		}
		return null;
	}

	/**
	* Get number of columns of this table.
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
	* Get number of rows of this table.
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
	* Add a new column name to the last column of the table.
	*/ 
	public void addColName(String newColName) {
		try {
			this.colName.add(newColName);
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			System.out.println("E: Can't add column name!");
		}
	}

	/**
	* Add a new row name to the last row of the table.
	*/ 
	public void addRowName(String newRowName) {
		try {
			this.rowName.add(newRowName);
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			System.out.println("E: Can't add row name!");
		}
	}

	/**
	* Remove name of the column with given index. 
	*/ 
	public void remColName(int index) {
		try {
			this.colName.remove(index);
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			System.out.println("E: Can't remove column name!");
		}
	}

	/**
	* Remove name of the row with given index. 
	*/ 
	public void remRowName(int index) {
		try {
			this.rowName.remove(index);
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			System.out.println("E: Can't remove row name!");
		}
	}

	/**
	* Set a new value to the name of the column with given index.
	*/ 
	public void setColName(int index, String newColName) {
		try {
			this.colName.set(index, newColName);
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			System.out.println("E: Can't set column name!");
		}
	}

	/**
	* Set a new value to the name of the row with given index.
	*/ 
	public void setRowName(int index, String newRowName) {
		try {
			this.rowName.set(index, newRowName);
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			System.out.println("E: Can't set row name!");
		}
	}
}