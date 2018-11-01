package tw.walter.stack.tokens;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/*
 * Walter Stack Language - Group class
 * Represents a group token
 */
public class TGroup implements Token, Iterable<Token>, Iterator<Token> {

	private ArrayList<Token> elements;
	private int count = -1;

	public TGroup() {
		elements = new ArrayList<>();
	}

	public TGroup(ArrayList<Token> elements) {
		this.elements = elements;
	}

	public void add(Token t) {
		elements.add(t);
	}

	public String toString() {
		String content = "";
		for (Token t: elements) {
			content += t.toString() + " ";
		}
		content = content.substring(0, content.length() > 0 ? content.length()-1 : 0);
		return "TGroup<" + content + ">";
	}

	public ArrayList<Token> getList() {
		return elements;
	}

	/**
	 * Methods for iteration
	 */
	@Override
	public boolean hasNext() {
		return count < elements.size() - 1;
	}

	@Override
	public Token next() {
		if (count == elements.size() - 1) {
			throw new NoSuchElementException();
		}
		count++;
		return elements.get(count);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Token> iterator() {
		count = -1; // Reset the counter
		return this;
	}

}
