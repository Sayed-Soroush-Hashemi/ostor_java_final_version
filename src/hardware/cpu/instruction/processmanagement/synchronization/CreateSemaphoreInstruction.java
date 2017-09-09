package hardware.cpu.instruction.processmanagement.synchronization;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;

import hardware.cpu.instruction.Instruction;
import os.OS;

public class CreateSemaphoreInstruction extends Instruction {
	protected String semaphoreName;
	protected String semaphoreValueExp;

	public CreateSemaphoreInstruction(String semaphoreName, String semaphoreValueExp) {
		this.semaphoreName = semaphoreName;
		this.semaphoreValueExp = semaphoreValueExp;
	}
	
	public void execute(OS os, HardwareThread thread, ALU alu) {
		Value semaphoreValue = alu.eval(semaphoreValueExp);

		if(semaphoreValue.isInteger() == false) {
			// TODO: throw exception
		}

		int semaphoreValueNumber = semaphoreValue.toInteger();

		os.createSemaphoreSystemCall(thread, semaphoreName, semaphoreValueNumber);
	}
} 
 
