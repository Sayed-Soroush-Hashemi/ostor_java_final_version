package os;

import java.io.FileNotFoundException;
import java.util.Vector;
import java.util.HashMap;

import hardware.SecondaryMemory.SecondaryMemory;
import hardware.cpu.CPU;
import hardware.cpu.HardwareThread;
import hardware.physicalmemory.PhysicalMemory;
import os.physicalmemory.VirtualMemoryManager;
import os.scheduler.Scheduler;
import os.ipc.channel.Channel;
import os.screendriver.ScreenDriver;

import hardware.cpu.Value;
import hardware.cpu.Core;
import hardware.screen.Screen;
import os.secondarymemory.BasicSecondaryMemoryManager;
import os.secondarymemory.FileManager;
import os.secondarymemory.SecondaryMemoryDriver;

public class OS {
	protected Vector<Process> processes = new Vector<>();
	protected Vector<Thread> threads = new Vector<>();
	protected HashMap<String, Channel> channels = new HashMap<>();
	protected int nextPId = 1;
	protected int nextTId = 1;
	protected Scheduler scheduler;
	protected int coreTimerBound;

	protected ScreenDriver screenDriver;
	protected CPU cpu;
	protected VirtualMemoryManager virtualMemoryManager;
	protected SecondaryMemoryDriver secondaryMemoryDriver;
	protected BasicSecondaryMemoryManager basicSecondaryMemoryManager;
	protected FileManager fileManager;

	public OS(OSBootParameters oSBootParameters) throws FileNotFoundException {
		scheduler = new Scheduler();
		processes = new Vector<>();
		threads = new Vector<>();
		channels = new HashMap<>();
		nextPId = 1;
		nextTId = 1;
		coreTimerBound = oSBootParameters.getTimer();

		ProcessFactory processFactory = new ProcessFactory();
		Process initProcess = processFactory.create(oSBootParameters.getInitProgramPath());
		addProcessToOS(initProcess);
	}

	public void addPhysicalMemory(PhysicalMemory physicalMemory, int processLoadablePagesCount, String virtualMemorySchedulerName) {
		virtualMemoryManager = new VirtualMemoryManager(physicalMemory, processLoadablePagesCount, virtualMemorySchedulerName);
	}

	public void addScreenHardware(Screen screenHardware) {
		screenDriver = new ScreenDriver(screenHardware);
	}

	public void addSecondaryMemoryHardware(SecondaryMemory secondaryMemory) {
		secondaryMemoryDriver = new SecondaryMemoryDriver(secondaryMemory);
		basicSecondaryMemoryManager = new BasicSecondaryMemoryManager(secondaryMemoryDriver);
		fileManager = new FileManager(basicSecondaryMemoryManager);
	}

	public void setCPU(CPU cpu) {
		this.cpu = cpu;
		scheduler.setCPU(cpu);
	}

	public boolean isShuttingDown() {
		deadlockHandler();
		for(Thread thread: threads) 
			if(!thread.isTerminated())
				return false;
		return true;
	}

	public void interrupt(InterruptType interruptType, HardwareThread hardwareThread, Core core) {
		if(interruptType == InterruptType.IDLE)
			idleInterrupt(hardwareThread, core);
		else if(interruptType == InterruptType.OUTOFINSTRUCTION)
			outOfInstructionInterrupt(hardwareThread, core);
		else if(interruptType == InterruptType.WAITING)
			waitingInterrupt(hardwareThread, core);
		else if(interruptType == InterruptType.TIMER)
			timerInterrupt(hardwareThread, core);
		else if(interruptType == InterruptType.TERMINATED)
			terminationInterrupt(hardwareThread, core);
	}
	protected void runThreadOnCore(Thread thread, Core core) {
		core.setThread(thread);
		if(thread != null) {
			changeStatus(thread, Status.RUNNING);
			core.setTimer(thread.getPriority() * coreTimerBound);
		}
	}
	protected void outOfInstructionInterrupt(HardwareThread hardwareThread, Core core) {
		Thread thread = (Thread)hardwareThread;

		killThreadSystemCall(thread, thread.getTId());

		Thread nextThreadToRun = scheduler.getBestThreadToRun();
		runThreadOnCore(nextThreadToRun, core);
	}
	protected void idleInterrupt(HardwareThread hardwareThread, Core core) {
		Thread nextThreadToRun = scheduler.getBestThreadToRun();
		runThreadOnCore(nextThreadToRun, core);
	}
	protected void timerInterrupt(HardwareThread hardwareThread, Core core) {
		Thread thread = (Thread)hardwareThread;

		changeStatus(thread, Status.READY);

		Thread nextThreadToRun = scheduler.getBestThreadToRun();
		runThreadOnCore(nextThreadToRun, core);
	}
	protected void waitingInterrupt(HardwareThread hardwareThread, Core core) {
		Thread nextThreadToRun = scheduler.getBestThreadToRun();
		runThreadOnCore(nextThreadToRun, core);
	}
	protected void terminationInterrupt(HardwareThread hardwareThread, Core core) {
		Thread nextThreadToRun = scheduler.getBestThreadToRun();
		runThreadOnCore(nextThreadToRun, core);
	}

