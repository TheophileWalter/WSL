package tw.walter.stack.functions;

import tw.walter.stack.CallStack;
import tw.walter.stack.tokens.TNumber;
import tw.walter.stack.tokens.TString;
import tw.walter.stack.tokens.Token;

import java.util.EmptyStackException;
import java.util.Stack;

/*
 * Walter Stack Language - Source code of "substr" function
 * Return a substring from a given string
 */
public class FSubstr implements WFunction {

	public static final String name = "strlen";

	@Override
	public boolean execute(Stack<Token> s, CallStack callStack) {
		try {
			Token end = s.pop(), start = s.pop(), string = s.pop();

			if (!(string instanceof TString) || !(start instanceof TNumber) || !(end instanceof TNumber)) {
				System.err.println("Error: Function \"" + name + "\" expect a string and two numbers!\n" + callStack.toString());
				return false;
			} else {
				String stringValue = ((TString) string).getValue();
				int length = stringValue.length();
				// Prepare the indexes
				int startInt = (int) ((TNumber) start).getValue(), endInt = (int) ((TNumber) end).getValue();
				if (startInt < 0) {
					startInt += length;
				}
				if (endInt < 0) {
					endInt += length;
				}
				if (startInt < 0) {
					startInt = 0;
				} else if (startInt > length) {
					startInt = length;
				}
				if (endInt < 0) {
					endInt = 0;
				} else if (endInt > length) {
					endInt = length;
				}
				if (endInt < startInt) {
					endInt = startInt;
				}
				// Push the substring to the stack
				s.push(new TString(stringValue.substring(startInt, endInt)));
			}
		} catch (EmptyStackException e) {
			System.err.println("Error: Function \"" + name + "\": the stack contains less than three elements!\n" + callStack.toString());
			return false;
		}
		return true;
	}

}