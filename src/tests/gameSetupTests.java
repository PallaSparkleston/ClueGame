package tests;

/*
 * This program tests that config files are loaded properly.
 */

// Doing a static import allows me to write assertEquals rather than
// Assert.assertEquals
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.BadConfigFormatException;
import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.DoorDirection;

public class gameSetupTests {

	private static final int NUM_PLAYER_CARDS = 3;
	private static final int NUM_PLAYERS = 6;
	private static Board board;
	
	@BeforeClass
	public static void setUp() throws FileNotFoundException, BadConfigFormatException {
		// get and set up the board class
		board = Board.getInstance();
		board.setConfigFiles("ClueRoom.csv", "ClueRooms.txt");
		board.loadConfigFiles("CluePlayers.txt", "ClueWeapons.txt");
		board.initialize();
	}

	@Test
	public void LoadPeopleTest() {

		// test if there are 6 players
		assertEquals(6, board.getPlayers().size());

		// test each player's name
		assertEquals("Miss Scarlet", board.getPlayers().get(0).getPlayerName());
		assertEquals("Colonel Mustard", board.getPlayers().get(1).getPlayerName());
		assertEquals("Mrs Peacock", board.getPlayers().get(2).getPlayerName());
		assertEquals("Professor Plum", board.getPlayers().get(3).getPlayerName());
		assertEquals("Mr Green", board.getPlayers().get(4).getPlayerName());
		assertEquals("Mrs White", board.getPlayers().get(5).getPlayerName());

		// test if the colors of each player match
		assertEquals(Color.RED, board.getPlayers().get(0).getPlayerColor());
		assertEquals(Color.YELLOW, board.getPlayers().get(1).getPlayerColor());
		assertEquals(Color.BLUE, board.getPlayers().get(2).getPlayerColor());
		assertEquals(Color.MAGENTA, board.getPlayers().get(3).getPlayerColor());
		assertEquals(Color.GREEN, board.getPlayers().get(4).getPlayerColor());
		assertEquals(Color.WHITE, board.getPlayers().get(5).getPlayerColor());

		// test if first player is the human
		assertTrue(board.getPlayers().get(0).getIsHuman());

		// test if third player is a computer
		assertFalse(board.getPlayers().get(2).getIsHuman());

		// test if last player is a computer
		assertFalse(board.getPlayers().get(5).getIsHuman());

		// test each player's starting location
		assertEquals(board.getCellAt(0, 17), board.getPlayers().get(0).getPlayerStart());
		assertEquals(board.getCellAt(3, 12), board.getPlayers().get(1).getPlayerStart());
		assertEquals(board.getCellAt(4, 4), board.getPlayers().get(2).getPlayerStart());
		assertEquals(board.getCellAt(12, 2), board.getPlayers().get(3).getPlayerStart());
		assertEquals(board.getCellAt(17, 5), board.getPlayers().get(4).getPlayerStart());
		assertEquals(board.getCellAt(18, 12), board.getPlayers().get(5).getPlayerStart());
	}

	@Test
	public void DeckInitializedTest() {

		// test if there are NUM_PLAYERS-1 player cards because one of them was taken to be the secret player
		assertEquals(NUM_PLAYERS-1, board.getPlayerCards().size());


		// test if there are 5 weapons cards because one of them was taken to be the secret weapon
		assertEquals(5, board.getWeaponCards().size());


		// test if there are 8 rooms cards because one of them was taken to be the secret room
		assertEquals(8, board.getRoomCards().size());


		// test getting the three cards to start the game
		Card[] hiddenCards = board.drawSecretCards();
		assertEquals(hiddenCards[0].getCardType(), CardType.PERSON);
		assertEquals(hiddenCards[1].getCardType(), CardType.WEAPON);
		assertEquals(hiddenCards[2].getCardType(), CardType.ROOM);

	}


	@Test
	public void DealCardsTest() {
		board.dealCards();

		// test if the board deck is empty
		assertEquals(0, board.getDeck().size());

		// test if each player has 3 cards
		assertEquals(NUM_PLAYER_CARDS, board.getPlayers().get(0).getCards().size());
		assertEquals(NUM_PLAYER_CARDS, board.getPlayers().get(1).getCards().size());
		assertEquals(NUM_PLAYER_CARDS, board.getPlayers().get(2).getCards().size());
		assertEquals(NUM_PLAYER_CARDS, board.getPlayers().get(3).getCards().size());
		assertEquals(NUM_PLAYER_CARDS, board.getPlayers().get(4).getCards().size());
		assertEquals(NUM_PLAYER_CARDS, board.getPlayers().get(5).getCards().size());

		// test if the cards are different
		ArrayList<String> cards = new ArrayList<>();
		for (int i = 0; i < board.getPlayers().size(); i++) {
			for (int j = 0; j < board.getPlayers().get(i).getCards().size(); j++) {
				cards.add(board.getPlayers().get(i).getCards().get(j).getCardName());
			}
		}
		Collections.sort(cards);
		for (int i = 0; i < cards.size()-1; i++) {
			if (cards.get(i).equals(cards.get(i+1))) {
				fail("Duplicate " + cards.get(i) + " exists");
			}
		}

	}

}
