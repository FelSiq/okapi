package plotsOkapiPack;

/**
* Plot style Line.
*/
public class PlotLine extends GeneralPlot {
	// -------------------------------------------------
	// INNER CLASS SECTION

	// -------------------------------------------------
	// CLASS CONSTRUCTOR
	public PlotLine() {
		super();
	}
	// -------------------------------------------------
	// METHOD SECTION

	/**
	* Setup up GeneralPlot parameters do this plot style.
	*/
	public static void setupPlotLine() {
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