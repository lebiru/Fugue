import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;

public class View {
	
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int width = (int)screenSize.getWidth();
	int height = (int)screenSize.getHeight();
	
	public View()
	{
		
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
	public static void drawMe(Graph g)
	{
	
		
		
		
	}

}
