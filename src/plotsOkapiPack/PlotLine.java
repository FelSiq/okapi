package plotsOkapiPack;
// -------------------------------------------------
import generalOkapiPack.OkapiTable;
// IMPORT SECTION
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.awt.BasicStroke;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.util.List;
import java.awt.Color;
// -------------------------------------------------

/**
* Plot style Line.
*/
public class PlotLine extends GeneralPlot {
	// -------------------------------------------------
	// INNER CLASS SECTION
	/**
	* 
	*/
	private static class Tuple implements Comparable<Tuple> {
		// 
		private Double x, y;

		/**
		* Class constructor.
		*/
		public Tuple(Double newX, Double newY) {
			 this.x = newX;
			 this.y = newY;
		}

		/**
		* Get this tuple x value (getter)
		*/
		public Double getX() {
			return this.x;
		}

		/**
		* Get this tuple y value (getter)
		*/
		public Double getY() {
			return this.y;
		}

		/**
		* Comparable interface abstract method.
		*/
		public int compareTo(Tuple b){
			return x.compareTo(b.x); 
		}
	}
	// -------------------------------------------------
	// VARIABLES SECTION
	private static BufferedImage plotImage;

	// -------------------------------------------------
	// CLASS CONSTRUCTOR
	public PlotLine() {
		super(PlotLine.plotImage);
	}
	// -------------------------------------------------
	// METHOD SECTION

	/**
	* Setup up GeneralPlot parameters do this plot style.
	*/
	public static void setupPlotLine(OkapiTable<Double> dataTable, Color userColor) {
		// Set up stuff related to PlotLine, if needed.
		GeneralPlot.setAxis(true);

		// Call plot method and prepare the graphic image
		// PlotLine requires a 2xN or Nx2 matrix.
		try {
			if (dataTable.getRowNum() == 2 || dataTable.getColNum() == 2) {
				PlotLine.plot(dataTable, userColor);
			} else {
				System.out.println("E: magplot type \"line\" requires a 2xN or a Mx2 table.");
			}
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	* Print stuff specific to this plot style.
	*/
	private static void plot(OkapiTable<Double> dataTable, Color userColor) {
		// Get the plotting space dimensions
		Integer bgDim = GeneralPlot.getBackgroundDim();

		// Create the basis image for this plot
		PlotLine.plotImage = new BufferedImage(
			bgDim, bgDim, BufferedImage.TYPE_4BYTE_ABGR);

		// Creates the basis drawer of this plot
		Graphics2D g = (Graphics2D) plotImage.getGraphics();

		// Set up user interface into a Tuple arraylist and sort it.
		final List<Tuple> dataTableClone = new ArrayList<Tuple>();

		// Verify if this is a horizontal or vertical vector, and clone it.
		if (dataTable.getRowNum() == 2) {
			for (int i = 0; i < dataTable.getColNum(); i++) {
				dataTableClone.add(new PlotLine.Tuple(
					dataTable.getElement(i, 0), 
					dataTable.getElement(i, 1)));
			}
		} else {
			GeneralPlot.verticalTableWarning();
			for (int i = 0; i < dataTable.getRowNum(); i++) {
				dataTableClone.add(new PlotLine.Tuple(
					dataTable.getElement(0, i), 
					dataTable.getElement(1, i)));
			}
		}

		// Now sort the tuple array.
		Collections.sort(dataTableClone);

		// 
		g.setColor(userColor);

		// 
		Tuple currentTuple, nextTuple = dataTableClone.get(0);
		for (int i = 0; i < dataTableClone.size() - 1; i++) {
			currentTuple = nextTuple;
			nextTuple = dataTableClone.get(i + 1);
			g.drawLine(
				GeneralPlot.getXPosition(currentTuple.getX()).intValue(),
				GeneralPlot.getYPosition(currentTuple.getY()).intValue(),
				GeneralPlot.getXPosition(nextTuple.getX()).intValue(),
				GeneralPlot.getYPosition(nextTuple.getY()).intValue());
		}
	}
}