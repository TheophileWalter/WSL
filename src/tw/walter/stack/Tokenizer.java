package tw.walter.stack;

import java.util.ArrayList;

import tw.walter.stack.tokens.*;

public class Tokenizer implements StringConsumerListener {
	
	// Error constants
	public static final int NO_ERROR = 0;
	public static final int UNEXPECTED_CHARACTER_ERROR = 1;
	public static final int MULTIPLE_DOTS_IN_DECIMAL_ERROR = 2;
	public static final int END_OF_STREAM_IN_STRING_ERROR = 3;
	public static final int END_OF_STREAM_IN_GROUP_ERROR = 4;
	public static final int UNEXPECTED_CHARACTER_IN_COMMENT_ERROR = 5;
	public static final int NEGATIVE_NUMBER_ERROR = 6;

	// Private attributes
	private StringConsumer sc;
	private ArrayList<Token> tokens;
	private String source;
	private int currentLine;
	private final boolean showError;
	private boolean isTopLevel;
	private int lastError;

	public Tokenizer(boolean showError) {
		this.showError = showError;
	}

	/*
	 * Return a token list from a code string
	 */
	public ArrayList<Token> getTokens(String s, String source, boolean isTopLevel) {
		return getTokens(s, source, isTopLevel, 1);
	}
	public ArrayList<Token> getTokens(String s, String source, boolean isTopLevel, int currentLine) {
		
		// The last error
		this.lastError = NO_ERROR;
		this.isTopLevel = isTopLevel;
		
		// Set up the source information
		this.source = source;
		this.currentLine = currentLine;

		// Create a new StringConsumer
		sc = new StringConsumer(s);
		
		// Set itself as a listener on the StringConsumer to increment or decrement the line count
		sc.setListener(this);

		// The token list to be returned
		tokens = new ArrayList<>();

		// Enter in the main iteration function
		mainLoop();

		return tokens;
	}
	
	/*
	 * Get the last error code
	 */
	public int getLastError() {
		return this.lastError;
	}

	/*
	 * Main loop, iter over tokens
	 */
	private void mainLoop() {
		char c = sc.get();
		while (c != '\0') {

			// Try to identify the first char

			// A keyword starting character
			if (isKeywordStart(c)) {
				if (!keywordLoop(c)) {
					tokens = null;
					return;
				}
			}

			// A number
			else if (isNumberStart(c)) {
				if (!numberLoop(c)) {
					tokens = null;
					return;
				}
			}

			// A string starting character
			else if (isStringDelimiter(c)) {
				if (!stringLoop()) {
					tokens = null;
					return;
				}
			}

			// A group starting character
			else if (isGroupStart(c)) {
				if (!groupLoop()) {
					tokens = null;
					return;
				}
			}

			// A comment
			else if (isCommentStart(c)) {
				if (!commentLoop()) {
					tokens = null;
					return;
				}
			}

			// A blank character (space, tabulation, new line)
			else if (!isBlank(c)) {
				if (this.showError) {
					System.err.println("Error: Unexpected character '" + c + "'!\nSource \"" + source + "\" line " + currentLine);
				}
				this.lastError = UNEXPECTED_CHARACTER_ERROR;
				tokens = null;
				return;
			}

			c = sc.get();
		}
	}

	/*
	 * Iterate to get an identifier
	 */
	private boolean keywordLoop(char firstChar) {
		StringBuilder keyword = new StringBuilder(Character.toString(firstChar));
		int lineAtStart = currentLine; // Saves the line number of the start of the keyword
		char c = sc.get();
		while (isKeyword(c)) {
			keyword.append(c);
			c = sc.get();
		}
		// Reverse last char (to be able to get it out of this function)
		sc.reverse();
		// Add the keyword to the result
		TKeyword k = new TKeyword(keyword.toString());
		k.setOrigin(source, lineAtStart);
		tokens.add(k);
		return true;
	}

	private boolean numberLoop(char firstChar) {
		StringBuilder number = new StringBuilder(Character.toString(firstChar));
		int lineAtStart = currentLine; // Saves the line number of the start of the number
		boolean decimal = false;
		char c = sc.get();
		while (isNumber(c) || isDecimalSeparator(c)) {
			// Two dots in a number is not possible
			if (decimal && isDecimalSeparator(c)) {
				if (this.showError) {
					System.err.println("Error: A decimal number must contains only one dot!\nSource \"" + source + "\" line " + currentLine);
				}
				this.lastError = MULTIPLE_DOTS_IN_DECIMAL_ERROR;
				return false;
			} else if (isDecimalSeparator(c)) {
				decimal = true;
			}
			number.append(c);
			c = sc.get();
		}
		// Reverse last char (to be able to get it out of this function)
		sc.reverse();
		// Check if the number is only "-"
		if ("-".equals(number.toString()) || "-.".equals(number.toString())) {
			if (this.showError) {
				System.err.println("Error: A number is expected after \"-\"!\nSource \"" + source + "\" line " + currentLine);
			}
			this.lastError = NEGATIVE_NUMBER_ERROR;
			return false;
		}
		// Add the keyword to the result
		TNumber n = new TNumber(Double.parseDouble(number.toString()));
		n.setOrigin(source, lineAtStart);
		tokens.add(n);
		return true;
	}

