package tw.walter.stack;

/*
 * Walter Stack Language - Allow to read a string char by char
 */
public class StringConsumer {

	private char[] s;
	private int offset;
	private StringConsumerListener listener;

	public StringConsumer(String s) {
		this.s = s.toCharArray();
		this.offset = 0;
		listener = null;
	}

	public char get() {
		offset++;
		if (listener != null) {
			listener.onStringConsumerGet(arrayTidyRead(s, offset-1));
		}
		return arrayTidyRead(s, offset-1);
	}

	public void reverse() {
		if (listener != null) {
			listener.onStringConsumerReverse(arrayTidyRead(s, offset-1));
		}
		offset--;
	}

	public void reset() {
		offset = 0;
	}
	
	private char arrayTidyRead(char[] array, int index) {
		if (index < 0 || index >= array.length) {
			return '\0';
		}
		return array[index];
	}
	
	// Only one listener is possible
	public void setListener(StringConsumerListener listener) {
		this.listener = listener;
	}
	
	public void removeListener() {
		this.listener = null;
	}

}
