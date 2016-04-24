package blokus;
/**
 * Interface used with agents for Blokus
 * @author Avery Johnson
 * @author Leah Cole
 * @author Gil Eisbruch
 * @author Adrian Carpenter *
 */
interface Player {
	
	int score = 0;
	int color = 0;
	
	/**
	 * The method called from controller when it is this AI's turn.
	 * Returns null if no possible moves. Otherwise it should
	 * @return BlokusPiece with x and y set to the desired location
	 * (pixel coordinates, not board coordinates)
	 */
	BlokusPiece chooseMove();

	int getColor();

	
}
