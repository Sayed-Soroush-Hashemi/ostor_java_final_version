package hardware.cpu.instruction.processmanagement;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;

import hardware.cpu.instruction.Instruction;
import os.OS;

public class PrintThreadInfoInstruction extends Instruction {

	public PrintThreadInfoInstruction() {
		return ;
	}

	public void execute(OS os, HardwareThread thread, ALU alu) {
		os.printThreadInfoSystemCall(thread);
	}
} 
 
