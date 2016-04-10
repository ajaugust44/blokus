package blokus;

import processing.core.PApplet;

/**
 * Window box used to verify that player wants to quit
 * @author Avery Johnson
 * @author Leah Cole
 * @author Gil Eisbruch
 * @author Adrian Carpenter
 *
 */
public class ExitView implements WindowBox {

	public ExitView(PApplet parent) {
		this.parent = parent;
		No = new Button(x+50,y+125, 80, 30, "No", colors, false, parent);
		Yes = new Button(x+200,y+125, 80, 30, "Yes",colors, false, parent);
		buttons = new Button[] {No, Yes};
	}

	PApplet parent;
	int h = 200;
	int w = 350;
	int x = 500 - w/2;
	int y = 400 - h/2;
	int[] colors = {180, 200, 100};
	boolean active = false;
	Button No = new Button(x+50,y+125, 80, 30, "No", colors, false, parent);
	Button Yes = new Button(x+200,y+125, 80, 30, "Yes",colors, false, parent);
	int[][] rectangles = {{x,y,w,h}};
	String[] words = {"Do you really want to quit?"};
	Button[] buttons;
	int[][] wordXY = {{x+75,y+50}};
	
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getHeight() {
		return h;
	}
	public int getWidth() {
		return w;
	}
	public boolean getActive() {
		return active;
	}
	public void setActive(boolean active){
		this.active = active;
		for(int b = 0; b<buttons.length; b++){
			buttons[b].setVisible(false);
		}
	}
	public boolean isActive() {		
		return active;
	}

	public int[] getColors() {
		return colors;
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
	public int[][] getFillColors() {
		return fillColors;
	}
}
	

