import java.util.ArrayList;
import java.util.Random;


public class Graph {

	ArrayList<ArrayList<Integer>> graph = new ArrayList<ArrayList<Integer>>();
	
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
	public void textDisplay() {
		
		for(int i = 0; i < graph.size(); i++)
		{
			System.out.print("Node " + i + " points to: ");
			System.out.print(graph.get(i));
			System.out.println();
		}
		
		
		
	}
	
	
	
}
