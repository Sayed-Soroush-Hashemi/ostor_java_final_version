package os;

public interface Expected {
	public void addWaiter(Thread thread);
	public Thread getNextWaiter();
	public boolean hasNextWaiter();
} 
