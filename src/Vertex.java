/*
 * Class Vertex for the graph.
 * Make a vertex for stack frames, objects, and primitive types.
 */
public class Vertex {
	
	int id = 0;
	String name = "";
	Boolean func = false;
	
	public Vertex()
	{
		
	}
	
	public Vertex(int id)
	{
		this.id = id;
	}
	
	public Vertex(int id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public int getID()
	{
		return this.id;
	}
	
	public void displayVertex()
	{
		System.out.println("Vertex: " + this.id + " Name: " + this.name);
	}
	
	public void resetVertex()
	{
		this.id = 0;
		this.name = "";
	}
	
}
