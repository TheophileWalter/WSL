package tw.walter.stack.tokens;

/*
 * Walter Stack Language - String class
 * Represents a string token
 */
public class TString implements Token {

	private String value;

	public TString(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public String toString() {
		return "TString<" + value + ">";
	}

}
