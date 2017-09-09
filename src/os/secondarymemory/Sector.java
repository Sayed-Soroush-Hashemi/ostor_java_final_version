package os.secondarymemory;

public class Sector {
    public static int SECTORSIZE = 1;
    public int[] data;

    public Sector() {
        data = new int[SECTORSIZE];
    }


}
