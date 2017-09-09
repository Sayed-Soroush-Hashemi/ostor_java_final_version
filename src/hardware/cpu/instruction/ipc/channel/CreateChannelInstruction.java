package hardware.cpu.instruction.ipc.channel;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;

import hardware.cpu.instruction.Instruction;
import os.OS;

public class CreateChannelInstruction extends Instruction {
	protected String channelNameExp;

	public CreateChannelInstruction(String channelNameExp) {
		this.channelNameExp = channelNameExp;
	}
	
	public void execute(OS os, HardwareThread thread, ALU alu) {
		Value channelNameVal = alu.eval(channelNameExp);

		if(channelNameVal.isString() == false) {
			// TODO: throw exception
		}

		String channelName = channelNameVal.toString();

		os.createChannelSystemCall(thread, channelName);
	}
} 
 
