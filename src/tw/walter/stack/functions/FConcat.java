package tw.walter.stack.functions;

import java.util.EmptyStackException;
import java.util.Stack;

import tw.walter.stack.CallStack;
import tw.walter.stack.tokens.*;

/*
 * Walter Stack Language - Source code of "concat" function
 * Concatenate two strings
 * Example: "a b concat" will give "ab"
 */
public class FConcat implements WFunction {

	public static final String name = "concat";

	@Override
	public boolean execute(Stack<Token> s, CallStack callStack) {
		try {
			Token b = s.pop(), a = s.pop();
			if (!(a instanceof TString) || !(b instanceof TString)) {
				System.err.println("Error: Function \"" + name + "\" excpect two strings!\n" + callStack.toString());
				return false;
			}
			String ac = ((TString) a).getValue(), bc = ((TString) b).getValue();
			s.push(new TString(ac + bc));
		} catch (EmptyStackException e) {
			System.err.println("Error: Function \"" + name + "\": the stack contains less than two elements!\n" + callStack.toString());
			return false;
		}
		return true;
	}

}