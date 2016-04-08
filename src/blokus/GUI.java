package blokus;
import java.awt.event.KeyEvent;

import processing.core.PApplet;

public class GUI extends PApplet{
	/**
	 * BLOKUS
	 * @author Avery Johnson
	 * @author Leah Cole
	 * @author Gil Eisbruch
	 * @author Adrian Carpenter
	 * date: May 26th, 2014
	 */
	private static final long serialVersionUID = 1L;
	Controller controller;
	int windowX, windowY; //size of window
	int bkgrdColor=color(230, 230, 235);

	int[] buttonColors = {180, 200, 100};

	//Make lists of buttons so we can iterate through them later
	Button newGame, quit, howToPlay;
	Button[] buttons;

	GameOver gameover = new GameOver();
	HowToPlay htp = new HowToPlay();
	ExitView exitView = new ExitView();
	NewGame newGameSetup = new NewGame();

	WindowBox[] windows = {gameover, htp, exitView, newGameSetup};


	//From Avery
	public final int[] BLUE = {color(60,60,160), color(90,90,190), 0};
	public final int[] YELLOW = {color(225,225,40), color(255,255,70), 1};
	public final int[] RED = {color(150, 50, 50), color(180, 80, 80), 2};
	public final int[] GREEN = {color(50,150,50), color(80,180,80), 3};

	public final int BOARDCOLOR = color(200);

	public final int BLOCKSIZE = 20;

	//These are the official names of blokus pieces[0]
	public final String[] pieceNames = {"i", "l", "u", "z", "t", "x", "w", "v", "f", "p", "y", "n",
			"shorti", "shortt", "shortl", "shortz", "square", "crooked3", "3", "2", "1"};

	//Relative coordinates of every piece
	public final int[][][] pieceShapes = {
			{{0,0},{0,1},{0,2},{0,3},{0,4}}, // "i"
			{{0,0},{0,1},{0,2},{0,3},{1,3}}, //l
			{{0,0},{0,1},{1,1},{2,1},{2,0}}, //u
			{{0,0},{1,0},{1,1},{1,2},{2,2}}, //z
			{{0,0},{1,0},{2,0},{1,1},{1,2}}, //t
			{{0,1},{1,0},{1,1},{2,1},{1,2}}, //x
			{{0,0},{1,0},{1,1},{2,1},{2,2}}, //w
			{{0,0},{1,0},{2,0},{2,1},{2,2}}, //v
			{{1,0},{2,0},{0,1},{1,1},{1,2}}, //f
			{{0,0},{0,1},{1,0},{1,1},{0,2}}, //p
			{{0,0},{0,1},{1,1},{0,2},{0,3}}, //y
			{{0,0},{1,0},{1,1},{2,1},{3,1}}, //n
			{{0,0},{0,1},{0,2},{0,3}}, //shorti
			{{0,0},{1,0},{2,0},{1,1}}, //shortt
			{{0,0},{0,1},{0,2},{1,2}}, //shortl
			{{0,0},{0,1},{1,1},{1,2}}, //shortz
			{{0,0},{0,1},{1,0},{1,1}}, //square
			{{0,0},{0,1},{1,1}}, //crooked3
			{{0,0},{0,1},{0,2}}, //3
			{{0,0},{0,1}}, //2
			{{0,0}}, //1
	};
	//Starting locations of each set of pieces
	public final int[][][] pieceLocations = {
			{
				{-172, 493, 3}, {-133, 415, -4}, {-130, 491, 2}, {236, 505, -3}, 
				{27, 414, 1}, {99, 410, 1}, {-71, 469, -4}, {-34, 413, -4},
				{206, 462, -1}, {114, 482, 1}, {15, 506, 2}, {187, 415, 1}, 
				{436, 470, 3}, {414, 508, 3}, {332, 433, -2}, {364, 413, 1},
				{307, 486, 1}, {469, 485, 1}, {477, 412, -1}, {523, 414, -1},
				{525, 501, 1}, 
			}, {
				{-56, 0, 4}, {-116, 31, -4}, {-73, 120, 2}, {-159, 283, -3},
				{-86, 157, 1}, {-145, 168, 1}, {-103, 87, 4}, {-200, -2, -4},
				{-48, 233, -1}, {-221, 252, 1}, {-216, 89, 2}, {-224, 137, 1},
				{-185, 372, 3}, {-214, 358, 3}, {-164, 218, -2}, {-114, 336, 1},
				{-81, 301, 1}, {-58, 355, 1}, {-150, 329, -1}, {-256, 285, -1},
				{-255, 242, 1}

			},
			{
				{-63, -36, 3}, {32, -33, -2}, {-30, -78, 2}, {244, -59, -3},
				{85, -146, 1}, {116, -97, 1}, {15, -105, -3}, {23, -58, -3},
				{191, -119, -4}, {163, -146, 1}, {127, -31, -2}, {159, -30, -3},
				{370, -85, 3}, {335, -74, 3}, {278, -127, -2}, {269, -29, 2},
				{314, -147, 1}, {421, -124, 3}, {334, -29, -4}, {362, -57, -4},
				{424, -86, 1}

			}, {
				{436, 375, 2}, {496, 344, -4}, {427, 318, 2}, {488, 216, -3},
				{568, 287, 1}, {582, 180, 1}, {487, 269, 1}, {421, 208, -4},
				{578, 145, -1}, {497, 98, 1}, {538, 252, 2}, {449, 201, 2},
				{600, 108, 3}, {513, 67, 3}, {419, 174, -3}, {428, 66, 1},
				{549, 89, 1}, {433, 12, 1}, {562, 11, -1}, {527, 13, -1},
				{490, 9, 1}
			}
	};


