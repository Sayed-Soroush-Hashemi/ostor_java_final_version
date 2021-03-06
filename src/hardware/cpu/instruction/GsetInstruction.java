package hardware.cpu.instruction;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;

import os.OS;

public class GsetInstruction extends Instruction {
	protected String varName;
	protected String valExp;

	public GsetInstruction(String varName, String valExp) {
		this.varName = varName;
		this.valExp = valExp;
	}
	
	public void execute(OS os, HardwareThread thread, ALU alu) {
		Value val = alu.eval(valExp);

		thread.setInHeap(varName, val);
	}
} 
 