	private boolean stringLoop() {
		StringBuilder string = new StringBuilder(); // Skip the first char (because it's a \")
		int lineAtStart = currentLine; // Saves the line number of the start of the string
		char c = sc.get();
		while (!isStringDelimiter(c)) {

			// Check for end of code
			if (c == '\0') {
				if (this.showError && !this.isTopLevel) {
					System.err.println("Error: End of stream reached while scanning string!\nSource \"" + source + "\" line " + currentLine);
				}
				this.lastError = END_OF_STREAM_IN_STRING_ERROR;
				return false;
			}

			// Check for escape
			if (isStringEscape(c)) {
				// Append directly the next character
				c = sc.get();
				// Check for end of code
				switch (c) {
				case '\0':
					if (this.showError && !this.isTopLevel) {
						System.err.println("Error: End of stream reached while scanning string!\nSource \"" + source + "\" line " + currentLine);
					}
					this.lastError = END_OF_STREAM_IN_STRING_ERROR;
					return false;
				case 'n':
					c = '\n';
					break;
				case 't':
					c = '\t';
					break;
				}
			}

			string.append(c);
			c = sc.get();
		}
		// No reverse needed because the last character is a \"
		// Add the keyword to the result
		TString s = new TString(string.toString());
		s.setOrigin(source, lineAtStart);
		tokens.add(s);
		return true;
	}

	public boolean groupLoop() {

		StringBuilder string = new StringBuilder(); // Skip the first char
		int lineAtStart = currentLine; // Saves the line number of the start of the group
		// Reads the string in the group
		char c = sc.get();
		int balance = 0;
		while (!isGroupEnd(c) || balance != 0) {

			// The two following conditions allows to define groups inside groups

			// If a new group is opened
			if (isGroupStart(c)) {
				balance++;
			}

			// If a group is closed
			if (isGroupEnd(c)) {
				balance--;
			}

			// Check for end of code
			if (c == '\0') {
				if (this.showError && !this.isTopLevel) {
					System.err.println("Error: End of stream reached while scanning group!\nSource \"" + source + "\" line " + currentLine);
				}
				this.lastError = END_OF_STREAM_IN_GROUP_ERROR;
				return false;
			}

			string.append(c);
			c = sc.get();
		}

		// No reverse needed because the last character is a closing character

		// Analyze the tokens in the group
		Tokenizer ntk = new Tokenizer(this.showError);
		ArrayList<Token> groupList = ntk.getTokens(string.toString(), source, this.isTopLevel, lineAtStart);

		// Check for an error
		if (groupList == null) {
			return false;
		}

		// Add the group to the result
		TGroup g = new TGroup(groupList);
		g.setOrigin(source, lineAtStart);
		tokens.add(g);

		return true;
	}

	public boolean commentLoop() {

		char c = sc.get();

		// A comment must start with two *
		if (!isCommentStart(c)) {
			
			// Special error if the character is a line break
			String displayedC = Character.toString(c);
			int displayedLine = currentLine;
			if (isLineBreak(c)) {
				if (c == '\n') {
					displayedC = "\\n";
				} else if (c == '\r') {
					displayedC = "\\r";
				}
				displayedLine = currentLine-1;
			}
			
			// Print the error
			if (this.showError) {
				System.err.println("Error: Unexpected character '" + displayedC + "' after '*'!\nSource \"" + source + "\" line " + displayedLine);
			}
			this.lastError = UNEXPECTED_CHARACTER_IN_COMMENT_ERROR;
			return false;
		}

		// Accept all characters except the end
		while (!isCommentEnd(c)) {
			c = sc.get();
		}

		// No reverse needed because the last character is an end character

		return true;
	}

	public static boolean isLetter(char c) {
		return (c >= 65 && c <= 90) || (c >= 97 && c <= 122);
	}
	
	public static boolean isNumberStart(char c) {
		return c == '-' || isNumber(c);
	}

	public static boolean isNumber(char c) {
		return c >= 48 && c <= 57;
	}

	public static boolean isKeywordStart(char c) {
		return c == '_' || isLetter(c);
	}

	public static boolean isKeywordSeparator(char c) {
		return c == '_' || c == '.';
	}

	public static boolean isKeyword(char c) {
		return isLetter(c) || isNumber(c) || isKeywordSeparator(c);
	}

	public static boolean isBlank(char c) {
		return c == ' ' || c == '\t' || c == '\n' || c == '\r';
	}

	public static boolean isStringDelimiter(char c) {
		return c == '"';
	}

	public static boolean isStringEscape(char c) {
		return c == '\\';
	}

	public static boolean isDecimalSeparator(char c) {
		return c == '.';
	}

	public static boolean isGroupStart(char c) {
		return c == '(';
	}

	public static boolean isGroupEnd(char c) {
		return c == ')';
	}

	public static boolean isCommentStart(char c) {
		return c == '*';
	}

	public static boolean isCommentEnd(char c) {
		return c == '\n' || c == '\r' || c == '\0';
	}
	
	public static boolean isLineBreak(char c) {
		return c == '\n' || c == '\r';
	}
	
	// Functions used as a callback from StringConsumer
	@Override
	public void onStringConsumerGet(char c) {
		if (isLineBreak(c)) {
			currentLine++;
		}
	}

	@Override
	public void onStringConsumerReverse(char c) {
		if (isLineBreak(c)) {
			currentLine--;
		}
	}

}
