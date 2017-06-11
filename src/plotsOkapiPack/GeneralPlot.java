package plotsOkapiPack;
// -----------------------------------------
// IMPORT SECTION
// 1. Swing 
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

// 2. Events
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Font;

// 3. Aesthetics stuff
import java.awt.Color;

// To be deleted.
import javax.swing.SwingUtilities;
// -----------------------------------------

/**
* This class should handle all common stuff between all plot types.
*/
public abstract class GeneralPlot {
	// -------------------------------------------------
	// CONSTANTS SECTION
	// Title displayed on the plotting window
	public static final String PLOT_WINDOW_TITLE = "Okapi Magplot Interface";

	// -------------------------------------------------
	// VARIABLE SECTION
	// Controls the display of the plot axis
	private static boolean plot_axis_visible = false;

	// Offset of x-axis on the plot (first quadrant only, by default)
	private static int plot_axis_xoffset = 0;

	// Offset of y-axis on the plot (first quadrant only, by default)
	private static int plot_axis_yoffset = 0;

	// Label of y-axis on the plot (null by default)
	private static String plot_axis_xlabel = null;

	// Label of y-axis on the plot (first quadrant only, by default)
	private static String plot_axis_ylabel = null;

	// Define the max value of the scale on the x-axis
	private static Double plot_xlim_max = 10.0;

	// Define the min value of the scale on the x-axis
	private static Double plot_xlim_min = 0.0;

	// Define the max value of the scale on the y-axis
	private static Double plot_ylim_max = 10.0;

	// Define the min value of the scale on the y-axis
	private static Double plot_ylim_min = 0.0;

	// Number of intervals between min and max on the x-axis
	private static int plot_xinterval_num = 10;

	// Number of intervals between min and max on the y-axis
	private static int plot_yinterval_num = 10;

	// Main title of the plot
	private static String plot_title = null;

	// -------------------------------------------------
	// INNER CLASS SECTION
	/**
	* This class is responsible to add a clean white backgroung (white black borders) on the
	* plot window.
	*/
	private class BackgroundRectangle extends JPanel {
		// -------------------------------------------------
		// CONSTANTS SECTION
		protected static final int BG_X = 2;
		protected static final int BG_Y = 2;
		protected static final int BG_W = 624;
		protected static final int BG_H = 624;
		// -------------------------------------------------

		/**
		* Print the plot window background (white rectangle with black border)
		*/
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			// Draw the white background of the graphic
			g.setColor(Color.WHITE);
			g.fillRect(
				BackgroundRectangle.BG_X, 
				BackgroundRectangle.BG_Y,
				BackgroundRectangle.BG_W,
				BackgroundRectangle.BG_H);
			// Draw a black border around the background rectangle
			g.setColor(Color.BLACK);
			g.drawRect(
				BackgroundRectangle.BG_X, 
				BackgroundRectangle.BG_Y,
				BackgroundRectangle.BG_W,
				BackgroundRectangle.BG_H);
		}

