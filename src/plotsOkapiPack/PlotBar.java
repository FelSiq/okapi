package plotsOkapiPack;
// -------------------------------------------------
import generalOkapiPack.OkapiTable;
// IMPORT SECTION
import java.util.Collections;
import java.util.Arrays;
import java.awt.image.BufferedImage;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.List;
// -------------------------------------------------

/**
* Plot style Bar.
*/
public class PlotBar extends GeneralPlot {
	// -------------------------------------------------
	// VARIABLES SECTION
	private static BufferedImage plotImage;

	// -------------------------------------------------
	// CONSTANT SECTION
	private static final int X_AXIS_OFFSET = 150;

	private static final int Y_AXIS_OFFSET = 20;

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
		// Set up stuff related to BarPlot, if needed.
		GeneralPlot.setAxis(true);

		// Set a good visual x-axis offset 
		GeneralPlot.setXAxisOffset(PlotBar.X_AXIS_OFFSET);

		// Set a good visual y-axis offset 
		GeneralPlot.setYAxisOffset(PlotBar.Y_AXIS_OFFSET);

		// Call plot method and prepare the graphic image
		if (dataTable != null) {
			PlotBar.plot(dataTable, userColor);
		} else {
			System.out.println("E: invalid table to BarPlot.");
		}
	}

	/**
	* Create a nested (with intervals, for real numbers) frenquency vector with the user table.
	*/
	private static int[] createFreqVector(OkapiTable<Double> dataTable, Double xMaxValue, Double xMinValue) {
		int[] freqVector = new int[GeneralPlot.getXInterval()];

		final Double constantCoef = ((double) (GeneralPlot.getXInterval()))/(GeneralPlot.FLOAT_EQUIVALENCE + xMaxValue - xMinValue);
		try {
			for (int i = 0; i < dataTable.getRowNum(); i++) {
				for (int j = 0; j < dataTable.getColNum(); j++) {
					freqVector[((Double) ((dataTable.getElement(i, j) - xMinValue) * constantCoef)).intValue()]++; 
				}
			}
		} catch (NullPointerException | IndexOutOfBoundsException | ArithmeticException e) {
			System.out.println(e.getMessage());
		}

		//for (int i = 0; i < freqVector.length; i++)
		//	System.out.print(freqVector[i] + " ");
		//System.out.println();

		return freqVector;
	}

	/**
	* Draw bars section of the barplot.
	*/
	private static void drawBars(Graphics2D g, int[] freqVector, Double xMinValue, Double xMaxValue, Color userColor) {
		final Double intervalRange = ((xMaxValue - xMinValue)/(double) GeneralPlot.getXInterval());
		final int rectHeightMin = GeneralPlot.getYPosition(0.0).intValue();

		int x1, x2, y1;
		for (int i = 0; i < GeneralPlot.getXInterval(); i++) {
			x1 = GeneralPlot.getXPosition(i * intervalRange + xMinValue).intValue();
			x2 = GeneralPlot.getXPosition((1 + i) * intervalRange + xMinValue).intValue();
			y1 = GeneralPlot.getYPosition(freqVector[i]).intValue();

			g.setColor(userColor);
			g.fillRect(x1, y1, x2 - x1, rectHeightMin - y1);
			g.setColor(Color.BLACK);
			g.drawRect(x1, y1, x2 - x1, rectHeightMin - y1);
		}
	}

	/**
	* Set up custom maximum y-axis limits, based on the built frequency vector.
	*/
	private static void setCorrectYLimMax(int []freqVector) {
		Double yMaxLim = Double.MIN_VALUE;
		for (int i = 0; i < freqVector.length; i++) {
			yMaxLim = (Double) (yMaxLim < freqVector[i] ? freqVector[i] : yMaxLim);
		}
		yMaxLim += yMaxLim * 1.0/10.0;
		GeneralPlot.setYLimits(-(yMaxLim * 1.0/10.0), yMaxLim);
	}

	/**
	* Print stuff specific to this plot style.
	*/
	private static void plot(OkapiTable<Double> dataTable, Color userColor) {
		// Get the plotting space dimensions
		Integer bgDim = GeneralPlot.getBackgroundDim();

		// Create the basis image for this plot
		PlotBar.plotImage = new BufferedImage(
			bgDim, bgDim, BufferedImage.TYPE_4BYTE_ABGR);

		// Creates the basis drawer of this plot
		Graphics2D g = (Graphics2D) plotImage.getGraphics();

		// Auxiliary variables, to set x-axis and y-axis limits adequately.
		Double xMaxValue = Double.MIN_VALUE, xMinValue = Double.MAX_VALUE;

		// Get the min and max of the table.
		try {
			Double dummy = 0.0;
			for (int i = 0; i < dataTable.getRowNum(); i++) {
				for (int j = 0; j < dataTable.getColNum(); j++) {
					dummy = dataTable.getElement(i, j);
					xMaxValue = (xMaxValue < dummy ? dummy : xMaxValue);
					xMinValue = (xMinValue > dummy ? dummy : xMinValue);
				}
			}
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		}

		// Create a frequency vector, for each nest
		int[] freqVector = createFreqVector(dataTable, xMaxValue, xMinValue);
		
		// Set up barplot y limits
		PlotBar.setCorrectYLimMax(freqVector);

		// Set up a good stroke (just for aesthetics)
		BasicStroke strokeStyle = new BasicStroke(2);
		g.setStroke(strokeStyle);

		// Draw bars
		PlotBar.drawBars(g, freqVector, xMinValue, xMaxValue, userColor);
	}
}