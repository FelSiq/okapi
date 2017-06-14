package plotsOkapiPack;
// -------------------------------------------------
// IMPORT SECTION
import generalOkapiPack.OkapiTable;
// 1. Drawing
import java.awt.image.BufferedImage;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
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
	private static final int AXIS_OFFSET = 150;

	private static final int BOX_HEIGHT = (GeneralPlot.getBackgroundDim())/2 - PlotBox.AXIS_OFFSET + 50;

	private static final int BOX_OFFSET = (GeneralPlot.getBackgroundDim() - PlotBox.AXIS_OFFSET)/2;

	private static final int VALUE_OFFSET = (BOX_OFFSET + (BOX_HEIGHT/2) + 30);

	private static final int TEXT_OFFSET = (BOX_OFFSET - (BOX_HEIGHT/2) - 30);

	private static final int CHARACTER_OFFSET = 7;
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
	public static void setupPlotBox(OkapiTable<Double> dataTable, Color userColor) {
		// Set up stuff related to PlotBox, if needed.
		GeneralPlot.setAxis(true);

		// Boxplot is unidimensional by nature
		GeneralPlot.setYLimits(0.0, 0.0);

		// Set a good visual x-axis offset 
		GeneralPlot.setXAxisOffset(PlotBox.AXIS_OFFSET);

		// Prepare Boxplot image
		if (dataTable != null) {
			PlotBox.plot(dataTable, userColor);
		} else {
			System.out.println("E: invalid table to Boxplot.");
		}
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
	*
	*/
	private static void drawValue(Graphics2D g, Double value, Integer position, Integer yAdjust) {
		String dummy = value.toString();
		g.drawString(dummy, position - CHARACTER_OFFSET * dummy.length()/2, PlotBox.TEXT_OFFSET + yAdjust);
	}

	/**
	* Print stuff specific to this plot style.
	*/
	private static void plot(OkapiTable<Double> userTable, Color userColor) {
		// ---------------------------------------------------
		// SETUP SECTION
		final List<List<Double>> dataTable = userTable.getUserTable();
		// Get the plotting space dimensions
		final Integer bgDim = GeneralPlot.getBackgroundDim();
		
		// Clone user table (because boxplot need to sort it, and we don't want
		// to move user's stuff around).
		final List<Double> dataTableClone = new ArrayList<Double>();

		// Verify if this is a horizontal or vertical vector
		if (dataTable.size() == 1) {
			for (Double cloneItem : dataTable.get(0))
				dataTableClone.add(cloneItem);
		} else {
			GeneralPlot.verticalTableWarning();
			for (List<Double> traveller : dataTable) {
				dataTableClone.add(traveller.get(0));
			}
		}

		// Sort clone table.
		Collections.sort(dataTableClone, new Comparator<Double>() {
			@Override
			public int compare(Double a, Double b) {
				return a.compareTo(b);
			}
		});

		// Create the basis image for this plot
		PlotBox.plotImage = new BufferedImage(
			bgDim, bgDim, BufferedImage.TYPE_4BYTE_ABGR);

		// Creates the basis drawer of this plot
		final Graphics2D g = (Graphics2D) plotImage.getGraphics();

		// ---------------------------------------------------
		// CALCULUS SECTION
		// Basic values for boxplot
		Double minValue = Collections.min(dataTableClone);
		Double fstQuartile = PlotBox.getQuartile(1, dataTableClone);
		Double sndQuartile = PlotBox.getQuartile(2, dataTableClone);
		Double trdQuartile = PlotBox.getQuartile(3, dataTableClone);
		Double maxValue = Collections.max(dataTableClone);

		// Calculate the right screen position of each piece
		Integer minValueXPosition = GeneralPlot.getXPosition(minValue).intValue();
		Integer fstQuartileXPosition = GeneralPlot.getXPosition(fstQuartile).intValue();
		Integer sndQuartileXPosition = GeneralPlot.getXPosition(sndQuartile).intValue();
		Integer trdQuartileXPosition = GeneralPlot.getXPosition(trdQuartile).intValue();
		Integer maxValueXPosition = GeneralPlot.getXPosition(maxValue).intValue();

		// IQR and outliers threshold stuff
		Double interQuartileRange = (trdQuartile - fstQuartile);
		Double minOutlier = fstQuartile - 1.5 * interQuartileRange; 
		Double maxOutlier = trdQuartile + 1.5 * interQuartileRange; 

		// Calculate the outliers threshold screen position
		Integer minOutlierXPosition = GeneralPlot.getXPosition(minOutlier).intValue();
		Integer maxOutlierXPosition = GeneralPlot.getXPosition(maxOutlier).intValue();

		// ---------------------------------------------------
		// PAINT SECTION
		BasicStroke strokeStyle = new BasicStroke(2);
		g.setStroke(strokeStyle);

		// Set user given color (white by default)
		g.setColor(userColor);

		// 
		g.fillRect(
			fstQuartileXPosition, 
			PlotBox.BOX_OFFSET - PlotBox.BOX_HEIGHT/2, 
			trdQuartileXPosition - fstQuartileXPosition, 
			PlotBox.BOX_HEIGHT);

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

		// ---------------------------------------------------
		// TEXT SECTION
		// Need to be improved.
		Integer q2CharacterAdjust = (sndQuartileXPosition - fstQuartileXPosition < CHARACTER_OFFSET*4) ? (CHARACTER_OFFSET * 2): 0;
		Integer q3CharacterAdjust = (trdQuartileXPosition - sndQuartileXPosition < CHARACTER_OFFSET*4) ? (CHARACTER_OFFSET * 2): 0;

		drawValue(g, minValue, minValueXPosition, 0);
		drawValue(g, fstQuartile, fstQuartileXPosition, 0);
		drawValue(g, sndQuartile, sndQuartileXPosition, - q2CharacterAdjust);
		drawValue(g, trdQuartile, trdQuartileXPosition, - q3CharacterAdjust -
			(q2CharacterAdjust > 0 && q3CharacterAdjust > 0 ? q2CharacterAdjust : 0));
		drawValue(g, maxValue, maxValueXPosition, 0);

		g.drawString("MIN", minValueXPosition - CHARACTER_OFFSET, PlotBox.VALUE_OFFSET);
		g.drawString("Q1", fstQuartileXPosition - CHARACTER_OFFSET, PlotBox.VALUE_OFFSET);
		g.drawString("Q2", sndQuartileXPosition - CHARACTER_OFFSET, PlotBox.VALUE_OFFSET + q2CharacterAdjust);
		g.drawString("Q3", trdQuartileXPosition - CHARACTER_OFFSET, PlotBox.VALUE_OFFSET + q3CharacterAdjust + 
			(q2CharacterAdjust > 0 && q3CharacterAdjust > 0 ? q2CharacterAdjust : 0));
		g.drawString("MAX", maxValueXPosition - CHARACTER_OFFSET, PlotBox.VALUE_OFFSET );

		// Outliers
		if (minValue < minOutlier) {
			g.setColor(Color.BLACK);
			g.drawString("(OUTLIER)", minValueXPosition - CHARACTER_OFFSET*4, PlotBox.VALUE_OFFSET - CHARACTER_OFFSET*2);
			g.setColor(Color.RED);
			g.drawString("MIN", minOutlierXPosition - CHARACTER_OFFSET, PlotBox.VALUE_OFFSET);
			g.drawString("(THRESHOLD)", minOutlierXPosition - CHARACTER_OFFSET*5, PlotBox.VALUE_OFFSET + CHARACTER_OFFSET*2);
			drawValue(g, minOutlier, minOutlierXPosition, 0);
			g.drawLine(minOutlierXPosition, 
				PlotBox.BOX_OFFSET - PlotBox.BOX_HEIGHT/2, 
				minOutlierXPosition, 
				PlotBox.BOX_HEIGHT/2 + PlotBox.BOX_OFFSET);
		}
		if (maxValue > maxOutlier) {
			g.setColor(Color.BLACK);
			g.drawString("(OUTLIER)", maxValueXPosition - CHARACTER_OFFSET*4, PlotBox.VALUE_OFFSET - CHARACTER_OFFSET*2);
			g.setColor(Color.RED);
			g.drawString("MAX", maxOutlierXPosition - CHARACTER_OFFSET, PlotBox.VALUE_OFFSET);
			g.drawString("(THRESHOLD)", maxOutlierXPosition - CHARACTER_OFFSET*5, PlotBox.VALUE_OFFSET + CHARACTER_OFFSET*2);
			drawValue(g, maxOutlier, maxOutlierXPosition, 0);
			g.drawLine(maxOutlierXPosition, 
				PlotBox.BOX_OFFSET - PlotBox.BOX_HEIGHT/2, 
				maxOutlierXPosition, 
				PlotBox.BOX_HEIGHT/2 + PlotBox.BOX_OFFSET);
		}
	}
}