import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;


public class Graph {

	HashMap<Integer, ArrayList<Integer>> graph = new HashMap<Integer, ArrayList<Integer>>();
	
	HashMap<Integer, Vertex> vertices = new HashMap<Integer, Vertex>();
	HashMap<Integer, Edges> edges = new HashMap<Integer, Edges>();

	public Graph()
	{

	}
	
	/*
	 * Constructor with vertices and edges as parameters. 
	 */
	public Graph(HashMap<Integer, Vertex> vertices, HashMap<Integer, Edges> edges)
	{
		this.vertices = vertices;
		this.edges = edges;
	}
	
	/*
	 * Updates our graph. Call this to make an updated graph.
	 */
	public void updateGraph(HashMap<Integer, Vertex> vertices, HashMap<Integer, Edges> edges)
	{

		graph = new HashMap<Integer, ArrayList<Integer>>();
		
		for(Vertex v : vertices.values())
		{
			graph.put(v.id, new ArrayList<Integer>());
		}
		
		for(Edges e : edges.values())
		{
			ArrayList<Integer> newEdges = graph.get(e.getSource().id);
			newEdges.add(e.id);
			graph.put((Integer)e.getSource().id, newEdges);
		}
		
	}

	/*
	 * Returns the graph.
	 */
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

	/*
	 * Adds a vertex to the vertices HashMap
	 */
	public void addVertex(Vertex v)
	{
		vertices.put(v.id, v);
	}
	
	/*
	 * Adds an edge to the edges HashMap
	 */
	public void addEdge(Edges e)
	{
		edges.put(e.id, e);
	}
	
	/*
	 * Input: Vertex ID
	 * Output: The specified Vertex
	 */
	public Vertex getVertex(int id)
	{
		return vertices.get(id);
	}
	
	/*
	 * Input: Edge ID
	 * Output: The specified edge
	 */
	public Edges getEdge(int id)
	{
		return edges.get(id);
	}
	
	public String getVertexValue(int id)
	{
		return vertices.get(id).value;
	}
	
	public String getEdgeName(int id)
	{
		return edges.get(id).name;
	}

	/*DEPRECATED
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
		
	}
	
	
	/*
	 * This method tests dynamically filling a graph.
	 * It is a model for the data gathering part. 
	 * Make sure you give an ID number when adding new vertices/edges.
	 */
	public void dynamicTestFillGraph()
	{
		
		Vertex one = new Vertex(1, "Main", true);
		Vertex two = new Vertex(2, "This is the test string", false);
		Vertex three = new Vertex(3, "2.22", false);
		Vertex four = new Vertex(4, "b", false);
		
		Edges a = new Edges(5, one, one, "number333");
		Edges b = new Edges(6, one, two, "testString");
		Edges c = new Edges(7, two, three, "doubleVariable");
		Edges d = new Edges(8, three, four, "characterVariable");
		Edges e = new Edges(9, four, three, "doubleVariable");
		Edges f = new Edges(10, four, two, "testString");
		
		
		addVertex(one);
		addVertex(two);
		addVertex(three);
		addVertex(four);
		
		addEdge(a);
		addEdge(b);
		addEdge(c);
		addEdge(d);
		addEdge(e);
		addEdge(f);
		
		updateGraph(vertices, edges);
		
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

	/*
	 * Displays all the edges on the console.
	 */
	public void displayEdges()
	{

		for(Edges e: edges.values())
		{
			e.displayEdge();
		}
	}

	/*
	 * Displays all the vertexes on the console.
	 */
	public void displayVertexes() 
	{

		for(Vertex v : vertices.values())
		{
			v.displayVertex();
		}

	}

}
