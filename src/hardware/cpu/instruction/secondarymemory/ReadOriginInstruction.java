package hardware.cpu.instruction.secondarymemory;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;
import hardware.cpu.instruction.Instruction;
import os.OS;

public class ReadOriginInstruction extends Instruction {

	protected String fileIdExp;
	protected String blockIndExp;
	protected String sector1VarName;
	protected String sector2VarName;
	
	public ReadOriginInstruction(String fileIdExp, String blockIndExp, String sector1VarName, String sector2VarName){
		this.fileIdExp = fileIdExp;
		this.blockIndExp = blockIndExp;
		this.sector1VarName = sector1VarName;
		this.sector2VarName = sector2VarName;
	}
	
	public void execute(OS os, HardwareThread thread, ALU alu) {
		Value fileIdVal = alu.eval(fileIdExp);
        int fileId = fileIdVal.toInteger();
		
		Value blockIndVal = alu.eval(blockIndExp);
		int blockInd = blockIndVal.toInteger();
		
		int[] blockData = os.readBlockOfFileSystemCall(thread, fileId, blockInd);

		Value sector1Val = new Value(blockData[0]);
		thread.setInStack(sector1VarName, sector1Val);

		Value sector2Val = new Value(blockData[1]);
		thread.setInStack(sector2VarName, sector2Val);
	}
}
