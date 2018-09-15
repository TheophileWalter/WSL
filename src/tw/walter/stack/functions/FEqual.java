package tw.walter.stack.functions;

import java.util.EmptyStackException;
import java.util.Stack;

import tw.walter.stack.tokens.*;

/*
 * Walter Stack Language - Source code of "equal" function
 * Return 1 if the two values are equals, else 0
 */
public class FEqual implements WFunction {

	public static final String name = "equal";

	@Override
	public boolean execute(Stack<Token> s) {
		try {
			Token a = s.pop(), b = s.pop();
			
			if (a instanceof TNumber && b instanceof TNumber) {
				double ac = ((TNumber) a).getValue(), bc = ((TNumber) b).getValue();
				s.add(new TNumber(ac == bc ? 1. : 0.));
			} else if (a instanceof TString && b instanceof TString) {
				String ac = ((TString) a).getValue(), bc = ((TString) b).getValue();
				s.add(new TNumber(ac.equals(bc) ? 1. : 0.));
			} else {
				System.err.println("Error: Function \"" + name + "\" excpect two strings or two numbers!");
				return false;
			}
			
		} catch (EmptyStackException e) {
			System.err.println("Error: Function \"" + name + "\": the stack contains less than two elements!");
			return false;
		}
		return true;
	}

}