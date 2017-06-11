package plotsOkapiPack;

/**
* Plot style BoxPlot.
*/
public class PlotBox extends GeneralPlot {
	// -------------------------------------------------
	// INNER CLASS SECTION

	// -------------------------------------------------
	// CLASS CONSTRUCTOR
	public PlotBox() {
		super();
	}
	// -------------------------------------------------
	// METHOD SECTION

	/**
	* Setup up GeneralPlot parameters do this plot style.
	*/
	public static void setupPlotBox() {
		// Set up stuff related to PlotBox, if needed.
		GeneralPlot.setAxis(true);
	}

	/**
	* Print stuff specific to this plot style.
	*/
	public void plot() {
		// Do individual plot stuff
	}
}