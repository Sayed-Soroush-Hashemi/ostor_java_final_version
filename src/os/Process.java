package os;

import java.util.LinkedList;
import java.util.Vector;
import java.util.Queue;
import java.util.HashMap;

import hardware.cpu.Value;

public class Process implements Expected {
	protected int pId;
	protected Vector<Thread> threads;
	protected Queue<Thread> waiters;
	
	protected HashMap<String, Value> heap;
	protected HashMap<String, Semaphore> semaphores;

	public int getPId() {
		return pId;
	}
	public void setPId(int newPId) {
		pId = newPId;
	}

	public Process() {
		threads = new Vector<>();
		waiters = new LinkedList<>();

		heap = new HashMap<>();
		semaphores = new HashMap<>();
	}

	public Vector<Thread> getThreads() {
		return threads;
	}
	public void clearThreads() {
		threads = new Vector<Thread>();
	}
	public void addThread(Thread thread) {
		threads.add(thread);
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
	public void cleanWaiters() {
		waiters = new LinkedList<Thread>();
	}
	public Queue<Thread> getWaiters() {
		return waiters;
	}

	public HashMap<String, Semaphore> getSemaphores() {
		return semaphores;
	}
	public Semaphore getSemaphore(String semaphoreName) {
		return semaphores.get(semaphoreName);
	}
	public void clearSemaphores() {
		semaphores = new HashMap<String, Semaphore>();
	}
	public void addSemaphore(String semaphoreName, Semaphore semaphore) {
		semaphores.put(semaphoreName, semaphore);
	}

	public HashMap<String, Value> getHeap() {
		return heap;
	}
	public Value getFromHeap(String varName) {
		return heap.get(varName);
	}
	public void clearHeap() {
		heap = new HashMap<String, Value>();
	}
	public void setInHeap(String varName, Value value) {
		heap.put(varName, value);
	}
	public boolean isInHeap(String varName) {
		return (heap.get(varName) != null);
	}

	public boolean isInSemaphores(String varName) {
		Semaphore targetSemaphore = semaphores.get(varName);
		return (targetSemaphore != null);
	}
	public Value getFromSemaphores(String varName) {
		Semaphore targetSemaphore = semaphores.get(varName);
		return new Value(targetSemaphore.getCredit());
	}
}
