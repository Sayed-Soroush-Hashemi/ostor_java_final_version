package hardware.cpu.instruction.secondarymemory;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;
import hardware.cpu.instruction.Instruction;
import os.OS;

public class ReadFileInstruction extends Instruction {

	protected String blockIndInFileExp;
	protected String fileIdExp;
	protected String contentVarName;
	protected String EOFVarName;
	
	public ReadFileInstruction(String fileIdExp, String blockIndInFile, String contentVarName, String EOFVarName){
		this.fileIdExp = fileIdExp;
		this.blockIndInFileExp = blockIndInFile;
		this.contentVarName = contentVarName;
		this.EOFVarName = EOFVarName;
	}
	
	public void execute(OS os, HardwareThread thread, ALU alu) {
		Value fileIdVal = alu.eval(fileIdExp);
		int fileId = fileIdVal.toInteger();
		
		Value blockIndVal = alu.eval(blockIndInFileExp);
		int blockInd = blockIndVal.toInteger();

		int fileSize = os.getFileSizeSystemCall(thread, fileId);
		if(blockInd > fileSize - 1) {
			Value EOFVal = new Value(1);
			thread.setInStack(EOFVarName, EOFVal);
		} else {
			int content = os.readFileSystemCall(thread, fileId, blockInd);

			Value contentVal = new Value(content);
			thread.setInStack(contentVarName, contentVal);

			Value EOFVal = new Value(0);
			thread.setInStack(EOFVarName, EOFVal);
		}
	}
}
