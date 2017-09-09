package hardware.cpu.instruction.secondarymemory;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;
import hardware.cpu.instruction.Instruction;
import os.OS;

public class WritePhysicalInstruction extends Instruction {
	protected String sectorIndExp;
	protected String newSectorDataExp;

	public WritePhysicalInstruction(String sectorIndExp, String newSectorDataExp){
		this.sectorIndExp = sectorIndExp;
		this.newSectorDataExp = newSectorDataExp;
	}
	
	public void execute(OS os, HardwareThread thread, ALU alu){
		
		Value sectorIndVal = alu.eval(sectorIndExp);
		int sectorInd = sectorIndVal.toInteger();

		Value newSectorValue = alu.eval(newSectorDataExp);
		int newSectorData = newSectorValue.toInteger();
		
		os.writeSectorSystemCall(thread, sectorInd, newSectorData);
	}
}
