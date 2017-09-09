package hardware.cpu.instruction;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;

import os.OS;

public class LabelInstruction extends Instruction {
	protected String labelNumber;

	public LabelInstruction(String labelNumber) {
		this.labelNumber = labelNumber;
	}

	public void execute(OS os, HardwareThread thread, ALU alu) {
		return ;
	}
} 
 
