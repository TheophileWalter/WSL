package tw.walter.stack.functions;

import java.util.EmptyStackException;
import java.util.Stack;

import tw.walter.stack.tokens.*;

/*
 * Walter Stack Language - Source code of "sqrt" function
 * Compute the square root of a number
 */
public class FSqrt implements WFunction {

	public static final String name = "sqrt";

	@Override
	public boolean execute(Stack<Token> s) {
		try {
			Token a = s.pop();
			if (!(a instanceof TNumber)) {
				System.err.println("Error: Function \"" + name + "\" excpect a number!");
				return false;
			}
			double n = ((TNumber) a).getValue();
			s.add(new TNumber(Math.sqrt(n)));
		} catch (EmptyStackException e) {
			System.err.println("Error: Function \"" + name + "\": the stack contains less than one element!");
			return false;
		}
		return true;
	}

}