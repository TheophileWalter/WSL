package tw.walter.stack;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Stack;

import tw.walter.stack.functions.WFunctionsList;
import tw.walter.stack.functions.WFunction;
import tw.walter.stack.tokens.*;

/*
 * Walter Stack Language - Interpretor
 * Execute an array of tokens as a WSL code
 */
public class Interpretor {

	// The stack
	private Stack<Token> stack;
	
	// List of bult-in functions
	private WFunctionsList list;
	
	// User defined-groups
	private HashMap<String, Token> env;
	
	// Names of current group and parent group
	private String groupName;
	private String parentName;
	
	// Call stack (for error printing)
	private CallStack callStack;

	public Interpretor(Stack<Token> stack, HashMap<String, Token> env, CallStack callStack, String groupName, String parentName) {
		this.stack = stack;
		list = new WFunctionsList();
		this.env = env;
		this.groupName = groupName;
		this.parentName = parentName;
		this.callStack = callStack;
	}

	public Interpretor() {
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
				String originSource = ((TKeyword) t).getOriginSource();
				int originLine = ((TKeyword) t).getOriginLine();

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
							System.err.println("Error: Keyword \"" + name + "\" excpects a string as second stack value!\n" + callStack.getFullStack(name, originSource, originLine));
							return -4;
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
							System.err.println("Error: Keyword \"" + name + "\": the chosen name \"" + stringName
									+ "\" is not compatible with the keyword format!\n" + callStack.getFullStack(name, originSource, originLine));
							return -5;
						} else {

							// Add the group to the environment with the correct group name
							String finalName = (groupName == null || isGlobal ? "" : groupName + ".") + stringName;
							env.put(finalName, code);
							
							// If the group is created with "static", call it once
							if (isStatic) {
								
								int returnCode = execute_from_env(stack, env, finalName, groupName, (TKeyword) t);
								if (returnCode != 0) {
									return returnCode;
								}
								
							}

						}

