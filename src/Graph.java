import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class Graph {

	HashMap<Integer, HashMap<Integer, Integer>> graph = new HashMap<Integer, HashMap<Integer, Integer>>();
	
	HashMap<Integer, Vertex> vertexes = new HashMap<Integer, Vertex>();
	HashMap<Integer, Edges> edges = new HashMap<Integer, Edges>();

	public Graph()
	{

	}

	public void updateGraph()
	{
		//working on it	
	}

	public HashMap<Integer, HashMap<Integer, Integer>> getGraph()
	{
		return this.graph;
	}

	/*
	 * Returns the edges a node points to.
	 */
	public HashMap<Integer, Edges> getEdges(int vertexID)
	{
		return edges; 
	}

	public void addVertex(Vertex v)
	{
		//vertexes.add(v);
	}

	public void deleteVertex(int vertexID)
	{
//		int counter = 0;
//		for(Vertex v : vertexes)
//		{
//			counter++;
//			if(v.id == vertexID)
//			{
//				vertexes.remove(counter);
//				return;
//			}
//		}

	}



	/*
	 * For testing purposes, fills a graph with random vertexes and edges.
	 * Vertexes will not point to themselves.
	 * A node can have multiple edges to another node. 
	 */

	
	public void staticTestFillGraph()
	{
		
		ArrayList<Integer> source = new ArrayList<Integer>();
		ArrayList<Integer> destination = new ArrayList<Integer>(3);

		


	}

	/*
	 * Displays the graph on the console
	 */
	public void displayGraph() 
	{

		for(int i = 0; i < graph.size(); i++)
		{
			System.out.print("Vertex " + i + " points to: ");
			System.out.print(graph.get(i));
			System.out.println();
		}

	}

	public void displayEdges()
	{

//		for(Edges e : edges)
//		{
//			e.displayEdge();
//		}
	}

	public void displayVertexes() 
	{

//		for(Vertex v : vertexes)
//		{
//			v.displayVertex();
//		}

	}





}