	int blue = 0, yellow = 1, red = 2, green = 3; 

	BlokusPiece[][] pieces;

	BlokusPiece selected;
	int xOffset, yOffset;


	int[] boardColors = {color(200), BLUE[0], YELLOW[0], RED[0], GREEN[0]};

	//BOARD
	public Board brd;

	Player[] players;
//	LAgent2[] players;
	
	//Creates a board size and generates variables for all menu buttons.
	public void setup(){
		frameRate(200);
		windowX = 1000;
		if (displayHeight>1000) 
			windowY = 1000;
		else if (displayHeight < 800)
			windowY = 800;
		else
			windowY = displayHeight;
		size(windowX,windowY);

		brd = new Board(this, boardColors);

		controller = new Controller(this);
		players = controller.getPlayers();
		setupButtons();

		pieces = new BlokusPiece[4][pieceNames.length];
		int[][] colors = {BLUE, YELLOW, RED, GREEN};
		boolean isHuman = false;
		int pX, pY;
		for (int i = 0; i< pieces.length; i++) {
			isHuman = (players[i] == null);
			for (int j = 0; j<pieces[0].length; j++) {
				pX = pieceLocations[i][j][0] + brd.getX();
				pY = pieceLocations[i][j][1] + brd.getY();
				pieces[i][j] = new BlokusPiece(colors[i], pieceNames[j], this, pX, pY, isHuman);
				pieces[i][j].setDirection(pieceLocations[i][j][2]);
			}
		}
		selected = pieces[0][pieces[0].length-1];
		strokeJoin(ROUND);
	}

	/**
	 * This is a built in method from Processing that constantly loops
	 */
	public void draw() {
		background(bkgrdColor);
		showTurn();
		showBoard(brd);
		updatePiece();

		boolean windowOpen = (htp.isActive() || exitView.isActive() || newGameSetup.isActive() || gameover.isActive());

		if(!controller.gameOver() && !windowOpen) {
			if(players[controller.getTurn()] != null) {
				frameRate((float) 2);
				controller.move(players[controller.getTurn()]);

				int[] scores = controller.calcScore();
				System.out.println("SCORES");
				for(int i = 0;i<scores.length;i++){
					System.out.println(scores[i]);
				}

			} else{
				frameRate(200);
			}
		} else {
			frameRate(200);
			if(!(htp.isActive() || exitView.isActive() || newGameSetup.isActive())) {
				gameover.setActive(true);
			}
		}
		showWindows();
		updateButton();
	}


	/**
	 * This is a built in method in processing. If the mouse is clicked, then it performs everything
	 * in this function. Right now it checks to see if shapes or menus have been clicked
	 */
	public void mousePressed(){
		clickShape();
		clickButton();
	}

	/**
	 * This is a built in method in processing. If the mouse is released, it unclicks a button,
	 * if it was previously selected.
	 */
	public void mouseReleased() {
		unclickButton();
	}

