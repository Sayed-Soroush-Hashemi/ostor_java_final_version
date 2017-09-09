package hardware.cpu.instruction.processmanagement;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;

import hardware.cpu.instruction.Instruction;
import os.OS;

public class KillThreadInstruction extends Instruction {
	protected String tIdExp;

	public KillThreadInstruction(String tIdExp) {
		this.tIdExp = tIdExp;
	}
	
	public void execute(OS os, HardwareThread thread, ALU alu) {
		Value tIdValue = alu.eval(tIdExp);

		if(tIdValue.isInteger() == false) {
			// TODO: throw exception
		}

		int tId = tIdValue.toInteger();

		os.killThreadSystemCall(thread, tId);
	}
} 
 
