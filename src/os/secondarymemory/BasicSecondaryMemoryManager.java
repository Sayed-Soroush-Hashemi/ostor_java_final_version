package os.secondarymemory;

public class BasicSecondaryMemoryManager {
    protected SecondaryMemoryDriver secondaryMemoryDriver;

    public BasicSecondaryMemoryManager(SecondaryMemoryDriver secondaryMemoryDriver) {
        this.secondaryMemoryDriver = secondaryMemoryDriver;
    }

    public int[] readBlock(int blockInd) {
        int[] blockData = new int[2];

        int sector1Ind = 2*blockInd;
        int sector2Ind = 2*blockInd + 1;

        blockData[0] = secondaryMemoryDriver.readSector(sector1Ind);
        blockData[1] = secondaryMemoryDriver.readSector(sector2Ind);

        return blockData;
    }

    public void writeBlock(int blockInd, int[] blockData) {
        int sector1Ind = 2*blockInd;
        int sector2Ind = 2*blockInd + 1;

        secondaryMemoryDriver.writeSector(sector1Ind, blockData[0]);
        secondaryMemoryDriver.writeSector(sector2Ind, blockData[1]);
    }

    public int getSize() {
        return secondaryMemoryDriver.getSize() / 2;
    }
}
