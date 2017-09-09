package os;

public class OSBootParameters {
	protected int timer;
	protected String initProgramPath;
	protected int processLoadablePagesCount;
	protected String virtualMemorySchedulerName;

	public OSBootParameters(String initProgramPath, int timer, int processLoadablePagesCount, String virtualMemorySchedulerName) {
		this.timer = timer;
		this.initProgramPath = initProgramPath;
		this.processLoadablePagesCount = processLoadablePagesCount;
		this.virtualMemorySchedulerName = virtualMemorySchedulerName;
	}
	
	public int getTimer() { 
		return timer; 
	}
	
	public String getInitProgramPath() { 
		return initProgramPath; 
	}

	public int getProcessLoadablePagesCount() {
		return processLoadablePagesCount;
	}

	public String getVirtualMemorySchedulerName() {
		return virtualMemorySchedulerName;
	}
} 
