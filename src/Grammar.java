import java.util.*;

public class Grammar {

	private Set<Symbol> nonTerminalSymbols = new HashSet<Symbol>();
	private Symbol initialSymbol = null;

	private String recursiveNonTerminals = "";

	static Set<Symbol> NaCalculated = new HashSet<Symbol>();

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

		initialSymbol.setInitial(true);

		for (Symbol S: P.getDestiny()) {
			if (S.getType() == Symbol.Type.NON_TERMINAL) {
				nonTerminalSymbols.add(S);
			}
		}

		nonTerminalSymbols.add(P.getFrom());
		P.getFrom().addProductionTo(P);
	}

	/*
	 * Returns the FOLLOW Map
	 * */
	public Map<Symbol, Set<Symbol>> getFollowMap() {
		Map<Symbol, Set<Symbol>> ret = new HashMap<Symbol, Set<Symbol>>();

		for (Symbol nT: nonTerminalSymbols) {
			ret.put(nT, new HashSet<Symbol>());
		}

		Boolean hasChange = true;
		while (hasChange) {
			hasChange = false;
			for (Symbol nT: nonTerminalSymbols) {
				Set<Symbol> f = nT.getFollow();
				if (ret.get(nT).size() != f.size()) {
					hasChange = true;
					ret.get(nT).addAll(f);
				}
			}
		}

		return ret;
	}

	/*
	 * Returns the FIRST Map
	 * */
	public Map<Symbol, Set<Symbol>> getFirstMap() {
		Map<Symbol, Set<Symbol>> ret = new HashMap<Symbol, Set<Symbol>>();

		for (Symbol nT: nonTerminalSymbols) {
			ret.put(nT, nT.getFirst());
		}

		return ret;
	}

	/*
	 * Verifies if a symbol of the grammar contains left recursion
	 * */
	private Boolean hasDirectLeftRecursion(Symbol nT) {
		for (Production P: nT.getProductionsTo()) {
			if (P.getDestiny()[0] == nT) {
				recursiveNonTerminals += nT.toString() + ": Recursão Direta\n";
				return true;
			}
		}
		return false;
	}

	/*
	 * Remove direct left recursions
	 * */
	private void removeDirectLeftRecursion(Symbol nT) {
		if (hasDirectLeftRecursion(nT)) {
			Symbol nS = new Symbol(nT.toString() + "'", Symbol.Type.NON_TERMINAL);
			Set<Production> prodsToAdd = new HashSet<Production>();
			Set<Production> prodsToRemove = new HashSet<Production>();
			for (Production P: nT.getProductionsTo()) {
				if (P.getDestiny()[0] == nT) {
					LinkedList<Symbol> temp = new LinkedList<Symbol>();
					for (Symbol S: P.getDestiny()) {
						temp.add(S);
					}
					temp.removeFirstOccurrence(nT);
					temp.add(nS);
					prodsToAdd.add(new Production(nS, temp.toArray(new Symbol[temp.size()])));
					prodsToRemove.add(P);
				} else {
					LinkedList<Symbol> temp = new LinkedList<Symbol>();
					for (Symbol S: P.getDestiny()) {
						temp.add(S);
					}
					temp.add(nS);
					prodsToAdd.add(new Production(nT, temp.toArray(new Symbol[temp.size()])));
					prodsToRemove.add(P);
				}
			}
			Symbol[] epsilon = {new Symbol("&", Symbol.Type.TERMINAL)};
			prodsToAdd.add(new Production(nS, epsilon));

			nT.getProductionsTo().removeAll(prodsToRemove);

			for (Production P: prodsToAdd) {
				addProduction(P);
			}
		}
	}

	/*
	 * Remove left recursions
	 * */
	public void removeLeftRecursions() {
		ArrayList<Symbol> nTList = new ArrayList<Symbol>();
		nTList.addAll(nonTerminalSymbols);

		for (int i = 0; i < nTList.size(); i++) {
			for (int j = 0; j < i; j++) {
				Set<Production> prodsToAdd = new HashSet<Production>();
				Set<Production> prodsToRemove = new HashSet<Production>();

				for (Production P: nTList.get(i).getProductionsTo()) {
					if (P.getDestiny()[0] == nTList.get(j)) {
						recursiveNonTerminals += nTList.get(j).toString() + ": Recursão Indireta\n";
						prodsToRemove.add(P);

						for (Production P2: nTList.get(j).getProductionsTo()) {
							LinkedList<Symbol> temp = new LinkedList<Symbol>();

							for (Symbol S: P2.getDestiny()) {
								temp.add(S);
							}

							for (Symbol S: P.getDestiny()) {
								temp.add(S);
							}

							temp.remove(nTList.get(j));

							prodsToAdd.add(new Production(nTList.get(i), temp.toArray(new Symbol[temp.size()])));
						}
					}
				}

				for (Production P: prodsToAdd) {
					addProduction(P);
				}

				for (Production P: prodsToRemove) {
					Symbol FROM = P.getFrom();

					FROM.getProductionsTo().remove(P);
				}
			}

			removeDirectLeftRecursion(nTList.get(i));
		}
	}

	/*
	 * Verifies if the grammar has a left recursion
	 * */
	public Boolean hasLeftRecursion() {
		int nonTerminalAmount = nonTerminalSymbols.size();

		removeLeftRecursions();

		return (nonTerminalAmount == nonTerminalSymbols.size()) ? false : true;
	}

	/*
	 * Returns the recursive non-terminals
	 * */
	public String getRecursiveNonTerminals() {
		return recursiveNonTerminals;
	}

	/*
	 * Verifies if the grammar is factored
	 * */
	public Boolean isFactored() {
		Map<Symbol, LinkedList<Symbol>> symbolSet = new HashMap<Symbol, LinkedList<Symbol>>();

		for (Symbol nT: nonTerminalSymbols) {
			LinkedList<Symbol> temp = new LinkedList<Symbol>();
			for (Production P: nT.getProductionsTo()) {
				temp.add(P.getDestiny()[0]);
			}
			symbolSet.put(nT, temp);
		}

		Boolean hasChange = true;
		while (hasChange) {
			hasChange = false;
			for (Symbol nT: nonTerminalSymbols) {
				LinkedList<Symbol> toAdd = new LinkedList<Symbol>();
				LinkedList<Symbol> toRemove = new LinkedList<Symbol>();
				for (Symbol S: symbolSet.get(nT)) {
					if (S.getType() == Symbol.Type.NON_TERMINAL) {
						for (Symbol S2: symbolSet.get(S)) {
							toAdd.add(S2);
						}
						toRemove.add(S);
						hasChange = true;
					}
				}
				symbolSet.get(nT).addAll(toAdd);
				symbolSet.get(nT).removeAll(toRemove);
			}
		}

		for (Symbol nT: nonTerminalSymbols) {
			Set<Symbol> temp = new HashSet<Symbol>();
			temp.addAll(symbolSet.get(nT));
			if (symbolSet.get(nT).size() != temp.size()) {
				return false;
			}
		}

		return true;
	}

	/*
	 * Verifies if the grammar is factorable in n steps
	 * */
	public Boolean isFactorable(int steps) {
		if (isFactored()) {
			return true;
		}

		Map<Symbol, Set<Symbol>> symbolSet = new HashMap<Symbol, Set<Symbol>>();
		for (int i = 0; i < steps; i++) {
			//Direct
			Set<Production> prodsToAdd = new HashSet<Production>();
			for (Symbol nT: nonTerminalSymbols) {
				Set<Symbol> temp = new HashSet<Symbol>();
				Set<Symbol> toAdd = new HashSet<Symbol>();
				for (Production P: nT.getProductionsTo()) {
					if (temp.contains(P.getDestiny()[0])) {
						toAdd.add(P.getDestiny()[0]);
					} else {
						temp.add(P.getDestiny()[0]);
					}
				}
				symbolSet.put(nT, toAdd);

				Set<Production> toRemove = new HashSet<Production>();

				for (Symbol S: symbolSet.get(nT)) {
					Symbol nS = new Symbol(nT.toString() + S.toString(), Symbol.Type.NON_TERMINAL);
					for (Production P: nT.getProductionsTo()) {
						if (P.getDestiny()[0] == S) {
							LinkedList<Symbol> temp2 = new LinkedList<Symbol>();
							for (int j = 1; j < P.getDestiny().length; j++) {
								temp2.add(P.getDestiny()[j]);
							}
							if (temp2.size() == 0) {
								temp2.add(new Symbol("&", Symbol.Type.TERMINAL));
							}
							Production P2 = new Production(nS, temp2.toArray(new Symbol[temp2.size()]));
							prodsToAdd.add(P2);
							toRemove.add(P);
						}
					}
					Symbol[] newProd = {S, nS};
					prodsToAdd.add(new Production(nT, newProd));
				}

				nT.getProductionsTo().removeAll(toRemove);
			}
			for (Production P: prodsToAdd) {
				addProduction(P);
			}

			// Indirect
			Set<Production> prodsToAdd2 = new HashSet<Production>();
			Set<Production> prodsToRemove = new HashSet<Production>();
			for (Symbol nT: nonTerminalSymbols) {
				for (Production P: nT.getProductionsTo()) {
					if (P.getDestiny()[0].getType() == Symbol.Type.NON_TERMINAL) {
						for (Production P2: P.getDestiny()[0].getProductionsTo()) {
							for (Production P3: nT.getProductionsTo()) {
								if (P3.getDestiny()[0] == P.getDestiny()[0]) {
									LinkedList<Symbol> destiny = new LinkedList<Symbol>();
									for (Symbol S: P2.getDestiny()) {
										destiny.add(S);
									}
									for (int j = 1; j < P3.getDestiny().length; j++) {
										destiny.add(P3.getDestiny()[j]);
									}
									prodsToAdd2.add(new Production(nT, destiny.toArray(new Symbol[destiny.size()])));
								}
							}
						}
						prodsToRemove.add(P);
					}
				}
			}
			for (Symbol nT: nonTerminalSymbols) {
				nT.getProductionsTo().removeAll(prodsToRemove);
			}
			for (Production P: prodsToAdd2) {
				addProduction(P);
			}

			removeNonFertile();
			removeUnreachable();

			if (this.isFactored()) {
				return true;
			}

			symbolSet.clear();
		}

		return false;
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
	 * Verifies if the grammar is infinite
	 * */
	public Boolean isInfinite() {
		this.removeNonFertile();
		this.removeUnreachable();

		if (nonTerminalSymbols.size() == 0) {
			return false;
		}

		Map<Symbol, Set<Symbol>> symbolSet = new HashMap<Symbol, Set<Symbol>>();

		for (Symbol nT: nonTerminalSymbols) {
			Set<Symbol> temp = new HashSet<Symbol>();
			for (Production P: nT.getProductionsTo()) {
				for (Symbol S: P.getDestiny()) {
					if (S.getType() == Symbol.Type.NON_TERMINAL) {
						temp.add(S);
					}
				}
			}
			symbolSet.put(nT, temp);
		}

		Boolean hasChange = true;
		while (hasChange) {
			hasChange = false;
			for (Symbol nT: nonTerminalSymbols) {
				Set<Symbol> toAdd = new HashSet<Symbol>();
				for (Symbol S: symbolSet.get(nT)) {
					for (Symbol S2: symbolSet.get(S)) {
						if (!symbolSet.get(nT).contains(S2)) {
							toAdd.add(S2);
							hasChange = true;
						}
					}
				}
				symbolSet.get(nT).addAll(toAdd);
			}
		}

		for (Symbol nT: nonTerminalSymbols) {
			if (symbolSet.get(nT).contains(nT)) {
				return true;
			}
		}

		return false;
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

		int count = 0;
		for (Symbol nT: nonTerminalSymbols) {
			for (Production P: nT.getProductionsTo()) {
				int temp = 0;
				for (Symbol S: P.getDestiny()) {
					if (Ne.contains(S)) {
						temp++;
					}
				}
				count = (temp > count) ? temp : count;
			}
		}

		for (int i = 0; i < count; i++) {
			for (Symbol nT: nonTerminalSymbols) {
				Set<Production> toAdd = new HashSet<Production>();
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
							toAdd.add(P2);
						}
					}
				}
				for (Production P: toAdd) {
					addProduction(P);
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
		Grammar.NaCalculated.add(nT);
		Set<Symbol> Na = new HashSet<Symbol>();
		Na.add(nT);

		for (Production P: nT.getProductionsTo()) {
			if (P.getDestiny().length == 1 && P.getDestiny()[0].getType() == Symbol.Type.NON_TERMINAL) {
				Na.add(P.getDestiny()[0]);
				if (!Grammar.NaCalculated.contains(P.getDestiny()[0])) {
					Na.addAll(buildNaSet(P.getDestiny()[0]));
				}
			}
		}

		return Na;
	}

	/*
	 * Remove simple productions returning the NA sets
	 * */
	public Map<Symbol, Set<Symbol>> removeSimpleProductions() {
		Map<Symbol, Set<Symbol>> Na = new HashMap<Symbol, Set<Symbol>>();
		Grammar.NaCalculated.clear();

		for (Symbol nT: nonTerminalSymbols) {
			Na.put(nT, buildNaSet(nT));
		}

		Boolean hasChange = true;
		while (hasChange) {
			hasChange = false;
			for (Symbol nT: nonTerminalSymbols) {
				Set<Symbol> toAdd = new HashSet<Symbol>();
				for (Symbol S: Na.get(nT)) {
					for (Symbol S2: Na.get(S)) {
						if (!Na.get(nT).contains(S2)) {
							hasChange = true;
							toAdd.add(S2);
						}
					}
				}
				Na.get(nT).addAll(toAdd);
			}
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

	/*
	 * Parse a grammar
	 * */
	public static Grammar readGrammar(String text) {
		Grammar G = new Grammar();

		String[] lines = text.split(System.getProperty("line.separator"));
		Map<String, Symbol> symbols = new HashMap<String, Symbol>();

		for (String l: lines) {
			String FROM = l.split("(\\s+)-")[0];

			Symbol S;
			if (!symbols.containsKey(FROM)) {
				S = new Symbol(FROM, Symbol.Type.NON_TERMINAL);
				symbols.put(FROM, S);
			} else {
				S = symbols.get(FROM);
			}

			String sub = l.split(">(\\s+)")[1];
			String prods[] = sub.split("(\\s+)\\|(\\s+)");

			for (String j: prods) {
				LinkedList<Symbol> toAdd = new LinkedList<Symbol>();
				for (String k: j.split("\\s+")) {
					Symbol.Type type = null;

					if (k.matches("[A-Z]+.*")) {
						type = Symbol.Type.NON_TERMINAL;
					} else if (k.matches("[^A-Z]*")) {
						type = Symbol.Type.TERMINAL;
					}

					if (type != null) {
						if (!symbols.containsKey(k)) {
							symbols.put(k, new Symbol(k, type));
						}
						toAdd.add(symbols.get(k));
					}
				}
				Production P = new Production(S, toAdd.toArray(new Symbol[toAdd.size()]));
				G.addProduction(P);
			}
		}
		
		return G;
	}

}