		/**
		* Calculate the total frame size (to adjust the window automatically with pack() method).
		*/
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(
				BackgroundRectangle.BG_W + 2 * BackgroundRectangle.BG_X, 
				BackgroundRectangle.BG_H + 2 * BackgroundRectangle.BG_Y);
		}
	}

	/**
	* This inner class is responsible of drawing a plot space, with all configuration
	* of BackgroundRectangle class plus cartesian axes.
	*/
	private class MakeAxis extends BackgroundRectangle {
		// -------------------------------------------------
		// CONSTANTS SECTION
		// Default pixel-offset of the axes label
		private static final int LABEL_OFFSET = 6;

		// Default pixel-offset value of a character
		private static final int CHAR_PIXELOFFSET = 7;

		// Values correspondent to x-axis on the Jframe
		private static final int X_AXIS_X1 = 12;
		private static final int X_AXIS_X2 = 620;
		private static final int X_AXIS_Y1 = 600;
		private static final int X_AXIS_Y2 = MakeAxis.X_AXIS_Y1;

		// Values correspondent to y-axis on the Jframe
		private static final int Y_AXIS_X1 = 24;
		private static final int Y_AXIS_X2 = MakeAxis.Y_AXIS_X1;
		private static final int Y_AXIS_Y1 = 620;
		private static final int Y_AXIS_Y2 = 12;

		// -------------------------------------------------
		// VARIABLE SECTION
		private String x_axis_label; 
		private String y_axis_label;
		private int x_axis_offset;
		private int y_axis_offset; 

		// -------------------------------------------------
		// METHOD SECTION

		/**
		* Auxiliary method, used to print both x-axis base line and x-axis label
		*/
		private void drawXAxis(Graphics g) {
			// Print x-axis base line
			g.drawLine(
				MakeAxis.X_AXIS_X1, 
				MakeAxis.Y_AXIS_Y1 - this.x_axis_offset + Y_AXIS_Y2,
				MakeAxis.X_AXIS_X2,
				MakeAxis.Y_AXIS_Y1 - this.x_axis_offset + Y_AXIS_Y2);

			// Draw x-axis label, if available
			if (this.x_axis_label != null)
				g.drawString(this.x_axis_label, 
					MakeAxis.Y_AXIS_Y1 + Y_AXIS_Y2 - MakeAxis.LABEL_OFFSET + 1 - MakeAxis.CHAR_PIXELOFFSET * this.x_axis_label.length(), 
					MakeAxis.Y_AXIS_Y1 + Y_AXIS_Y2 - MakeAxis.LABEL_OFFSET - this.x_axis_offset);
		}

		/**
		* Auxiliary method, used to print both y-axis base line and y-axis label
		*/
		private void drawYAxis(Graphics g) {
			// Print y-axis base line
			g.drawLine(
				this.y_axis_offset, 
				MakeAxis.Y_AXIS_Y1,
				this.y_axis_offset,
				MakeAxis.Y_AXIS_Y2);

			// Draw y-axis label, if available
			if (this.y_axis_label != null)
				g.drawString(this.y_axis_label, 
					MakeAxis.Y_AXIS_X2 + MakeAxis.LABEL_OFFSET + this.y_axis_offset, 
					MakeAxis.Y_AXIS_Y2 + MakeAxis.LABEL_OFFSET);
		}

		/**
		* Auxiliary method, used to print x-axis interval values
		*/
		private void drawXIntervals(Graphics g) {
			// First, get axes subinterval sizes
			Double xSubintervalSize = (GeneralPlot.plot_xlim_max - GeneralPlot.plot_xlim_min) / GeneralPlot.plot_xinterval_num;
			
			// Print x-axis interval values
			for (Double i = (GeneralPlot.plot_xlim_max - xSubintervalSize); i > GeneralPlot.plot_xlim_min; i -= xSubintervalSize) {
				// Calculate the correspondent x position on the Jframe of the x value of the interval
				Double xPlace = MakeAxis.X_AXIS_X1 + ((MakeAxis.X_AXIS_X2 - MakeAxis.X_AXIS_X1) * 
					((i - GeneralPlot.plot_xlim_min) / (GeneralPlot.plot_xlim_max  - GeneralPlot.plot_xlim_min)));

				// Turn the found value into a integer
				Integer xPlaceInteger = ((Double) Math.ceil(xPlace)).intValue(); 

				// Fix up found value in order to print the value correctly (and not something like 3.9999999987).
				String xPositionValue = ((Integer) ((Double) Math.ceil(i)).intValue()).toString();

				// Draw a small horizontal mark on the correspondent x-value of the current interval value
				g.drawLine(
					xPlaceInteger, MakeAxis.Y_AXIS_Y1 + Y_AXIS_Y2 - this.x_axis_offset - 2, 
					xPlaceInteger, MakeAxis.Y_AXIS_Y1 + Y_AXIS_Y2 - this.x_axis_offset + 2);

				// Print up the correspondent interval value, on the current x-axis JFrame position
				g.drawString(
					xPositionValue, 
					xPlaceInteger - MakeAxis.CHAR_PIXELOFFSET * xPositionValue.length()/2, 
					MakeAxis.Y_AXIS_Y1 + Y_AXIS_Y2 - this.x_axis_offset + 16);
			}
		}

		/**
		* Auxiliary method, used to print y-axis interval values
		*/
		private void drawYIntervals(Graphics g) {
			// First, get axes subinterval sizes
			Double ySubintervalSize = (GeneralPlot.plot_ylim_max - GeneralPlot.plot_ylim_min) / GeneralPlot.plot_yinterval_num;
			
			// 
			for (Double i = (GeneralPlot.plot_ylim_min + ySubintervalSize); i < GeneralPlot.plot_ylim_max; i += ySubintervalSize) {
				// Calculate the correspondent y position on the Jframe of the y value of the interval
				Double yPlace = MakeAxis.Y_AXIS_Y1 + ((MakeAxis.Y_AXIS_Y2 - MakeAxis.Y_AXIS_Y1) * 
					((i - GeneralPlot.plot_ylim_min) / (GeneralPlot.plot_ylim_max  - GeneralPlot.plot_ylim_min)));

				// Turn the found value into a integer
				Integer yPlaceInteger = ((Double) Math.ceil(yPlace)).intValue(); 

				// Fix up found value in order to print the value correctly (and not something like 3.9999999987).
				String yPositionValue = ((Long) Math.round(i)).toString();

				// Draw a small horizontal mark on the correspondent y-value of the current interval value
				g.drawLine(
					this.y_axis_offset - 2, yPlaceInteger,
					this.y_axis_offset + 2, yPlaceInteger);

				// Print up the correspondent interval value, on the current y-axis JFrame position
				g.drawString(
					yPositionValue,
					this.y_axis_offset + 16, 
					yPlaceInteger);
			}
		}

		/**
		* Print axes and it's labels, if available. 
		*/
		@Override
		protected void paintComponent(Graphics g) {
			// Draw a black border around the background rectangle
			super.paintComponent(g);
			
			// Axes are black by default
			g.setColor(Color.BLACK);

			// Draw a labeled x-axis base
			drawXAxis(g);
			// Draw a labeled y-axis base
			drawYAxis(g);

			// Draw axes limits
			drawXIntervals(g);
			drawYIntervals(g);
		}

		/**
		* Class constructor with labeled axes and first quadrant only.
		*/
		public MakeAxis(String newXAxisLabel, String newYAxisLabel) {
			this.x_axis_label = newXAxisLabel;
			this.y_axis_label = newYAxisLabel;
			this.x_axis_offset = 0;
			this.y_axis_offset = 0;
		}

		/**
		* Class constructor with unlabeled axes and first quadrant only.
		*/
		public MakeAxis() {
			this.x_axis_label = "x";
			this.y_axis_label = "y";
			this.x_axis_offset = 0;
			this.y_axis_offset = 0;
		}

		/**
		* Class constructor with unlabeled axes and cartesian origin offset.
		*/
		public MakeAxis(int xAxisOffset, int yAxisOffset) {
			this.x_axis_label = "x";
			this.y_axis_label = "y";
			this.x_axis_offset = xAxisOffset;
			this.y_axis_offset = yAxisOffset;
		}

		/**
		* Class constructor with labeled axes and cartesian origin offset.
		*/
		public MakeAxis(String newXAxisLabel, String newYAxisLabel, int xAxisOffset, int yAxisOffset) {
			this.x_axis_label = newXAxisLabel;
			this.y_axis_label = newYAxisLabel;
			this.x_axis_offset = xAxisOffset;
			this.y_axis_offset = yAxisOffset;
		}
	}

	// -------------------------------------------------
	// METHODS SECTION

	/**
	* Instantiate a new JButton.
	* @Return New JButton with given text and mnemonic.
	*/
	private JButton createButton(String buttonText, int buttonMnemonic) {
		JButton newButton = new JButton(buttonText);
		newButton.setMnemonic(buttonMnemonic);
		return newButton;
	}

	/**
	* Set up user's title to the current graphic, if any.
	*/
	private void addGraphicTitle(JFrame myFrame) {
		// If user specified a title to the graphic...
		if (GeneralPlot.plot_title != null) {
			// Create a new font to the Graphic
			Font graphicTitleFont = new Font("Arial", Font.PLAIN, 20);

			// Create the title container itself
			JLabel graphicTitle = new JLabel(GeneralPlot.plot_title);
			
			// Set the font created previously to the title container
			graphicTitle.setFont(graphicTitleFont);

			// Create a title Panel, in order to keep graphic title centralized
			JPanel titlePanel = new JPanel();

			// Add the title to the brand-new title panel
			titlePanel.add(graphicTitle);

			// Add the Title Panel to the top of the given JFrame
			myFrame.add(titlePanel, BorderLayout.PAGE_START);
		}
	}

	/**
	* Instantiates a new main JPanel.
	* @Return A brand-new main Jpanel.
	*/
	private JPanel createPanel() {
		// Creates new panel
		JPanel newPanel = new JPanel();

		// Add plot background with or without axis
		// Beware with the ternary operator.
		newPanel.add(GeneralPlot.plot_axis_visible 
			? new GeneralPlot.MakeAxis(
				GeneralPlot.plot_axis_xlabel, 
				GeneralPlot.plot_axis_ylabel, 
				GeneralPlot.plot_axis_xoffset, 
				GeneralPlot.plot_axis_yoffset) 
			: new GeneralPlot.BackgroundRectangle());

		// Return brand-new panel
		return newPanel;
	}

	/**
	* Set given frame to a centered position in the monitor
	*/
	private void setFrameCentered(JFrame myFrame) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		myFrame.setLocation(dim.width/2-myFrame.getSize().width/2, dim.height/2-myFrame.getSize().height/2);
	}

	/**
	* Creates a new frame with the given name and panel.
	* @return New visible frame with given specifications. 
	*/
	private JFrame createFrame(String frameName, JPanel mainPanel, JPanel buttonPanel) {
		// Creates new main frame
		JFrame newFrame = new JFrame(frameName);

		// Set previously made panel to the frame
		newFrame.add(mainPanel, BorderLayout.CENTER);

		// Add button Panel to the bottom of the frame
		newFrame.add(buttonPanel, BorderLayout.SOUTH);

		// Set default action on pressing the close button
		newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add graphic title to Frame, if any
		addGraphicTitle(newFrame);

		// Adjust the window
		newFrame.pack();

		// Set this frame to the center of the monitor 
		setFrameCentered(newFrame);

		// Change interface L&F to system L&F (Metal by default)
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | 
				InstantiationException | 
				IllegalAccessException | 
				UnsupportedLookAndFeelException e) {
			System.out.println(e.getMessage());
		}

		// Set visible
		newFrame.setVisible(true);

		return newFrame;
	}

	/**
	* Create a Panel button only
	*/
	private JPanel createButtonsPanel(JButton... buttons) {
		JPanel newButtonPanel = new JPanel();
		for (JButton jb : buttons)
			newButtonPanel.add(jb);
		return newButtonPanel;
	}

	/**
	* GUI plot initialization
	*/
	private GeneralPlot() {
		// Instantiate auxiliary buttons
		// Save button (create a output file with the given graphic)
		JButton bSave = createButton("Save", KeyEvent.VK_P);
		bSave.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				// To be continued..
			}
		});

		// Close the plot window
		JButton bClose = createButton("Close", KeyEvent.VK_C);

		// Creates the panel
		JPanel mainPanel = createPanel();

		// Create a button-only panel
		JPanel buttonsPanel = createButtonsPanel(bSave, bClose);

		// Create frame
		JFrame myFrame = createFrame(GeneralPlot.PLOT_WINDOW_TITLE, mainPanel, buttonsPanel);

		// Add funcionality on the Close button
		bClose.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				myFrame.dispose();
			}
		});
	}

	/**
	* Reverse the axes visibility on the plot (false by default).
	*/
	public static void setAxis() {
		GeneralPlot.plot_axis_visible = !GeneralPlot.plot_axis_visible;
	}

	/**
	* Set the value off x-axis offset on the plot (setter). Final user should
	* not hande this manually, generalPlot adjust this automatically instead.
	*/
	private static void setXAxisOffset(int newXOffsetValue) {
		GeneralPlot.plot_axis_xoffset = newXOffsetValue;
	}

	/**
	* Set the value off y-axis offset on the plot (setter). Final user should
	* not hande this manually, generalPlot adjust this automatically instead.
	*/
	private static void setYAxisOffset(int newYOffsetValue) {
		GeneralPlot.plot_axis_yoffset = newYOffsetValue;
	}

	/**
	* Set the value off x-axis label on the plot (setter)
	*/
	public static void setXAxisLabel(String newXLabel) {
		GeneralPlot.plot_axis_xlabel = newXLabel;
	}

	/**
	* Set the value off y-axis label on the plot (setter)
	*/
	public static void setYAxisLabel(String newYLabel) {
		GeneralPlot.plot_axis_ylabel = newYLabel;
	}

	/**
	* Set the interval of x-axis and the number of subintervals between each x-axis value print.
	*/
	public static void setXLimits(Double xMin, Double xMax, int xIntervalNumber) throws IllegalArgumentException {
		// Check if parameters are mathematically possible
		if (xIntervalNumber <= 0)
			throw new IllegalArgumentException("E: Interval number must be strictly positive.");
		if (xMin >= xMax)
			throw new IllegalArgumentException("E: min-x value must be strictly smaller than max-x.");

		// Set up new values for variables
		GeneralPlot.plot_xinterval_num = xIntervalNumber;
		GeneralPlot.plot_xlim_min = xMin;
		GeneralPlot.plot_xlim_max = xMax;

		// Automatically adjust y-axis offset, to match x = 0.
		GeneralPlot.setYAxisOffset(((Double) (MakeAxis.X_AXIS_X1 + ((MakeAxis.X_AXIS_X2 - MakeAxis.X_AXIS_X1) * 
				((GeneralPlot.plot_xlim_min) / (Double) (GeneralPlot.plot_xlim_min - GeneralPlot.plot_xlim_max))))).intValue());
	}

	/**
	* Set the interval of y-axis and the number of subintervals between each y-axis value print.
	*/
	public static void setYLimits(Double yMin, Double yMax, int yIntervalNumber) throws IllegalArgumentException {
		// Check if parameters are mathematically possible
		if (yIntervalNumber <= 0)
			throw new IllegalArgumentException("E: Interval number must be strictly positive.");
		if (yMin >= yMax)
			throw new IllegalArgumentException("E: min-y value must be strictly smaller than max-y.");

		// Set up new values for variables
		GeneralPlot.plot_yinterval_num = yIntervalNumber;
		GeneralPlot.plot_ylim_min = yMin;
		GeneralPlot.plot_ylim_max = yMax;

		// Automatically adjust y-axis offset, to match y = 0.
		GeneralPlot.setXAxisOffset(((Double) (MakeAxis.Y_AXIS_Y2 + ((MakeAxis.Y_AXIS_Y1 - MakeAxis.Y_AXIS_Y2) * 
				((GeneralPlot.plot_ylim_min) / (Double) (GeneralPlot.plot_ylim_min - GeneralPlot.plot_ylim_max))))).intValue());
	}

	/**
	* Set the plot main title (setter)
	*/
	public static void setTitle(String newTitle) {
		GeneralPlot.plot_title = newTitle;
	}

	/**
	* Get the plot main title (getter)
	*/
	public static String getTitle() {
		return GeneralPlot.plot_title;
	}

	/**
	* Should be deleted, only for tests.
	*/
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GeneralPlot.setAxis();
				GeneralPlot.setTitle("TestGraphic");
				GeneralPlot.setXLimits(-20.0, 1220.0, 15);
				GeneralPlot.setYLimits(-20.0, 620.0, 10);
				GeneralPlot.setXAxisLabel("X-label");
				GeneralPlot.setYAxisLabel("Y-label");
				
				// Can't instantiate anymore, because now its abstract
				// GeneralPlot testPlot = new GeneralPlot();
			}
		});
	}
} 