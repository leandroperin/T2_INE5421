
public class Main {

	public static void main(String[] args) throws Exception {
		Symbol S = new Symbol("S", Symbol.Type.NON_TERMINAL);
		Symbol A = new Symbol("A", Symbol.Type.NON_TERMINAL);
		Symbol B = new Symbol("B", Symbol.Type.NON_TERMINAL);
		
		Symbol a = new Symbol("a", Symbol.Type.TERMINAL);
		Symbol b = new Symbol("b", Symbol.Type.TERMINAL);
		Symbol epsilon = new Symbol("&", Symbol.Type.TERMINAL);
		
		Symbol[] s0 = {A, B};
		Symbol[] a0 = {a, A};
		Symbol[] a1 = {epsilon};
		Symbol[] b0 = {b, B};
		Symbol[] b1 = {epsilon};
		
		Production p0 = new Production(S, s0);
		Production p1 = new Production(A, a0);
		Production p2 = new Production(A, a1);
		Production p3 = new Production(B, b0);
		Production p4 = new Production(B, b1);

		Grammar G = new Grammar();
		G.addProduction(p0);
		G.addProduction(p1);
		G.addProduction(p2);
		G.addProduction(p3);
		G.addProduction(p4);
		
		System.out.println("Ne = " + G.toEpsilonFree());
		System.out.println(G.toString());
		
		System.out.println("Na = " + G.removeSimpleProductions());
		System.out.println(G.toString());
		
		System.out.println("Nf = " + G.removeNonFertile());
		System.out.println(G.toString());
		
		System.out.println("Vi = " + G.removeUnreachable());
		System.out.println(G.toString());
	}

}
