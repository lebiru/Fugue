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
		// Setup Cach for DFS
		ArrayList<Integer> haveyouseen = new ArrayList<Integer>();
		// Connect to the virtual machine
		VirtualMachine vm = new VMAcquirer().connect(dataport);
		// Pause the VM
		vm.suspend();
		try {
			// Get a list of all the threads that ran
			List<ThreadReference> threadref = vm.allThreads();
			// run dfs to find the highest ID value
			maxIDsearch(vm);
			maxValue++;
			List<StackFrame> stack = threadref.get(3).frames();
			int framecount = threadref.get(3).frameCount();

			// Create the first vertex
			Vertex Begin = new Vertex(counter, "MAIN", true);
			g.addVertex(Begin);
			counter--;

			// go through each frame
			for (int i = 1; i < framecount; i++) {
				Vertex frame;
				// Obtain variables in the stack
				List<LocalVariable> localvariables = stack.get(i).visibleVariables();
				for (int j = 0; j < localvariables.size(); j++) {
					// Runs to find objectreferences in the stack
					
					
					
					
					
					
					if (stack.get(i).getValue(localvariables.get(j)) instanceof ObjectReference) {
						// If the vertex for the represented frame was not
						// created, then create one
						// and make a connection to it for the previous one
						if (haveyouseen.contains(i * (-1)) == false) {
							frame = new Vertex(i * -1, "Frame " + i, false);
							g.addVertex(frame);
							haveyouseen.add(i * -1);
							makeConnection(g, counter--, Begin, frame, "Frame ");
							counter--;
						} else {
							frame = g.vertices.get(i * -1);
						}
						
						
						// Get information on the local variables
						Value information = stack.get(i).getValue(localvariables.get(j));
						// Run a DFS
						Search((ObjectReference) information, haveyouseen, g, frame, information);
						// Set the current frame to a previous frame
						// for connection reference at the next iteration
						Begin = frame;
					}

				}
			}

		} catch (IncompatibleThreadStateException e) {
			e.printStackTrace();
		} catch (AbsentInformationException e) {
			e.printStackTrace();
		}
	}

	private static void Search(ObjectReference object, ArrayList<Integer> haveyouseen, Graph g, Vertex prev, Value information) throws InterruptedException,
			ClassNotLoadedException {
		// Gets all the reference field in the object reference
		List<Field> fields = object.referenceType().allFields();

		for (int i = 0; i < fields.size(); i++) {
			Value fieldValue = object.getValue(fields.get(i));
			// If it's an object reference we will do a DFS on the next
			// iteration
			if (fieldValue instanceof ObjectReference) {
				String name = fields.get(i).name();
				int key = (int) ((ObjectReference) fieldValue).uniqueID();
				// Runs if we haven't visted the current vertex
				if (haveyouseen.contains(key) == false) {
					haveyouseen.add(key);
					if (fieldValue.toString().contains("java.lang.Integer")) {
						// This is a special case of searching when dealing with
						// the Integer class
						caseInteger(g, fieldValue, prev, name);
					} else {
						// Creates a vertex
						Vertex current = new Vertex(key, fields.get(i).typeName() + " ID: " + key, false);
						g.addVertex(current);
						// Makes a connection to prev and current
						makeConnection(g, maxValue, prev, current, name);
						maxValue++;
						// Run DFS again
						Search((ObjectReference) fieldValue, haveyouseen, g, current,information);
					}
				}

				// If we have seen the ObjectReference, then we just make a
				// connection to it
				else {
					Vertex temp = g.getVertex(key);
					makeConnection(g, maxValue, prev, temp, temp.value);
					maxValue++;
					
				}
			}

			// Once we it a primative value, we create a vertex and connect it
			// to the previous vertex
			else if (fieldValue instanceof PrimitiveValue) {
				System.out.println(maxValue);
				Vertex current = new Vertex(maxValue, "Value: " + fieldValue.toString() + "  ID: " + maxValue, false);
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
		//Used the ID from the Integer class as the reference ID for the vertex
		int ID = (int) ((ObjectReference) fieldValue).uniqueID();
		Vertex current = new Vertex(ID, "Value: " + val + " ID: " + ID, false);
		g.addVertex(current);
		makeConnection(g, maxValue, prev, current, name);
		maxValue++;
	}

	//A useless function to add a connection.
	//But, it made me feel safe doing it this way.
	//As too why not all the other stuff...no much of an explanation
	private static void makeConnection(Graph g, int key, Vertex prev, Vertex current, String string) {
		g.addEdge(new Edge(key, prev, current, string));
	}
	
	//This is a DFS, but just to find the max value of the ID
	private static void idValueSearch(ObjectReference or, HashMap<Integer, Integer> haveyouseen) throws InterruptedException, ClassNotLoadedException {
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
	
	//Go through every stack frame to find the largest ID value
	private static void maxIDsearch(VirtualMachine vm) throws IncompatibleThreadStateException, AbsentInformationException, InterruptedException,
			ClassNotLoadedException {
		HashMap<Integer, Integer> hashId = new HashMap<Integer, Integer>();
		List<ThreadReference> threadref = vm.allThreads();
		List<StackFrame> stack;
		stack = threadref.get(3).frames();
		int framecount = threadref.get(3).frameCount();
		for (int j = 1; j < framecount; j++) {
			List<LocalVariable> localvariables = stack.get(j).visibleVariables();
			for (int k = 0; k < localvariables.size(); k++) {
				if (stack.get(j).getValue(localvariables.get(k)) instanceof ObjectReference) {
					Value information = stack.get(j).getValue(localvariables.get(k));
					idValueSearch((ObjectReference) information, hashId);
				}
			}
		}
	}
}
