import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Main {
	private static void createFFFrame(JTextArea GLC) {
		Grammar G = Grammar.readGrammar(GLC.getText());
		
		JFrame frame = new JFrame();
		
		frame.setSize(600, 570);
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setTitle("FIRST & FOLLOW");
		
		JLabel firstLbl = new JLabel("FIRST");
		firstLbl.setBounds(20, 20, 200, 30);
		frame.add(firstLbl);
		
		JTextArea firstArea = new JTextArea(G.getFirstMap().toString());
		firstArea.setBounds(20, 50, 550, 220);
		firstArea.setEditable(false);
		frame.add(firstArea);
		
		JLabel followLbl = new JLabel("FOLLOW");
		followLbl.setBounds(20, 280, 200, 30);
		frame.add(followLbl);
		
		JTextArea followArea = new JTextArea(G.getFollowMap().toString());
		followArea.setBounds(20, 310, 550, 220);
		followArea.setEditable(false);
		frame.add(followArea);
	}
	
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
		
		JButton showMoreEpsilonFree = new JButton("Expandir");
		showMoreEpsilonFree.setBounds(20, 250, 200, 30);
		showMoreEpsilonFree.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame, epsilonfree_text.getText(), "&-LIVRE", JOptionPane.PLAIN_MESSAGE);
				
			}
		});
		frame.add(showMoreEpsilonFree);
		
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
		
		JButton showMoreSimple = new JButton("Expandir");
		showMoreSimple.setBounds(240, 250, 200, 30);
		showMoreSimple.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame, simple_text.getText(), "Sem produções simples", JOptionPane.PLAIN_MESSAGE);
				
			}
		});
		frame.add(showMoreSimple);
		
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
		
		JButton showMoreInfertile = new JButton("Expandir");
		showMoreInfertile.setBounds(460, 250, 200, 30);
		showMoreInfertile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame, infertile_text.getText(), "Sem símbolos inférteis", JOptionPane.PLAIN_MESSAGE);
				
			}
		});
		frame.add(showMoreInfertile);
		
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
		
		JButton showMoreUnreachable = new JButton("Expandir");
		showMoreUnreachable.setBounds(680, 250, 200, 30);
		showMoreUnreachable.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame, unreachable_text.getText(), "Sem símbolos inalcançáveis", JOptionPane.PLAIN_MESSAGE);
				
			}
		});
		frame.add(showMoreUnreachable);
	}
	
	private static void createComponents(JFrame frame) {
		JTextArea GLC_Text = new JTextArea("E->E + T|T\nT->T * F|F\nF->( E )|id");
		GLC_Text.setBounds(50, 20, 200, 250);
		frame.add(GLC_Text);
		
		JLabel empty = new JLabel("Vazia:");
		empty.setBounds(50, 350, 200, 30);
		frame.add(empty);
		
		JLabel finite = new JLabel("Finita:");
		finite.setBounds(50, 380, 200, 30);
		frame.add(finite);
		
		JLabel infinite = new JLabel("Infinita:");
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
		firstFollow.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				createFFFrame(GLC_Text);				
			}
		});
		frame.add(firstFollow);
		
		JLabel factored = new JLabel("Fatorada:");
		factored.setBounds(300, 210, 200, 30);
		frame.add(factored);
		
		JLabel factorable = new JLabel("Fatorável em 5 passos:");
		factorable.setBounds(300, 280, 200, 30);
		frame.add(factorable);
		
		SpinnerModel value = new SpinnerNumberModel(1, 1, 99, 1);
		JSpinner factorable_spinner = new JSpinner(value);
		factorable_spinner.setBounds(300, 240, 200, 30);
		factorable_spinner.setToolTipText("Digite a quantidade de passos");
		factorable_spinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				Grammar G = Grammar.readGrammar(GLC_Text.getText());
				
				int value = (int) factorable_spinner.getValue();
				
				if (value < 1) {
					value = 1;
				} else if (value > 99) {
					value = 99;
				}
				
				factorable.setText((G.isFactorable(value)) ? "Fatorável em " + value + " passos: SIM" : "Fatorável em " + value + " passos: NÃO");
			}
		});
		frame.add(factorable_spinner);
		
		JButton recNonTerminals = new JButton("Exibir NT Recursivos");
		recNonTerminals.setBounds(300, 350, 200, 30);
		recNonTerminals.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Grammar G = Grammar.readGrammar(GLC_Text.getText());
				
				G.toProper();
				
				G.removeLeftRecursions();
				
				JOptionPane.showMessageDialog(frame, G.getRecursiveNonTerminals(), "Não-Terminais Recursivos", JOptionPane.PLAIN_MESSAGE);
			}
		});
		frame.add(recNonTerminals);
		
		JLabel leftRecursion = new JLabel("Possui recursões à esquerda:");
		leftRecursion.setBounds(550, 80, 200, 30);
		frame.add(leftRecursion);
		
		JLabel leftRecursionRes = new JLabel("NÃO");
		leftRecursionRes.setBounds(550, 100, 200, 30);
		frame.add(leftRecursionRes);
		
		JButton leftRecursionVerification = new JButton("Verificar Recursões");
		leftRecursionVerification.setBounds(550, 20, 200, 30);
		leftRecursionVerification.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Grammar G = Grammar.readGrammar(GLC_Text.getText());
				
				G.toProper();
				
				leftRecursionRes.setText((G.hasLeftRecursion()) ? "SIM" : "NÃO");
			}
		});
		frame.add(leftRecursionVerification);
		
		JTextArea leftRecursionGLC = new JTextArea("");
		leftRecursionGLC.setBounds(550, 180, 200, 240);
		leftRecursionGLC.setEditable(false);
		frame.add(leftRecursionGLC);
		
		JButton leftRecursionElimination = new JButton("Eliminar Recursões");
		leftRecursionElimination.setBounds(550, 140, 200, 30);
		leftRecursionElimination.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Grammar G = Grammar.readGrammar(GLC_Text.getText());
				
				G.toProper();
				
				G.removeLeftRecursions();
				
				leftRecursionGLC.setText(G.toString());
			}
		});
		frame.add(leftRecursionElimination);
		
		JButton expandGLC = new JButton("Expandir");
		expandGLC.setBounds(550, 430, 200, 30);
		expandGLC.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(frame, leftRecursionGLC.getText(), "Sem recursões à esquerda", JOptionPane.PLAIN_MESSAGE);
			}
		});
		frame.add(expandGLC);
		
		JButton validate = new JButton("Verificar Propriedades");
		validate.setBounds(50, 280, 200, 30);
		validate.setToolTipText("L(G) vazia, finita ou infinita, fatorada e se é fatorável em n passos");
		validate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Grammar G = Grammar.readGrammar(GLC_Text.getText());
				
				factored.setText((G.isFactored()) ? "Fatorada: SIM" : "Fatorada: NÃO");
				
				int value = (int) factorable_spinner.getValue();
				
				if (value < 1) {
					value = 1;
				} else if (value > 99) {
					value = 99;
				}
				
				factorable.setText((G.isFactorable(value)) ? "Fatorável em " + value + " passos: SIM" : "Fatorável em " + value + " passos: NÃO");
				
				empty.setText((G.isEmpty()) ? "Vazia: SIM" : "Vazia: NÃO");
				
				if (G.isInfinite()) {
					finite.setText("Finita: NÃO");
					infinite.setText("Infinita: SIM");
				} else {
					finite.setText("Finita: SIM");
					infinite.setText("Infinita: NÃO");
				}
			}
		});
		frame.add(validate);
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
