//Author: George Karachepone, Devin Gao
package clueGame;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import clueGame.SuggestionPrompt;

public class PlayerActionsPanel extends JPanel{
	private JTextField die;
	private JTextField turn;
	protected static JTextField guess;
	protected JTextField disprove;
	private JButton nextPlayer = new JButton("Next Player");
	private JButton makeAccusation = new JButton("Make an accusation");
	Board board = Board.getInstance();
	BoardPanel boardPanel = BoardPanel.getInstance();

	// variable used for singleton pattern
	private static PlayerActionsPanel theInstance = new PlayerActionsPanel();
	// this method returns the only Board
	public static PlayerActionsPanel getInstance() {
		if (theInstance == null) {
			theInstance = new PlayerActionsPanel();
		}
		return theInstance;
	}



	public JButton getNextPlayer() {
		return nextPlayer;
	}

	public void setNextPlayer(JButton nextPlayer) {
		this.nextPlayer = nextPlayer;
	}

	public JButton getMakeAccusation() {
		return makeAccusation;
	}

	public void setMakeAccusation(JButton makeAccusation) {
		this.makeAccusation = makeAccusation;
	}



	private PlayerActionsPanel()
	{


		// Create two panels, one for buttons and one for player info
		setLayout(new GridLayout(2,0));
		JPanel pane1 = createButtonPanel();
		add(pane1);
		JPanel pane2 = createInfoPanel();
		add(pane2);

		// Add Action Listeners to the buttons
		NextPlayerListener nextListener = new NextPlayerListener();
		nextPlayer.addActionListener(nextListener);
		AccusationListener accusationListener = new AccusationListener();
		makeAccusation.addActionListener(accusationListener);

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
		// roll the die, but not on the first turn!
		if (board.currentPlayerTurn != -1)
			rollThatDie();
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

	private void rollThatDie() {
		board.rollDie();
		die.setText("" + board.getDieRoll());
	}


	private JPanel createButtonPanel() {

		JLabel playerTurnLayout = new JLabel();

		JPanel turnPanel = new JPanel();


		JPanel panel = new JPanel();
		//Set the layout for 1 by 3
		panel.setLayout(new GridLayout(1,3));

		turn = new JTextField(20);
		//set textfield to not be editable
		turn.setEditable(false);
		if (board.getCurrentPlayerTurn() != -1) {
			turn.setText(board.getPlayers().get(board.currentPlayerTurn).getPlayerName());
		}


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

	//	public static void main(String[] args) {
	//		// Create a JFrame with all the normal functionality
	//		JFrame frame = new JFrame();
	//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//		frame.setTitle("Clue Game Actions");
	//		frame.setSize(900, 300);	
	//		// Create the JPanel and add it to the JFrame
	//		PlayerActionsPanel gui = new PlayerActionsPanel();
	//		frame.add(gui, BorderLayout.CENTER);
	//		// Now let's view it
	//		frame.setVisible(true);
	//	}

	private class NextPlayerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (board.getCurrentPlayerTurn() != 0) {
				boardPanel.setHasMoved(true);
			}


			if (nextPlayer.isEnabled() && boardPanel.getHasMoved()) {
				board.nextPlayer();
				rollThatDie();
				turn.setText(board.getPlayers().get(board.currentPlayerTurn).getPlayerName());
				boardPanel.setHasMoved(false);
				boardPanel.repaint();
			}

			if (board.getCurrentPlayerTurn() == 0 && !boardPanel.getHasMoved()) {
				JFrame frame = new JFrame();
				JOptionPane.showMessageDialog(frame, "Click a Highlighted (Cyan) Tile", "Move Your Player", JOptionPane.INFORMATION_MESSAGE);

			}

			if (board.getCurrentPlayerTurn() != 0) {
				Player currentPlayer = board.getPlayers().get(board.currentPlayerTurn);
				BoardCell destination = currentPlayer.pickLocation(currentPlayer.getRow(), currentPlayer.getColumn(), board.getDieRoll());
//				board.movePlayer(board.currentPlayerTurn, ((int)rect.getY()-1)/40, ((int)rect.getX()-1)/40);
				board.movePlayer(board.currentPlayerTurn, destination.getRow(), destination.getColumn());
				boardPanel.repaint();
			}
		}
	}

	private class AccusationListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				AccusationPrompt accusationDialog = new AccusationPrompt();
				
				JDialog response= new JDialog();
				JTextArea message = new JTextArea();
				boolean showDialog = true;
				
				// Make a dialog in case the button is pressed at the wrong time
				message.setEditable(false);
				if (board.getCurrentPlayerTurn() != 0) {
					message.setText("It is not your turn!");
					showDialog = false;
				} else if (boardPanel.getHasMoved()) {
					message.setText("You have already made a move");
					showDialog = false;
				}
				
				if (showDialog) {
					accusationDialog.showAccusationPrompt();
					if (!accusationDialog.getResult()) {
						boardPanel.repaint();
					}
				} else {
					// set up and display the error dialog
					response.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					response.setTitle("Accusation Response");
					response.setSize(200,200);
					response.setLocationRelativeTo(null);
					response.setModalityType(ModalityType.APPLICATION_MODAL);
					response.add(message);
					response.setVisible(true);
				}
			} catch (FileNotFoundException | BadConfigFormatException e1) {
				e1.printStackTrace();
			}
			
		}


	}
}

