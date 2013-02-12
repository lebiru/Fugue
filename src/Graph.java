import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class Graph {

	ArrayList<ArrayList<Integer>> graph = new ArrayList<ArrayList<Integer>>();
	HashMap<String, Integer> id_chart = new HashMap<String, Integer>();
	ArrayList<Edges> edges = new ArrayList<Edges>();
	
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
	
	/*
	 * Returns the edges a node points to.
	 */
	public ArrayList<Integer> getEdges(int node)
	{
		return graph.get(node); 
	}
	
	public void setNode(int node)
	{
		//working on it
	}
	
	public void deleteNode(int node)
	{
		//working on it
	}
	
	/*
	 * For testing purposes, fills a graph with random nodes and edges.
	 * Nodes will not point to themselves.
	 * A node can have multiple edges to another node. 
	 */
	public void testFillGraph(int nodes, int edgesPerNodes, int randomDistribution)
	{
		Random r = new Random();
		
		//Nodes
		for(int i = 0; i < nodes; i++)
		{
			ArrayList<Integer> array = new ArrayList<Integer>();
			
			//Edges
			for(int j = 0; j < edgesPerNodes; j++)
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
	
	public void displayEdges(Graph g)
	{
		
		for(Edges e : edges)
		{
			e.displayEdge();
		}
	}
	
	
	
	
	
}
