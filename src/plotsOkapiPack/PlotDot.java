package plotsOkapiPack;
// -------------------------------------------------
// IMPORT SECTION
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.util.List;

/**
* Plot style Dot.
*/
public class PlotDot extends GeneralPlot {
	// -------------------------------------------------
	// VARIABLES SECTION
	private static BufferedImage plotImage;

	// -------------------------------------------------
	// CLASS CONSTRUCTOR
	public PlotDot() {
		super(PlotDot.plotImage);
	}
	// -------------------------------------------------
	// METHOD SECTION

	/**
	* Setup up GeneralPlot parameters do this plot style.
	*/
	public static void setupPlotDot(List<List<Double>> dataTable, Color userColor) {
		// Set up stuff related to PlotDot, if needed.
		GeneralPlot.setAxis(true);
	}

	/**
	* Print stuff specific to this plot style.
	*/
	public void plot() {
		// Do individual plot stuff
	}
}