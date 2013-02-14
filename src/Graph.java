import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class Graph {

	ArrayList<ArrayList<Integer>> graph = new ArrayList<ArrayList<Integer>>();
	HashMap<String, Integer> id_chart = new HashMap<String, Integer>();
	ArrayList<Edges> edges = new ArrayList<Edges>();
	ArrayList<Vertex> vertexes = new ArrayList<Vertex>();

	public Graph()
	{

	}

	public void updateGraph()
	{
		//working on it	
	}

	public ArrayList<ArrayList<Integer>> getGraph()
	{
		return this.graph;
	}
	
	public HashMap<String, Integer> getID_chart()
	{
		return id_chart;
	}

	/*
	 * Returns the edges a node points to.
	 */
	public ArrayList<Integer> getEdges(int node)
	{
		return graph.get(node); 
	}

	public void addVertex(Vertex v)
	{
		vertexes.add(v);
	}

	public void deleteVertex(int vertexID)
	{
		int counter = 0;
		for(Vertex v : vertexes)
		{
			counter++;
			if(v.id == vertexID)
			{
				vertexes.remove(counter);
				return;
			}
		}

	}



	/*
	 * For testing purposes, fills a graph with random nodes and edges.
	 * Nodes will not point to themselves.
	 * A node can have multiple edges to another node. 
	 */
	public void testFillGraph(int vertex, int edgesPerVertex, int randomDistribution)
	{
		Random r = new Random();

		//Vertexs
		for(int i = 0; i < vertex; i++)
		{
			ArrayList<Integer> array = new ArrayList<Integer>();

			//Edges
			for(int j = 0; j < edgesPerVertex; j++)
			{
				int number = Math.abs(r.nextInt()%randomDistribution);
				while(number == i)
				{
					number = Math.abs(r.nextInt()%randomDistribution); //find another number
				}

				array.add(number);
			}

			graph.add(array);
		}


	}

	/*
	 * Displays the graph on the console
	 */
	public void displayGraph() 
	{

		for(int i = 0; i < graph.size(); i++)
		{
			System.out.print("Node " + i + " points to: ");
			System.out.print(graph.get(i));
			System.out.println();
		}

	}

	public void testFillEdges(Graph g)
	{
		int id = 1; //Start id assignment at 1, 0 is reserved for blank id

		for(int i = 0; i < g.getGraph().size(); i++)
		{


			for(int j = 0; j < g.getGraph().get(i).size(); j++)
			{
				Edges e = new Edges(id, i, g.getEdges(i).get(j));
				edges.add(e);
				id += 1;
			}

		}

	}

	public void testFillVertexes(Graph g)
	{
		int id = 1; //Start id assignment at 1, 0 is reserved for blank id

		for(int i = 0; i < g.getGraph().size(); i++)
		{


			Vertex v = new Vertex(id, "testVertex" + id);
			vertexes.add(v);
			id += 1;


		}

	}

	public void displayEdges()
	{

		for(Edges e : edges)
		{
			e.displayEdge();
		}
	}

	public void displayVertexes() 
	{

		for(Vertex v : vertexes)
		{
			v.displayVertex();
		}

	}





}
