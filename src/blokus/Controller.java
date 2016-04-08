package blokus;

/**
 * Controller class for Blokus
 * @author Avery Johnson
 * @author Leah Cole
 * @author Gil Eisbruch
 * @author Adrian Carpenter
 *
 */
public class Controller{

	GUI parent;
	Board board;
	
	public final int[] BLUE = {0x3c3ca0, 0x5a5abe, 0};
	public final int[] YELLOW = {0xffff28, 0xffff46, 1};
	public final int[] RED = {0x963232, 0xB45050, 2};
	public final int[] GREEN = {0x329632, 0x50B450, 3};
	public final int BOARDCOLOR = 0xC8C8C8;


	public final int BLOCKSIZE = 20;

	int turn = 0;
	/*This boolean indicates whether or not a player has passed on his or her 
	 * turn(can no longer make a legal move)
	 */
	boolean[] passed = {true, true, true, true};
	BlokusPiece[] lastMove = {null, null, null, null};
	
	int[] boardColors;

	
	Player[] players = {null, null, null, null};
	
	BlokusPiece[][] pieces;
	
	public Controller(GUI parent){
		this.parent = parent;
		this.board = parent.getBoard();
		this.boardColors = board.getColors();
		this.pieces = parent.getPieces();
	}

	/**
	 * This method resets the variables that should be reset when a new game
	 * is started. 
	 */
	public void newGame(boolean[] human){
		GUI gui = this.parent;
		//Reset passed to all falses
		boolean newPassed[] = {false, false, false, false};
		this.passed = newPassed;
		//Reset turn counter to 0
		this.turn = 0;
		//Reset the board in our gui
		board = gui.getBoard();
		
		//Reset the players
		for(int i = 0; i<4; i++) {
			if(!human[i]) {
				players[i] = new LAgent2(this, i);
			} else if(!human[i]) {
				players[i] = new AGent(this, i);
			}
			else {
				players[i] = null;
			}
		}
		
		pieces = parent.getPieces();
	}

