package tw.walter.stack.tokens;

/*
 * Walter Stack Language - Number class
 * Represents a number (all number are double precision floating points)
 */
public class TNumber implements Token {
    
    private double value;
    
    public TNumber(double value) {
        this.value = value;
    }
    
    public double getValue() {
        return value;
    }
    
    public String toString() {
        return "TNumber<" + value + ">";
    }

}
