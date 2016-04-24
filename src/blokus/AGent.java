package blokus;
import java.util.ArrayList;
import java.lang.Math;

import static blokus.Board.numBlocksHigh;
import static blokus.Board.numBlocksWide;

public class AGent implements Player {
	/**
	 * BLOKUS
	 * 
	 * @author Gil Eisbruch
	 * @author Adrian Carpenter
	 * date: May 26th, 2014
	 */
	
	Controller controller;
	int score;
	int color;
	//int[] heuristic;
	Board board;
	BlokusPiece[] hand;
	BlokusPiece[][] hands;
	int turn;
	int corner;
	int[][][] barasanaIndex;
	int boardColor;
	int plies;
	int depth;
	
	public AGent(Controller controller, int color) {
		this.controller = controller;
		this.board = controller.getBoard();
		this.color = color;
		this.hand = controller.parent.pieces[color];
		this.hands = controller.parent.pieces;
		this.turn = 0;
		this.boardColor = board.getColors()[color];
		//ply limit set
		this.plies = 3;
		this.depth = 0;
		
		
	}
	public int minValue(int[][] currentBoard, int color, BlokusPiece[][] hands, int player) {
		//minValue helper for minMax. simulates all possible comibinations of the 3 opponents' moves, returning the worst possible heuristic value for the AGent.
		ArrayList <BlokusPiece> possibleMoves;
		int val;
		if (this.depth==this.plies){
			//if we are at our ply limit, return the heuristic value for the current board state.
			return heuristic(currentBoard, color);
		}else{
			
			val = 10000000;
			//FIRST PLAYER MOVES
			int thisPlayer = (player+1)%4;
			int thisColor = controller.parent.boardColors[thisPlayer+1];
			//calculate all possible moves for the current player given the current game state
			possibleMoves = allMoves(currentBoard, thisColor, hands[thisPlayer]);
		
			if(possibleMoves.size() == 0){
				return val;
			}
			//for every possible move for the first player.
			for(int i=0;i<possibleMoves.size();i++){
				//copy the board, make the move on copied board, call minValue on that board.
				BlokusPiece thisPiece = possibleMoves.get(i);
				int[][] thisBoard = new int[20][20];
				//copy the board so that we don't mess up future iterations of this for loop.
				for (int x=0;x<thisBoard.length;x++){
					for (int y=0;y<thisBoard[0].length;y++){
						thisBoard[y][x]=currentBoard[y][x];
					}
				}
				//get the spots of thisPiece, and set those spots of the board to our color.
				int[][] pieceShape = controller.parent.flip(controller.parent.rotate(controller.parent.getShape(thisPiece.getID()), thisPiece.getDirection()), thisPiece.getDirection());
				int[] thisXY = controller.board.getBoardLocation(thisPiece.getX(),thisPiece.getY());
				int[][] spots = new int[pieceShape.length][pieceShape[0].length];
				for(int j = 0; j < pieceShape.length; j++){
					//thisXY + spots[j] should be the spot on the board we are placing the piece.
					spots[j][0] = pieceShape[j][0] + thisXY[0];
					spots[j][1] = pieceShape[j][1] + thisXY[1];
					//now we set the currentBoard spots:
					thisBoard[spots[j][0]][spots[j][1]] = thisColor;
	
				}
				
				//SECOND PLAYER MOVES
				thisPlayer = (player+2)%4;
				thisColor = controller.parent.boardColors[thisPlayer+1];
				//get all possible moves for this player
				possibleMoves = allMoves(currentBoard, thisColor, hands[thisPlayer]);
				if(possibleMoves.size() == 0){
					continue;
				}
				for(i=0;i<possibleMoves.size();i++){
					//copy the board, make the move on copied board, call minValue on that board.
					thisPiece = possibleMoves.get(i);
					int[][] secondBoard = new int[20][20];
					//copy the board
					for (int x=0;x<secondBoard.length;x++){
						for (int y=0;y<secondBoard[0].length;y++){
							secondBoard[y][x]=thisBoard[y][x];
						}
					}
					//get the spots of thisPiece, and set those spots of currentboard to our color.
					pieceShape = controller.parent.flip(controller.parent.rotate(controller.parent.getShape(thisPiece.getID()), thisPiece.getDirection()), thisPiece.getDirection());
					thisXY = controller.board.getBoardLocation(thisPiece.getX(),thisPiece.getY());
					spots = new int[pieceShape.length][pieceShape[0].length];
					for(int j = 0; j < pieceShape.length; j++){
						//thisXY + spots[j] should be the spot on the board we are placing the piece.
						spots[j][0] = pieceShape[j][0] + thisXY[0];
						spots[j][1] = pieceShape[j][1] + thisXY[1];
						//now we set the currentBoard spots:
						secondBoard[spots[j][0]][spots[j][1]] = thisColor;
		
					}
					
					//THIRD PLAYER MOVES
					thisPlayer = (player+3)%4;
					thisColor = controller.parent.boardColors[thisPlayer+1];
					if(thisPlayer == player){
						//System.out.println("IN minValue, thisPlayer = player. THIS IS BAD");
					}
					possibleMoves = allMoves(currentBoard, thisColor, hands[thisPlayer]);
					if(possibleMoves.size() == 0){
						continue;
					}
					for(i=0;i<possibleMoves.size();i++){
						//copy the board, make the move on copied board, call minValue on that board.
						thisPiece = possibleMoves.get(i);
						int[][] thirdBoard = new int[20][20];
						//copy the board
						for (int x=0;x<thirdBoard.length;x++){
							for (int y=0;y<thirdBoard[0].length;y++){
								thirdBoard[y][x]=secondBoard[y][x];
							}
						}
						//get the spots of thisPiece, and set those spots of currentboard to our color.
						pieceShape = controller.parent.flip(controller.parent.rotate(controller.parent.getShape(thisPiece.getID()), thisPiece.getDirection()), thisPiece.getDirection());
						thisXY = controller.board.getBoardLocation(thisPiece.getX(),thisPiece.getY());
						spots = new int[pieceShape.length][pieceShape[0].length];
						for(int j = 0; j < pieceShape.length; j++){
							//thisXY + spots[j] should be the spot on the board we are placing the piece.
							spots[j][0] = pieceShape[j][0] + thisXY[0];
							spots[j][1] = pieceShape[j][1] + thisXY[1];
							//now we set the currentBoard spots:
							thirdBoard[spots[j][0]][spots[j][1]] = thisColor;
			
						}
						this.depth++;
						int maxV = maxValue(thirdBoard, color, hands, player);
						if(maxV<val){
							return maxV;
						}
					}
				}
			}
				
		}
		//System.out.println("WE ARE AT THE END OF MINVALUE AND THIS IS NOT GOOD");
		return val;
			
		 
	}
	public int maxValue(int[][] currentBoard, int color, BlokusPiece[][] hands, int player){
		ArrayList <BlokusPiece> possibleMoves;
		int val;
	
		if (this.depth==this.plies){
			//System.out.println("returning heuristic of");
			//System.out.println(heuristic(currentBoard,color));
			return heuristic(currentBoard, color);
		}else{
			
			
			val = -1000000;
			possibleMoves = allMoves(currentBoard, color, hands[player]);
		
		
		for(int i=0;i<possibleMoves.size();i++){
			//copy the board, make the move on copied board, call minValue on that board.
			BlokusPiece thisPiece = possibleMoves.get(i);
			int[][] thisBoard = new int[20][20];
			//copy the board
			for (int x=0;x<thisBoard.length;x++){
				for (int y=0;y<thisBoard[0].length;y++){
					thisBoard[y][x]=currentBoard[y][x];
				}
			}
			//get the spots of thisPiece, and set those spots of currentboard to our color.
			int[][] pieceShape = controller.parent.flip(controller.parent.rotate(controller.parent.getShape(thisPiece.getID()), thisPiece.getDirection()), thisPiece.getDirection());
			int[] thisXY = controller.board.getBoardLocation(thisPiece.getX(),thisPiece.getY());

			int[][] spots = new int[pieceShape.length][pieceShape[0].length];

			for(int j = 0; j < pieceShape.length; j++){
				//thisXY + spots[j] should be the spot on the board we are placing the piece.
				spots[j][0] = pieceShape[j][0] + thisXY[0];
				spots[j][1] = pieceShape[j][1] + thisXY[1];
				//now we set the currentBoard spots:
				thisBoard[spots[j][0]][spots[j][1]] = color;

				}
			BlokusPiece[][] fakeHands = new BlokusPiece[4][21];
			for(int k = 0;k<fakeHands.length;k++){
				for(int j=0;j<fakeHands[k].length;j++){
					BlokusPiece newPiece = new BlokusPiece(hands[k][j].getColors(), hands[k][j].getID(), controller.parent, hands[k][j].getX(), hands[k][j].getY());
					newPiece.setPlayed(hands[k][j].isPlayed());
					fakeHands[k][j] = newPiece;
				}
			}
			this.depth++;
			int minV = minValue(thisBoard, color, fakeHands, (player+1)%4);
			if(minV>val){
				return minV;
			}
		}
		
		//System.out.println("WE ARE AT THE END OF MAXVALUE AND THIS IS NOT GOOD");
		return val;
		
	}
	}
	public BlokusPiece minMax(int[][] currentBoard, BlokusPiece[][] hands, int color){
		BlokusPiece bestMove = null;
		BlokusPiece[] fakeHand = new BlokusPiece[21];
		for(int i = 0;i<fakeHand.length;i++){
			BlokusPiece newPiece = new BlokusPiece(hands[this.color][i].getColors(), hands[this.color][i].getID(), controller.parent, hands[this.color][i].getX(), hands[this.color][i].getY());
			newPiece.setPlayed(hand[i].isPlayed());
			fakeHand[i] = newPiece;
		}
		
		ArrayList <BlokusPiece> possibleMoves = allMoves(this.board.board, color, fakeHand);

		if(possibleMoves.size() == 0){
			return null;
		}
		
		int val = -1000000;
		
	
		for(int i=0;i<possibleMoves.size();i++){
			//copy the board, make the move on copied board, call minValue on that board.
			BlokusPiece thisPiece = possibleMoves.get(i);
			int[][] thisBoard = new int[20][20];
			//copy the board
			for (int x=0;x<thisBoard.length;x++){
				for (int y=0;y<thisBoard[0].length;y++){
					thisBoard[y][x]=currentBoard[y][x];
				}
			}
			//get the spots of thisPiece, and set those spots of currentboard to our color.
			int[][] pieceShape = controller.parent.flip(controller.parent.rotate(controller.parent.getShape(thisPiece.getID()), thisPiece.getDirection()), thisPiece.getDirection());
			int[] thisXY = controller.board.getBoardLocation(thisPiece.getX(),thisPiece.getY());
	
			int[][] spots = new int[pieceShape.length][pieceShape[0].length];
	
			for(int j = 0; j < pieceShape.length; j++){
				//thisXY + spots[j] should be the spot on the board we are placing the piece.
				spots[j][0] = pieceShape[j][0] + thisXY[0];
				spots[j][1] = pieceShape[j][1] + thisXY[1];
				//now we set the currentBoard spots:
				thisBoard[spots[j][0]][spots[j][1]] = color;
	
				}
			int minV = minValue(thisBoard, color, hands, this.color);
			if(minV>val){
				val = minV;
				System.out.println("new best minValue");
				System.out.println(minV);
				bestMove = thisPiece;
			}
		
		}
		return bestMove;
		
	}
	
