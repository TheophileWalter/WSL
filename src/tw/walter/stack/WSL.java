package tw.walter.stack;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

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
	public WSL() {
		this.tk = new Tokenizer();
		this.it = new Interpretor();
	}

	/*
	 * Execute a code from a string Return the execution status code
	 */
	public int execute(String code) {
		return execute(code, "<execute>");
	}
	public int execute(String code, String source) {

		// Get the token list
		ArrayList<Token> tokens = this.tk.getTokens(code, source);

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

	public void __debug_print_env() {
		it.__debug_print_env();
	}

}
