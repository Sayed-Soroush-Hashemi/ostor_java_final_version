package os.secondarymemory;

public class Cluster {
    public static int CLUSTERSIZE = 2;
    protected int[] data = new int[2];

    public Cluster(int[] data) {
        this.data = new int[data.length];
        for(int i = 0; i < data.length; i++)
            this.data[i] = data[i];
    }

    public int getSize() {
        return CLUSTERSIZE;
    }

    public int read(int ind) {
        return data[ind];
    }

    public void write(int ind, int data) {
        this.data[ind] = data;
    }
}
