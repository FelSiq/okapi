package plotsOkapiPack;
// -------------------------------------------------
import generalOkapiPack.OkapiTable;
// IMPORT SECTION
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.util.List;

/**
* Plot style Line.
*/
public class PlotLine extends GeneralPlot {
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
	}

	/**
	* Print stuff specific to this plot style.
	*/
	public void plot() {
		// Do individual plot stuff
	}
}