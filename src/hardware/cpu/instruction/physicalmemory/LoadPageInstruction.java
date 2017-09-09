package hardware.cpu.instruction.physicalmemory;

import hardware.cpu.ALU;
import hardware.cpu.HardwareThread;
import hardware.cpu.Value;
import hardware.cpu.instruction.Instruction;
import os.OS;

/**
 * Created by soroush on 7/19/17.
 */
public class LoadPageInstruction extends Instruction {
    protected String pageNumberExp;

    public LoadPageInstruction(String pageNumberExp) {
        this.pageNumberExp = pageNumberExp;
    }

    public void execute(OS os, HardwareThread thread, ALU alu) {
        Value pageNumberVal = alu.eval(pageNumberExp);
        int pageNumber = pageNumberVal.toInteger();

        os.loadPageSystemCall(thread, pageNumber);
    }
}
