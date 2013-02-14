import java.io.IOException;

import com.sun.jdi.ClassNotLoadedException;

public class Main {
	/**
	 * @param args
	 * @throws ClassNotLoadedException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static void main(String[] args) { //throws IOException, InterruptedException, ClassNotLoadedException {
		System.out.println("Fugue: Visualized Java Debugger");
		
		
//		Runtime.getRuntime().exec("javac -g InterestingQueue.java").waitFor();
//		Runtime.getRuntime().exec("java -Xdebug -Xrunjdwp:transport=dt_socket,address=8686,server=y,suspend=n InterestingQueue").waitFor();
//		
//		try {
//		    Thread.sleep(100);
//		} catch(InterruptedException ex) {
//		    Thread.currentThread().interrupt();
//		}
//		
//		
//
//		FieldMonitor m = new FieldMonitor();	
//		FieldMonitor.monitorSys(8686);
		
		//Graph test

		Graph g = new Graph();
		g.staticTestFillGraph();
		g.displayGraph();
		
//		g.testFillEdges(g);
//		g.testFillVertexes(g);
		
		g.displayEdges();
		g.displayVertexes();
		
		// call function to draw graph
		View v = new View();
		v.drawMe(g);

		
	}
	
	

}
