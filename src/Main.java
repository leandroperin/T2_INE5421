import javax.swing.*;

public class Main {
	
	private static void createComponents(JFrame frame) {
		JTextArea GLC_Text = new JTextArea("S->A B\nA->a A|&\nB->b B|&");
		GLC_Text.setBounds(50, 20, 200, 250);
		frame.add(GLC_Text);
		
		JButton validate = new JButton("Validar Gramática");
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
		frame.add(properGLC);
		
		JButton firstFollow = new JButton("Obter FIRST & FOLLOW");
		firstFollow.setBounds(300, 80, 200, 30);
		frame.add(firstFollow);
		
		JLabel factored = new JLabel("Fatorada: SIM");
		factored.setBounds(300, 200, 200, 30);
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
