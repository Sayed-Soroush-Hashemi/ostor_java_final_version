package os;

import java.util.LinkedList;
import java.util.Vector;
import java.util.Queue;
import java.util.HashMap;

import hardware.cpu.HardwareThread;
import hardware.cpu.Value;
import hardware.cpu.instruction.*;

public class Thread implements HardwareThread, SoftwareThread, Expected {
	protected Process process;
	protected int tId;
	protected int priority;

	protected Status status;
	protected Queue<Thread> waiters = new LinkedList<>();
	protected Expected expected;
	
	protected HashMap<String, Value> stack = new HashMap<>();
	
	protected int pc;
	protected Vector<Instruction> code = new Vector<>();

	public void setProcess(Process process) {
		this.process = process;
	}
	public Process getProcess() {
		return this.process;
	}


	public HashMap<String, Value> getStack() {
		return stack;
	}

	public Vector<Instruction> getCode() {
		return code;
	}
	public void setCode(Vector<Instruction> code) {
		this.code = code;
	}

	
	public void setTId(int tId) {
		this.tId = tId;
	}
	public int getTId() {
		return tId;
	}

	
	public void setStatus(Status newStatus) {
		status = newStatus;
	}
	public Status getStatus() {
		return status;
	}
	public boolean isRunning() {
		return status == Status.RUNNING;
	}
	public boolean isReady() {
		return status == Status.READY;
	}
	public boolean isWaiting() {
		return status == Status.WAITING;
	}
	public boolean isTerminated() {
		return status == Status.TERMINATED;
	}
	public boolean isOutOfInstructions() {return pc >= code.size(); }


	public HashMap<String, Semaphore> getSemaphores() {
		return process.getSemaphores();
	}
	public Semaphore getSemaphore(String semaphoreName) {
		return process.getSemaphore(semaphoreName);
	}
	public void clearSemaphores() {
		process.clearSemaphores();
	}
	public void addSemaphore(String semaphoreName, Semaphore semaphore) {
		process.addSemaphore(semaphoreName, semaphore);
	}
	

	public void setExpected(Expected expected) {
		this.expected = expected;
	}
	public Expected getExpected() {
		return this.expected;
	}

	public boolean isInStack(String varName) {
		return (stack.get(varName) != null);
	}
	public Value getFromStack(String varName) {
		return stack.get(varName);
	}

	@Override
	public void setInStack(String varName, Value value) {
		stack.put(varName, value);
	}


	public boolean isInHeap(String varName) {
		return process.isInHeap(varName);
	}
	public Value getFromHeap(String varName) {
		return process.getFromHeap(varName);
	}

	@Override
	public void setInHeap(String varName, Value value) {
		process.setInHeap(varName, value);
	}

	@Override
	public boolean isVarDeclared(String varName) {
		return isInStack(varName) | isInHeap(varName) | isInSemaphores(varName);
	}

	@Override
	public Value getVar(String varName) {
		if(isInHeap(varName))
			return getFromHeap(varName);
		if(isInStack(varName))
			return getFromStack(varName);
		if(isInSemaphores(varName))
			return getFromSemaphores(varName);
		// TODO: throw exception
		return null;
	}

	@Override
	public boolean isInSemaphores(String varName) {
		return process.isInSemaphores(varName);
	}

	@Override
	public Value getFromSemaphores(String varName) {
		return process.getFromSemaphores(varName);
	}


	public Instruction getNextInstruction() {
		pc++;
		return code.get(pc-1);
	}
	public int getPC() { 
		return pc;
	}
	public void setPC(int newPC) {
		pc = newPC;
	}


	public void setPriority(int newPriority) {
		this.priority = newPriority;
	}
	public int getPriority() {
		return this.priority;
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
}
