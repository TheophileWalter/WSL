package tw.walter.stack.functions;

import java.util.EmptyStackException;
import java.util.Stack;

import tw.walter.stack.CallStack;
import tw.walter.stack.tokens.*;

/*
 * Walter Stack Language - Source code of "mul" function
 * Multiply two numbers
 */
public class FMul implements WFunction {

	public static final String name = "mul";

	@Override
	public boolean execute(Stack<Token> s, CallStack callStack) {
		try {
			Token a = s.pop(), b = s.pop();
			if (!(a instanceof TNumber) || !(b instanceof TNumber)) {
				System.err.println("Error: Function \"" + name + "\" expect two numbers!\n" + callStack.toString());
				return false;
			}
			double ac = ((TNumber) a).getValue(), bc = ((TNumber) b).getValue();
			s.push(new TNumber(ac * bc));
		} catch (EmptyStackException e) {
			System.err.println("Error: Function \"" + name + "\": the stack contains less than two elements!\n" + callStack.toString());
			return false;
		}
		return true;
	}

}