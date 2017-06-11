package plotsOkapiPack;

/**
* Plot style Bar.
*/
public class PlotBar extends GeneralPlot {
	// -------------------------------------------------
	// INNER CLASS SECTION

	// -------------------------------------------------
	// CLASS CONSTRUCTOR
	public PlotBar() {
		super();
	}
	// -------------------------------------------------
	// METHOD SECTION

	/**
	* Setup up GeneralPlot parameters do this plot style.
	*/
	public static void setupPlotBar() {
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