import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;


public class Graph {

	HashMap<Integer, ArrayList<Integer>> graph = new HashMap<Integer, ArrayList<Integer>>();
	
	HashMap<Integer, Vertex> vertexes = new HashMap<Integer, Vertex>();
	HashMap<Integer, Edges> edges = new HashMap<Integer, Edges>();

	public Graph()
	{

	}

	public void updateGraph()
	{
		//working on it	
	}

	public HashMap<Integer, ArrayList<Integer>> getGraph()
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
		
		ArrayList<Integer> one = new ArrayList<Integer>();
		ArrayList<Integer> two = new ArrayList<Integer>();
		ArrayList<Integer> three = new ArrayList<Integer>();
		ArrayList<Integer> four = new ArrayList<Integer>();
		
		one.add(2);
		one.add(1);
		
		two.add(3);
		
		three.add(4);
		
		four.add(3);
		four.add(2);
		
		graph.put(1, one);
		graph.put(2, two);
		graph.put(3, three);
		graph.put(4, four);
		
		
		
		
//		int[] sourceArray =      {3,4,8,2,5,7,4,8,3,1};
//		int[] destinationArray = {9,1,2,4,7,8,3,6,8,5};
//		
//		for(int i : sourceArray)
//		{
//			if(!graph.containsKey(sourceArray[i]))
//			{
//				graph.put(sourceArray[i], new HashMap<Integer, Integer>());
//			}
//			
//			graph.get(sourceArray[i]).put(sourceArray[i], destinationArray[i]);
//			
//			if(!graph.containsKey(destinationArray[i]))
//			{
//				graph.put(sourceArray[i], new HashMap<Integer, Integer>());
//			}
//			
//		}
//		
//		HashMap<Integer, Integer>
		
		
		
//		ArrayList<Integer> source = new ArrayList<Integer>();
//		ArrayList<Integer> destination = new ArrayList<Integer>();
//		
//		//Putting sourceArray -> source & destinationArray -> destination
//		for(int i : sourceArray)
//		{
//			source.add(sourceArray[i]);
//			destination.add(destinationArray[i]);
//		}
//		
//		
//		//Adding distinct sources to HashMap sources
//		for(Integer i : source)
//		{
//			if(!vertexes.containsKey(i))
//			{
//				vertexes.put(source.get(i), new Vertex(source.get(i)));
//			}
//			
//		}
//		
//		for(Integer i : destination)
//		{
//			
//			if(!vertexes.containsKey(i))
//			{
//				vertexes.put(destination.get(i), new Vertex(destination.get(i)));
//			}
//			
//		}
		
		
	}

	/*
	 * Displays the graph on the console
	 */
	public void displayGraph() 
	{

		for(Entry<Integer, ArrayList<Integer>> g : graph.entrySet())
		{
			int key = g.getKey();
			ArrayList<Integer> value = g.getValue();
			System.out.println("Vertex: " + key + ": [" + value.toString() + "]");
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
