// Author: George Karachepone and Devin Gao

package experiment;

import clueGame.DoorDirection;

public class Boardcell {
	private int row;
	private int column;
	private char identity;
	private DoorDirection door;
	
	public Boardcell(int c, int r, char cellLegend) {
		column = c;
		row = r;
		identity = cellLegend;
	}
	
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	
	public char getIdentity() {
		return identity;
	}
	
	public void setIdentity(char identity) {
		this.identity = identity;
	}
	

	public void setDoor(DoorDirection door) {
		this.door = door;
	}

	public DoorDirection getDoorDirection() {
		// TODO Auto-generated method stub
		return door;
	}
	
	
	
}
