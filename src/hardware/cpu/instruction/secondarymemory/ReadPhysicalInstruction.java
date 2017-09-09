package hardware.cpu.instruction.secondarymemory;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;
import hardware.cpu.instruction.Instruction;
import os.OS;

public class ReadPhysicalInstruction extends Instruction {
	
	protected String sectorIndExp;
	protected String varName;
	
	public ReadPhysicalInstruction(String sectorIndExp, String varName){
		this.sectorIndExp = sectorIndExp;
		this.varName = varName;
	}
	
	public void execute(OS os, HardwareThread thread, ALU alu){
		
		Value sectorIndVal = alu.eval(sectorIndExp);
		int sectorInd = sectorIndVal.toInteger();
		
		int sectorData = os.readSectorSystemCall(thread, sectorInd);

		Value sectorValue = new Value(sectorData);
		thread.setInStack(varName , sectorValue);

		return ;
	}
}
