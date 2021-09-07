// Author: George Karachepone and Devin Gao

package experiment;
import java.util.HashMap;
import java.util.HashSet;


public class IntBoard {
	HashSet<Boardcell> adjSet = new HashSet<>();
	HashSet<Boardcell> targetList = new HashSet<>();
	HashSet<Boardcell> visited = new HashSet<>();
	static final int MAX_ROW = 3;
	static final int MAX_COLUMN = 3;
	Boardcell[][] currentCell = new Boardcell[MAX_COLUMN+1][MAX_ROW+1];
	HashMap<Boardcell, HashSet<Boardcell>> adjMap = new HashMap<>();

	// Calculates the map of adjacencies of all the board cells
	public void calcAdjacencies() {
		adjMap.clear();
		
		for(int i = 0; i <= MAX_ROW; i++) {
			for(int j = 0; j <= MAX_COLUMN; j++) {
				calcAdjList(currentCell, j, i);
			}
		}
		
	}
	
	// Calculates one adjacency set for a single cell
	public void calcAdjList(Boardcell[][] grid, int column, int row) {
		adjSet = new HashSet<>();
		if(grid[column][row].getColumn() + 1 <= MAX_COLUMN) {
			adjSet.add(grid[column+1][row]);
		}
		if(grid[column][row].getColumn() - 1 >= 0) {
			adjSet.add(grid[column-1][row]);
		}
		if(grid[column][row].getRow() + 1 <= MAX_ROW) {
			adjSet.add(grid[column][row+1]);
		}
		if(grid[column][row].getRow() - 1 >= 0) {
			adjSet.add(grid[column][row-1]);
		}
		adjMap.put(grid[column][row], adjSet);
	}
	
	// Calculates all the targets for a single cell
	public HashSet<Boardcell> calcTargets(Boardcell startCell, int pathLength) {
		visited.add(startCell);
		for(Boardcell adjCell: adjMap.get(startCell)) {
			if(visited.contains(adjCell)) {
				continue;
			}
			visited.add(adjCell);
			if(pathLength ==1) {
				targetList.add(adjCell);
			}
			else {
				calcTargets(adjCell,pathLength-1);
			}
			visited.remove(adjCell);
			
		}
//		if (pathLength == 0) {
//			targetList.add(startCell);
//			return targetList;
//		} else {
//			for (BoardCell nextCell:adjMap.get(startCell)) {
//				if (visited.contains(nextCell)) { continue; }
//				calcTargets(nextCell, pathLength - 1);
//			}
//		}
//		
//		visited.remove(startCell);
		return targetList;
		
	}

	public HashSet<Boardcell> getAdjList() {
		return adjSet;
	}

	public void setAdjList(HashSet<Boardcell> adjList) {
		this.targetList = adjList;
	}

	// Constructor
	public IntBoard() {
		super();
		for(int i = 0; i <= MAX_ROW; i++) {
			for(int j = 0; j <= MAX_COLUMN; j++) {
			currentCell[i][j] = new Boardcell(i, j, 'W');
			}
			
			
		}
		
		calcAdjacencies();
		calcTargets(currentCell[0][0], 3);
	}

	public void clearLists() {
		visited.clear();
		targetList.clear();
	}
	

	public HashSet<Boardcell> getTargets() {
		return targetList;
	}
	
	public Boardcell getCell(int row, int column) {
		return currentCell[row][column];
	}
	
	public Boardcell[][] getGrid(){
		return currentCell;
	}

}
