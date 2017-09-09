package hardware.cpu;

import java.util.Vector;

import hardware.cpu.instruction.Instruction;

public interface HardwareThread {
	public boolean isRunning();
	public boolean isReady();
	public boolean isWaiting();
	public boolean isTerminated();
	public boolean isOutOfInstructions();
	
	public boolean isInStack(String varName);
	public Value getFromStack(String varName);
	public void setInStack(String varName, Value value);
	
	public boolean isInHeap(String varName);
	public Value getFromHeap(String varName);
	public void setInHeap(String varName, Value value);

	public boolean isVarDeclared(String varName);
	public Value getVar(String varName);

	public boolean isInSemaphores(String varName);
	public Value getFromSemaphores(String varName); 

	public Instruction getNextInstruction();
	public int getPC();
	public void setPC(int newPC);
} 
