package clueGame;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class HumanPlayer extends Player {
	
	public HumanPlayer(String name, Color color) throws FileNotFoundException, BadConfigFormatException {
		super(name, color, true);
		System.out.println("A human enters");
	}

	@Override
	protected BoardCell pickLocation(int r, int c, int pathLength) {
		return null;
	}

	@Override
	public Card[] createSuggestion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean testCardSuggestionChoice(int i, int j) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void setSeenPlayers(ArrayList<Card> seen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSeenWeapons(ArrayList<Card> seen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearPreviouslyVisitedRooms() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWillAccuse(boolean wa) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void makeAccusation() {
		// TODO Auto-generated method stub
		
	}
}
