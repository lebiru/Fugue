import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import com.sun.jdi.*;
import com.sun.tools.corba.se.idl.constExpr.Positive;

public class FieldMonitor {
	static int counter = 1000;
	static int maxValue=0;
	public static void monitorSys(int dataport, Graph g) throws IOException,
			InterruptedException, ClassNotLoadedException 
	{
		HashMap<Integer, Integer> haveyouseen = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> hashId = new HashMap<Integer, Integer>();
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
						idValueSearch((ObjectReference) information, hashId);
					}
				}
			}
			
			System.out.println("MAX ID VALUE: " +maxValue);
			maxValue++;
			{
			for (int j = 1; j < framecount; j++) {
				List<LocalVariable> localvariables = stack.get(j)
						.visibleVariables();
				for (int k = 0; k < localvariables.size(); k++) {
					if (stack.get(j).getValue(localvariables.get(k)) instanceof ObjectReference) {
						Value information = stack.get(j).getValue(
								localvariables.get(k));
												
						Vertex Main = new Vertex(1, "Main", true);
						g.addVertex(Main);
						Search((ObjectReference) information, haveyouseen, g,
								Main);
					}
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

	private static void idValueSearch(ObjectReference or, HashMap<Integer, Integer> haveyouseen)
			throws InterruptedException, ClassNotLoadedException 
	{
		List<Field> fields = or.referenceType().allFields();
		for (int i = 0; i < fields.size(); i++) 
		{
			Value fieldValue = or.getValue(fields.get(i));
			
			if(fieldValue instanceof ObjectReference) 
			{
				int key = (int)((ObjectReference) fieldValue).uniqueID();
				if(haveyouseen.containsKey(key)==false)
				{
					haveyouseen.put(key,1);
					maxValue = Math.max(maxValue, key);
					idValueSearch((ObjectReference) fieldValue, haveyouseen);
				}
			}
		}
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
					//Special Case for the Integer Class (not going to run until regular ints runs first
					/*if(fields.get(i).toString().contains("java.lang.Integer")){
						Search((ObjectReference) fieldValue, haveyouseen, g, prev);
					}
					else
					{*/
					Vertex current = new Vertex(key, fields.get(i).typeName(), false);
					g.addVertex(current);
					counter++;
					makeConnection(g, counter, prev, current, name);
					Search((ObjectReference) fieldValue, haveyouseen, g, current);
					//System.out.println(fieldValue + "  "+fields.get(i) + "   ");
					System.out.println(key);
					//}
				}
				else 
				{
					counter++;
					Vertex current = new Vertex(key,name,false);
					makeConnection(g, counter, prev, current, name);
		
				}
			}
			
			else if (fieldValue instanceof PrimitiveValue)
			{
				counter++;
				Vertex current = new Vertex(maxValue,fieldValue.toString(),false);
				g.addVertex(current);
				makeConnection(g,counter,prev,current, fields.get(i).name());
				maxValue++;
			
				//System.out.println(fieldValue + "  "+fields.get(i) + "   ");
			}
			
			//The where we find a null value, we can ignore it
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
