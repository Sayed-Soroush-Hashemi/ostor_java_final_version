import hardware.MotherBoard;
import os.OSBootParameters;

import java.io.FileNotFoundException;

/**
 * Created by soroush on 7/12/17.
 */
public class Main {

    public static void main(String... args) throws FileNotFoundException {
        int coreCount = 1;
        int timer = 50;
        int processFramesCount = 10;
        int processLoadablePagesCount = 3;
        String virtualMemorySchedulerName = "LRU";
        int secondaryMemorySize = 100;
        String initProgramPath = "init.ostor";

        // parse parameters
        for(int i = 0; i < args.length; i++) {
            if(args[i].equals("-cpu"))
                coreCount = Integer.valueOf(args[++i]);
            else if(args[i].equals("-timer"))
                timer = Integer.valueOf(args[++i]);
            else if(args[i].equals("-secmem"))
                secondaryMemorySize = Integer.valueOf(args[++i]);
            else if(args[i].equals("-mem")) {
                String[] temp = args[++i].split(":");
                processFramesCount = Integer.valueOf(temp[0]);
                processLoadablePagesCount = Integer.valueOf(temp[1]);
                virtualMemorySchedulerName = temp[2];
            }
            else
                initProgramPath = args[i];
        }

        OSBootParameters oSBootParameters = new OSBootParameters(initProgramPath, timer, processLoadablePagesCount, virtualMemorySchedulerName);
        MotherBoard motherBoard = new MotherBoard(coreCount, secondaryMemorySize, processFramesCount);
        motherBoard.powerOn(oSBootParameters);
    }
}
