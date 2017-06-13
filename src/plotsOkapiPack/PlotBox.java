package plotsOkapiPack;
// -------------------------------------------------
// IMPORT SECTION
// 1. Drawing
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Color;
// 2. Collections
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
// -------------------------------------------------

/**
* Plot style BoxPlot.
*/
public class PlotBox extends GeneralPlot {
	// -------------------------------------------------
	// CONSTANTS SECTON
	private static final int BOX_HEIGHT = 80;

	private static final int BOX_OFFSET = GeneralPlot.getBackgroundDim()/2 + 20;

	private static final int BOX_TEXTOFFSET = 60;

	// -------------------------------------------------
	// VARIABLES SECTION
	private static BufferedImage plotImage;

	// -------------------------------------------------
	// CLASS CONSTRUCTOR
	public PlotBox() {
		super(PlotBox.plotImage);
	}
	// -------------------------------------------------
	// METHOD SECTION

	/**
	* Setup up GeneralPlot parameters do this plot style.
	*/
	public static void setupPlotBox(List<List<Double>> dataTable) {
		// Set up stuff related to PlotBox, if needed.
		GeneralPlot.setAxis(true);

		// Boxplot is unidimensional by nature
		GeneralPlot.setYLimits(0.0, 0.0);

		// Set a good visual x-axis offset 
		GeneralPlot.setXAxisOffset(175);

		// Prepare Boxplot image
		PlotBox.plot(dataTable);
	}

	/**
	*
	*/
	private static Double getQuartile(int quartile, List<Double> values) {
		Double aux = ((double) values.size()) * ((double) quartile / 4.0);
		Integer k = aux.intValue();
		return (values.get(k) + ((Math.abs(aux - k) < GeneralPlot.FLOAT_EQUIVALENCE) ? values.get(k - 1) : values.get(k))) / 2.0;
	}

	/**
	* Print stuff specific to this plot style.
	*/
	private static void plot(List<List<Double>> dataTable) {
		// Do individual plot stuff
		// ---------------------------------------------------
		// SETUP SECTION
		// 
		Integer bgDim = GeneralPlot.getBackgroundDim();
		
		// 
		List<Double> dataTableClone = new ArrayList<Double>();

		//
		for (Double cloneItem : dataTable.get(0))
			dataTableClone.add(cloneItem);

		//
		dataTableClone.sort(new Comparator<Double>() {
			@Override
			public int compare(Double a, Double b) {
				return a.compareTo(b);
			}
		});

		System.out.println(dataTableClone.toString());

		// ---------------------------------------------------
		// CALCULUS SECTION
		// 
		Double minValue = Collections.min(dataTableClone);
		Double fstQuartile = getQuartile(1, dataTableClone);
		Double sndQuartile = getQuartile(2, dataTableClone);
		Double trdQuartile = getQuartile(3, dataTableClone);
		Double maxValue = Collections.max(dataTableClone);

		// 
		Integer minValueXPosition = getXPosition(minValue).intValue();
		Integer fstQuartileXPosition = getXPosition(fstQuartile).intValue();
		Integer sndQuartileXPosition = getXPosition(sndQuartile).intValue();
		Integer trdQuartileXPosition = getXPosition(trdQuartile).intValue();
		Integer maxValueXPosition = getXPosition(maxValue).intValue();

		// 
		Double interQuartileRange = (trdQuartile - fstQuartile);
		Double minOutlier = fstQuartile - 1.5 * interQuartileRange; 
		Double maxOutlier = trdQuartile + 1.5 * interQuartileRange; 

		// 
		Integer minOutlierXPosition = getXPosition(minOutlier).intValue();
		Integer maxOutlierXPosition = getXPosition(maxOutlier).intValue();


		// 
		PlotBox.plotImage = new BufferedImage(
			bgDim, bgDim, BufferedImage.TYPE_4BYTE_ABGR);

		// 
		Graphics g = plotImage.getGraphics();
		g.setColor(Color.BLACK);

		// 
		g.drawRect(
			fstQuartileXPosition, 
			PlotBox.BOX_OFFSET - PlotBox.BOX_HEIGHT/2, 
			trdQuartileXPosition - fstQuartileXPosition, 
			PlotBox.BOX_HEIGHT);

		// ---------------------------------------------------
		// LINE SECTION
		// 
		g.drawLine(minValueXPosition, 
			PlotBox.BOX_OFFSET - PlotBox.BOX_HEIGHT/2, 
			minValueXPosition, 
			PlotBox.BOX_HEIGHT/2 + PlotBox.BOX_OFFSET);
		// 
		g.drawLine(sndQuartileXPosition, 
			PlotBox.BOX_OFFSET - PlotBox.BOX_HEIGHT/2, 
			sndQuartileXPosition, 
			PlotBox.BOX_HEIGHT/2 + PlotBox.BOX_OFFSET);
		// 
		g.drawLine(maxValueXPosition, 
			PlotBox.BOX_OFFSET - PlotBox.BOX_HEIGHT/2, 
			maxValueXPosition, 
			PlotBox.BOX_HEIGHT/2 + PlotBox.BOX_OFFSET);
		//		
		g.drawLine(minValueXPosition, 
			PlotBox.BOX_OFFSET, 
			fstQuartileXPosition, 
			PlotBox.BOX_OFFSET);
		// 
		g.drawLine(trdQuartileXPosition, 
			PlotBox.BOX_OFFSET, 
			maxValueXPosition, 
			PlotBox.BOX_OFFSET);

		// ---------------------------------------------------
		// TEXT SECTION
		// 
		g.drawString((minValue < minOutlier ? "(outlier)\n" : "") + "(min)\n" + minValue.toString(), minValueXPosition, PlotBox.BOX_OFFSET - PlotBox.BOX_TEXTOFFSET);
		g.drawString((maxValue > maxOutlier ? "(outlier)\n" : "") + "(max)\n" + maxValue.toString(), maxValueXPosition, PlotBox.BOX_OFFSET - PlotBox.BOX_TEXTOFFSET);
		g.drawString("(Q1)\n" + fstQuartile.toString(), fstQuartileXPosition, PlotBox.BOX_OFFSET - PlotBox.BOX_TEXTOFFSET);
		g.drawString("(Q2)\n" + sndQuartile.toString(), sndQuartileXPosition, PlotBox.BOX_OFFSET - PlotBox.BOX_TEXTOFFSET);
		g.drawString("(Q3)\n" + trdQuartile.toString(), trdQuartileXPosition, PlotBox.BOX_OFFSET - PlotBox.BOX_TEXTOFFSET);

		if (minValue < minOutlier)
			g.drawString("(min)\n" + minOutlier.toString(), minOutlierXPosition, PlotBox.BOX_OFFSET - PlotBox.BOX_TEXTOFFSET);
		if (maxValue > maxOutlier)
			g.drawString("(max)\n" + maxOutlier.toString(), maxOutlierXPosition, PlotBox.BOX_OFFSET - PlotBox.BOX_TEXTOFFSET);
	}
}