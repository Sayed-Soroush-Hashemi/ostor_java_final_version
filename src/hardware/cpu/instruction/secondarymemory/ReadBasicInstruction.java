package hardware.cpu.instruction.secondarymemory;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;
import hardware.cpu.instruction.Instruction;
import os.OS;

public class ReadBasicInstruction extends Instruction {

	protected String blockIndExp;
	protected String sector1VarName;
	protected String sector2VarName;
	
	public ReadBasicInstruction(String blockIndExp, String sector1VarName, String sector2VarName){
		this.blockIndExp = blockIndExp;
		this.sector1VarName = sector1VarName;
		this.sector2VarName = sector2VarName;
	}
	
	public void execute(OS os, HardwareThread thread, ALU alu) {
		Value blockIndVal = alu.eval(blockIndExp);
		int blockInd = blockIndVal.toInteger();

		int[] blockData = os.readBlockSystemCall(thread, blockInd);

		Value sector1Val = new Value(blockData[0]);
		thread.setInStack(sector1VarName, sector1Val);

		Value sector2Val = new Value(blockData[1]);
		thread.setInStack(sector2VarName, sector2Val);
	}
}
