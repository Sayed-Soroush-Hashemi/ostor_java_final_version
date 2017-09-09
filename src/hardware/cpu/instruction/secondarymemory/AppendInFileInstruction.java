package hardware.cpu.instruction.secondarymemory;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;
import hardware.cpu.instruction.Instruction;
import os.OS;

public class AppendInFileInstruction extends Instruction {
	
	protected String fileIdExp;
	protected String newContentExp;
	
	public AppendInFileInstruction(String fileIdExp, String newContentExp) {
		this.fileIdExp = fileIdExp;
		this.newContentExp = newContentExp;
	}
	
	public void execute(OS os, HardwareThread thread, ALU alu) {
		Value fileIdVal = alu.eval(fileIdExp);
		int fileId = fileIdVal.toInteger();

		Value newContentVal = alu.eval(newContentExp);
		int newContent = newContentVal.toInteger();

		os.appendToFileSystemCall(thread, fileId, newContent);
	}
}
