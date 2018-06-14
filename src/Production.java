
public class Production {

	private Symbol from;
	private Symbol[] destiny;
	
	/*
	 * Creates a new production
	 * */
	public Production(Symbol _from, Symbol[] _destiny) {
		from = _from;
		destiny = _destiny.clone();
	}
	
	/*
	 * Returns the first symbol of the production
	 * */
	public Symbol getFrom() {
		return from;
	}
	
	/*
	 * Returns the first symbol in String format
	 * */
	public String getFromString() {
		return from.toString();
	}
	
	/*
	 * Returns the destiny symbols of the production
	 * */
	public Symbol[] getDestiny() {
		return destiny;
	}
	
	/*
	 * Returns the destiny in String format
	 * */
	public String getDestinyString() {
		String ret = "";
		for (Symbol s: destiny) {
			ret += s.toString() + " ";
		}
		return ret.substring(0, ret.length() - 1);
	}
	
}
