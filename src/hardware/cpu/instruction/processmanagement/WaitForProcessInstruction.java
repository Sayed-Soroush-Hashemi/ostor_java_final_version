package hardware.cpu.instruction.processmanagement;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;

import hardware.cpu.instruction.Instruction;
import os.OS;

public class WaitForProcessInstruction extends Instruction {
	protected String pIdExp;

	public WaitForProcessInstruction(String pIdExp) {
		this.pIdExp = pIdExp;
	}

	public void execute(OS os, HardwareThread thread, ALU alu) {
		Value pId = alu.eval(pIdExp);

		if(pId.isInteger() == false) {
			// TODO: throw exception
		}

		int pIdNumber = pId.toInteger();
		os.waitForProcessSystemCall(thread, pIdNumber);
	}
} 
 
