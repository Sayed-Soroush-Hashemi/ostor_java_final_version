package hardware.cpu.instruction.ipc.channel;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;

import hardware.cpu.instruction.Instruction;
import os.OS;

public class ReadFromChannelInstruction extends Instruction {
	protected String channelNameExp;
	protected String varName;

	public ReadFromChannelInstruction(String channelNameExp, String varName) {
		this.channelNameExp = channelNameExp;
		this.varName = varName;
	}
	
	public void execute(OS os, HardwareThread thread, ALU alu) {
		Value channelName = alu.eval(channelNameExp);

		if(channelName.isString() == false) {
			// TODO: throw exception
		}

		String channelNameStr = channelName.toString();

		int readMessage = os.readFromChannelSystemCall(thread, channelNameStr);

		Value readMessageVal = new Value(readMessage);

		thread.setInStack(varName, readMessageVal);
	}
} 
 
