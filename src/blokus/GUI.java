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
	int windowWidth, windowHeight; //size of window
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
    private final double[][][] pieceLocations = {
        {{-172/20.0, 493/20.0, 3}, {-133/20.0, 415/20.0, -4}, {-130/20.0, 491/20.0, 2}, {236/20.0, 505/20.0, -3},
            {27/20.0, 414/20.0, 1}, {99/20.0, 410/20.0, 1}, {-71/20.0, 469/20.0, -4}, {-34/20.0, 413/20.0, -4},
            {206/20.0, 462/20.0, -1}, {114/20.0, 482/20.0, 1}, {15/20.0, 506/20.0, 2}, {187/20.0, 415/20.0, 1},
            {436/20.0, 470/20.0, 3}, {414/20.0, 508/20.0, 3}, {332/20.0, 433/20.0, -2}, {364/20.0, 413/20.0, 1},
            {307/20.0, 486/20.0, 1}, {469/20.0, 485/20.0, 1}, {477/20.0, 412/20.0, -1}, {523/20.0, 414/20.0, -1},
            {525/20.0, 501/20.0, 1},},
        {{-56/20.0, 0/20.0, 4}, {-116/20.0, 31/20.0, -4}, {-73/20.0, 120/20.0, 2}, {-159/20.0, 283/20.0, -3},
            {-86/20.0, 157/20.0, 1}, {-145/20.0, 168/20.0, 1}, {-103/20.0, 87/20.0, 4}, {-200/20.0, -2/20.0, -4},
            {-48/20.0, 233/20.0, -1}, {-221/20.0, 252/20.0, 1}, {-216/20.0, 89/20.0, 2}, {-224/20.0, 137/20.0, 1},
            {-185/20.0, 372/20.0, 3}, {-214/20.0, 358/20.0, 3}, {-164/20.0, 218/20.0, -2}, {-114/20.0, 336/20.0, 1},
            {-81/20.0, 301/20.0, 1}, {-58/20.0, 355/20.0, 1}, {-150/20.0, 329/20.0, -1}, {-256/20.0, 285/20.0, -1},
            {-255/20.0, 242/20.0, 1}},
        {{-63/20.0, -36/20.0, 3}, {32/20.0, -33/20.0, -2}, {-30/20.0, -78/20.0, 2}, {244/20.0, -59/20.0, -3},
            {85/20.0, -146/20.0, 1}, {116/20.0, -97/20.0, 1}, {15/20.0, -105/20.0, -3}, {23/20.0, -58/20.0, -3},
            {191/20.0, -119/20.0, -4}, {163/20.0, -146/20.0, 1}, {127/20.0, -31/20.0, -2}, {159/20.0, -30/20.0, -3},
            {370/20.0, -85/20.0, 3}, {335/20.0, -74/20.0, 3}, {278/20.0, -127/20.0, -2}, {269/20.0, -29/20.0, 2},
            {314/20.0, -147/20.0, 1}, {421/20.0, -124/20.0, 3}, {334/20.0, -29/20.0, -4}, {362/20.0, -57/20.0, -4},
            {424/20.0, -86/20.0, 1}},
        {{436/20.0, 375/20.0, 2}, {496/20.0, 344/20.0, -4}, {427/20.0, 318/20.0, 2}, {488/20.0, 216/20.0, -3},
            {568/20.0, 287/20.0, 1}, {582/20.0, 180/20.0, 1}, {487/20.0, 269/20.0, 1}, {421/20.0, 208/20.0, -4},
            {578/20.0, 145/20.0, -1}, {497/20.0, 98/20.0, 1}, {538/20.0, 252/20.0, 2}, {449/20.0, 201/20.0, 2},
            {600/20.0, 108/20.0, 3}, {513/20.0, 67/20.0, 3}, {419/20.0, 174/20.0, -3}, {428/20.0, 66/20.0, 1},
            {549/20.0, 89/20.0, 1}, {433/20.0, 12/20.0, 1}, {562/20.0, 11/20.0, -1}, {527/20.0, 13/20.0, -1},
            {490/20.0, 9/20.0, 1}}
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
		windowWidth = 1000;
		if (displayHeight>1000) 
			windowHeight = 1000;
		else if (displayHeight < 800)
			windowHeight = 800;
		else
			windowHeight = displayHeight;
		size(windowWidth, windowHeight);

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
				pX = (int) ((pieceLocations[i][j][0] * BLOCKSIZE)) + brd.getX();
				pY = (int) (pieceLocations[i][j][1] * BLOCKSIZE) + brd.getY();
				pieces[i][j] = new BlokusPiece(colors[i], pieceNames[j], this, pX, pY);
				pieces[i][j].setDirection((int) pieceLocations[i][j][2]);
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
				pX = (int)(pieceLocations[i][j][0] * BLOCKSIZE) + brd.getX();
				pY = (int)(pieceLocations[i][j][1] * BLOCKSIZE) + brd.getY();

				newPieces[i][j] = new BlokusPiece(colors[i], pieceNames[j], this, pX, pY);
				newPieces[i][j].setDirection((int) pieceLocations[i][j][2]);
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
