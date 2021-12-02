package tw.walter.stack.functions;

import java.util.Stack;

import tw.walter.stack.CallStack;
import tw.walter.stack.tokens.Token;

public interface WFunction {

	boolean execute(Stack<Token> s, CallStack callStack);

}