	public BlokusPiece chooseMove() {
		int ourColor = hand[0].getOrigColor();
		////System.out.println("In chooseMove:");
		////System.out.println(ourColor);

		
		BlokusPiece[][] fakeHands = new BlokusPiece[4][21];
		for(int i = 0;i<fakeHands.length;i++){
			for(int j=0;j<fakeHands[i].length;j++){
				BlokusPiece newPiece = new BlokusPiece(hands[i][j].getColors(), hands[i][j].getID(), controller.parent, hands[i][j].getX(), hands[i][j].getY());
				newPiece.setPlayed(hands[i][j].isPlayed());
				fakeHands[i][j] = newPiece;
			}
		}
		
		BlokusPiece ourMove = minMax(this.board.board, fakeHands, ourColor);
		if(ourMove == null){
			return null;
		}
		
		String pieceID = ourMove.getID();
		int result = 0;
		for(int id = 0; id< controller.parent.pieceNames.length; id++) {
			if(pieceID == controller.parent.pieceNames[id]) {
				result = id;
			}
		}
		
		hand[result] = ourMove;
		return ourMove;
		
		
		
	}

	
	public int heuristic(int[][] thisBoard, int color){
		//return 9;
		int count = 0;
		for(int x=0; x< thisBoard.length; x++){
			for(int y=0; y<thisBoard[x].length; y++){
				if (thisBoard[x][y]== color)
					count++;
			}
		}
		return count;
//		int them = 0;
//		int[] allColors = controller.parent.boardColors;
//		for (int c=1; c<allColors.length;c++){
//			if(allColors[c] != color){
//				them+= legalSpots(thisBoard, allColors[c]).size();
//			}
//		}
//		int us = (legalSpots(thisBoard, color).size());
//		int thisH = 4*us - them;
//		return thisH;
	}
	
	
	
	
	public int getColor() {
		return color;
	}

	
	public BlokusPiece bestMove(ArrayList <BlokusPiece> possibleMoves, int[][] currentBoard, int color){
		//given a list of possible piece moves, use heuristic to determine which is best for this turn.
		//maxH will keep track of the best heuristic value so far
		int maxH = -1000;
		//bestPiece will keep track of the move that gave maxH
		BlokusPiece bestPiece = null;
		for(int i=0;i<possibleMoves.size();i++){
			//copy the board, make the move on copied board, check heuristic
			BlokusPiece thisPiece = possibleMoves.get(i);
			int[][] thisBoard = new int[numBlocksHigh][numBlocksWide];
			//copy the board
			for (int x=0;x<thisBoard.length;x++){
				for (int y=0;y<thisBoard[0].length;y++){
					thisBoard[y][x]=currentBoard[y][x];
				}
			}
			//get the spots of thisPiece, and set those spots of currentboard to our color.
			int[][] pieceShape = controller.parent.flip(controller.parent.rotate(controller.parent.getShape(thisPiece.getID()), thisPiece.getDirection()), thisPiece.getDirection());
			int[] thisXY = controller.board.getBoardLocation(thisPiece.getX(),thisPiece.getY());

			int[][] spots = new int[pieceShape.length][pieceShape[0].length];

			for(int j = 0; j < pieceShape.length; j++){
				//thisXY + spots[j] should be the spot on the board we are placing the piece.
				spots[j][0] = pieceShape[j][0] + thisXY[0];
				spots[j][1] = pieceShape[j][1] + thisXY[1];
				//now we set the currentBoard spots:
				thisBoard[spots[j][0]][spots[j][1]] = color;

				}
			int them = 0;
			int[] allColors = controller.parent.boardColors;
			for (int c=1; c<allColors.length;c++){
				if(allColors[c] != color){
					them+= legalSpots(thisBoard, allColors[c]).size();
				}
			}
			int us = (legalSpots(thisBoard, color).size());
			int thisH = 4*us - them;

			
			if(thisH > maxH){
				maxH = thisH;
				//System.out.println("this heuristic");
				//System.out.println(thisH);
				bestPiece = thisPiece;
			}
			
			
		}
		//System.out.println("HEURISTIC FINAL");
		//System.out.println(maxH);
		//System.out.println(controller.board.getBoardLocation(bestPiece.getX(),bestPiece.getY())[0]);
		//System.out.println(controller.board.getBoardLocation(bestPiece.getX(),bestPiece.getY())[1]);
		
		return bestPiece;
	}
	
