package compiler;

import java.io.FileNotFoundException;
import java.util.Vector;
import java.util.Scanner;
import java.io.File;

import hardware.cpu.instruction.physicalmemory.LoadPageInstruction;
import hardware.cpu.instruction.physicalmemory.PrintLoadedPagesInstruction;
import hardware.cpu.instruction.processmanagement.*;
import hardware.cpu.instruction.processmanagement.synchronization.*;
import hardware.cpu.instruction.secondarymemory.*;
import hardware.cpu.instruction.screen.*;
import hardware.cpu.instruction.*;
import hardware.cpu.instruction.ipc.channel.*;



public class Compiler {
	protected Parser parser = new Parser();

	protected Instruction compileSingleInstruction(String instructionText) {
		int argsStart = 0;
		while(instructionText.charAt(argsStart++) != '(');
		argsStart--;

		String instructionName = instructionText.substring(0, argsStart);
		String argsText = instructionText.substring(argsStart + 1, instructionText.length() - 1);
		Vector<String> args = parser.getArgs(argsText);

		if(instructionName.equals("create_thread"))
			return new CreateThreadInstruction(args.get(0));
		else if(instructionName.equals("wait_for_thread"))
			return new WaitForThreadInstruction(args.get(0));
		else if(instructionName.equals("kill_thread"))
			return new KillThreadInstruction(args.get(0));
		else if(instructionName.equals("print_thread_info"))
			return new PrintThreadInfoInstruction();

		else if(instructionName.equals("create_process"))
			return new CreateProcessInstruction(args.get(0));
		else if(instructionName.equals("wait_for_process"))
			return new WaitForProcessInstruction(args.get(0));
		else if(instructionName.equals("kill_process"))
			return new KillProcessInstruction(args.get(0));
		else if(instructionName.equals("print_process_info"))
			return new PrintProcessInfoInstruction();


		else if(instructionName.equals("cj"))
			return new CjInstruction(args.get(0), args.get(1), args.get(2));

		else if(instructionName.equals("echo"))
			return new EchoInstruction(args.get(0));

		else if(instructionName.equals("exec"))
			return new ExecInstruction(args.get(0));

		else if(instructionName.equals("set"))
			return new SetInstruction(args.get(0), args.get(1));
		else if(instructionName.equals("gset"))
			return new GsetInstruction(args.get(0), args.get(1));

		else if(instructionName.equals("set_priority"))
			return new SetPriorityInstruction(args.get(0));

		else if(instructionName.equals("semaphore"))
			return new CreateSemaphoreInstruction(args.get(0), args.get(1));
		else if(instructionName.equals("signal"))
			return new SignalSemaphoreInstruction(args.get(0));
		else if(instructionName.equals("wait"))
			return new WaitForSemaphoreInstruction(args.get(0));

		else if(instructionName.equals("create_channel"))
			return new CreateChannelInstruction(args.get(0));
		else if(instructionName.equals("read"))
			return new ReadFromChannelInstruction(args.get(0), args.get(1));
		else if(instructionName.equals("write"))
			return new WriteInChannelInstruction(args.get(0), args.get(1));

		else if(instructionName.equals("load_page"))
			return new LoadPageInstruction(args.get(0));
		else if(instructionName.equals("print_pages"))
			return new PrintLoadedPagesInstruction();

		else if(instructionName.equals("read_physical"))
			return new ReadPhysicalInstruction(args.get(0), args.get(1));
		else if(instructionName.equals("write_physical"))
			return new WritePhysicalInstruction(args.get(0), args.get(1));
		else if(instructionName.equals("read_basic"))
			return new ReadBasicInstruction(args.get(0), args.get(1), args.get(2));
		else if(instructionName.equals("write_basic"))
			return new WriteBasicInstruction(args.get(0), args.get(1), args.get(2));
		else if(instructionName.equals("read_org"))
			return new ReadOriginInstruction(args.get(0), args.get(1), args.get(2), args.get(3));
		else if(instructionName.equals("write_org"))
			return new WriteOriginInstruction(args.get(0), args.get(1), args.get(2), args.get(3));
		else if(instructionName.equals("create_file"))
			return new CreateFileInstruction(args.get(0));
		else if(instructionName.equals("open"))
			return new OpenFileInstruction(args.get(0), args.get(1));
		else if(instructionName.equals("read_file"))
			return new ReadFileInstruction(args.get(0), args.get(1), args.get(2), args.get(3));
		else if(instructionName.equals("append"))
			return new AppendInFileInstruction(args.get(0), args.get(1));
		else if(instructionName.equals("write_file"))
			return new WriteInFileInstruction(args.get(0), args.get(1), args.get(2));
		else if(instructionName.equals("clear"))
			return new ClearFileInstruction(args.get(0));
		else if(instructionName.equals("delete_file"))
			return new DeleteFileInstruction(args.get(0));
		else if(instructionName.equals("size"))
			return new SizeInstruction(args.get(0), args.get(1));
		else if(instructionName.equals("print_map"))
			return new PrintMapInstruction();
		else if(instructionName.equals("print_file_map"))
			return new PrintFileMapInstruction();
		else if(instructionName.equals("print_free_map"))
			return new PrintFreeMapInstruction();

		else {
			// TODO: unknown instruction
		}
		return null;
	}

	protected String readProgram(String programPath) throws FileNotFoundException {
		File programFile = new File(programPath);
		Scanner scanner = new Scanner(programFile);

		String program = "";
		while(scanner.hasNextLine())
			program += scanner.nextLine() + "\n";

		return program;
	}

	public Vector<Instruction> compile(String programPath) throws FileNotFoundException {
		String programText = readProgram(programPath);
		Vector<String> parsedInstructions = parser.parse(programText);

		Vector<Instruction> instructions = new Vector<Instruction>();
		for(int i = 0; i < parsedInstructions.size(); i++) {
			Instruction currentInstruction = compileSingleInstruction(parsedInstructions.get(i));
			if (currentInstruction != null)
				instructions.add(currentInstruction);
		}

		return instructions;
	}

}