package hardware.cpu.instruction.secondarymemory;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;
import hardware.cpu.instruction.Instruction;
import os.OS;

import java.util.Arrays;

public class SizeInstruction extends Instruction {

	protected String fileIdExp;
	protected String varName;

	public SizeInstruction(String fileIdExp, String varName) {
		this.fileIdExp = fileIdExp;
		this.varName = varName;
	}

	public void execute(OS os, HardwareThread thread, ALU alu) {
		Value fileIdVal = alu.eval(fileIdExp);
		int fileId = fileIdVal.toInteger();

		int fileSize = os.getFileSizeSystemCall(thread, fileId);
		Value fileSizeVal = new Value(fileSize);
		thread.setInStack(varName, fileSizeVal);
	}
}
