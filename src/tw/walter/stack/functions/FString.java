package tw.walter.stack.functions;

import java.util.EmptyStackException;
import java.util.Stack;

import tw.walter.stack.CallStack;
import tw.walter.stack.tokens.*;

/*
 * Walter Stack Language - Source code of "string" function
 * Converts any type to a string
 */
public class FString implements WFunction {

	public static final String name = "string";

	@Override
	public boolean execute(Stack<Token> s, CallStack callStack) {
		try {
			Token a = s.pop();

			if (a instanceof TNumber) {
				double n = ((TNumber) a).getValue();

				// Check if we can cast it as integer
				if (((double) (int) n) == n) {
					s.push(new TString(Integer.toString((int) n)));
				} else {
					s.push(new TString(Double.toString(n)));
				}

			} else if (a instanceof TKeyword) {
				String n = a.toString();
				s.push(new TString(n));
			} else if (a instanceof TGroup) {
				String n = a.toString();
				s.push(new TString(n));
			} else if (a instanceof TString) {
				s.push(a);
			} else {
				System.err.println("Error: Unknown type for conversion to string!\n" + callStack.toString());
				return false;
			}
		} catch (EmptyStackException e) {
			System.err.println("Error: Function \"" + name + "\": the stack contains less than one element!\n" + callStack.toString());
			return false;
		}
		return true;
	}

}