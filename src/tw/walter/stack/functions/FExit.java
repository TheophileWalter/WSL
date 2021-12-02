package tw.walter.stack.functions;

import java.util.EmptyStackException;
import java.util.Stack;

import tw.walter.stack.CallStack;
import tw.walter.stack.tokens.*;

/*
 * Walter Stack Language - Source code of "exit" function
 * Exit the script with a given code
 */
public class FExit implements WFunction {

	public static final String name = "exit";

	@Override
	public boolean execute(Stack<Token> s, CallStack callStack) {
		try {
			Token a = s.pop();
			if (!(a instanceof TNumber)) {
				System.err.println("Error: Function \"" + name + "\" expect a number!\n" + callStack.toString());
				return false;
			}
			int n = (int)((TNumber) a).getValue();
			System.exit(n);
		} catch (EmptyStackException e) {
			System.err.println("Error: Function \"" + name + "\": the stack contains less than one element!\n" + callStack.toString());
			return false;
		}
		return true;
	}

}