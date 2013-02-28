import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.sun.jdi.*;

public class FieldMonitor {
	static int counter = 1;
	static int maxValue = 100;

	public static void monitorSys(int dataport, Graph g) throws IOException,
			InterruptedException, ClassNotLoadedException {
		ArrayList<Integer> haveyouseen = new ArrayList<Integer>();
		VirtualMachine vm = new VMAcquirer().connect(dataport);
		vm.suspend();

		try {
			List<ThreadReference> threadref = vm.allThreads();
			maxIDsearch(vm);
			maxValue++;
			List<StackFrame> stack;
			stack = threadref.get(3).frames();
			int framecount = threadref.get(3).frameCount();
			{
				Vertex Begin = new Vertex(counter, "MAIN", true);
				//maxValue++;
				g.addVertex(Begin);
				counter--;

				for (int j = 1; j < framecount; j++) {
					Vertex frame = new Vertex(maxValue,"Frame "+j,false);
					g.addVertex(frame);
					maxValue++;
					List<LocalVariable> localvariables = stack.get(j).visibleVariables();
					for (int k = 0; k < localvariables.size(); k++) {
						if (stack.get(j).getValue(localvariables.get(k)) instanceof ObjectReference) {
							Value information = stack.get(j).getValue(localvariables.get(k));
							makeConnection(g, maxValue, Begin, frame,"Frame ");
							maxValue++;
							Search((ObjectReference) information, haveyouseen,g, frame);
							Begin=frame;
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
						caseInteger(g, fieldValue, prev, name);
						g.updateGraph(g.vertices, g.edges);
					} else {
						Vertex current = new Vertex(key, fields.get(i)
								.typeName() + " ID: " + key, false);
						g.addVertex(current);
						makeConnection(g, maxValue, prev, current, name);
						maxValue++;
						g.updateGraph(g.vertices, g.edges);
						Search((ObjectReference) fieldValue, haveyouseen, g,
								current);
						g.updateGraph(g.vertices, g.edges);
					}
				}

				else {

						Vertex temp = g.getVertex(key);
						makeConnection(g, maxValue, prev, temp, temp.value);
						maxValue++;
					//Search((ObjectReference) fieldValue, haveyouseen, g, temp);
				}
			}

			else if (fieldValue instanceof PrimitiveValue) {
				System.out.println(maxValue);
				Vertex current = new Vertex(maxValue, "Value: "
						+ fieldValue.toString() + "  ID: " + maxValue, false);
				g.addVertex(current);
				maxValue++;
				makeConnection(g, maxValue, prev, current, fields.get(i).name());
				maxValue++;
				g.updateGraph(g.vertices, g.edges);
			}

			// This is the case we find a null value
			else {

			
				  Vertex pointNull = new Vertex(maxValue, "NULL", false);
				  maxValue++; 
				  g.addVertex(pointNull); 
				  makeConnection(g,maxValue, prev, pointNull, "NULL"); 
				  maxValue++;

			}

		}
	}

	private static void caseInteger(Graph g, Value fieldValue, Vertex prev, String name) {
		Field intFields = ((ObjectReference) fieldValue).referenceType()
				.fieldByName("value");
		if (intFields.toString().contains("value")) {
			String val = ((ObjectReference) fieldValue).getValue(intFields)
					.toString();
			System.out.println(maxValue);
			int ID = (int) ((ObjectReference) fieldValue).uniqueID();
			Vertex current = new Vertex(ID, "Value: " + val + " ID: "
					+ ID, false);
			g.addVertex(current);
			//maxValue++;
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
