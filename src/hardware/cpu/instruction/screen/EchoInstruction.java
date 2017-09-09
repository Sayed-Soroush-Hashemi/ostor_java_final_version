package hardware.cpu.instruction.screen;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;

import hardware.cpu.instruction.Instruction;
import os.OS;

public class EchoInstruction extends Instruction {
	protected String exp;

	public EchoInstruction(String exp) {
		this.exp = exp;
	}
	
	public void execute(OS os, HardwareThread thread, ALU alu) {
		Value expVal = alu.eval(exp);

		String x = expVal.toString();

		os.echoSystemCall(thread, x);
	}
} 
 