	public ArrayList <BlokusPiece> allMoves(int[][] currentBoard, int color, BlokusPiece[] currentHand){
		ArrayList <BlokusPiece> allMoves = new ArrayList <BlokusPiece>();
		ArrayList <int[]> allSquares = legalSpots(currentBoard, color);
		//for every square, find every piece in the goodmoves, add that to allmoves.
		for(int i = 0; i<allSquares.size(); i++){
			ArrayList<BlokusPiece> theseMoves = findMoves(allSquares.get(i), currentHand, currentBoard);
			for(int k = 0; k<theseMoves.size();k++){
				allMoves.add(theseMoves.get(k));
			}
			
		}
		return allMoves;
		
	}
			
	
	
	public ArrayList<BlokusPiece> findMoves(int[] block, BlokusPiece[] currentHand,int[][] currentBoard) {
		//Given a [x,y] block, find all moves that touch that block and are legal.
		ArrayList<BlokusPiece> goodMoves = new ArrayList<BlokusPiece>();
		int d;
		int x = block[0];
		int y = block[1];
		//each piece
		for(int i = 0;i<currentHand.length;i++){
			if (!currentHand[i].isPlayed()) {
				//each rotation
				for(int r = 1;r<5;r++){
					//each flip
					for(int f = 0;f<2;f++){
						if(f == 0) d = -r;
						else d = r;
						
						int[][] pieceShape = controller.parent.flip(controller.parent.rotate(controller.parent.pieceShapes[i], d), d);
						for(int q=0;q<pieceShape.length;q++){
							
						
						
							//spots doesn't really equal pieceShape, just to make eclipse happy.
							int[][] spots = new int[pieceShape.length][pieceShape[0].length];
							for(int j = 0; j < pieceShape.length; j++){
								//x,y + spots[j] should be the spots on the board we are placing the piece.
								spots[j][0] = pieceShape[j][0] + x - pieceShape[q][0];
								//System.out.println(spots[j][0]);
								spots[j][1] = pieceShape[j][1] + y - pieceShape[q][1];
								//System.out.println(spots[j][0]);
							}
							if(controller.legalMove(spots, currentHand[i].getOrigColor())){
								currentHand[i].setDirection(d);
								int newX = board.getX() + 20 * x -20*pieceShape[q][0];
								int newY = board.getY() + 20 * y -20*pieceShape[q][1];
								currentHand[i].setX(newX);
								currentHand[i].setY(newY);
								goodMoves.add(currentHand[i]);
							}
						}
						
						
					}
				}
			}
		}
		return goodMoves;
	}
	
