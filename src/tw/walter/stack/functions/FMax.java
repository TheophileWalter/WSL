package tw.walter.stack.functions;

import java.util.EmptyStackException;
import java.util.Stack;

import tw.walter.stack.tokens.*;

/*
 * Walter Stack Language - Source code of "max" function
 * Return the greater number
 */
public class FMax implements WFunction {

	public static final String name = "max";

	@Override
	public boolean execute(Stack<Token> s) {
		try {
			Token a = s.pop(), b = s.pop();
			if (!(a instanceof TNumber) || !(b instanceof TNumber)) {
				System.err.println("Error: Function \"" + name + "\" excpect two numbers!");
				return false;
			}
			double ac = ((TNumber) a).getValue(), bc = ((TNumber) b).getValue();
			s.push(new TNumber(Math.max(ac, bc)));
		} catch (EmptyStackException e) {
			System.err.println("Error: Function \"" + name + "\": the stack contains less than two elements!");
			return false;
		}
		return true;
	}

}