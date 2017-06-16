package generalOkapiPack;
//------------------------------------
// IMPORT SECTION
//1. COLLECTIONS
import java.util.List;
import java.util.ArrayList;

//2. File
import java.io.IOException;
import java.io.FileReader;
import java.io.File;

//3. General
import java.util.Scanner;
//------------------------------------

/**
* This class should handle Data Table operations, used as source to plots.
*/
public abstract class TableManager extends GeneralOkapi {
	/**
	* Init a empty ArrayList of ArrayLists of Double values.
	* @Return A ArrayList of ArrayLists of Double values
	* @Throws No exception.
	*/
	public static OkapiTable<Double> create(int rowNum, int colNum) {
		try {
			// Init given Table
			List<List<Double>> table = new ArrayList<List<Double>>(rowNum);
			OkapiTable<Double> newTable = new OkapiTable<Double>(table);
			
			// Fill up brand-new table 
			while (table.size() < rowNum) {
				newTable.addRowName("Row" + newTable.getRowNum());
				table.add(new ArrayList<Double>(colNum));
			}

			// Return OkapiTable
			return newTable;
		} catch (IllegalArgumentException iae) {
			System.out.println(iae.getMessage());
		}
		return null;
	} 

	/**
	* Init a table with specified rows and columns quantity, and fill up with values of given InputFile.
	* If input files does not have sufficient values, this method will fill up the remaining positions
	* with zeros.
	* @Return A ArrayList of Arraylists of Double values.
	* @Throws No exception.
	*/
	public static OkapiTable<Double> create(int rowNum, int colNum, File inputFile) {
		try {
			OkapiTable<Double> newTable = TableManager.create(rowNum, colNum);
			List<List<Double>> table = newTable.getUserTable();
			// Auxiliary instances
			FileReader fileReader = new FileReader(inputFile);
			Scanner myInput = new Scanner(inputFile);
			
			// Fill up created table from given file
			for (int i = 0; i < rowNum; i++) {
				for (int j = 0; j < colNum; j++) {
					newTable.addColName("Col" + newTable.getColNum());
					table.get(i).add((myInput.hasNextDouble() || inputFile.equals(System.in)) ? myInput.nextDouble() : 0.0);
				}
			}

			// Close openened file and scanner
			fileReader.close();
			myInput.close();

			// Return created table
			return newTable;
		} catch (NullPointerException npe) {
			System.out.println(npe.getMessage());
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
		return null;
	} 

	/**
	* Add a new row on the table and fill up with zeros.
	* @Throws No exception.
	*/
	public static void addemptyrow(OkapiTable<Double> userTable) {
		try {
			//
			List<List<Double>> table = userTable.getUserTable();

			// Check the column size
			int columnSize = table.size() > 0 ? table.get(0).size() : 1;

			// Create a new row on the table, with the same number of columns as
			// every other row, if any. If there's no rows on the table at all, 
			// the new row will have only a single column.
			table.add(new ArrayList<Double>());

			// Fill up all with zeros (default value)
			while (table.get(table.size() - 1).size() < columnSize) {
				table.get(table.size() - 1).add(0.0);
			}
			// Set up row name
			userTable.addRowName("Row" + userTable.getRowNum());

		} catch (NullPointerException npe) {
			System.out.println(npe.getMessage());
		} catch (IndexOutOfBoundsException ioobe) {
			System.out.println(ioobe.getMessage());
		}
	}

	/**
	* Add a new row on the table and fill up with the values given by inputFile. If inputFile does
	* not have sufficient values on it, this method will fill up the remaining positions of the
	* table with zeros.
	* @Throws No exception.
	*/
	public static void addrow(OkapiTable<Double> userTable, File inputFile) {
		try {
			//
			List<List<Double>> table = userTable.getUserTable();

			// Check the column size
			int columnSize = table.size() > 0 ? table.get(0).size() : 1;

			// Create a new row on the table, with the same number of columns as
			// every other row, if any. If there's no rows on the table at all, 
			// the new row will have only a single column.
			table.add(new ArrayList<Double>());

			// Auxiliary instances
			FileReader fileReader = new FileReader(inputFile);
			Scanner myInput = new Scanner(inputFile);

			// Fill up all with values of the inputFile or zeros (default value)
			while (table.get(table.size() - 1).size() < columnSize) {
				table.get(table.size() - 1).add((myInput.hasNextDouble() || inputFile.equals(System.in)) ? myInput.nextDouble() : 0.0);
			}
			// Set up row name
			userTable.addRowName("Row" + userTable.getRowNum());

			// Close openened file and scanner
			fileReader.close();
			myInput.close();

		} catch (NullPointerException npe) {
			System.out.println(npe.getMessage());
		} catch (IndexOutOfBoundsException ioobe) {
			System.out.println(ioobe.getMessage());
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}

	/**
	* Add a new column on the table and fill up with zeros.
	* @Throws No exception.
	*/
	public static void addemptycol(OkapiTable<Double> userTable) {
		try {
			//
			List<List<Double>> table = userTable.getUserTable();

			// If there's no rows at all, creates the first row
			if (table.isEmpty()) {
				table.add(new ArrayList<Double>());
			}

			// Fill up all new column with zeros (default value)
			for (int i = 0; i < table.size(); i++) {
				table.get(i).add(0.0);
			}
			// Set up column name
			userTable.addColName("Col" + userTable.getColNum());

		} catch (NullPointerException npe) {
			System.out.println(npe.getMessage());
		} catch (IndexOutOfBoundsException ioobe) {
			System.out.println(ioobe.getMessage());
		}
	}

	/**
	* Add a new column on the table and fill up with values from given InputFile. If inputFile
	* does not have sufficient values on it, the this method will fill all remaining table
	* positions with zeros.
	* @Throws No exception.
	*/
	public static void addcol(OkapiTable<Double> userTable, File inputFile) {
		try {
			//
			List<List<Double>> table = userTable.getUserTable();

			// If there's no rows at all, creates the first row
			if (table.isEmpty()) {
				table.add(new ArrayList<Double>());
			}

			// Auxiliary instances
			FileReader fileReader = new FileReader(inputFile);
			Scanner myInput = new Scanner(inputFile);

			// Fill up all new column with zeros (default value)
			for (int i = 0; i < table.size(); i++) {
				table.get(i).add((myInput.hasNextDouble() || inputFile.equals(System.in)) ? myInput.nextDouble() : 0.0);
			}

			// Set up Column name
			userTable.addColName("Col" + userTable.getColNum());

			// Close openened file and scanner
			fileReader.close();
			myInput.close();

		} catch (NullPointerException npe) {
			System.out.println(npe.getMessage());
		} catch (IndexOutOfBoundsException ioobe) {
			System.out.println(ioobe.getMessage());
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}

	/**
	* Remove the last row on the given table
	* @Throws No exceptions.
	*/
	public static void remlastrow(OkapiTable<Double> userTable) {
		try {
			//
			List<List<Double>> table = userTable.getUserTable();

			// Remove the row name of this row
			if (userTable.getRowNum() > 1){
				userTable.remRowName(userTable.getRowNum() - 1);
			} else System.out.println("E: this table is empty!");

			if (!table.isEmpty()) {
				userTable.remColName(userTable.getColNum() - 1);
				table.remove(table.size() - 1);
			}
		} catch (NullPointerException npe) {
			System.out.println(npe.getMessage());
		} catch (IndexOutOfBoundsException ioobe) {
			System.out.println(ioobe.getMessage());
		}
	}

	/**
	* Remove a entire row with given index on the given table
	* @Throws No exceptions.
	*/
	public static void remrow(OkapiTable<Double> userTable, int index) {
		try {
			//
			List<List<Double>> table = userTable.getUserTable();

			if (userTable.getRowNum() > index && index >= 0) {
				userTable.remRowName(index);
				table.remove(index);
			} else System.out.println("E: invalid row index!");
		} catch (NullPointerException npe) {
			System.out.println(npe.getMessage());
		} catch (IndexOutOfBoundsException ioobe) {
			System.out.println(ioobe.getMessage());
		}
	}

	/**
	* Remove the last column on the given table
	* @Throws No exceptions.
	*/
	public static void remlastcol(OkapiTable<Double> userTable) {
		try {
			//
			List<List<Double>> table = userTable.getUserTable();

			// Remove the name of its column
			if (userTable.getColNum() > 1) {
				userTable.remColName(userTable.getColNum() - 1);
			} else System.out.println("E: this table is empty!");

			for (int i = 0; i < table.size(); i++) {
				table.get(i).remove(table.get(i).size() - 1);

				// If this row is empty, remove it and it's name
				if (table.get(i).isEmpty()) {
					userTable.remRowName(i);
					table.remove(i);
				}
			}
		} catch (NullPointerException npe) {
			System.out.println(npe.getMessage());
		} catch (IndexOutOfBoundsException ioobe) {
			System.out.println(ioobe.getMessage());
		}
	}

	/**
	* Remove the column with given index on the given table
	* @Throws No exceptions.
	*/
	public static void remcol(OkapiTable<Double> userTable, int index) {
		try {
			//
			List<List<Double>> table = userTable.getUserTable();

			// Remove the name of that column
			if (userTable.getColNum() > index && index >= 0) {
				userTable.remColName(index);

				for (int i = 0; i < table.size(); i++) {
					table.get(i).remove(index);

					// If this row is empty, remove it and it's name
					if (table.get(i).isEmpty()) {
						userTable.remRowName(i);
						table.remove(i);
					}
				}
			} else System.out.println("E: invalid column index!");

		} catch (NullPointerException npe) {
			System.out.println(npe.getMessage());
		} catch (IndexOutOfBoundsException ioobe) {
			System.out.println(ioobe.getMessage());
		}
	}

	/**
	* Calls toString() method to every List of the given List of Lists.
	* @Throws No exception.
	*/
	public static void print(OkapiTable<Double> userTable) {
		try {
			//
			List<List<Double>> table = userTable.getUserTable();

			if (userTable.getColNum() > 0)
				System.out.print("[    ]");

			for (Integer i = 0; i < userTable.getColNum(); i++) {
				String colName = userTable.getColName(i);
				System.out.print("[" + (colName != null ? colName : i.toString()) + "]");
			}

			System.out.println();

			for (Integer i = 0; i < table.size(); i++) {
				String rowName = userTable.getRowName(i);
				System.out.println("[" + (rowName != null ? rowName : i.toString()) + "]" + table.get(i).toString());
			}
		} catch (NullPointerException npe) {
			System.out.println(npe.getMessage());
		}
	}

	/**
	* Set name of the columns on the table.
	* @Throws No exception.
	*/
	public static void colname(OkapiTable<Double> userTable, File sourceFile) {
		try {
			FileReader fileInput = new FileReader(sourceFile);
			Scanner scannerInput = new Scanner(fileInput);

			for (int i = 0; i < userTable.getColNum() && scannerInput.hasNext(); i++) {
				userTable.setColName(i, scannerInput.next());
			}

			scannerInput.close();
		} catch (IOException | NullPointerException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	* Set name of the rows on the table.
	* @Throws No exception.
	*/
	public static void rowname(OkapiTable<Double> userTable, File sourceFile) {
		try {
			FileReader fileInput = new FileReader(sourceFile);
			Scanner scannerInput = new Scanner(fileInput);

			for (int i = 0; i < userTable.getRowNum() && scannerInput.hasNext(); i++) {
				userTable.setRowName(i, scannerInput.next());
			}

			scannerInput.close();
		} catch (IOException | NullPointerException e) {
			System.out.println(e.getMessage());
		}
	}
}