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
		// connect
		Runtime.getRuntime().exec("javac -g InterestingQueue.java").waitFor();
		Runtime.getRuntime()
				.exec("java -Xdebug -Xrunjdwp:transport=dt_socket,address=8686,server=y,suspend=n InterestingQueue")
				.waitFor();

		VirtualMachine vm = new VMAcquirer().connect(dataport);
		vm.suspend();

		// set watch field on already loaded classes
		List<ThreadReference> threadref = vm.allThreads();
		try {
			for (int i = 3; i < threadref.size(); i++) {
				List<StackFrame> stack;
				stack = threadref.get(i).frames();
				int framecount = threadref.get(i).frameCount();

				for (int j = 1; j < framecount; j++) {
					List<LocalVariable> vv = stack.get(j).visibleVariables();

					for (int k = 0; k < vv.size(); k++) {
						System.out.println("Name: "+vv.get(k).name()+" ---  Signarture: "+vv.get(k).signature());
						
						if (stack.get(j).getValue(vv.get(k)) instanceof ObjectReference) {
							Value information = stack.get(j)
									.getValue(vv.get(k));
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

	private static void Search(ObjectReference or)
			throws InterruptedException {
		Stack<ObjectReference> s = new Stack<ObjectReference>();
		ObjectReference popped;

		s.push(or);

		while (!s.isEmpty()) {
			popped = s.pop();
			ReferenceType rt = popped.referenceType();
			List<Field> fields = rt.allFields();

			for (int i = 0; i < fields.size(); i++) {
				if (fields.get(i) instanceof ObjectReference) {
					s.push((ObjectReference) fields.get(i));
					System.out.println((ObjectReference) fields.get(i) + " is an object");
				}

				else {
					System.out.println(fields.get(i));

				}

			}

		}

	}



}