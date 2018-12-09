package tw.walter.stack.functions;

import java.util.Stack;

import tw.walter.stack.CallStack;
import tw.walter.stack.tokens.*;

/*
 * Walter Stack Language - Source code of "random" function
 * Generate a float random number in range [0, 1]
 */
public class FRandom implements WFunction {

	public static final String name = "random";

	@Override
	public boolean execute(Stack<Token> s, CallStack callStack) {
		s.push(new TNumber(Math.random()));
		return true;
	}

}