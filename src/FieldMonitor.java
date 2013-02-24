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
				for (int k = 0; k < localvariables.size(); k++) {
					if (stack.get(j).getValue(localvariables.get(k)) instanceof ObjectReference) {
						Value information = stack.get(j).getValue(
								localvariables.get(k));
						Vertex Main = new Vertex(1, "Main", true);
						g.addVertex(Main);
						Search((ObjectReference) information, haveyouseen, g,
								Main);
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

		
		List<Field> fields = or.referenceType().allFields();
		for (int i = 0; i < fields.size(); i++) {
			String name = fields.get(i).name();
			String key = name + or.uniqueID();
			Value fieldValue = or.getValue(fields.get(i));
			if (haveyouseen.containsKey(key) == false) {
				if (haveyouseen.get(key) == null) {
					haveyouseen.put(key, 1);
				}
				if ((fieldValue instanceof ObjectReference)) {
					testInformation(fieldValue, name, or.uniqueID(), fields
							.get(i).signature(), true);
					Vertex current;
					if (fieldValue.equals(null)) {
						 current = new Vertex((int) or.uniqueID(),"EMPTY".toString(), true);
					} else {
						 current = new Vertex((int) or.uniqueID(),
								fieldValue.toString(), true);
					}
					g.addVertex(current);
					edgeCreation(g, prev, current, (int) or.uniqueID(),
							fieldValue, true);
					Search((ObjectReference) fieldValue, haveyouseen, g,
							current);

				} else {
					testInformation(fieldValue, name, or.uniqueID(), fields
							.get(i).signature(), false);
					Vertex current;
					if (fieldValue != null) {
						current = new Vertex((int) or.uniqueID(),
								fieldValue.toString(), false);
					} else {
						current = new Vertex((int) or.uniqueID(), "itsempty",
								false);
					}

					g.addVertex(current);
					edgeCreation(g, prev, current, (int) or.uniqueID(),
							fieldValue, false);
				}

			}
		}
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

	public static void edgeCreation(Graph g, Vertex prev, Vertex current,
			int ID, Value fieldValue, boolean funcOrNot) {
		if (fieldValue != null) {
			g.addEdge(new Edge(ID, prev, current, fieldValue.toString()));
			
		} else {
			g.addEdge(new Edge(ID, prev, current, "wef"));
		}
	}

}
