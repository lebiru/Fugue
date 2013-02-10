import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;

public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Fugue: Visualized Java Debugger");
		System.out.println("working");
		
		
		Graph g = new Graph();
		g.testFillGraph(5,2,5);
		g.textDisplay();
		
		// call function to draw graph
		View v = new View();
		v.drawMe(g);
		
	}
	
	

}
