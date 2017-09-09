there are 3 threads:
T1 is waiting on T2. Then T2 forks. So T1 is waiting for T2 and T3. As soon as T2 or T3 terminates, T1 should be able to continue execution. 

semaphore used to synchronize thredas execution and make the output independent of scheduler 
