package os.physicalmemory.virtualmemoryscheduler;

import os.Thread;

import java.util.HashMap;
import java.util.Vector;

public class FirstInFirstOutVirtualMemoryScheduler extends SimpleVirtualMemoryScheduler {

    protected HashMap<Thread, Vector<Integer>> loadingClock;

    public FirstInFirstOutVirtualMemoryScheduler(int processLoadablePages) {
        super(processLoadablePages);
        this.processLoadablePages = processLoadablePages;
        currentClock = 0;
        loadedPages = new HashMap<>();
        loadingClock = new HashMap<>();
    }

    protected void fillNextFreePage(Thread thread, int pageNumber) {
        loadedPages.get(thread).add(pageNumber);
        loadingClock.get(thread).add(currentClock);
    }

    protected void loadProcess(Thread thread) {
        loadedPages.put(thread, new Vector<Integer>());
        loadingClock.put(thread, new Vector<Integer>());
    }

    protected int findBestReplacementInd(Thread thread, int pageNumber) {
        int targetInd = 0;
        for(int i = 1; i < loadingClock.get(thread).size(); i++) {
            if(loadingClock.get(thread).get(i) < loadingClock.get(thread).get(targetInd)) {
                targetInd = i;
            }
        }
        return targetInd;
    }

    protected void replaceNewPage(Thread thread, int newPageNumber, int replacementInd) {
        loadedPages.get(thread).set(replacementInd, newPageNumber);
        loadingClock.get(thread).set(replacementInd, currentClock);
    }
}
