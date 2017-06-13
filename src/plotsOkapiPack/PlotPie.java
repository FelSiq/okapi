package plotsOkapiPack;
// -------------------------------------------------
// IMPORT SECTION
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.util.List;

/**
* Plot style Pie.
*/
public class PlotPie extends GeneralPlot {
	// -------------------------------------------------
	// VARIABLES SECTION
	private static BufferedImage plotImage;

	// -------------------------------------------------
	// CLASS CONSTRUCTOR
	public PlotPie() {
		super(PlotPie.plotImage);
	}
	// -------------------------------------------------
	// METHOD SECTION

	/**
	* Setup up GeneralPlot parameters do this plot style.
	*/
	public static void setupPlotPie(List<List<Double>> dataTable, Color userColor) {
		// Set up stuff related to PlotPie, if needed.
		GeneralPlot.setAxis(false);

	}

	/**
	* Print stuff specific to this plot style.
	*/
	public void plot() {
		// Do individual plot stuff
	}
}