	public void addThreadToOS(Thread newThread) {
		newThread.setTId(nextTId++);
		threads.add(newThread);
		scheduler.addThread(newThread);
	}
	public void delThreadFromOS(Thread thread) {
		threads.remove(thread);
		scheduler.delThread(thread);
	}

	public void addProcessToOS(Process newProcess) {
		newProcess.setPId(nextPId++);
		processes.add(newProcess);
		for(Thread newThread: newProcess.getThreads())
			addThreadToOS(newThread);
	}
	public void delProcessFromOS(Process process) {
		processes.remove(process);
		for(Thread thread: process.getThreads())
			delThreadFromOS(thread);
	}

	public void makeThreadToWaitForExpected(Thread thread, Expected expected) {
		expected.addWaiter(thread);
		thread.setExpected(expected);
		changeStatus(thread, Status.WAITING);
	}

	protected void changeStatus(Thread thread, Status newStatus) {
		if(thread.getStatus() == newStatus)
			return ;

		if(thread.isTerminated())
			return ;

		if(thread.isWaiting() && newStatus == Status.READY)
			thread.setExpected(null);

		scheduler.update(thread, thread.getStatus(), newStatus);
		thread.setStatus(newStatus);
	}
	protected void killProcess(Process process) {
		for(Thread thread: process.getThreads()) {
			killJustThisThread(thread);
		}

		while(process.hasNextWaiter()) {
			Thread waiter = process.getNextWaiter();
			changeStatus(waiter, Status.READY);
		}

		delProcessFromOS(process);
	}
	protected void killJustThisThread(Thread thread) {
		if(thread.isTerminated())
			return ;
		
		changeStatus(thread, Status.TERMINATED);
		while(thread.hasNextWaiter()) {
			Thread waiter = thread.getNextWaiter();
			changeStatus(waiter, Status.READY);
		}

		delThreadFromOS(thread);
	}

	public void deadlockHandler() {
		Vector<Process> processesToKill = new Vector<>();
		for(Process process: processes) {
			boolean deadlockDetected = true;
			for(Thread thread: process.getThreads()) {
				if(!(thread.isWaiting() && Semaphore.class.isInstance(thread.getExpected()))) {
					deadlockDetected = false;
					break;
				}
			}
			if(deadlockDetected)
				processesToKill.add(process);
		}
		for(Process processToKill: processesToKill)
			killProcess(processToKill);
	}

	public Channel getChannel(String channelName) {
		// TODO: if doesn't exist, throw exception 
		return channels.get(channelName);
	}

	public Thread getThread(int tId) {
		for(Thread thread: threads) {
			if(thread.getTId() == tId)
				return thread;
		}
		// TODO: throw Exception
		return null;
	}

	public Process getProcess(int pId) {
		for(Process process: processes) {
			if(process.getPId() == pId)
				return process;
		}
		// TODO: throw Exception
		return null;
	}

	public boolean isProcessTerminated(Process process) {
		for(Thread thread: process.getThreads()) {
			if(thread.isTerminated() == false)
				return false;
		}
		return true;
	}

	public void echoSystemCall(HardwareThread hardwareThread, String x) {
		screenDriver.show(x);
	}
	
