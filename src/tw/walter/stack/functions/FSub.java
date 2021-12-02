package tw.walter.stack.functions;

import java.util.EmptyStackException;
import java.util.Stack;

import tw.walter.stack.CallStack;
import tw.walter.stack.tokens.*;

/*
 * Walter Stack Language - Source code of "sub" function
 * Subtract two numbers
 * Example: "a b sub" will perform "a-b"
 */
public class FSub implements WFunction {

	public static final String name = "sub";

	@Override
	public boolean execute(Stack<Token> s, CallStack callStack) {
		try {
			Token b = s.pop(), a = s.pop();
			if (!(a instanceof TNumber) || !(b instanceof TNumber)) {
				System.err.println("Error: Function \"" + name + "\" expect two numbers!");
				return false;
			}
			double ac = ((TNumber) a).getValue(), bc = ((TNumber) b).getValue();
			s.push(new TNumber(ac - bc));
		} catch (EmptyStackException e) {
			System.err.println("Error: Function \"" + name + "\": the stack contains less than two elements!");
			return false;
		}
		return true;
	}

}