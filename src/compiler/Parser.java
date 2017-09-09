package compiler;

import java.util.Vector;

public class Parser {
    protected int findBlockEnd(String text, int start){
        int end = start + 1, count = 1;
        boolean inQuote = false;
        while(count > 0) {
            if(text.charAt(end) == '\"')
                inQuote = !inQuote;
            if(inQuote) {
                end++;
                continue;
            }
            if(text.charAt(end) == '{')
                count ++;
            if(text.charAt(end) == '}')
                count --;
            end ++;
        }
        return end - 1;
    }

    protected int getInstructionEnd(String text, int start){
        boolean flag = false;
        int count = 0;
        boolean inQuote = false;
        while(!flag || count > 0) {
            if(text.charAt(start) == '\"')
                inQuote = !inQuote;
            if(inQuote) {
                start++;
                continue;
            }
            if(text.charAt(start) == '(' || text.charAt(start) == ')')
                flag = true;
            if(text.charAt(start) == '(')
                count++;
            if(text.charAt(start) == ')')
                count--;
            start++;
        }
        return start - 1;
    }

    protected Vector<String> parseIf(String ifCommand, Vector<String> blockInstructions){
        int start = 0;
        while(ifCommand.charAt(start++) != '(');
        String argsText = ifCommand.substring(start, ifCommand.length() - 1);
        String expression = getArgs(argsText).get(0);

        // generate instructions
    /*
        cj(expression, false, end)
        <block>
        end:
    */
        Vector<String> instructions = new Vector<String>();
        String jumpCount = String.valueOf(blockInstructions.size());
        instructions.add("cj(" + expression + ",0," + jumpCount + ")");
        instructions.addAll(blockInstructions);

        return instructions;

    }

    protected Vector<String> parseWhile(String whileCommand, Vector<String> blockInstructions) {
        int start = 0;
        while(whileCommand.charAt(start++) != '(');
        String argsText = whileCommand.substring(start, whileCommand.length() - 1);
        String expression = getArgs(argsText).get(0);

        // generate instructions
    /*
        start:
        cj(expression, false, end)
        <block>
        j start
        end:
    */
        Vector<String> instructions = new Vector<String>();
        instructions.add("cj(" + expression + ",0," + String.valueOf(blockInstructions.size() + 1) + ")");
        instructions.addAll(blockInstructions);
        instructions.add("cj(0,0," + String.valueOf(-(blockInstructions.size() + 2)) + ")");

        return instructions;

    }

    protected Vector<String> parseFor(String forCommand, Vector<String> blockInstructions){
        int start = 0;
        while(forCommand.charAt(start++) != '(');
        String argsText = forCommand.substring(start, forCommand.length() - 1);
        Vector<String> args = getArgs(argsText, ';');

        // generate instructions
    /*
        for(ex1; ex2; ex3) { <block> }

        ex1
        start:
        cj(ex2, false, end)
        <block>
        ex3
        j start
        end:
    */
        Vector<String> instructions = new Vector<String>();
        instructions.add(args.get(0));
        instructions.add("cj(" + args.get(1) + ",0," + (blockInstructions.size() + 2) + ")");
        instructions.addAll(blockInstructions);
        instructions.add(args.get(2));
        instructions.add("cj(0,0," + (-blockInstructions.size() - 3) + ")");

        return instructions;
    }

    protected boolean isWhiteSpace(char ch){
        return ch == ' ' ||
                ch == '\n' ||
                ch == '\t' ||
                ch == '\r';
    }

    protected String deleteWhitespaces(String str){
        boolean inQuote = false;
        for(int i = 0; i < str.length(); i++) {
            if(str.charAt(i) == '\"')
                inQuote = !inQuote;
            if(inQuote)
                continue;
            if(isWhiteSpace(str.charAt(i))) {
                str = str.substring(0, i) + str.substring(i+1, str.length());
                i--;
            }
        }
        return str;
    }

    protected void clean(Vector<String> instructions) {
        for(int i = 0; i < instructions.size(); i++) {
            instructions.set(i, deleteWhitespaces(instructions.get(i)));
            if(instructions.get(i).length() == 0) {
                instructions.remove(instructions.get(i));
                i--;
            }
        }
    }

    public Vector<String> parse(String text){
        Vector<String> all_instructions = new Vector<String>();

        for(int i = 0; i < text.length(); ) {
            // delete whitespaces from the beginning
            while(i < text.length() && isWhiteSpace(text.charAt(i++)));
            if(i >= text.length())
                break;
            i--;

            // parse "if", "for", and "while" commands
            if(text.charAt(i) == '{') {

                // complie this block to Vector of instructions
                int block_end = findBlockEnd(text, i);
                String block_text = text.substring(i+1, block_end);
                Vector<String> block_instructions = parse(block_text);
                // setting next index to search for commands
                i = block_end + 1;

                // exclude last instruction from all_instructions
                String last_command = all_instructions.remove(all_instructions.size()-1);

                // parse "if", "while", and "for" command
                Vector<String> new_instructions = new Vector<String>();
                if(last_command.substring(0, 2).equals("if"))
                    new_instructions = parseIf(last_command, block_instructions);
                else if(last_command.substring(0, 3).equals("for"))
                    new_instructions = parseFor(last_command, block_instructions);
                else if(last_command.substring(0, 5).equals("while"))
                    new_instructions = parseWhile(last_command, block_instructions);
                else {
                    // TODO: invalid command exception
                }

                // add parsed instruction
                all_instructions.addAll(new_instructions);

            } else {

                // parse next instruction
                int new_instruction_end = getInstructionEnd(text, i);
                String new_instruction = text.substring(i, new_instruction_end + 1);
                all_instructions.add(new_instruction);

                // setting index for next command
                i = new_instruction_end + 1;
            }
        }

        clean(all_instructions);
        return all_instructions;
    }

    public Vector<String> getArgs(String argsText){
        return getArgs(argsText, ',');
    }

    public Vector<String> getArgs(String args_text, char delimiter){
        Vector<String> args = new Vector<String>();
        int brackets_count = 0, paranthesis_count = 0;
        boolean in_quote = false;
        int arg_start = 0;
        for(int i = 0; i < args_text.length(); i++) {

            // handle quotes
            if(args_text.charAt(i) == '\"')
                in_quote = !in_quote;
            if(in_quote)
                continue;

            // handle brackets
            if(args_text.charAt(i) == '{')
                brackets_count++;
            if(args_text.charAt(i) == '}')
                brackets_count--;

            // handle paranthesises
            if(args_text.charAt(i) == '(')
                paranthesis_count++;
            if(args_text.charAt(i) == ')')
                paranthesis_count--;

            if(brackets_count == 0 &&
                    paranthesis_count == 0 &&
                    args_text.charAt(i) == delimiter) {
                String new_arg = args_text.substring(arg_start, arg_start + i - arg_start);
                args.add(new_arg);
                arg_start = i+1;
            }
        }
        args.add(args_text.substring(arg_start, args_text.length()));

        return args;
    }

} 
