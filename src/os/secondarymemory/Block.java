package os.secondarymemory;

public class Block {
    public static int BLOCKSIZE = 1;
    protected Cluster[] clusters;

    public Block(Cluster[] clusters) {
        clusters = clusters;
    }

    public int getClusterNumber(int ind) {
        return ind / Cluster.CLUSTERSIZE;
    }
    public int getIndexInCluster(int ind) {
        return ind % Cluster.CLUSTERSIZE;
    }

    public int read(int ind) {
        return -1;
    }
}
