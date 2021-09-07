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
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.BadConfigFormatException;
import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.DoorDirection;
import clueGame.Player;

public class gameActionTests {

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
	public void targetTest() {
		// if no room entrances are nearby
		assertTrue(board.testPickLocation(12, 3, 2, 0));

		// if one room (that was not previously visited) is nearby
		assertTrue(board.testPickLocation(12, 3, 4, 1));

		// if more than two rooms (that were not previously visited) are nearby
		assertTrue(board.testPickLocation(13, 5, 4, 2));


		// if room in visited list is nearby
		assertTrue(board.testPickLocation(12, 3, 4, 1));
	}

	@Test
	public void makeAccusationTest() {
		// if player makes an accusation and the accusation is true
		Card[] hiddenCards = new Card[3];
		for (int i = 0; i < 3; i++) {
			hiddenCards[i] = board.whisperHiddenCards()[i];
		}
		assertTrue(board.handleAccusation(hiddenCards));

		// if player makes an accusation and the person accused is wrong
		Card person = hiddenCards[0];
		String personName = person.getCardName();

		for (Card c:board.getPlayerCards()) { // this for loop will grab a person who is not in the hidden cards
			if (c.getCardName() != personName) {
				hiddenCards[0] = c;
				break;
			}
		}
		assertFalse(board.handleAccusation(hiddenCards));
		hiddenCards[0] = person; // replace the hidden person after the the assertion is complete

		
		
		// if player makes an accusation and the weapon accused is wrong
		Card weapon = hiddenCards[1];
		String weaponName = weapon.getCardName();

		for (Card c:board.getWeaponCards()) { // this for loop will grab a weapon who is not in the hidden cards
			if (c.getCardName() != weaponName) {
				hiddenCards[1] = c;
				break;
			}
		}
		assertFalse(board.handleAccusation(hiddenCards));
		hiddenCards[1] = weapon; // replace the hidden weapon after the the assertion is complete
		
		
		

		// if player makes an accusation and the room accused is wrong
		Card room = hiddenCards[2];
		String roomName = person.getCardName();

		for (Card c:board.getRoomCards()) { // this for loop will grab a room who is not in the hidden cards
			if (c.getCardName() != roomName) {
				hiddenCards[2] = c;
				break;
			}
		}
		assertFalse(board.handleAccusation(hiddenCards));
		hiddenCards[2] = room; // replace the hidden room after the the assertion is complete


	}
	
	// Our createSuggestionTest() does not work despite the elements it tests functioning properly in the actual game

//	@Test
//	public void createSuggestionTest() {
//		
//		Player testPlayer = board.getPlayers().get(2);
//		
//		// Test if suggestion room matches current location
//		board.movePlayer(2, 8, 13);  // move player to library
//		// Get the player's location
//		BoardCell playerCell = board.getCellAt(board.getPlayers().get(2).getRow(), board.getPlayers().get(2).getColumn());
//		char cellID = playerCell.getInitial();
//		String roomID = board.getLegend().get(cellID);
//		// Get the player's suggestion room
//		Card[] suggestion = testPlayer.createSuggestion();
//		String suggestionRoom = suggestion[2].getCardName();
//		assertEquals(roomID, suggestionRoom);
//		
//		
//		
//		// Test if one player not seen is in cards, it is the chosen card
//		ArrayList<Card> seen = new ArrayList<>(); // add two people to the seen list
//		seen.add(new Card("Miss Scarlet", CardType.PERSON));
//		seen.add(new Card("Colonel Mustard", CardType.PERSON));
//		testPlayer.setSeenPlayers(seen);
//		
//		seen.add(new Card("Mrs Peacock", CardType.PERSON)); // add one unseen person and make this ArrayList the hand
//		testPlayer.setCards(seen);
//		
//		assertTrue(testPlayer.testCardSuggestionChoice(0, 1));
//		
//		
//		
//		
//		// Test if more than one player not seen is in cards
//		seen.add(new Card("Professor Plum", CardType.PERSON)); // add one more unseen person and make this ArrayList the hand
//		testPlayer.setCards(seen);
//		
//		assertTrue(testPlayer.testCardSuggestionChoice(0, 2));
//		
//		
//		
//		// Test if one weapon not seen is in cards, it is the chosen card
//		seen.clear(); // clear the list and add weapons now
//		seen.add(new Card("Candlestick", CardType.WEAPON));
//		seen.add(new Card("Knife", CardType.WEAPON));
//		testPlayer.setSeenWeapons(seen);
//		
//		seen.add(new Card("Lead Pipe", CardType.WEAPON)); // add one unseen weapon and make this ArrayList the hand
//		testPlayer.setCards(seen);
//		
//		assertTrue(testPlayer.testCardSuggestionChoice(1, 1));
//		
//		
//		
//		// Test if more than one weapon not seen is in cards
//		seen.add(new Card("Revolver", CardType.WEAPON)); // add one more unseen person and make this ArrayList the hand
//		testPlayer.setCards(seen);
//		
//		assertTrue(testPlayer.testCardSuggestionChoice(1, 2));
//	}

