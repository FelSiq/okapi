package plotsOkapiPack;
// -------------------------------------------------
import generalOkapiPack.OkapiTable;
// IMPORT SECTION
import java.lang.reflect.InvocationTargetException;
import java.awt.image.BufferedImage;
import java.lang.reflect.Method;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.List;
// -------------------------------------------------

/**
* Plot style Dot.
*/
public class PlotDot extends GeneralPlot {
	// -------------------------------------------------
	// VARIABLES SECTION
	private static BufferedImage plotImage;

	// Radius of the dots on the plot image
	private static final int dotRadius = 7;

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
	public static void setupPlotDot(OkapiTable<Double> dataTable, Color userColor) {
		// Set up stuff related to PlotDot, if needed.
		GeneralPlot.setAxis(true);

		// Call plot method and prepare the graphic image
		if (dataTable != null) {
			PlotDot.plot(dataTable, userColor);
		} else {
			System.out.println("E: invalid table to Boxplot.");
		}
	}

	/**
	* Print dot using a bidimensional horizontal dataTable.
	*/
	private static void plotDotHorizontalBidimensional(OkapiTable<Double> dataTable, Graphics2D g, int i) {
		int x = GeneralPlot.getXPosition(dataTable.getElement(0, i)).intValue();
		int y = GeneralPlot.getYPosition(dataTable.getElement(1, i)).intValue();
		g.drawOval(x - PlotDot.dotRadius/2, y - PlotDot.dotRadius/2, PlotDot.dotRadius, PlotDot.dotRadius);
	}

	/**
	* Print dot using a unidimensional horizontal dataTable.
	*/
	private static void plotDotHorizontalUnimensional(OkapiTable<Double> dataTable, Graphics2D g, int i) {
		int x = GeneralPlot.getXPosition(dataTable.getElement(0, i)).intValue();
		int y = GeneralPlot.getBackgroundDim()/2;
		g.drawOval(x - PlotDot.dotRadius/2, y, PlotDot.dotRadius, PlotDot.dotRadius);
	}

	/**
	* Print dot using a bidimensional vertical dataTable.
	*/
	private static void plotDotVerticalBidimensional(OkapiTable<Double> dataTable, Graphics2D g, int i) {
		int x = GeneralPlot.getXPosition(dataTable.getElement(i, 0)).intValue();
		int y = GeneralPlot.getYPosition(dataTable.getElement(i, 1)).intValue();
		g.drawOval(x - PlotDot.dotRadius/2, y - PlotDot.dotRadius/2, PlotDot.dotRadius, PlotDot.dotRadius);
	}

	/**
	* Print dot using a unidimensional vertical dataTable.
	*/
	private static void plotDotVerticalUnimensional(OkapiTable<Double> dataTable, Graphics2D g, int i) {
		int x = GeneralPlot.getXPosition(dataTable.getElement(i, 0)).intValue();
		int y = GeneralPlot.getBackgroundDim()/2;
		g.drawOval(x - PlotDot.dotRadius/2, y, PlotDot.dotRadius, PlotDot.dotRadius);
	}

	/**
	* Print stuff specific to this plot style.
	*/
	private static void plot(OkapiTable<Double> dataTable, Color userColor) {
		// Get the plotting space dimensions
		final Integer bgDim = GeneralPlot.getBackgroundDim();

		// Create the basis image for this plot
		PlotDot.plotImage = new BufferedImage(
			bgDim, bgDim, BufferedImage.TYPE_4BYTE_ABGR);

		// Creates the basis drawer of this plot
		final Graphics2D g = (Graphics2D) plotImage.getGraphics();

		// Set User given color
		g.setColor(userColor);

		// Auxiliary variables to identify the correct dot plotting method.
		final Class<?>[] parametersArray = {OkapiTable.class, Graphics2D.class, int.class};
		Method plotDotMethod = null;
		String methodToSearch = null;
		int size = 0;

		// Get the correct method for dot plotting (using reflexion)
		try {
			if (dataTable.getRowNum() <= 2) {
				size = dataTable.getColNum();
				methodToSearch = (dataTable.getRowNum() == 1 ?
					"plotDotHorizontalUnimensional" : 
					"plotDotHorizontalBidimensional");
			} else {
				GeneralPlot.verticalTableWarning();
				size = dataTable.getRowNum();
				methodToSearch = (dataTable.getColNum() == 1 ? 
					"plotDotVerticalUnimensional" : 
					"plotDotVerticalBidimensional");
			}
			plotDotMethod = PlotDot.class.getDeclaredMethod(methodToSearch, parametersArray);
		} catch (NoSuchMethodException e) {
			System.out.println(e.getMessage());
		}

		// Plot dots.
		try {
			for (int i = 0; i < size; i++) {
				plotDotMethod.invoke(null, dataTable, g, i);
			}
		} catch (InvocationTargetException | IllegalAccessException | 
			NullPointerException | IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}

	}
}