	public void createThreadSystemCall(HardwareThread hardwareThread, String varName) {
		Thread thread = (Thread)hardwareThread;

		ThreadFactory threadFactory = new ThreadFactory();
		Thread newThread = threadFactory.forkForCreateThread(thread);

		addThreadToOS(newThread);

		thread.setInStack(varName, new Value(newThread.getTId()));
		newThread.setInStack(varName, new Value(0));
	}
	public void killThreadSystemCall(HardwareThread hardwareThread, int threadId) {
		Thread targetThread = getThread(threadId);
		Process targetProcess = targetThread.getProcess();

		killJustThisThread(targetThread);
		
		if(isProcessTerminated(targetProcess))
			killProcess(targetProcess);
	}
	public void waitForThreadSystemCall(HardwareThread hardwareThread, int tId) {
		Thread thread = (Thread)hardwareThread;

		Thread targetThread = getThread(tId);

		// thread might have been terminated and deleted
		if(targetThread != null)
			makeThreadToWaitForExpected(thread, targetThread);
	}
	public void printThreadInfoSystemCall(HardwareThread hardwareThread) {
		Thread thread = (Thread)hardwareThread;

		String info = String.format("tid: %d\npid: %d", thread.getTId(), thread.getProcess().getPId());
		echoSystemCall(hardwareThread, info);
	}
	
	public void createProcessSystemCall(HardwareThread hardwareThread, String varName) {
		Thread thread = (Thread)hardwareThread;

		Process process = thread.getProcess();
		ProcessFactory processFactory = new ProcessFactory();
		Process newProcess = processFactory.fork(process);

		addProcessToOS(newProcess);

		process.setInHeap(varName, new Value(newProcess.getPId()));
		newProcess.setInHeap(varName, new Value(0));
	}
	public void killProcessSystemCall(HardwareThread hardwareThread, int pId) {
		Process targetProcess = getProcess(pId);

		killProcess(targetProcess);
	}
	public void waitForProcessSystemCall(HardwareThread hardwareThread, int pId) {
		Thread thread = (Thread)hardwareThread;

		Process targetProcess = getProcess(pId);

		// process might have been terminated and deleted
		if(targetProcess != null)
			makeThreadToWaitForExpected(thread, targetProcess);
	}
	public void printProcessInfoSystemCall(HardwareThread hardwareThread) {
		Thread thread = (Thread)hardwareThread;

		String info = String.format("pid: %d\nthreads: %d", thread.getProcess().getPId(), thread.getProcess().getThreads().size());
		echoSystemCall(hardwareThread, info);
	}

	public void execSystemCall(HardwareThread hardwareThread, String programPath) throws FileNotFoundException {
		Thread thread = (Thread)hardwareThread;
		Process process = thread.getProcess();

		ThreadFactory threadFactory = new ThreadFactory();
		Thread newThread = threadFactory.create(programPath);

		process.addThread(newThread);
		addThreadToOS(newThread);
		newThread.setProcess(process);

		for(Thread oldThread: process.getThreads()) {
			if(oldThread == newThread)
				continue;
			killJustThisThread(oldThread);
		}
	}

	public void createSemaphoreSystemCall(HardwareThread hardwareThread, String semaphoreName, int semaphoreValue) {
		Thread thread = (Thread)hardwareThread;

		Process process = thread.getProcess();
		Semaphore newSemaphore = new Semaphore(semaphoreValue);

		process.addSemaphore(semaphoreName, newSemaphore);
	}
	public void waitForSemaphoreSystemCall(HardwareThread hardwareThread, String semaphoreName) {
		Thread thread = (Thread)hardwareThread;

		Semaphore targetSemaphore = thread.getSemaphore(semaphoreName);

		if(targetSemaphore.hasCredit())
			targetSemaphore.decreaseCredit();
		else
			makeThreadToWaitForExpected(thread, targetSemaphore);
	}
	public void signalSemaphoreSystemCall(HardwareThread hardwareThread, String semaphoreName) {
		Thread thread = (Thread)hardwareThread;

		Semaphore targetSemaphore = thread.getSemaphore(semaphoreName);

		while(targetSemaphore.hasNextWaiter()) {
			Thread waiter = targetSemaphore.getNextWaiter();
			if(waiter.isWaiting() == false)
				continue;
			changeStatus(waiter, Status.READY);
			return ;
		}
		targetSemaphore.increaseCredit();
	}

