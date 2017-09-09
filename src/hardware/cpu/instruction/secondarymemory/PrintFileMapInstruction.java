package hardware.cpu.instruction.secondarymemory;

import hardware.cpu.HardwareThread;
import hardware.cpu.ALU;
import hardware.cpu.Value;
import hardware.cpu.instruction.Instruction;
import os.OS;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

public class PrintFileMapInstruction extends Instruction {
	
	public PrintFileMapInstruction(){
		return;
	}

	public void execute(OS os, HardwareThread thread, ALU alu) {
		HashMap<Integer, Vector<Integer>> fileMap = os.getFileMapSystemCall(thread);

		String fileMapStr = "";
		Object[] fileIds = fileMap.keySet().toArray();
		for(int i = 0; i < fileIds.length; i++) {
			int curFileId = (int)fileIds[i];
			Object[] tempFileMap = fileMap.get(curFileId).toArray();
			Integer[] curFileMap = new Integer[tempFileMap.length];
			for(int j = 0; j < tempFileMap.length; j++)
				curFileMap[j] = (Integer)tempFileMap[j];

			String curFileMapStr = "" + fileIds[i] + "[" + curFileMap.length + "]: ";

			String temp = Arrays.toString(curFileMap);
			temp = temp.substring(1, temp.length()-2);

			curFileMapStr += temp;

			fileMapStr += curFileMapStr + "\n";
		}

		os.echoSystemCall(thread, fileMapStr);
	}
}
