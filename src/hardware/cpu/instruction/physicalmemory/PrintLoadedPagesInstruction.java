package hardware.cpu.instruction.physicalmemory;

import hardware.cpu.ALU;
import hardware.cpu.HardwareThread;
import hardware.cpu.instruction.Instruction;
import os.OS;

import java.util.Arrays;

public class PrintLoadedPagesInstruction extends Instruction {

    public PrintLoadedPagesInstruction() {

    }

    public void execute(OS os, HardwareThread thread, ALU alu) {
        int[] loadedPages = os.getLoadedPagesSystemCall(thread);
        String loadedPagesStr = Arrays.toString(loadedPages);

        os.echoSystemCall(thread, loadedPagesStr);
    }
}
