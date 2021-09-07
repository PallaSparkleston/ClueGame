package clueGame;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;


import java.util.Scanner;
import java.util.Set;

import clueGame.BoardCell;
import tests.CTest_FileInitTests;
import tests.ClueBoardTests;

public class Board {

	public static final int MAX_BOARD_SIZE = 50;

	private static final int NUM_PLAYERS = 6;

	//	Rows and Columns
	private static int numRows = MAX_BOARD_SIZE;
	private static int numColumns = MAX_BOARD_SIZE;


	private static BoardCell[][] board = new BoardCell[numRows][numColumns];
	private Map<Character, String> legend = new HashMap<>();
	private Map<Character, String> type = new HashMap<>();
	private Map<BoardCell, Set<BoardCell>> adjMatrix = new HashMap<>();
	private Set<BoardCell> targets = new HashSet<>();
	private ArrayList<Player> players = new ArrayList<>();
	private ArrayList<Card> weaponCards = new ArrayList<>();
	private ArrayList<Card> playerCards= new ArrayList<>();
	private ArrayList<Card> roomCards = new ArrayList<>();
	private ArrayList<Card> deck = new ArrayList<>();
	Card[] hiddenCards = new Card[3];
	HashSet<BoardCell> adjSet = new HashSet<>();
	HashSet<BoardCell> visited = new HashSet<>();
	int deckSize;
	int dieRoll;
	int currentPlayerTurn = -1;
	private Card[] currentGuess;
	private Card[] lastSuggestion;
	private Card lastDisprove = new Card("Empty", CardType.PERSON);

	public Card[] getLastSuggestion() {
		return lastSuggestion;
	}

	public Card getLastDisprove() {
		return lastDisprove;
	}

	public Card[] getCurrentGuess() {
		return currentGuess;
	}
	public void setCurrentGuess(Card[] playerGuess) {
		this.currentGuess = playerGuess;
	}

	private String boardConfigFile;
	private String roomConfigFile;
	private String playerConfigFile;
	private String weaponConfigFile;


	// variable used for singleton pattern
	private static Board theInstance = new Board();
	// constructor is private to ensure only one can be created
	private Board() {
	}
	// this method returns the only Board
	public static Board getInstance() {
		if (theInstance == null) {
			theInstance = new Board();
		}
		return theInstance;
	}


	public void initialize() throws FileNotFoundException, BadConfigFormatException {
		try {
			loadRoomConfig();
			loadBoardConfig();
			calcAdjacencies();
			makeDeck();
		} catch (BadConfigFormatException e) {
			e.getMessage();
		}

	}

	public void loadRoomConfig() throws BadConfigFormatException, FileNotFoundException {
		// check for bad room format
		Iterator room = type.entrySet().iterator();
		while (room.hasNext()) {
			Map.Entry roomType = (Map.Entry)room.next();
			switch ((String)roomType.getValue()) {
			case "Card":
			case "Other":
				break; // do nothing if the format is correct
			default:
				throw new BadConfigFormatException(roomConfigFile);
			}

			room.remove(); // avoids a ConcurrentModificationException
		}

	}

