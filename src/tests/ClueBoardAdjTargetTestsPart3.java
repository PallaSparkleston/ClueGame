package tests;

import java.io.FileNotFoundException;

/*
 * This program tests that adjacencies and targets are calculated correctly.
 */

import java.util.Set;

//Doing a static import allows me to write assertEquals rather than
//assertEquals
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.BadConfigFormatException;
import clueGame.Board;
import clueGame.BoardCell;

public class ClueBoardAdjTargetTestsPart3 {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
	private static Board board;
	@BeforeClass
	public static void setUp() throws FileNotFoundException, BadConfigFormatException {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueRoom.csv", "ClueRooms.txt");
		board.loadConfigFiles("CluePlayers.txt", "ClueWeapons.txt");
		// Initialize will load BOTH config files 
		board.initialize();
	}

	// Ensure that player does not move around within room
	// These cells are BLUE on our planning spreadsheet
	@Test
	public void testAdjacenciesInsideRooms()
	{
		// Test upper edge
		Set<BoardCell> testList = board.getAdjList(0, 7);
		assertEquals(0, testList.size());
		// Test a corner
		testList = board.getAdjList(0, 19);
		assertEquals(0, testList.size());
		// Test near walkway
		testList = board.getAdjList(6, 15);
		assertEquals(0, testList.size());
		// Test one that is in middle of room
		testList = board.getAdjList(7, 16);
		assertEquals(0, testList.size());

	}

