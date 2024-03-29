package tw.walter.stack.functions;

import java.util.EmptyStackException;
import java.util.Stack;

import tw.walter.stack.CallStack;
import tw.walter.stack.tokens.*;

/*
 * Walter Stack Language - Source code of "number" function
 * Converts a string or a number type to a number
 */
public class FNumber implements WFunction {

	public static final String name = "number";

	@Override
	public boolean execute(Stack<Token> s, CallStack callStack) {
		try {
			Token a = s.pop();
			
			if (a instanceof TString) {
				s.add(new TNumber(Double.parseDouble(((TString) a).getValue())));
			} else if (a instanceof TNumber) {
				s.add(a);
			} else {
				System.err.println("Error: Function \"" + name + "\" expect a number or a string!\n" + callStack.toString());
				return false;
			}
			
		} catch (EmptyStackException e) {
			System.err.println("Error: Function \"" + name + "\": the stack contains less than one element!\n" + callStack.toString());
			return false;
		}
		return true;
	}

}