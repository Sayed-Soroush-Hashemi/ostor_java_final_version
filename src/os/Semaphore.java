package os;

import java.util.LinkedList;
import java.util.Queue;

public class Semaphore implements Expected {
	protected int credit;
	protected Queue<Thread> waiters;

	public Semaphore(int credit) {
		this.credit = credit;
		waiters = new LinkedList<>();
	}

	public void addWaiter(Thread newWaiter) {
		waiters.add(newWaiter);
	}
	public Thread getNextWaiter() {
		if(waiters.size() == 0) {
			// TODO: throw exception
		}
		return waiters.poll();
	}
	public boolean hasNextWaiter() {
		return waiters.size() != 0;
	}
	public Queue<Thread> getWaiters() {
		return waiters;
	}

	public boolean hasCredit() {
		return credit > 0;
	}
	public void increaseCredit() {
		credit++;
	}
	public void decreaseCredit() {
		credit--;
	}
	public int getCredit() {
		return credit;
	}
} 
