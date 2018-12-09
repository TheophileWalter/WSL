package tw.walter.stack.functions;

import java.util.EmptyStackException;
import java.util.Stack;

import tw.walter.stack.CallStack;
import tw.walter.stack.tokens.*;

/*
 * Walter Stack Language - Source code of "div" function
 * Divide two numbers
 * Example: "a b div" will perform "a/b"
 */
public class FDiv implements WFunction {
    
    public static final String name = "div";

    @Override
    public boolean execute(Stack<Token> s, CallStack callStack) {
        try {
            Token b = s.pop(), a = s.pop();
            if (!(a instanceof TNumber) || !(b instanceof TNumber)) {
				System.err.println("Error: Function \"" + name + "\" excpect two numbers!\n" + callStack.toString());
                return false;
            }
            double ac = ((TNumber)a).getValue(), bc = ((TNumber)b).getValue();
            s.push(new TNumber(ac / bc));
        } catch (EmptyStackException e) {
            System.err.println("Error: Function \"" + name + "\": the stack contains less than two elements!\n" + callStack.toString());
            return false;
        }
        return true;
    }

}