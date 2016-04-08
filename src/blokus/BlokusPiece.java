package blokus;

import processing.core.PApplet;

/**
 *Blokus piece class
 *@author Avery Johnson 
 */

/* make pieces of the correct shapes 
 * that someone can select, rotate, and flip.
 * and have a color
 */


/* Color init rules
 *  Colors cannot be initialized like
 * 		"color red = color(255, 0, 0);"
 * They are integers, so instead use 
 * 		"int red = color(255, 0, 0);"
 */

public class BlokusPiece {
	//first color is color when piece is unselected, second is when selected.
	int[] colors; 
	String id;
	//we changed this from PApplet to GUI
	GUI parent;
	boolean clicked;
	boolean selected;
	//direction-- 1: default (in pieceList). 2: left 90 degs. 3: upside down. 4: right 90 degs.
	int direction; 	
	int colorID;
	boolean clickable;
	boolean played;
	int size;
	int x;
	int y;
	

	public BlokusPiece(int[] colors,  String pieceID, GUI parent, int x, int y, boolean clickable)  {
		id = pieceID;
		this.parent = parent;
		this.x = x;
		this.y = y;
		clicked = false;
		selected = false;
		direction = 1;
		this.colors = colors;
		this.clickable = clickable;
		this.size = parent.getShape(pieceID).length;
		this.played = false;
	}

	
	public int getSize() {
		return size;
	}

	public boolean isClickable() {
		return !played && clickable;
	}

	public void setClickable(boolean clickable) {
		this.clickable = clickable;
	}

	public boolean getClicked() {
		return this.clicked;
	}

	public void click() {
		this.clicked = true;
	}

	public void unclick() {
		this.clicked = false;
	}

	public int getColor() {
		if (selected)
			return colors[1];
		return colors[0];
	}

	public int getOrigColor(){
		return colors[0];
	}

	public String getID() {
		return id;
	}
	public void setID(String id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int dir) {
		if (dir < 0) {
			if(dir <= -5) dir = -1;
		}
		else {
			if (dir >= 5) dir = 1;
		}

		this.direction = dir;
	}

	public void rotateLeft() {
		if (clickable && !played) {
			direction ++;
			if (direction == 0) {
				direction = -4;
			}
			if (direction == 5) {
				direction = 1;
			}
		}
	}

	public void rotateRight() {
		if (clickable && !played) {
			direction --;
			if (direction == 0) {
				direction = 4;
			}
			if (direction == -5) {
				direction = -1;
			}
		}
	}

	public void flip() {
		if (clickable && !played) {
			direction = -direction;
		}
	}

	public void deselect() {
		selected = false;
	}

	public void select() {
		selected = true;
	}


	public int getColorID() {
		return colors[2];
	}


	public boolean isPlayed() {
		return played;
	}


	public void setPlayed(boolean played) {
		this.played = played;
	}


	public int[] getColors() {
		return colors;
	}

}