	public void loadBoardConfig() throws BadConfigFormatException, FileNotFoundException {
		// check for bad columns
		for (int i = 0; i < numRows; i++) {
			int count = 0;
			for (int j = 0; j < numColumns; j++) {
				if (board[i][j] != null) {
					count++;
				}
			}
			if (count != numColumns) {
				throw new BadConfigFormatException(boardConfigFile);
			}
		}

		// check for bad rooms
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				if (!legend.containsKey(board[i][j].getInitial())) {
					throw new BadConfigFormatException();
				}
			}

		}
	}

	// Is run when initialized and builds the adjMatrix map
	public void calcAdjacencies() {
		adjMatrix.clear();

		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				calcAdjList(board, i, j);
			}
		}
	}

	public void calcAdjList(BoardCell[][] grid, int row, int column) {
		adjSet = new HashSet<>();
		if(grid[row][column].getColumn() + 1 < numColumns && grid[row][column+1].isWalkway(grid[row][column])) {
			adjSet.add(grid[row][column+1]);
		}
		if(grid[row][column].getColumn() - 1 >= 0 && grid[row][column-1].isWalkway(grid[row][column])) {
			adjSet.add(grid[row][column-1]);
		}
		if(grid[row][column].getRow() + 1 < numRows && grid[row+1][column].isWalkway(grid[row][column])) {
			adjSet.add(grid[row+1][column]);
		}
		if(grid[row][column].getRow() - 1 >= 0 && grid[row-1][column].isWalkway(grid[row][column])) {
			adjSet.add(grid[row-1][column]);
		}
		adjMatrix.put(grid[row][column], adjSet);
	}

	public void calcTargets (int row, int column, int pathLength) {
		BoardCell currentCell = getCellAt(row, column);
		visited.add(currentCell);
		for(BoardCell adjCell: adjMatrix.get(currentCell)) {
			if(visited.contains(adjCell)) {
				continue;
			}
			visited.add(adjCell);
			if(pathLength == 1 || adjCell.getDoorDirection() != DoorDirection.NONE) {
				targets.add(adjCell);
			}
			else {
				calcTargets(adjCell.getRow(), adjCell.getColumn(), pathLength-1);
			}
			visited.remove(adjCell);
		}
	}


	public Set<BoardCell> getAdjList(int row, int column) {
		BoardCell keyCell = getCellAt(row, column);
		return adjMatrix.get(keyCell);
	}

	public Set<BoardCell> getTargets() {
		return targets;
	}

	public Map<Character, String> getLegend() {
		return legend;
	}
	public int getNumRows() {
		return numRows;
	}
	public void setNumRows(int r) {
		numRows = r;
	}
	public int getNumColumns() {
		return numColumns;
	}
	public void setNumColumns(int c) {
		numColumns = c;
	}

	public BoardCell getCellAt(int i, int j) {
		return board[i][j];
	}
	public void setConfigFiles(String string, String string2) throws BadConfigFormatException, FileNotFoundException {
		// Set boardFile and roomFile
		boardConfigFile = string;
		roomConfigFile = string2;

		// Read in the two files
		FileReader reader = new FileReader(string);
		Scanner boardStuff = new Scanner (reader);



		// count the number of rows
		numRows = 0;
		while(boardStuff.hasNext()) {
			boardStuff.nextLine();
			numRows++;
		}

		// set the boardStuff reader back to the beginning
		boardStuff.close();
		FileReader reader2 = new FileReader(string);
		Scanner boardStuff2 = new Scanner (reader2);

		// read in the board tile by tile
		for (int i=0; i < numRows; i++) {
			if (!boardStuff2.hasNext()) { break; }

			// Get entire row and store it in an array
			String cellLabel = boardStuff2.nextLine();
			String[] currentStringRow = cellLabel.split(",");

			if (i == 0) {
				// Get numColumns, but only the first time
				numColumns = currentStringRow.length;
			}

			// Make an array of BoardCell
			BoardCell[] currentBoardRow = new BoardCell[numColumns];


			for (int j = 0; j < numColumns; j++) {

				if (currentStringRow.length != numColumns) {
					continue;
				}

				// Get the cell symbol
				char cellLegend = currentStringRow[j].charAt(0);

				currentBoardRow[j] = new BoardCell(j, i, cellLegend);

				// Get direction if a door is present
				if (currentStringRow[j].length() >= 2) {
					char cellDirection = currentStringRow[j].charAt(1);
					switch (cellDirection) {
					case 'U':
						currentBoardRow[j].setDoor(DoorDirection.UP);
						break;
					case 'D':
						currentBoardRow[j].setDoor(DoorDirection.DOWN);
						break;
					case 'L':
						currentBoardRow[j].setDoor(DoorDirection.LEFT);
						break;
					case 'R':
						currentBoardRow[j].setDoor(DoorDirection.RIGHT);
						break;
					case 'N':
						currentBoardRow[j].setDoor(DoorDirection.NONE);
						break;
					}
				}

				// put row in the map
				board[i] = currentBoardRow;




			}
		}

		boardStuff2.close();

		// Open Legend file
		FileReader reader1 = new FileReader(string2);
		Scanner legendStuff = new Scanner(reader1);

		legendStuff.useDelimiter(",|\r\n");

		// read in the legend word by word
		while (legendStuff.hasNext()) {
			// Gets the symbol
			if (!legendStuff.hasNext()) { break; }
			String legendSymbol = legendStuff.next();
			Character actualLegendSymbol = legendSymbol.charAt(0);

			// Gets the description
			String legendDescription = legendStuff.next();
			if (!legendStuff.hasNext()) { break; }
			legendDescription = legendDescription.substring(1); // Remove the space

			if (!legendStuff.hasNext()) { break; }
			legend.put(actualLegendSymbol, legendDescription); // put the symbol and description into map

			// Gets the type (this is put in the type map)
			if (!legendStuff.hasNext()) { break; }
			String legendType = legendStuff.next();

			legendType = legendType.substring(1); // Remove the space

			type.put(actualLegendSymbol, legendType);
		}
		legendStuff.close();

		// Now make room cards and store them in roomCards
		for(Character room: legend.keySet()) {
			if (type.get(room).equals("Card")) {
				Card roomCard = new Card(legend.get(room), CardType.ROOM);
				roomCards.add(roomCard);
			}
		}

	}

	public void clearLists() {
		visited.clear();
		targets.clear();

	}

	public void loadConfigFiles(String string, String string2) throws FileNotFoundException, BadConfigFormatException {
		// Set boardFile and roomFile
		playerConfigFile = string;
		weaponConfigFile = string2;

		// Read in the Players
		FileReader reader = new FileReader(string);
		Scanner playerStuff = new Scanner (reader);



		// Gets the line, then splits into the player and color
		String playerString = playerStuff.nextLine();
		String[] playerInfo = playerString.split(",");

		// Gets player name
		String playerName = playerInfo[0];

		// Get the player's color from their symbol
		char playerColorSymbol = playerInfo[1].charAt(1);
		Color playerColor;
		switch (playerColorSymbol) {
		case 'R':
			playerColor = Color.RED;
			break;
		case 'Y':
			playerColor = Color.YELLOW;
			break;
		case 'B':
			playerColor = Color.BLUE;
			break;
		case 'M':
			playerColor = Color.MAGENTA;
			break;
		case 'G':
			playerColor = Color.GREEN;
			break;
		case 'W':
			playerColor = Color.WHITE;
			break;
		default:
			playerColor = Color.BLACK;
		}

		// Make the human player and add them to the player set
		HumanPlayer human = new HumanPlayer(playerName, playerColor);
		players.add(human); // put into players

		// read in the players line by line
		while (playerStuff.hasNext()) {
			// Gets the line, then splits into the player and color
			playerString = playerStuff.nextLine();
			playerInfo = playerString.split(",");

			// Gets player name
			playerName = playerInfo[0];

			// Get the player's color from their symbol
			playerColorSymbol = playerInfo[1].charAt(1);
			switch (playerColorSymbol) {
			case 'R':
				playerColor = Color.RED;
				break;
			case 'Y':
				playerColor = Color.YELLOW;
				break;
			case 'B':
				playerColor = Color.BLUE;
				break;
			case 'M':
				playerColor = Color.MAGENTA;
				break;
			case 'G':
				playerColor = Color.GREEN;
				break;
			case 'W':
				playerColor = Color.WHITE;
				break;	
			default:
				playerColor = Color.BLACK;
			}

			// Make the computer players and add them to the player set
			ComputerPlayer computer = new ComputerPlayer(playerName, playerColor);
			players.add(computer); // put into players


		}

		// now turn each player into a card and add to playerCards set
		for(Player P: players) {
			Card playerCard = new Card(P.getPlayerName(), CardType.PERSON);
			playerCards.add(playerCard);
		}

		playerStuff.close();

		// Now, read in the Weapons
		FileReader reader2 = new FileReader(string2);
		Scanner weaponStuff = new Scanner (reader2);

		// Read in the weapons line by line
		while (weaponStuff.hasNext()) {
			String weaponName = weaponStuff.nextLine();

			// Turn weapons into cards and add them to the weapon cards
			Card weaponCard = new Card(weaponName, CardType.WEAPON);
			weaponCards.add(weaponCard);
		}

		weaponStuff.close();
		

	}
	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void movePlayer(int playerIndex, int row, int col) {
		players.get(playerIndex).setRow(row);
		players.get(playerIndex).setColumn(col);
	}

	public ArrayList<Card> getPlayerCards() {
		return playerCards;
	}
	public ArrayList<Card> getWeaponCards() {
		return weaponCards;
	}
	public ArrayList<Card> getRoomCards() {
		return roomCards;
	}
	public Card[] drawSecretCards() {
		// Get a random card from playerCards
		int randomNum = (int) (Math.random()*5);
		hiddenCards[0] = playerCards.get(randomNum);
		playerCards.remove(randomNum); // make sure to remove the hidden card

		// Get a random card from weaponCards
		randomNum = (int) (Math.random()*5);
		hiddenCards[1] = weaponCards.get(randomNum);
		weaponCards.remove(randomNum); // make sure to remove the hidden card

		// Get a random card from roomCards
		randomNum = (int) (Math.random()*8);
		hiddenCards[2] = roomCards.get(randomNum);
		roomCards.remove(randomNum); // make sure to remove the hidden card

		return hiddenCards;
	}


	public void makeDeck() {
		// draw the secret cards before the deck is made
		drawSecretCards();

		// add cards from each set to the deck
		for (Card card: playerCards) {
			Card deckCard = new Card(card.getCardName(), card.getCardType());
			deck.add(deckCard);
		}
		for (Card card: weaponCards) {
			Card deckCard = new Card(card.getCardName(), card.getCardType());
			deck.add(deckCard);
			}
		for (Card card: roomCards) {
			Card deckCard = new Card(card.getCardName(), card.getCardType());
			deck.add(deckCard);
			}

		deckSize = deck.size();
	}

	public void shuffleCards() {
		Collections.shuffle(deck);
	}

	public void dealCards() {
		shuffleCards();

		// give cards to each player, then remove card from deck
		for (Player P: players) {
			for (int i = 0; i < (deckSize/NUM_PLAYERS); i ++) {
				Card cardInHand = deck.get(0);
				P.giveCard(cardInHand);
				deck.remove(0);
			}
		}

	}

	public ArrayList<Card> getDeck() {
		return deck;
	}

	public boolean handleAccusation(Card[] accusation) {
		// Move the accused player to the accused room
		String accusedPlayer = accusation[0].getCardName();
		
		String accusedRoom = accusation[2].getCardName();
		
		char accusedRoomKey = 'X';
        for (Map.Entry<Character,String> entry : legend.entrySet()) {
        	if (accusedRoom.equals(entry.getValue())) {
        		accusedRoomKey = entry.getKey();
        	}
        }
        
        int accusedRoomDoorRow = 0;
        int accusedRoomDoorColumn = 0;
        for (int i=0; i < numRows; i++) {
			for (int j=0; j < numColumns; j++) {
				BoardCell currentCell = getCellAt(j, i);
				if (accusedRoomKey == currentCell.getInitial() && currentCell.getDoorDirection() != DoorDirection.NONE) {
					accusedRoomDoorRow = currentCell.getRow();
					accusedRoomDoorColumn = currentCell.getColumn();
					break;
				}
			}
        }
		
		for (Player p: players) {
			if (accusedPlayer.equals(p.getPlayerName())) {
				movePlayer(p.getPlayerIndex(), accusedRoomDoorRow, accusedRoomDoorColumn);
			}
		}
		
		for (int i = 0; i < 3; i++) {
			if (!accusation[i].equals(hiddenCards[i])) {
				return false;
			}
		}
		return true;
	}

	public Card handleSuggestion(Player player, Card[] suggestion) {
		lastSuggestion = suggestion;
		
		ArrayList<Card> disproveList = new ArrayList<>();
		ArrayList<Card> humanDisproveList = new ArrayList<>();
		
		if (player.playerIndex != 5) {
			for (int i = player.playerIndex+1; i < players.size(); i++) {
				disproveList.add(players.get(i).disproveSuggestion(suggestion));
			}

			for (int i = 0; i < player.playerIndex; i++) {
				if (player.playerIndex == 0) {
					break;
				} else {
					if (i == 0) {
						humanDisproveList.add(players.get(i).disproveSuggestion(suggestion));
					} else {
						disproveList.add(players.get(i).disproveSuggestion(suggestion));
					}
				}
			}


		} else {
			for (int i = 0; i < 5; i++) {
				disproveList.add(players.get(i).disproveSuggestion(suggestion));
			}
		}
		
		

		// delete all null elements
		disproveList.removeAll(Collections.singleton(null));
		
		if (disproveList.isEmpty() && humanDisproveList.isEmpty()) {
			lastDisprove = null;
			
			// if the last disprove is null, every computer player will try to accuse until an accusation is made
			for (Player p:players) {
				p.setWillAccuse(true);
			}
			
			return null;
		} else if (disproveList.isEmpty() && !humanDisproveList.isEmpty()) {
			lastDisprove = humanDisproveList.get(0);
			return humanDisproveList.get(0);
		} else {
			lastDisprove = disproveList.get(0);
			return disproveList.get(0);
		}
	}






	// used only in tests to make sure the Computer Players are being good little boys and girls
	public boolean testPickLocation(int row, int column, int pathLength, int numNewRoomsNear) {
		clearLists();
		calcTargets(row, column, pathLength);

		Set<BoardCell> locationsPicked = new HashSet<>();

		// pick targets a lot so that randomly chosen spots are accounted for
		for (int i = 0; i < 1000; i++) {
			players.get(2).clearPreviouslyVisitedRooms();
			locationsPicked.add(players.get(2).pickLocation(row, column, pathLength));
		}

		// if no new doors are near
		if (numNewRoomsNear == 0) {
			if (targets.equals(locationsPicked)) {
				return true;
			} else {
				return false;
			}
		} else {
			// if any number of new doors are near, make sure that the computer visits one of the doors
			if (locationsPicked.size() == numNewRoomsNear) {
				return true;
			} else {
				return false;
			}
		}
	}

	public Card[] whisperHiddenCards() {
		return hiddenCards;
	}
	
	public void rollDie() {
		dieRoll = (int) (Math.random()*6 + 1);
	}
	
	public int getDieRoll() {
		return dieRoll;
	}
	
	public void nextPlayer() {
		incrementPlayerTurn();
		
		// check if the next computer player wants to make an accusation, and if so, do it 
		players.get(currentPlayerTurn).makeAccusation();
		
	}
	
	public int getCurrentPlayerTurn() {
		return currentPlayerTurn;
	}
	
	public void incrementPlayerTurn() {
		switch (currentPlayerTurn) {
		case(-1):
			currentPlayerTurn = 0;
			break;
		case(5):
			currentPlayerTurn = 0;
			break;
		default:
			currentPlayerTurn++;
		}
	}



}
