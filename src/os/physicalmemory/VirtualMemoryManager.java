package os.physicalmemory;

import hardware.physicalmemory.PhysicalMemory;
import os.Thread;
import os.physicalmemory.virtualmemoryscheduler.*;

public class VirtualMemoryManager {

    protected PhysicalMemory physicalMemory;
    protected int processLoadablePagesCount;
    protected VirtualMemoryScheduler virtualMemoryScheduler;

    public VirtualMemoryManager(PhysicalMemory physicalMemory, int processLoadablePagesCount, String virtualMemorySchedulerName) {
        this.physicalMemory = physicalMemory;
        this.processLoadablePagesCount = processLoadablePagesCount;

        if(virtualMemorySchedulerName.equals("MFU"))
            virtualMemoryScheduler = new MostFrequentlyUsedVirtualMemoryScheduler(processLoadablePagesCount);
        else if(virtualMemorySchedulerName.equals("LFU"))
            virtualMemoryScheduler = new LeastFrequentlyUsedVirtualMemoryScheduler(processLoadablePagesCount);
        else if(virtualMemorySchedulerName.equals("LRU"))
            virtualMemoryScheduler = new LeastRecentlyUsedVirtualMemoryScheduler(processLoadablePagesCount);
        else if(virtualMemorySchedulerName.equals("FIFO"))
            virtualMemoryScheduler = new FirstInFirstOutVirtualMemoryScheduler(processLoadablePagesCount);
        else {
            // TODO: throw exception: uknown virtual memory scheduler
        }
    }

    public void loadPage(Thread thread, int pageNumber) {
        virtualMemoryScheduler.loadPage(thread, pageNumber);
    }

    public int[] getLoadedPages(Thread thread) {
        return virtualMemoryScheduler.getLoadedPages(thread);
    }

}
