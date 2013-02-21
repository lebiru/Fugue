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
    	ArrayList<Integer> coordinates = new ArrayList<Integer>();
    	ArrayList<Integer> triCoordinates = new ArrayList<Integer>();
    	int[] triX = new int[3], triY = new int[3];
    	// keep track of coordinates of each of the objects (hash idX and idY)
		HashMap<Integer, Integer> idX = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> idY = new HashMap<Integer, Integer>();
		int numFunctions, from, to, fromX, fromY, toX, toY, temp;
		int midX, midY, nameLength, self;
		double section;
		visual.setColor(Color.BLACK);
		
		inputGraph.getGraph();
		
		// add function to return the list of vertex IDs
		for (int i : inputGraph.vertices.keySet())
		{
			if(inputGraph.vertices.get(i).isAFunction)
			{
				func.add(1);
			}
			else
			{
				func.add(0);
			}
		}
		numFunctions = sum(func);
		section = (double)width / numFunctions;
		int x = 0, y = 0, changeX = 1, count = 0;
		
		
		for(int i : inputGraph.vertices.keySet())
		{
			// draw the vertex
			if(inputGraph.vertices.get(i).isAFunction) //function
			{
				// find starting x (10 from left)
				x = (int)(width - section*(numFunctions) + 10);
				// always start functions 50 from top
				y = 50;
				changeX = 1;
				count = 0;
				
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
					count = 1;
				}
				else
				{
					// move each object down from the last one
					y = y + 50;
					if(count%2 == 0)
					{
						x = x + 20;
					}
					else
					{
						x = x - 20;
					}
					count = count + 1;
				}
				// draw rectangle for functions
				visual.drawRoundRect(x, y, 150, 20, 20, 20);
			}
			// record coordinates
			idX.put(i, x);
			idY.put(i, y);
			
			// display value
			visual.drawString(inputGraph.vertices.get(i).value, x+5, y+15);
			// modify this function to only return the edges for a specific vertex 
			
		}
		
		visual.setColor(Color.RED);
		for(int i : inputGraph.edges.keySet())
		{
			// draw the arrow for the edge
			from = inputGraph.edges.get(i).source.id;
			fromX = idX.get(from);
			fromY = idY.get(from);
			to = inputGraph.edges.get(i).destination.id;
			toX = idX.get(to);
			toY = idY.get(to);
			
			if(to != from)
			{
				// find coordinates to draw line
				coordinates = findConnectingPoints(fromX, fromY, toX, toY);
				fromX = coordinates.get(0);
				fromY = coordinates.get(1);
				toX = coordinates.get(2);
				toY = coordinates.get(3);
				visual.drawLine(fromX, fromY, toX, toY);
				// find coordinates to draw arrow
				triCoordinates = findTriCordinates(coordinates);
				triX[0] = triCoordinates.get(0);
				triX[1] = triCoordinates.get(1);
				triX[2] = triCoordinates.get(2);
				triY[0] = triCoordinates.get(3);
				triY[1] = triCoordinates.get(4);
				triY[2] = triCoordinates.get(5);
				visual.fillPolygon(triX, triY, 3);
				// display the name of the edge
				midX = (fromX + toX) / 2;
				midY = (fromY + toY) / 2;
				nameLength = inputGraph.edges.get(i).name.length();
				visual.drawString(inputGraph.edges.get(i).name, midX - (nameLength*5/2), midY);
			}
			else
			{
				visual.drawArc(fromX+50, fromY+10, 50, 20, 180, 180);
				triX[0] = fromX + 100;
				triX[1] = (int)(triX[0] - 10.0 * Math.cos(Math.toRadians(330)));
				triX[2] = (int)(triX[0] - 10.0 * Math.cos(Math.toRadians(280)));
				triY[0] = toY + 20;
				triY[1] = (int)(triY[0] - 10.0 * Math.sin(Math.toRadians(330)));
				triY[2] = (int)(triY[0] - 10.0 * Math.sin(Math.toRadians(280)));
				visual.fillPolygon(triX, triY, 3);
				// display the name of the edge
				midX = (fromX + 75);
				midY = (fromY + 40);
				nameLength = inputGraph.edges.get(i).name.length();
				visual.drawString(inputGraph.edges.get(i).name, midX - (nameLength*5/2), midY);
			}
		}
    }

	private ArrayList<Integer> findTriCordinates(ArrayList<Integer> coor) {
		// TODO Auto-generated method stub
		ArrayList<Integer> triCoor = new ArrayList<Integer>();
		double angle = 0, deltaX, deltaY, sideLength = 10.0;
		double remAngle, tempX, tempY;
		// get the point touching the "to" vertex
		triCoor.add(coor.get(2));
		triCoor.add(0);
		triCoor.add(0);
		triCoor.add(coor.get(3));
		triCoor.add(0);
		triCoor.add(0);
		// get the angle to create the triangle at
		if(coor.get(0) == coor.get(2))
		{
			if(coor.get(1) < coor.get(3))
			{
				angle = 270.0;
			}
			else
			{
				angle = 90.0;
			}
		}
		else
		{
			deltaX = (double)(coor.get(2) - coor.get(0));
			deltaY = (double)(coor.get(3) - coor.get(1));
			angle = Math.atan(deltaY/deltaX);
			angle = Math.toDegrees(angle);
			if (deltaX < 0)
			{
				angle = angle + 180;
			}
		}
		
		// use the point and angle to create triangle
		remAngle = angle + 30.0;
		tempX = triCoor.get(0) - sideLength * Math.cos(Math.toRadians(remAngle));
		tempY = triCoor.get(3) - sideLength * Math.sin(Math.toRadians(remAngle));
		triCoor.set(1, (int)tempX);
		triCoor.set(4, (int)tempY);
		remAngle = 180 - 30 - (180-angle);
		tempX = triCoor.get(0) - sideLength * Math.cos(Math.toRadians(remAngle));
		tempY = triCoor.get(3) - sideLength * Math.sin(Math.toRadians(remAngle));
		triCoor.set(2, (int)tempX);
		triCoor.set(5, (int)tempY);
		return triCoor;
	}

	private ArrayList<Integer> findConnectingPoints(int fromX, int fromY, int toX, int toY) {
		// TODO Auto-generated method stub
		ArrayList<Integer> coor = new ArrayList<Integer>();
		coor.add(0);
		coor.add(0);
		coor.add(0);
		coor.add(0);
		
		if(Math.abs(fromX - toX) < 50)
		{
			coor.set(0, fromX + 75);
			coor.set(2, toX + 75);
			if(fromY < toY)
			{
				coor.set(1, fromY + 20);
				coor.set(3, toY);
			}
			else
			{
				coor.set(1, fromY);
				coor.set(3, toY + 20);
			}
		}
		else if(fromY == toY)
		{
			coor.set(1, fromY + 10);
			coor.set(3, toY + 10);
			if(fromX < toX)
			{
				coor.set(0,fromX + 150);
				coor.set(2, toX);
			}
			else
			{
				coor.set(0,fromX);
				coor.set(2, toX + 150);
			}
		}
		return coor;
	}

private static int sum(ArrayList<Integer> func) {
	int total = 0;
	for(int i : func)
	{
		total = total + i;
	}
	return total;
}


}
