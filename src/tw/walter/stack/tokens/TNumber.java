package tw.walter.stack.tokens;

/*
 * Walter Stack Language - Number class
 * Represents a number (all number are double precision floating points)
 */
public class TNumber implements Token {

	private double value;
	private String originSource;
	private int originLine;

	public TNumber(double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "TNumber<" + value + ">";
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
