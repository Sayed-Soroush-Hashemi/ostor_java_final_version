package hardware.cpu.instruction.processmanagement.synchronization;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;

import hardware.cpu.instruction.Instruction;
import os.OS;

public class SignalSemaphoreInstruction extends Instruction {
	protected String semaphoreName;

	public SignalSemaphoreInstruction(String semaphoreName) {
		this.semaphoreName = semaphoreName;
	}
	
	public void execute(OS os, HardwareThread thread, ALU alu) {
		os.signalSemaphoreSystemCall(thread, semaphoreName);
	}
} 
 
