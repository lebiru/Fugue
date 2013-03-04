import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.Scanner;

import com.sun.jdi.ClassNotLoadedException;

public class Main {

	public static void main(String[] args) throws IOException, InterruptedException, ClassNotLoadedException  {

		Graph g = new Graph();
		System.out.println("Fugue: Visualized Java Debugger");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screenSize.getWidth() - 10;
		int height = (int)screenSize.getHeight() - 80;
		Frame app = new Frame("Graph");
		System.out.println("Enter the name of the program, exluding .java, that you would like to examine:");
		Scanner s = new Scanner(System.in);
		String programName = s.nextLine();
		System.out.println("Please Enter a port number between (8000-9999): ");
		Scanner t = new Scanner(System.in);
		String port = s.nextLine();
		System.out.println("Starting Fugue on: " + programName);
		s.close();
		
		
		String os = System.getProperty("os.name");

		//if(os.contains("Windows")) //if Windows is the Operating System

		//WINDOWS
		if(os.contains("Windows"))
		{
			Runtime.getRuntime().exec(
					"cmd PATH = " +
							"\"C:\\Program Files\\Java\\jdk1.7.0_13\\bin\";" +
							"\"C:\\Program Files (x86)\\Java\\jdk1.7.0_13\\lib\";" +
					"\"C:\\Program Files (x86)\\Java\\jdk1.7.0_13\\lib\\tools.jar\"");
			Runtime.getRuntime().exec("cmd javac -g " + programName + ".java");
			Runtime.getRuntime().exec("cmd java -Xdebug -Xrunjdwp:transport=dt_socket,address="+port+",server=y,suspend=n " + programName + "").waitFor();
		}
		//UNIX
		else 
		{
			Runtime.getRuntime().exec("javac -g " + programName + ".java").waitFor();
			Runtime.getRuntime().exec("java -Xdebug -Xrunjdwp:transport=dt_socket,address="+port+",server=y,suspend=n " + programName + "").waitFor();
		}

		try {
		    Thread.sleep(100);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}

		FieldMonitor m = new FieldMonitor();	
		FieldMonitor.monitorSys(Integer.parseInt(port),g);
		g.updateGraph(g.vertices, g.edges);

		//Graph test
		g.displayGraph();
		g.displayEdges();

		// call function to draw graph
		ClickyCanvas c = new ClickyCanvas(g);

		app.addWindowListener(new Closer());
		c.setPreferredSize(new Dimension(width, height));
		app.add(c);
		app.pack();
		app.setVisible(true);
		c.setVisible(true);
		c.setBackground(Color.white);

	}

	private static String getNameOfProgram() {
		// TODO Auto-generated method stub
		return null;
	}



}