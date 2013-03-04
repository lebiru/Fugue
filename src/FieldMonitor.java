import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.sun.jdi.*;

public class FieldMonitor {
	// Used for ID values of the edges in the graph
	static int maxValue = 0;
	// Used for the ID values of main and the frames
	static int counter = 0;

	public static void monitorSys(int dataport, Graph g) throws IOException, InterruptedException, ClassNotLoadedException {
		ArrayList<Integer> haveyouseen = new ArrayList<Integer>();
		VirtualMachine vm = new VMAcquirer().connect(dataport);
		vm.suspend();
		try {
			List<ThreadReference> threadref = vm.allThreads();
			maxIDsearch(vm);
			maxValue++;
			List<StackFrame> stack = threadref.get(3).frames();
			int framecount = threadref.get(3).frameCount();
			Vertex Begin = new Vertex(counter, "MAIN", true);
			g.addVertex(Begin);
			counter--;

			for (int i = 1; i < framecount; i++) {
				List<LocalVariable> locals = stack.get(i).visibleVariables();
				for (int j = 0; j < locals.size(); j++) {

					Vertex frame = frameHandle(g, haveyouseen, i, Begin);
					LocalVariable infoData = locals.get(j);
					String name = infoData.name();
					Value information = stack.get(i).getValue(locals.get(j));
					if(information instanceof ArrayReference || information instanceof StringReference ){
						Vertex current =otherReferences(g,information,infoData);
						g.addVertex(current);
						makeConnection(g,maxValue,frame,current,infoData.name());
						maxValue++;
					}
					else if (information instanceof ObjectReference) {
						int key = (int) ((ObjectReference) information).uniqueID();
						if (haveyouseen.contains(key) == false) {
							if (information.toString().contains("java.lang.Integer")) {
								caseInteger(g, information, frame, name);
							} else {
								Vertex current = new Vertex(key, infoData.typeName().toString(), false);
								haveyouseen.add(key);
								g.addVertex(current);
								makeConnection(g, maxValue, frame, current, infoData.name());
								maxValue++;
								Search((ObjectReference) information, haveyouseen, g, current);
							}
						} else {
							Vertex temp = g.getVertex(key);
							makeConnection(g, maxValue, frame, temp, temp.value);
							maxValue++;
						}
					} else {
						Vertex current = new Vertex(maxValue, "NULL", false);
						maxValue++;
						g.addVertex(current);
						makeConnection(g, maxValue, frame, current, infoData.name().toString());
						maxValue++;
						System.out.println("ACTIVATED : " + locals.get(j).type() + "  " + infoData.name() + "  ");
					}
					Begin = frame;

				}
			}

		} catch (IncompatibleThreadStateException e) {
			e.printStackTrace();
		} catch (AbsentInformationException e) {
			e.printStackTrace();
		}
	}
	
	
	private static Vertex otherReferences(Graph g, Value information, LocalVariable infoData) throws ClassNotLoadedException{
		Vertex result;
		
		if(information instanceof ArrayReference) {
				Vertex current = new Vertex(maxValue, infoData.type().toString(),false);
				g.addVertex(current);
				maxValue++;
				System.out.println(infoData.type());
				
			result = current;
		}
		else {
			Vertex current = new Vertex(maxValue, infoData.typeName().toString(), false);
			g.addVertex(current);
			maxValue++;
			result = current;
		}
		return result;
	}


	private static Vertex frameHandle(Graph g, ArrayList<Integer> haveyouseen, int i, Vertex Begin) {
		Vertex vertex;
		if (haveyouseen.contains(i * (-1)) == false) {
			vertex = new Vertex(i * -1, "Frame " + i, false);
			g.addVertex(vertex);
			haveyouseen.add(i * -1);
			makeConnection(g, counter--, Begin, vertex, "Frame ");
			counter--;
		} else {
			vertex = g.vertices.get(i * -1);
		}
		return vertex;
	}