						// Catch the stack error
					} catch (EmptyStackException e) {
						System.err.println("Error: Keywork \"def\": the stack contains less than two elements!\n" + callStack.getFullStack(name, originSource, originLine));
						return -3;
					}

				}

				// If it's an execution order
				else if ("exec".equals(name)) {

					// Execute the previous group in the stack
					try {

						Token a = stack.pop();

						// Check the type, only a group can be executed
						if (!(a instanceof TGroup)) {

							System.err.println("Error: Keyword \"exec\" excpect an instruction group!\n" + callStack.getFullStack(name, originSource, originLine));
							return -8;

						}

						// Execute
						Interpretor newIt = new Interpretor(stack, env, callStack.add(name, originSource, originLine), groupName, parentName); // Execute in the same group name
						int exitCode = newIt.execute(((TGroup) a).getList());

						// Check for an error
						if (exitCode != 0) {
							return exitCode;
						}

					} catch (EmptyStackException e) {
						System.err.println("Error: Keyword \"exec\": the stack contains less than one element!\n" + callStack.getFullStack(name, originSource, originLine));
						return -7;
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

							System.err.println("Error: Keyword \"call\" excpect a string!\n" + callStack.getFullStack(name, originSource, originLine));
							return -15;

						}

						// Execute with a single keyword
						String callName = ((TString) a).getValue();
						ArrayList<Token> singleton = new ArrayList<Token>();
						singleton.add(new TKeyword(callName));
						Interpretor newIt = new Interpretor(stack, env, callStack.add(name, originSource, originLine), groupName, parentName); // Execute in the same group name
						int exitCode = newIt.execute(singleton);

						// Check for an error
						if (exitCode != 0) {
							return exitCode;
						}

					} catch (EmptyStackException e) {
						System.err.println("Error: Keyword \"call\": the stack contains less than one element!\n" + callStack.getFullStack(name, originSource, originLine));
						return -14;
					}
					
				}
				
				// If it's a loop
				else if ("repeat".equals(name)) {

					// Execute the previous group in the stack
					try {

						Token number = stack.pop(), code = stack.pop();

						// Check the type, only a group can be executed
						if (!(code instanceof TGroup) || !(number instanceof TNumber)) {

							System.err.println("Error: Keyword \"repeat\" excpect a number and an instruction group!\n" + callStack.getFullStack(name, originSource, originLine));
							return -10;

						}

						// Execute...
						Interpretor newIt = new Interpretor(stack, env, callStack.add(name, originSource, originLine), groupName, parentName); // Execute in the same group name
						int exitCode = 0, max = (int)((TNumber) number).getValue();
						ArrayList<Token> repeatInstructions = ((TGroup) code).getList();
						
						// ...in a loop
						for (int i = 0; i < max && exitCode == 0; i++) {
							exitCode = newIt.execute(repeatInstructions);
						}

						// Check for an error
						if (exitCode != 0) {
							return exitCode;
						}

					} catch (EmptyStackException e) {
						System.err.println("Error: Keyword \"repeat\": the stack contains less than two elements!\n" + callStack.getFullStack(name, originSource, originLine));
						return -9;
					}
					
				}
				
				// If it's a condition
				else if ("if".equals(name)) {

					// Execute the correct group based on the first value in the stack
					try {

						Token condition = stack.pop(), elseGroup = stack.pop(), ifGroup = stack.pop();

						// Check the types
						if (!(condition instanceof TNumber) || !(ifGroup instanceof TGroup) || !(elseGroup instanceof TGroup)) {

							System.err.println("Error: Keyword \"if\" excpect a number and two instruction groups!\n" + callStack.getFullStack(name, originSource, originLine));
							return -12;

						}
						
						// Check the condition value
						double conditionValue = ((TNumber) condition).getValue();
						if (conditionValue != 0. && conditionValue != 1.) {
							System.err.println("Error: Keyword \"if\" excpect 0 or 1 as condition argument!\n" + callStack.getFullStack(name, originSource, originLine));
							return -13;
						}

						// Execute...
						Interpretor newIt = new Interpretor(stack, env, callStack.add(name, originSource, originLine), groupName, parentName); // Execute in the same group name
						ArrayList<Token> ifInstructions = ((TGroup) (conditionValue == 1. ? ifGroup : elseGroup)).getList();
						
						// ...based on the evaluation
						int exitCode = newIt.execute(ifInstructions);

						// Check for an error
						if (exitCode != 0) {
							return exitCode;
						}

					} catch (EmptyStackException e) {
						System.err.println("Error: Keyword \"if\": the stack contains less than three elements!\n" + callStack.getFullStack(name, originSource, originLine));
						return -11;
					}
					
				}
				
				// If it's a prefix getter
				else if ("group_prefix".equals(name)) {
					
					String prefix = groupName == null ? "" : groupName + ".";
					stack.push(new TString(prefix));
					
				// Parent prefix getter
				} else if ("parent_prefix".equals(name)) {
					
					String prefix = parentName == null ? "" : parentName + ".";
					stack.push(new TString(prefix));

				}

				// If it's not a language keyword
				else {

					// Else, if it's a token we get the function to execute
					WFunction function = list.get(name);

					// If it's a language function
					if (function != null) {

						// Execute the function on the current stack
						if (!function.execute(stack, callStack.add(name, originSource, originLine))) {
							return -2;
						}

					}

					// If it's not a language function, then we check in the environment
					else {

						// If it's a user defined group
						if (env.containsKey(name)) {

							int code = execute_from_env(stack, env, name, groupName, (TKeyword) t);
							if (code != 0) {
								return code;
							}

						}

						// Else, fail
						else {
							System.err.println("Error: Undefined function name \"" + name + "\"!\n" + callStack.getFullStack(name, originSource, originLine));
							return -1;
						}

					}

				}

			}

			// Else, it's an unexpected object in the stack
			else {

				System.err.println("Error: Unexpected object in call stack!");
				return -3;

			}

		}

		return 0;
	}
	
	// Execute a user defined group
	private int execute_from_env(Stack<Token> stack, HashMap<String, Token> env, String name, String parent, TKeyword t) {
		
		// Get informations about the token
		String tName = t.getName();
		String tSource = t.getOriginSource();
		int tLine = t.getOriginLine();
		
		// Create a new interpretor, and execute the code on the current stack
		Interpretor newIt = new Interpretor(stack, env, callStack.add(tName,  tSource, tLine), name, parent);

		Token c = env.get(name);

		try {

			// Execute (prepare an ArrayList if needed)
			int exitCode = 0;
			if (c instanceof TGroup) {
				exitCode = newIt.execute(((TGroup) c).getList());
			} else {
				ArrayList<Token> l2 = new ArrayList<>();
				l2.add(c);
				exitCode = newIt.execute(l2);
			}
			if (exitCode != 0) {
				return exitCode;
			}

			// Prevent stack overflow
		} catch (StackOverflowError e) {
			System.err.println("Error: Stack overflow while calling \"" + name + "\"!\n" + callStack.getFullStack(tName, tSource, tLine));
			return -6;
		}
		
		return 0;
		
	}

	public void __debug_print_env() {
		if (env != null) {
			for (String k : env.keySet()) {
				System.out.println("\"" + k + "\" = " + env.get(k).toString());
			}
		}
	}

}
