package os.secondarymemory;

import hardware.SecondaryMemory.SecondaryMemory;

public class SecondaryMemoryDriver {
    protected SecondaryMemory secondaryMemory;

    public SecondaryMemoryDriver(SecondaryMemory secondaryMemory) {
        this.secondaryMemory = secondaryMemory;
    }

    public int readSector(int sectorInd) {
        return secondaryMemory.read(sectorInd);
    }

    public void writeSector(int sectorInd, int sectorData) {
        secondaryMemory.write(sectorInd, sectorData);
    }

    public int getSize() {
        return secondaryMemory.getSize();
    }

    public int[] getAllSectorsData() {
        int[] allSectorsData = new int[getSize()];
        for(int i = 0;i < allSectorsData.length; i++)
            allSectorsData[i] = readSector(i);
        return allSectorsData;
    }

}
