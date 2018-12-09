package tw.walter.stack.tokens;

/*
 * Walter Stack Language - Keyword class
 * Represents a keyword token (function or variable)
 */
public class TKeyword implements Token {

	private String name;
	private String originSource;
	private int originLine;

	public TKeyword(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return "TKeyword<" + name + ">";
	}

	/*
	 * Functions needed to implement Token
	 */
	
	@Override
	public void setOrigin(String source, int line) {
		originSource = source;
		originLine = line;
	}

	@Override
	public String getOriginSource() {
		return originSource;
	}

	@Override
	public int getOriginLine() {
		return originLine;
	}

}
