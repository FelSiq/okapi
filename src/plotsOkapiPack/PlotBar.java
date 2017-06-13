package plotsOkapiPack;
// -------------------------------------------------
import generalOkapiPack.OkapiTable;
// IMPORT SECTION
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.util.List;

/**
* Plot style Bar.
*/
public class PlotBar extends GeneralPlot {
	// -------------------------------------------------
	// VARIABLES SECTION
	private static BufferedImage plotImage;

	// -------------------------------------------------
	// CLASS CONSTRUCTOR
	public PlotBar() {
		super(PlotBar.plotImage);
	}
	// -------------------------------------------------
	// METHOD SECTION

	/**
	* Setup up GeneralPlot parameters do this plot style.
	*/
	public static void setupPlotBar(OkapiTable<Double> dataTable, Color userColor) {
		// Set up stuff related to PlotBar, if needed.
		GeneralPlot.setAxis(true);
	}

	/**
	* Print stuff specific to this plot style.
	*/
	public void plot() {
		// Do individual plot stuff
	}
}