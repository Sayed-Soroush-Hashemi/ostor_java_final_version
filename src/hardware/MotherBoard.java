package hardware;

import java.io.FileNotFoundException;

import hardware.SecondaryMemory.SecondaryMemory;
import hardware.cpu.CPU;
import hardware.physicalmemory.PhysicalMemory;
import hardware.screen.Screen;

import os.OS;
import os.OSBootParameters;

public class MotherBoard {
	protected CPU cpu;
	protected PhysicalMemory physicalMemory;
	protected Screen screen;
	protected SecondaryMemory secondaryMemory;

	public MotherBoard(int coreCount, int secondaryMemorySize, int processFramesCount) {
		plugInHardware(coreCount, processFramesCount, secondaryMemorySize);
	}

	protected void plugInHardware(int coreCount, int framesCount, int secondaryMemorySize) {
		cpu = new CPU(coreCount);
		physicalMemory = new PhysicalMemory(framesCount);
		screen = new Screen();
		secondaryMemory = new SecondaryMemory(secondaryMemorySize);
	}

	public void powerOn(OSBootParameters oSBootParameters) throws FileNotFoundException {
		// boot os
		bootOS(oSBootParameters);
		
		// now cores start executing commands
		cpu.run();
	}

	protected void bootOS(OSBootParameters oSBootParameters) throws FileNotFoundException {
		OS os = new OS(oSBootParameters);		

		cpu.setOS(os);

		os.setCPU(cpu);
		os.addScreenHardware(screen);
		os.addSecondaryMemoryHardware(secondaryMemory);
		os.addPhysicalMemory(physicalMemory, oSBootParameters.getProcessLoadablePagesCount(), oSBootParameters.getVirtualMemorySchedulerName());
	}
} 
