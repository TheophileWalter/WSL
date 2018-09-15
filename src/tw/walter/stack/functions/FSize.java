package tw.walter.stack.functions;

import java.util.Stack;

import tw.walter.stack.tokens.TNumber;
import tw.walter.stack.tokens.Token;

/*
 * Walter Stack Language - Source code of "size" function
 * Return the size of the stack
 */
public class FSize implements WFunction {

	public static final String name = "size";

	@Override
	public boolean execute(Stack<Token> s) {
		s.add(new TNumber((double) s.size()));
		return true;
	}

}