	/**
	 * Built in Processing function - when a key is pressed, performs the actions listed
	 */
	@Override
	public void keyPressed() {
		//If we're pressing the left arrow, rotate selected piece left.
		if(keyCode == LEFT) {
			selected.rotateLeft();	
		}//If we're pressing the right arrow, rotate selected piece right.
		else if (keyCode == RIGHT) {
			selected.rotateRight();
		} //If we're pressing the space bar, flip the piece
		else if (keyCode == KeyEvent.VK_SPACE) {
			selected.flip();
		} //If the enter key is passed and it's our turn, make a move
		else if (keyCode == KeyEvent.VK_ENTER) {
			if(players[controller.getTurn()] == null) {
				controller.makeMove(selected);
			}
		} //If the "P" key is pressed, pass.
		else if (keyCode == KeyEvent.VK_P) {
			controller.pass();
		}
	}

	/**
	 * When the newGame button is pressed, this resets all aspects in GUI
	 * that need to be reset for a new game to be started.
	 */
	private void newGame() {
		//Call the newGame controller method

		Button[] humanButtons = newGameSetup.getButtons();

		boolean[] humans = {true, true, true, true};
		for(int i = 0; i<4; i++) {
			if(!humanButtons[i*2 + 1].isChecked()) {
				humans[i] = false;
			}
		}


		gameover.setActive(false);
		//Make a new board
		brd = new Board(this, boardColors);

		//Make new blokus pieces and initialize them in their starting spots
		BlokusPiece[][] newPieces = new BlokusPiece[4][pieceNames.length];
		int[][] colors = {BLUE, YELLOW, RED, GREEN};
		int pX, pY;
		boolean isHuman;
		for (int i = 0; i< newPieces.length; i++) {
			//Reset all players
			isHuman = (players[i] == null);
			for (int j = 0; j<newPieces[0].length; j++) {
				pX = pieceLocations[i][j][0] + brd.getX();
				pY = pieceLocations[i][j][1] + brd.getY();

				newPieces[i][j] = new BlokusPiece(colors[i], pieceNames[j], this, pX, pY, isHuman);
				newPieces[i][j].setDirection(pieceLocations[i][j][2]);
			}
		}
		pieces = newPieces;

		controller.newGame(humans);
		players = controller.getPlayers();

		for (int i = 0; i< newPieces.length; i++) {
			//Reset all players
			isHuman = (players[i] == null);
			String a = isHuman?"":"not ";
			System.out.println("Player " + i + " is " + a +  "human" );
			for (int j = 0; j<pieces[0].length; j++) {
				pieces[i][j].setClickable(isHuman);
			}
		}
		controller.pieces = pieces;
	}

	/**
	 * Initializes the buttons (must be in GUI because of Processing)s
	 */
	private void setupButtons() {
		int bWidth = 110;
		int bHeight = 30;
		//x distance from either side, y distance from the top, bro
		int bX = 50;
		int bY = 10;

		newGame = new Button(bX, bY,  bWidth, bHeight, "New Game", buttonColors, true);
		quit = new Button(width - bX - bWidth, bY, bWidth, bHeight, "Quit", buttonColors, true);
		howToPlay = new Button(width/2-bWidth/2, bY,bWidth, bHeight, "How To Play", buttonColors, true);

		int scoreHeight = 50, scoreWidth = 50;
		int scoreX = brd.getX(), scoreY = brd.getY();

		//		color(150, 50, 50), color(180, 80, 80), 2};
		//		public final int[] GREEN = {color(50,150,50), color(80,180,80), 3};


		Button[] gob = gameover.getButtons();
		Button[] htpb = htp.getButtons();
		Button[] evb = exitView.getButtons();
		Button[] ngsb = newGameSetup.getButtons();

		int builtIn = 3;
		int numButtons = builtIn + gob.length + htpb.length + evb.length + ngsb.length;
		buttons = new Button[numButtons];
		buttons[0] = newGame;
		buttons[1] = quit;
		buttons[2] = howToPlay;
		int tot = builtIn;
		for(int i = tot; i < gob.length + tot; i++) {
			buttons[i] = gob[i - tot];

		}
		tot += gob.length;
		for(int i = tot; i < tot + htp.getButtons().length; i++){
			buttons[i] = htpb[i - tot];

		}
		tot += htpb.length;
		for(int i = tot; i < tot + evb.length; i++) {
			buttons[i] = evb[i - tot];

		}
		tot += evb.length;
		for(int i = tot; i < tot + ngsb.length; i++) {
			buttons[i] = newGameSetup.getButtons()[i-tot];
			if(i>0) {
				buttons[i].setCheckBox(true);
				if (i%2 == 0) {
					buttons[i].setChecked(true);
				}
			}
		}
	}


