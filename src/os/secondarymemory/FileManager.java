package os.secondarymemory;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

public class FileManager {
    protected BasicSecondaryMemoryManager basicSecondaryMemoryManager;
    protected HashMap<Integer, Integer> fileNameToFileId;
    protected HashMap<Integer, Integer> fileIdToInodeNumber;
    protected Vector<Integer> freeBlocks;
    protected int nextFileId;
    protected int inodeTableSize;

    public FileManager(BasicSecondaryMemoryManager basicSecondaryMemoryManager) {
        this.basicSecondaryMemoryManager = basicSecondaryMemoryManager;

        nextFileId = 0;
        fileNameToFileId = new HashMap<>();
        
        inodeTableSize = 10;
        fileIdToInodeNumber = new HashMap<>();

        freeBlocks = new Vector<>();
        for(int i = inodeTableSize; i < basicSecondaryMemoryManager.getSize(); i++)
            freeBlocks.add(i);
    }

    public void createFile(int fileName) {
        // if file table is full, so we can't open another file
        if(fileNameToFileId.size() >= inodeTableSize) {
            // TODO: throw exception
            return;
        }

        int newFileId = nextFileId;
        nextFileId++;

        fileNameToFileId.put(fileName, newFileId);

        int[] newInodeContent = {newFileId, -1};
        for(int i = 0; i < inodeTableSize; i++) {
            int[] currentInodeContent = basicSecondaryMemoryManager.readBlock(i);
            int currentFileId = currentInodeContent[0];
            if(currentFileId == -1) {
                basicSecondaryMemoryManager.writeBlock(i, newInodeContent);
                fileIdToInodeNumber.put(newFileId, i);
                break;
            }
        }
    }

    public int openFile(int fileName) {
        return fileNameToFileId.get(fileName);
    }

    public void clearFile(int fileId) {
        int inodeNumber = fileIdToInodeNumber.get(fileId);
        int[] inodeContent = basicSecondaryMemoryManager.readBlock(inodeNumber);

        Vector<Integer> newEmptyBlocks = new Vector<Integer>();

        int currentBlockInd = inodeContent[1];
        while(currentBlockInd != -1) {
            newEmptyBlocks.add(currentBlockInd);

            int[] blockData = basicSecondaryMemoryManager.readBlock(currentBlockInd);
            int nextBlockInd = blockData[1];

            int[] newBlockData = {-1, -1};
            basicSecondaryMemoryManager.writeBlock(currentBlockInd, newBlockData);

            currentBlockInd = nextBlockInd;
        }

        for(int i = newEmptyBlocks.size() - 1; i >= 0; i--) {
            int newEmptyBlockInd = newEmptyBlocks.get(i);
            freeBlocks.insertElementAt(newEmptyBlockInd, 0);
        }

        return;
    }

    public void deleteFile(int fileName) {
        int fileId = fileNameToFileId.get(fileName);
        clearFile(fileId);

        int inodeNumber = fileIdToInodeNumber.get(fileId);

        int[] newInodeContent = {-1, -1};
        basicSecondaryMemoryManager.writeBlock(inodeNumber, newInodeContent);

        fileNameToFileId.remove(fileName);
        fileIdToInodeNumber.remove(fileId);

        return ;
    }

    public void append(int fileId, int newContent) {
        int newBlockInd = freeBlocks.get(0);
        freeBlocks.remove(0);

        // write new block of file
        int[] newBlockData = {newContent, -1};
        basicSecondaryMemoryManager.writeBlock(newBlockInd, newBlockData);

        int inodeNumber = fileIdToInodeNumber.get(fileId);
        int[] inodeContent = basicSecondaryMemoryManager.readBlock(inodeNumber);

        int currentBlockInd = inodeNumber;
        int nextBlockInd = inodeContent[1];
        while(nextBlockInd != -1) {
            int[] blockData = basicSecondaryMemoryManager.readBlock(nextBlockInd);
            currentBlockInd = nextBlockInd;
            nextBlockInd = blockData[1];
        }

        int[] lastBlockData = basicSecondaryMemoryManager.readBlock(currentBlockInd);
        int lastBlockContent = lastBlockData[0];
        int[] updatedLastBlockData = {lastBlockContent, newBlockInd};
        basicSecondaryMemoryManager.writeBlock(currentBlockInd, updatedLastBlockData);

        return ;
    }

    public int[] getFreeBlocksList() {
        int[] ret = new int[freeBlocks.size()];
        for(int i = 0; i < freeBlocks.size(); i++)
            ret[i] = freeBlocks.get(i);
        return ret;
    }

    public HashMap<Integer, Vector<Integer>> getFileMap() {
        HashMap<Integer, Vector<Integer>> ret = new HashMap<>();

        Object[] fileIds = fileIdToInodeNumber.keySet().toArray();
        for(int i = 0; i < fileIds.length; i++) {
            int curFileId = (int)fileIds[i];
            Vector<Integer> curFileMap = new Vector<>();

            int inodeNumber = fileIdToInodeNumber.get(curFileId);
            int[] inodeContent = basicSecondaryMemoryManager.readBlock(inodeNumber);

            int currentBlockInd = inodeContent[1];
            while(currentBlockInd != -1) {
                curFileMap.add(currentBlockInd);
                int[] blockData = basicSecondaryMemoryManager.readBlock(currentBlockInd);
                currentBlockInd = blockData[1];
            }

            ret.put(curFileId, curFileMap);
        }

        return ret;
    }

    public int[] readBlock(int fileId, int blockIndInFile) {
        int inodeNumber = fileIdToInodeNumber.get(fileId);
        int[] inodeContent = basicSecondaryMemoryManager.readBlock(inodeNumber);

        int currentBlockInd = inodeContent[1];
        while(blockIndInFile > 0) {
            int[] blockData = basicSecondaryMemoryManager.readBlock(currentBlockInd);
            currentBlockInd = blockData[1];
            blockIndInFile --;
        }
        int[] blockData = basicSecondaryMemoryManager.readBlock(currentBlockInd);

        return blockData;
    }

    public void writeBlock(int fileId, int blockIndInFile, int[] newBlockData) {
        int inodeNumber = fileIdToInodeNumber.get(fileId);
        int[] inodeContent = basicSecondaryMemoryManager.readBlock(inodeNumber);

        int currentBlockInd = inodeContent[1];
        while(blockIndInFile > 0) {
            int[] blockData = basicSecondaryMemoryManager.readBlock(currentBlockInd);
            currentBlockInd = blockData[1];
            blockIndInFile--;
        }
        basicSecondaryMemoryManager.writeBlock(currentBlockInd, newBlockData);

        return ;
    }

    public int readContent(int fileId, int blockIndInFile) {
        int[] blockData = readBlock(fileId, blockIndInFile);
        int blockContent = blockData[0];
        return blockContent;
    }

    public void writeContent(int fileId, int blockIndInFile, int newContent) {
        int[] blockData = readBlock(fileId, blockIndInFile);
        int[] newBlockData = {newContent, blockData[1]};
        writeBlock(fileId, blockIndInFile, newBlockData);

        return ;
    }

    public int getFileSize(int fileId) {
        int inodeNumber = fileIdToInodeNumber.get(fileId);
        int[] inodeContent = basicSecondaryMemoryManager.readBlock(inodeNumber);

        int fileSize = 0;
        int currentBlockInd = inodeContent[0];
        while(currentBlockInd != -1) {
            fileSize++;
            int[] blockData = basicSecondaryMemoryManager.readBlock(currentBlockInd);
            currentBlockInd = blockData[1];
        }

        return fileSize + 1;
    }

}