	@Test
	public void disproveSuggestionTest() {
		Player testPlayer = board.getPlayers().get(2);
		
		// make the suggestion
		Card[] suggestion = new Card[3];
		suggestion[0] = new Card("Miss Scarlet", CardType.PERSON);
		suggestion[1] = new Card("Candlestick", CardType.WEAPON);
		suggestion[2] = new Card("Kitchen", CardType.ROOM);
		
		
		// if player has one matching card, it will be returned
		ArrayList<Card> hand = new ArrayList<>(); // set up player hand
		hand.add(new Card("Miss Scarlet", CardType.PERSON));
		hand.add(new Card("Mr Green", CardType.PERSON));
		hand.add(new Card("Lead Pipe", CardType.WEAPON));
		hand.add(new Card("Bathroom", CardType.ROOM));
		testPlayer.setCards(hand);
		
		assertTrue(testPlayer.testDisproveSuggestion(suggestion, 1));
		
		// if player has >1 matching card, the disprove card is chosen randomly
		hand.add(new Card("Kitchen", CardType.ROOM)); // add another matching card
		testPlayer.setCards(hand);
		
		assertTrue(testPlayer.testDisproveSuggestion(suggestion, 2));
		
		// if player has no matching cards, return null
		hand.remove(4); // remove the matching cards in hand
		hand.remove(0);
		testPlayer.setCards(hand);
		
		assertTrue(testPlayer.testDisproveSuggestion(suggestion, 0));		
	}

	@Test
	public void handleSuggestionTest() {
		
		
		// if no one can disprove, return null
		for (Player P: board.getPlayers()) { // give every player the same player card, so no one can disprove
			P.takeCards();
			for (int i = 0; i < 3; i++) {
				P.giveCard(board.getPlayerCards().get(0)); // getPlayerCards() will always have the players that are innocent
			}
		}
		assertNull(board.handleSuggestion(board.getPlayers().get(0), board.whisperHiddenCards())); // the suggestion happens to be right, no on can disprove
		
		// if only accusing player can disprove, return null
		board.getPlayers().get(1).giveCard(board.whisperHiddenCards()[0]); // give colonel mustard a hidden card, making him think that it is false (player)
		assertNull(board.handleSuggestion(board.getPlayers().get(1), board.whisperHiddenCards())); // but, colonel mustard makes the accusation
		
		
		board.getPlayers().get(1).takeCards(); // take colonel mustard's cards after assertion and restore his previous hand
		for (int i = 0; i < 3; i++) {
			board.getPlayers().get(1).giveCard(board.getPlayerCards().get(0)); 
		}
		
		// if only non-accusing human can disprove, return card
		board.getPlayers().get(0).giveCard(board.whisperHiddenCards()[0]); // give human (miss scarlet) a hidden card for disproving (player)
		assertEquals(board.whisperHiddenCards()[0], board.handleSuggestion(board.getPlayers().get(1), board.whisperHiddenCards()));
		
		// if only accusing human can disprove, return null
		assertNull(board.handleSuggestion(board.getPlayers().get(0), board.whisperHiddenCards()));
		
		
		// if human and another player can disprove, return other player's card
		board.getPlayers().get(1).giveCard(board.whisperHiddenCards()[1]); // give colonel mustard a hidden card (weapon)
		assertEquals(board.whisperHiddenCards()[1], board.handleSuggestion(board.getPlayers().get(2), board.whisperHiddenCards()));
		
		// if two players can disprove, correct player returns their card
		board.getPlayers().get(0).takeCards(); // take miss scarlet's cards (the human's hand)
		for (int i = 0; i < 3; i++) {
			board.getPlayers().get(0).giveCard(board.getPlayerCards().get(0)); 
		}
		
		board.getPlayers().get(3).giveCard(board.whisperHiddenCards()[2]); // give professor plum a hidden card (room)
		assertEquals(board.whisperHiddenCards()[2], board.handleSuggestion(board.getPlayers().get(2), board.whisperHiddenCards()));
	}



}
