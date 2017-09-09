package hardware.cpu.instruction.secondarymemory;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;
import hardware.cpu.instruction.Instruction;
import os.OS;

public class WriteBasicInstruction extends Instruction {

	protected String blockIndExp;
	protected String sector1ContentExp;
	protected String sector2ContentExp;
	
	public WriteBasicInstruction(String blockIndExp, String sector1ContentExp, String sector2ContentExp){
		this.blockIndExp = blockIndExp;
		this.sector1ContentExp = sector1ContentExp;
		this.sector2ContentExp = sector2ContentExp;
	}
	
	public void execute(OS os, HardwareThread thread, ALU alu) {
		
		Value blockIndVal = alu.eval(blockIndExp);
		int blockInd = blockIndVal.toInteger();

		Value sector1ContentVal = alu.eval(sector1ContentExp);
		int sector1Content = sector1ContentVal.toInteger();

		Value sector2ContentVal = alu.eval(sector2ContentExp);
		int sector2Content = sector2ContentVal.toInteger();

		int[] newBlockData = {sector1Content, sector2Content};
		
		os.writeBlockSystemCall(thread, blockInd, newBlockData);
	}
}
