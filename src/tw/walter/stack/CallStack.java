package tw.walter.stack;

import java.util.ArrayList;

import javafx.util.Pair;

/*
 * Manage the call stack for printing debug
 */
public class CallStack {
	
	private ArrayList<Pair<String, Pair<String, Integer>>> list; // (functionName, (Source, Line))
	
	public CallStack() {
		list = new ArrayList<>();
	}
	
	// Add an element
	// Will not modify the current stack, only returns a new one with the new value
	public CallStack add(String name, String source, int line) {
		
		// Create a new stack with current values
		CallStack n = new CallStack();
		for (Pair<String, Pair<String, Integer>> e: list) {
			n._add(e);
		}
		
		// Add the new value
		n._add(new Pair<>(name, new Pair<>(source, line)));
		
		// Return the new stack
		return n;
		
	}
	
	// Add an element to the stack
	public void _add(Pair<String, Pair<String, Integer>> v) {
		list.add(v);
	}
	
	// Get the current stack as string
	@Override
	public String toString() {
		String r = "";
		for (Pair<String, Pair<String, Integer>> e: list) {
			r = "function \"" + e.getKey() + "\" from source\n\t\"" + e.getValue().getKey() + "\" at line " + e.getValue().getValue().toString() + "\n" + r;
		}
		return r;
	}
	
	// Returns the full stack with last token as a parameter
	public String getFullStack(String name, String source, int line) {
		return this.add(name, source, line).toString();
	}

}
