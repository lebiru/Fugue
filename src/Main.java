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


		Graph g = new Graph();
		g.testFillGraph(5,2,5);
		g.textDisplay();
		
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
