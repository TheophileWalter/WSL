package tw.walter.stack.tokens;

/*
 * Walter Stack Language - Keyword class
 * Represents a keyword token (function or variable)
 */
public class TKeyword implements Token {

    private String name;
    
    public TKeyword(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public String toString() {
        return "TKeyword<" + name + ">";
    }
    
}
