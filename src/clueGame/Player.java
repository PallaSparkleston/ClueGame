package clueGame;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public abstract class Player {
	private String playerName;
	protected int playerIndex;
	protected int row;
	protected int col;
	protected boolean isHuman;
	private Color color;
	private ArrayList<Card> cards = new ArrayList<>();
	protected ArrayList<Card> playerCardPool = new ArrayList<>();
	protected ArrayList<Card> weaponCardPool = new ArrayList<>();
	protected ArrayList<Card> roomCardPool = new ArrayList<>();
	private DetectiveNotes lists = new DetectiveNotes();
	
	public Player(String name, Color color, boolean isH) throws FileNotFoundException, BadConfigFormatException {
		playerName = name;
		this.color = color;
		isHuman = isH;
		
		switch (playerName) {
		case "Miss Scarlet":
			row = 0;
			col = 17;
			playerIndex = 0;
			break;
		case "Colonel Mustard":
			row = 3;
			col = 12;
			playerIndex = 1;
			break;
		case "Mrs Peacock":
			row = 4;
			col = 4;
			playerIndex = 2;
			break;
		case "Professor Plum":
			row = 12;
			col = 2;
			playerIndex = 3;
			break;
		case "Mr Green":
			row = 17;
			col = 5;
			playerIndex = 4;
			break;
		case "Mrs White":
			row = 18;
			col = 12;
			playerIndex = 5;
			break;
		
		}
		
		Board board = Board.getInstance();
		
		
		
		// populate playerCardPool
		playerCardPool = lists.getPeople();
		
		
//		for (Card card: board.getPlayerCards()) {
//			Card deckCard = new Card(card.getCardName(), card.getCardType());
//			playerCardPool.add(deckCard);
//		}
//		playerCardPool.add(board.whisperHiddenCards()[0]);
		
		
		// populate weaponCardPool
		weaponCardPool = lists.getWeapons();
		
//		for (Card card: board.getWeaponCards()) {
//			Card deckCard = new Card(card.getCardName(), card.getCardType());
//			weaponCardPool.add(deckCard);
//		}
//		weaponCardPool.add(board.whisperHiddenCards()[1]);
		
		// populate roomCardPool
		roomCardPool = lists.getRooms();
		
		//		for (Card card: board.getRoomCards()) {
//			Card deckCard = new Card(card.getCardName(), card.getCardType());
//			roomCardPool.add(deckCard);
//		}
//		roomCardPool.add(board.whisperHiddenCards()[2]);
//		System.out.println(playerCardPool);
//		System.out.println(weaponCardPool);
//		System.out.println(roomCardPool);
	}
	
	public ArrayList<Card> getPlayerCardPool() {
		return playerCardPool;
	}

	public void setPlayerCardPool(ArrayList<Card> playerCardPool) {
		this.playerCardPool = playerCardPool;
	}

	public ArrayList<Card> getWeaponCardPool() {
		return weaponCardPool;
	}

	public void setWeaponCardPool(ArrayList<Card> weaponCardPool) {
		this.weaponCardPool = weaponCardPool;
	}

	public ArrayList<Card> getRoomCardPool() {
		return roomCardPool;
	}

	public void setRoomCardPool(ArrayList<Card> roomCardPool) {
		this.roomCardPool = roomCardPool;
	}

	public Card disproveSuggestion(Card[] suggestion) {
		Random rand = new Random();
		
		ArrayList<Card> matchingCards = new ArrayList<>();
		
		// iterate through the suggestions, checking if each card is in hand, and then push matching cards to an array
		for (Card c: suggestion) {
			for (Card hand: cards) {
				if (c.equals(hand)) {
					matchingCards.add(hand);
				}
			}
		}
		
		// randomly select a card from the matchingCards array
		if (matchingCards.size() > 0) {
			return matchingCards.get(rand.nextInt(matchingCards.size()));
		} else {
			return null;
		}
	}

	public String getPlayerName() {
		return playerName;
	}

	public Color getPlayerColor() {
		return color;
	}

	public boolean getIsHuman() {
		return isHuman;
	}

	public BoardCell getPlayerStart() {
		Board board = Board.getInstance();
		return board.getCellAt(row, col);
	}

	public ArrayList<Card> getCards() {
		return cards;
	}
	
	public void setCards(ArrayList<Card> seen) {
		cards = seen;
	}

	public void giveCard(Card cardInHand) {
		cards.add(cardInHand);
	}
	
	public void takeCards() {
		cards.clear();
	}

	public int getRow() {
		return row;
	}
	
	public void setRow(int r) {
		row = r;
	}
	
	public int getColumn() {
		return col;
	}
	
	public void setColumn(int c) {
		col = c;
	}
	
	// used only in tests to make sure that disproving suggestions works
	public boolean testDisproveSuggestion(Card[] suggestion, int numMatches) {
		
		Set<Card> disproves = new HashSet<>();
		
		// disprove the same suggestion a lot so that randomly chosen cards are accounted for
		for (int i = 0; i < 1000; i++) {
			disproves.add(disproveSuggestion(suggestion));
		}

		// if no cards in hand match the suggestion
		if (numMatches == 0) {
			if (disproves.size() == 1 && disproves.contains(null)) {
				return true;
			} else {
				return false;
			}
		} else if (numMatches == 1){
			// if one card in hand matches the suggestion
			if (disproves.size() == 1 && !disproves.contains(null)) {
				return true;
			} else {
				return false;
			}
		} else {
			// else, more than one card in hand matches the suggestion, so randomly choose one
			if(disproves.size() == numMatches) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public int getPlayerIndex() {
		return playerIndex;
	}
	

	
	protected abstract BoardCell pickLocation(int r, int c, int pathLength);
	public abstract Card[] createSuggestion();
	public abstract boolean testCardSuggestionChoice(int i, int j);

	public abstract void setSeenPlayers(ArrayList<Card> seen);

	public abstract void setSeenWeapons(ArrayList<Card> seen);

	public abstract void clearPreviouslyVisitedRooms();
	
	public abstract void makeAccusation();
	
	public abstract void setWillAccuse(boolean wa);



}



