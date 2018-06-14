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
