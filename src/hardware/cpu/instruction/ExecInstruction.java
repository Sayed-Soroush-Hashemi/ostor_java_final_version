package hardware.cpu.instruction;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;

import os.OS;

import java.io.FileNotFoundException;

public class ExecInstruction extends Instruction {
	protected String programPathExp;

	public ExecInstruction(String programPath) {
		this.programPathExp = programPath;
	}
	
	public void execute(OS os, HardwareThread thread, ALU alu) {
		Value programPathVal = alu.eval(programPathExp);

		if(programPathVal.isString() == false) {
			// TODO: throw exception
		}

		String programPath = programPathVal.toString();

		try {
			os.execSystemCall(thread, programPath);
		} catch (FileNotFoundException e) {
			// TODO: throw exception
		}
	}
} 
 
