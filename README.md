# ostor_java_final_version
Operating System university course project - phase 1 through 4 - in java

### 1. What is this project about?
This project is an operating system built on a simulated hardware. In fact, the purpose of this project is to run programs in a C-like language designed only for this project. This language contains basic loop and condition constructs along with a multitude of commands which require some basic OS services. 

A bunch of these commands is meant to manage processes and threads, like forking, synchronization with semaphores, and managing threads' priorities. A few require interprocess communication services through channels. A very minimal physical memory management is also implemented for some commands that try to manage memory pages. A rather big secondary memory management system is coded as well in order to answer commands that ask for services ranging from reading raw blocks' data from the media through manipulating files. 

Note that we were not supposed to use any relevant tool in our implementations, e.g. threading libraries, interprocess communication libraries, and file management libraries. So everything is running on a single thread, though managed to create an illusion of a multi-core hardware for all the programs it's running. Additionally, the whole secondary memory management system is based on an array of numbers hold in your computer main memory. 

### 2. What is the introduced language's specification?
First of all, this language only has 2 data types: integer and string. A variable has no data type, so you can save a string in a variable and then save an integer in it (i.e. it's a weakly typed language).

This language contains `if`, `for`, and `while` constructs along with 2 variable-defining commands separated to distinguish heap and stack variables. Also, there is an `echo` (i.e. `print`) command for printing things on the command line. 

It also consists of a multitude of commands for using different OS services:
1. process management: `create_process`, `wait_for_process`, `kill_process`, `print_process_info`, and `exec`
2. thread management: `create_thread`, `wait_for_thread`, `kill_thread`, and `print_thread_info`, `set_priority` along with an option in hardware creation procedure to choose the number of cores and setting cores' timers
3. synchronization: `semaphore`, `signal`, and `wait`
4. interprocess communication: `create_channel`, `read`, and `write` 
5. main memory management: `load_page`, and `print_pages` along with an option in booting process to choose an algorithm for managing pages(e.g. LRU, and LFU)
6. secondary memory management: `read_physical`, `write_physical`, `read_basic`, `write_basic`, `read_org`, `write_org`, `create_file`, `open`, `read_file`, `write_file`, `append`, `clear`, `delete_file`, `size`, `print_map`, `print_file_map`, `print_free_map`

Commands' specification is not included in documents currently, but I hope one day it will be :)

Let's just take a look into a simple program in this language:
```C++
semaphore(s, 0)
semaphore(s2, 0)
create_thread(x)
if(x == 0) {
	echo("before T2 executes wait for semaphore")
	signal(s)
	wait(s2)
	echo("after T2 executes wait for semaphore")
} 
if(x != 0) {
	wait(s)
	echo("before T1 executes wait for semaphore")
	wait(s2)
	echo("after T1 executes wait for semaphore")
}
```

### 3. How does this "OS" execute programs?
First, it compiles each program. It converts all flow control commands(e.g. `if`, and `for`) to a series of `cj`s (conditional jump) and labels. More specifically, an `if` statement checks whether its expression evaluates to `true` or `false`. If it is `true`, it should run all commands in its block and if it doesn't, it should jump to the end of its block. So it's very intuitive to convert all control flow commands to conditional jumps and labels. Details of these new low-level commands can be checked in the source code. After this parsing stage, each command is transformed into an instruction, so the os can execute them.

`Instruction` is a class defined in the `hardware` package. It includes an `execute` method. Each type of instruction is derived from it and override its `execute` method to execute itself. These instructions are fed to the `CPU` object directly one by one in the execution process. Some instructions can be accomplished with no interference by the OS, like setting a value to a variable and executing a `cj` command. But some requires special services provided by the OS, like IPC and secondary memory management. `execute` method of these instructions use OS system calls in order to provide the instruction's needs. 

When the compilation stage is completed, we have a sequence of instruction objects that we can run like normal assembly code. Each thread has a "program counter" variable which points to the next instruction object to execute. Note that this variable can change through `cj` instructions. 

### 4. How is the hardware simulated?
There is a package named `hardware` in the source code. It contains a multitude of classes including `CPU`, `Core`, `ALU`, `PhysicalMemory`, and `SecondaryMemory`. Each classes' name is self-explanatory. `CPU` object has a few `Core` objects in it and each `Core` has an `ALU` object. `SecondaryMemory` contains only an array of numbers along with a few methods providing low-level services. `PhysicalMemory` is something like a dummy class that I can't remember why we decided to have it. 

When you turn on the computer, first it creates hardware pieces and plugs them all to the `MotherBoard` object and then boots the OS. More specifically, when the `Main` class runs, it creates a `MotherBoard` object. This object creates all required hardware objects, according to the given arguments(e.g. number of cores). Then it boots the OS. By booting I mean it creates an `Ostor` object and introduces each hardware piece to it one by one. The `Ostor` object creates a driver object for each hardware to manage it. After this stage, the initial program, which its path is given in the arguments, will be compiled. After compilation stage, `Ostor` object has a thread containing the generated sequence of instructions. From now on, the `CPU` object plays the central role. In an "almost" infinite loop, it walks through all its cores one by one and asks them to run a single instruction. 

A core has a `HardwareThread` object. Normally, it gets its next instruction and calls its `execute` method. But if the thread object equals to `null`, then this core is idle and must ask the OS for a thread to run. Note that it's possible that the OS has no thread in the ready queue. In this case, the core remains idle. The core might realize that the thread it is supposed to run has run out of instructions, has been killed, or is waiting for a process, thread, or semaphore. In this case, it should report to the OS and asks for another thread. This sort of communication between `core` and the OS is accomplished by the interrupt mechanism. Note that the `core`s timer might cause an interrupt as well. 

So, the simulated computer works just like a normal computer. 

### 4. How to run the project?
You just need to run the following commands in your terminal:
```shell
cd <project directory>
mkdir out
cd src
javac Main.java -d ../out/
```
Now you only need to run the following command to run the initial program with Ostor:
```shell
cd ../out/
java Main [optional arguments] <initial program path>
```

### 5. Who are the contributors?
The team consists of [Elnaz Mehrzadeh](https://github.com/elie-naz), Bahar Salamatian, and of course me. The contributors' Github account will be added to this README file soon. 

### 6. Why hasn't this project be added to git until it was over?
As the team leader, I was responsible for such choices and convincing team members. After so many failures, I learned something about the mixture of team work and learning: Never learn two new things at once in the same project. 

### 7. Why is this readme structured like this?
I always struggled to find the information I want in readme pages. I found the "question-answer" format richest for those who just want to skim, like me.
