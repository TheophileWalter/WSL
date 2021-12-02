package tw.walter.stack.functions;

import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Stack;

import tw.walter.stack.CallStack;
import tw.walter.stack.tokens.*;

/*
 * Walter Stack Language - Source code of "dup" function
 * Duplicate the n elements in the stack
 * Example: "1 2 3 4 5 2 dup" will copy "4" and "5"
 *          The stack will be "1 2 3 4 5 4 5"
 */
public class FDup implements WFunction {

	public static final String name = "dup";

	@Override
	public boolean execute(Stack<Token> s, CallStack callStack) {
		try {
			
			// Get the parameters
			Token num = s.pop();
			if (!(num instanceof TNumber)) {
				System.err.println("Error: Function \"" + name + "\" expect a number!\n" + callStack.toString());
				return false;
			}
			
			// We will store the value to insert them twice after
			int max = (int)((TNumber) num).getValue();
			Token[] backup = new Token[max];
			for (int i = max-1; i >= 0; i--) { // Fill it reversed
				backup[i] = s.pop();
			}
			
			// Put the value to move and rebuild the stack
			for (int i = 0; i < 2; i++) {
				Collections.addAll(s, backup);
			}
			
		} catch (EmptyStackException e) {
			System.err.println("Error: Function \"" + name + "\": the stack does not contains enough elements!\n" + callStack.toString());
			return false;
		}
		return true;
	}

}