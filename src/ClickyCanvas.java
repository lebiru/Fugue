import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.text.html.HTMLDocument.Iterator;


@SuppressWarnings("serial")
public class ClickyCanvas extends Canvas {
	/**
	 * 
	 */
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	static int width = (int)screenSize.getWidth() - 10;
	static int height = (int)screenSize.getHeight() - 80;
	static Graph inputGraph = new Graph();
    
    public ClickyCanvas(Graph graph) {
		// TODO Auto-generated constructor stub
    	inputGraph = graph;
	}

	@Override
    public void paint(Graphics visual)
    {
    	ArrayList<Integer> func = new ArrayList<Integer>();
		// keep track of coordinates of each of the objects (hash idX and idY)
		HashMap<Integer, Integer> idX = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> idY = new HashMap<Integer, Integer>();
		int numFunctions;
		double section;
		visual.setColor(Color.BLACK);
		
		inputGraph.getGraph();
		
		// add function to return the list of vertex IDs
		for (int i : inputGraph.vertices.keySet())
		{
			if(inputGraph.vertices.get(i).func)
			{
				func.add(1);
			}
			else
			{
				func.add(0);
			}
		}
		numFunctions = 1;
		section = (double)width / numFunctions;
		int x = 0, y = 0, changeX = 1;
		
		
		for(int i : inputGraph.vertices.keySet())
		{
			System.out.println(i);
			// draw the vertex
			if(func.get(i) == 1) //function
			{
				// find starting x (10 from left)
				x = (int)(width - section*(numFunctions-i) + 10);
				// always start functions 50 from top
				y = 50;
				changeX = 1;
				
				// record coordinates
				idX.put(i, x);
				idY.put(i, y);
				
				// draw rectangle for functions
				visual.drawRect(x, y, 150, 20);
			}
			else //object
			{
				// first object of the function moves over,
				//   the rest will be aligned under it
				if(changeX == 1)
				{
					x = x + 250;
					changeX = 0;
				}
				else
				{
					// move each object down from the last one 
					y = y + 30;
				}
				// draw rectangle for functions
				visual.drawOval(x, y, 150, 20);
			}
			// modify this function to only return the edges for a specific vertex 
			//g.getEdges(i);
			//edges.add(27);
			//edges.add(34);
			//visual.drawArrow(idX, idY, vertices.get(i), edges.get(0));
		}
		
		// draw arrow function definition
		//combine a line and a triangle to create an arrow
    	/*int fromX, fromY, toX, toY, left = 0;
    	double angle;
    	// get coordinates of each object
    	fromX = idX.get(from);
    	fromY = idY.get(from);
    	toX = idX.get(to);
    	toY = idY.get(to);
    	// if arrow is pointing left
    	if(fromX > toX)
    	{
    		left = 1;
    	}*/
		
    }

private static int sum(ArrayList<Integer> func) {
	int total = 0;
	for(int i : func)
	{
		total = total + func.get(i);
	}
	return total;
}


}
