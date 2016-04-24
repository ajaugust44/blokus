package blokus;

import java.util.ArrayList;

import static blokus.BlokusUtils.println;
import static blokus.GUI.BLOCKSIZE;

public class LAgent2 implements Player {

	Controller controller;
	int[][] board;
	int color;

	/*This array corresponds with our blokus pieces and marks which direction
	each of them is initially facing. */
	private int[] pieceDirections = {1, 3, 2, 3, 2, 0, 3, 3, 3, 3, 3, 3, 1, 2, 3, 3, 0, 3, 1, 1, 0};
	private int round = 0;

	private int[] pieceSizes = {5,5,5,5,5,5,5,5,5,5,5,5,4,4,4,4,4,3,3,2,1};
	//5+5+5+5+5+5+5+5+5+5+5+5+4+4+4+4+4+3+3+2+1 == 89 is max score

	public LAgent2(Controller controller, int color) {
		this.controller = controller;
		this.board = controller.board.getSimpleBoard();
		this.color = color;
	}
	private int[] prevMove = null;

	//Given a heuristic, it returns the higher of the two heuristics
	private int heuristic(int[][] state){
		int h1 = heuristic1(state);
		int h2 = heuristic2(state);
		return Math.max(h1, h2);
	}
	
	/**
	 * Paranoia heuristic: picks state with best score for me and worst score for opponents
	 * @param state
	 * @return
	 */
	private int heuristic1(int[][] state){
		int[] scores = {0,0,0,0};
		boolean[][] hands = {hexToHand(state[20][0]), hexToHand(state[20][1]), hexToHand(state[20][2]), hexToHand(state[20][3])};
		for(int i = 0; i<hands.length; i++) {
			scores[i] = 89;
			for(int j = 0; j<hands[i].length; j++) {
				if(hands[i][j]) {
					scores[i] -= pieceSizes[i];
				}
			}
		}
		return paranoia(scores, getTurn(state));
	}

	/**
	 * Smarter heuristic - adds the sum of all blocks that have been
	 * played or have possible places to be played. 
	 * @param state
	 * @return
	 */
	private int heuristic2(int[][] state) { //If I have a move for a shape w/ 5 blocks, add 5 to heuristic
		int[] vector = {0,0,0,0};
		int turn = getTurn(state);
		int[][] allMoves;
		boolean[][] hands = new boolean[4][21];
		boolean[][] goodPieces = new boolean[4][21]; // a piece is "good" if it has been moved or can be moved;
		for(int i = 0; i<4; i++) {
			hands[i] = hexToHand(state[20][i]);
			allMoves = allMoves(state, i);
			for (int p = 0; p<21; p++) {
				if(!hands[i][p]){
					goodPieces[i][p] = true;
				}
			}
			for (int[] allMove : allMoves) {
				goodPieces[i][allMove[0]] = true;
			}
			for(boolean b : goodPieces[i]) {
				if (b){
					vector[i] ++;
				}
			}
		}
		return paranoia(vector, turn);
	}

	/** Input a heuristic vector. Return a heuristic value based on my and
	 *	my opponent's scores
	 * 
	 * @param hValues
	 * @return
	 */
	int paranoia(int[] hValues, int turn) {
		if(turn == -1) {
			System.err.println("wrong turn");
			int sum = 0;
			int me;
			for(int i = 0; i<4; i++){
				me = (i==color)?1:-1;
				sum += me * hValues[i];
			}
			return sum;
		}
		int[] theirs = new int[3];
		for(int i = 0; i<hValues.length; i++){
			if(i < turn) {
				theirs[i] = hValues[i];
			} else if(i>turn) {
				theirs[i-1] = hValues[i];
			}
		}
		int sum = 0;
		for(int i = 0; i<3; i++){
			sum += theirs[i];
		}
		return hValues[turn] - (sum/3);
	}


