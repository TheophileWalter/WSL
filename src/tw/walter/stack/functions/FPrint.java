package tw.walter.stack.functions;

import java.util.EmptyStackException;
import java.util.Stack;

import tw.walter.stack.tokens.*;

/*
 * Walter Stack Language - Source code of "print" function
 * Print a string
 */
public class FPrint implements WFunction {

	public static final String name = "print";

	@Override
	public boolean execute(Stack<Token> s) {
		try {
			Token a = s.pop();

			if (!(a instanceof TString)) {
				System.err.println("Error: Function \"" + name + "\" excpect a string!");
				return false;
			}

			System.out.print(((TString) a).getValue());

		} catch (EmptyStackException e) {
			System.err.println("Error: Function \"" + name + "\": the stack contains less than one element!");
			return false;
		}
		return true;
	}

}