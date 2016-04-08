package blokus;
/**
 * NewGame window for Blokus game
 * @author Avery Johnson
 * @author Leah Cole
 * @author Gil Eisbruch
 * @author Adrian Carpenter
 * Makes window with setup page for user to choose how many human and computer players 
 *
 */
public class NewGame implements WindowBox {
	int x = 300;
	int y = 280;
	int h = 410;
	int w = 400;
	
	boolean active = false;
	int backgroundC = 255;
	int textC = 0;
	int[] colors = {180, 200, 100};
	
	int[][] fillColors = {{50, 50, 50, 255}, {90,90,190,160},{225,225,70,160}, {180,80,80, 160}, {80,180,80,160}};
	
	int[][] rectangles = {{x,y,w,h},{x+20,y+50,75,50},{x+20,y+130,75,50},{x+20,y+210,75,50},{x+20,y+290,75,50}};
	
	int[] font = {56,30,30,30,30};
	
	int[][] buttonxy = {{10,35},{35,35},{50,60}};
	
	String[] words = {"Set Up","Blue","Yellow","Red","Green"};
	
	int[][] wordXY = {{x+180,y+15,w,h},{x+25,y+55,50,50},{x+25,y+135,100,100},{x+25,y+215,100,100},{x+25,y+295,100,100}};
	
	int buttonWidth = 90;
	int buttonHeight = 30;
	int buttonX = x+100;
	int buttonY = y+60;
	
	int[] buttonColors = {200, 200, 200};
	
	Button hmnBlue = new Button(buttonX,buttonY,buttonWidth,buttonHeight, "Human", buttonColors, false); 
	Button cpuBlue = new Button(buttonX+100,buttonY,buttonWidth,buttonHeight, "CPU", buttonColors, false);
	Button hmnYellow = new Button(buttonX,buttonY + 80,buttonWidth,buttonHeight, "Human", buttonColors, false); 
	Button cpuYellow = new Button(buttonX+100,buttonY + 80,buttonWidth,buttonHeight, "CPU", buttonColors, false);
	Button hmnRed = new Button(buttonX,buttonY + 160,buttonWidth,buttonHeight, "Human", buttonColors, false); 
	Button cpuRed = new Button(buttonX+100,buttonY + 160,buttonWidth,buttonHeight, "CPU", buttonColors, false);
	Button hmnGreen = new Button(buttonX,buttonY + 240,buttonWidth,buttonHeight, "Human", buttonColors, false); 
	Button cpuGreen = new Button(buttonX+100,buttonY + 240,buttonWidth,buttonHeight, "CPU", buttonColors, false);
	Button create = new Button (x+ w/2 - 20,y+h-40,60,30, "Create", colors, false);
	
	Button[] buttons = {create, hmnBlue,cpuBlue,hmnYellow,cpuYellow,hmnRed,cpuRed,hmnGreen,cpuGreen};
	
	
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
	public int[] getFont() {
		return font;
	}
	public int[][] getWordXY() {
		return wordXY;
	}
	public int[][] getFillColors() {
		return fillColors;
	}
}
	
