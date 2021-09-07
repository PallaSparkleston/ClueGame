package tests;

/*
 * This program tests that config files are loaded properly.
 */

// Doing a static import allows me to write assertEquals rather than
// Assert.assertEquals
import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.BadConfigFormatException;
import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;

public class ClueBoardTests {
	// Constants that I will use to test whether the file was loaded correctly
	public static final int LEGEND_SIZE = 11;
	public static final int NUM_ROWS = 20;
	public static final int NUM_COLUMNS = 20;

	// NOTE: I made Board static because I only want to set it up one 
	// time (using @BeforeClass), no need to do setup before each test.
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
	@Test
	public void testTileIdentities() {
		// Get the map of initial => room 
		Map<Character, String> legend = board.getLegend();
		// Ensure we read the correct number of rooms
		assertEquals(LEGEND_SIZE, legend.size());
		// To ensure data is correctly loaded, test retrieving a few rooms 
		// from the hash, including the first and last in the file and a few others
		assertEquals("Kitchen", legend.get('K'));
		assertEquals("Arcade", legend.get('A'));
		assertEquals("Bathroom", legend.get('M'));
		assertEquals("Dining Room", legend.get('D'));
		assertEquals("Library", legend.get('L'));
		assertEquals("Bedroom", legend.get('B'));
		assertEquals("Piano Room", legend.get('P'));
		assertEquals("Observatory", legend.get('O'));
		assertEquals("Theater", legend.get('T'));
		assertEquals("Closet", legend.get('X'));
		assertEquals("Walkway", legend.get('W'));
	}
	
	@Test
	public void testBoardDimensions() {
		// Ensure we have the proper number of rows and columns
		assertEquals(NUM_ROWS, board.getNumRows());
		assertEquals(NUM_COLUMNS, board.getNumColumns());		
	}
	
	// Test a doorway in each direction (RIGHT/LEFT/UP/DOWN), plus 
	// two cells that are not a doorway.
	// These cells are white on the planning spreadsheet
	@Test
	public void FourDoorDirections() {
		BoardCell room = board.getCellAt(11, 16);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.RIGHT, room.getDoorDirection());
		room = board.getCellAt(3, 6);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.DOWN, room.getDoorDirection());
		room = board.getCellAt(15, 14);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.LEFT, room.getDoorDirection());
		room = board.getCellAt(11, 19);
		assertTrue(room.isDoorway());
		assertEquals(DoorDirection.UP, room.getDoorDirection());
		// Test that room pieces that aren't doors know it
		room = board.getCellAt(15, 15);
		assertFalse(room.isDoorway());	
		// Test that walkways are not doors
		BoardCell cell = board.getCellAt(15, 13);
		assertFalse(cell.isDoorway());		

	}
	
	// Test that we have the correct number of doors
	@Test
	public void testNumberOfDoorways() 
	{
		int numDoors = 0;
		for (int row=0; row<board.getNumRows(); row++)
			for (int col=0; col<board.getNumColumns(); col++) {
				BoardCell cell = board.getCellAt(row, col);
				if (cell.isDoorway())
					numDoors++;
			}
		assertEquals(14, numDoors);
	}

	// Test a few room cells to ensure the room initial is correct.
	@Test
	public void testRoomInitials() {
		// Test first row
		assertEquals('O', board.getCellAt(0, 0).getInitial());
		assertEquals('W', board.getCellAt(0, 4).getInitial());
		assertEquals('P', board.getCellAt(0, 5).getInitial());
		assertEquals('T', board.getCellAt(0, 13).getInitial());
		assertEquals('B', board.getCellAt(0, 19).getInitial());
		
		// Test last row
		assertEquals('K', board.getCellAt(19, 0).getInitial());
		assertEquals('W', board.getCellAt(19, 5).getInitial());
		assertEquals('D', board.getCellAt(19, 6).getInitial());
		assertEquals('A', board.getCellAt(19, 14).getInitial());

		
		// Test last cell
		assertEquals('A', board.getCellAt(19, 19).getInitial());
		
		// Test the closet
		assertEquals('X', board.getCellAt(6,5).getInitial());
	}
	
	@Test
	public void testClosetSize() {
		int numTiles = 0;
		for (int row=0; row<board.getNumRows(); row++)
			for (int col=0; col<board.getNumColumns(); col++) {
				BoardCell cell = board.getCellAt(row, col);
				if (cell.getInitial() == 'X')
					numTiles++;
			}
		assertEquals(42, numTiles);
	}

}

