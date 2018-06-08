
public class Main {

	public static void main(String[] args) throws Exception {
		// S->aSb|ab
		
		Symbol S = new Symbol("S", Symbol.Type.NON_TERMINAL);
		Symbol a = new Symbol("a", Symbol.Type.TERMINAL);
		Symbol b = new Symbol("b", Symbol.Type.TERMINAL);
		
		Symbol[] s1 = {a, S, b};
		Symbol[] s2 = {a, b};
		
		Production p1 = new Production(S, s1);
		Production p2 = new Production(S, s2);

		Grammar G = new Grammar();
		G.addProduction(p1);
		G.addProduction(p2);
		
		System.out.println(G.toString());
	}

}
