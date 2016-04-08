package blokus;
/**
 * StupidAgent is the agent we built to test Blokus
 * @author Avery Johnson
 * @author Leah Cole
 * @author Gil Eisbruch
 * @author Adrian Carpenter
 *
 */

public class StupidAgent implements Player {
	Controller controller;
	Board board;
	int color;


	public StupidAgent(Controller controller, int color) {
		this.controller=controller;
		this.board = controller.getBoard();
		this.color = color;
	}

	/**
	 * Takes the first piece in the hand of a computer player and places it in 
	 * the first legal spot
	 */
	public BlokusPiece chooseMove() {
		int d;
		BlokusPiece[] hand = controller.parent.pieces[color];
		//find any spot where agent can put any piece
		for(int i = 0;i<hand.length;i++){
			//each rotation
			if (!hand[i].isPlayed()) {
				for(int r = 1;r<5;r++){
					//try each flip
					for(int f = 0;f<2;f++){
						if(f == 0) d = -r;
						else d = r;
						//each board spot
						for(int x = 0;x<board.board.length;x++){
							for(int y = 0;y<board.board[0].length;y++){
								//each piece, each x,y coordinate
								int[][] pieceShape = controller.parent.flip(controller.parent.rotate(controller.parent.pieceShapes[i], d), d);
								//spots doesn't really equal pieceShape, just to make eclipse happy.
								int[][] spots = new int[pieceShape.length][pieceShape[0].length];
								for(int j = 0; j < pieceShape.length; j++){
									//x,y + spots[j] should be the spots on the board we are placing the piece.
									spots[j][0] = pieceShape[j][0] + x;
									spots[j][1] = pieceShape[j][1] + y;
								}
								if(controller.legalMove(spots, hand[i].getOrigColor())){
									hand[i].setDirection(d);
									int newX = board.getX() + 20 * x;
									int newY = board.getY() + 20 * y;
									hand[i].setX(newX);
									hand[i].setY(newY);
									return hand[i];
								}
							}
						}
					}
				}
			}
		}
		return null;
	}



	public int getColor() {
		return color;
	}



}
