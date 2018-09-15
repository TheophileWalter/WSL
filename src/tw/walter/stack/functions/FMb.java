package tw.walter.stack.functions;

import java.util.EmptyStackException;
import java.util.Stack;

import tw.walter.stack.tokens.*;

/*
 * Walter Stack Language - Source code of "mb" function
 * Move the second element back in the stack
 * Example: "1 2 3 4 5 2 mb" will move "5" back from "2" positions in the stack.
 *          The stack will be "1 2 5 3 4"
 */
public class FMb implements WFunction {

	public static final String name = "mb";

	@Override
	public boolean execute(Stack<Token> s) {
		try {
			
			// Get the parameters
			Token back = s.pop(), value = s.pop();
			if (!(back instanceof TNumber)) {
				System.err.println("Error: Function \"" + name + "\" excpect a number!");
				return false;
			}
			
			// We will store the value to insert the top value before them
			int max = (int)((TNumber) back).getValue();
			Token[] backup = new Token[max];
			for (int i = max-1; i >= 0; i--) { // Fill it reversed
				backup[i] = s.pop();
			}
			
			// Put the value to move and rebuild the stack
			s.add(value);
			for (Token t: backup) {
				s.add(t);
			}
			
		} catch (EmptyStackException e) {
			System.err.println("Error: Function \"" + name + "\": the stack does not contains enought elements!");
			return false;
		}
		return true;
	}

}