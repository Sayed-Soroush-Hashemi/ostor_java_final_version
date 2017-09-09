package os;

import hardware.cpu.Value;

import java.io.FileNotFoundException;
import java.util.HashMap;

public class ProcessFactory {
	public Process create(String initProgramPath) throws FileNotFoundException {
		// the following process's attributes is not set in this method:
		// 		pId
		// these must be set by the caller

		Process newProcess = new Process();

		ThreadFactory threadFactory = new ThreadFactory();
		Thread newThread = threadFactory.create(initProgramPath);

		newProcess.addThread(newThread);
		newThread.setProcess(newProcess);

		return newProcess;
	}
	
	public Process fork(Process process) {
		// the following process's attributes is not set in this method:
		// 		pId
		// these must be set by the caller


		Process newProcess = new Process();

		// process heap
		for(String varName: process.getHeap().keySet()) {
			Value varValue = process.getFromHeap(varName);
			newProcess.setInHeap(varName, new Value(varValue.getValue(), varValue.getType()));
		}

		// process's threads:
		HashMap<Thread, Thread> oldToNewThread = new HashMap<Thread, Thread>();
		ThreadFactory threadFactory = new ThreadFactory();
		for(Thread oldThread: process.getThreads()) {
			Thread newThread = threadFactory.forkForCreateProcess(oldThread);

			// thread's process
			newProcess.addThread(newThread);
			newThread.setProcess(newProcess);

			oldToNewThread.put(oldThread, newThread);
		}

		// process semaphores
		HashMap<Semaphore, Semaphore> oldToNewSemaphore = new HashMap<Semaphore, Semaphore>();
		SemaphoreFactory semaphoreFactory = new SemaphoreFactory();
		for(String semaphoreName: process.getSemaphores().keySet()) {
			Semaphore oldSemaphore = process.getSemaphores().get(semaphoreName);
			Semaphore newSempahore = semaphoreFactory.fork(oldSemaphore, oldToNewThread);

			newProcess.addSemaphore(semaphoreName, newSempahore);

			oldToNewSemaphore.put(oldSemaphore, newSempahore);
		}

		// process's waiters;
		for(Thread oldWaiter: process.getWaiters()) {
			Thread newWaiter = oldToNewThread.get(oldWaiter);
			if(newWaiter != null)
				newProcess.addWaiter(newWaiter);
			else
				newProcess.addWaiter(oldWaiter);
		}


		// threads' waiters and expected
		for(Thread oldThread: process.getThreads()) {
			Thread newThread = oldToNewThread.get(oldThread);

			// thread waiters;
			for(Thread oldWaiter: oldThread.getWaiters()) {
				Thread newWaiter = oldToNewThread.get(oldWaiter);
				if(newWaiter != null) 
					newThread.addWaiter(newWaiter);
				else
					newThread.addWaiter(oldWaiter);
			}

			// thread expected;
			if(Process.class.isInstance(oldThread.getExpected())) {
				if(oldThread.getExpected() == process) {
					newThread.setExpected(newProcess);
					newProcess.addWaiter(newThread);
				} else {
					newThread.setExpected(oldThread.getExpected());
					oldThread.getExpected().addWaiter(newThread);
				}
			} else if(Thread.class.isInstance(oldThread.getExpected())) {
				if(oldToNewThread.get(oldThread.getExpected()) != null) {
					newThread.setExpected(oldToNewThread.get(oldThread.getExpected()));
					oldToNewThread.get(oldThread.getExpected()).addWaiter(newThread);
				} else {
					newThread.setExpected(oldThread.getExpected());
					oldThread.getExpected().addWaiter(newThread);
				}
			} else if(Semaphore.class.isInstance(oldThread.getExpected())) {
				if(oldToNewSemaphore.get(oldThread.getExpected()) != null) {
					newThread.setExpected(oldToNewSemaphore.get(oldThread.getExpected()));
					oldToNewSemaphore.get(oldThread.getExpected()).addWaiter(newThread);
				} else {
					newThread.setExpected(oldThread.getExpected());
					oldThread.getExpected().addWaiter(newThread);
				}
			}
		}

		return newProcess;
	}
} 
