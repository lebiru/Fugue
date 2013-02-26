import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.sun.jdi.*;

public class FieldMonitor {
	static int counter = 300;
	static int maxValue = 32;

	public static void monitorSys(int dataport, Graph g) throws IOException,
			InterruptedException, ClassNotLoadedException {
		ArrayList<Integer> haveyouseen = new ArrayList<Integer>();
		VirtualMachine vm = new VMAcquirer().connect(dataport);
		vm.suspend();
		List<ThreadReference> threadref = vm.allThreads();
		try {

			maxIDsearch(vm);
			List<StackFrame> stack;
			stack = threadref.get(3).frames();
			int framecount = threadref.get(3).frameCount();
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
							Search((ObjectReference) information, haveyouseen,
									g, Main);
						}
					}
				}
			}
		} catch (IncompatibleThreadStateException e) {
			e.printStackTrace();
		} catch (AbsentInformationException e) {
			e.printStackTrace();
		}

	}

	private static void Search(ObjectReference or,
			ArrayList<Integer> haveyouseen, Graph g, Vertex prev)
			throws InterruptedException, ClassNotLoadedException {
		List<Field> fields = or.referenceType().allFields();
		for (int i = 0; i < fields.size(); i++) {
			Value fieldValue = or.getValue(fields.get(i));

			if (fieldValue instanceof ObjectReference) {
				String name = fields.get(i).name();
				int key = (int) ((ObjectReference) fieldValue).uniqueID();

				if (haveyouseen.contains(key) == false) {
					haveyouseen.add(key);
					if (fieldValue.toString().contains("java.lang.Integer")) {
						createGraph(g, fieldValue, prev, name, key,i,or);
					} else {
						Vertex current = new Vertex(key, "Value: "
								+ fields.get(i).typeName() + " ID: " + key,
								false);
						g.addVertex(current);
						makeConnection(g, maxValue, prev, current, name);
						maxValue++;
						Search((ObjectReference) fieldValue, haveyouseen, g,
								current);
					}
				}

				else {
					//Vertex current = g.getVertex(key);
					//makeConnection(g, maxValue, prev, current, name);
					//maxValue++;
				}
			}

			else if (fieldValue instanceof PrimitiveValue) {


				Vertex current = new Vertex(maxValue, "Value: "
						+ fieldValue.toString() + "  ID: " + maxValue, false);
				g.addVertex(current);
				maxValue++;
				makeConnection(g, maxValue, prev, current, fields.get(i).name());
				maxValue++;
			}

			// The where we find a null value, we can ignore it
			else {
				//System.out.println("Null Value");
			}

		}
	}

	private static void createGraph(Graph g, Value fieldValue, Vertex prev,
			String name, int key, int i, ObjectReference or) {
		Field intFields = ((ObjectReference) fieldValue).referenceType().fieldByName("value");
		if (intFields.toString().contains("value")) {
			String val =((ObjectReference) fieldValue).getValue(intFields).toString();
			Vertex current = new Vertex(maxValue, "Value: "
					+ val + " ID: " + maxValue, false);
			g.addVertex(current);
			maxValue++;
			makeConnection(g, maxValue, prev, current, name);
			maxValue++;
		}
	}

	private static void makeConnection(Graph g, int key, Vertex prev,
			Vertex current, String string) {
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

	private static void idValueSearch(ObjectReference or,
			HashMap<Integer, Integer> haveyouseen) throws InterruptedException,
			ClassNotLoadedException {
		List<Field> fields = or.referenceType().allFields();
		for (int i = 0; i < fields.size(); i++) {
			Value fieldValue = or.getValue(fields.get(i));

			if (fieldValue instanceof ObjectReference) {
				int key = (int) ((ObjectReference) fieldValue).uniqueID();
				if (haveyouseen.containsKey(key) == false) {
					haveyouseen.put(key, 1);
					maxValue = Math.max(maxValue, key);
					idValueSearch((ObjectReference) fieldValue, haveyouseen);
					System.out.println(fieldValue);
				}
			}
		}
	}

	private static void maxIDsearch(VirtualMachine vm)
			throws IncompatibleThreadStateException,
			AbsentInformationException, InterruptedException,
			ClassNotLoadedException {
		HashMap<Integer, Integer> hashId = new HashMap<Integer, Integer>();
		List<ThreadReference> threadref = vm.allThreads();
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
	}
}
