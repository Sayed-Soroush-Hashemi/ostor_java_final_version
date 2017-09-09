package os;

public interface SoftwareThread {
	public void setProcess(Process process);
	public Process getProcess();

	public void setTId(int tId);
	public int getTId();

	public Status getStatus();
	public void setStatus(Status newStatus);
} 
