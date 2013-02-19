import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.io.IOException;
import com.sun.jdi.ClassNotLoadedException;

public class Main {
	
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotLoadedException  {
		
		
		System.out.println("Fugue: Visualized Java Debugger");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screenSize.getWidth() - 10;
		int height = (int)screenSize.getHeight() - 80;
		Frame app = new Frame("Click me!");
		
		String os = System.getProperty("os.name");
		if(os.contains("Windows")) //if Windows is the Operating System
		{
			Runtime.getRuntime().exec(
					"cmd PATH = " +
					"C:\\Program Files\\Java\\jdk1.7.0_13\\bin;" +
					"C:\\Program Files (x86)\\Java\\jdk1.7.0_13\\lib;" +
					"C:\\Program Files (x86)\\Java\\jdk1.7.0_13\\lib\\tools.jar");
					Runtime.getRuntime().exec("cmd javac -g InterestingQueue.java");
					Runtime.getRuntime().exec("cmd java -Xdebug -Xrunjdwp:transport=dt_socket,address=8686,server=y,suspend=n InterestingQueue").waitFor();
		}
		else //if Linux
		{
			Runtime.getRuntime().exec("javac -g InterestingQueue.java").waitFor();
			Runtime.getRuntime().exec("java -Xdebug -Xrunjdwp:transport=dt_socket,address=8686,server=y,suspend=n InterestingQueue").waitFor();
		}
		
		
		
		try {
		    Thread.sleep(100);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		
		

		FieldMonitor m = new FieldMonitor();	
		FieldMonitor.monitorSys(8686);
		
		//Graph test

		Graph g = new Graph();
		g.staticTestFillGraph();
		g.displayGraph();
		g.dynamicTestFillGraph();
		g.displayGraph();
		

		
		// call function to draw graph
		ClickyCanvas c = new ClickyCanvas();
		
		app.addWindowListener(new Closer());
		c.setPreferredSize(new Dimension(width, height));
		app.add(c);
		app.pack();
		app.setVisible(true);
		c.setVisible(true);
		c.setBackground(Color.white);

		
	}
	
	

}
