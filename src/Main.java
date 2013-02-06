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
		System.out.println("Testing using EGit");
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screenSize.getWidth();
		int height = (int)screenSize.getHeight();
		
		// initialize window
		Frame app = new Frame("Click me!");
		app.addWindowListener(new Closer());
        ClickyCanvas c = new ClickyCanvas();
		c.setPreferredSize(new Dimension(width, height));
		app.add(c);
        app.pack();
        app.setVisible(true);
		c.setVisible(true);
		c.setBackground(Color.white);

	}

}