	/**
	 * Run MiniMax by calling maxValue on current board state and this.color
	 */
	public BlokusPiece chooseMove() {
		//move[0] == -1, return null
		//otherwise make spots and see if it's a legalMove
		//if so, make move, otherwise, revert to stupidAgent strategy
		boolean[][] hands = new boolean[4][21];
		for(int h = 0; h<controller.parent.getPieces().length; h++) {
			for(int b=0; b< controller.parent.getPieces()[h].length; b++){
				hands[h][b] = !controller.parent.getPieces()[h][b].isPlayed();
			}
		}
		int[] hexHands = new int[4];
		for (int i = 0 ; i<4; i++){
			hexHands[i] = handToHex(hands[i]);
		}

		int[][] state = generateState(board, hexHands);

		int[] move = maxValue(state, 3, new int[] {90, -1}, this.color); 
		//If our first move is -1, make a stupid move
		if(move[0] == -1) {
			round ++;
			BlokusPiece b = stupidMove(state, color);
			return b;
		}
		//if repeat move
		if (prevMove != null && move[0] == prevMove[0]) {
			BlokusPiece b = stupidMove(state, color);
			return b;
		}
		int[][] spots = getShape(move);
		if(controller.legalMove(spots, controller.parent.pieces[move[4]][move[0]].getOrigColor())) {
			BlokusPiece b = controller.parent.pieces[move[4]][move[0]];
			int[] loc = controller.board.getWindowLocation(move[2], move[3]);

			b.setX(loc[0]);
			b.setY(loc[1]);
			b.setDirection(move[1]);
			round ++;
			prevMove = move;
			return b;
		} else {
			round ++;
			return stupidMove(state, color);
		}
	}

