package hardware.SecondaryMemory;

public class SecondaryMemory {
    protected int[] sectors;

    public SecondaryMemory(int size) {
        sectors = new int[size];
        for(int i = 0; i < size; i++)
            sectors[i] = -1;
    }

    public int read(int sectorInd) {
        return sectors[sectorInd];
    }

    public void write(int sectorInd, int sectorData) {
        sectors[sectorInd] = sectorData;
    }

    public int getSize() {
        return sectors.length;
    }

}
