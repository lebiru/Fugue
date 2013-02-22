import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import com.sun.jdi.*;

public class FieldMonitor {
	static int counter = 0;

	public static void monitorSys(int dataport, Graph g) throws IOException,
			InterruptedException, ClassNotLoadedException {
		HashMap<String, Integer> haveyouseen = new HashMap<String, Integer>();

		VirtualMachine vm = new VMAcquirer().connect(dataport);
		vm.suspend();
		List<ThreadReference> threadref = vm.allThreads();

		try {
			List<StackFrame> stack;
			stack = threadref.get(3).frames();
			int framecount = threadref.get(3).frameCount();
			for (int j = 1; j < framecount; j++) {
				List<LocalVariable> localvariables = stack.get(j)
						.visibleVariables();
				for (int k = 0; k < localvariables.size()-1; k++) {
					if (stack.get(j).getValue(localvariables.get(k)) instanceof ObjectReference) {
						Value information = stack.get(j).getValue(
								localvariables.get(k));
						Search((ObjectReference) information, haveyouseen, g,
								null);
						System.out.println("EXECUTING \n\n\n\n");

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

	private static void Search(ObjectReference or,
			HashMap<String, Integer> haveyouseen, Graph g, Vertex prev)
			throws InterruptedException, ClassNotLoadedException {

		ReferenceType rt = or.referenceType();
		List<Field> fields = rt.allFields();
		for (int i = 0; i < fields.size(); i++) {
			String name = fields.get(i).name();
			String key = name + or.uniqueID();
			Value fieldValue = or.getValue(fields.get(i));
			if (haveyouseen.containsKey(key) == false) {
				if (haveyouseen.get(key) == null)
					haveyouseen.put(key, 1);
				if ((fieldValue instanceof ObjectReference)) {
					String signature = fields.get(i).signature();
					
					
					if (!signature.contains("java")) {
						System.out.println("OBJECT Field: " + fieldValue
								+ " name: " + name + " ID: " + or.uniqueID());
						Vertex current = new Vertex((int) or.uniqueID(),
								fieldValue.toString(), true);
						
						//If Vertex array is not empty, then create an edge
						//Also, set current to equal previous
						if (g.vertices.size()>=2) {
							Edge e = new Edge((int) or.uniqueID(), prev, current, name);
							g.addEdge(e);
						}	
						//Add a vertex
						g.addVertex(current);
						Search((ObjectReference) fieldValue, haveyouseen, g, current);
					}
					
					//Search((ObjectReference) fieldValue, haveyouseen, g, current);

				} else if (fieldValue instanceof IntegerValue) {
					// else{
					String signature = fields.get(i).signature();
						System.out.println("Primative: " + fieldValue
								+ "  fieldValue" + " name: " + name + " ID "
								+ or.uniqueID() + "   " + signature);
						
						//Primative are never functions
						Vertex current = (new Vertex((int) or.uniqueID(),
								fieldValue.toString(), false));
						g.addVertex(current);
					
					if (g.vertices.size()>=2) {
						Edge e = new Edge((int) or.uniqueID(), prev, current, name);
						g.addEdge(e);
					} 
				}
			}
		}
	}
}
