package plotsOkapiPack;
// -------------------------------------------------
import generalOkapiPack.OkapiTable;
// IMPORT SECTION
import java.awt.image.BufferedImage;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.List;
// -------------------------------------------------

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

		// Call plot method and prepare the graphic image
		PlotLine.plot(dataTable, userColor);
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

		// INSTRUCTIONS: 
		// 1. Just draw on g (g.drawLine, g.drawOval, g.drawRect...)
		// And the magic will just happens. Trust me.
		// 2. If you want to plot a (x, y) dot, then always draw
		// on the (GeneralPlot.getXPosition(x), GeneralPlot.getYPosition(y)) 
		// This will convert the (x, y) value to the correct (x', y') screen position.

	}
}