	/**
	 * StupidMove function, taken from our original stupidAgent.
	 * Plays first piece in first available spot
	 * @param state
	 * @param color2
	 * @return
	 */
	private BlokusPiece stupidMove(int[][] state, int color2) {
		BlokusPiece[][] hands = controller.pieces;
		int d, x, y, newX, newY;
		BlokusPiece[] hand = hands[color];
		int[][] pieceShape, spots;
		//find any spot where i can put any piece
		for(int i = 0;i<hand.length;i++){
			//each rotation
			if (!hand[i].isPlayed()) {
				for(int r = 1;r<5;r++){
					//each flip
					for(int f = 0;f<2;f++){
						if(f == 0) d = -r;
						else d = r;
						//each board spot
						for(x = 0;x<board.length;x++){
							for(y = 0;y<board[0].length;y++){
								//each piece, each x,y coordinate
								pieceShape = controller.parent.flip(controller.parent.rotate(controller.parent.pieceShapes[i], d), d);
								//spots doesn't really equal pieceShape, just to make eclipse happy.
								spots = new int[pieceShape.length][pieceShape[0].length];
								for(int j = 0; j < pieceShape.length; j++){
									//x,y + spots[j] should be the spots on the board we are placing the piece.
									spots[j][0] = pieceShape[j][0] + x;
									spots[j][1] = pieceShape[j][1] + y;
								}
								if(controller.legalMove(spots, hand[i].getOrigColor())){
									hand[i].setDirection(d);
									newX = controller.board.getX() + BLOCKSIZE * x;
									newY = controller.board.getY() + BLOCKSIZE * y;
									println("NewX: " + newX + ", NewY: " + newY);
									hand[i].setX(newX);
									hand[i].setY(newY);
									BlokusPiece b = hand[i];
									prevMove = new int[] {controller.parent.getPieceIndex(b.getID()), b.getDirection(), x, y, b.getColorID()};
									return b;
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Min of minimax
	 * @param state
	 * @param plies
	 * @param alphaBeta
	 * @param color
	 * @return
	 */
	//Moves: 0: pieceIndex, 1: direction, 2: x, 3: y, 4: color, 5: alpha, 6: beta
	public int[] minValue(int[][] state, int plies, int[] alphaBeta, int color) {
		int[][] moves = allMoves(state, color);
		//If we have no moves
		if(moves.length == 0) {
			return new int[] {-1, -1, -1, -1, -1, heuristic(state), heuristic(state)}; //whenever move starts with -1, it is a null move
		}
		int[] best = {-1, -1, -1, -1, -1, alphaBeta[1], alphaBeta[1]};
		int alpha = alphaBeta[0];
		int beta = alphaBeta[1];
		//Only look one ply ahead: minimize that move
		if(plies == 1) {
			int value = heuristic(state); //We're going for a low value
			int[][] newState;
			for(int[] move : moves){
				newState = getState(move, state);
				value = heuristic(newState);
				if(value<beta){
					beta = new Integer(value);
					best = move;
				}
				if (value < alpha) {
					return new int[] {best[0], best[1], best[2], best[3], best[4], new Integer(alpha), new Integer(alpha)};
				}
			}
			return new int[] {best[0], best[1], best[2], best[3], best[4], beta, beta};
		}
		if((color+1)%4 == this.color) {
			//find worst of their best
			int[] maxMove;
			int maxValueAlpha;
			int[][] newState;
			for(int[] move : moves) {
				newState = getState(move, state);
				maxMove = maxValue(newState, plies-1, new int[] {alpha, beta}, color);
				maxValueAlpha = maxMove[5];
				if(maxValueAlpha < beta){
					best = move;
					beta = new Integer(maxValueAlpha);
				}
				if(maxValueAlpha < alpha) {
					return new int[] {best[0], best[1], best[2], best[3], best[4], alpha, alpha};
				}
			}
			return new int[] {best[0], best[1], best[2], best[3], best[4], beta, beta}; 
		} else {
			int[] minMove;
			int minValueBeta;
			int[][] newState;
			for(int[] move : moves) {
				newState = getState(move, state);
				minMove = minValue(newState, plies-1, new int[] {new Integer(beta), new Integer(alpha)}, color);
				minValueBeta = minMove[6];
				if(minValueBeta < beta){
					best = move;
					beta = new Integer(minValueBeta);
				}
				if(minValueBeta > alpha) {
					return new int[] {best[0], best[1], best[2], best[3], best[4], alpha, alpha}; 		
				}
			}
			return new int[] {best[0], best[1], best[2], best[3], best[4], beta, beta};
		}
	}

	/**
	 * Max of minimax
	 * @param state
	 * @param plies
	 * @param alphaBeta
	 * @param color
	 * @return
	 */
	public int[] maxValue(int[][] state, int plies, int[] alphaBeta, int color) {
		int[][] moves = allMoves(state, color);
		//		System.out.print(boardToString(state));

		if(moves.length == 0) {
			return new int[] {-1, -1, -1, -1, -1, heuristic(state), heuristic(state)}; //whenever move starts with -1, it is a null move
		}
		int[] best = {-1, -1, -1, -1, -1, heuristic(state), heuristic(state)};
		int beta = alphaBeta[0];
		int alpha = alphaBeta[1];
		if(plies == 1) {

			int value;
			int[][] newState;
			for(int[] move : moves){
				newState = getState(move, state);
				value = heuristic(newState);
				if(value > alpha){
					alpha = new Integer(value);
					best = move;
				}
				if (value > beta) {
					return new int[] {best[0], best[1], best[2], best[3], best[4], new Integer(beta), new Integer(beta)};
				}
			}
			return new int[] {best[0], best[1], best[2], best[3], best[4], alpha, alpha};
		}

		//find best of their worst
		int[] minMove;
		int minValueAlpha, minValueBeta;
		int[][] newState;
		for(int[] move : moves) {
			newState = getState(move, state);
			minMove = minValue(newState, plies-1, new int[] {alpha, beta}, color);
			minValueAlpha = minMove[5];
			minValueBeta = minMove[6];
			if(minValueAlpha > alpha){
				best = move;
				alpha = new Integer(minValueAlpha);
			}

			if(minValueBeta > beta) {
				return new int[] {best[0], best[1], best[2], best[3], best[4], beta, beta}; 		
			}
		}
		return new int[] {best[0], best[1], best[2], best[3], best[4], alpha, alpha}; 		
	}
	
	/**
	 * Given a state, find all of the moves and return them in int[][] format
	 * @param state
	 * @param color
	 * @return
	 */
	public int[][] allMoves(int[][] state, int color){
		ArrayList<Integer[]> corners = findCorners(state, color);
		ArrayList<Integer[]> moves = findAllMoves(state, corners, color);
		int size = moves.size();
		int[][] arrayMoves = new int[size][7];
		for(int i = 0; i < size; i++){
			Integer[] move = moves.get(i);
			for(int j = 0; j < move.length; j++){
				arrayMoves[i][j] = move[j];
			}
		}
		return arrayMoves;
	}


	public int getColor() {
		return color;
	}

	/**
	 * Given a state, corners, and color, find all possible moves and return them in ArrayList format
	 * @param state
	 * @param allCorners
	 * @param color
	 * @return
	 */
	public ArrayList<Integer[]> findAllMoves(int[][] state, ArrayList<Integer[]> allCorners, int color){
		//For each player, find all possible moves
		//Return list of {pieceID index, x, y, direction (incl. flip)


		//Calculates everyone's hand as a boolean array
		boolean[][] hands = new boolean[4][21];
		for (int i = 0; i<hands.length; i ++) {
			hands[i] = hexToHand(state[state.length-1][i]);
		}

		ArrayList<Integer[]> possibleMoves = new ArrayList<Integer[]>();
		if(allCorners == null) {
			allCorners = findCorners(state, color);
		}

		for(int s=0; s<hands[color].length;s++){
			//calculate shape
			if(hands[color][s]){
				int[][] shape = controller.parent.getShape(controller.parent.pieceNames[s]);
				for(int f=-1; f<2; f+=2){
					if ((pieceDirections[s] == 3) || (pieceDirections[s] < 3 && f == -1)) {
						//this flips the shape
						for(int r=1; r<5;r++){
							if ((pieceDirections[s] > 1) || (pieceDirections[s] == 1 && (r == 1 || r == 2)) ||
									(pieceDirections[s] == 0 && r == 1)) {
								int d = f*r; //direction of piece
								int[][] spots = controller.parent.rotate(controller.parent.flip(shape, d), d);
								for(Integer[] cornerSlot : allCorners) {
									for(int[] corner : spots){
										//need the new spots to be the actual board indices of where we want to place this piece
										int[][] newSpots = new int[spots.length][2];
										for(int block = 0; block < spots.length; block++) {
											newSpots[block][0] = spots[block][0] + cornerSlot[0] - corner[0];
											newSpots[block][1] = spots[block][1] + cornerSlot[1] - corner[1];
										}
										if(legalMove(newSpots, color, state)) {
											//use spots to find 0,0 spot on board
											int x = newSpots[0][0] - spots[0][0];
											int y = newSpots[0][1] - spots[0][1];
											Integer[] move = {s, d, x, y, color};
											possibleMoves.add(move);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return possibleMoves;
	}


	/**
	 * Finds available corners where we can play
	 * @param state - board and scores
	 * @return board indices of corners for each player
	 */
	public ArrayList<Integer[]>  findCorners(int[][] state, int color){
		//check four corners to see if our color is in any of them. if not, return all of the corners that are empty. if so, proceed
		ArrayList<Integer[]> freeSquares = new ArrayList<Integer[]>();
		int x, y;
		boolean hasPlayed = false;
		for(int y0 = 0; y0<2; y0++) {
			for (int x0 = 0; x0<2; x0++) {
				y = y0 * 19;
				x = x0 * 19;

				if (state[y][x] == -1) {
					freeSquares.add(new Integer[] {x,y});
				}
			}
		}

		if(round == 0) {
			return freeSquares;
		}

		freeSquares = new ArrayList<Integer[]>();

		//Go through the whole board and for each color, see if a corner is adjacent to that color and not edge adjacent to same color
		for(y = 0; y<state.length-1; y++){
			for(x = 0; x<state.length-1; x++){
				//check corner adjacent
				boolean corAdj = cornerAdjacent(x,y,color, state);
				//check orth adj
				boolean orthAdj = orthAdjacent(x,y,color, state);
				//if corner not check 
				if(corAdj && !orthAdj){
					freeSquares.add(new Integer[] {x,y});
				}
			}
		}
		return freeSquares;
	}

	/** Identical to orthAdjacent with a board given
	 * 
	 * @param x1
	 * @param y1
	 * @param c
	 * @param sb
	 * @return
	 */
	private boolean orthAdjacent(int x1, int y1, int c, int[][] sb){
		//check left right
		for(int y=-1; y<2; y+=2){
			if(y1 + y <= 19 && y1 + y >= 0 && sb[y1+y][x1] == c && sb[y1][x1] == -1){
				return true;
			}
		}
		//check up down
		for(int x=-1; x<2; x+=2){
			if(x1 + x <= 19 && x1 + x >= 0 && sb[y1][x1+x] == c && sb[y1][x1] == -1){
				return true;
			}
		}
		return false;
	}

	/**
	 * Identical to cornerAdjacent, but with a given board
	 * @param x1
	 * @param y1
	 * @param c
	 * @param sb
	 * @return
	 */
	private boolean cornerAdjacent(int x1, int y1, int c, int[][] sb){
		for(int y=-1; y<2; y+=2){
			for(int x=-1; x<2; x+=2){
				if(y1 + y <= 19 && y1 + y >= 0 && x1 + x <=19 && x1 + x >= 0 && sb[y1+y][x1+x] == c && sb[y1][x1] == -1){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Verify that a move is legal given a simple board, the spots where a
	 * piece is to be placed, and the color of the current player
	 * @param spots
	 * @param color
	 * @param sb
	 * @return
	 */
	private boolean legalMove(int[][] spots, int color, int[][] sb){
		//go through each spot, see that at least one of them is corner adjacent (do we need this?)
		//see if it is overlapping any color or if it is orthogonally adjacent to its own color
		boolean cornerAdjacent = false;
		for(int[] spot : spots){
			if(spot[0] < 0 || spot[0] > 19 || spot[1] < 0 || spot[1] > 19) {
				return false;
			}
			if(sb[spot[1]][spot[0]] != -1){
				return false;
			}
			if(orthAdjacent(spot[0], spot[1], color, sb)){
				return false;
			}
			if(cornerAdjacent(spot[0], spot[1], color, sb) || round == 0) {
				cornerAdjacent = true;
			}
		}
		return cornerAdjacent;
	}

	/**
	 * Get a state given an old state and where we want to make a move
	 * @param move
	 * @param oldState
	 * @return
	 */
	int[][] getState(int[] move, int[][] oldState) {
		int[][] newState = new int[oldState.length][oldState[0].length];

		int[][] stateBoard = new int[20][20];
		for(int i = 0; i<20; i++) {
			for (int j = 0; j< 20; j++){
				stateBoard[i][j] = oldState[i][j];
				newState[i][j] = oldState[i][j];
			}
		}

		//Generate new hands for each player
		boolean[][] hands = new boolean[4][21];
		for (int i = 0; i< 4; i++) {
			if (i == move[4]){
				hands[i] = hexToHand(oldState[20][i]);
				hands[i][move[0]] = false;
				newState[20][i] = handToHex(hands[i]);
			} else {
				newState[20][i] = oldState[20][i];
			}
		}

		int[][] shape = getShape(move);
		int x, y;
		boolean cornerAdjacent = false;
		for(int i = 0; i< shape.length; i++){
			x = shape[i][0];
			y = shape[i][1];
			if(oldState[y][x] == -1 && !orthAdjacent(x, y, move[4], stateBoard)) {
				if(cornerAdjacent(x,y, move[4], stateBoard) || ((x==0||x==19) && (y == 0 || y == 19))) {
					cornerAdjacent = true;
				}
				newState[y][x] = move[4];
			} else {
				System.err.println("Trying to generate new state for invalid move:");
				System.err.println(move[0] + ", " + move[1] + ", " + move[2] + ", " + move[3] + ", " + move[4]);
				return null;
			}
		}
		if(!cornerAdjacent){
			System.err.println("Trying to generate new state for invalid move: not corner adjacent");
			System.err.println(move[0] + ", " + move[1] + ", " + move[2] + ", " + move[3] + ", " + move[4]);
			return null;
		}
		return newState;
	}
	
	/**
	 * Get the int[][] shape correctly rotated and flipped given a move
	 * @param move
	 * @return
	 */
	int[][] getShape(int[] move) {
		int[][] oldShape = controller.parent.rotate(controller.parent.flip(controller.parent.pieceShapes[move[0]], move[1]), move[1]);
		int[][] newShape = new int[oldShape.length][2];
		for(int i = 0; i< newShape.length; i++){
			newShape[i][0] = oldShape[i][0] + move[2];
			newShape[i][1] = oldShape[i][1] + move[3];
		}
		return newShape;
	}

	/**
	 * Given a board and the hands of players, generate a state
	 * @param board2
	 * @param hands
	 * @return
	 */
	public int[][] generateState(int[][] board2, int[] hands) {
		int[][] state = new int[21][20];

		state[20] = hands;

		for (int col = 0; col < 20; col++) {
			for (int row = 0; row < 20; row ++) {
				state[col][row] = board2[col][row];
			}
		}
		return state;
	}

	/**
	 * Given a state, figure out whose turn it is. 
	 * @param state
	 * @return
	 */
	public int getTurn(int[][] state) {
		int[] numMoves = new int[4];
		boolean[][] hands = new boolean[4][21];
		for (int i = 0; i<4; i++) {
			hands[i] = hexToHand(state[20][i]);
			for(int j = 0; j<hands[i].length; j++) {
				if(!hands[i][j]) {
					numMoves[i] += 1;
				}
			}
		}
		for(int i = 1; i<4; i++){
			if(numMoves[i] < numMoves[(i+1)%4]) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * Converts boolean hand to hex hand
	 * @param hand boolean array of size 21 representing what pieces we have
	 * @return 6 hex values indicating what combination of things are true and false
	 */
	public int handToHex(boolean[] hand){
		int hex = 0x0;
		int num = 0;
		for(int i=0; i<hand.length;i++){
			num = hand[i]?1:0;
			num*=Math.pow(2, (20-i));
			hex+=num;
		}
		return hex;
	}

	/**
	 * Converts hex hand to boolean hand
	 * @param hex values indicating what combination of things are true and false
	 * @return boolean array of size 21 representing what pieces we have
	 */
	public boolean[] hexToHand(int hex){
		boolean[] hand = new boolean[21];
		String sH = Integer.toBinaryString(hex);
		String handString = String.format("%21s", sH).replace(' ', '0');
		for(int i=0; i<handString.length();i++){
			hand[i] = (handString.charAt(i) == '0')?false:true;
		}
		return hand;
	}


	/**
	 * 
	 * @param sb
	 * @return
	 */
	public String boardToString(int[][] sb){
		String boardString = "\n";
		for(int row = 0; row<20; row++) {
			for(int col = 0; col < 20; col++){
				String space = (sb[row][col] == -1)?" ":"  ";
				boardString =  boardString + space + sb[row][col];
			}
			boardString = boardString + "\n";
		}
		return boardString;
	}

}
