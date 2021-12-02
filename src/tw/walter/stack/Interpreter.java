package tw.walter.stack;

import java.io.File;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;

import tw.walter.stack.functions.WFunctionsList;
import tw.walter.stack.functions.WFunction;
import tw.walter.stack.tokens.*;

/*
 * Walter Stack Language - Interpreter
 * Execute an array of tokens as a WSL code
 */
public class Interpreter {
	
	// Error constants
	public static final int NO_ERROR = 0;
	public static final int UNDEFINED_FUNCTION_ERROR = -1;
	public static final int FUNCTION_EXECUTION_ERROR = -2;
	public static final int UNEXPECTED_OBJECT_ERROR = -3;
	public static final int DEF_TOO_FEW_ELEMENTS_ERROR = -4;
	public static final int DEF_STRING_EXPECTED_ERROR = -5;
	public static final int DEF_WRONG_KEYWORD_FORMAT_ERROR = -6;
	public static final int STACK_OVERFLOW_ERROR = -7;
	public static final int EXEC_TOO_FEW_ELEMENTS_ERROR = -8;
	public static final int EXEC_GROUP_EXPECTED_ERROR = -9;
	public static final int REPEAT_TOO_FEW_ELEMENTS_ERROR = -10;
	public static final int REPEAT_STRING_AND_GROUP_EXPECTED_ERROR = -11;
	public static final int IF_TOO_FEW_ELEMENTS_ERROR = -12;
	public static final int IF_NUMBER_AND_TWO_GROUPS_EXPECTED_ERROR = -13;
	public static final int IF_BOOLEAN_EXPECTED_ERROR = -14;
	public static final int CALL_TOO_FEW_ELEMENTS_ERROR = -15;
	public static final int CALL_STRING_EXPECTED_ERROR = -16;
	public static final int SOURCE_TOO_FEW_ELEMENTS_ERROR = -17;
	public static final int SOURCE_STRING_EXPECTED_ERROR = -18;
	public static final int SOURCE_EXECUTION_ERROR = -19;
	public static final int STATIC_EXECUTION_ERROR = -20;
	public static final int EXEC_EXECUTION_ERROR = -21;
	public static final int CALL_EXECUTION_ERROR = -22;
	public static final int REPEAT_EXECUTION_ERROR = -23;
	public static final int IF_EXECUTION_ERROR = -24;
	public static final int USER_GROUP_EXECUTION_ERROR = -25;
	
	// The stack
	private final Stack<Token> stack;
	
	// List of built-in functions
	private final WFunctionsList list;
	
	// User defined-groups
	private final HashMap<String, Token> env;
	
	// Names of current group and parent group
	private final String groupName;
	private final String parentName;
	
	// Call stack (for error printing)
	private final CallStack callStack;
	
	// Do we need to show errors
	private final boolean showError;

	public Interpreter(boolean showError, Stack<Token> stack, HashMap<String, Token> env, CallStack callStack, String groupName, String parentName) {
		this.showError = showError;
		this.stack = stack;
		list = new WFunctionsList();
		this.env = env;
		this.groupName = groupName;
		this.parentName = parentName;
		this.callStack = callStack;
	}

	public Interpreter(boolean showError) {
		this.showError = showError;
		stack = new Stack<>();
		list = new WFunctionsList();
		env = new HashMap<>();
		groupName = null; // The first group is set to null to don't prefix the keywords
		parentName = null;
		callStack = new CallStack();
	}

