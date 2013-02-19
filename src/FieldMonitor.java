import java.io.IOException;
import java.util.List;
import java.util.Stack;

import javax.xml.crypto.Data;

import com.sun.jdi.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.*;

public class FieldMonitor {

	public static void monitorSys(int dataport) throws IOException,
			InterruptedException, ClassNotLoadedException {

		VirtualMachine vm = new VMAcquirer().connect(dataport);
		vm.suspend();


		List<ThreadReference> threadref = vm.allThreads();
		try {
			for (int i = 0; i < threadref.size(); i++) {
				List<StackFrame> stack;
				stack = threadref.get(i).frames();
				int framecount = threadref.get(i).frameCount();

				for (int j = 1; j < framecount; j++) {
					List<LocalVariable> localvariables = stack.get(j).visibleVariables();

					for (int k = 0; k < localvariables.size(); k++) {
						
						if (stack.get(j).getValue(localvariables.get(k)) instanceof ObjectReference) {
							Value information = stack.get(j)
									.getValue(localvariables.get(k));
							Search((ObjectReference) information);
						}
					}
				}
			}

		}

		catch (IncompatibleThreadStateException e) {
			e.printStackTrace();
		}

		catch (AbsentInformationException e) {
			e.printStackTrace();
		}
	}

	private static void Search(ObjectReference or) throws InterruptedException,
			ClassNotLoadedException {
		Stack<ObjectReference> s = new Stack<ObjectReference>();
		ObjectReference popped;

		s.push(or);

		while (!s.isEmpty()) {
			popped = s.pop();
			ReferenceType rt = popped.referenceType();
			List<Field> fields = rt.allFields();

			for (int i = 0; i < fields.size(); i++) {
				Value fieldValue = popped.getValue(fields.get(i));
				Type type = fields.get(i).type();
				String name = fields.get(i).name();

				if ((fields.get(i) instanceof ObjectReference)) {
				//	System.out.println("FieldValue: " + fieldValue + " Type: "
				//			+ type + " name:" + name);
					Search((ObjectReference) fields.get(i));
				}

				else {
					System.out.println("FieldValue: " + fieldValue + " Type: "
							+ type + " name:" + name);
				}

			}

		}

	}

}