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
			for (Symbol S: ret) {
				for (Production P: S.getProductionsTo()) {
					for (Symbol nT: P.getDestiny()) {
						if (nT.getType() == Symbol.Type.NON_TERMINAL) {
							if (!ret.contains(nT)) {
								ret.add(nT);
								hasChange = true;
							}
						}
					}
				}
			}
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
		
		for (Symbol S: symbolsToRemove) {
			nonTerminalSymbols.remove(S);
		}
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
					
					for (Production P: prodsToRemove) {
						S.getProductionsTo().remove(P);
					}
				}
			}
		}
		
		for (Symbol S: symbolsToRemove) {
			nonTerminalSymbols.remove(S);
		}
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
			for (Production P: prodsToRemove) {
				nT.getProductionsTo().remove(P);
			}
		}
		
		
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
