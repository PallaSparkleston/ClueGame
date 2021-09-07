package clueGame;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import java.util.Random;

public class ComputerPlayer extends Player {
	private Set<Character> previouslyVisitedRooms = new HashSet<>();
	private ArrayList<Card> seenPeople = new ArrayList<>();
	private ArrayList<Card> seenWeapons = new ArrayList<>();
	private ArrayList<Card> seenRooms = new ArrayList<>();
	private boolean willAccuse = false;
	private Card[] computerAccusation;
	private static Board board = Board.getInstance();
	

	public ComputerPlayer(String name, Color color) throws FileNotFoundException, BadConfigFormatException {
		super(name, color, false);
		System.out.println("A computer enters");

	}
	
	@Override
	public BoardCell pickLocation(int row, int column, int pathLength) {
		Random rand = new Random();
		Board board = Board.getInstance();
		board.clearLists();
		board.calcTargets(row, column, pathLength);
		
		ArrayList<BoardCell> targetsToPick = new ArrayList<>();
		for (BoardCell cell: board.getTargets()) {
			if (cell.getDoorDirection() != DoorDirection.NONE && !previouslyVisitedRooms.contains(cell.getInitial())) {
				targetsToPick.add(cell);
			}
			
		}
		
		BoardCell pickedCell;
		
		if (targetsToPick.size() == 0) {
			ArrayList<BoardCell> targetsArray = new ArrayList<>();
			for (BoardCell cell: board.getTargets()) {
				targetsArray.add(cell);
			}
			pickedCell = targetsArray.get(rand.nextInt(targetsArray.size()));
		} else {
			pickedCell = targetsToPick.get(rand.nextInt(targetsToPick.size()));
		}
		
		if (pickedCell.getDoorDirection() != DoorDirection.NONE)
			previouslyVisitedRooms.add(pickedCell.getInitial());
		
		return pickedCell;
	}
	
	
	
	public void makeAccusation() {
		
		if (willAccuse) {
			computerAccusation = board.getLastSuggestion();
			
			// First, show a dialog stating that an accusation is being made
			JDialog accusationDialog = new JDialog();
			JTextArea message1 =new JTextArea();
			
			message1.setEditable(false);
			
			// Turn the accusation into a string
			String accusationString = getPlayerName();
			accusationString += " is accusing: ";
			for (Card element:computerAccusation) {
				accusationString +=  element.getCardName();

				// If its not the last element, add a comma
				if (element != computerAccusation[2]) {
					accusationString += ", ";
				}
			}
			
			message1.setText(accusationString);
			
			accusationDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			accusationDialog.setTitle("Accusation: " + getPlayerName());
			accusationDialog.setSize(500,100);
			accusationDialog.setLocationRelativeTo(null);
			accusationDialog.setModalityType(ModalityType.APPLICATION_MODAL);
			accusationDialog.add(message1);
			accusationDialog.setVisible(true);
			
			
			JDialog response= new JDialog();
			JTextArea message2 = new JTextArea();
			
			
			// Check if hidden cards and accusation match
			boolean isCorrect = true;
			for (int i = 0; i < 3; i++) {
				if (!board.whisperHiddenCards()[i].getCardName().equals(computerAccusation[i].getCardName())) {
					isCorrect = false;
				}
			}
			message2.setEditable(false);
			if (isCorrect) {
				message2.setText("Correct accusation, " + getPlayerName() + " wins.");
			} else {
				message2.setText("Incorrect accusation");
			}
			
			response.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			response.setTitle("Accusation Response");
			response.setSize(00,100);
			response.setLocationRelativeTo(null);
			response.setModalityType(ModalityType.APPLICATION_MODAL);
			response.add(message2);
			response.setVisible(true);
			
			// if they are correct, the game ends
			if (isCorrect) {
				System.exit(0);
			} else {
				// if not, then everyone will have willAccuse set back to false
				for (Player player:board.getPlayers()) {
					player.setWillAccuse(false);
				}
				board.incrementPlayerTurn();
			}
			
			
		}
	}
	
	public Card[] createSuggestion() {
//		System.out.println(playerCardPool);
//		System.out.println(weaponCardPool);
//		System.out.println(roomCardPool);
		Random rand = new Random();
		
		Board board = Board.getInstance();
		
		Card[] suggestion = new Card[3];
		
		// Suggest random unseen player card
		ArrayList<Card> randomPlayerCardPool = new ArrayList<>();
		for (Card c: playerCardPool) {
			if (!seenPeople.contains(c)) {
				randomPlayerCardPool.add(c);
			}
		}
		if (randomPlayerCardPool.size() == 0) {
			randomPlayerCardPool = playerCardPool;
		}
		suggestion[0] = randomPlayerCardPool.get(rand.nextInt(randomPlayerCardPool.size()));
		
		
		// Suggest random unseen weapon card
		ArrayList<Card> randomWeaponCardPool = new ArrayList<>();
		for (Card c: weaponCardPool) {
			if (!seenPeople.contains(c)) {
				randomWeaponCardPool.add(c);
			}
		}
		if (randomWeaponCardPool.size() == 0) {
			randomWeaponCardPool = weaponCardPool;
		}
		suggestion[1] = randomWeaponCardPool.get(rand.nextInt(randomWeaponCardPool.size()));
		
		
		// Suggest current room
		char roomID = board.getCellAt(row, col).getInitial();
		String roomName = board.getLegend().get(roomID);
		Card roomSuggestion = new Card(roomName, CardType.ROOM);
		suggestion[2] = roomSuggestion;
		
		return suggestion;
	}
	
	public void clearPreviouslyVisitedRooms() {
		previouslyVisitedRooms.clear();
	}
	

	
	
	
	// used only in tests to make sure the suggestions make sense (cardTypeIndex = 0
	// for testing players and 1 for testing weapons)
	public boolean testCardSuggestionChoice(int cardTypeIndex, int numUnseen) {
		
		Set<Card> suggestions = new HashSet<>();
		
		// create suggestion a lot so that randomly chosen cards are accounted for
		for (int i = 0; i < 1000; i++) {
			suggestions.add(createSuggestion()[cardTypeIndex]);
		}

		// if one unseen card is in hand
		if (numUnseen == 1) {
			if (suggestions.size() == 1) {
				return true;
			}
		} else if (numUnseen > 1){
			// if more than one unseen card is in hand
			if (suggestions.size()+1 == numUnseen) {
				return true;
			}
		} else {
			return true;
		}
		return true;
	}

	@Override
	public void setSeenPlayers(ArrayList<Card> seen) {
		seenPeople = seen;
		
	}

	@Override
	public void setSeenWeapons(ArrayList<Card> seen) {
		seenWeapons = seen;
		
	}

	@Override
	public void setWillAccuse(boolean wa) {
		willAccuse = wa;
		
	}
	

}
