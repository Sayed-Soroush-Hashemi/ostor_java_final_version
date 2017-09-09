package hardware.cpu.instruction.secondarymemory;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;
import hardware.cpu.instruction.Instruction;
import os.OS;

import java.util.Arrays;

public class PrintFreeMapInstruction extends Instruction {

	public PrintFreeMapInstruction(){
		return;
	}
	
	public void execute(OS os, HardwareThread thread, ALU alu) {
		int[] freeMap = os.getFreeMapSystemCall(thread);

		String freeMapStr = Arrays.toString(freeMap);

		os.echoSystemCall(thread, freeMapStr);
	}
}
