import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.io.IOException;

import com.sun.jdi.ClassNotLoadedException;

public class Main {
	/**
	 * @param args
	 * @throws ClassNotLoadedException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotLoadedException {
		System.out.println("Fugue: Visualized Java Debugger");
		
		
		FieldMonitor m = new FieldMonitor();
		m.monitorSys(8686);

	

		Graph g = new Graph();

		g.testFillGraph(100,10,20);
		g.displayGraph();

		g.testFillGraph(5,2,5);
		g.testFillEdges(g);
		g.displayGraph();
		g.displayEdges(g);
		
		// call function to draw graph
		View v = new View();
		v.drawMe(g);

		
	}
	
	

}
