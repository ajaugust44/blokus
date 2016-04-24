package blokus;

import processing.core.PApplet;

/**
 * Box that pops up when a game is over offering a new game to the player
 * @author Avery Johnson
 * @author Leah Cole
 * @author Gil Eisbruch
 * @author Adrian Carpenter
 */

public class GameOver extends WindowBox {

	public GameOver(PApplet parent) {
		w = 200;
		h = 100;
		x = 500 - w/2;
		y = 400 - h/2;

		active = false;
		rectangles = new int[][]{{x, y, w, h}};
		int[] buttonColors = {180, 200, 100};
		words = new String[]{"New Game?"};
		wordXY= new int[][]{{x + w / 2 - 40, y + 20}};
		colors = null;

		this.parent = parent;
		Button newGameOK = new Button(480, 400, 40, 30, "OK", buttonColors, false, parent);
		buttons = new Button[] {newGameOK};
	}

	public void setActive(boolean active) {
		this.active = active;
		if(!active) {
			for (Button button : buttons) {
				button.setVisible(false);
			}
		}
	}
}
