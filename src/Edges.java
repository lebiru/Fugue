
/*
 * Edges hold the names of the variables.
 * Every edge has it's own ID.
 * An edge has a source vertex and a destination vertex.
 */

public class Edges {
	
	int source = 0;
	int destination = 0;
	int id = 0;
	
	
	public Edges(int id, int source, int destination)
	{
		this.source = source;
		this.destination = destination;
		this.id = id;
	}
	
	public Edges()
	{
		
	}

	public void setSource(int source)
	{
		this.source = source;
	}
	
	public void setDestination(int destination)
	{
		this.destination = destination;
	}
	
	
	public void setid(int id)
	{
		this.id = id;
	}
	
	public int getSource()
	{
		return this.source;
	}
	
	public int getDestination()
	{
		return this.destination;
	}
	
	public int getid()
	{
		return this.id;
	}
	
	public void displayEdge()
	{
		System.out.println("Edge: " + this.id + ", Source: " + this.source + ", Destination: " + this.destination);
	}
	
	public void resetEdge()
	{
		this.source = 0;
		this.destination = 0;
		this.id = 0;
		
	}
	
	
	
}
