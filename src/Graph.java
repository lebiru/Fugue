import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Graph {

	HashMap<Integer, ArrayList<Integer>> graph = new HashMap<Integer, ArrayList<Integer>>();

	HashMap<Integer, Vertex> vertices = new HashMap<Integer, Vertex>();
	HashMap<Integer, Edge> edges = new HashMap<Integer, Edge>();
	int reinstatedID = 1;


	public Graph()
	{

	}

	/*
	 * Constructor with vertices and edges as parameters. 
	 */
	public Graph(HashMap<Integer, Vertex> vertices, HashMap<Integer, Edge> edges)
	{
		this.vertices = vertices;
		this.edges = edges;
	}

	/*
	 * Updates our graph. Call this to make an updated graph.
	 */
	public void updateGraph(HashMap<Integer, Vertex> vertices, HashMap<Integer, Edge> edges)
	{

		resetID();

		graph = new HashMap<Integer, ArrayList<Integer>>();


		int temp = fixVertexID(vertices, reinstatedID);
		fixEdgeID(edges, reinstatedID, temp);

		for(Vertex v : vertices.values())
		{
			graph.put(v.id, new ArrayList<Integer>());
		}

		for(Edge e : edges.values())
		{
			//System.out.println(graph.get(e.getSource().id) + " " + e.getSource().value);
			ArrayList<Integer> newEdges = graph.get(e.getSource().id);
			e.displayEdge();
			newEdges.add(e.id);
			graph.put((Integer)e.getSource().id, newEdges);
		}



	}

	private void resetID() 
	{
		this.reinstatedID = 1;
	}

	private int fixVertexID(HashMap<Integer, Vertex> vertices, int reinstatedID) 
	{
		
		for (Integer i : vertices.keySet()) 
		{
			i = reinstatedID;
			System.out.println("Key: " + i);
			reinstatedID++;
			
		}
		
		reinstatedID = 1;
		
		for (Vertex v : vertices.values())
		{
			v.id = reinstatedID;
			System.out.println("Value: " + v.id);
			reinstatedID++;
		}
		
		return reinstatedID;
		
	}

	private void fixEdgeID(HashMap<Integer, Edge> edges, int reinstatedID, int temp) 
	{
		int temporary = temp;
		for (Integer i : edges.keySet()) 
		{
			i = temp;
			System.out.println("Key: " + i);
			temp++;
		}
		
		temp = temporary;
		
		for (Edge e : edges.values())
		{
			e.id = temp;
			System.out.println("Value: " + e.id);
			temp++;
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
	public HashMap<Integer, Edge> getEdges(int vertexID)
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
	public void addEdge(Edge e)
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
	public Edge getEdge(int id)
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

	public int getVerticesSize()
	{
		return vertices.size();
	}

	/*
	 * Displays the graph on the console
	 */
	public void displayGraph() 
	{
		System.out.println("HERE IS THE GRAPH");
		for(Entry<Integer, ArrayList<Integer>> g : graph.entrySet())
		{
			int key = g.getKey();
			ArrayList<Integer> value = g.getValue();
			System.out.println("Vertex ID: " + key + " Value: " + vertices.get(key).value + "[" + value.toString() + "]");
		}

	}

	/*
	 * Displays all the edges on the console.
	 */
	public void displayEdges()
	{

		for(Edge e: edges.values())
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
