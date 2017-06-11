package plotsOkapiPack;

/**
* Plot style Dot.
*/
public class PlotDot extends GeneralPlot {
	// -------------------------------------------------
	// INNER CLASS SECTION

	// -------------------------------------------------
	// CLASS CONSTRUCTOR
	public PlotDot() {
		super();
	}
	// -------------------------------------------------
	// METHOD SECTION

	/**
	* Setup up GeneralPlot parameters do this plot style.
	*/
	public static void setupPlotDot() {
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