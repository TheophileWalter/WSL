package tw.walter.stack.functions;

import tw.walter.stack.CallStack;
import tw.walter.stack.tokens.TString;
import tw.walter.stack.tokens.Token;

import java.util.Scanner;
import java.util.Stack;

/*
 * Walter Stack Language - Source code of "input" function
 * Read input from stdin and put it as a string into the stack
 */
public class FInput implements WFunction {

	public static final String name = "input";

	// The stdin scanner
	private final Scanner scanner;

	// Needs a constructor to get the stdin scanner
	public FInput(Scanner scanner) {
		this.scanner = scanner;
	}

	@Override
	public boolean execute(Stack<Token> s, CallStack callStack) {

		// Reads one line from stdin and pushes it into the stack
		s.push(new TString(scanner.nextLine()));

		return true;
	}

}