package tw.walter.stack.functions;

import java.util.HashMap;

/*
 * Walter Stack Language - WFunctionsList class
 * Associate keyword name to a function to execute
 */
public class WFunctionsList {

    private HashMap<String, WFunction> list;
    
    public WFunctionsList() {
        list = new HashMap<>();
        
        // Add the functions
        add("add",    new FAdd());
        add("sub",    new FSub());
        add("mul",    new FMul());
        add("div",    new FDiv());
        add("string", new FString());
        add("concat", new FConcat());
        add("print",  new FPrint());
        add("exch",   new FExch());
        add("pop",    new FPop());
        add("copy",   new FCopy());
        add("sqrt",   new FSqrt());
        
    }

    public WFunction get(String name) {
        if (!list.containsKey(name)) {
            return null;
        }
        return list.get(name);
    }
    
    private void add(String name, WFunction function) {
        list.put(name, function);
    }
    
}