import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import com.sun.jdi.*;
import com.sun.tools.corba.se.idl.constExpr.Positive;

public class FieldMonitor {
	static int counter = 100;

	public static void monitorSys(int dataport, Graph g) throws IOException,
			InterruptedException, ClassNotLoadedException 
	{
		HashMap<Integer, Integer> haveyouseen = new HashMap<Integer, Integer>();
		VirtualMachine vm = new VMAcquirer().connect(dataport);
		vm.suspend();
		List<ThreadReference> threadref = vm.allThreads();
		int called = 0;
		try {
			List<StackFrame> stack;
			stack = threadref.get(3).frames();
			int framecount = threadref.get(3).frameCount();
			for (int j = 1; j < framecount; j++) {
				List<LocalVariable> localvariables = stack.get(j)
						.visibleVariables();
				for (int k = 0; k < localvariables.size(); k++) {
					if (stack.get(j).getValue(localvariables.get(k)) instanceof ObjectReference) {
						Value information = stack.get(j).getValue(
								localvariables.get(k));
												
						Vertex Main = new Vertex(0, "Main", true);
						g.addVertex(Main);
						Search((ObjectReference) information, haveyouseen, g,
								Main);
					}
				}
			}
		} catch (IncompatibleThreadStateException e) {
			e.printStackTrace();
		} catch (AbsentInformationException e) {
			e.printStackTrace();
		}

		g.updateGraph(g.vertices, g.edges);
	}

<<<<<<< HEAD
	private static void Search(ObjectReference or, HashMap<String, Integer> haveyouseen, Graph g, Vertex prev)
			throws InterruptedException, ClassNotLoadedException {
		Stack<ObjectReference> stack = new Stack<ObjectReference>();
		ObjectReference popped;
		stack.push(or);
		Vertex temp = prev; 
		while (!stack.isEmpty()) {
			popped = stack.pop();
			List<Field> fields = popped.referenceType().allFields();
			for (int i = 0; i < fields.size(); i++) {

				String name = fields.get(i).name();
				String key = name + popped.uniqueID();
				Value fieldValue = popped.getValue(fields.get(i));
				if (haveyouseen.containsKey(key) != true) {
					haveyouseen.put(key, 1);

					if ((fieldValue instanceof ObjectReference)) {
						// Type type = fields.get(i).type();
						testInformation(fieldValue, name, popped.uniqueID(),
								fields.get(i).signature(), true);

						Vertex current;
						if (fieldValue.equals(null)) {
							current = new Vertex((int) popped.uniqueID(),
									"EMPTY".toString(), true);
							g.addVertex(current);
						} else {
							current = new Vertex((int) popped.uniqueID(), name,
									true);
							g.addVertex(current);
						}
						edgeCreation(g, prev, current, (int) popped.uniqueID(),
								name, true);
						// Search((ObjectReference) fieldValue, haveyouseen, g,
						// current);
						System.out.println("Previous --> Current: " + prev.value + " "+ prev.id
								+ " --> " + current.value + " "+current.id);
						prev = current;
						stack.push((ObjectReference) fieldValue);

					
					} else {
						testInformation(fieldValue, name, popped.uniqueID(),
								fields.get(i).signature(), false);
						Vertex current;
						if (fieldValue != null) {
							current = new Vertex((int) popped.uniqueID(),
									fieldValue.toString(), false);
						} else {
							current = new Vertex((int) popped.uniqueID(),
									"itsempty", false);
						}
						g.addVertex(current);
						edgeCreation(g, prev, current, (int) popped.uniqueID(),
								name, false);
						System.out.println("Previous --> Current: " + prev.value + " "+ prev.id
								+ " --> " + name + " "+current.id);
				
=======
	private static void Search(ObjectReference or, HashMap<Integer, Integer> haveyouseen, Graph g, Vertex prev)
			throws InterruptedException, ClassNotLoadedException 
	{
		List<Field> fields = or.referenceType().allFields();
		for (int i = 0; i < fields.size(); i++) 
		{
			Value fieldValue = or.getValue(fields.get(i));
			
			if(fieldValue instanceof ObjectReference) 
			{
				String name = fields.get(i).name();
				int key = (int)((ObjectReference) fieldValue).uniqueID();
				if(haveyouseen.containsKey(key)==false)
				{
					haveyouseen.put(key,1);
					//Special Case for the Integer Class
					/*if(fields.get(i).toString().contains("java.lang.Integer")){
						Search((ObjectReference) fieldValue, haveyouseen, g, prev);
					}
					else*/
					{
					Vertex current = new Vertex(key, "Node", false);
					g.addVertex(current);
					makeConnection(g, key, prev, current, name);
					Search((ObjectReference) fieldValue, haveyouseen, g, current);
					//System.out.println(fieldValue + "  "+fields.get(i) + "   ");
	
>>>>>>> 3d3de30bd996fb9c0df9288f4005fdabb9ad9d57
					}
				}
				else 
				{
					Vertex current = new Vertex(key,"Node",false);
					makeConnection(g, key, prev, current, name);
		
				}
			}
			
			else if (fieldValue instanceof PrimitiveValue)
			{
				Vertex current = new Vertex(counter,fieldValue.toString(),false);
				makeConnection(g,counter,prev,current, fields.get(i).name());
				counter++;
				
			
				//System.out.println(fieldValue + "  "+fields.get(i) + "   ");
			}
			
			else 
			{
				System.out.println("Null Value");
			}
			
		}
	}

	

	private static void makeConnection(Graph g, int key, Vertex prev, Vertex current,
			String string) {
		g.addEdge(new Edge(key, prev, current, string));
	}

	public static void testInformation(Value fieldValue, String name, long ID,
			String signature, boolean type) {
		if (type == true)
			System.out.println("OBJECT  Value: " + fieldValue + " String: "
					+ name + " ID: " + ID + " Signature: " + signature);
		else
			System.out.println("PRIMATIVE  Value: " + fieldValue + " String: "
					+ name + " ID: " + ID + " Signature: " + signature);

	}
}
