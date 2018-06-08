import java.util.*;

public class Symbol {
	
	public enum Type {
		TERMINAL,
		NON_TERMINAL
	}
	
	private Type type;
	private String name;
	private Set<Production> productionsTo = new HashSet<Production>();
	
	/*
	 * Creates a new NonTerminalSymbol
	 * */
	public Symbol(String _name, Type _type) throws Exception {
		if (_name.length() == 1) {
			name = (_type == Type.NON_TERMINAL) ? _name.toUpperCase() : _name.toLowerCase();
			type = _type;
		} else {
			throw new Exception("NonTerminalSymbol needs to be a single character");
		}
	}
	
	/*
	 * Add a production starting with this symbol
	 * */
	public void addProductionTo(Production P) {
		productionsTo.add(P);
	}
	
	/*
	 * Get the productions starting with this symbol
	 * */
	public Set<Production> getProductionsTo() {
		return productionsTo;
	}
	
	/*
	 * Returns the name in String format
	 * */
	public String toString() {
		return name;
	}
	
	/*
	 * Returns the type of the Symbol
	 * */
	public Type getType() {
		return type;
	}

}
