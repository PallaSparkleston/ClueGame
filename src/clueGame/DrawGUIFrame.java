package clueGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class DrawGUIFrame extends JFrame {
	private BoardPanel drawBoard;
	private PlayerActionsPanel drawPlayerActions;
	private static Player human;
	private static Board board;
	private ArrayList<Card> displayedCards;
	//private static ArrayList<Card> deck = new ArrayList<>(); 
	private int dx, dy;

	public DrawGUIFrame() throws FileNotFoundException, BadConfigFormatException
	{
		setSize(1010, 1000);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Clue Game");
		
		JMenuBar fileMenuBar = new JMenuBar();
		setJMenuBar(fileMenuBar);
		fileMenuBar.add(createFileMenu());
		
		
		drawBoard = BoardPanel.getInstance();
		drawPlayerActions = PlayerActionsPanel.getInstance();
		
		add(drawBoard);
		add(drawPlayerActions, BorderLayout.SOUTH);
		
		loadHuman();
		displayedCards = human.getCards();
		JPanel cardPanel = makeCardPanel();
		add(cardPanel, BorderLayout.LINE_END);
		
	}
	
	private JMenu createFileMenu() throws FileNotFoundException, BadConfigFormatException {
		JMenu menu = new JMenu("File");
		menu.add(createFileDetectiveNotesItem());
		
		menu.add(createFileExitItem());
		return menu;
	}
	
	private JMenuItem createFileExitItem() {
		JMenuItem item = new JMenuItem("Exit");

		// Create a subclass for detective notes
		class MenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		}
		
		item.addActionListener(new MenuItemListener());
		
		return item;
	}
	
	private JMenuItem createFileDetectiveNotesItem() throws FileNotFoundException, BadConfigFormatException {
		JMenuItem item = new JMenuItem("Detective Notes");
		DetectiveNotes.loadLists();
		// Create a subclass for detective notes
		
		class MenuItemListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {		
				try {
					DetectiveNotes.showDetectiveNotes();
				} catch (FileNotFoundException | BadConfigFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		item.addActionListener(new MenuItemListener());
		
		return item;
	}
	
	
//	public void updateDrawing(int dx, int dy)
//	{
//		this.dx = dx;
//		this.dy = dy;
//		//drawPanel.translate(dx, dy);
//		// 1000 millisecond delay
//		Timer t = new Timer(2000, new TimerListener());
//		t.start();
//
//	}
//	private class TimerListener implements ActionListener {
//		public void actionPerformed(ActionEvent e) {
//			drawPanel.translate(dx, dy);
//		}
//	}
	public JPanel makeCardPanel() throws FileNotFoundException, BadConfigFormatException {
		//deals cards to all players
	    board.dealCards();

	    // For testing
	    //	    for(int i = 0; i < 3; i ++) {
//	        System.out.println(board.whisperHiddenCards()[i].getCardName());
//	    }
//	    for(int i = 0; i < 6; i ++) {
//	    	for (int j = 0; j < 3; j ++) {
//	    		System.out.println(board.getPlayers().get(i).getCards().get(j).getCardName());
//	    	}
//	    }
	    
	    
	    //ArrayLists to store cards for card types Players, Rooms, and Weapons
		ArrayList<Card> peopleCards = new ArrayList<>();
		ArrayList<Card> roomsCards = new ArrayList<>();
		ArrayList<Card> weaponsCards = new ArrayList<>();
		
		//New JPanel to hold everything
		JPanel panel = new JPanel();
		//SetLayout for CardPanel, 3 rows with 1 item in each row
		panel.setLayout(new GridLayout(3,1));
		
		//Used TextArea instead of TextField because it has the newline character
		JTextArea peopleText = new JTextArea(12,12);
		JTextArea roomsText = new JTextArea(12,12);
		JTextArea weaponsText = new JTextArea(12,12);
		
		//Set Borders for People, Rooms, and Weapons
		peopleText.setBorder(new TitledBorder (new EtchedBorder(), "People"));
		roomsText.setBorder(new TitledBorder (new EtchedBorder(), "Rooms"));
		weaponsText.setBorder(new TitledBorder (new EtchedBorder(), "Weapons"));
		
		// Iterate through the player's cards and organize them by type
		for(Card c: board.getPlayers().get(0).getCards()) {
			switch (c.getCardType()) {
			case PERSON:
				peopleCards.add(c);
				break;
			case ROOM:
				roomsCards.add(c);
				break;
			case WEAPON:
				weaponsCards.add(c);
				break;
			}
		}
		
		
		//Three for loops used to print out the cards onto the JTextArea
		for(int i = 0; i < peopleCards.size(); i++) {
			peopleText.replaceSelection(peopleCards.get(i).getCardName() + "\n\n");
		}
		for(int i = 0; i < roomsCards.size(); i++) {
			roomsText.replaceSelection(roomsCards.get(i).getCardName() + "\n\n");			
		}
		for(int i = 0; i < weaponsCards.size(); i++) {
			weaponsText.replaceSelection(weaponsCards.get(i).getCardName() + "\n\n");			
		}
		
		//Made the Card's uneditable, incase the user accidently deletes card information
		peopleText.setEditable(false);
		roomsText.setEditable(false);
		weaponsText.setEditable(false);
		
		//Added the JTextArea to the final panel
		panel.add(peopleText);
		panel.add(roomsText);
		panel.add(weaponsText);
		//Set the title of the panel
		panel.setBorder(new TitledBorder (new EtchedBorder(), "My Cards"));

		
		return panel;
		
	}
//

	public static void loadHuman() {
		board = Board.getInstance();
		for (Player person: board.getPlayers()) {
			if (person.isHuman) {
				human = person;
			}
		}
	}
	
	private class TileListener extends JPanel implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}

	


	public static void main(String[] args) throws FileNotFoundException, BadConfigFormatException {
		//loadPeople();
		//loadHuman();
		DrawGUIFrame frame = new DrawGUIFrame();
		frame.loadHuman();
		JOptionPane.showMessageDialog(frame, "You are " + human.getPlayerName() + ", press Next Player to begin play", "Welcome to Clue", JOptionPane.INFORMATION_MESSAGE);
		frame.setVisible(true);
		


	}

}
