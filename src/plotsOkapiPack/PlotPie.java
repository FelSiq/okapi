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
		GeneralPlot.setAxis(false);

		// Call plot method and prepare the graphic image
		PlotPie.plot(dataTable, userColor);
	}

	/**
	* Print stuff specific to this plot style.
	*/
	private static void plot(OkapiTable<Double> dataTable, Color userColor) {
		// Get the plotting space dimensions
		Integer bgDim = GeneralPlot.getBackgroundDim();
		starter = 0;
		
		// Create the basis image for this plot
		PlotPie.plotImage = new BufferedImage(
			bgDim, bgDim, BufferedImage.TYPE_4BYTE_ABGR);

		// Creates the basis drawer of this plot
		Graphics2D g = (Graphics2D) plotImage.getGraphics();
		JPanel panel = new JPanel();
		
		g.drawOval(bgDim/2, bgDim/2, 300, 300);
		
		for(int i=0;i<dataTable.getRowNum();i++){
			Random rand = new Random();
			Color c = new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255),
					rand.nextInt(255));
			JLabel lab = new JLabel(dataTable.getRowName(i),JLabel.RIGHT);
			panel.add(lab);
			
			g.setColor(c);
			g.drawRect(5, 10, 20, 20);
			g.fillArc(bgDim/2, bgDim/2, 300, 300, starter, 
				(int) (starter+(360/dataTable.getElement(i, 0))));
			starter +=  360/dataTable.getRowNum();
		}
		
		panel.setVisible(true);
		// INSTRUCTIONS: 
		// 1. Just draw on g (g.drawLine, g.drawOval, g.drawRect...)
		// And the magic will just happens. Trust me.
		// 2. If you want to plot a (x, y) dot, then always draw
		// on the (GeneralPlot.getXPosition(x), GeneralPlot.getYPosition(y)) 
		// This will convert the (x, y) value to the correct (x', y') screen position.

	}
}