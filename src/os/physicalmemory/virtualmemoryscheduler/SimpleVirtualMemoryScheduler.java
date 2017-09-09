package os.physicalmemory.virtualmemoryscheduler;

import os.Thread;

import java.util.HashMap;
import java.util.Vector;

public class SimpleVirtualMemoryScheduler implements VirtualMemoryScheduler {

    protected int processLoadablePages;
    protected int currentClock;
    protected HashMap<Thread, Vector<Integer>> loadedPages;

    public SimpleVirtualMemoryScheduler(int processLoadablePages) {
        this.processLoadablePages = processLoadablePages;
        currentClock = 0;
        loadedPages = new HashMap<>();
    }

    protected int findPageInLoadedTables(Thread thread, int pageNumber) {
        for(int i = 0; i < loadedPages.get(thread).size(); i++) {
            if(loadedPages.get(thread).get(i) == pageNumber) {
                return i;
            }
        }
        return -1;
    }

    protected void useAlreadyLoadedPage(Thread thread, int pageNumber, int currentPageInd) {
        return ;
    }

    protected boolean hasFreePages(Thread thread) {
        return loadedPages.get(thread).size() < processLoadablePages;
    }

    protected void fillNextFreePage(Thread thread, int pageNumber) {
        loadedPages.get(thread).add(pageNumber);
    }

    protected int findBestReplacementInd(Thread thread, int pageNumber) {
        return 0;
    }

    protected void replaceNewPage(Thread thread, int newPageNumber, int replacementInd) {
        loadedPages.get(thread).set(replacementInd, newPageNumber);
    }

    protected boolean isProcessLoaded(Thread thread) {
        return loadedPages.keySet().contains(thread);
    }

    protected void loadProcess(Thread thread) {
        loadedPages.put(thread, new Vector<Integer>());
    }

    public void loadPage(Thread thread, int pageNumber) {
        currentClock++;

        if(!isProcessLoaded(thread)) {
            loadProcess(thread);
        }

        int targetPageNumberInd = findPageInLoadedTables(thread, pageNumber);
        if(targetPageNumberInd != -1) {
            useAlreadyLoadedPage(thread, pageNumber, targetPageNumberInd);
            return ;
        }

        if(hasFreePages(thread)) {
            fillNextFreePage(thread, pageNumber);
        } else {
            int replacementInd = findBestReplacementInd(thread, pageNumber);
            replaceNewPage(thread, pageNumber, replacementInd);
        }
    }

    public int[] getLoadedPages(Thread thread) {
        Vector<Integer> temp = loadedPages.get(thread);
        if(temp == null)
            return new int[0];
        int[] loadedPages = new int[temp.size()];
        for(int i = 0; i < temp.size(); i++)
            loadedPages[i] = temp.get(i);
        return loadedPages;
    }

}
