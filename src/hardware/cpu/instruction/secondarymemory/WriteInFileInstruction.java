package hardware.cpu.instruction.secondarymemory;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;
import hardware.cpu.instruction.Instruction;
import os.OS;

public class WriteInFileInstruction extends Instruction {

	protected String fileIdExp;
	protected String blockIndInFileExp;
	protected String newContentExp;
	
	public WriteInFileInstruction(String fileIdExp, String blockIndInFileExp, String newContentExp){
		this.fileIdExp = fileIdExp;
		this.blockIndInFileExp = blockIndInFileExp;
		this.newContentExp = newContentExp;
	}
	
	public void execute(OS os, HardwareThread thread, ALU alu) {
		Value fileIdVal = alu.eval(fileIdExp);
		int fileId = fileIdVal.toInteger();

		Value blockIndVal = alu.eval(blockIndInFileExp);
		int blockInd = blockIndVal.toInteger();

		Value newContentVal = alu.eval(newContentExp);
		int newContent = newContentVal.toInteger();
		
		os.writeInFileSystemCall(thread, blockInd, fileId, newContent);
	}
}
