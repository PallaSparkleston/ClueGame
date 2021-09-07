// Author: George Karachepone and Devin Gao

package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import experiment.Boardcell;
import experiment.IntBoard;

class IntBoardTests {
	IntBoard board = new IntBoard();
	


	@Test
	public void testAdjacency0_0() {
		
		board.calcAdjList(board.getGrid(), 0, 0);
		HashSet<Boardcell> testList = board.getAdjList();
		assertTrue(testList.contains(board.getCell(1,0)));
		assertTrue(testList.contains(board.getCell(0,1)));
		assertEquals(2, testList.size());
	}
	
	@Test
	public void testAdjacency3_3() {
		board.calcAdjList(board.getGrid(), 3, 3);
		HashSet<Boardcell> testList = board.getAdjList();
		assertTrue(testList.contains(board.getCell(3,2)));
		assertTrue(testList.contains(board.getCell(2,3)));
		assertEquals(2, testList.size());
	}
	
	@Test
	public void testAdjacency1_3() {
		board.calcAdjList(board.getGrid(), 1, 3);
		HashSet<Boardcell> testList = board.getAdjList();
		assertTrue(testList.contains(board.getCell(0,3)));
		assertTrue(testList.contains(board.getCell(2,3)));
		assertTrue(testList.contains(board.getCell(1,2)));
		assertEquals(3, testList.size());
	}
	
	@Test
	public void testAdjacency3_0() {
		board.calcAdjList(board.getGrid(), 3, 0);
		HashSet<Boardcell> testList = board.getAdjList();
		assertTrue(testList.contains(board.getCell(2,0)));
		assertTrue(testList.contains(board.getCell(3,1)));
		assertEquals(2, testList.size());
	}
	
	@Test
	public void testAdjacency1_1() {
		board.calcAdjList(board.getGrid(), 1, 1);
		HashSet<Boardcell> testList = board.getAdjList();
		assertTrue(testList.contains(board.getCell(0,1)));
		assertTrue(testList.contains(board.getCell(1,0)));
		assertTrue(testList.contains(board.getCell(2,1)));
		assertTrue(testList.contains(board.getCell(1,2)));
		assertEquals(4, testList.size());
	}
	
	@Test
	public void testAdjacency2_2() {
		board.calcAdjList(board.getGrid(), 2, 2);
		HashSet<Boardcell> testList = board.getAdjList();
		assertTrue(testList.contains(board.getCell(1,2)));
		assertTrue(testList.contains(board.getCell(2,1)));
		assertTrue(testList.contains(board.getCell(3,2)));
		assertTrue(testList.contains(board.getCell(2,3)));
		assertEquals(4, testList.size());
	}
	
	@Test
	public void testTargets0_0_3()
	{
		Boardcell cell = board.getCell(0, 0);
		board.clearLists();
		board.calcTargets(cell, 3);
		HashSet<Boardcell> targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(1, 0)));
	}
	
	@Test
	public void testTargets3_3_3()
	{
		Boardcell cell = board.getCell(3, 3);
		board.clearLists();
		board.calcTargets(cell, 3);
		HashSet<Boardcell> targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(3, 2)));
		assertTrue(targets.contains(board.getCell(2, 3)));
	}
	
	@Test
	public void testTargets1_1_3()
	{
		Boardcell cell = board.getCell(1, 1);
		board.clearLists();
		board.calcTargets(cell, 3);
		HashSet<Boardcell> targets = board.getTargets();
		assertEquals(8, targets.size());
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(1, 0)));
		assertTrue(targets.contains(board.getCell(2, 3)));
		assertTrue(targets.contains(board.getCell(3, 2)));
	}
	
	@Test
	public void testTargets1_3_2()
	{
		Boardcell cell = board.getCell(1, 3);
		board.clearLists();
		board.calcTargets(cell, 2);
		HashSet<Boardcell> targets = board.getTargets();
		assertEquals(4, targets.size());
		assertTrue(targets.contains(board.getCell(3, 3)));
		assertTrue(targets.contains(board.getCell(1, 1)));
		assertTrue(targets.contains(board.getCell(0, 2)));
		assertTrue(targets.contains(board.getCell(2, 2)));
	}
	
	@Test
	public void testTargets2_2_2()
	{
		Boardcell cell = board.getCell(2, 2);
		board.clearLists();
		board.calcTargets(cell, 2);
		HashSet<Boardcell> targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(2, 0)));
		assertTrue(targets.contains(board.getCell(3, 1)));
		assertTrue(targets.contains(board.getCell(0, 2)));
		assertTrue(targets.contains(board.getCell(1, 3)));
		assertTrue(targets.contains(board.getCell(1, 1)));
		assertTrue(targets.contains(board.getCell(3, 3)));
	}
	
	@Test
	public void testTargets3_0_3()
	{
		Boardcell cell = board.getCell(3, 0);
		board.clearLists();
		board.calcTargets(cell, 3);
		HashSet<Boardcell> targets = board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCell(0, 0)));
		assertTrue(targets.contains(board.getCell(3, 3)));
		assertTrue(targets.contains(board.getCell(1, 1)));
		assertTrue(targets.contains(board.getCell(2, 2)));
		assertTrue(targets.contains(board.getCell(3, 1)));
		assertTrue(targets.contains(board.getCell(2, 0)));
	}
	
	

}
