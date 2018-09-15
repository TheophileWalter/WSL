package tw.walter.stack;

/*
 * Walter Stack Language - Allow to read a string char by char
 */
public class StringConsumer {

    private char[] s;
    private int offset;
    
    public StringConsumer(String s) {
        this.s = s.toCharArray();
        this.offset = 0;
    }
    
    public char get() {
        offset++;
        if (offset-1 >= s.length) {
           return '\0';
        }
        return s[offset-1];
    }
    
    public void reverse() {
        offset--;
    }
    
    public void reset() {
        offset = 0;
    }
    
}
