package tw.walter.stack.functions;

import java.util.EmptyStackException;
import java.util.Stack;

import tw.walter.stack.CallStack;
import tw.walter.stack.tokens.Token;

/*
 * Walter Stack Language - Source code of "pop" function
 * Remove the element on top of the stack
 */
public class FPop implements WFunction {

	public static final String name = "pop";

	@Override
	public boolean execute(Stack<Token> s, CallStack callStack) {
		try {
			s.pop();
		} catch (EmptyStackException e) {
			System.err.println("Error: Function \"" + name + "\": the stack contains less than one element!\n" + callStack.toString());
			return false;
		}
		return true;
	}

}
