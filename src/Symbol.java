import java.util.*;

public class Symbol {
	
	public enum Type {
		TERMINAL,
		NON_TERMINAL
	}
	
	private Type type;
	private String name;
	private Set<Production> productionsTo = new HashSet<Production>();
	private Set<Symbol> first = new HashSet<Symbol>();
	private Set<Symbol> follow = new HashSet<Symbol>();
	private Boolean fertile = false;
	
	/*
	 * Creates a new Symbol
	 * */
	public Symbol(String _name, Type _type) {
		name = (_type == Type.NON_TERMINAL) ? _name.toUpperCase() : _name.toLowerCase();
		type = _type;
	}
	
	/*
	 * Verifies if the symbol is fertile
	 * */
	public Boolean isFertile() {
		for (Production P: productionsTo) {
			Boolean temp = true;
			for (Symbol b: P.getDestiny()) {
				if (b.getType() == Symbol.Type.NON_TERMINAL && !b.getFertile()) {
					temp = false;
					break;
				}
			}
			if (temp) {
				fertile = true;
				return true;
			}
		}
		
		return false;
	}
	
	/*
	 * Auxiliary function
	 * */
	public Boolean getFertile() {
		return fertile;
	}
	
	/*
	 * Find the FIRST of the symbol
	 * */
	private void calculateFirst() {
		// TODO Fix
		first.clear();
		if (type == Type.TERMINAL) {
			first.add(this);
		} else {
			for (Production P: productionsTo) {
				for (Symbol s: P.getDestiny()) {
					if (s.getType() == Type.TERMINAL) {
						first.add(s);
						break;
					} else {
						Set<Symbol> firstY = s.getFirst();
						first.addAll(firstY);
						
						Boolean existEpsilon = false;
						for (Symbol tmp: firstY) {
							if (tmp.toString().equals("&")) {
								existEpsilon = true;
								break;
							}
						}
						
						if (!existEpsilon) {
							break;
						}
					}
				}
			}
		}
	}
	
	/*
	 * Returns the FIRST of the symbol
	 * */
	public Set<Symbol> getFirst() {
		calculateFirst();
		return first;
	}
	
	/*
	 * Find the FOLLOW of the symbol
	 * */
	private void calculateFollow() {
		// TODO
	}
	
	/*
	 * Returns the FOLLOW of the symbol
	 * */
	public Set<Symbol> getFollow() {
		calculateFollow();
		return follow;
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
