package hardware.cpu;

import hardware.cpu.instruction.Instruction;
import os.InterruptType;

import os.OS;

public class Core {
	protected int timer;
	protected HardwareThread thread;
	protected ALU alu;
	protected OS os;

	public Core() {
		alu = new ALU();
	}

	public void setThread(HardwareThread thread) {
		this.thread = thread;
		alu.setThread(thread);
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public void setOS(OS os) {
		this.os = os;
	}

	public int execute() {
		if(thread == null) {
			os.interrupt(InterruptType.IDLE, null, this);
		} else if(thread.isTerminated()) {
			os.interrupt(InterruptType.TERMINATED, thread, this);
		} else if(thread.isWaiting()) {
			os.interrupt(InterruptType.WAITING, thread, this);
		} else if(timer == 0) {
			os.interrupt(InterruptType.TIMER, thread, this);
		}
		while(thread != null && thread.isOutOfInstructions())
			os.interrupt(InterruptType.OUTOFINSTRUCTION, thread, this);

		if(thread == null || thread.isRunning() == false)
			return 0;

		Instruction nextInstruction = thread.getNextInstruction();
		nextInstruction.execute(os, thread, alu);
		timer--;
		return 1;
	}
} 
