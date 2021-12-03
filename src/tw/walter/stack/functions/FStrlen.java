package tw.walter.stack.functions;

import tw.walter.stack.CallStack;
import tw.walter.stack.tokens.*;

import java.util.EmptyStackException;
import java.util.Stack;

/*
 * Walter Stack Language - Source code of "strlen" function
 * Return the length of a string
 */
public class FStrlen implements WFunction {

	public static final String name = "strlen";

	@Override
	public boolean execute(Stack<Token> s, CallStack callStack) {
		try {
			Token a = s.pop();

			if (a instanceof TString) {

				// Put the string length in the stack
				s.push(new TNumber(((TString) a).getValue().length()));

			} else {
				System.err.println("Error: Function \"" + name + "\" expect a string!\n" + callStack.toString());
				return false;
			}
		} catch (EmptyStackException e) {
			System.err.println("Error: Function \"" + name + "\": the stack contains less than one element!\n" + callStack.toString());
			return false;
		}
		return true;
	}

}