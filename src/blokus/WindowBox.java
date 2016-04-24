package blokus;

import processing.core.PApplet;

/**
 * Interface for menu boxes that pop up
 * @author Avery Johnson
 * @author Leah Cole
 * @author Gil Eisbruch
 * @author Adrian Carpenter
 *
 */

public abstract class WindowBox {
    PApplet parent;

	int x = 0, y = 0, h = 0, w = 0, backgroundC = 0, textC = 0;
	boolean active = false;
	int[][] rectangles = null;
	Button[] buttons = null;
	String[] words = null;
	int[][] wordXY= null;
	int[] colors = {180, 200, 100};
	int[][] fillColors = null;
	
	int getX() {
        return x;
    }
	int getY() {
        return y;
    }
	int getWidth() {
        return w;
    }
	int getHeight() {
        return h;
    }
	int[] getColors() {
        return colors;
    }
	int[][] getRect() {
        return rectangles;
    }
    Button[] getButtons() {
        return buttons;
    }
    String[] getWords() {
        return words;
    }
	int[][] getWordXY() {
        return wordXY;
    }
	int[][] getFillColors() {
        return fillColors;
    }
	boolean isActive() {
        return active;
    }
	void setActive(boolean active) {
        this.active = active;
    }

}
