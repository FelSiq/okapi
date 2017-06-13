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
	public static OkapiTable<Double> create(int rowNum, int colNum, List<String> rowNames, List<String> colNames) {
		try {
			// Init given Table
			List<List<Double>> table = new ArrayList<List<Double>>(rowNum);
			
			// Fill up brand-new table 
			while (table.size() < rowNum) {
				table.add(new ArrayList<Double>(colNum));
			}

			// 
			OkapiTable<Double> newTable = new OkapiTable<Double>(table, rowNames, colNames);

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
	public static OkapiTable<Double> create(int rowNum, int colNum, File inputFile, List<String> rowNames, List<String> colNames) {
		try {
			OkapiTable<Double> userTable = TableManager.create(rowNum, colNum, rowNames, colNames);
			List<List<Double>> table = userTable.getUserTable();
			// Auxiliary instances
			FileReader fileReader = new FileReader(inputFile);
			Scanner myInput = new Scanner(inputFile);
			
			// Fill up created table from given file
			for (int i = 0; i < rowNum; i++) {
				for (int j = 0; j < colNum; j++) {
					table.get(i).add((myInput.hasNextDouble() || inputFile.equals(System.in)) ? myInput.nextDouble() : 0.0);
				}
			}

			// Close openened file and scanner
			fileReader.close();
			myInput.close();

			// Return created table
			return userTable;
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
	public static void addRow(OkapiTable<Double> userTable) {
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
			while (table.get(table.size() - 1).size() < columnSize)
				table.get(table.size() - 1).add(0.0);

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
	public static void addRow(OkapiTable<Double> userTable, File inputFile) {
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
			while (table.get(table.size() - 1).size() < columnSize)
				table.get(table.size() - 1).add((myInput.hasNextDouble() || inputFile.equals(System.in)) ? myInput.nextDouble() : 0.0);

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
	public static void addCol(OkapiTable<Double> userTable) {
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
	public static void addCol(OkapiTable<Double> userTable, File inputFile) {
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
	public static void remRow(OkapiTable<Double> userTable) {
		try {
			//
			List<List<Double>> table = userTable.getUserTable();

			if (!table.isEmpty())
				table.remove(table.size() - 1);
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
	public static void remRow(OkapiTable<Double> userTable, int index) {
		try {
			//
			List<List<Double>> table = userTable.getUserTable();

			table.remove(index);
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
	public static void remCol(OkapiTable<Double> userTable) {
		try {
			//
			List<List<Double>> table = userTable.getUserTable();

			for (int i = 0; i < table.size(); i++) {
				table.get(i).remove(table.get(i).size() - 1);
				if (table.get(i).isEmpty())
					table.remove(i);
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
	public static void remCol(OkapiTable<Double> userTable, int index) {
		try {
			//
			List<List<Double>> table = userTable.getUserTable();

			for (int i = 0; i < table.size(); i++) {
				table.get(i).remove(index);
				if (table.get(i).isEmpty())
					table.remove(i);
			}
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

			for (int i = 0; i < table.size(); i++) {
				System.out.println(table.get(i).toString());
			}
		} catch (NullPointerException npe) {
			System.out.println(npe.getMessage());
		}
	}
}