first T1 forks itself. Then T2 forks itself. T1 waits for T2 and T2 waits for T3.
Then T3 forks the process. So there will be 3 threads in P2: T'1(which is waiting for T'2), T'2(which is waiting for T'3), and T'3. Then each thread prints a single line that identifies themselves.

threads and processes executios is synchronized with semaphores and channels, so the output is independent of the scheduler.