	public void createChannelSystemCall(HardwareThread hardwareThread, String channelName) {
		Channel newChannel = new Channel();

		channels.put(channelName, newChannel);
	}
	public void writeInChannelSystemCall(HardwareThread hardwareThread, String channelName, Integer message) {
		Channel targetChannel = getChannel(channelName);

		targetChannel.write(message);
	}
	public int readFromChannelSystemCall(HardwareThread hardwareThread, String channelName) {
		Channel targetChannel = getChannel(channelName);

		return targetChannel.read();
	}

	public void setPrioritySystemCall(HardwareThread hardwareThread, int newPriority) {
		Thread thread = (Thread)hardwareThread;

		thread.setPriority(newPriority);
	}

	////////////////////////////////////////////////////////////////////////////////////
	//		physical memory system calls
	////////////////////////////////////////////////////////////////////////////////////

	public void loadPageSystemCall(HardwareThread thread, int pageNumber) {
		virtualMemoryManager.loadPage((Thread)thread, pageNumber);
	}

	public int[] getLoadedPagesSystemCall(HardwareThread thread) {
		return virtualMemoryManager.getLoadedPages((Thread)thread);
	}

	////////////////////////////////////////////////////////////////////////////////////
	//		secondary memory system calls
	////////////////////////////////////////////////////////////////////////////////////

	public int readSectorSystemCall(HardwareThread hardwareThread, int sectorInd) {
		return secondaryMemoryDriver.readSector(sectorInd);
	}

	public void writeSectorSystemCall(HardwareThread hardwareThread, int sectorInd, int newSectorData) {
		secondaryMemoryDriver.writeSector(sectorInd, newSectorData);
	}

	public int[] readBlockSystemCall(HardwareThread hardwareThread, int blockInd) {
		return basicSecondaryMemoryManager.readBlock(blockInd);
	}

	public void writeBlockSystemCall(HardwareThread hardwareThread, int blockInd, int[] newBlockDat) {
		basicSecondaryMemoryManager.writeBlock(blockInd, newBlockDat);
	}

	public int[] readBlockOfFileSystemCall(HardwareThread hardwareThread, int fileId, int blockInd) {
		return fileManager.readBlock(fileId, blockInd);
	}

	public void writeBlockOfFileSystemCall(HardwareThread hardwareThread, int fileId, int blockIndInFile, int[] newBlockData) {
		fileManager.writeBlock(fileId, blockIndInFile, newBlockData);
	}

	public void createFileSystemCall(HardwareThread hardwareThread, int fileName) {
		fileManager.createFile(fileName);
	}

	public int openFileSystemCall(HardwareThread hardwareThread, int fileName) {
		return fileManager.openFile(fileName);
	}

	public int getFileSizeSystemCall(HardwareThread hardwareThread, int fileId) {
		return fileManager.getFileSize(fileId);
	}

	public int readFileSystemCall(HardwareThread hardwareThread, int fileId, int blockIndInFile) {
		return fileManager.readContent(fileId, blockIndInFile);
	}

	public void appendToFileSystemCall(HardwareThread hardwareThread, int fileId, int newContent) {
		fileManager.append(fileId, newContent);
	}

	public void writeInFileSystemCall(HardwareThread hardwareThread, int fileId, int blockIndInFile, int newContent) {
		fileManager.writeContent(fileId, blockIndInFile, newContent);
	}

	public void clearFileSystemCall(HardwareThread hardwareThread, int fileId) {
		fileManager.clearFile(fileId);
	}

	public void deleteFileSystemCall(HardwareThread hardwareThread, int fileName) {
		fileManager.deleteFile(fileName);
	}

	public int[] readAllSecondaryMemorySectorsSystemCall(HardwareThread hardwareThread) {
		return secondaryMemoryDriver.getAllSectorsData();
	}

	public HashMap<Integer, Vector<Integer>> getFileMapSystemCall(HardwareThread hardwareThread) {
		return fileManager.getFileMap();
	}

	public int[] getFreeMapSystemCall(HardwareThread hardwareThread) {
		return fileManager.getFreeBlocksList();
	}
}
