import java.util.*;

public class Grammar {

	private Set<Symbol> nonTerminalSymbols = new HashSet<Symbol>();
	private Symbol initialSymbol = null;
	
	/*
	 * Creates a new context-free grammar
	 * */
	public Grammar() {
		
	}
	
	/*
	 * Add a production to the grammar
	 * */
	public void addProduction(Production P) {
		initialSymbol = (initialSymbol == null) ? P.getFrom() : initialSymbol;
		nonTerminalSymbols.add(P.getFrom());
		P.getFrom().addProductionTo(P);
	}
	
	/*
	 * Returns the fertile symbols
	 * */
	private Set<Symbol> getFertileSymbols() {
		Set<Symbol> ret = new HashSet<Symbol>();
		Boolean hasChange = true;
		
		while (hasChange) {
			hasChange = false;
			for (Symbol nT: nonTerminalSymbols) {
				if (nT.isFertile()) {
					if (!ret.contains(nT)) {
						ret.add(nT);
						hasChange = true;
					}
				}
			}
		}
		
		return ret;
	}
	
	/*
	 * Returns the reachable symbols
	 * */
	private Set<Symbol> getReachableSymbols() {
		Set<Symbol> ret = new HashSet<Symbol>();
		Boolean hasChange = true;
		
		ret.add(initialSymbol);
		
		while (hasChange) {
			hasChange = false;
			Set<Symbol> toAdd = new HashSet<Symbol>();
			for (Symbol S: ret) {
				for (Production P: S.getProductionsTo()) {
					for (Symbol nT: P.getDestiny()) {
						if (nT.getType() == Symbol.Type.NON_TERMINAL) {
							if (!ret.contains(nT)) {
								toAdd.add(nT);
								hasChange = true;
							}
						}
					}
				}
			}
			ret.addAll(toAdd);
		}
		
		return ret;
	}
	
	/*
	 * Remove unreachable symbols
	 * */
	private void removeUnreachable() {
		Set<Symbol> reachable = getReachableSymbols();
		
		Set<Symbol> symbolsToRemove = new HashSet<Symbol>();
		
		for (Symbol nT: nonTerminalSymbols) {
			if (!reachable.contains(nT)) {
				symbolsToRemove.add(nT);
			}
		}

		nonTerminalSymbols.removeAll(symbolsToRemove);
	}
	
	/*
	 * Remove non-fertile symbols
	 * */
	private void removeNonFertile() {
		Set<Symbol> fertile = getFertileSymbols();
		
		Set<Symbol> symbolsToRemove = new HashSet<Symbol>();
		
		for (Symbol nT: nonTerminalSymbols) {
			if (!fertile.contains(nT)) {
				symbolsToRemove.add(nT);
				
				for (Symbol S: nonTerminalSymbols) {
					Set<Production> prodsToRemove = new HashSet<Production>();
					
					for (Production P: S.getProductionsTo()) {
						for (Symbol D: P.getDestiny()) {
							if (D == nT) {
								prodsToRemove.add(P);
								break;
							}
						}
					}
					S.getProductionsTo().removeAll(prodsToRemove);
				}
			}
		}
		
		nonTerminalSymbols.removeAll(symbolsToRemove);
	}
	
	/*
	 * Verifies if the grammar is empty
	 * */
	public Boolean isEmpty() {
		return !getFertileSymbols().contains(initialSymbol);
	}
	
	/*
	 * Remove the useless symbols
	 * */
	public void removeUseless() {
		removeNonFertile();
		removeUnreachable();
	}
	
	/*
	 * Builds the auxiliary set to epsilon-free transformation
	 * */
	private Set<Symbol> buildNeSet() {
		Set<Symbol> Ne = new HashSet<Symbol>();
		Boolean hasChange = true;
		
		while (hasChange) {
			hasChange = false;
			for (Symbol nT: nonTerminalSymbols) {
				for (Production P: nT.getProductionsTo()) {
					if (P.getDestinyString().contains("&")) {
						if (!Ne.contains(nT)) {
							Ne.add(nT);
							hasChange = true;
							break;
						}
					} else {
						Boolean toAdd = true;
						
						for (Symbol S: P.getDestiny()) {
							if (S.getType() == Symbol.Type.TERMINAL) {
								toAdd = false;
								break;
							} else {
								if (!Ne.contains(S)) {
									toAdd = false;
									break;
								}
							}
						}
						
						if (toAdd) {
							if (!Ne.contains(nT)) {
								Ne.add(nT);
								hasChange = true;
								break;
							}
						}
					}
				}
			}
		}
		
		return Ne;
	}
	
	/*
	 * Turns the grammar into epsilon-free
	 * */
	public void toEpsilonFree() {		
		Set<Symbol> Ne = buildNeSet();
			
		for (Symbol nT: nonTerminalSymbols) {
			Set<Production> prodsToRemove = new HashSet<Production>();	
			for (Production P: nT.getProductionsTo()) {
				if (P.getDestinyString().contains("&")) {
					prodsToRemove.add(P);
				}
			}
			nT.getProductionsTo().removeAll(prodsToRemove);
		}
		
		for (Symbol nT: nonTerminalSymbols) {
			for (Production P: nT.getProductionsTo()) {
				for (Symbol S: P.getDestiny()) {
					if (Ne.contains(S)) {
						LinkedList<Symbol> newDestiny = new LinkedList<Symbol>();
						for (Symbol S2: P.getDestiny()) {
							if (S != S2) {
								newDestiny.add(S2);
							}
						}
						Production P2 = new Production(nT, newDestiny.toArray(new Symbol[newDestiny.size()]));
						addProduction(P2);
					}
				}
			}
		}
		
		if (Ne.contains(initialSymbol)) {
			Symbol S = new Symbol(initialSymbol.toString() + "'", Symbol.Type.NON_TERMINAL);
			
			Symbol[] s0 = {initialSymbol};
			Symbol[] s1 = {new Symbol("&", Symbol.Type.TERMINAL)};
			
			addProduction(new Production(S, s0));
			addProduction(new Production(S, s1));
			
			initialSymbol = S;
		}
	}
	
	/*
	 * Remove simple productions
	 * */
	public void removeSimpleProductions() {
		
	}
	
	/*
	 * Turns into a proper grammar
	 * */
	public void toProper() {
		this.toEpsilonFree();
		this.removeSimpleProductions();
		this.removeUseless();
	}
	
	/*
	 * Returns the grammar in String format
	 * */
	public String toString() {
		String ln = System.getProperty("line.separator");
		String ret = "";
		
		String temp = initialSymbol.toString() + "->";
		Boolean grepFlag = false;
		
		for (Production P: initialSymbol.getProductionsTo()) {
			if (grepFlag) {
				temp += "|";
			}
			
			temp += P.getDestinyString();
			
			grepFlag = true;
		}
		
		ret += temp + ln;
		
		for (Symbol nT: nonTerminalSymbols) {
			if (!initialSymbol.equals(nT)) {
				temp = nT.toString() + "->";
				grepFlag = false;
				
				for (Production P: nT.getProductionsTo()) {
					if (grepFlag) {
						temp += "|";
					}
					
					temp += P.getDestinyString();
					
					grepFlag = true;
				}
				
				ret += temp + ln;
			}
		}
		
		return ret;
	}
	
}
