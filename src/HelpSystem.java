// IMPORT SECTION
// 1. Regex
import java.util.regex.PatternSyntaxException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
// 2. Swing
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;

// 3. AWT
import java.awt.event.ActionListener;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Font;
// 4. IO
import java.io.IOException;
import java.io.FileReader;

// ----------------------------------------------

/**
* Give user a detailed information about a choosen available command.
*/
public class HelpSystem {
	// ----------------------------------------------
	// VARIABLE SECTION
	private boolean matched;

	// Parse a whole XML function.
	private static final String REGEX_XML_ITEM = "\\s*<summary>\\s*([^<>]*)\\s*</summary>(\\s*<parameters>((?:.|\\s)*?)</parameters>)?";

	// Parse a parameter of a function.
	private static final String REGEX_PARAMETER_PARSING = "<field>\\s*([^<>]*)</field>\\s*<description>\\s*([^<>]+)\\s*</description>";

	// Default path for the Okapi XML Help Document.
	private static final String XML_DOC_PATH = "./OkapiHelp.xml";

	// Main title of the Okapi Help System Window.
	private static final String HELP_FRAME_NAME = "Okapi Help System";

	// ----------------------------------------------
	// METHOD SECTION

	/**
	* Verify if this help system found the desired functionalitie.
	* @Return The current state (true if command is found, false otherwise) of this section of the Help System.
	*/
	public boolean state() {
		return this.matched;
	}

	/**
	* @Return A pattern-form of the given user function name.
	*/
	private String strRegexForm(String string) {
		return ("<name>\\s*(" + string + ")\\s*</name>");
	}

	/**
	* Red the whole Okapi XML document, and return it's content.
	*/
	private String readFileDoc() {
		FileReader docContentFile = null;
		String docContent = "";
		try {
			docContentFile = new FileReader(HelpSystem.XML_DOC_PATH);
			int c;
			
			while ((c = docContentFile.read()) != -1) {
				docContent += ((char) c);
			}

			docContentFile.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return docContent;
	}

	/**
	* Set given frame to a centered position in the monitor
	*/
	private void setFrameCentered(JFrame myFrame) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		myFrame.setLocation(dim.width/2-myFrame.getSize().width/2, dim.height/2-myFrame.getSize().height/2);
	}

	/**
	* Translate the given raw XML parameter string to it's "printable" form.
	*/
	private String formatParameters(String parameterXMLString) {
		String formatedParameters = "";
		Matcher m = Pattern.compile(REGEX_PARAMETER_PARSING).matcher(parameterXMLString);
		Integer counter = 0;
		while (m.find()) {
			formatedParameters += (counter.toString() + ". " + m.group(1));
			formatedParameters += (": " + m.group(2) + "<br>");
			counter++;
		}
		return formatedParameters;
	}

	/**
	* Verify if a given string is a null string. If it is the case, then convert it to a
	* "No information." string.
	*/
	private String stringTreatment(String oldString) {
		return (oldString != null ? oldString : "No information.");
	}

	/**
	* Set up the main Help panel, using the GridBagLayout.
	*/
	private JPanel createHelpPanel(String function, String summary, String parameters) {
		// Instantiate a brand-new panel.
		final JPanel helpPanel = new JPanel();

		// Create the section title font.
		final Font graphicTitleFont = new Font("Arial", Font.PLAIN, 20);

		// Set up section titles.
		final JLabel labelFunctionName = new JLabel("1. FUNCTION:");
		final JLabel labelSummaryName = new JLabel("2. SUMMARY:");
		final JLabel labelParametersName = new JLabel("3. PARAMETERS:");
		
		// Set up font to the section titles.
		labelFunctionName.setFont(graphicTitleFont);
		labelSummaryName.setFont(graphicTitleFont);
		labelParametersName.setFont(graphicTitleFont);

		// Set some GridBagLayout parameters, in order to make
		// everything to go to its correct place. 
		helpPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		gbc.weighty = 0.1;
		gbc.gridx = 0;

		// Add command name section.
		gbc.gridy = 0;
		helpPanel.add(labelFunctionName, gbc);

		// Display function name.
		gbc.gridy = 1;
		helpPanel.add(new JLabel(function), gbc);
		
		// Add summary section.
		gbc.gridy = 2;
		helpPanel.add(labelSummaryName, gbc);

		// Display summarized information.
		gbc.gridy = 3;
		helpPanel.add(new JLabel("<html>" + summary + "</html>"), gbc);

		// Add Parameters section.
		gbc.gridy = 4;
		helpPanel.add(labelParametersName, gbc);

		// Create Parameters list.
		gbc.gridy = 5;
		gbc.weightx = 0.0;
		gbc.ipady = 40;
		helpPanel.add(new JLabel("<html>" + formatParameters(parameters) + "</html>"), gbc);

		return helpPanel;
	}

	/**
	* Instantiate a working JFrame "Close" button.
	*/
	private JButton createCloseButton(JFrame frame) {
		final JButton closeButton = new JButton("Close");
		closeButton.setMnemonic(KeyEvent.VK_C);
		closeButton.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent ae) {
				frame.dispose();
			}
		});
		return closeButton;
	}

	/**
	* Set up the help system window, with the given function, summary and parameters data
	* found on the Okapi XML document.
	*/
	private void setUpHelpInterface(String function, String summary, String parameters) {
		// Help main frame
		final JFrame mainHelpFrame = new JFrame(HelpSystem.HELP_FRAME_NAME);

		// Close Button
		JButton closeButton = createCloseButton(mainHelpFrame);

		// Create the main Panel of Help Frame, with all information
		// to be displayed to user.
		final JPanel helpPanel = createHelpPanel(
			stringTreatment(function), 
			stringTreatment(summary), 
			stringTreatment(parameters));

		// Adde both close button and the help data panel to the help frame.
		mainHelpFrame.add(helpPanel, BorderLayout.CENTER);
		mainHelpFrame.add(closeButton, BorderLayout.SOUTH);

		// Set final frame parameters up.
		mainHelpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainHelpFrame.setPreferredSize(new Dimension(640, 480));
		mainHelpFrame.pack();
		setFrameCentered(mainHelpFrame);
		mainHelpFrame.setVisible(true);
	}

	/**
	* Search the given function name, and print a help message about it, if found.
	* @Return True, if function was found. False otherwise. 
	*/
	public boolean search(String function) {
		try {
			// Compile the regex XML Pattern interpretation, based on the user given function.
			Matcher m = Pattern.compile(strRegexForm(function) + HelpSystem.REGEX_XML_ITEM).matcher(readFileDoc());
			if (m.find()) {
				// If something is found on the Okapi Help XML Document, then create a help window
				// with the found data.
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						setUpHelpInterface(m.group(1), m.group(2), m.group(3));
					}
				});
				return true;
			} else {
				// No information found on the Okapi XML Help document, display a error message.
				System.out.println("System: not found \"" + function + "\" on Okapi help system.");
			}
		} catch (PatternSyntaxException | NullPointerException e) {
			System.out.println(e.getMessage());
		}

		// If no information is found, return false.
		return false;
	}

	/**
	* Search on the help system the given function, and display its information, if found.
	* Display an error message if the given function was not found in the Okapi help XML document.
	*/
	public HelpSystem(String function) {
		this.matched = this.search(function); 
	}
}