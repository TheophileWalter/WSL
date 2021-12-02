package tw.walter.stack.functions;

import tw.walter.stack.CallStack;
import tw.walter.stack.WSL;
import tw.walter.stack.tokens.*;

import java.util.EmptyStackException;
import java.util.Stack;

/*
 * Walter Stack Language - Source code of "var" function
 * Return the value of a WSL system variable
 */
public class FVar implements WFunction {

	public static final String name = "var";

	@Override
	public boolean execute(Stack<Token> s, CallStack callStack) {
		try {
			Token a = s.pop();

			if (!(a instanceof TString)) {
				System.err.println("Error: Function \"" + name + "\" expect a string!\n" + callStack.toString());
				return false;
			}

			String name = ((TString) a).getValue();
			switch (name) {
				case "version":
					s.push(new TString(WSL.WSL_VERSION));
					break;
				case "version_code":
					s.push(new TNumber(WSL.WSL_VERSION_CODE));
					break;
				case "is_alpha":
					s.push(new TNumber(WSL.WSL_IS_ALPHA ? 1 : 0));
					break;
				case "is_beta":
					s.push(new TNumber(WSL.WSL_IS_BETA ? 1 : 0));
					break;
				case "lib_version":
					s.push(new TString(WSL.WSL_LIB_VERSION));
					break;
				case "lib_version_code":
					s.push(new TNumber(WSL.WSL_LIB_VERSION_CODE));
					break;
				case "copyright":
					s.push(new TString(WSL.WSL_COPYRIGHT));
					break;
				case "line":
					s.push(new TNumber(callStack.currentLine()));
					break;
				case "file":
					s.push(new TString(callStack.currentFile()));
					break;
				default:
					System.err.println("Error: Unknown WSL system variable \"" + name + "\"!\n" + callStack.toString());
					return false;
			}

		} catch (EmptyStackException e) {
			System.err.println("Error: Function \"" + name + "\": the stack contains less than one element!\n" + callStack.toString());
			return false;
		}
		return true;
	}

}