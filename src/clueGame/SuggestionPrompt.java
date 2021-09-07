package clueGame;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.junit.BeforeClass;
import experiment.ControlGUI;

public class SuggestionPrompt extends JDialog {
	private static DetectiveNotes lists = new DetectiveNotes();
	private static ArrayList<Card> people;
	private static ArrayList<Card> rooms;
	private static ArrayList<Card> weapons;
	private static Board board = Board.getInstance();

	

	private JButton submitButton = new JButton("Submit");
	private JButton cancelButton = new JButton("Cancel");
	
	JComboBox<String> playerBox;
	JTextField roomBox;
	JComboBox<String> weaponBox;
	
	String guessResponse;


	public String getGuessResponse() {
		return guessResponse;
	}

	public SuggestionPrompt() throws FileNotFoundException, BadConfigFormatException{
		// steal the lists from DetectiveNotes
		people = lists.getPeople();
		rooms = lists.getRooms();
		weapons = lists.getWeapons();

		setLayout(new GridLayout(4,1));
		JPanel panel1 = suggestionRoomsPanel();
		JPanel panel2 = suggestionPeoplePanel();
		JPanel panel3 = suggestionWeaponsPanel();
		JPanel panel4 = suggestionButtons();
		add(panel1);
		add(panel2);
		add(panel3);
		add(panel4);

		// Add Action Listeners to the buttons
		SubmitButtonListener submitListener = new SubmitButtonListener();
		submitButton.addActionListener(submitListener);
		
		CancelButtonListener cancelListener = new CancelButtonListener();
		cancelButton.addActionListener(cancelListener);
	}

	private JPanel suggestionPeoplePanel() {

		// Make an array of people's names
		String[] playerNames = new String[people.size()];
		for (int i = 0; i < playerNames.length; i++) {
			playerNames[i] = people.get(i).getCardName();
		}

		// Make people panel
		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(1,2));
		JPanel peoplePanel = new JPanel();
		peoplePanel.setBorder(new TitledBorder (new EtchedBorder(), "People"));



		// Add the drop-down menu
		JPanel personGuess = new JPanel();
		personGuess.setVisible(true);
		personGuess.setBorder(new TitledBorder (new EtchedBorder(), "Person"));
		panel.add(personGuess);




		playerBox = new JComboBox<String>(playerNames);

		playerBox.setVisible(true);
		personGuess.add(playerBox);

		return panel;
	}


	private JPanel suggestionRoomsPanel() {

		// Make an array of room names
		String[] roomNames = new String[rooms.size()];
		for (int i = 0; i < roomNames.length; i++) {
			roomNames[i] = rooms.get(i).getCardName();
		}


		String currentRoom;
		if(board.getCurrentPlayerTurn() == -1) {
			currentRoom = "Test Room";
		} else {
			Player currentPlayer = board.getPlayers().get(board.currentPlayerTurn);
			BoardCell currentCell = board.getCellAt(currentPlayer.getRow(), currentPlayer.getColumn());
			currentRoom = board.getLegend().get(currentCell.getInitial());
		}


		// Make room panel
		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(1,2));
		JPanel roomPanel = new JPanel();
		roomPanel.setBorder(new TitledBorder (new EtchedBorder(), "Rooms"));




		// Add the drop-down menu
		JPanel roomGuess = new JPanel();
		roomGuess.setVisible(true);
		roomGuess.setBorder(new TitledBorder (new EtchedBorder(), "Your Room"));
		panel.add(roomGuess);




		roomBox = new JTextField(10);

		roomBox.setHorizontalAlignment(JTextField.CENTER);
		roomBox.setEditable(false);
		roomBox.setText(currentRoom);
		roomBox.setVisible(true);
		roomGuess.add(roomBox);

		return panel;
	}

	private JPanel suggestionWeaponsPanel() {

		// Make an array of room names
		String[] weaponNames = new String[weapons.size()];
		for (int i = 0; i < weaponNames.length; i++) {
			weaponNames[i] = weapons.get(i).getCardName();
		}

		// Make room panel
		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(1,2));
		JPanel weaponPanel = new JPanel();
		weaponPanel.setBorder(new TitledBorder (new EtchedBorder(), "Weapons"));




		// Add the drop-down menu
		JPanel weaponGuess = new JPanel();
		weaponGuess.setVisible(true);
		weaponGuess.setBorder(new TitledBorder (new EtchedBorder(), "Weapon"));
		panel.add(weaponGuess);



		// Make weapon combo box
		weaponBox = new JComboBox<String>(weaponNames);

		weaponBox.setVisible(true);
		weaponGuess.add(weaponBox);

		return panel;
	}

	private JPanel suggestionButtons() {
		JLabel playerTurnLayout = new JLabel();


		JPanel panel = new JPanel();


		//add the turnPanel as well as the two buttons to the GUI
		panel.add(submitButton, BorderLayout.WEST);
		panel.add(cancelButton, BorderLayout.EAST);




		return panel;
	}



	public static void showSuggestionPrompt() throws FileNotFoundException, BadConfigFormatException {
		SuggestionPrompt dialog = new SuggestionPrompt();
		dialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		dialog.setTitle("Make a Guess");
		dialog.setSize(300, 300);
		dialog.setLocationRelativeTo(null);

		// Now let's view it
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);
		dialog.setVisible(true);

	}

//	public static void main (String[] args) throws FileNotFoundException, BadConfigFormatException {
//		DetectiveNotes lists = new DetectiveNotes();
//		lists.loadLists();
//		showSuggestionPrompt();
//	}

	private class SubmitButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			guessResponse = (String) playerBox.getSelectedItem() + ", " + (String) weaponBox.getSelectedItem() + ", " + roomBox.getText();
//			guessResponse[0] = (String) playerBox.getSelectedItem();
//			guessResponse[1] = roomBox.getText();
//			guessResponse[2] = 	(String) weaponBox.getSelectedItem();
//			
			PlayerActionsPanel playerActions = PlayerActionsPanel.getInstance();
			playerActions.guess.setText(guessResponse);
			
			Card[] suggestionArray = {new Card((String)playerBox.getSelectedItem(), CardType.PERSON), new Card((String)weaponBox.getSelectedItem(), CardType.WEAPON), new Card(roomBox.getText(), CardType.ROOM)}; 
			Card disprove = board.handleSuggestion(board.getPlayers().get(0), suggestionArray);
			
			if (disprove != null) {
				playerActions.disprove.setText(disprove.getCardName());
			} else {
				playerActions.disprove.setText("no new clue");
			}
//			System.out.println(playerBox.getSelectedItem());
//			System.out.println(roomBox.getText());
//			System.out.println(weaponBox.getSelectedItem());
			dispose();
		}
	}

	private class CancelButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dispose();	
		}


	}
	
	public static ArrayList<Card> getPeople() {
		return people;
	}

	public static ArrayList<Card> getRooms() {
		return rooms;
	}

	public static ArrayList<Card> getWeapons() {
		return weapons;
	}
}