	/**
	 * Checks to see if players can make any more available moves.
	 * @return true if all four players have passed
	 */
	public boolean gameOver() {
		for(int i = 0; i< passed.length; i++) {
			if (!passed[i]) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * This method makes each player move if they have a possible legal move.
	 * @param Player p
	 * @return true if a valid move has been made
	 */
	public boolean move(Player p) {
		if (turn != p.getColor()) {
			System.out.println("Incorrect no no no");
			return false;
		}
		boolean valid = false;
		BlokusPiece move;
		while(!valid) {
			move = p.chooseMove();
			if (move == null) {
				turn ++;
				turn = turn % 4;
				passed[p.getColor()] = true;
				lastMove[p.getColor()] = null;
				return false;
			}
			valid = makeMove(move);
		}
		return true;
	}

	/**
	 * Calculates the scores of each of the players based on what is left in
	 * their hands
	 * @return
	 */
	public int[] calcScore(){
		int[] scores = {0,0,0,0};
		for(int i = 0; i<4;i++){
			BlokusPiece[] hand = parent.pieces[i];
			for(int p = 0;p<hand.length;p++){
				BlokusPiece thisPiece = hand[p];
				if (!thisPiece.played){
					scores[i] -= thisPiece.getSize();
				}
			}
		}
		return scores;
	}

	/**
	 * This method takes in the spots that a certain piece is located in
	 * and the color of said piece. 
	 * @param spots
	 * @param color
	 * @return
	 */
	public boolean legalMove(int[][] spots, int color){
		int[][] brd = this.board.getBoard();
		boolean cornerHit = false;
		boolean sideHit = false;
		boolean overlap = false;
		boolean firstCorner = false;
		for(int i = 0; i < spots.length;i++){
			int[] currentSpot = spots[i];
			int x = currentSpot[0];
			int y = currentSpot[1];

			//Part of the shape is out of bounds
			if(x<0 || x>19 || y<0 || y>19) {
				return false;
			}
			//if adjacent spot occupied by same color, then illegal move!
			if(brd[y][x] != this.boardColors[0]){
				overlap = true;
				return false;
			}
			//Various cases for if a piece is on the edge of the board
			if (y>0 && x>0){
				if(y<19 && x<19){
					//everything is in bounds.
					//check for adjacency
					if(brd[y+1][x] == color || brd[y-1][x] == color || brd[y][x+1] == color || brd[y][x-1] == color){
						sideHit = true;
						return false;
					}
					//check for corner of piece.
					if(brd[y+1][x+1] == color || brd[y+1][x-1] == color || brd[y-1][x+1] == color || brd[y-1][x-1] == color){
						cornerHit = true;
					}
				}else if(y<19){
					//x is 19, y is in bounds.
					//check for adjacency.
					if(brd[y+1][x] == color || brd[y-1][x] == color || brd[y][x-1] == color){
						sideHit = true;
						return false;
					}
					//check for corner of piece.
					if(brd[y+1][x-1] == color || brd[y-1][x-1] == color){
						cornerHit = true;
					}
				}else if(x<19){
					//y is 19, x is in bounds.
					//check for adjacency
					if(brd[y-1][x] == color || brd[y][x+1] == color || brd[y][x-1] == color){
						sideHit = true;
						return false;
					}
					//check for corner of piece.
					if(brd[y-1][x+1] == color || brd[y-1][x-1] == color){
						cornerHit = true;
					}

				}else{
					//y and x are both 19
					//check for adjacency
					firstCorner = true;
					if(brd[y-1][x] == color || brd[y][x-1] == color){
						sideHit = true;
						return false;
					}
					//check for corner of piece.
					if(brd[y-1][x-1] == color){
						cornerHit = true;
					}
				}
			}else if(y>0){
				//x is 0
				if(y<19){
					//check for adjacency
					if(brd[y+1][x] == color || brd[y-1][x] == color || brd[y][x+1] == color){
						sideHit = true;
						return false;
					}
					//check for corner of piece.
					if(brd[y+1][x+1] == color || brd[y-1][x+1] == color){
						cornerHit = true;
					}
				}else{
					//19,0
					firstCorner = true;
				}


			}else if(x>0){
				//y is 0. x is either 19, or in bounds.
				if(x<19){
					//check for adjacency
					if(brd[y+1][x] == color || brd[y][x+1] == color || brd[y][x-1] == color){
						sideHit = true;
						return false;
					}
					//check for corner of piece.
					if(brd[y+1][x+1] == color || brd[y+1][x-1] == color){
						cornerHit = true;
					}
				}else{
					//0,19
					firstCorner = true;
				}
			}else{
				//0,0
				firstCorner = true;
			}
		}
		if((cornerHit || firstCorner) && !sideHit && !overlap){

			return true;
		}else{
			return false;
		}

	}
	
	/**
	 * Given a blokus piece belonging to a certain player, make a move
	 * @param b
	 * @return
	 */
	public boolean makeMove(BlokusPiece b) {
		System.out.println(b.getColorID() + ", " + turn);
		//If it's not the correct players turn, don't make the move
		if (turn != b.getColorID()) {
			System.err.println("NOT YOUR TURN");
			return false;
		}
		boolean good;
		int firstbX = b.getX();
		int firstbY = b.getY();
		//at this point, firstbX, firstbY are in the hundreds
		int[] bxy = board.getBoardLocation(firstbX,firstbY);
		int bX = bxy[0];
		int bY = bxy[1];
		//now, bX and bY should be 0-19 values.
		if (bX >= 0 && bX < 20 && bY >= 0 && bY < 20) {
			//newLoc is the beginning location of [0,0] of piece shape.
			int[] newLoc = {bX, bY};
			int[][] pieceShape = parent.flip(parent.rotate(parent.getShape(b.getID()), b.getDirection()), b.getDirection());
			//spots doesn't really equal pieceShape, just to make eclipse happy.
			int[][] spots = new int[pieceShape.length][pieceShape[0].length];

			for(int j = 0; j < pieceShape.length; j++){
				//newLoc + spots[j] should be the spot on the board we are placing the piece.
				spots[j][0] = pieceShape[j][0] + newLoc[0];
				spots[j][1] = pieceShape[j][1] + newLoc[1];
			}
			int pieceColor = b.getOrigColor();
			good = this.legalMove(spots, pieceColor);
			//If the move is legal, make it
			if(good){
				System.out.println("GOOD MOVE");
				int[] newPosition = board.getWindowLocation(newLoc[0], newLoc[1]);
				b.setX(newPosition[0]);
				b.setY(newPosition[1]);
				b.setClickable(false); //We want to make it so the piece is not clickable anymore
				//This sets the board spaces to the appropriate color so that they can be used in other calculations
				board.setSpots(spots,pieceColor);
				b.setPlayed(true);
				passed[b.getColorID()] = false;
				this.turn ++;
				this.turn = this.turn % 4;
				this.lastMove[b.getColorID()] = b;
				return true;
			}else{
				System.err.println("SUCKY MOVE");
				return false;
			}
			//return true;
		} else {
			System.err.println("Cannot make move to board coordinates: " + bX + ", " + bY);
			return false;
			//return false;
		}
	}

	public int getRealColor(int i) {
		return parent.boardColors[i+1];
	}
	
	public void pass() {
		passed[turn] = true;
		turn ++;
		turn = turn % 4;
	}
	
	public Board getBoard(){
		return this.board;
	}

	public int getTurn() {
		return this.turn;
	}

	public Player[] getPlayers() {
//	public LAgent2[] getPlayers() {
		return this.players;
	}


}

