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
	 * Returns the fertile states
	 * */
	public Set<Symbol> getFertileStates() {
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
	 * Remove non-fertile symbols
	 * */
	public void removeNonFertile() {
		Set<Symbol> fertile = getFertileStates();
		
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
		return !getFertileStates().contains(initialSymbol);
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
