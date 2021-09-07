package clueGame;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class BoardPanel extends JPanel implements MouseListener {
	private int x, y;
	private Board board = Board.getInstance();
	private Player human;
	private ArrayList<Rectangle> targetRects= new ArrayList<>();
	private boolean hasMoved = false;

	// For getting guesses
	private String guessString;

	// variable used for singleton pattern
	private static BoardPanel theInstance = new BoardPanel();
	// this method returns the only BoardPanel
	public static BoardPanel getInstance() {
		if (theInstance == null) {
			theInstance = new BoardPanel();
		}
		return theInstance;
	}


	private BoardPanel() {
		// Make the Cards
		setLayout(new GridLayout(1,2));
		loadHuman();


		// Set up variables for reading the board
		x = 0;
		y = 0;

		addMouseListener(this);
	}

	public void loadHuman() {
		for (Player person: board.getPlayers()) {
			if (person.isHuman) {
				human = person;
			}
		}
	}

	//	public void translate(int dx, int dy) {
	//	  x += dx;
	//	  y += dy;
	//	  // Must include this to see changes
	//	  repaint();
	//	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		for (int i=0; i < board.getNumRows(); i++) {
			for (int j=0; j < board.getNumColumns(); j++) {
				BoardCell currentCell = board.getCellAt(j, i);


				if(currentCell.getInitial() == 'W') {
					//Sets a black outline and fills the inside with yellow if cell is a walkway
					g.setColor(Color.BLACK);
					g.drawRect(i*40, j*40, 40,40);
					g.setColor(Color.pink);
					g.fillRect((i*40)+1, (j*40)+1, 38, 38);

					//Highlight squares blue if it is the human's turn
					if (board.getCurrentPlayerTurn() == 0 && !hasMoved) {
						board.clearLists();
						board.calcTargets(board.getPlayers().get(0).getPlayerStart().getRow(), board.getPlayers().get(0).getPlayerStart().getColumn(), board.getDieRoll());
						g.setColor(Color.CYAN);
						Set<BoardCell> targets = board.getTargets();
						targetRects.clear();
						for(BoardCell b: targets) {
							g.fillRect(b.getColumn()*40+1, b.getRow()*40+1, 39, 39);
							Rectangle rect = new Rectangle(b.getColumn()*40+1, b.getRow()*40+1, 39, 39);
							targetRects.add(rect);

						}
						//while(itr.hasNext()) {
						//System.out.println(itr.next().getColumn());
						//							g.setColor(Color.BLACK);
						//							g.drawRect(itr.next().getColumn()*40, itr.next().getRow()*40, 40,40);

						//							System.out.println(itr.next());
						//							
						//							System.out.println("Column" + itr.next().getColumn());
						//							System.out.println("Row" + itr.next().getRow());
						//g.setColor(Color.CYAN);
						//g.fillRect(itr.next().getColumn()*40+1, itr.next().getRow()*40+1, 39, 39);
						//}
						//repaint();
						//g.drawRect(board)
					}

					//Draw each player
					for (Player person: board.getPlayers()) {
						boolean isCovering = false;


						for (int k = person.getPlayerIndex()-1; k >= 0; k--) {
							
							if (board.getCellAt(person.getRow(), person.getColumn()) == board.getCellAt(board.getPlayers().get(k).getRow(), board.getPlayers().get(k).getColumn())) {
								isCovering = true;
								break;
							}
						}

						if (!isCovering) {
							g.setColor(person.getPlayerColor());
							g.fillOval(person.getPlayerStart().getColumn()*40, person.getPlayerStart().getRow()*40, 38, 38);
						} else {
							g.setColor(person.getPlayerColor());
							g.fillOval(person.getPlayerStart().getColumn()*40, person.getPlayerStart().getRow()*40, 19, 19);
						}

					}

					if (board.currentPlayerTurn != -1) {
						Player currentPlayer = board.getPlayers().get(board.currentPlayerTurn);
						if (board.getCellAt(currentPlayer.getRow(), currentPlayer.getColumn()).getDoorDirection() != DoorDirection.NONE) {
							Card[] playerGuess = currentPlayer.createSuggestion();
							if (playerGuess != null) {
								Card disprove = board.handleSuggestion(currentPlayer, playerGuess);
								board.setCurrentGuess(playerGuess);
								//								System.out.println(playerGuess[0]);
								//								System.out.println(playerGuess[1]);
								//								System.out.println(playerGuess[2]);

								// Turn playerGuess into a string
								String playerGuessString = "";
								for (Card element:playerGuess) {
									playerGuessString = playerGuessString + element.getCardName();

									// If its not the last element, add a comma
									if (element != playerGuess[2]) {
										playerGuessString += ", ";
									}
								}


								PlayerActionsPanel playerActions = PlayerActionsPanel.getInstance();
								playerActions.guess.setText(playerGuessString);
								if (disprove != null) {
									playerActions.disprove.setText(disprove.getCardName());
								} else {
									playerActions.disprove.setText("no new clue");
								}
							}

						} else {
							PlayerActionsPanel playerActions = PlayerActionsPanel.getInstance();
							playerActions.guess.setText("");
							playerActions.disprove.setText("");
						}
					}
				}
				if(currentCell.getInitial() != 'W') {
					//If not a walkway, then fill the square with gray
					g.setColor(Color.GRAY);
					g.fillRect((i*40)+1, (j*40)+1, 40, 40);
					//writes the names of the rooms on certain blocks
					g.setColor(Color.LIGHT_GRAY);
					g.drawString("Obersvatory", 1*40, 7*40);
					g.drawString("Piano Room", 7*40, 2*40);
					g.drawString("Theater", 14*40, 2*40);
					g.drawString("Bedroom", 18*40, 2*40);
					g.drawString("Bathroom", 18*40, 12*40);
					g.drawString("Library", 14*40, 9*40);
					g.drawString("Arcade", 16*40, 17*40);
					g.drawString("Dining Room", 7*40, 17*40);
					g.drawString("Kitchen", 1*40, 17*40);
					//if the door direction is down, then make a blue rectangle on the bottom of the block
					if(currentCell.getDoorDirection() == DoorDirection.DOWN) {
						g.setColor(Color.BLUE);
						g.fillRect((i*40), (j*40)+34, 39, 39);
					}
					//if door direction is up, then make blue rectangle on the top
					else if(currentCell.getDoorDirection() == DoorDirection.UP) {
						g.setColor(Color.BLUE);
						g.fillRect((i*40), (j*40), 40, 6);
					}
					//if door direction is left, then make blue rectangle on the left side
					else if(currentCell.getDoorDirection() == DoorDirection.LEFT) {
						g.setColor(Color.BLUE);
						g.fillRect((i*40), (j*40), 6, 40);
					}
					//if door direction is right, then make blue rectangle on the right side
					else if(currentCell.getDoorDirection() == DoorDirection.RIGHT) {
						g.setColor(Color.BLUE);
						g.fillRect((i*40)+34, (j*40), 39, 39);
					}
				}

			}	  
		}
	}

	//	public ArrayList<Rectangle> getTargetRects() {
	//		return targetRects;
	//	}

	public String getGuessString() {
		return guessString;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		JFrame frame = new JFrame();
		boolean isValidInput = false;

		if (hasMoved) {
			JOptionPane.showMessageDialog(frame, "You Have Already Moved", "Invalid Input", JOptionPane.INFORMATION_MESSAGE);
			return;
		} else {

			for (Rectangle rect: targetRects) {

				if (containsClick(rect, e.getX(), e.getY()) && !hasMoved) {
					board.movePlayer(board.currentPlayerTurn, ((int)rect.getY()-1)/40, ((int)rect.getX()-1)/40);
					repaint();
					isValidInput = true;
					hasMoved = true;
					break;
				}
			}
		}


		if (!isValidInput) {
			if (board.getCurrentPlayerTurn() == 0) {
				JOptionPane.showMessageDialog(frame, "Please Click a Highlighted (Cyan) Tile", "Invalid Input", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(frame, "It is Not Your Turn", "Invalid Input", JOptionPane.INFORMATION_MESSAGE);

			}
		}

		Player currentPlayer = board.getPlayers().get(board.currentPlayerTurn);

		if (board.getCellAt(currentPlayer.getRow(), currentPlayer.getColumn()).getDoorDirection() != DoorDirection.NONE && hasMoved) {
			try {
				SuggestionPrompt suggestion = new SuggestionPrompt();
				suggestion.showSuggestionPrompt();

			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (BadConfigFormatException e1) {
				e1.printStackTrace();
			}

		}
	}		

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public boolean containsClick(Rectangle rect, int mouseX, int mouseY) {
		if (rect.contains(new Point (mouseX, mouseY))) {
			return true;
		} else {
			return false;
		}
	}

	public void setHasMoved(boolean hm) {
		hasMoved = hm;
	}

	public boolean getHasMoved() {
		return hasMoved;
	}

}

