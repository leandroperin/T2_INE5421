import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Main {
	
	private static void createProperGLCFrame(JTextArea GLC) {
		Grammar G = Grammar.readGrammar(GLC.getText());
		
		JFrame frame = new JFrame();
		
		frame.setSize(900, 570);
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setTitle("Gramática Própria");
		
		JLabel ne_lbl = new JLabel("Ne");
		ne_lbl.setBounds(20, 280, 200, 30);
		frame.add(ne_lbl);
		
		JTextArea ne_text = new JTextArea(G.toEpsilonFree().toString());
		ne_text.setBounds(20, 310, 200, 200);
		ne_text.setEditable(false);
		frame.add(ne_text);
		
		JLabel epsilonfree_lbl = new JLabel("&-LIVRE");
		epsilonfree_lbl.setBounds(20, 20, 200, 30);
		frame.add(epsilonfree_lbl);
		
		JTextArea epsilonfree_text = new JTextArea(G.toString());
		epsilonfree_text.setBounds(20, 50, 200, 200);
		epsilonfree_text.setEditable(false);
		frame.add(epsilonfree_text);
		
		JLabel na_lbl = new JLabel("Na");
		na_lbl.setBounds(240, 280, 200, 30);
		frame.add(na_lbl);
		
		
		JTextArea na_text = new JTextArea("");
		na_text.setBounds(240, 310, 200, 200);
		na_text.setEditable(false);
		frame.add(na_text);
		
		String na_str = G.removeSimpleProductions().toString();
		for (String s: na_str.split("],")) {
			na_text.append(s + "],\n");
		}
		
		JLabel simple_lbl = new JLabel("Sem produções simples");
		simple_lbl.setBounds(240, 20, 200, 30);
		frame.add(simple_lbl);
		
		JTextArea simple_text = new JTextArea(G.toString());
		simple_text.setBounds(240, 50, 200, 200);
		simple_text.setEditable(false);
		frame.add(simple_text);
		
		JLabel nf_lbl = new JLabel("NF");
		nf_lbl.setBounds(460, 280, 200, 30);
		frame.add(nf_lbl);
		
		JTextArea nf_text = new JTextArea(G.removeNonFertile().toString());
		nf_text.setBounds(460, 310, 200, 200);
		nf_text.setEditable(false);
		frame.add(nf_text);
		
		JLabel infertile_lbl = new JLabel("Sem símbolos inférteis");
		infertile_lbl.setBounds(460, 20, 200, 30);
		frame.add(infertile_lbl);
		
		JTextArea infertile_text = new JTextArea(G.toString());
		infertile_text.setBounds(460, 50, 200, 200);
		infertile_text.setEditable(false);
		frame.add(infertile_text);
		
		JLabel vi_lbl = new JLabel("Vi");
		vi_lbl.setBounds(680, 280, 200, 30);
		frame.add(vi_lbl);
		
		JTextArea vi_text = new JTextArea(G.removeUnreachable().toString());
		vi_text.setBounds(680, 310, 200, 200);
		vi_text.setEditable(false);
		frame.add(vi_text);
		
		JLabel unreachable_lbl = new JLabel("Sem símbolos inalcançáveis");
		unreachable_lbl.setBounds(680, 20, 200, 30);
		frame.add(unreachable_lbl);
		
		JTextArea unreachable_text = new JTextArea(G.toString());
		unreachable_text.setBounds(680, 50, 200, 200);
		unreachable_text.setEditable(false);
		frame.add(unreachable_text);
	}
	
	private static void createComponents(JFrame frame) {
		JTextArea GLC_Text = new JTextArea("S->A B\nA->a A|&\nB->b B|&");
		GLC_Text.setBounds(50, 20, 200, 250);
		frame.add(GLC_Text);
		
		JButton validate = new JButton("Verificar Propriedades");
		validate.setBounds(50, 280, 200, 30);
		frame.add(validate);
		
		JLabel empty = new JLabel("Vazia: NÃO");
		empty.setBounds(50, 350, 200, 30);
		frame.add(empty);
		
		JLabel finite = new JLabel("Finita: NÃO");
		finite.setBounds(50, 380, 200, 30);
		frame.add(finite);
		
		JLabel infinite = new JLabel("Infinita: SIM");
		infinite.setBounds(50, 410, 200, 30);
		frame.add(infinite);
		
		JButton properGLC = new JButton("Obter GLC Própria");
		properGLC.setBounds(300, 20, 200, 30);
		properGLC.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				createProperGLCFrame(GLC_Text);				
			}
		});
		frame.add(properGLC);
		
		JButton firstFollow = new JButton("Obter FIRST & FOLLOW");
		firstFollow.setBounds(300, 80, 200, 30);
		frame.add(firstFollow);
		
		JLabel factored = new JLabel("Fatorada: SIM");
		factored.setBounds(300, 210, 200, 30);
		frame.add(factored);
		
		JLabel factorable = new JLabel("Fatorável em n passos: SIM");
		factorable.setBounds(300, 240, 200, 30);
		frame.add(factorable);
		
		JButton leftRecursionVerification = new JButton("Verificar Recursões");
		leftRecursionVerification.setBounds(550, 20, 200, 30);
		frame.add(leftRecursionVerification);
		
		JLabel leftRecursion = new JLabel("Possui recursões à esquerda:");
		leftRecursion.setBounds(550, 80, 200, 30);
		frame.add(leftRecursion);
		
		JLabel leftRecursionRes = new JLabel("NÃO");
		leftRecursionRes.setBounds(550, 100, 200, 30);
		frame.add(leftRecursionRes);
		
		JButton leftRecursionElimination = new JButton("Eliminar Recursões");
		leftRecursionElimination.setBounds(550, 140, 200, 30);
		frame.add(leftRecursionElimination);
		
		JTextArea leftRecursionGLC = new JTextArea("");
		leftRecursionGLC.setBounds(550, 200, 200, 250);
		leftRecursionGLC.setEditable(false);
		frame.add(leftRecursionGLC);
	}
	
	private static void setUpInterface(JFrame frame) {
		createComponents(frame);
		
		frame.setSize(800, 500);
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("INE5421 - Linguagens Formais e Compiladores");
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		} catch (ClassNotFoundException e) {
		    e.printStackTrace();
		} catch (InstantiationException e) {
		    e.printStackTrace();
		} catch (IllegalAccessException e) {
		    e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
		    e.printStackTrace();
		}
		
		JFrame home = new JFrame();
		
		setUpInterface(home);
	}

}
