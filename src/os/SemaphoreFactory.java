package os;

import java.util.HashMap;

public class SemaphoreFactory {
	public Semaphore create(int val) {
		return new Semaphore(val);
	}

	public Semaphore fork(Semaphore semaphore, HashMap<Thread, Thread> oldToNewThread) {
		Semaphore newSemaphore = new Semaphore(semaphore.getCredit());
		
		for(Thread oldWaiter: semaphore.getWaiters()) {
			Thread newWaiter = oldToNewThread.get(oldWaiter);
			newSemaphore.addWaiter(newWaiter);
		}

		return newSemaphore;
	}
} 
