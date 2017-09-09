package hardware.cpu.instruction.processmanagement;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;

import hardware.cpu.instruction.Instruction;
import os.OS;

public class WaitForThreadInstruction extends Instruction {
	protected String tIdExp;

	public WaitForThreadInstruction(String tIdExp) {
		this.tIdExp = tIdExp;
	}

	public void execute(OS os, HardwareThread thread, ALU alu) {
		Value tId = alu.eval(tIdExp);

		if(tId.isInteger() == false) {
			// TODO: throw exception
		}

		int tIdNumber = tId.toInteger();
		os.waitForThreadSystemCall(thread, tIdNumber);
	}
} 
 
