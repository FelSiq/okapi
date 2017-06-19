// -----------------------------------------------
//IMPORT SECTION
// 1. IO
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

// 2. SWING
import javax.swing.SwingUtilities;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.Timer;

// 3. AWT
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Font;
// -----------------------------------------------

/**
* Main host class of this program.
*/
public abstract class MainInterface {
	// -----------------------------------------------
	// CONSTANT SECTION
	// Default used image path (icon and logo)
	private static final String IMG_DEFAULT_PATH = "./images/"; 

	// How many seconds the logo screen should last (in seconds).
	private static final int LOGO_FRAME_SECONDS = 3;
	// -----------------------------------------------
	// METHOD SECTION

	/**
	* Set given frame to a centered position in the monitor
	*/
	private static void setFrameCentered(JFrame myFrame) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		myFrame.setLocation(dim.width/2-myFrame.getSize().width/2, dim.height/2-myFrame.getSize().height/2);
	}

	/**
	* Draw the initial window of the program, which one display logo and welcome text.
	*/
	private static void initialWindow() {
		try {
			// Create logo main frame.
			final JFrame janela = new JFrame("Okapi");

			// Load icon.
			BufferedImage myPicture2 = ImageIO.read(new File(MainInterface.IMG_DEFAULT_PATH + "icone.png"));
			janela.setIconImage(myPicture2);

			// Load logo.
			BufferedImage myPicture = ImageIO.read(new File(MainInterface.IMG_DEFAULT_PATH + "okapi.png"));
			JLabel picLabel = new JLabel(new ImageIcon(myPicture));
			janela.add(picLabel, BorderLayout.CENTER);
		    
		    // Set up welcome text.
		    JPanel auxiliaryPanel = new JPanel();
		    JLabel welcomeText = new JLabel ("<html><center>Welcome to</center></html>");
		    welcomeText.setFont(new Font("Arial", 1, 25));
		    auxiliaryPanel.add(welcomeText);
			janela.add(auxiliaryPanel, BorderLayout.NORTH);

			// Set up initial instructions.
		    JLabel instructionText = new JLabel("<html>Type 'commands' for a list of commands, and use 'help' if needed.</html>");
		    instructionText.setFont(new Font("Arial", 1, 15));
		    janela.add(instructionText, BorderLayout.SOUTH);

		    // Pack logo frame.
		    janela.pack();

		    // Set initial window to the center of the screen
		    MainInterface.setFrameCentered(janela);

		    // Turn frame visible to user.
		    janela.setVisible(true);

		    // Set up a timer, which one automatically dispose the logo frame 3 seconds after
		    // program starts.
		    Timer timer = new Timer(MainInterface.LOGO_FRAME_SECONDS * 1000, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					janela.dispose();
				}
		    });
		    timer.start();

		} catch (IOException | NullPointerException e) {
			System.out.println(e.getMessage());
		}
	}

	// -----------------------------------------------
	// MAIN FUNCTION
	public static void main(String[] args) {
		// Initial program logo		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainInterface.initialWindow();
			}
		});


		// Compile Interpreter regexes.
	    Interpreter.compile();

	    // Aesthetics 
	    System.out.println("You may type a command now: ");

	    // Main program Loop
		while(!Interpreter.programEnds()) {
			System.out.print("> ");
			Interpreter.getInput();
		}

		// Kill all threads when main program loop ends.
		System.exit(0);
	}
}