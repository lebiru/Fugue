import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;


public class Main {
	// Katrina's test
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Hello world!");
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screenSize.getWidth();
		int height = (int)screenSize.getHeight();
		
		// initialize window
		Frame app = new Frame("Click me!");
		app.setPreferredSize(new Dimension(width, height));
		app.addWindowListener(new Closer());
		app.pack();
		app.setVisible(true);
		app.setBackground(Color.white);

	}

}
