This project was a combination final for AI and Software Design.

About Blokus: 
http://lmgtfy.com/?q=blokus (let me google that for you Blokus)

Scoring:
Every player has 21 pieces for a total of 89 1x1 tiles. Every player begins with 
a score of -89, and for every piece that is placed on the board, her
score increases by the number of 1x1 tiles that have been placed. The player with the
highest score wins.

MODEL:
Board: The board knows what color each of the 400 spots is. These are set by
the controller during gameplay and then this is output to the display so the 
players can see where spots have been placed.

Player: The player is simply an interface used with our agents

LAgent2: Leah and Avery's Player

AGent: Adrian and Gil's AI Player

StupidAgent: Our default stupid agent, who isn't actually that stupid.
This agent goes through all of its pieces in a specific order, which happens to 
be an order that favors playing larger pieces first, and plays those pieces in 
the first legal move it finds, flipping and/or rotating them if necessary. As
we discovered, this is not a bad strategy.

VIEW:
Board: The board is visually represented with a 20x20 grid and players can see 
what spots are occupied.

Button: Users can see these buttons in menus and can make choices using them.

WindowBox: Interface used with NewGame, HowToPlay, ExitView, and GameOver

NewGame: A new game WindowBox that allows players to choose how many human and
computer players there are. 

HowToPlay: A how-to-play WindowBox that explains the basic rules of the game and
what the controls are.

ExitView: A WindowBox that appears so that the player can verify if it wants to 
actually quit. 

GameOver: A WindowBox that appears when a game is over offering to start a new game.

BlokusPiece: Pieces are drawn and given proper shapes and colors such that the 
user can see them and work with them. 

GUI - Anything that has to do with Processing is in the GUI. Though we tried to
split MVC as cleanly as possible, certain restrictions meant that we had to keep 
specific functions in the GUI. In terms of view, any object represented visually is 
drawn here.  

CONTROLLER:
Controller - Our controller class controls as much as it can given Processing restrictions.
It controls the players and board as much as possible and monitors gameplay. 

BlokusPiece - Users are able to manipulate these pieces and those actions are conveyed to
the appropriate places during gameplay.

GUI - Any controlling that cannot happen in the controller is done in the GUI.


MOST IMPORTANT THINGS TO KNOW: 
The controls! (This is also in the how to play.) Click and drag a piece to place it on the board
press space bar to flip and use the left and right arrow keys to rotate. Press enter to make
your move. In the event that you think you can't, press p.

OTHER IMPORTANT THING: LAGENT2 is the important LAgent to deal with. LAgent (original) had space
issues when calculating a tree and so we had to try a different approach.