	public BlokusPiece[][] createHands(int[][] currentBoard){
		BlokusPiece[][] hands = new BlokusPiece[4][21];
		for(int i = numBlocksHigh; i<currentBoard.length;i++) {
			for(int j = 0; j<currentBoard[i].length;j++) {
				BlokusPiece thisPiece = this.hand[j];
				if(currentBoard[i][j] == 1){
					thisPiece.setPlayed(false);
				}else{
					thisPiece.setPlayed(true);
				}
				hands[i-numBlocksHigh][j] = thisPiece;
			}
		}
		return hands;
	}
	
	public int[][] createState(int[][] currentBoard, BlokusPiece[][] hands){
		int[][] state = new int[24][21];
		for(int i = 0; i<currentBoard.length-4;i++) {
			for(int j = 0; j<currentBoard[i].length-1;j++) {
				state[i][j] = currentBoard[i][j];
			}
		}
		for(int i = numBlocksHigh; i<currentBoard.length;i++) {
			for(int j = 0; j<currentBoard[i].length;j++) {
				if(hands[i-numBlocksHigh][j].isPlayed()){
					state[i][j] = 0;
				}else{
					state[i][j] = 1;
				}
			}
		}
		
		
		return state;
	}
	
	public int awaySide(int[][] currentBoard, int color){
		int XDistance;
		int YDistance;
		int bestDistance = 0;
		for(int x = 0;x<currentBoard.length;x++){
			for(int y = 0;y<currentBoard[0].length;y++){
				if (currentBoard[y][x] == color){
					if(x<10){
						XDistance = x;
					}else{
						XDistance = 19-x;
					}
					if(y<10){
						YDistance = y;
					}else{
						YDistance = 19-y;
					}
					
					int thisDistance = XDistance + YDistance;
					//System.out.println("calculating heuristic:");
					//System.out.println(x);
					//System.out.println(y);
					//System.out.println(thisDistance);
					if(thisDistance > bestDistance){
						bestDistance = thisDistance;
						 
					}
				}
			}
		}
		
		return bestDistance;
		
	}
	
