import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import javax.xml.crypto.Data;
import com.sun.jdi.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.*;
import com.sun.xml.internal.xsom.impl.scd.Iterators.Map;

public class FieldMonitor {

	public static void monitorSys(int dataport) throws IOException,
			InterruptedException, ClassNotLoadedException {
		HashMap<Integer,Integer> haveyouseen = new HashMap<Integer,Integer>();
		VirtualMachine vm = new VMAcquirer().connect(dataport);
		vm.suspend();
		List<ThreadReference> threadref = vm.allThreads();

		try {
			// for (int i = 2; i < threadref.size(); i++) {
			List<StackFrame> stack;
			stack = threadref.get(3).frames();
			int framecount = threadref.get(3).frameCount();
			for (int j = 1; j < framecount; j++) {
				List<LocalVariable> localvariables = stack.get(j)
						.visibleVariables();

				for (int k = 0; k < localvariables.size(); k++) {
					System.out.println("Name: " + localvariables.get(k).name()
							+ " ---  Signarture: "
							+ localvariables.get(k).signature());
					if (stack.get(j).getValue(localvariables.get(k)) instanceof ObjectReference) {
						Value information = stack.get(j).getValue(
								localvariables.get(k));
						Search((ObjectReference) information, haveyouseen);
					}

				}
			}
			// }

		}

		catch (IncompatibleThreadStateException e) {
			e.printStackTrace();
		}

		catch (AbsentInformationException e) {
			e.printStackTrace();
		}
	}

	private static void Search(ObjectReference or,
			HashMap<Integer,Integer> haveyouseen) throws InterruptedException,
		ClassNotLoadedException {
		Stack<ObjectReference> s = new Stack<ObjectReference>();
		ObjectReference popped;

		s.push(or);

		while (!s.isEmpty()) {
			popped = s.pop();
			int counter = 0;
			ReferenceType rt = popped.referenceType();
			List<Field> fields = rt.allFields();
			for (int i = 0; i < fields.size(); i++) {

				Value fieldValue = popped.getValue(fields.get(i));


				if (haveyouseen.containsKey((int)popped.uniqueID()) == false||haveyouseen.get((int)popped.uniqueID())== 1) {
					{
					if(haveyouseen.get((int)popped.uniqueID())== null) {
						haveyouseen.put((int)popped.uniqueID(), 1);
					}
				
					
					
					else if(haveyouseen.get((int)popped.uniqueID())== 1) {
						haveyouseen.put((int)popped.uniqueID(), 2);
					}
					}
				
						// IF Object References
					
					
					
					
					if ((fieldValue instanceof ObjectReference)) {

						Type type = fields.get(i).type();
						String name = fields.get(i).name();
						
						System.out.println("Field: " + fields + " name: "
								+ name + " ID: "+ popped.uniqueID());
						/*if(counter==0) {
							LastVertex = new Vertex((int)popped.uniqueID(),fieldValue.toString(),true);
						}*/


						Search((ObjectReference) fieldValue, haveyouseen);
						//s.push((ObjectReference) fieldValue);

						/*Vertex temp = (new Vertex((int)popped.uniqueID(),fieldValue.toString(),true));
						g.addVertex(temp);
						LastVertex = temp;*/

						/*g.addEdge(new Edges((int)popped.uniqueID(),temp,temp,fieldValue.toString()));*/
					}
					// Else Primative Type
					else {
						//haveyouseen.add((int) popped.uniqueID());
						System.out.println("Primative: "+ fieldValue +"  fieldValue"
								+ " name: " + fields.get(i).name() + " ID "+popped.uniqueID());



					}
					
				}

			}

		}

	}


}