package os.scheduler;

import java.util.HashMap;

import hardware.cpu.CPU;
import os.Thread;
import os.Status;

public class Scheduler {
	protected HashMap<Thread, Integer> totalWait = new HashMap<Thread, Integer>();
	protected HashMap<Thread, Integer> checkpoint = new HashMap<Thread, Integer>();
	protected CPU cpu;

	public void addThread(Thread thread) {
		totalWait.put(thread, 0);
		checkpoint.put(thread, getCurrentTotalExecCount());
	}
	public void delThread(Thread thread) {
		totalWait.remove(thread);
		checkpoint.remove(thread);
	}
	
	public void update(Thread thread, Status oldStatus, Status newStatus) {
		if(oldStatus == newStatus)
			return ;

		if(oldStatus == Status.READY && newStatus == Status.RUNNING) {
			int lastWait = getCurrentTotalExecCount() - checkpoint.get(thread);
			totalWait.put(thread, totalWait.get(thread) + lastWait);
		}

		checkpoint.put(thread, getCurrentTotalExecCount());
	}

	public Thread getBestThreadToRun() {
		int minWait = 1000000000;
		Thread bestThread = null;
		for(Thread thread: totalWait.keySet()) {
			int lastWait = 0;
			if(!thread.isReady())
				continue ;
			lastWait = getCurrentTotalExecCount() - checkpoint.get(thread);
			int currentWait = totalWait.get(thread) + lastWait;

			if(currentWait < minWait) {
				minWait = currentWait;
				bestThread = thread;
			}
		}

		return bestThread;
	}

	public void setCPU(CPU cpu) {
		this.cpu = cpu;
	}

	protected int getCurrentTotalExecCount() {
		if(cpu == null)
			return 0;
		return cpu.getTotalInstructionsExecuted();
	}
} 
