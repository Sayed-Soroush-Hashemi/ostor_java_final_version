package hardware.cpu.instruction.processmanagement;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;

import hardware.cpu.instruction.Instruction;
import os.OS;

public class SetPriorityInstruction extends Instruction {
	protected String priorityExp;

	public SetPriorityInstruction(String priorityExp) {
		this.priorityExp = priorityExp;
	}
	
	public void execute(OS os, HardwareThread thread, ALU alu) {
		Value priority = alu.eval(priorityExp);

		if(priority.isInteger() == false) {
			// TODO: throw exception
		}

		int priorityNumber = priority.toInteger();

		os.setPrioritySystemCall(thread, priorityNumber);
	}
} 
 
