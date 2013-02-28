import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.text.html.HTMLDocument.Iterator;


@SuppressWarnings("serial")
public class ClickyCanvas extends Canvas {
	// Get the size of the screen to create a full screen window
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	static int width = (int)screenSize.getWidth() - 10;
	static int height = (int)screenSize.getHeight() - 80;

	// Create a graph to be filled with the current graph later
	static Graph inputGraph = new Graph();

	public ClickyCanvas(Graph graph) {
		// Fill the graph with the current graph
		inputGraph = graph;
	}

	// Override the paint function to display the graph
	@Override
	public void paint(Graphics g)
	{
		Graphics2D visual = (Graphics2D)g;
		// Array for coordinates of the line for the arrow
		ArrayList<Integer> coordinates = new ArrayList<Integer>();
		// Array for coordinates of the triangle for the arrow
		ArrayList<Integer> triCoordinates = new ArrayList<Integer>();
		// Int arrays for the x and y coordinates of triangle (input to fillPoly function)
		int[] triX = new int[3], triY = new int[3];
		// Int arrays for the x and y coordinates of the line (input to polyLine function)
		int[] edgeX = new int[3], edgeY = new int[3];
		// Keep track of coordinates of each of the objects (hash idX and idY)
		HashMap<Integer, Integer> idX = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> idY = new HashMap<Integer, Integer>();
		int numFunctions = 0, from, to, fromX, fromY, toX, toY;
		int midX, midY, nameLength;
		double section;
		visual.setColor(Color.BLACK);

		inputGraph.getGraph();

		// Count how many functions there are in the graph
		for (int i : inputGraph.vertices.keySet())
		{
			if(inputGraph.vertices.get(i).isAFunction)
			{
				numFunctions++;
			}
		}
		// Divide the screen up for each function
		section = (double)width / numFunctions;
		int x = 0, y = 0, changeX = 0, count = 0, countF = 0;

		// Display the vertices
		for(int i : inputGraph.vertices.keySet())
		{
			// If off the screen, start the next column
			if(y > (height - 250))
			{
				y = 50;
				x = x + 300;
			}
			// If too far to the right, start back at the beginning
			if(x > (width - 200))
			{
				y = 100;
				x = 30;
			}
			// Draw the vertex
			if(inputGraph.vertices.get(i).isAFunction) //function
			{
				// find starting x (10 from left)
				x = (int)(width - section*(numFunctions) + 10);
				// always start functions 50 from top
				y = 50;
				changeX = 1;
				countF = countF + 1; //function count
				count = 0;

				// draw rectangle for functions
				visual.drawRect(x, y, 150, 20);
			}
			else //object
			{
				// first object of the function moves over,
				//   the rest will be aligned under it
				if(countF == 0 && count == 0)
				{
					y = 50;
					x = 30;
					count++;
				}
				if(changeX == 1) // If it is the first object
				{
					x = x + 250;
					changeX = 0;
					count = 1;
				}
				else
				{
					// move each object down from the last one
					y = y + 100;
					// offset the next object so its not in a straight line
					if(count%2 == 0)
					{
						x = x + 20;
					}
					else
					{
						x = x - 20;
					}
					count++;
				}
				// draw rounded rectangle for objects
				visual.drawRoundRect(x, y, 150, 20, 20, 20);
			}
			// record coordinates
			idX.put(i, x);
			idY.put(i, y);

			// display value
			visual.drawString(inputGraph.vertices.get(i).value, x+5, y+15);
		}
		
		// Set the color to red to display the edges
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

			// As long as it is not looped to itself
			if(to != from)
			{
				// find coordinates to draw line
				coordinates = findConnectingPoints(fromX, fromY, toX, toY);
				edgeX[0] = coordinates.get(0);
				edgeX[1] = coordinates.get(1);
				edgeX[2] = coordinates.get(2);
				edgeY[0] = coordinates.get(3);
				edgeY[1] = coordinates.get(4);
				edgeY[2] = coordinates.get(5);
				visual.drawPolyline(edgeX, edgeY, 3);
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
				midX = edgeX[1];
				midY = edgeY[1] + 10;
				nameLength = inputGraph.edges.get(i).name.length();
				visual.drawString(inputGraph.edges.get(i).name, midX - (nameLength*5/2), midY);
			}
			else // If it is looped to itself
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
	
	private ArrayList<Integer> findTriCordinates(ArrayList<Integer> coorOutput) {
		// Function to calculate the 3 points to create the triangle for the arrow
		ArrayList<Integer> triCoor = new ArrayList<Integer>();
		ArrayList<Integer> coor = new ArrayList<Integer>();
		// Get the second part of the line to calculate the angle to make the triangle
		coor.add(coorOutput.get(1));
		coor.add(coorOutput.get(4));
		coor.add(coorOutput.get(2));
		coor.add(coorOutput.get(5));
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
			// Calculate the angle
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
		// use math to find the other 2 points of the triangle
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
		// Function to calculate the 3 points of the angled line for the arrow
		ArrayList<Integer> coor = new ArrayList<Integer>();
		double angle, deltaX, deltaY;
		double offset = 30.0;
		coor.add(0);
		coor.add(0);
		coor.add(0);
		coor.add(0);
		coor.add(0);
		coor.add(0);

		// If they are in the same column, connect the center x-wise
		if(Math.abs(fromX - toX) < 50)
		{
			coor.set(0, fromX + 75);
			coor.set(2, toX + 75);
			if(fromY < toY)
			{
				coor.set(3, fromY + 20);
				coor.set(5, toY);
			}
			else
			{
				coor.set(3, fromY);
				coor.set(5, toY + 20);
			}
		}
		else // connect the center y-wise
		{
			coor.set(3, fromY + 10);
			coor.set(5, toY + 10);
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
		
		// calculate the angle
		deltaX = (double)(coor.get(2) - coor.get(0));
		deltaY = (double)(coor.get(5) - coor.get(3));
		angle = Math.atan(deltaY/deltaX);
		angle = Math.toDegrees(angle);
		if (deltaX < 0)
		{
			angle = angle + 180;
		}
		// calculate the offset center point
		int tempX = (coor.get(0) + coor.get(2)) / 2;
		int tempY = (coor.get(3) + coor.get(5)) / 2;
		tempX = (int) (tempX - offset*Math.cos(180 - (angle + 90)));
		tempY = (int) (tempY + offset*Math.sin(180 - (angle + 90)));
		coor.set(1, tempX);
		coor.set(4, tempY);
		
		return coor;
	}

}