	/*
	 * Execute an array of tokens Return the status code
	 */
	public int execute(ArrayList<Token> instructions) {

		// Read the instructions
		for (Token t : instructions) {

			// If the token is a value or a code to put in the stack
			if (t instanceof TNumber || t instanceof TString || t instanceof TGroup) {
				stack.push(t);
			}

			// If it is a keyword, we first check in the reserved keywords, and in the
			// additional
			else if (t instanceof TKeyword) {

				String name = ((TKeyword) t).getName();
				String originSource = t.getOriginSource();
				int originLine = t.getOriginLine();

				// First, check for the language keywords
				if ("def".equals(name) || "global".equals(name) || "static".equals(name)) {

					// Try to add the keyword to the environment
					try {
						
						// Global or not
						boolean isGlobal = "global".equals(name);
						// Static or not
						boolean isStatic = "static".equals(name);

						// Get the elements
						Token code = stack.pop(), newName = stack.pop();

						// Check the type
						if (!(newName instanceof TString)) {
							if (showError) {
								System.err.println("Error: Keyword \"" + name + "\" expects a string as second stack value!\n" + callStack.getFullStack(name, originSource, originLine));
							}
							return DEF_STRING_EXPECTED_ERROR;
						}

						// Check the name
						String stringName = ((TString) newName).getValue();
						char[] arrayName = stringName.toCharArray();
						boolean error = false;
						if (arrayName.length == 0 || !Tokenizer.isKeywordStart(arrayName[0])) {
							error = true;
						} else {
							for (char c : arrayName) {
								if (!Tokenizer.isKeyword(c)) {
									error = true;
									break;
								}
							}
						}
						if (error) {
							if (showError) {
								System.err.println("Error: Keyword \"" + name + "\": the chosen name \"" + stringName
									+ "\" is not compatible with the keyword format!\n" + callStack.getFullStack(name, originSource, originLine));
							}
							return DEF_WRONG_KEYWORD_FORMAT_ERROR;
						} else {

							// Add the group to the environment with the correct group name
							String finalName = (groupName == null || isGlobal ? "" : groupName + ".") + stringName;
							env.put(finalName, code);
							
							// If the group is created with "static", call it once
							if (isStatic) {
								
								int returnCode = execute_from_env(stack, env, finalName, groupName, (TKeyword) t);
								if (returnCode != 0) {
									return STATIC_EXECUTION_ERROR;
								}
								
							}

						}

						// Catch the stack error
					} catch (EmptyStackException e) {
						if (showError) {
							System.err.println("Error: Keyword \"def\": the stack contains less than two elements!\n" + callStack.getFullStack(name, originSource, originLine));
						}
						return DEF_TOO_FEW_ELEMENTS_ERROR;
					}

				}

				// If it's an execution order
				else if ("exec".equals(name)) {

					// Execute the previous group in the stack
					try {

						Token a = stack.pop();

						// Check the type, only a group can be executed
						if (!(a instanceof TGroup)) {
							if (showError) {
								System.err.println("Error: Keyword \"exec\" expect an instruction group!\n" + callStack.getFullStack(name, originSource, originLine));
							}
							return EXEC_GROUP_EXPECTED_ERROR;

						}

						// Execute
						Interpreter newIt = new Interpreter(showError, stack, env, callStack.add(name, originSource, originLine), groupName, parentName); // Execute in the same group name
						int exitCode = newIt.execute(((TGroup) a).getList());

						// Check for an error
						if (exitCode != 0) {
							return EXEC_EXECUTION_ERROR;
						}

					} catch (EmptyStackException e) {
						if (showError) {
							System.err.println("Error: Keyword \"exec\": the stack contains less than one element!\n" + callStack.getFullStack(name, originSource, originLine));
						}
						return EXEC_TOO_FEW_ELEMENTS_ERROR;
					}

				}
				
				// If it's a call order (call a function or group from a string containing it's name)
				else if ("call".equals(name)) {
					
					// Execute the function from the name
					try {

						Token a = stack.pop();

						// Check the type, only a string can be called
						// To execute a group use "exec"
						if (!(a instanceof TString)) {
							if (showError) {
								System.err.println("Error: Keyword \"call\" expect a string!\n" + callStack.getFullStack(name, originSource, originLine));
							}
							return CALL_STRING_EXPECTED_ERROR;

						}

						// Execute with a single keyword
						String callName = ((TString) a).getValue();
						ArrayList<Token> singleton = new ArrayList<>();
						singleton.add(new TKeyword(callName));
						Interpreter newIt = new Interpreter(showError, stack, env, callStack.add(name, originSource, originLine), groupName, parentName); // Execute in the same group name
						int exitCode = newIt.execute(singleton);

						// Check for an error
						if (exitCode != 0) {
							return CALL_EXECUTION_ERROR;
						}

					} catch (EmptyStackException e) {
						if (showError) {
							System.err.println("Error: Keyword \"call\": the stack contains less than one element!\n" + callStack.getFullStack(name, originSource, originLine));
						}
						return CALL_TOO_FEW_ELEMENTS_ERROR;
					}
					
				}
				
				// If it's a loop
				else if ("repeat".equals(name)) {

					// Execute the previous group in the stack
					try {

						Token number = stack.pop(), code = stack.pop();

						// Check the type, only a group can be executed
						if (!(code instanceof TGroup) || !(number instanceof TNumber)) {
							if (showError) {
								System.err.println("Error: Keyword \"repeat\" expect a number and an instruction group!\n" + callStack.getFullStack(name, originSource, originLine));
							}
							return REPEAT_STRING_AND_GROUP_EXPECTED_ERROR;

						}

						// Execute...
						Interpreter newIt = new Interpreter(showError, stack, env, callStack.add(name, originSource, originLine), groupName, parentName); // Execute in the same group name
						int exitCode = 0, max = (int)((TNumber) number).getValue();
						ArrayList<Token> repeatInstructions = ((TGroup) code).getList();
						
						// ...in a loop
						for (int i = 0; i < max && exitCode == 0; i++) {
							exitCode = newIt.execute(repeatInstructions);
						}

						// Check for an error
						if (exitCode != 0) {
							return REPEAT_EXECUTION_ERROR;
						}

					} catch (EmptyStackException e) {
						if (showError) {
							System.err.println("Error: Keyword \"repeat\": the stack contains less than two elements!\n" + callStack.getFullStack(name, originSource, originLine));
						}
						return REPEAT_TOO_FEW_ELEMENTS_ERROR;
					}
					
				}
				
				// If it's a condition
				else if ("if".equals(name)) {

					// Execute the correct group based on the first value in the stack
					try {

						Token condition = stack.pop(), elseGroup = stack.pop(), ifGroup = stack.pop();

						// Check the types
						if (!(condition instanceof TNumber) || !(ifGroup instanceof TGroup) || !(elseGroup instanceof TGroup)) {
							if (showError) {
								System.err.println("Error: Keyword \"if\" expect a number and two instruction groups!\n" + callStack.getFullStack(name, originSource, originLine));
							}
							return IF_NUMBER_AND_TWO_GROUPS_EXPECTED_ERROR;

						}
						
						// Check the condition value
						double conditionValue = ((TNumber) condition).getValue();
						if (conditionValue != 0. && conditionValue != 1.) {
							if (showError) {
								System.err.println("Error: Keyword \"if\" expect 0 or 1 as condition argument!\n" + callStack.getFullStack(name, originSource, originLine));
							}
							return IF_BOOLEAN_EXPECTED_ERROR;
						}

						// Execute...
						Interpreter newIt = new Interpreter(showError, stack, env, callStack.add(name, originSource, originLine), groupName, parentName); // Execute in the same group name
						ArrayList<Token> ifInstructions = ((TGroup) (conditionValue == 1. ? ifGroup : elseGroup)).getList();
						
						// ...based on the evaluation
						int exitCode = newIt.execute(ifInstructions);

						// Check for an error
						if (exitCode != 0) {
							return IF_EXECUTION_ERROR;
						}

					} catch (EmptyStackException e) {
						if (showError) {
							System.err.println("Error: Keyword \"if\": the stack contains less than three elements!\n" + callStack.getFullStack(name, originSource, originLine));
						}
						return IF_TOO_FEW_ELEMENTS_ERROR;
					}
					
				}
				
				// If it's a prefix getter
				else if ("current".equals(name)) {
					
					String prefix = groupName == null ? "" : groupName;
					stack.push(new TString(prefix));
					
				// Parent prefix getter
				} else if ("parent".equals(name)) {
					
					String prefix = parentName == null ? "" : parentName;
					stack.push(new TString(prefix));

				}
				
				// Including a file
				else if ("source".equals(name)) {
					
					try {
						
						Token str = stack.pop();
						
						if (!(str instanceof TString)) {
							if (showError) {
								System.err.println("Error: Keyword \"source\" expect a string!\n" + callStack.getFullStack(name, originSource, originLine));
							}
							return SOURCE_STRING_EXPECTED_ERROR;
						}
						
						// Get the string
						String filePath = ((TString)str).getValue();
						
						// Prepare the path
						filePath = filePath.replace("@lib", "libs"); // TODO
						File file = new File(filePath);
						String absolutePath = file.getAbsolutePath();
						
						// Execute it in a new WSL object with the same interpreter but new tokenizer
						WSL wsl = new WSL(this, showError);
						int returnCode = wsl.executeFile(absolutePath);
						
						// Search for an error
						if (returnCode != 0) {
							// Only print the error stack, the message is printed during the tokenization, interpretation
							if (showError) {
								System.err.println(callStack.getFullStack(name, originSource, originLine));
							}
							return SOURCE_EXECUTION_ERROR;
						}
					
					} catch (EmptyStackException e) {
						if (showError) {
							System.err.println("Error: Keyword \"source\": the stack contains less than one element!\n" + callStack.getFullStack(name, originSource, originLine));
						}
						return SOURCE_TOO_FEW_ELEMENTS_ERROR;
					}
					
				}

				// If it's not a language keyword
				else {

					// Else, if it's a token we get the function to execute
					WFunction function = list.get(name);

					// If it's a language function
					if (function != null) {

						// Execute the function on the current stack
						if (!function.execute(stack, callStack.add(name, originSource, originLine))) {
							return FUNCTION_EXECUTION_ERROR;
						}

					}

					// If it's not a language function, then we check in the environment
					else {

						// If it's a user defined group
						if (env.containsKey(name)) {

							int code = execute_from_env(stack, env, name, groupName, (TKeyword) t);
							if (code != 0) {
								return code; // Return an interpreter error code
							}

						}

						// Else, fail
						else {
							if (showError) {
								System.err.println("Error: Undefined function name \"" + name + "\"!\n" + callStack.getFullStack(name, originSource, originLine));
							}
							return UNDEFINED_FUNCTION_ERROR;
						}

					}

				}

			}

			// Else, it's an unexpected object in the stack
			else {

				if (showError) {
					System.err.println("Error: Unexpected object in call stack!");
				}
				return UNEXPECTED_OBJECT_ERROR;

			}

		}

		return NO_ERROR;
	}
	
