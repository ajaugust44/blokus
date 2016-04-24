package blokus;

import static blokus.GUI.BLOCKSIZE;

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
	

	public BlokusPiece(int[] colors, String pieceID, GUI parent, int x, int y)  {
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

    void showPiece() {
		parent.pushStyle();
		parent.fill(getColor(),180);
		parent.strokeWeight(2);
		parent.stroke(getColor() - 202020);
		int x1, y1;
        int[][] shape = parent.getShape(id);
		shape = parent.flip(parent.rotate(shape, direction), direction);
        for (int[] aShape : shape) {
            x1 = x + BLOCKSIZE * aShape[0];
            y1 = y + BLOCKSIZE * aShape[1];
            parent.rect(x1, y1, BLOCKSIZE, BLOCKSIZE);
        }
		parent.popStyle();
	}


	public int getColor() {
		if (selected)
			return colors[1];
		return colors[0];
	}

	void setDirection(int dir) {
		if (dir < 0) {
			if(dir <= -5) dir = -1;
		}
		else {
			if (dir >= 5) dir = 1;
		}

		this.direction = dir;
	}

	void rotateLeft() {
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

	void rotateRight() {
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

	void flip() {
		if (clickable && !played) {
			direction = -direction;
		}
	}
	void deselect() {
		selected = false;
	}
	void select() {
		selected = true;
	}
	int getColorID() {
		return colors[2];
	}
	boolean isPlayed() {
		return played;
	}
	void setPlayed(boolean played) {
		this.played = played;
	}
	int[] getColors() {
		return colors;
	}
    int getSize() {
        return size;
    }
    boolean isClickable() {
        return !played && clickable;
    }
    void setClickable(boolean clickable) {
        this.clickable = clickable;
    }
    boolean getClicked() {
        return this.clicked;
    }
    void click() {
        this.clicked = true;
    }
    void unclick() {
        this.clicked = false;
    }
    int getOrigColor(){
        return colors[0];
    }
    String getID() {
        return id;
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
    int getDirection() {
        return direction;
    }
}





