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
class ExitView extends WindowBox {
	ExitView(PApplet parent) {
        h = 200;
        w = 350;
        x = 500 - w/2;
        y = 400 - h/2;
        colors = new int[] {180, 200, 100};
        active = false;

		this.parent = parent;
		No = new Button(x+50,y+125, 80, 30, "No", colors, false, parent);
		Yes = new Button(x+200,y+125, 80, 30, "Yes",colors, false, parent);
		buttons = new Button[] {No, Yes};

        rectangles = new int[][] {{x,y,w,h}};
        words = new String[]{"Do you really want to quit?"};
        wordXY = new int[][]{{x + 75, y + 50}};
	}

    Button No, Yes;

	@Override
	public void setActive(boolean active){
		this.active = active;
		for (Button button : buttons) {
			button.setVisible(false);
		}
	}
}
	

