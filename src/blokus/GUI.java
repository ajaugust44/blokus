package blokus;
import java.awt.event.KeyEvent;
import java.util.Objects;

import processing.core.PApplet;

/**
 * BLOKUS
 * @author Avery Johnson
 * @author Leah Cole
 * @author Gil Eisbruch
 * @author Adrian Carpenter
 * date: May 26th, 2014
 */
public class GUI extends PApplet{
	private static final long serialVersionUID = 1L;
	private Controller controller;
	int windowX, windowY; //size of window
	private int bkgrdColor=color(250, 250, 250);

    private Button[] buttons;

	private GameOver gameover;
	private HowToPlay htp;
	private ExitView exitView;
	private NewGame newGameSetup;

	private WindowBox[] windows;

    private boolean initializedWindows = false;

	private final int[] BLUE = {color(60,60,160), color(90,90,190), 0};
	private final int[] YELLOW = {color(225,225,40), color(255,255,70), 1};
	private final int[] RED = {color(150, 50, 50), color(180, 80, 80), 2};
	private final int[] GREEN = {color(50,150,50), color(80,180,80), 3};

	final int BOARDCOLOR = color(200);

	static final int BLOCKSIZE = 15;

	//These are the official names of blokus pieces[0]
	final String[] pieceNames = {"i", "l", "u", "z", "t", "x", "w", "v", "f", "p", "y", "n",
			"shorti", "shortt", "shortl", "shortz", "square", "crooked3", "3", "2", "1"};

	//Relative coordinates of every piece
	final int[][][] pieceShapes = {
			{{0,0},{0,1},{0,2},{0,3},{0,4}}, // i
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
	private final int[][][] pieceLocations = {
			{{-172/1000, 493/800, 3}, {-133/1000, 415/800, -4}, {-130/1000, 491/800, 2}, {236/1000, 505/800, -3},
                {27/1000, 414/800, 1}, {99/1000, 410/800, 1}, {-71/1000, 469/800, -4}, {-34/1000, 413/800, -4},
                {206/1000, 462/800, -1}, {114/1000, 482/800, 1}, {15/1000, 506/800, 2}, {187/1000, 415/800, 1},
                {436/1000, 470/800, 3}, {414/1000, 508/800, 3}, {332/1000, 433/800, -2}, {364/1000, 413/800, 1},
                {307/1000, 486/800, 1}, {469/1000, 485/800, 1}, {477/1000, 412/800, -1}, {523/1000, 414/800, -1},
                {525/1000, 501/800, 1}},
            {{-56/1000, 0/800, 4}, {-116/1000, 31/800, -4}, {-73/1000, 120/800, 2}, {-159/1000, 283/800, -3},
                {-86/1000, 157/800, 1}, {-145/1000, 168/800, 1}, {-103/1000, 87/800, 4}, {-200/1000, -2/800, -4},
                {-48/1000, 233/800, -1}, {-221/1000, 252/800, 1}, {-216/1000, 89/800, 2}, {-224/1000, 137/800, 1},
                {-185/1000, 372/800, 3}, {-214/1000, 358/800, 3}, {-164/1000, 218/800, -2}, {-114/1000, 336/800, 1},
                {-81/1000, 301/800, 1}, {-58/1000, 355/800, 1}, {-150/1000, 329/800, -1}, {-256/1000, 285/800, -1},
                {-255/1000, 242/800, 1}},
			{{-63/1000, -36/800, 3}, {32/1000, -33/800, -2}, {-30/1000, -78/800, 2}, {244/1000, -59/800, -3},
                {85/1000, -146/800, 1}, {116/1000, -97/800, 1}, {15/1000, -105/800, -3}, {23/1000, -58/800, -3},
                {191/1000, -119/800, -4}, {163/1000, -146/800, 1}, {127/1000, -31/800, -2}, {159/1000, -30/800, -3},
                {370/1000, -85/800, 3}, {335/1000, -74/800, 3}, {278/1000, -127/800, -2}, {269/1000, -29/800, 2},
                {314/1000, -147/800, 1}, {421/1000, -124/800, 3}, {334/1000, -29/800, -4}, {362/1000, -57/800, -4},
                {424/1000, -86/800, 1}},
            {{436/1000, 375/800, 2}, {496/1000, 344/800, -4}, {427/1000, 318/800, 2}, {488/1000, 216/800, -3},
                {568/1000, 287/800, 1}, {582/1000, 180/800, 1}, {487/1000, 269/800, 1}, {421/1000, 208/800, -4},
                {578/1000, 145/800, -1}, {497/1000, 98/800, 1}, {538/1000, 252/800, 2}, {449/1000, 201/800, 2},
                {600/1000, 108/800, 3}, {513/1000, 67/800, 3}, {419/1000, 174/800, -3}, {428/1000, 66/800, 1},
                {549/1000, 89/800, 1}, {433/1000, 12/800, 1}, {562/1000, 11/800, -1}, {527/1000, 13/800, -1},
                {490/1000, 9/800, 1}}
	};

	BlokusPiece[][] pieces;

	private BlokusPiece selected;
	private int xOffset, yOffset;

	int[] boardColors = {color(200), BLUE[0], YELLOW[0], RED[0], GREEN[0]};

	//BOARD
    private Board brd;

	private Player[] players;
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

        gameover = new GameOver(this);
        htp = new HowToPlay(this);
        exitView = new ExitView(this);
        newGameSetup = new NewGame(this);

        windows = new WindowBox[] {gameover, htp, exitView, newGameSetup};

		controller = new Controller(this);
		players = controller.getPlayers();
		setupButtons();

		pieces = new BlokusPiece[4][pieceNames.length];
		int[][] colors = {BLUE, YELLOW, RED, GREEN};
		int pX, pY;
		for (int i = 0; i< pieces.length; i++) {
			for (int j = 0; j<pieces[0].length; j++) {
				pX = pieceLocations[i][j][0] + brd.getX();
				pY = pieceLocations[i][j][1] + brd.getY();
				pieces[i][j] = new BlokusPiece(colors[i], pieceNames[j], this, pX, pY);
				pieces[i][j].setDirection(pieceLocations[i][j][2]);
			}
		}
		selected = pieces[0][pieces[0].length-1];
		strokeJoin(ROUND);
        initializedWindows = true;
	}