	public double calcSlope(int[][] currentBoard, int color){
		double bestSlope = 0;
		for(int x = 0;x<currentBoard.length;x++){
			for(int y = 0;y<currentBoard[0].length;y++){
				if (currentBoard[y][x] == color){
					if (10-y == 0) {
						continue;
					}
					else{
						double slope = ((10-x)/(10-y));
						if (Math.abs(slope) == 1) {
							bestSlope = 50;
						}
					}
				
				}
			}
		}
		return bestSlope;
	}

	public double centerDist(int[][] currentBoard, int color){
		int[] closest = {0,0};
		double bestDistance = Math.sqrt(200.0);
		for(int x = 0;x<currentBoard.length;x++){
			for(int y = 0;y<currentBoard[0].length;y++){
				if (currentBoard[y][x] == color){
					double XDistance = Math.abs(10-x);
					double YDistance = Math.abs(10-y);
					double thisDistance = Math.sqrt((XDistance*XDistance)  + (YDistance*YDistance));
					//System.out.println("calculating heuristic:");
					//System.out.println(x);
					//System.out.println(y);
					//System.out.println(thisDistance);
					if(thisDistance < bestDistance){
						bestDistance = thisDistance;
						closest[0] = x;
						closest[1] = y;
					}
				}
			}
		}
		
		return bestDistance;
		
	}
	
