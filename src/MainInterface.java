import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class MainInterface {
	public static void main(String[] args) throws IOException, InterruptedException {
		JPanel panel = new JPanel();
		JFrame janela = new JFrame("Okapi");
		BufferedImage myPicture = ImageIO.read(new File("okapi.png"));
		BufferedImage myPicture2 = ImageIO.read(new File("icone.png"));
		JLabel picLabel = new JLabel(new ImageIcon(myPicture));
		janela.setIconImage(myPicture2);
		janela.add(picLabel);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    janela.pack();
	    janela.setVisible(true);
	    //Sleep do logo
	    Thread.sleep(3000);
	    janela.remove(picLabel);
	    JLabel text = new JLabel("Welcome to Okapi! Type 'help' for a list of commands.");
	    text.setFont(new Font("Arial",1,15));
	    panel.add(text);
	    janela.add(panel);
	    janela.validate();
	    janela.repaint();
	}
}