	private static void Search(ObjectReference object, ArrayList<Integer> haveyouseen, Graph g, Vertex prev) throws InterruptedException,
			ClassNotLoadedException {
		List<Field> fields = object.referenceType().allFields();

		for (int i = 0; i < fields.size(); i++) {
			Value fieldValue = object.getValue(fields.get(i));
			if (fieldValue instanceof ObjectReference) {
				String name = fields.get(i).name();
				int key = (int) ((ObjectReference) fieldValue).uniqueID();
				if (haveyouseen.contains(key) == false) {
					haveyouseen.add(key);
					if (fieldValue.toString().contains("java.lang.Integer")) {
						caseInteger(g, fieldValue, prev, name);
					} else {
						Vertex current = new Vertex(key, fields.get(i).typeName(), false);
						g.addVertex(current);
						makeConnection(g, maxValue, prev, current, name);
						maxValue++;
						Search((ObjectReference) fieldValue, haveyouseen, g, current);
					}
				}

				else {
					Vertex temp = g.getVertex(key);
					makeConnection(g, maxValue, prev, temp, name);
					maxValue++;
				}

			}

			// Once we it a primative value, we create a vertex and connect it
			// to the previous vertex
			else if (fieldValue instanceof PrimitiveValue) {
				Vertex current = new Vertex(maxValue, "Value: " + fieldValue.toString(), false);
				g.addVertex(current);
				maxValue++;
				makeConnection(g, maxValue, prev, current, fields.get(i).name());
				maxValue++;
			}

			// This is the case of hitting null
			// Once DFS hits a null value, we make a special null vertex
			else {
				Vertex pointNull = new Vertex(maxValue, "NULL", false);
				maxValue++;
				g.addVertex(pointNull);
				makeConnection(g, maxValue, prev, pointNull, fields.get(i).name());
				maxValue++;
			}
		}
	}

	// This is a special case for the Integer Class
	private static void caseInteger(Graph g, Value fieldValue, Vertex prev, String name) {
		Field intFields = ((ObjectReference) fieldValue).referenceType().fieldByName("value");
		String val = ((ObjectReference) fieldValue).getValue(intFields).toString();
		// Used the ID from the Integer class as the reference ID for the vertex
		int ID = (int) ((ObjectReference) fieldValue).uniqueID();
		Vertex current = new Vertex(ID, "Value: " + val, false);
		g.addVertex(current);
		makeConnection(g, maxValue, prev, current, name);
		maxValue++;
	}

	// Creates a an edge
	private static void makeConnection(Graph g, int key, Vertex prev, Vertex current, String string) {
		g.addEdge(new Edge(key, prev, current, string));
	}

	// This is a DFS, but just to find the max value of the ID
	private static void idValueSearch(ObjectReference or, ArrayList<Integer> haveyouseen) throws InterruptedException, ClassNotLoadedException {
		List<Field> fields = or.referenceType().allFields();
		for (int i = 0; i < fields.size(); i++) {
			Value fieldValue = or.getValue(fields.get(i));

			if (fieldValue instanceof ObjectReference) {
				int key = (int) ((ObjectReference) fieldValue).uniqueID();
				if (haveyouseen.contains(key) == false) {
					haveyouseen.add(key);
					maxValue = Math.max(maxValue, key);
					idValueSearch((ObjectReference) fieldValue, haveyouseen);
				}
			}
		}
	}

	// Go through every stack frame to find the largest ID value
	private static void maxIDsearch(VirtualMachine vm) throws IncompatibleThreadStateException, AbsentInformationException, InterruptedException,
			ClassNotLoadedException {
		ArrayList<Integer> ID = new ArrayList<Integer>();
		List<ThreadReference> threadref = vm.allThreads();
		List<StackFrame> stack;
		stack = threadref.get(3).frames();
		int framecount = threadref.get(3).frameCount();
		for (int j = 1; j < framecount; j++) {
			List<LocalVariable> localvariables = stack.get(j).visibleVariables();
			for (int k = 0; k < localvariables.size(); k++) {
				if (stack.get(j).getValue(localvariables.get(k)) instanceof ObjectReference) {
					Value information = stack.get(j).getValue(localvariables.get(k));
					idValueSearch((ObjectReference) information, ID);
				}
			}
		}
	}
}