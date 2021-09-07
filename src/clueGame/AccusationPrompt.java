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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.junit.BeforeClass;
import experiment.ControlGUI;

public class AccusationPrompt extends JDialog {
	private static DetectiveNotes lists = new DetectiveNotes();
	private static ArrayList<Card> people;
	private static ArrayList<Card> rooms;
	private static ArrayList<Card> weapons;
	private static Board board = Board.getInstance();
	private static BoardPanel boardPanel = BoardPanel.getInstance();

	

	private JButton submitButton = new JButton("Submit");
	private JButton cancelButton = new JButton("Cancel");
	
	JComboBox<String> playerBox;
	JComboBox<String> roomBox;
	JComboBox<String> weaponBox;
	
	Card[] accusation;
	boolean result;


	public Card[] getAccusation() {
		return accusation;
	}
	
	public boolean getResult() {
		return result;
	}

	public AccusationPrompt() throws FileNotFoundException, BadConfigFormatException{
		System.out.println(board.whisperHiddenCards()[0].getCardName());
		System.out.println(board.whisperHiddenCards()[1].getCardName());
		System.out.println(board.whisperHiddenCards()[2].getCardName());
		// steal the lists from DetectiveNotes
		people = lists.getPeople();
		rooms = lists.getRooms();
		weapons = lists.getWeapons();

		setLayout(new GridLayout(4,1));
		JPanel panel1 = accusationRoomsPanel();
		JPanel panel2 = accusationPeoplePanel();
		JPanel panel3 = accusationWeaponsPanel();
		JPanel panel4 = accusationButtons();
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

	private JPanel accusationPeoplePanel() {

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


	private JPanel accusationRoomsPanel() {

		// Make an array of room names
		String[] roomNames = new String[rooms.size()];
		for (int i = 0; i < roomNames.length; i++) {
			roomNames[i] = rooms.get(i).getCardName();
		}



		// Make room panel
		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(1,2));
		JPanel roomPanel = new JPanel();
		roomPanel.setBorder(new TitledBorder (new EtchedBorder(), "Rooms"));




		// Add the drop-down menu
		JPanel roomGuess = new JPanel();
		roomGuess.setVisible(true);
		roomGuess.setBorder(new TitledBorder (new EtchedBorder(), "Room"));
		panel.add(roomGuess);




		roomBox = new JComboBox<String>(roomNames);

		roomBox.setVisible(true);
		roomGuess.add(roomBox);

		return panel;
	}

	private JPanel accusationWeaponsPanel() {

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

	private JPanel accusationButtons() {
		JLabel playerTurnLayout = new JLabel();


		JPanel panel = new JPanel();


		//add the turnPanel as well as the two buttons to the GUI
		panel.add(submitButton, BorderLayout.WEST);
		panel.add(cancelButton, BorderLayout.EAST);




		return panel;
	}



	public static void showAccusationPrompt() throws FileNotFoundException, BadConfigFormatException {
		AccusationPrompt dialog = new AccusationPrompt();
		dialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		dialog.setLocationRelativeTo(null);
		dialog.setTitle("Make an Accusation");
		dialog.setSize(300, 300);

		// Now let's view it
		dialog.setModalityType(ModalityType.APPLICATION_MODAL);
		dialog.setVisible(true);

	}

	public static void main (String[] args) throws FileNotFoundException, BadConfigFormatException {
		DetectiveNotes lists = new DetectiveNotes();
		lists.loadLists();
		showAccusationPrompt();
	}

	private class SubmitButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			accusation = new Card[3];
			accusation[0] = new Card((String)playerBox.getSelectedItem(), CardType.PERSON);
			accusation[1] = new Card((String)weaponBox.getSelectedItem(), CardType.WEAPON);
			accusation[2] = new Card((String)roomBox.getSelectedItem(), CardType.ROOM);
			
			
			JDialog response= new JDialog();
			JTextArea message = new JTextArea();
			
			
			// Check if hidden cards and accusation match
			boolean isCorrect = true;
			for (int i = 0; i < 3; i++) {
				if (!board.whisperHiddenCards()[i].getCardName().equals(accusation[i].getCardName())) {
					isCorrect = false;
				}
			}
			message.setEditable(false);
			if (isCorrect) {
				result = true;
				message.setText("Correct accusation\n\r You win!");
			} else {
				result = false;
				message.setText("Incorrect accusation");
			}
			
			response.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			response.setTitle("Accusation Response");
			response.setSize(200,200);
			response.setLocationRelativeTo(null);
			response.setModalityType(ModalityType.APPLICATION_MODAL);
			response.add(message);
			response.setVisible(true);
			
			if (isCorrect) {
				System.exit(0);
			} else {
				dispose();
				boardPanel.setHasMoved(true);
			}
			
			
			
		}
	}

	private class CancelButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dispose();	
		}


	}
}



