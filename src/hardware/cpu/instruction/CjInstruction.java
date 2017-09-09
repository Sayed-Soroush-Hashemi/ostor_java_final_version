package hardware.cpu.instruction;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;

import os.OS;

public class CjInstruction extends Instruction {
	protected String actualExp;
	protected String expectedExp;
	protected String jumpCountExp;

	public CjInstruction(String actualExp, String expectedExp, String jumpCountExp) {
		this.actualExp = actualExp;
		this.expectedExp = expectedExp;
		this.jumpCountExp = jumpCountExp;
	}

	public void execute(OS os, HardwareThread thread, ALU alu) {
		Value actualVal = alu.eval(actualExp);
		Value expectedVal = alu.eval(expectedExp);
		Value jumpCountVal = alu.eval(jumpCountExp);

		if(!jumpCountVal.isInteger()) {
			// TODO: throw exception
		}

		int jumpCount = jumpCountVal.toInteger();

		if(actualVal.isEqual(expectedVal))
			thread.setPC(thread.getPC() + jumpCount);

	}
}
