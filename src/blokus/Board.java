package blokus;
import processing.core.PApplet;

import static blokus.BlokusUtils.println;
import static blokus.GUI.BLOCKSIZE;


/**
 * Board class used in our Blokus game
 * @author Avery Johnson
 * @author Leah Cole
 * @author Gil Eisbruch
 * @author Adrian Carpenter
 * date: May 26th, 2014
 */


class Board{
    final static int numBlocksWide = 20, numBlocksHigh = 20;
	int x, y;
	private int height = numBlocksHigh * BLOCKSIZE, width = numBlocksWide * BLOCKSIZE;
	PApplet parent;
	int[][] board = new int[numBlocksHigh][numBlocksWide];
	private int[] colors;
	int[][] simpleBoard = new int[numBlocksHigh][numBlocksWide];
	
	Board(GUI parent, int[] colors){
		this.x = parent.windowX/2 - width/2;
		this.y = parent.windowY/2 - 2*height/3 + 50;
		this.parent = parent;
		this.colors = colors;
		//Initialize each cell of Board.board to be boardcolor
		for (int y = 0; y<numBlocksHigh; y++) {
			int[] row = new int[numBlocksWide];
			for (int x = 0; x<numBlocksWide; x++) {
				row[x] = colors[0];
			}
			board[y] = row;
		}
		//Initialize each cell of simpleBoard to be boardcolor
		for (int y = 0; y<numBlocksHigh; y++) {
			int[] row = new int[numBlocksWide];
			for (int x = 0; x<numBlocksWide; x++) {
				row[x] = -1;
			}
			simpleBoard[y] = row;
		}
		
	}


	/**
	 * This can be called to draw the initial empty board. Loops through and
	 * fills each square with its proper color
	 */
    void showBoard(){
		parent.pushStyle();
		parent.strokeWeight(1);
		parent.stroke(150);
		for (int y = 0; y<numBlocksHigh; y++) {
			for (int x = 0; x<numBlocksWide; x++) {
				int[] cor = getCoordinates(x,y);
				parent.fill(board[y][x]); //fill each square with the color at that point in the array
				parent.rect(cor[0],cor[1], BLOCKSIZE,BLOCKSIZE);
			}
		}
		parent.popStyle();
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
    int[] getBoardLocation(int x, int y) {
        println("xPix: " + x + ", yPix: " + y);
        println("boardX: " +this.x + " boardY: " + this.y);
		int col = (x - this.x)/BLOCKSIZE;
		int row = (y - this.y)/BLOCKSIZE;
        println("Moving to board location: " + col +  ", " + row);
        return new int[]{col, row};
	}
	
	/**
	 * Given x and y coordinates in terms of the board, return where in the 
	 * window this is in terms of pixels
	 * @param col
	 * @param row
	 * @return
	 */
	public int[] getWindowLocation(int col, int row) {
		int x = (col * BLOCKSIZE) + this.x;
		int y = (row * BLOCKSIZE) + this.y;
        return new int[]{x, y};
	}
	
	/**
	 * Figures out the top left corner of the board
	 * @param r
	 * @param c
	 * @return
	 */
	public int[] getCoordinates(int r, int c) {
		int[] coords = new int[2];
		coords[0] = (r * BLOCKSIZE) + this.x;
		coords[1] = (c * BLOCKSIZE) + this.y;
		return coords;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	int getHeight() {
		return height;
	}

	int getWidth() {
		return width;
	}

	int[] getColors() {
		return this.colors;
	}

	int[][] getSimpleBoard() {
		return this.simpleBoard;
	}

	public String toString() {
		String boardString = "";
		for(int row = 0; row<numBlocksHigh; row++) {
			for(int col = 0; col < numBlocksWide; col++){
				boardString = boardString + simpleBoard[row][col] + "\t";
			}
			boardString = boardString + "\n";
		}
		return boardString;
	}

}
