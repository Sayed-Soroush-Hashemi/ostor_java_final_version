package hardware.cpu.instruction.secondarymemory;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;
import hardware.cpu.instruction.Instruction;
import os.OS;

public class ClearFileInstruction extends Instruction {

	protected String fileIdExp;
	
	public ClearFileInstruction(String fileIdExp){
		this.fileIdExp = fileIdExp;
	}
	
	public void execute(OS os, HardwareThread thread, ALU alu) {
		
		Value fileIdVal = alu.eval(fileIdExp);
		int fileId = fileIdVal.toInteger();
		
		os.clearFileSystemCall(thread, fileId);
	}
}
