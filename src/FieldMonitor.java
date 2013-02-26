import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import com.sun.jdi.*;
import com.sun.tools.corba.se.idl.constExpr.Positive;

public class FieldMonitor {
	static int counter = 101;

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
												
						Vertex Main = new Vertex(100, "Main", true);
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
					Vertex current = new Vertex(key, "Node", false);
					g.addVertex(current);
					makeConnection(g, key, prev, current, name);
					Search((ObjectReference) fieldValue, haveyouseen, g, current);
				}
				else 
				{
					Vertex current = new Vertex(key,"Node",false);
					makeConnection(g, key, prev, current, name);
				}
			}
			
			else if (fieldValue instanceof PrimitiveValue)
			{
				System.out.println(fieldValue.toString());
				Vertex current = new Vertex(counter,fieldValue.toString(),false);
				makeConnection(g,counter,prev,current, fields.get(i).name());
				counter++;
				//System.out.println(counter);
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