package blokus;

/**
 * Interface for menu boxes that pop up
 * @author Avery Johnson
 * @author Leah Cole
 * @author Gil Eisbruch
 * @author Adrian Carpenter
 *
 */

public interface WindowBox {
	int x = 0, y = 0, h = 0, w = 0, backgroundC = 0, textC = 0;
	boolean active = false;
	int[][] rectangles = null;
	Button[] buttons = null;
	String[] words = null;
	int[][] wordXY= null;
	int[] colors = {180, 200, 100};
	int[][] fillColors = null;
	
	int getX();
	int getY();
	int getWidth();
	int getHeight();
	int[] getColors();
	int[][] getRect();
	Button[] getButtons();
	String[] getWords();
	int[][] getWordXY();
	int[][] getFillColors();
	boolean isActive();
	void setActive(boolean active);

}
