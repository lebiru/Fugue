
import java.io.IOException;
import java.util.List;

import com.sun.jdi.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.*;

public class FieldMonitor {

	public static final String CLASS_NAME = "Test";
	public static final String FIELD_NAME = "foo";

	public static void monitorSys(int dataport) throws IOException,
			InterruptedException, ClassNotLoadedException {
		// connect
		VirtualMachine vm = new VMAcquirer().connect(dataport);
		vm.suspend();

		// set watch field on already loaded classes
		List<ReferenceType> referenceTypes = vm.classesByName(CLASS_NAME);
		List<ThreadReference> tr = vm.allThreads();
		try {
		for (int i = 0; i < tr.size(); i++) {
			List<StackFrame> sf;
			sf = tr.get(i).frames();
			int fc = tr.get(i).frameCount();
			System.out.println(fc);
			for(int j = 1; j < fc; j++) {
				List<LocalVariable> vv = sf.get(j).visibleVariables();

				for (int k = 0; k < vv.size(); k++) {
					
					System.out.println("Type: " + vv.get(k).type());
					System.out.println("Name: " + vv.get(k).name());
					System.out.println("typename: " + vv.get(k).typeName());
					System.out.println("Signature Type  " + vv.get(k).type().signature());
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
		
	
		
		
/*		for (ReferenceType refType : referenceTypes) {
			addFieldWatch(vm, refType);
		}
		// watch for loaded classes
		addClassWatch(vm);

		// resume the vm
		vm.resume();
		// process events
		EventQueue eventQueue = (EventQueue) vm.eventQueue();
		while (true) {
			EventSet eventSet = eventQueue.remove();
			for (Event event : eventSet) {
				if (event instanceof VMDeathEvent
						|| event instanceof VMDisconnectEvent) {
					// exit
					return;
				} else if (event instanceof ClassPrepareEvent) {
					// watch field on loaded class
					ClassPrepareEvent classPrepEvent = (ClassPrepareEvent) event;
					ReferenceType refType = classPrepEvent.referenceType();
					addFieldWatch(vm, refType);
				} else if (event instanceof ModificationWatchpointEvent) {
					// a Test.foo has changed
					ModificationWatchpointEvent modEvent = (ModificationWatchpointEvent) event;
					System.out.println("old=" + modEvent.valueCurrent());
					System.out.println("new=" + modEvent.valueToBe());
					System.out.println(); 
				}

				
			}
			eventSet.resume();
		}*/
		
	}

	/** Watch all classes of name "Test" */
	private static void addClassWatch(VirtualMachine vm) {
		EventRequestManager erm = vm.eventRequestManager();
		ClassPrepareRequest classPrepareRequest = erm
				.createClassPrepareRequest();
		classPrepareRequest.addClassFilter(CLASS_NAME);
		classPrepareRequest.setEnabled(true);
	}

	/** Watch field of name "foo" */
	private static void addFieldWatch(VirtualMachine vm, ReferenceType refType) {
		EventRequestManager erm = vm.eventRequestManager();
		Field field = refType.fieldByName(FIELD_NAME);
		ModificationWatchpointRequest modificationWatchpointRequest = erm
				.createModificationWatchpointRequest(field);
		modificationWatchpointRequest.setEnabled(true);
	}

}