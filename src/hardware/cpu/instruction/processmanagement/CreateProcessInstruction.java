package hardware.cpu.instruction.processmanagement;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;

import hardware.cpu.instruction.Instruction;
import os.OS;

public class CreateProcessInstruction extends Instruction {
	protected String varName;

	public CreateProcessInstruction(String varName) {
		this.varName = varName;
	}
	
	public void execute(OS os, HardwareThread thread, ALU alu) {
		os.createProcessSystemCall(thread, varName);
	}
} 
 