	public ArrayList<int[]> legalSpots(int[][] currentBoard, int color){
		//return a list of all [x,y] spots that we can legally go in based on corner, side, overlap, etc.
		////System.out.println("In legalSpots:");
		////System.out.println(color);
		ArrayList<int[]> goodMoves = new ArrayList<int[]>();
		for(int x = 0;x<currentBoard.length;x++){
			for(int y = 0;y<currentBoard[0].length;y++){
				//////System.out.println("CHECKING:");
				//////System.out.println(x);
				//////System.out.println(y);
				boolean sideHit = false;
				boolean cornerHit = false;
				boolean overlap = false;
				boolean firstCorner = false;

				//Part of the shape is out of bounds
				if(x<0 || x>19 || y<0 || y>19) {
					//////System.out.println("In legalMoves heuristic, part of shape is out of bounds!");
				}
				//if adjacent spot occupied by same color, then illegal move!
				if(currentBoard[y][x] != controller.boardColors[0]){
					overlap = true;
					
				}
				if (y>0 && x>0){
					if(y<19 && x<19){
						//everything is in bounds.
						//check for adjacency
						if(currentBoard[y+1][x] == color || currentBoard[y-1][x] == color || currentBoard[y][x+1] == color || currentBoard[y][x-1] == color){
							sideHit = true;
						}
						//check for corner of our own piece.
						if(currentBoard[y+1][x+1] == color || currentBoard[y+1][x-1] == color || currentBoard[y-1][x+1] == color || currentBoard[y-1][x-1] == color){
							cornerHit = true;
						}
					}else if(y<19){
						//x is 19, y is in bounds.
						//check for adjacency.
						if(currentBoard[y+1][x] == color || currentBoard[y-1][x] == color || currentBoard[y][x-1] == color){
							sideHit = true;

						}
						//check for corner of piece.
						if(currentBoard[y+1][x-1] == color || currentBoard[y-1][x-1] == color){
							cornerHit = true;
						}
					}else if(x<19){
						//y is 19, x is in bounds.
						//check for adjacency
						if(currentBoard[y-1][x] == color || currentBoard[y][x+1] == color || currentBoard[y][x-1] == color){
							sideHit = true;
	
						}
						//check for corner of piece.
						if(currentBoard[y-1][x+1] == color || currentBoard[y-1][x-1] == color){
							cornerHit = true;
						}

					}else{
						//y and x are both 19
						//check for adjacency
						firstCorner = true;
						if(currentBoard[y-1][x] == color || currentBoard[y][x-1] == color){
							sideHit = true;

						}
						//check for corner of piece.
						if(currentBoard[y-1][x-1] == color){
							cornerHit = true;
						}
					}
				}else if(y>0){
					//x is 0
					if(y<19){
						//check for adjacency
						if(currentBoard[y+1][x] == color || currentBoard[y-1][x] == color || currentBoard[y][x+1] == color){
							sideHit = true;
	
						}
						//check for corner of piece.
						if(currentBoard[y+1][x+1] == color || currentBoard[y-1][x+1] == color){
							cornerHit = true;
						}
					}else{
						//19,0
						firstCorner = true;
	
					}


				}else if(x>0){
					//y is 0. x is either 19, or in bounds.
					if(x<19){
						//check for adjacency
						if(currentBoard[y+1][x] == color || currentBoard[y][x+1] == color || currentBoard[y][x-1] == color){
							sideHit = true;

						}
						//check for corner of piece.
						if(currentBoard[y+1][x+1] == color || currentBoard[y+1][x-1] == color){
							cornerHit = true;
						}
					}else{
						//0,19
						firstCorner = true;
						//////System.out.println();

					}
				}else{
					//0,0
					firstCorner = true;
					//////System.out.println();

				}
			
				if((cornerHit || firstCorner) && !sideHit && !overlap){
					//////System.out.println("ADDING TO GOODMOVE");
					int[] square = {x,y};
					goodMoves.add(square);
				}else{
					//////System.out.println("NOT ADDING TO GOODMOVE");
					
					//////System.out.println(cornerHit);
					//////System.out.println(sideHit);
					//////System.out.println(overlap);
					
				}
			}
			
			
		}
		//////System.out.println(goodMoves);
		return goodMoves;
	}


	
	public BlokusPiece openingStrategy(){
		int[][][] barasanaIndex = {
	
			     {{8,340,284,1},{6,380,324,-1},{5,360,364,1},{11,420,404,4}}, //TopLeft
	
			     {{8,680,324,3},{6,640,364,-2},{5,580,364,1},{11,560,404,-4}}, //TopRight
	
			     {{8,300,624,1},{6,380,624,-4},{5,360,544,1},{11,420,544,-2}}, //BottomLeft
	
			     {{8,680,624,-3},{6,640,584,-3},{5,580,544,1},{11,560,544,2}}, //BottomRight
	
			 };
		int k = barasanaIndex[0][0][0];
		int[][] pieceShape = controller.parent.flip(controller.parent.rotate(controller.parent.pieceShapes[k],-1),-1);
		int[][] spots = new int[pieceShape.length][pieceShape[0].length];
	
		for(int j = 0; j < pieceShape.length; j++){
			//x,y + spots[j] should be the spots on the board we are placing the piece.
			spots[j][0] = pieceShape[j][0];
			spots[j][1] = pieceShape[j][1];
		}
		hand[k].setDirection(-1);
		int newX = board.getX() + 20 * 2;
		int newY = board.getY() + 20 * 0;
		hand[k].setX(newX);
		hand[k].setY(newY);
	
		turn++;
		return hand[k];
	}
}