	// Ensure that the adjacency list from a doorway is only the
	// walkway. NOTE: This test could be merged with door 
	// direction test. 
	// These tests are LIGHT GREEN on our planning spreadsheet
	@Test
	public void testAdjacencyRoomExit()
	{
		// TEST DOORWAY RIGHT 
		Set<BoardCell> testList = board.getAdjList(17, 10);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(17, 11)));
		// TEST DOORWAY LEFT 
		testList = board.getAdjList(8, 13);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(8, 12)));
		//TEST DOORWAY DOWN
		testList = board.getAdjList(3, 10);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(4, 10)));
		//TEST DOORWAY UP
		testList = board.getAdjList(5, 2);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(4, 2)));
		//TEST DOORWAY RIGHT, WHERE THERE'S A WALKWAY TO THE RIGHT
		testList = board.getAdjList(5, 2);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(4, 2)));
		
	}
	
	// Test adjacency at entrance to rooms
	// These tests are GREEN in planning spreadsheet
	@Test
	public void testAdjacencyDoorways()
	{
		// Test beside a door direction UP next to a room wall
		Set<BoardCell> testList = board.getAdjList(4, 2);
		assertTrue(testList.contains(board.getCellAt(4, 3)));
		assertTrue(testList.contains(board.getCellAt(5, 2)));
		assertTrue(testList.contains(board.getCellAt(3, 2)));
		assertEquals(3, testList.size());
		// Test beside a door direction DOWN
		testList = board.getAdjList(10, 2);
		assertTrue(testList.contains(board.getCellAt(9, 2)));
		assertTrue(testList.contains(board.getCellAt(10, 3)));
		assertTrue(testList.contains(board.getCellAt(10, 1)));
		assertTrue(testList.contains(board.getCellAt(11, 2)));
		assertEquals(4, testList.size());
		// Test beside a door direction RIGHT next to a room wall
		testList = board.getAdjList(16, 5);
		assertTrue(testList.contains(board.getCellAt(15, 5)));
		assertTrue(testList.contains(board.getCellAt(17, 5)));
		assertTrue(testList.contains(board.getCellAt(16, 4)));
		assertEquals(3, testList.size());
		// Test beside a door direction UP
		testList = board.getAdjList(13, 7);
		assertTrue(testList.contains(board.getCellAt(13, 6)));
		assertTrue(testList.contains(board.getCellAt(13, 8)));
		assertTrue(testList.contains(board.getCellAt(12, 7)));
		assertTrue(testList.contains(board.getCellAt(14, 7)));
		assertEquals(4, testList.size());
		// Test beside a door direction RIGHT
		testList = board.getAdjList(17, 11);
		assertTrue(testList.contains(board.getCellAt(16, 11)));
		assertTrue(testList.contains(board.getCellAt(18, 11)));
		assertTrue(testList.contains(board.getCellAt(17, 12)));
		assertTrue(testList.contains(board.getCellAt(17, 10)));
		assertEquals(4, testList.size());
		// Test beside a door direction LEFT next to the closet
		testList = board.getAdjList(8, 12);
		assertTrue(testList.contains(board.getCellAt(7, 12)));
		assertTrue(testList.contains(board.getCellAt(9, 12)));
		assertTrue(testList.contains(board.getCellAt(8, 13)));
		assertEquals(3, testList.size());
		// Test beside a door direction LEFT next to a room wall
		testList = board.getAdjList(8, 17);
		assertTrue(testList.contains(board.getCellAt(7, 17)));
		assertTrue(testList.contains(board.getCellAt(9, 17)));
		assertTrue(testList.contains(board.getCellAt(8, 18)));
		assertEquals(3, testList.size());
		// Test beside a door direction UP next to a room wall
		testList = board.getAdjList(15, 18);
		assertTrue(testList.contains(board.getCellAt(14, 18)));
		assertTrue(testList.contains(board.getCellAt(15, 19)));
		assertTrue(testList.contains(board.getCellAt(16, 18)));
		assertEquals(3, testList.size());
		// Test beside a door direction UP next to a room wall
		testList = board.getAdjList(10, 19);
		assertTrue(testList.contains(board.getCellAt(10, 18)));
		assertTrue(testList.contains(board.getCellAt(11, 19)));
		assertEquals(2, testList.size());
	}

	// Test a variety of walkway scenarios
	// These tests are PURPLE on our planning spreadsheet
	@Test
	public void testAdjacencyWalkways()
	{
		// Test on top edge of board, just one walkway piece
		Set<BoardCell> testList = board.getAdjList(0, 4);
		assertTrue(testList.contains(board.getCellAt(1, 4)));
		assertEquals(1, testList.size());
		
		// Test on top edge of board, just one walkway piece
		testList = board.getAdjList(0, 12);
		assertTrue(testList.contains(board.getCellAt(1, 12)));
		assertEquals(1, testList.size());
		
		// Test on top edge of board, just one walkway piece
		testList = board.getAdjList(0, 17);
		assertTrue(testList.contains(board.getCellAt(1, 17)));
		assertEquals(1, testList.size());
		
		// Test on left edge of board, three walkway pieces
		testList = board.getAdjList(12, 0);
		assertTrue(testList.contains(board.getCellAt(11, 0)));
		assertTrue(testList.contains(board.getCellAt(13, 0)));
		assertTrue(testList.contains(board.getCellAt(12, 1)));
		assertEquals(3, testList.size());

		// Test on bottom of board, just one walkway piece
		testList = board.getAdjList(19, 5);
		assertTrue(testList.contains(board.getCellAt(18, 5)));
		assertEquals(1, testList.size());

		// Test surrounded by 4 walkways
		testList = board.getAdjList(15,12);
		assertTrue(testList.contains(board.getCellAt(15, 13)));
		assertTrue(testList.contains(board.getCellAt(15, 11)));
		assertTrue(testList.contains(board.getCellAt(14, 12)));
		assertTrue(testList.contains(board.getCellAt(16, 12)));
		assertEquals(4, testList.size());
		
		// Test on bottom edge of board, just one walkway piece
		testList = board.getAdjList(19, 13);
		assertTrue(testList.contains(board.getCellAt(19, 12)));
		assertEquals(1, testList.size());
		
		// Test on right edge of board, next to 1 room piece
		testList = board.getAdjList(13, 19);
		assertTrue(testList.contains(board.getCellAt(14, 19)));
		assertTrue(testList.contains(board.getCellAt(13, 18)));
		assertEquals(2, testList.size());

	}
	
	
	// Tests of just walkways, 1 step, includes on edge of board
	// and beside room
	// Have already tested adjacency lists on all four edges, will
	// only test two edges here
	// These are PINK on our planning spreadsheet
	@Test
	public void testTargetsOneStep() {
		board.clearLists();
		board.calcTargets(13, 1, 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCellAt(14, 1)));
		assertTrue(targets.contains(board.getCellAt(13, 0)));	
		assertTrue(targets.contains(board.getCellAt(13, 2)));	
		assertTrue(targets.contains(board.getCellAt(12, 1)));	
		
		board.clearLists();
		board.calcTargets(1, 3, 1);
		targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCellAt(1, 4)));
		assertTrue(targets.contains(board.getCellAt(2, 3)));	
		
		board.clearLists();
		board.calcTargets(19, 12, 1);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCellAt(19, 11)));
		assertTrue(targets.contains(board.getCellAt(18, 12)));	
		assertTrue(targets.contains(board.getCellAt(19, 13)));
	}
	
	// Tests of just walkways, 2 steps
	// These are PINK on the planning spreadsheet
	@Test
	public void testTargetsTwoSteps() {
		board.clearLists();
		board.calcTargets(13, 1, 2);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCellAt(14, 0)));
		assertTrue(targets.contains(board.getCellAt(14, 2)));	
		assertTrue(targets.contains(board.getCellAt(12, 0)));	
		assertTrue(targets.contains(board.getCellAt(12, 2)));
		assertTrue(targets.contains(board.getCellAt(11, 1)));
		assertTrue(targets.contains(board.getCellAt(13, 3)));
		
		board.clearLists();
		board.calcTargets(1, 3, 2);
		targets= board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCellAt(0, 4)));
		assertTrue(targets.contains(board.getCellAt(2, 4)));
		assertTrue(targets.contains(board.getCellAt(2, 2)));
		assertTrue(targets.contains(board.getCellAt(3, 3)));
		
		board.clearLists();
		board.calcTargets(19, 12, 2);
		targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCellAt(17, 12)));
		assertTrue(targets.contains(board.getCellAt(18, 11)));		
	}
	
	// Tests of just walkways, 4 steps
	// These are PINK on the planning spreadsheet
	@Test
	public void testTargetsFourSteps() {
		board.clearLists();
		board.calcTargets(13, 1, 4);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(12, targets.size());
		assertTrue(targets.contains(board.getCellAt(12, 0)));
		assertTrue(targets.contains(board.getCellAt(14, 2)));	
		assertTrue(targets.contains(board.getCellAt(14, 0)));	
		assertTrue(targets.contains(board.getCellAt(13, 3)));
		assertTrue(targets.contains(board.getCellAt(14, 4)));	
		assertTrue(targets.contains(board.getCellAt(12, 4)));	
		assertTrue(targets.contains(board.getCellAt(13, 5)));	
		assertTrue(targets.contains(board.getCellAt(11, 3)));	
		assertTrue(targets.contains(board.getCellAt(10, 2)));	
		assertTrue(targets.contains(board.getCellAt(11, 1)));	
		assertTrue(targets.contains(board.getCellAt(10, 0)));	
		assertTrue(targets.contains(board.getCellAt(12, 2)));	
				
	}	
	
	// Tests of just walkways plus one door, 6 steps
	// These are PINK on the planning spreadsheet
	// Note: there is enough movement to get into the Arcade from here

	@Test
	public void testTargetsSixSteps() {
		board.clearLists();
		board.calcTargets(19, 12, 6);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(9, targets.size());
		assertTrue(targets.contains(board.getCellAt(16, 11)));
		assertTrue(targets.contains(board.getCellAt(15, 12)));	
		assertTrue(targets.contains(board.getCellAt(14, 11)));	
		assertTrue(targets.contains(board.getCellAt(13, 12)));	
		assertTrue(targets.contains(board.getCellAt(14, 13)));	
		assertTrue(targets.contains(board.getCellAt(15, 14)));	
		assertTrue(targets.contains(board.getCellAt(17, 12)));
		assertTrue(targets.contains(board.getCellAt(17, 10)));	
		assertTrue(targets.contains(board.getCellAt(18, 11)));	
	}	
	
	// Test getting into room, doesn't require all steps
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsIntoRoomShortcut() 
	{
		board.clearLists();
		board.calcTargets(4, 16, 3);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(9, targets.size());
		// directly up and down
		assertTrue(targets.contains(board.getCellAt(3, 16)));
		assertTrue(targets.contains(board.getCellAt(5, 16)));
		// directly right and left
		assertTrue(targets.contains(board.getCellAt(4, 17)));
		assertTrue(targets.contains(board.getCellAt(4, 15)));
		// right then down
		assertTrue(targets.contains(board.getCellAt(6, 17)));
		// down then left
		assertTrue(targets.contains(board.getCellAt(5, 14)));
		// all the way left
		assertTrue(targets.contains(board.getCellAt(4, 13)));
		// into the room
		assertTrue(targets.contains(board.getCellAt(3, 15)));		
		// right the up
		assertTrue(targets.contains(board.getCellAt(2, 17)));				
		
	}

	// Test getting out of a room
	// These are LIGHT GREEN on our planning spreadsheet
	@Test
	public void testRoomExit()
	{
		// Take one step, essentially just the adj list
		board.clearLists();
		board.calcTargets(8, 13, 1);
		Set<BoardCell> targets= board.getTargets();
		// Ensure doesn't exit through the wall
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCellAt(8, 12)));
		// Take two steps
		board.clearLists();
		board.calcTargets(8, 13, 2);
		targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCellAt(7, 12)));
		assertTrue(targets.contains(board.getCellAt(9, 12)));
	}

}
