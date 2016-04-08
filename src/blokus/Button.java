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
	
	
	public Button(int x, int y,  int w, int h, String title, int[] colors, boolean visible) {
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
	}
	
	public int getColor() {
		if(selected && !clicked && !(checkBox && checked)) {
			return colors[1];
		} if ((checkBox && checked) || clicked) {
			return colors[2];
		}
		return colors[0];
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
}
	
