package hardware.cpu.instruction.secondarymemory;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;
import hardware.cpu.instruction.Instruction;
import os.OS;

public class DeleteFileInstruction extends Instruction {

	protected String fileNameExp;
	
	public DeleteFileInstruction(String fileNameExp){
		this.fileNameExp = fileNameExp;
	}
	
	public void execute(OS os, HardwareThread thread, ALU alu) {
		Value fileNameVal = alu.eval(fileNameExp);
		int fileName = fileNameVal.toInteger();
		os.deleteFileSystemCall(thread, fileName);
	}
}

