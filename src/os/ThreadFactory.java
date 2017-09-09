package os;

import java.io.FileNotFoundException;
import java.util.Vector;

import compiler.Compiler;
import hardware.cpu.instruction.*;

public class ThreadFactory {
	public Thread create(String programPath) throws FileNotFoundException {
		// the following thread's attributes is not set in this method:
		//		process
		// 		tId
		// these must be set by the caller

		Thread newThread = new Thread();

		newThread.setPriority(1);

		newThread.setStatus(Status.READY);

		newThread.setPC(0);

		Compiler compiler = new Compiler();
		Vector<Instruction> instructions = compiler.compile(programPath);
		newThread.setCode(instructions);

		return newThread;
	}

	public Thread forkForCreateThread(Thread thread) {
		// the following thread's attributes is not set in this method:
		// 		tId
		// these must be set by the caller
		Process process = thread.getProcess();

		Thread newThread = new Thread();
		
		process.addThread(newThread);
		newThread.setProcess(process);
		newThread.setPriority(thread.getPriority());

		newThread.setStatus(Status.READY);
		newThread.setExpected(null);
		for(Thread waiter: thread.getWaiters())
			newThread.addWaiter(waiter);
		
		for(String varName: thread.getStack().keySet())
			newThread.setInStack(varName, thread.getFromStack(varName));

		newThread.setPC(thread.getPC());
		newThread.setCode(thread.getCode());

		return newThread;
	}

	public Thread forkForCreateProcess(Thread thread) {
		// the following thread's attributes is not set in this method:
		// 		process
		// 		tId
		// 		waiters
		// 		expected
		// these must be set by the caller

		Thread newThread = new Thread();

		newThread.setPriority(thread.getPriority());
		if(thread.isRunning())
			newThread.setStatus(Status.READY);
		else 
			newThread.setStatus(thread.getStatus());

		for(String varName: thread.getStack().keySet())
			newThread.setInStack(varName, thread.getFromStack(varName));

		newThread.setPC(thread.getPC());
		newThread.setCode(thread.getCode());

		return newThread;
	}
} 
