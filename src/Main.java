import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.io.IOException;

//import com.sun.jdi.ClassNotLoadedException;

public class Main {
	/**
	 * @param args
	 * @throws ClassNotLoadedException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args) { //throws IOException, InterruptedException, ClassNotLoadedException {
		System.out.println("Fugue: Visualized Java Debugger");
		
		/*
		Runtime.getRuntime().exec("javac -g InterestingQueue.java").waitFor();
		Runtime.getRuntime().exec("java -Xdebug -Xrunjdwp:transport=dt_socket,address=8686,server=y,suspend=n InterestingQueue").waitFor();
		
		try {
		    Thread.sleep(100);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		*/
		
		//FieldMonitor m = new FieldMonitor();	
		//FieldMonitor.monitorSys(8686);
		Graph g = new Graph();
		g.testFillGraph(100,10,20);
		g.displayGraph();
		System.out.println("Hello");
		g.testFillGraph(5,2,5);
		g.testFillEdges(g);
		g.displayGraph();
		g.displayEdges(g);
		
		// call function to draw graph
		View v = new View();
		v.drawMe(g);

		
	}
	
	

}
