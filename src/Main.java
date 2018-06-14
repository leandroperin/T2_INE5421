
public class Main {

	public static void main(String[] args) throws Exception {
		Symbol S = new Symbol("S", Symbol.Type.NON_TERMINAL);
		Symbol B = new Symbol("B", Symbol.Type.NON_TERMINAL);
		Symbol A = new Symbol("A", Symbol.Type.NON_TERMINAL);
		
		Symbol a = new Symbol("a", Symbol.Type.TERMINAL);
		Symbol b = new Symbol("b", Symbol.Type.TERMINAL);
		Symbol c = new Symbol("c", Symbol.Type.TERMINAL);
		Symbol d = new Symbol("d", Symbol.Type.TERMINAL);
		Symbol epsilon = new Symbol("&", Symbol.Type.TERMINAL);
		
		Symbol[] s0 = {A, b};
		Symbol[] s1 = {A, B, c};
		Symbol[] b0 = {b, B};
		Symbol[] b1 = {A, d};
		Symbol[] b2 = {epsilon};
		Symbol[] a0 = {a, A};
		Symbol[] a1 = {epsilon};
		
		Production p0 = new Production(S, s0);
		Production p1 = new Production(S, s1);
		Production p2 = new Production(B, b0);
		Production p3 = new Production(B, b1);
		Production p4 = new Production(B, b2);
		Production p5 = new Production(A, a0);
		Production p6 = new Production(A, a1);

		Grammar G = new Grammar();
		G.addProduction(p0);
		G.addProduction(p1);
		G.addProduction(p2);
		G.addProduction(p3);
		G.addProduction(p4);
		G.addProduction(p5);
		G.addProduction(p6);
		
//		for (Symbol s: S.getFirst()) {
//			System.out.print(s.toString());
//		}
		
		System.out.println(G.toString());
	}

}
