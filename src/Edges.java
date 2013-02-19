
/*
 * Edges hold the names of the variables.
 * Every edge has it's own ID.
 * An edge has a source vertex and a destination vertex.
 */

public class Edges {
	
	Vertex source = new Vertex();
	Vertex destination = new Vertex();
	int id = 0;
	String name = "";
	
	
	public Edges(int id, Vertex source, Vertex destination, String name)
	{
		this.source = source;
		this.destination = destination;
		this.id = id;
		this.name = name;
	}
	
	public Edges(int id)
	{
		this.id = id;
	}
	
	public Edges()
	{
		
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
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
		System.out.println("Edge: " + this.id + ", Source: " + this.source.value + ", Destination: " + this.destination.value);
	}
	
	public void resetEdge()
	{
		this.source = new Vertex();
		this.destination = new Vertex();
		this.id = 0;
		
	}
	
	
	
}
