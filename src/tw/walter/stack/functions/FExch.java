package tw.walter.stack.functions;

import java.util.EmptyStackException;
import java.util.Stack;

import tw.walter.stack.tokens.*;

/*
 * Walter Stack Language - Source code of "exch" function
 * Exchange the two values on the top of the stack
 */
public class FExch implements WFunction {

	public static final String name = "exch";

	@Override
	public boolean execute(Stack<Token> s) {
		try {
			Token a = s.pop(), b = s.pop();
			s.push(a);
			s.push(b);
		} catch (EmptyStackException e) {
			System.err.println("Error: Function \"" + name + "\": the stack contains less than two elements!");
			return false;
		}
		return true;
	}

}