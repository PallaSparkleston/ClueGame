package clueGame;

public class BoardCell {
	private int row;
	private int column;
	private char initial;
	private DoorDirection door;

	public BoardCell(int c, int r, char i) {
		column = c;
		row = r;
		initial = i;
		door = DoorDirection.NONE;
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

	public char getInitial() {
		return initial;
	}

	public void setInitial(char initial) {
		this.initial = initial;
	}

	public boolean isDoorway() {
		if (door == DoorDirection.NONE) {
			return false;
		}

		return true;
	}

	public boolean isWalkway(BoardCell previous) {
		// If this is a door-less room tile, never put adjacencies in the adjMatrix
		if (previous.initial !='W' && previous.getDoorDirection() == DoorDirection.NONE) {
			return false;
		}

		// If the player is in a door, they can only exit with the door direction
		if (previous.getDoorDirection() != DoorDirection.NONE) {
			switch (previous.getDoorDirection()) {
			case RIGHT:
				if (column == previous.getColumn() + 1) {
					return true;
				}
				break;
			case LEFT:
				if (column == previous.getColumn() - 1) {
					return true;
				}
				break;
			case UP:
				if (row == previous.getRow() - 1) {
					return true;
				}
				break;
			case DOWN:
				if (row == previous.getRow() + 1) {
					return true;
				}
				break;
			}
			return false;
		}

		// If it is a walkway, then return true (as long as the player is not in a door), otherwise, check if it is a doorway, and return true if the
		// player is in the correct position to enter the door
		if (initial == 'W') {
			return true;
		} else {
			switch (door) {
			case RIGHT:
				if (previous.getColumn() == column + 1) {
					return true;
				}
				break;
			case LEFT:
				if (previous.getColumn() == column - 1) {
					return true;
				}
				break;
			case UP:
				if (previous.getRow() == row - 1) {
					return true;
				}
				break;
			case DOWN:
				if (previous.getRow() == row + 1) {
					return true;
				}
				break;
			case NONE:
				return false;
			}
		}



		return false;
	}



	public void setDoor(DoorDirection door) {
		this.door = door;
	}

	public DoorDirection getDoorDirection() {
		// TODO Auto-generated method stub
		return door;
	}




}
