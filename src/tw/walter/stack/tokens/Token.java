package tw.walter.stack.tokens;

public interface Token {
	
	// Function for the printing of errors
	void setOrigin(String source, int line);
	String getOriginSource();
	int getOriginLine();
}
