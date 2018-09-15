package tw.walter.stack;

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

		// Get the token list
		ArrayList<Token> tokens = this.tk.getTokens(code);

		// Check if the code has been parsed successfully
		if (tokens == null) {
			return -1;
		}

		// Execute the instructions
		it.execute(tokens);

		return 0;
	}

	public void __debug_print_env() {
		it.__debug_print_env();
	}

}
