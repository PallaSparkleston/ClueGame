package clueGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.junit.BeforeClass;

import experiment.ControlGUI;

public class DetectiveNotes extends JPanel {
	private static ArrayList<Card> people = new ArrayList<>(); 
	private static ArrayList<Card> rooms = new ArrayList<>(); 
	private static ArrayList<Card> weapons = new ArrayList<>(); 
	private static Board board;

	public DetectiveNotes(){
		setLayout(new GridLayout(3,2));
		JPanel panel1 = detectiveNotesPeoplePanel();
		JPanel panel2 = detectiveNotesRoomsPanel();
		JPanel panel3 = detectiveNotesWeaponsPanel();
		add(panel1);
		add(panel2);
		add(panel3);
	}
	
	public static void clearLists() {
		// Make sure the array lists are empty
		people.clear();
		rooms.clear();
		weapons.clear();
		
	}

	public static void loadLists() throws FileNotFoundException, BadConfigFormatException{
		clearLists();
		board = Board.getInstance();
		board.setConfigFiles("ClueRoom.csv", "ClueRooms.txt");
		board.loadConfigFiles("CluePlayers.txt", "ClueWeapons.txt");
//		System.out.println(board.getDeck());
		board.initialize();
//		System.out.println(board.getDeck());
//		System.out.println(people);
//		System.out.println(board.getPlayers().get(0).getPlayerCardPool().get(0));
//
//		board.dealCards();
//		System.out.println(board.getDeck());
//		System.out.println(people);
//		System.out.println(board.getPlayers().get(0).getPlayerCardPool().get(0));
		// Load people
		for(Card c: board.getPlayerCards()) {
			people.add(c);
		}
		Card secretPerson = board.whisperHiddenCards()[0];
		
		people.add(board.whisperHiddenCards()[0]);
		
		// Load weapons
		for(Card c: board.getWeaponCards()) {
			weapons.add(c);
		}
		Card secretWeapon = board.whisperHiddenCards()[1];
		
		weapons.add(board.whisperHiddenCards()[1]);
		
		// Load rooms
		for(Card c: board.getRoomCards()) {
			rooms.add(c);
		}
		Card secretRoom = board.whisperHiddenCards()[2];
		
		rooms.add(board.whisperHiddenCards()[2]);
		
		Collections.shuffle(people);
		Collections.shuffle(weapons);
		Collections.shuffle(rooms);
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


	public static Board getBoard() {
		return board;
	}

	private JPanel detectiveNotesPeoplePanel() {

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


		//peoplePanel.add(checkBoxPanel);


		// add People
		for (String person: playerNames) {
			JCheckBox player = new JCheckBox(person);
			peoplePanel.add(player);
		}
		
		//checkBoxPanel.add(player2);
		peoplePanel.setVisible(true);
		//peoplePanel.add(checkBoxPanel);
		panel.add(peoplePanel);




		// Add the drop-down menu
		JPanel personGuess = new JPanel();
		personGuess.setVisible(true);
		personGuess.setBorder(new TitledBorder (new EtchedBorder(), "Person Guess"));
		panel.add(personGuess);




		final JComboBox<String> playerBox = new JComboBox<String>(playerNames);

		playerBox.setVisible(true);
		personGuess.add(playerBox);

		return panel;
	}


	private JPanel detectiveNotesRoomsPanel() {

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



		for (String roomName: roomNames) {
			JCheckBox room = new JCheckBox(roomName);
			roomPanel.add(room);
		}
		
		roomPanel.setVisible(true);
		panel.add(roomPanel);




		// Add the drop-down menu
		JPanel roomGuess = new JPanel();
		roomGuess.setVisible(true);
		roomGuess.setBorder(new TitledBorder (new EtchedBorder(), "Room Guess"));
		panel.add(roomGuess);




		final JComboBox<String> roomBox = new JComboBox<String>(roomNames);

		roomBox.setVisible(true);
		roomGuess.add(roomBox);

		return panel;
	}
	
	private JPanel detectiveNotesWeaponsPanel() {

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



		for (String weaponName: weaponNames) {
			JCheckBox weapon = new JCheckBox(weaponName);
			weaponPanel.add(weapon);
		}
		
		weaponPanel.setVisible(true);
		panel.add(weaponPanel);




		// Add the drop-down menu
		JPanel weaponGuess = new JPanel();
		weaponGuess.setVisible(true);
		weaponGuess.setBorder(new TitledBorder (new EtchedBorder(), "Weapon Guess"));
		panel.add(weaponGuess);




		final JComboBox<String> weaponBox = new JComboBox<String>(weaponNames);

		weaponBox.setVisible(true);
		weaponGuess.add(weaponBox);

		return panel;
	}
	
	public static void showDetectiveNotes() throws FileNotFoundException, BadConfigFormatException {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setTitle("Detective Notes");
		frame.setSize(600, 600);	
		// Create the JPanel and add it to the JFrame
		DetectiveNotes gui = new DetectiveNotes();
		frame.add(gui, BorderLayout.CENTER);
		// Now let's view it
		frame.setVisible(true);
	}

	public static void main (String[] args) throws FileNotFoundException, BadConfigFormatException {
		loadLists();
		showDetectiveNotes();
	}
}
