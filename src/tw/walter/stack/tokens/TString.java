package tw.walter.stack.tokens;

/*
 * Walter Stack Language - String class
 * Represents a string token
 */
public class TString implements Token {

	private String value;
	private String originSource;
	private int originLine;

	public TString(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "TString<\"" + escaped() + "\">";
	}
	
	public String escaped() {
		return value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
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
