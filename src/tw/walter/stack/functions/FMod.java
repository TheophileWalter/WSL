package tw.walter.stack.functions;

import java.util.EmptyStackException;
import java.util.Stack;

import tw.walter.stack.CallStack;
import tw.walter.stack.tokens.*;

/*
 * Walter Stack Language - Source code of "mod" function
 * Compute the rest of the division of two numbers
 * Example: "10 3 mod" will put 1 in the stack
 */
public class FMod implements WFunction {
    
    public static final String name = "mod";

    @Override
    public boolean execute(Stack<Token> s, CallStack callStack) {
        try {
            Token b = s.pop(), a = s.pop();
            if (!(a instanceof TNumber) || !(b instanceof TNumber)) {
				System.err.println("Error: Function \"" + name + "\" expect two numbers!\n" + callStack.toString());
                return false;
            }
            int ac = (int)((TNumber)a).getValue(), bc = (int)((TNumber)b).getValue();
            s.push(new TNumber(ac % bc));
        } catch (EmptyStackException e) {
            System.err.println("Error: Function \"" + name + "\": the stack contains less than two elements!\n" + callStack.toString());
            return false;
        } catch (ArithmeticException e) {
        	System.err.println("Error: Function \"" + name + "\": cannot divide by zero!\n" + callStack.toString());
            return false;
        }
        return true;
    }

}