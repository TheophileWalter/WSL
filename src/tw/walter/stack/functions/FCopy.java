package tw.walter.stack.functions;

import java.util.EmptyStackException;
import java.util.Stack;

import tw.walter.stack.tokens.*;

/*
 * Walter Stack Language - Source code of "copy" function
 * Duplicate the element on top of the stack
 */
public class FCopy implements WFunction {

	public static final String name = "copy";

	@Override
	public boolean execute(Stack<Token> s) {
		try {
			Token a = s.pop();
			s.add(a);
			s.add(a);
		} catch (EmptyStackException e) {
			System.err.println("Error: Function \"" + name + "\": the stack contains less than one element!");
			return false;
		}
		return true;
	}

}