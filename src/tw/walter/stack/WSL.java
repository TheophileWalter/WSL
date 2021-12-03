package tw.walter.stack;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

import tw.walter.stack.tokens.*;

/*
 * Walter Stack Language - Main class
 * Execute a given code from a string
 */
public class WSL {
	
	// Public information about WSL
	public static final String  WSL_VERSION          = "1.0 beta";
	public static final int     WSL_VERSION_CODE     = 10;
	public static final boolean WSL_IS_ALPHA         = false;
	public static final boolean WSL_IS_BETA          = true;
    public static final String  WSL_LIB_VERSION      = "1.0";
    public static final int     WSL_LIB_VERSION_CODE = 10;
    public static final String  WSL_COPYRIGHT        = "Copyright 2021 - Théophile Walter";
	
	// Private attributes
	private final Tokenizer tk;
	private final Interpreter it;
	private final Scanner scanner = new Scanner(System.in);

	/*
	 * Constructor, instantiate the class
	 */
	public WSL() {
		this.tk = new Tokenizer(true);
		this.it = new Interpreter(scanner, true);
	}
	public WSL(boolean showError) {
		this.tk = new Tokenizer(showError);
		this.it = new Interpreter(scanner, showError);
	}
	public WSL(Interpreter it, boolean showError) {
		this.tk = new Tokenizer(showError);
		this.it = it;
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
		
		// Code may be null if the file to read don't exists
		if (code == null) {
			return -1;
		}

		// Get the token list
		ArrayList<Token> tokens = this.tk.getTokens(code, source, isTopLevel);

		// Check if the code has been parsed successfully
		if (tokens == null) {
			return -1;
		}

		// Execute the instructions
		int resultCode = it.execute(tokens);
		
		if (resultCode != 0) {
			return -1;
		}

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
			if (fis.read(data) <= 0) {
				System.err.println("Warning: No data in file  \"" + path + "\"");
			}
			fis.close();

			return new String(data, StandardCharsets.UTF_8).replaceAll("\\r\\n", "\n").replaceAll("\\r", "\n");
		} catch (Exception e) {
			System.err.println("Error: Unable to read file \"" + path + "\"");
			return null;
		}
	}
	
	/*
	 * Run an top-level interpreter in standard input/output
	 */
	public void topLevel() {
		
		// A string to save lave input in case of multiple line input
		String lastInput = "";
		
		System.out.println("WSL version " + WSL_VERSION + "\n" + WSL_COPYRIGHT);
		
		// Loop to execute every given lines
		//noinspection InfiniteLoopStatement
		while (true) {
			
		    // Get the line and execute it
		    if ("".equals(lastInput)) {
		    	System.out.print("\nwsl> ");
		    }
		    String inputString = scanner.nextLine();
		    int returnCode = this.execute(lastInput + inputString, "<stdin>", true);
		    
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

	//noinspection unused
	public void __debug_print_env() {
		it.__debug_print_env();
	}

}
