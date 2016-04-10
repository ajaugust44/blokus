# Blokus

This project was a combination final for AI and Software Design.
Blokus is a board game for 2-4 players. The pieces looks sort of like [tetris pieces](https://cf.geekdo-images.com/images/pic112331_md.jpg), and are made up of one to five blocks.
The goal of Blokus is to end up with fewer blocks in your hand than any of your opponents ([learn more](http://lmgtfy.com/?q=blokus)).

### Model
The board keeps track of the color each of the 400 spots. These are set by
the controller during gameplay and then this is output to the display so the 
players can see where spots have been placed.
#### The Agents
###### LAgent2
This is Leah and Avery's player. She is demonstrably the best, and uses a 3-ply minimax search to find the best move.
###### AGent
This is Adrian and Gil's AI Player. How it works is a mystery.
###### StupidAgent
Our default stupid agent, who isn't actually that stupid. This agent goes through all of its pieces in a specific order, which happens to be an order that favors playing larger pieces first, and plays those pieces in the first legal move it finds, flipping and/or rotating them if necessary. As we discovered, this is not a bad strategy.

## How to Play
The controls! (This is also in the how to play.) Click and drag a piece to place it on the board Press space bar to flip and use the left and right arrow keys to rotate. Press enter to make your move. If you ever want to pass, press P. The game ends when all four players pass.








