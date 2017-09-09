package hardware.cpu.instruction.secondarymemory;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;
import hardware.cpu.instruction.Instruction;
import os.OS;

public class WriteOriginInstruction extends Instruction {

	protected String fileIdExp;
	protected String blockIndInFile;
	protected String sector1NewContentExp;
	protected String sector2NewContentExp;

	public WriteOriginInstruction(String fileIdExp, String blockIndInFile, String sector1NewContentExp, String sector2NewContentExp){
		this.fileIdExp = fileIdExp;
		this.blockIndInFile = blockIndInFile;
		this.sector1NewContentExp = sector1NewContentExp;
		this.sector2NewContentExp = sector2NewContentExp;
	}

	public void execute(OS os, HardwareThread thread, ALU alu) {
		Value fileIdVal = alu.eval(fileIdExp);
		int fileId = fileIdVal.toInteger();

		Value blockIndVal = alu.eval(blockIndInFile);
		int blockInd = blockIndVal.toInteger();

		Value sector1NewContentVal = alu.eval(sector1NewContentExp);
		Value sector2NewContentVal = alu.eval(sector2NewContentExp);
		int sector1NewContent = sector1NewContentVal.toInteger();
		int sector2NewContent = sector2NewContentVal.toInteger();
		int[] newBlockData = {sector1NewContent, sector2NewContent};

		os.writeBlockOfFileSystemCall(thread, fileId, blockInd, newBlockData);
	}
}