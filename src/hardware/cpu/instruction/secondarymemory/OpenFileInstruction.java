package hardware.cpu.instruction.secondarymemory;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;
import hardware.cpu.instruction.Instruction;
import os.OS;

public class OpenFileInstruction extends Instruction {

	protected String fileNameExp;
	protected String varName;
	
	public OpenFileInstruction(String fileNameExp, String varName){
		this.fileNameExp = fileNameExp;
		this.varName = varName;
	}
	
	public void execute(OS os, HardwareThread thread, ALU alu) {
		Value fileNameVal = alu.eval(fileNameExp);
		int fileName = fileNameVal.toInteger();

		int fileId = os.openFileSystemCall(thread , fileName);
		Value fileIdVal = new Value(fileId);
		thread.setInStack(varName, fileIdVal);
	}
}