	// Execute a user defined group
	private int execute_from_env(Stack<Token> stack, HashMap<String, Token> env, String name, String parent, TKeyword t) {
		
		// Get information about the token
		String tName = t.getName();
		String tSource = t.getOriginSource();
		int tLine = t.getOriginLine();
		
		// Create a new interpreter, and execute the code on the current stack
		Interpreter newIt = new Interpreter(showError, stack, env, callStack.add(tName, tSource, tLine), name, parent);

		Token c = env.get(name);

		try {

			// Execute (prepare an ArrayList if needed)
			int exitCode;
			if (c instanceof TGroup) {
				exitCode = newIt.execute(((TGroup) c).getList());
			} else {
				ArrayList<Token> l2 = new ArrayList<>();
				l2.add(c);
				exitCode = newIt.execute(l2);
			}
			if (exitCode != 0) {
				return USER_GROUP_EXECUTION_ERROR;
			}

			// Prevent stack overflow
		} catch (StackOverflowError e) {
			if (showError) {
				System.err.println("Error: Stack overflow while calling \"" + name + "\"!\n" + callStack.getFullStack(tName, tSource, tLine));
			}
			return STACK_OVERFLOW_ERROR;
		}
		
		return NO_ERROR;
		
	}

	public void __debug_print_env() {
		if (env != null) {
			for (String k : env.keySet()) {
				System.out.println("\"" + k + "\" = " + env.get(k).toString());
			}
		}
	}

}
