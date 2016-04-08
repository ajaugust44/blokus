package blokus;

/**
 * Box that pops up when a game is over offering a new game to the player
 * @author Avery Johnson
 * @author Leah Cole
 * @author Gil Eisbruch
 * @author Adrian Carpenter
 */

public class GameOver implements WindowBox {

	int w = 200, h = 100;
	int x = 500 - w/2, y = 400 - h/2;

	boolean active = false;
	int[][] rectangles = {{x,y,w,h}};
	int[] buttonColors = {180, 200, 100};
	Button newGameOK = new Button(480, 400, 40, 30, "OK", buttonColors, false);
	Button[] buttons = {newGameOK};
	String[] words = {"New Game?"};
	int[][] wordXY= {{x + w/2 - 40, y + 20}};
	int[] colors = null;



	public boolean isActive() {
		return active;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}

	public int getWidth() {
		return w;
	}

	public int getHeight() {
		return y;
	}

	public int[] getColors() {
		return buttonColors;
	}

	public int[][] getRect() {
		return rectangles;
	}

	public Button[] getButtons() {
		return buttons;
	}

	public String[] getWords() {
		return words;
	}

	public int[][] getWordXY() {
		return wordXY;
	}

	public void setActive(boolean active) {
		this.active = active;
		if(!active) {
			for(int b = 0; b<buttons.length; b++){
				buttons[b].setVisible(false);
			}
		}
	}
	public int[][] getFillColors() {
		return fillColors;
	}
}
