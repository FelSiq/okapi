package plotsOkapiPack;
// -------------------------------------------------
import generalOkapiPack.OkapiTable;
// IMPORT SECTION
import java.awt.image.BufferedImage;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
// -------------------------------------------------

/**
* Plot style Pie.
*/
public class PlotPie extends GeneralPlot {
	// -------------------------------------------------
	// VARIABLES SECTION
	private static BufferedImage plotImage;

	private static int X_AXIS_OFFSET = GeneralPlot.getBackgroundDim()/2;	

	private static int Y_AXIS_OFFSET = GeneralPlot.getBackgroundDim()/2;

	private static int starter;
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
	public static void setupPlotPie(OkapiTable<Double> dataTable, Color userColor) {
		// Set up stuff related to PlotPie, if needed.
		GeneralPlot.setAxis(true);

		// Set a good visual x-axis offset 
		GeneralPlot.setXAxisOffset(PlotPie.X_AXIS_OFFSET);

		// Set a good visual y-axis offset 
		GeneralPlot.setYAxisOffset(PlotPie.Y_AXIS_OFFSET);

		// Call plot method and prepare the graphic image
		PlotPie.plot(dataTable, userColor);
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

		return freqVector;
	}

	/**
	* Draw bars section of the barplot.
	*/
	private static void drawPie(Graphics2D g, int[] freqVector, Double xMinValue, Double xMaxValue, Color userColor) {
		final Integer bgDim = GeneralPlot.getBackgroundDim();
		final Integer subInterval = ((Double) ((xMaxValue - xMinValue)/GeneralPlot.getXInterval())).intValue();

		final int halfRadius = bgDim/2;

		g.setColor(Color.BLACK);
		g.drawOval(0, 0, bgDim, bgDim);

		int freqSum = 0;
		for (int i = 0; i < freqVector.length; i++) {
			freqSum += freqVector[i];
		}

		int starter = 0;
		Integer currentInterval = 0;
		for(int i = 0; i < freqVector.length; i++) {
			g.fillRect(5, 5 + i*26, 20, 20);
			g.drawString( "[" + currentInterval + ", " + (currentInterval + subInterval) + ")", 30, 5 + 13 + i*26);
			currentInterval += subInterval;

			Random rand = new Random();
			Color c = new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255));
			g.setColor(c);

			int thetaval = ((360 * freqVector[i])/freqSum);
			g.fillArc(0, 0, bgDim, bgDim, starter, thetaval);

			starter += thetaval;
		}
	}

	/**
	* Set up custom maximum y-axis limits, based on the built frequency vector.
	*/
	private static void setCorrectYLimMax() {
		GeneralPlot.setYLimits(GeneralPlot.getXLimMin(), GeneralPlot.getXLimMax());
	}

	/**
	* Print stuff specific to this plot style.
	*/
	private static void plot(OkapiTable<Double> dataTable, Color userColor) {
		// Get the plotting space dimensions
		Integer bgDim = GeneralPlot.getBackgroundDim();

		// Create the basis image for this plot
		PlotPie.plotImage = new BufferedImage(
			bgDim, bgDim, BufferedImage.TYPE_4BYTE_ABGR);

		// Creates the basis drawer of this plot
		Graphics2D g = (Graphics2D) PlotPie.plotImage.getGraphics();

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
		
		// Set up a good stroke (just for aesthetics)
		BasicStroke strokeStyle = new BasicStroke(2);
		g.setStroke(strokeStyle);

		// 
		PlotPie.setCorrectYLimMax();

		// Draw bars
		PlotPie.drawPie(g, freqVector, xMinValue, xMaxValue, userColor);
	}
}