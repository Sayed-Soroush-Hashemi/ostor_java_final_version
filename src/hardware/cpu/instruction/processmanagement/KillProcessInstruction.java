package hardware.cpu.instruction.processmanagement;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;

import hardware.cpu.instruction.Instruction;
import os.OS;

public class KillProcessInstruction extends Instruction {
	protected String pIdExp;

	public KillProcessInstruction(String pIdExp) {
		this.pIdExp = pIdExp;
	}
	
	public void execute(OS os, HardwareThread thread, ALU alu) {
		Value pIdValue = alu.eval(pIdExp);

		if(pIdValue.isInteger() == false) {
			// TODO: throw exception
		}

		int pId = pIdValue.toInteger();

		os.killProcessSystemCall(thread, pId);
	}
} 
 