	/**
	 * This can be called to draw the initial empty board. Loops through and 
	 * fills each square with its proper color
	 * @param brd
	 */
	private void showBoard(Board brd){
		pushStyle();
		strokeWeight(2);
		stroke(100);
		for (int y = 0; y<20; y++) {
			for (int x = 0; x<20; x++) {
				int[] cor = brd.getCoordinates(x,y);
				fill(brd.board[y][x]); //fill each square with the color at that point in the array
				rect(cor[0],cor[1],20,20);
			}
		}
		popStyle();
	}

	/**
	 * This function finds out from the controller if the game is still going 
	 * and if so, whose turn it is. It then fills in the piece tray of the 
	 * current player.
	 */
	private void showTurn() { //0 is bottom, 1 left, 2 top, 3 right.
		pushStyle();
		strokeWeight(0);
		stroke(200);
		int[][] rects = {
				{brd.getX() - 200, brd.getY() + brd.getHeight() + 5, brd.getWidth() + 380, 150},
				{brd.getX() - 275, brd.getY()-3, 270, brd.getHeight() + 3},
				{brd.getX() - 80, brd.getY() - 150, brd.getWidth() + 150, 145},
				{brd.getX() + brd.getWidth() + 5, brd.getY() - 3, 270, brd.getHeight() + 3}
		};
		for (int i = 0; i< rects.length; i++) {
			if (i == controller.getTurn() && !controller.gameOver()) {
				stroke(50);
				strokeWeight(2);
				fill(240);
			} else {
				noStroke();
				fill(200);
			}
			rect(rects[i][0],rects[i][1],rects[i][2], rects[i][3], 2);
		}
		popStyle();
	}

	/**
	 * This function draws a given button. It must be here instead of the
	 * controller because of how processing works
	 * @param b
	 */
	private void showButton(Button b) {
		pushStyle();
		//Only draw the button if it is supposed to currently be visible
		if(b.isVisible()) {
			int x, y, bwidth, bheight, c;
			textSize(16);
			c = b.getColor();
			x = b.getX();
			y = b.getY();
			bwidth = b.getWidth();
			bheight = b.getHeight();
			fill(c);
			if(b.isChecked()) {
				strokeWeight(3);
				stroke(230);
				fill(100);
			} else if (b.isCheckBox()) {
				fill(100);
				noStroke();
			}
			else {
				noStroke(); 
			}
			rect(x, y, bwidth, bheight, 5);
			fill(0);
			text(b.getName(), x+5, y + 20);
		}
		popStyle();
	}

	/**
	 * Shows a blokus piece
	 * @param bp
	 */
	private void showPiece(BlokusPiece bp) {
		pushStyle();
		fill(bp.getColor(),180);
		strokeWeight(2);
		stroke(bp.getColor() - 202020);
		int x1, y1;
		int[][] shape = flip(rotate(getShape(bp.getID()), bp.getDirection()), bp.getDirection());
		for(int i = 0; i < shape.length ; i++) {
			x1 = bp.getX() + BLOCKSIZE * shape[i][0];
			y1 = bp.getY() + BLOCKSIZE * shape[i][1];
			rect(x1,y1, BLOCKSIZE, BLOCKSIZE);
		}
		popStyle();
	}

	/**
	 * This draws the window boxes when appropriate
	 */
	private void showWindows() {
		WindowBox w;
		boolean noVisible = true;
		for(int j = 0; j<windows.length; j++) {
			w = windows[j];
			//If a window is active, draw it
			if(w.isActive()) {
				noVisible = false;
				for(int i = 0; i < pieces.length; i++) {
					for (int k= 0; k < pieces[i].length; k++) {
						pieces[i][k].setClickable(false);
					}
				}
				//Draw the buttons in that window
				Button[] buttons = w.getButtons();
				for(int b = 0; b < buttons.length; b++) {
					buttons[b].setVisible(true);
				}
				int[][] rects = w.getRect();
				String[] words = w.getWords();
				int[][] wordXY = w.getWordXY();
				int[][] fillColors = w.getFillColors();
				if(fillColors == null) {
					fillColors = new int[rects.length][4];
					for(int i = 0; i < rects.length; i++) {
						fillColors[i] = new int[] {50, 50, 50, 255};
					}
				}
				//Draw the physical box and write the words
				for(int i = 0; i<rects.length; i++) {
					stroke(20);
					strokeWeight(2);
					fill(fillColors[i][0], fillColors[i][1], fillColors[i][2], fillColors[i][3]);
					rect(rects[i][0], rects[i][1], rects[i][2], rects[i][3], 5);
					textSize(16);
					fill(255);
					if (wordXY[0].length == 4) {
						text(words[i], wordXY[i][0], wordXY[i][1], wordXY[i][2], wordXY[i][3]);
					} else if (wordXY[0].length == 2) {
						text(words[i], wordXY[i][0], wordXY[i][1]);
					}
				}
			} else {
				Button[] buttons = w.getButtons();
				for(int b = 0; b < buttons.length; b++) {
					buttons[b].setVisible(false);
				}
			}
		}

		if (noVisible) {
			for(int i = 0; i < pieces.length; i++) {
				if(controller.getPlayers()[i] == null) {
					for (int k= 0; k < pieces[i].length; k++) {
						pieces[i][k].setClickable(true);
					}
				}
			}
		}
	}

