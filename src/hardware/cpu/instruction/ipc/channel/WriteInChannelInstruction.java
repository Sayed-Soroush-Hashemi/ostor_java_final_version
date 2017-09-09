package hardware.cpu.instruction.ipc.channel;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;

import hardware.cpu.instruction.Instruction;
import os.OS;

public class WriteInChannelInstruction extends Instruction {
	protected String channelNameExp;
	protected String messageExp;

	public WriteInChannelInstruction(String channelNameExp, String messageExp) {
		this.channelNameExp = channelNameExp;
		this.messageExp = messageExp;
	}

	public void execute(OS os, HardwareThread thread, ALU alu) {
		Value channelName = alu.eval(channelNameExp);
		Value message = alu.eval(messageExp);

		String channelNameStr = channelName.toString();
		int messageStr = message.toInteger();
		
		os.writeInChannelSystemCall(thread, channelNameStr, messageStr);
	}
} 
