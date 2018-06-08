import java.util.*;

public class Grammar {

	private Set<Symbol> nonTerminalSymbols = new HashSet<Symbol>();
	
	/*
	 * Creates a new context-free grammar
	 * */
	public Grammar() {
		
	}
	
	/*
	 * Add a production to the grammar
	 * */
	public void addProduction(Production P) {
		nonTerminalSymbols.add(P.getFrom());
		P.getFrom().addProductionTo(P);
	}
	
	/*
	 * Returns the grammar in String format
	 * */
	public String toString() {
		String ln = System.getProperty("line.separator");
		String ret = "";
		
		for (Symbol nT: nonTerminalSymbols) {
			String temp = nT.toString() + "->";
			Boolean grepFlag = false;
			
			for (Production P: nT.getProductionsTo()) {
				if (grepFlag) {
					temp += "|";
				}
				
				temp += P.getDestinyString();
				
				grepFlag = true;
			}
			
			ret += temp + ln;
		}
		
		return ret;
	}
	
}
