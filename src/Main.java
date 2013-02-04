import java.awt.Dimension;
import java.awt.Frame;


public class Main {
	// Katrina's test
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

		System.out.println("Hello world!");
		
		// attempt at opening a window
		Frame app = new Frame("Click me!");
		app.setPreferredSize(new Dimension(600, 400));
		//app.addWindowListener(new Closer());
		app.pack();
		app.setVisible(true);

	}

}