	/**
	 * This is a built in method from Processing that constantly loops
	 */
	public void draw() {
        if (! initializedWindows) return;

		background(bkgrdColor);
		showTurn();
		brd.showBoard();
		updatePiece();

		boolean windowOpen = (htp.isActive() || exitView.isActive() || newGameSetup.isActive() || gameover.isActive());

		if(!controller.gameOver() && !windowOpen) {
			if(players[controller.getTurn()] != null) {
				frameRate((float) 2);
				controller.move(players[controller.getTurn()]);

				int[] scores = controller.calcScore();
				System.out.println("SCORES");
                for (int score : scores) {
                    System.out.println(score);
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

				newPieces[i][j] = new BlokusPiece(colors[i], pieceNames[j], this, pX, pY);
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
			println("Player " + i + " is " + a +  "human" );
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

		int[] buttonColors = BlokusUtils.buttonColors();
        Button newGame = new Button(bX, bY, bWidth, bHeight, "New Game", buttonColors, true, this);
        Button quit = new Button(width - bX - bWidth, bY, bWidth, bHeight, "Quit", buttonColors, true, this);
        Button howToPlay = new Button(width / 2 - bWidth / 2, bY, bWidth, bHeight, "How To Play", buttonColors, true, this);

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
        System.arraycopy(gob, 0, buttons, tot, gob.length + tot - tot);
		tot += gob.length;
        System.arraycopy(htpb, 0, buttons, tot, tot + htp.getButtons().length - tot);
		tot += htpb.length;
        System.arraycopy(evb, 0, buttons, tot, tot + evb.length - tot);
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
	 * This draws the window boxes when appropriate
	 */
	private void showWindows() {
		WindowBox w;
		boolean noVisible = true;
        for (WindowBox window : windows) {
            w = window;
            //If a window is active, draw it
            if (w.isActive()) {
                noVisible = false;
                for (BlokusPiece[] piece : pieces) {
                    for (BlokusPiece aPiece : piece) {
                        aPiece.setClickable(false);
                    }
                }
                //Draw the buttons in that window
                Button[] buttons = w.getButtons();
                for (Button button : buttons) {
                    button.setVisible(true);
                }
                int[][] rects = w.getRect();
                String[] words = w.getWords();
                int[][] wordXY = w.getWordXY();
                int[][] fillColors = w.getFillColors();
                if (fillColors == null) {
                    fillColors = new int[rects.length][4];
                    for (int i = 0; i < rects.length; i++) {
                        fillColors[i] = new int[]{50, 50, 50, 255};
                    }
                }
                //Draw the physical box and write the words
                for (int i = 0; i < rects.length; i++) {
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
                for (Button button : buttons) {
                    button.setVisible(false);
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

        for (BlokusPiece[] piece : pieces) {
            for (int j = 0; j < pieces[0].length; j++) {
                b = piece[j];
                b.showPiece();
                if (!mousePressed) {
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
        for (Button button : buttons) {
            b = button;
            if (b.isVisible()) {
                b.showButton();
                if (overButton(b)) {
                    b.setSelected(true);
                } else
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
        return mouseX > x && mouseX < x + bWidth && mouseY > y && mouseY < y + bHeight;
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
        for (Button button : buttons) {
            b = button;
            b.setClicked(false);
        }
	}


	private void clickShape() {
		BlokusPiece b;
        for (BlokusPiece[] piece : pieces) {
            for (int j = 0; j < pieces[0].length; j++) {
                b = piece[j];
                int x1, y1;
                int[][] shape = flip(rotate(getShape(b.getID()), b.getDirection()), b.getDirection());
                for (int[] aShape : shape) {
                    x1 = b.getX() + BLOCKSIZE * aShape[0];
                    y1 = b.getY() + BLOCKSIZE * aShape[1];
                    if (mouseX >= x1 && mouseX < x1 + BLOCKSIZE &&
                            mouseY >= y1 && mouseY < y1 + BLOCKSIZE) {
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
			if(Objects.equals(pieceID, pieceNames[id])) {
				result = pieceShapes[id];
				return result;
			}
		}
		System.err.println("Error: improper shape name: " + pieceID);
		return result;
	}

	public int getPieceIndex(String pieceID) {
		for(int id = 0; id< pieceNames.length; id++) {
			if(Objects.equals(pieceID, pieceNames[id])) {
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
		int mx = constrain(mouseX, BLOCKSIZE, width);
		int my = constrain(mouseY, BLOCKSIZE, height);

		bp.setX(mx - xOffset);
		bp.setY(my - yOffset);
	}

	/** Snaps a blokus piece into the board
	 */
	private void snap(BlokusPiece bp) {
		//The next two lines move whatever piece is selected into a mod Blocksize position.
		if (bp.getX() > brd.getX()-(BLOCKSIZE/2) && bp.getX() < brd.getX()+ (BLOCKSIZE/2) + brd.getWidth() &&
				bp.getY() > brd.getY()- (BLOCKSIZE/2) && bp.getY() < brd.getY() + (BLOCKSIZE/2) + brd.getHeight()) {
			int[] loc = brd.getBoardLocation(bp.getX()+(BLOCKSIZE/2), bp.getY()+(BLOCKSIZE/2));

            System.err.println("loc[0]: " + loc[0] + " loc[1] " + loc[1]);

			bp.setX(brd.getX() + loc[0]*BLOCKSIZE);
			bp.setY(brd.getY() + loc[1]*BLOCKSIZE);
		}
	}

	public Board getBoard() {
		return this.brd;
	}

	BlokusPiece[][] getPieces() {
		return pieces;
	}
}
