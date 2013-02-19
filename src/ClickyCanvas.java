import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;


@SuppressWarnings("serial")
public class ClickyCanvas extends Canvas {
	/**
	 * 
	 */
	
	//static ClickyCanvas c = new ClickyCanvas();
	
	//static Graphics visual;
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	static int width = (int)screenSize.getWidth() - 10;
	static int height = (int)screenSize.getHeight() - 80;
		
		//visual = c.getGraphics();
		//visual.setColor(Color.BLACK);
		//c.paint(visual);
	
    
    @Override
    public void paint(Graphics visual)
    {
    	ArrayList<Integer> vertices = new ArrayList<Integer>();
		ArrayList<Integer> edges = new ArrayList<Integer>();
		ArrayList<Integer> func = new ArrayList<Integer>(); // 0 if object, 1 if function
		// keep track of coordinates of each of the objects (hash idX and idY)
		HashMap<Integer, Integer> idX = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> idY = new HashMap<Integer, Integer>();
		int numFunctions;
		double section;
		visual.setColor(Color.BLACK);
		
		// add function to return the list of vertex IDs
		vertices.add(0);
		vertices.add(1);
		vertices.add(2);
		
		func.add(1);
		func.add(0);
		func.add(0);
		numFunctions = sum(func);
		section = (double)width / numFunctions;
		int x = 0, y = 0, changeX = 1;
		
		
		for(int i : vertices)
		{
			// draw the vertex
			if(func.get(i) == 1) //function
			{
				// find starting x (10 from left)
				x = (int)(width - section*(numFunctions-i) + 10);
				// always start functions 50 from top
				y = 50;
				changeX = 1;
				
				// record coordinates
				idX.put(vertices.get(i), x);
				idY.put(vertices.get(i), y);
				
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
			edges.add(27);
			edges.add(34);
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
