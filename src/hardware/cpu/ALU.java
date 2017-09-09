package hardware.cpu;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ALU {
	protected HardwareThread thread;

	public void setThread(HardwareThread thread) {
		this.thread = thread;
	}

	public boolean isAtomic(String s) {
		// is it a number?
		String pattern = "^-?\\d+$";
		Pattern pat = Pattern.compile(pattern);
		Matcher match = pat.matcher(s);
		if(match.find()){
			return true;
		}

		// does it have any operator or function?
		String[] keywords = {
				")", "(", "+", "-", "*", "/", "&", "|", "!", "^", "%",
				">", "<", "<=", ">=", "==", "!=", "concat", "index"
		};
		boolean inQuote = false;
		for(int i = 0; i < s.length(); i++) {
			if(s.charAt(i) == '\"')
				inQuote = !inQuote;
			if(inQuote)
				continue;

			for(int j = 0; j < keywords.length; j++) {
				if(i + keywords[j].length() > s.length())
					continue;
				if(s.substring(i, keywords[j].length()+i).equals(keywords[j])) {
					return false;
				}
			}
		}
		return true;
	}

	public Value eval(String s){
		if(s.equals("6%4")) {
			int a = 0;
		}
		if(isAtomic(s)) {
			String pattern = "^-?\\d+$";
			Pattern pat = Pattern.compile(pattern);
			Matcher match = pat.matcher(s);
			if(match.find()){
				return new Value(Integer.valueOf(s));
			} else {
				if(s.charAt(0) == '\"')
					return new Value(s.substring(1, s.length()-1));
				else
					return thread.getVar(s);
			}
		}

		int[] count = new int[s.length()];
		count[0] = s.charAt(0)=='('?1:0;
		for (int i = 1; i < count.length; i++) {
			if (s.charAt(i) == '(')
				count[i] = count[i-1] + 1;
			else if(s.charAt(i) == ')')
				count[i] = count[i-1] - 1;
			else
				count[i] = count[i-1];
		}


		for (int i = s.length()-1; i > -1; i--) {
			char op = s.charAt(i);

			if(count[i]==0 && (
					op == '|' )) {
				Value operand1 = eval(s.substring(0, i));
				Value operand2 = eval(s.substring(i + 1));

				Value res = null;
				if (op == '|')
					res = operand1.or(operand2);
				return res;
			}
		}
		for (int i = s.length()-1; i > -1; i--) {
			char op = s.charAt(i);

			if(count[i]==0 && (
					op == '^' )) {
				Value operand1 = eval(s.substring(0, i));
				Value operand2 = eval(s.substring(i + 1));

				Value res = null;
				if (op == '^')
					res = operand1.xor(operand2);
				return res;
			}
		}

		for (int i = s.length()-1; i > -1; i--) {
			char op = s.charAt(i);

			if(count[i]==0 && (
					op == '&' )) {
				Value operand1 = eval(s.substring(0, i));
				Value operand2 = eval(s.substring(i + 1));

				Value res = null;
				if (op == '&')
					res = operand1.and(operand2);
				return res;
			}
		}

		for (int i = 0; i < s.length()-1; i++) {

			String opr2 = s.substring(i, i+2);

			if (count[i] == 0 && (
					opr2.equals("==") ||
							opr2.equals("!=") ||
							opr2.equals(">=") ||
							opr2.equals("<="))) {
				Value p1 = eval(s.substring(0, i));
				Value p2 = eval(s.substring(i+2));
				if (opr2.equals("=="))
					return new Value(p1.isEqual(p2));
				if (opr2.equals("!="))
					return new Value(p1.isNotEqual(p2));
				if (opr2.equals(">="))
					return new Value (p1.isGreaterThanOrEqual(p2));
				if (opr2.equals("<="))
					return new Value(p1.isLessThanOrEqual(p2));
			}

			String opr1 = s.substring(i,i+1);
			if(count[i]==0 && (
					opr1.equals(">") ||
							opr1.equals("<"))){
				Value p1 = eval(s.substring(0, i));
				Value p2 = eval(s.substring(i+1));
				if (opr1.equals("<"))
					return new Value(p1.isLessThan(p2));
				if (opr1.equals(">"))
					return new Value(p2.isGreaterThan(p2));
			}
		}

		for (int i = 0; i < s.length(); i++) {
			if (count[i] == 0 &&
					s.charAt(i) == '%') {
				Value p1 = eval(s.substring(0, i));
				Value p2 = eval(s.substring(i+1));
				return p1.mod(p2);
			}
		}

		for (int i = 0; i < s.length(); i++) {
			char opr = s.charAt(i);
			if (count[i] == 0 && (
					s.charAt(i) == '+' ||
							s.charAt(i) == '-')) {
				Value p1 = eval(s.substring(0, i));
				Value p2 = eval(s.substring(i+1));
				if (opr == '+')
					return p1.add(p2);
				else if (opr=='-')
					return p1.subtract(p2);
			}
		}

		for (int i = 0; i < s.length(); i++) {
			char opr = s.charAt(i);
			if (count[i] == 0 && (
					s.charAt(i) == '*' ||
							s.charAt(i) == '/')) {
				Value p1 = eval(s.substring(0, i));
				Value p2 = eval(s.substring(i+1));
				if (opr == '*')
					return p1.multiply(p2);
				else if (opr == '/')
					return p1.divide(p2);
			}
		}

		if(s.startsWith("(") && s.endsWith(")"))
			return eval(s.substring(1, s.length()-1));

		if (s.startsWith("!")) {
			Value res = eval(s.substring(1));
			return res.not();
		}

		if(s.startsWith("index")) {
			Vector<Integer> commas = new Vector<>();
			for (int i = 0; i < s.length(); i++) {
				if (count[i]==1 &&
						s.charAt(i)==',') {
					commas.add(i);
				}
			}
			Value par1 = eval(s.substring("index(".length(), commas.get(0)));
			Value par2 = eval(s.substring(commas.get(0)+1 , commas.get(1)));
			Value par3 = eval(s.substring(commas.get(1)+1, s.length()-1));

			int startInd = par1.toInteger();
			int endInd = par2.toInteger();
			String str = par3.toString();

			return new Value(par3.toString().substring(startInd, endInd));
		}

		if(s.startsWith("concat")) {

			for (int i = 0; i < s.length(); i++) {
				if (count[i]==1 &&
						s.charAt(i)==',') {
					Value par1 = eval(s.substring("concat(".length(), i));
					Value par2 = eval(s.substring(i+1, s.length()-1));
					return new Value(par1.toString() + par2.toString());
				}
			}
		}
		// TODO: throw exception
		return null;
	}
} 
