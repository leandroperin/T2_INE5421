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
	 * Remove unreachable symbols returning VI set
	 * */
	public Set<Symbol> removeUnreachable() {
		Set<Symbol> reachable = getReachableSymbols();
		
		Set<Symbol> symbolsToRemove = new HashSet<Symbol>();
		
		for (Symbol nT: nonTerminalSymbols) {
			if (!reachable.contains(nT)) {
				symbolsToRemove.add(nT);
			}
		}

		nonTerminalSymbols.removeAll(symbolsToRemove);
		
		return reachable;
	}
	
	/*
	 * Remove non-fertile symbols returning NF set
	 * */
	public Set<Symbol> removeNonFertile() {
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
		
		return fertile;
	}
	
	/*
	 * Verifies if the grammar is empty
	 * */
	public Boolean isEmpty() {
		return !getFertileSymbols().contains(initialSymbol);
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
	 * Turns the grammar into epsilon-free returning the NE set
	 * */
	public Set<Symbol> toEpsilonFree() {		
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
		
		return Ne;
	}
	
	/*
	 * Builds the auxiliary set to remove simple productions
	 * */
	private Set<Symbol> buildNaSet(Symbol nT) {
		Set<Symbol> Na = new HashSet<Symbol>();
		Na.add(nT);
		
		for (Production P: nT.getProductionsTo()) {
			if (P.getDestiny().length == 1 && P.getDestiny()[0].getType() == Symbol.Type.NON_TERMINAL) {
				Na.add(P.getDestiny()[0]);
				Na.addAll(buildNaSet(P.getDestiny()[0]));
			}
		}
		
		return Na;
	}
	
	/*
	 * Remove simple productions returning the NA sets
	 * */
	public Map<Symbol, Set<Symbol>> removeSimpleProductions() {
		Map<Symbol, Set<Symbol>> Na = new HashMap<Symbol, Set<Symbol>>();
		
		for (Symbol nT: nonTerminalSymbols) {
			Na.put(nT, buildNaSet(nT));
		}
		
		for (Symbol nT: nonTerminalSymbols) {
			Set<Production> toRemove = new HashSet<Production>();
			for (Production P: nT.getProductionsTo()) {
				if (P.getDestiny().length == 1 && P.getDestiny()[0].getType() == Symbol.Type.NON_TERMINAL) {
					toRemove.add(P);
				}
			}
			nT.getProductionsTo().removeAll(toRemove);
		}
		
		for (Symbol nT: nonTerminalSymbols) {
			Set<Production> toAdd = new HashSet<Production>();
			for (Symbol S: Na.get(nT)) {
				for (Production P: S.getProductionsTo()) {
					toAdd.add(new Production(nT, P.getDestiny()));
				}
			}
			for (Production P: toAdd) {
				addProduction(P);
			}
		}
		
		return Na;
	}
	
	/*
	 * Turns into a proper grammar
	 * */
	public void toProper() {
		this.toEpsilonFree();
		this.removeSimpleProductions();
		this.removeNonFertile();
		this.removeUnreachable();
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
