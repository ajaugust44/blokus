package blokus;

import processing.core.PApplet;

/**
 * Class for menu buttons
 * @author Avery Johnson
 * @author Leah Cole
 * @author Gil Eisbruch
 * @author Adrian Carpenter
 *
 */

public class Button{
	int x = 0;
	int y = 0;
	int height = 0;
	int width = 0;
	
	String name;
	boolean clicked;
	boolean selected;
	PApplet parent;
	boolean visible;

	int[] colors;
	boolean checkBox;
	boolean checked;
	
	public Button(int x, int y,  int w, int h, String title, int[] colors, boolean visible, PApplet parent) {
		this.x = x;
		this.y = y;
		height = h;
		width = w;
		name = title;
		selected = false;
		clicked = false;
		this.colors = colors;
		this.visible = visible;
		
		checkBox = false;
		checked = false;
        this.parent = parent;
	}

	public int getColor() {
		if(selected && !clicked && !(checkBox && checked)) {
			return colors[1];
		} if ((checkBox && checked) || clicked) {
			return colors[2];
		}
		return colors[0];
	}

    public void showButton() {
        if(parent == null) {
            return;
        }
        parent.pushStyle();
        //Only draw the button if it is supposed to currently be visible
        if(isVisible()) {
            parent.textSize(16);
            parent.fill(getColor());
            if(isChecked()) {
                parent.strokeWeight(3);
                parent.stroke(230);
                parent.fill(100);
            } else if (isCheckBox()) {
                parent.fill(100);
                parent.noStroke();
            }
            else {
                parent.noStroke();
            }
            parent.rect(x, y, width, height, 5);
            parent.fill(0);
            parent.text(getName(), x+5, y + 20);
        }
        parent.popStyle();
    }


	public boolean isChecked() {
		return checkBox && checked;
	}
	
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	public void setCheckBox(boolean checkBox) {
		this.checkBox = checkBox;
	}
		
	public boolean getSelected(){
		return this.selected;
	}
	
	public void setSelected(boolean s){
		this.selected = s;
	}
	
	public boolean getClicked(){
		return this.clicked;
	}
	
	public void setClicked(boolean c){
		this.clicked = c;
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

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isCheckBox() {
		return this.checkBox;
	}

    @Override
    public String toString() {
        return "Button: " + this.name;
    }
}
	
