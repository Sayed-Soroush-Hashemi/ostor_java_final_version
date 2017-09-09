package hardware.cpu.instruction.secondarymemory;

import hardware.cpu.ALU;
import hardware.cpu.HardwareThread;
import hardware.cpu.instruction.Instruction;
import os.OS;

import java.util.Arrays;

/**
 * Created by soroush on 7/11/17.
 */
public class PrintMapInstruction extends Instruction {

    public PrintMapInstruction() {}

    public void execute(OS os, HardwareThread thread, ALU alu) {
        int[] allSectorsData = os.readAllSecondaryMemorySectorsSystemCall(thread);
        String allSectorsStr = Arrays.toString(allSectorsData);
        os.echoSystemCall(thread, allSectorsStr);
    }
}