	/**
	 * This method updates the pieces and sees which, if any, are selected
	 */
	private void updatePiece(){
		BlokusPiece b;
		if (selected.getClicked())
			drag(selected);
		else
			snap(selected);

		for(int i = 0; i < pieces.length; i++) {
			for(int j = 0; j< pieces[0].length; j++) {
				b = pieces[i][j]; 
				showPiece(b);
				if (!mousePressed){
					b.unclick();
				}
			}
		}
	}

	/**
	 * This method sees if a button has been selected
	 */
	private void updateButton() {
		Button b;
		int[] scores = controller.calcScore();
		for(int i = 0; i < buttons.length; i++) {
			b = buttons[i];
			if(b.isVisible()) {
				showButton(b);
				if (overButton(b)) {
					b.setSelected(true);
				}
				else
					b.setSelected(false);
			}
		}
	}

	/**
	 * This method sees if the mouse is over a button
	 * @param b
	 * @return
	 */
	private boolean overButton(Button b) {
		int x = b.getX(), y = b.getY(), bWidth = b.getWidth(), bHeight = b.getHeight();
		if (mouseX > x && mouseX< x+bWidth && mouseY>y && mouseY<y+bHeight) {
			return true;
		}
		return false;
	}

	private void clickButton() {
		Button b;

		/* Buttons:
		 * 0 newGame: open newGameSetup
		 * 1 quit: open exitView
		 * 2 howToPlay: open HowToPlay
		 * 3 gameover0: gameOver window 
		 * 4 htp0: close htpWindow
		 * 5 exitView0: close exitViewWindow
		 * 6 exitView1: exit()
		 * 7 newGameSetup0: make new game
		 * 8 newGameSetup1: blue = humanPlayer
		 * 9 newGameSetup2: blue = AI
		 * 10 newGameSetup3: yellow = humanPlayer
		 * 11 newGameSetup4: yellow = AI
		 * 12 newGameSetup5: red = humanPlayer
		 * 13 newGameSetup6: red = AI
		 * 14 newGameSetup7: green = humanPlayer
		 * 15 newGameSetup8: green = AI
		 */

		for(int i = 0; i < buttons.length; i++) {
			b = buttons[i];
			if(overButton(b)) {
				if (!b.isVisible()) {
					continue;
				}
				b.setClicked(true);
				if(i==0){
					newGameSetup.setActive(true);
					htp.setActive(false);
					exitView.setActive(false);
					gameover.setActive(false);
				}else if(i==1){
					exitView.setActive(true);
					htp.setActive(false);
					gameover.setActive(false);
					newGameSetup.setActive(false);
				}else if(i==2){
					exitView.setActive(false);
					htp.setActive(true);
					gameover.setActive(false);
					newGameSetup.setActive(false);
				} else if (i == 3) {
					exitView.setActive(false);
					htp.setActive(false);
					gameover.setActive(false);
					newGameSetup.setActive(true);
				} else if (i == 4) {
					exitView.setActive(false);
					htp.setActive(false);
					gameover.setActive(false);
					newGameSetup.setActive(false);
				}
				else if (i == 5) {
					exitView.setActive(false);
				} else if (i == 6) {
					exit();
				} else if (i == 7) {
					newGameSetup.setActive(false);
					htp.setActive(false);
					exitView.setActive(false);
					gameover.setActive(false);
					newGame();
				} else if (i > 7) {
					if(i%2 == 0) {
						buttons[i+1].setChecked(false);
					} else {
						buttons[i-1].setChecked(false);
					}
					buttons[i].setChecked(true);
				}
			}
		}
	}

	private void unclickButton() {
		Button b;
		for(int i = 0; i < buttons.length; i++) {
			b = buttons[i];
			b.setClicked(false);
		}
	}


