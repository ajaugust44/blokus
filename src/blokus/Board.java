package blokus;
import processing.core.PApplet;



/**
 * Board class used in our Blokus game
 * @author Avery Johnson
 * @author Leah Cole
 * @author Gil Eisbruch
 * @author Adrian Carpenter
 * date: May 26th, 2014
 */


public class Board{
	int x, y;
	int height = 400, width = 400;
	PApplet parent;
	int[][] board = new int[20][20];
	int[] colors;
	int[][] simpleBoard = new int[20][20];
	
	public Board(GUI parent, int[] colors){
		this.x = parent.windowX/2 - width/2;
		this.y = parent.windowY/2 - 2*height/3 + 50;
		this.parent = parent;
		this.colors = colors;
		//Initialize each cell of Board.board to be boardcolor
		for (int y = 0; y<20; y++) {
			int[] row = new int[20];
			for (int x = 0; x<20; x++) {
				row[x] = colors[0];
			}
			board[y] = row;
		}
		//Initialize each cell of simpleBoard to be boardcolor
		for (int y = 0; y<20; y++) {
			int[] row = new int[20];
			for (int x = 0; x<20; x++) {
				row[x] = -1;
			}
			simpleBoard[y] = row;
		}
		
	}
	
	/**
	 * Board getter
	 * @return
	 */
	public int[][] getBoard(){
		return this.board;
	}
	
	/**
	 * Given an array of spots on the board and a color, set the spots
	 * on the board to that color
	 * @param spots
	 * @param color
	 */
	public void setSpots(int[][] spots, int color){
		int simpleColor = -1;
		for(int c=0; c<colors.length; c++){
			if(colors[c]==color){
				simpleColor = c-1;
			}
		}
		for(int i = 0;i<spots.length;i++){
			int thisX = spots[i][0];
			int thisY = spots[i][1];
			this.board[thisY][thisX] = color;
			this.simpleBoard[thisY][thisX] = simpleColor;
		}
	}
	
	/**
	 * Given an x coordinate in pixels and a y coordinate in pixels, get the
	 * location of this x and y in terms of the board
	 * @param x
	 * @param y
	 * @return
	 */
	public int[] getBoardLocation(int x, int y) {
		int col = (x - this.x)/20;
		int row = (y - this.y)/20;
		int[] result = {col, row};
		return result;
	}
	
	/**
	 * Given x and y coordinates in terms of the board, return where in the 
	 * window this is in terms of pixels
	 * @param col
	 * @param row
	 * @return
	 */
	public int[] getWindowLocation(int col, int row) {
		int x = (col * 20) + this.x;
		int y = (row * 20) + this.y;
		int[] result = {x, y};
		return result;
	}
	
	/**
	 * Figures out the top left corner of the board
	 * @param r
	 * @param c
	 * @return
	 */
	public int[] getCoordinates(int r, int c) {
		int[] coords = new int[2];
		coords[0] = (r * 20) + this.x;
		coords[1] = (c * 20) + this.y;
		return coords;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public int[] getColors() {
		return this.colors;
	}

	public int[][] getSimpleBoard() {
		return this.simpleBoard;
	}

	public String toString() {
		String boardString = "";
		for(int row = 0; row<20; row++) {
			for(int col = 0; col < 20; col++){
				boardString = boardString + simpleBoard[row][col] + "\t";
			}
			boardString = boardString + "\n";
		}
		return boardString;
	}

}
