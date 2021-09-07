//Author: George Karachepone, Devin Gao
package experiment;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
public class ControlGUI extends JPanel {
	private JTextField die;
	private JTextField turn;
	private JTextField guess;
	private JTextField disprove;

	public ControlGUI()
	{
		// Create two panels, one for buttons and one for player info
		setLayout(new GridLayout(2,0));
		JPanel pane1 = createButtonPanel();
		add(pane1);
		JPanel pane2 = createInfoPanel();
		add(pane2);

	}

	private JPanel createInfoPanel() {
		JPanel panel = new JPanel();
		// Use a grid layout, 1 row, 2 elements (label, text)
		panel.setLayout(new GridLayout(1,3));
		
		
		// Used JPanels to store the Layout and text field
		JPanel diePanel = new JPanel();
		JPanel guessPanel = new JPanel();
		JPanel disprovePanel = new JPanel();
		
		// Layout objects for the bottom row of GUI
		JLabel dieLayout = new JLabel("Roll");
		JLabel guessLayout = new JLabel("Guess");
		JLabel disproveLayout = new JLabel("Response");
		
		//Instance variables for textfield boxes
		die = new JTextField(20);
		//set textfield to not be editable
		die.setEditable(false);
		guess = new JTextField(20);
		//set textfield to not be editable
		guess.setEditable(false);
		disprove = new JTextField(20);
		//set textfield to not be editable
		disprove.setEditable(false);

		//Add to panels the layouts
		panel.add(dieLayout);
		panel.add(guessLayout);
		panel.add(disproveLayout);
		
		
		//add label and textfield to the diePanel
		diePanel.add(dieLayout, BorderLayout.NORTH);
		diePanel.add(die, BorderLayout.SOUTH);
		panel.add(diePanel, BorderLayout.WEST);
		
		//add label and textfield to the guessPanel
		guessPanel.add(guessLayout, BorderLayout.NORTH);
		guessPanel.add(guess, BorderLayout.SOUTH);
		panel.add(guessPanel, BorderLayout.CENTER);

		//add label and textfield to the disprovePanel
		disprovePanel.add(disproveLayout, BorderLayout.NORTH);
		disprovePanel.add(disprove, BorderLayout.SOUTH);
		panel.add(disprovePanel, BorderLayout.EAST);

		
		//Sets their titleBorder as their respective titles
		diePanel.setBorder(new TitledBorder (new EtchedBorder(), "Die"));
		guessPanel.setBorder(new TitledBorder (new EtchedBorder(), "Guess"));
		disprovePanel.setBorder(new TitledBorder (new EtchedBorder(), "Guess Result"));

		return panel;
	}

	
	private JPanel createButtonPanel() {
		//Make two buttons, one for next player and one for accusation
		JButton nextPlayer = new JButton("Next Player");
		JButton makeAccusation = new JButton("Make an accusation");
		
		JLabel playerTurnLayout = new JLabel();
		
		JPanel turnPanel = new JPanel();
		
		
		JPanel panel = new JPanel();
		//Set the layout for 1 by 3
		panel.setLayout(new GridLayout(1,3));
	
		turn = new JTextField(20);
		//set textfield to not be editable
		turn.setEditable(false);
		
		//add playerTurnLayout to the turnPanel
		turnPanel.add(playerTurnLayout, BorderLayout.NORTH);
		//also add text field to turnPanel
		turnPanel.add(turn, BorderLayout.NORTH);

		//add the turnPanel as well as the two buttons to the GUI
		panel.add(turnPanel, BorderLayout.WEST);
		panel.add(nextPlayer, BorderLayout.CENTER);
		panel.add(makeAccusation, BorderLayout.EAST);
		
		//set the title of turnPanel to Whose Turn?
		turnPanel.setBorder(new TitledBorder (new EtchedBorder(), "Whose turn?"));
		
		return panel;
	}

	public static void main(String[] args) {
		// Create a JFrame with all the normal functionality
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Clue Game Panel");
		frame.setSize(900, 300);	
		// Create the JPanel and add it to the JFrame
		ControlGUI gui = new ControlGUI();
		frame.add(gui, BorderLayout.CENTER);
		// Now let's view it
		frame.setVisible(true);
	}


}

