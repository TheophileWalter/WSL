package tw.walter.stack.functions;

import java.util.EmptyStackException;
import java.util.Stack;

import tw.walter.stack.CallStack;
import tw.walter.stack.tokens.*;

/*
 * Walter Stack Language - Source code of "pack" function
 * Put the n elements in the stack inside a group
 * Example: "1 2 3 4 5 2 pack" will put "4" and "5" in a group
 *          The stack will be "1 2 3 (4 5)"
 */
public class FPack implements WFunction {

	public static final String name = "pack";

	@Override
	public boolean execute(Stack<Token> s, CallStack callStack) {
		try {
			
			// Get the parameter
			Token num = s.pop();
			if (!(num instanceof TNumber)) {
				System.err.println("Error: Function \"" + name + "\" excpect a number!\n" + callStack.toString());
				return false;
			}
			
			// We will store the value to insert them in a group
			int max = (int)((TNumber) num).getValue();
			Token[] backup = new Token[max];
			for (int i = max-1; i >= 0; i--) { // Fill it reversed
				backup[i] = s.pop();
			}
			
			// Put the values in a group
			TGroup g = new TGroup();
			for (Token t: backup) {
				g.add(t);
			}
			
			// Add the group to the stack
			s.add(g);
			
		} catch (EmptyStackException e) {
			System.err.println("Error: Function \"" + name + "\": the stack does not contains enought elements!\n" + callStack.toString());
			return false;
		}
		return true;
	}

}