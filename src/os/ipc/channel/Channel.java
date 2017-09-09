package os.ipc.channel;

import java.util.Queue;
import java.util.LinkedList;

public class Channel {
	protected Queue<Integer> messages;

	public Channel() {
		messages = new LinkedList<Integer>();
	}

	public int read() {
		if(messages.size() == 0)
			return -1;
		return messages.poll();
	}

	public void write(int message) {
		messages.add(message);
	}
} 
