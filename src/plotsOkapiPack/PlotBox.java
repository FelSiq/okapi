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
		Double aux = ((double) quartile * ((double) values.size() + 1.0))/4.0;
		Integer k = aux.intValue();
		return values.get(k) + (aux - k)*(values.get(k + 1));
	}

	/**
	* Print stuff specific to this plot style.
	*/
	private static void plot(List<List<Double>> dataTable) {
		// Do individual plot stuff
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

		// 
		Double minOutlier = 0.0; 
		Double minValue = Collections.min(dataTableClone);
		Double fstQuartile = getQuartile(1, dataTableClone);
		Double sndQuartile = getQuartile(2, dataTableClone);
		Double trdQuartile = getQuartile(3, dataTableClone);
		Double maxValue = Collections.max(dataTableClone);
		Double maxOutlier = 0.0; 

		System.out.println(fstQuartile + "/"+sndQuartile + "/"+trdQuartile);
		// 
		PlotBox.plotImage = new BufferedImage(
			bgDim, bgDim, BufferedImage.TYPE_4BYTE_ABGR);

		// 
		Graphics g = plotImage.getGraphics();
		g.setColor(Color.BLACK);
		System.out.println(((Double) ((bgDim) * (fstQuartile/maxValue))).intValue());
		System.out.println(((Double) ((bgDim) * (trdQuartile/maxValue))).intValue());
		g.drawRect(
			bgDim/2 - ((Double) ((bgDim) * ((fstQuartile  - minValue)/(maxValue - minValue)))).intValue(), 
			bgDim/2, 
			((Double) ((bgDim) * ((trdQuartile - minValue)/(maxValue - minValue)))).intValue(), 
			60);
	}
}