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
    
    private Stack<Token> stack;
    private WFunctionsList list;
    private HashMap<String, Token> env;
    private String groupName;
    
    public Interpretor(Stack<Token> stack, HashMap<String, Token> env, String groupName) {
        this.stack = stack;
        this.env = env;
        this.groupName = groupName;
        list = new WFunctionsList();
    }
    
    public Interpretor() {
        list = new WFunctionsList();
        groupName = null; // The first group is set to null to don't prefix the keywords
        stack = new Stack<>();
        env = new HashMap<>();
    }
    
    /*
     * Execute an array of tokens
     * Return the status code
     */
    public int execute(ArrayList<Token> instructions) {
        
        // Create new stack and env if there is no given
        /*if (groupName == null) {
        
            // Create a new stack
            stack = new Stack<>();
            
            // Create the environment
            env = new HashMap<>();
            
        }*/
        
        // Read the instructions
        for (Token t: instructions) {
            
            // If the token is a value or a code to put in the stack
            if (t instanceof TNumber || t instanceof TString || t instanceof TGroup) {
                stack.push(t);
            }
                
            // If it is a keyword, we first check in the reserved keywords, and in the additional
            else if (t instanceof TKeyword) {
                
                String name = ((TKeyword) t).getName();
                
                // First, check for the language keywords
                if ("def".equals(name)) {
                    
                    // Try to add the keyword to the environment
                    try {
                        
                        // Get the elements
                        Token code = stack.pop(), newName = stack.pop();
                        
                        // Check the type
                        if (!(newName instanceof TString)) {
                            System.err.println("Error: Keyword \"def\" excpects a string as second stack value!");
                            return -4;
                        }
                        
                        // Check the name
                        String stringName = ((TString)newName).getValue();
                        char[] arrayName = stringName.toCharArray();
                        boolean error = false;
                        if (arrayName.length == 0 || !Tokenizer.isKeywordStart(arrayName[0])) {
                            error = true;
                        } else {
                            for (char c: arrayName) {
                                if (!Tokenizer.isKeyword(c)) {
                                    error = true;
                                    break;
                                }
                            }
                        }
                        if (error) {
                            System.err.println("Error: Keyword \"def\": the chosen name \"" + stringName + "\" is not compatible with the keyword format!");
                            return -5;
                        } else {
                            
                            // Add the group to the environment
                            env.put((groupName == null ? "" : groupName + ".") + stringName, code);
                            
                        }
                        
                    // Catch the stack error
                    } catch (EmptyStackException e) {
                        System.err.println("Error: Keywork \"def\": the stack contains less than two elements!");
                        return -3;
                    }
                    
                } else {
                
                    // Else, if it's a token we get the function to execute
                    WFunction function = list.get(name);
                    
                    // If it's a language function
                    if (function != null) {
                    
                        // Execute the function on the current stack
                        if (!function.execute(stack)) {
                            return -2;
                        }
                        
                    }
                    
                    // If it's not a language function, then we check in the environment
                    else {
                        
                        // If it's a user defined group
                        if (env.containsKey(name)) {
                            
                            // Create a new interpretor, and execute the code on the current stack
                            Interpretor newIt = new Interpretor(stack, env, name);
                            
                            Token c = env.get(name);
                            
                            try {
                            
	                            // Execute (prepare an ArrayList if needed
	                            int exitCode = 0;
	                            if (c instanceof TGroup) {
	                                exitCode = newIt.execute(((TGroup)c).getList());
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
                            	System.err.println("Error: Stack overflow while calling \"" + name + "\"!");
                                return -6;
                            }
                            
                        }
                        
                        // Else, fail
                        else {
                            System.err.println("Error: Undefined function name \"" + name + "\"!");
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
    
    public void __debug_print_env() {
        if (env != null) {
            for (String k: env.keySet()) {
                System.out.println("\"" + k + "\" = " + env.get(k).toString());
            }
        }
    }

}
