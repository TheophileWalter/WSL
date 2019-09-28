package tw.walter.stack;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;

import tw.walter.stack.tokens.*;

/*
 * Walter Stack Language - Main class
 * Execute a given code from a string
 */
public class WSL {

	private Tokenizer tk;
	private Interpretor it;

	/*
	 * Constructor, instantiate the class
	 */
	public WSL(boolean showError) {
		this.tk = new Tokenizer(showError);
		this.it = new Interpretor();
	}

	/*
	 * Execute a code from a string Return the execution status code
	 */
	public int execute(String code) {
		return execute(code, "<execute>", false);
	}
	public int execute(String code, String source) {
		return execute(code, source, false);
	}
	public int execute(String code, String source, boolean isTopLevel) {

		// Get the token list
		ArrayList<Token> tokens = this.tk.getTokens(code, source, isTopLevel);

		// Check if the code has been parsed successfully
		if (tokens == null) {
			return -1;
		}

		// Execute the instructions
		it.execute(tokens);

		return 0;
	}
	
	/*
	 * Execute a code from a file
	 */
	public int executeFile(String path) {
		return execute(readFile(path), path);
	}
	
	// Read an entire file
	private static String readFile(String path) {
		try {
			File file = new File(path);
			FileInputStream fis = new FileInputStream(file);
			byte[] data = new byte[(int) file.length()];
			fis.read(data);
			fis.close();

			return new String(data, "UTF-8");
		} catch (Exception e) {
			System.err.println("Error: Unable to read file \"" + path + "\"");
			return "";
		}
	}
	
	/*
	 * Run an top-level interpretor in standard input/output
	 */
	public void topLevel() {
		
		// The scanner for reading stdin
		@SuppressWarnings( "resource" )
		Scanner scanner = new Scanner(System.in);
		
		// A string to save lave input in case of multiple line input
		String lastInput = "";
		
		// Loop to execute every given lines
		while (true) {
			
		    // Get the line and execute it
		    if (lastInput == "") {
		    	System.out.print("\nwsl> ");
		    }
		    String inputString = scanner.nextLine();
		    int returnCode = this.execute(lastInput + "\n" + inputString, "<stdin>", true);
		    
		    // If the error is a end of stream error, we let the user print a new line
		    if (returnCode == -1 &&
		    		(this.tk.getLastError() == Tokenizer.END_OF_STREAM_IN_STRING_ERROR ||
		    		this.tk.getLastError() == Tokenizer.END_OF_STREAM_IN_GROUP_ERROR)) {
		    	lastInput += inputString + "\n";
		    } else {
		    	lastInput = "";
		    }
		    
		}
		
	}

	public void __debug_print_env() {
		it.__debug_print_env();
	}

}
