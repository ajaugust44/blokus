package blokus;

import com.sun.javafx.beans.annotations.NonNull;
import processing.core.PApplet;

import static blokus.BlokusUtils.println;

/**
 * NewGame window for Blokus game
 *
 * @author Avery Johnson
 * @author Leah Cole
 * @author Gil Eisbruch
 * @author Adrian Carpenter
 *         Makes window with setup page for user to choose how many human and computer players
 */
public class NewGame extends WindowBox {

    public NewGame(@NonNull PApplet parent) {
        int x = 300;
        int y = 280;
        int h = 410;
        int w = 400;

        active = false;
        backgroundC = 255;
        textC = 0;
        colors = new int[]{180, 200, 100};

        fillColors = new int[][]{{50, 50, 50, 255}, {90, 90, 190, 160}, {225, 225, 70, 160}, {180, 80, 80, 160}, {80, 180, 80, 160}};

        rectangles = new int[][]{{x, y, w, h}, {x + 20, y + 50, 75, 50}, {x + 20, y + 130, 75, 50}, {x + 20, y + 210, 75, 50}, {x + 20, y + 290, 75, 50}};

        words = new String[]{"Set Up", "Blue", "Yellow", "Red", "Green"};

        wordXY = new int[][]{{x + 180, y + 15, w, h}, {x + 25, y + 55, 50, 50}, {x + 25, y + 135, 100, 100}, {x + 25, y + 215, 100, 100}, {x + 25, y + 295, 100, 100}};

        int buttonWidth = 90;
        int buttonHeight = 30;
        int buttonX = x + 100;
        int buttonY = y + 60;

        int[] buttonColors = {200, 200, 200};

        this.parent = parent;
        Button hmnBlue = new Button(buttonX, buttonY, buttonWidth, buttonHeight, "Human", buttonColors, false, parent);
        Button cpuBlue = new Button(buttonX + 100, buttonY, buttonWidth, buttonHeight, "CPU", buttonColors, false, parent);
        Button hmnYellow = new Button(buttonX, buttonY + 80, buttonWidth, buttonHeight, "Human", buttonColors, false, parent);
        Button cpuYellow = new Button(buttonX + 100, buttonY + 80, buttonWidth, buttonHeight, "CPU", buttonColors, false, parent);
        Button hmnRed = new Button(buttonX, buttonY + 160, buttonWidth, buttonHeight, "Human", buttonColors, false, parent);
        Button cpuRed = new Button(buttonX + 100, buttonY + 160, buttonWidth, buttonHeight, "CPU", buttonColors, false, parent);
        Button hmnGreen = new Button(buttonX, buttonY + 240, buttonWidth, buttonHeight, "Human", buttonColors, false, parent);
        Button cpuGreen = new Button(buttonX + 100, buttonY + 240, buttonWidth, buttonHeight, "CPU", buttonColors, false, parent);
        Button create = new Button(x + w / 2 - 20, y + h - 40, 60, 30, "Create", colors, false, parent);

        buttons = new Button[]{create, hmnBlue, cpuBlue, hmnYellow, cpuYellow, hmnRed, cpuRed, hmnGreen, cpuGreen};
    }
}

