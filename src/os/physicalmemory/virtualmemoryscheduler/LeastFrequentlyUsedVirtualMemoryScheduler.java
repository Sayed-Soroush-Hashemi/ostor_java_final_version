package os.physicalmemory.virtualmemoryscheduler;

import os.Thread;

import java.util.HashMap;
import java.util.Vector;

public class LeastFrequentlyUsedVirtualMemoryScheduler extends SimpleVirtualMemoryScheduler {

    protected HashMap<Thread, Vector<Integer>> useFrequency;
    protected HashMap<Thread, Vector<Integer>> loadingClock;


    public LeastFrequentlyUsedVirtualMemoryScheduler(int processLoadablePages) {
        super(processLoadablePages);
        useFrequency = new HashMap<>();
        loadingClock = new HashMap<>();
    }

    protected void loadProcess(Thread thread) {
        loadedPages.put(thread, new Vector<Integer>());
        loadingClock.put(thread, new Vector<Integer>());
        useFrequency.put(thread, new Vector<Integer>());
    }

    protected void useAlreadyLoadedPage(Thread thread, int pageNumber, int currentPageInd) {
        useFrequency.get(thread).set(currentPageInd, useFrequency.get(thread).get(currentPageInd)+1);
        loadingClock.get(thread).set(currentPageInd, currentClock);
    }

    protected void fillNextFreePage(Thread thread, int pageNumber) {
        loadedPages.get(thread).add(pageNumber);
        useFrequency.get(thread).add(1);
        loadingClock.get(thread).add(currentClock);
    }

    protected int findBestReplacementInd(Thread thread, int pageNumber) {
        int targetInd = 0;
        for(int i = 1; i < useFrequency.get(thread).size(); i++) {
            if(useFrequency.get(thread).get(i) < useFrequency.get(thread).get(targetInd)
                    || (useFrequency.get(thread).get(i) == useFrequency.get(thread).get(targetInd)
                    && loadingClock.get(thread).get(i) < loadingClock.get(thread).get(targetInd))) {
                targetInd = i;
            }
        }
        return targetInd;
    }

    protected void replaceNewPage(Thread thread, int newPageNumber, int replacementInd) {
        loadedPages.get(thread).set(replacementInd, newPageNumber);
        useFrequency.get(thread).set(replacementInd, 1);
        loadingClock.get(thread).set(replacementInd, currentClock);
    }

}
