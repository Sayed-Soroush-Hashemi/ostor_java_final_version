package hardware.cpu;

import java.util.Vector;

import os.OS;

public class CPU {
	protected Vector<Core> cores;
	protected OS os;

	protected int totalInstructionsExecuted;

	public CPU(int coreCount) {
		cores = new Vector<Core>();
		for(int i = 0; i < coreCount; i++) 
			cores.add(new Core());

		totalInstructionsExecuted = 0;
	}

	public void setOS(OS os) {
		this.os = os;
		for(Core core: cores) 
			core.setOS(os);
	}

	public int getTotalInstructionsExecuted() {
		return this.totalInstructionsExecuted;
	}

	public void run() {
		while(os.isShuttingDown() == false) {
			for(Core currentCore: cores) {
				totalInstructionsExecuted += currentCore.execute();
			}
		}
	}
} 
