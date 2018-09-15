package tw.walter.stack.functions;

import java.util.Stack;
import tw.walter.stack.tokens.Token;

public interface WFunction {

    public boolean execute(Stack<Token> s);
    
}
