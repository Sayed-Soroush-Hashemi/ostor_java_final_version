package hardware.cpu.instruction.processmanagement;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;

import hardware.cpu.instruction.Instruction;
import os.OS;

public class PrintProcessInfoInstruction extends Instruction {

	public PrintProcessInfoInstruction() {
		return ;
	}
	
	public void execute(OS os, HardwareThread thread, ALU alu) {
		os.printProcessInfoSystemCall(thread);
	}
} 
 
