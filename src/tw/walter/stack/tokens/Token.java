package tw.walter.stack.tokens;

public interface Token {
	
	// Function for the printing of errors
	public void setOrigin(String source, int line);
	public String getOriginSource();
	public int getOriginLine();
}
