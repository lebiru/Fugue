
/*
 * Edges hold the names of the variables.
 * Every edge has it's own ID.
 * An edge has a source vertex and a destination vertex.
 */

public class Edges {
	
	Vertex source = new Vertex();
	Vertex destination = new Vertex();
	int id = 0;
	
	
	public Edges(int id, Vertex source, Vertex destination)
	{
		this.source = source;
		this.destination = destination;
		this.id = id;
	}
	
	public Edges()
	{
		
	}

	public void setSource(Vertex source)
	{
		this.source = source;
	}
	
	public void setDestination(Vertex destination)
	{
		this.destination = destination;
	}
	
	
	public void setid(int id)
	{
		this.id = id;
	}
	
	public Vertex getSource()
	{
		return this.source;
	}
	
	public Vertex getDestination()
	{
		return this.destination;
	}
	
	public int getID()
	{
		return this.id;
	}
	
	public void displayEdge()
	{
		System.out.println("Edge: " + this.id + ", Source: " + this.source.name + ", Destination: " + this.destination.name);
	}
	
	public void resetEdge()
	{
		this.source = new Vertex();
		this.destination = new Vertex();
		this.id = 0;
		
	}
	
	
	
}