	private void clickShape() {
		BlokusPiece b;
		for(int k = 0; k< pieces.length; k++) {
			for(int j = 0; j< pieces[0].length; j++) {
				b = pieces[k][j];
				int x1, y1;
				int[][] shape = flip(rotate(getShape(b.getID()), b.getDirection()), b.getDirection());
				for(int i=0; i<shape.length; i++) {
					x1 = b.getX() + BLOCKSIZE * shape[i][0];
					y1 = b.getY() + BLOCKSIZE * shape[i][1];
					if(mouseX >= x1 && mouseX < x1 + BLOCKSIZE &&
							mouseY >=y1 && mouseY < y1 + BLOCKSIZE) {
						b.click();
						xOffset = mouseX - b.getX();
						yOffset = mouseY - b.getY();
						select(b);
						break;
					}
				}
			}
		}
	}

	/**
	 * This selects a blokus piece
	 * @param bp
	 */
	private void select(BlokusPiece bp) {
		if(bp.isClickable()) {
			selected.deselect();
			selected = bp;
			selected.select();
		}
	}

	/**
	 * Given a string, gets the coordinates of a blokus piece
	 * @param pieceID, a string ID of a piece
	 * @return result, the array version of the piece
	 */
	public int[][] getShape(String pieceID) {
		int[][] result = {};
		for(int id = 0; id< pieceNames.length; id++) {
			if(pieceID == pieceNames[id]) {
				result = pieceShapes[id];
				return result;
			}
		}
		System.err.println("Error: improper shape name: " + pieceID);
		return result;
	}

	public int getPieceIndex(String pieceID) {
		for(int id = 0; id< pieceNames.length; id++) {
			if(pieceID == pieceNames[id]) {
				return id;
			}
		}
		System.err.println("Error: improper shape name: " + pieceID);
		return -1;
	}


	/**
	 * Rotate function for blokus pieces.
	 * direction-- 0: default (in pieceList). 1: left 90 degs. 2: upside down. 3: right 90 degs.
	 * @param shape
	 * @param direction
	 * @return
	 */

	public int[][] rotate(int[][] shape, int direction) {
		if (direction < 0) {
			direction = -direction;
		}
		if (direction == 1) return shape;
		int[][] newShape = new int[shape.length][2];
		for (int i = 0; i < shape.length; i++) {
			int x = shape[i][0], y = shape[i][1];
			switch(direction) {
			case 2:
				newShape[i][0] = y;
				newShape[i][1] = -x;
				break;
			case 3:
				newShape[i][0] = -x;
				newShape[i][1] = -y;
				break;
			case 4:
				newShape[i][0] = -y;
				newShape[i][1] = x;
				break;
			default:
				System.err.println("Improper direction: " + direction);
				return new int[1][1];
			}
		}
		return newShape;
	}

	/**
	 * Flip function for blokus pieces
	 * @param shape
	 * @param direction
	 * @return
	 */
	public int[][] flip(int[][] shape,  int direction) {
		if (direction == 0) {
			System.err.println("Improper direction: " + direction);
			return new int[1][1];
		}

		if (direction > 0) {
			return shape;
		}
		int[][] newShape = new int[shape.length][2];

		for(int i = 0; i < shape.length; i++) {
			int x = shape[i][0], y = shape[i][1];
			newShape[i][0] = -x;
			newShape[i][1] = y;
		}

		return newShape;
	}

	/**
	 * Drag function for blokus pieces
	 * @param bp
	 */
	private void drag(BlokusPiece bp) {
		if (!bp.isClickable()) {
			return;
		}
		int mx = constrain(mouseX, 20, width);
		int my = constrain(mouseY, 20, height);


		bp.setX(mx - xOffset);
		bp.setY(my - yOffset);
	}

	/** Snaps a blokus piece into the board
	 */
	private void snap(BlokusPiece bp) {
		//The next two lines move whatever piece is selected into a mod20 position.
		if (bp.getX() > brd.getX()-10 && bp.getX() < brd.getX()+ 10 + brd.getWidth() &&
				bp.getY() > brd.getY()- 10 && bp.getY() < brd.getY() + 10 + brd.getHeight()) {
			int[] loc = brd.getBoardLocation(bp.getX()+10, bp.getY()+10);

			bp.setX(brd.getX() + loc[0]*20);
			bp.setY(brd.getY() + loc[1]*20);
		}
	}

	public Board getBoard() {
		return this.brd;
	}

	public BlokusPiece[][] getPieces() {
		return pieces;
	}
}
