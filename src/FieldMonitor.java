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
					/*System.out.println("Name: " + localvariables.get(k).name()
							+ " ---  Signarture: "
							+ localvariables.get(k).signature());*/
					if (stack.get(j).getValue(localvariables.get(k)) instanceof ObjectReference) {
						Value information = stack.get(j).getValue(
								localvariables.get(k));
						Search((ObjectReference) information, haveyouseen);
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
		

	private static void Search(ObjectReference or, HashMap<String, Integer> haveyouseen) throws InterruptedException,
			ClassNotLoadedException {
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
						
						if(!signature.contains("java")){
						System.out.println("OBJECT Field: " + fieldValue  + " name: "
								+ name + " ID: " + or.uniqueID()+ "    " +signature);
						}
						
						
						Search((ObjectReference) fieldValue, haveyouseen);
						
					}
					
					else if (fieldValue instanceof IntegerValue) {
						//else{
						String signature = fields.get(i).signature();
						System.out.println("Primative: " + fieldValue
								+ "  fieldValue" + " name: " + name + " ID "
								+ or.uniqueID() + "   "+signature);
					}
				}
			}
		}
	
}
	

