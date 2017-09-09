package os.physicalmemory.virtualmemoryscheduler;

import os.Thread;

/**
 * Created by soroush on 7/19/17.
 */
public interface VirtualMemoryScheduler {

    public void loadPage(Thread thread, int pageNumber);

    public int[] getLoadedPages(Thread thread);

}
