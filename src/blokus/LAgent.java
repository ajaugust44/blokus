package blokus;

import java.util.ArrayList;

/**
 * LeahAveryAgent=LAgent
 * 
 * @author Leah Cole and Avery Johnson
 *
 */


/**
 * 
 * PLEASE LOOK AT LAGENT2
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 *
 */

/*
 * We need to implement Minimax!! Things we need:
 * Plies
 * Utility
 * Min Value
 * Max Value
 * Is Terminal
 * Heuristic as defined in our outline
 * Don't do his legal moves thing like last time. Make find all moves
 * 
 * 
 * 
 * What we said we were going to prioritize in our proposal:
 * Play blocks that have 5 tiles
 * Play blocks greater than 2 tiles
 * Put off playing the singleton as long as possible
 * Build a barrier with large pieces that can fill in smaller ones later
 * Avoid traveling directly across the board
 * Minimize other players available spaces to play
 */
public class LAgent implements Player {
	Controller controller;
	Board board;
	int color;
	int boardColor;

	public int[] pieceDirections = {1, 3, 2, 3, 2, 0, 3, 3, 3, 3, 3, 3, 1, 2, 3, 3, 0, 3, 1, 1, 0};
	int[][][] firstMoves = {
			{{6, -2, 2, 2}, {7, -2, 5, 5}, {5, 1, 3, 6}, {8, -2, 8, 7}}, 		//0,0
			{{6, -1, 2, 17}, {7, -1, 5, 14}, {5, 1, 6, 14}, {8, -1, 7, 11}}, 	//0,19
			{{6, -3, 17, 2}, {7, -3, 14, 5}, {5, 1, 11, 3}, {8, -3, 12, 8}},	//19,0
			{{6, -4, 17, 17}, {7, -4, 14, 14}, {5, 1, 14, 11}, {8, -4, 11, 12}} //19,19
	};
	int round = 0;

	boolean firstMovesDone = true; 
	boolean treeExists = false;

	int plies = 1;

	int corner;

	Node root;

	public LAgent(Controller controller, int color) {
		this.controller = controller;
		this.board = controller.getBoard();
		this.color = color;
		this.boardColor = controller.parent.BOARDCOLOR;
	}

	/**
	 * Currently: makes first few set moves, then use StupidAgent strategy
	 */
	public BlokusPiece chooseMove() {
		int x, y, d;

		BlokusPiece[][] hands = controller.pieces;

		BlokusPiece[] hand = hands[color];

		int[] hexHands = generateHexHands(hands);

		if (round == 0) { // set the instance variable corner so we know what to do.
			setCorner(generateState(board.simpleBoard, hexHands));
		}

		if (round <= 3 && !firstMovesDone) {
			int[] move = firstMoves[corner][round];
			BlokusPiece p = controller.pieces[color][move[0]];
			int origX = p.getX();
			int origY = p.getY();
			p.setDirection(move[1]);
			p.setX(move[2]);
			p.setY(move[3]);
			int[][] pieceShape = controller.parent.flip(controller.parent.rotate(controller.parent.pieceShapes[move[0]], p.getDirection()), p.getDirection());
			int[][] spots = new int[pieceShape.length][pieceShape[0].length];
			for(int j = 0; j < pieceShape.length; j++){
				//x,y + spots[j] should be the spots on the board we are placing the piece.
				spots[j][0] = pieceShape[j][0] + p.getX();
				spots[j][1] = pieceShape[j][1] + p.getY();
			}
			int[] loc = board.getWindowLocation(p.getX(), p.getY());
			if(controller.legalMove(spots, p.getOrigColor())) {
				p.setX(loc[0]);
				p.setY(loc[1]);
				round ++;
				return p;
			} else {
				p.setX(origX);
				p.setY(origY);
				firstMovesDone = true;
			}
		}

		int[][] state = generateState(board.simpleBoard, generateHexHands(controller.pieces));

		if(!treeExists) {
			generateTree(this.plies, state);
		} else {
			generateNextLevel();
		}

		int bestChild = 0;
		int bestMove = root.children.get(0).value;
		for(int i = 0; i<root.children.size(); i++){
			if(bestMove<root.children.get(i).value) {
				bestMove = root.children.get(i).value;
				bestChild = i;
			}
		}
		Integer[] move = root.moveList.get(bestChild);
		root = root.children.get(bestChild);
		generateNextLevel();

		BlokusPiece b = controller.parent.pieces[color][move[0]];
		if(b.isPlayed()) {
			System.err.println("Trying to play piece that has already been played!");
			return null;
		}
		if(color != move[4]) {
			System.err.println("Trying to play piece of wrong color!");
		}

		int oldX = b.getX();
		int oldY = b.getY();
		int oldDir = b.getDirection();

		b.setDirection(move[1]);
		int newX = board.getX() + 20 * move[2];
		int newY = board.getY() + 20 * move[3];
		b.setX(newX);
		b.setY(newY);

		int[][] pieceShape = controller.parent.flip(controller.parent.rotate(controller.parent.pieceShapes[move[0]], move[1]), move[1]);
		int[][] spots = new int[pieceShape.length][pieceShape[0].length];
		for(int j = 0; j < pieceShape.length; j++){
			spots[j][0] = pieceShape[j][0] + move[2];
			spots[j][1] = pieceShape[j][1] + move[3];
		}
		if(controller.legalMove(spots, b.getOrigColor())){
			round ++;
			return b;
		} else {
			b.setDirection(oldDir);
			b.setY(oldY);
			b.setX(oldX);
		}

		System.out.println("No moves in tree");

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
						for(x = 0;x<board.simpleBoard.length;x++){
							for(y = 0;y<board.simpleBoard[0].length;y++){
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
									newX = board.getX() + 20 * x;
									newY = board.getY() + 20 * y;
									hand[i].setX(newX);
									hand[i].setY(newY);
									round ++;
									return hand[i];
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	private void generateNextLevel() {
		// go through moves made in controller + down corresponding branch
		// set root to new root.
		Integer[][] lastMoves = {null, null, null, null};
		BlokusPiece[] lm = controller.lastMove;
		for(int m = 0; m < lm.length; m++){
			if(lm[m] != null) {
				BlokusPiece b = lm[m];
				int[] loc = board.getBoardLocation(b.getX(), b.getY());
				lastMoves[m][0] = controller.parent.getPieceIndex(b.getID());
				lastMoves[m][1] = b.getDirection();
				lastMoves[m][2] = loc[0];
				lastMoves[m][3] = loc[1];
				lastMoves[m][4] = b.getColorID();
			} else {
				lastMoves[m][0] = -1;
				lastMoves[m][1] = 0;
				lastMoves[m][2] = -1;
				lastMoves[m][3] = -1;
				lastMoves[m][4] = -1;
			}
		}
		boolean[] newRoot = {false, false, false};
		for(int i = 0; i<3; i++){ 
			int t = color + i % 4;
			for(int c = 0; c<root.children.size(); c++) {
				if(root.moveList.get(c) == lastMoves[t]) {
					root = root.children.get(c);
					newRoot[i] = true;
					break;
				}
			}
			if (!newRoot[i]){
				System.err.println("Couldn't find correct branch for opponent move");
				return;
			}
		}

		//go down to leaves and do leafToNode for another 3 plies
		int p = 3;
		if(plies < 3){
			p = plies;
		}
		ArrayList<Node> leaves = getLeaves(root);
		ArrayList<Node> parents = new ArrayList<Node>();
		for(int i = 0; i< leaves.size(); i++){
			Node parent = leaves.get(i).parent;
			Node newNode = leafToNode(leaves.get(i), p);
			if (!parents.contains(parent)) {
				parents.add(parent);
				parent.children = new ArrayList<Node>();
			}
			parent.children.add(newNode);
		}
	}

	private Node leafToNode(Node leafRoot, int plies) {
		if(plies == 0) {
			return leafRoot;
		}
		if (!leafRoot.isLeaf) {
			System.err.println("Node passed to leafToNode is not leaf");
			return null;
		}
		if(plies >3) {
			System.out.println("In leafToNode with " + plies + " plies");
		}
		ArrayList<ArrayList<Integer[]>> moves = findAllMoves(leafRoot.state, leafRoot.corners);
		ArrayList<Node> children = new ArrayList<Node>(moves.get(leafRoot.color).size());
		ArrayList<Integer[]> colorMoves = moves.get(leafRoot.color);
		ArrayList<ArrayList<Integer[]>> newCorners;

		for(int i = 0; i < colorMoves.size(); i++) {
			Integer[] move = colorMoves.get(i);
			int[][] newState = getNewState(move, leafRoot.state);
			newCorners = newCorners(move, newState, leafRoot.state);

			children.add(leafToNode(new LeafNode(null, newState, newCorners, (i + 1)%4, this.color), plies - 1));
		}
		Node newNode = new Node(leafRoot.parent, leafRoot.state, children, colorMoves, leafRoot.color, this.color);
		for(int i=0; i< newNode.children.size(); i++){
			newNode.children.get(i).parent = newNode;
		}

		return newNode;
	}

	private ArrayList<Node> getLeaves(Node parent) {
		System.out.println("You should maybe do this iteratively");
		if(parent.isLeaf) {
			ArrayList<Node> result = new ArrayList<Node>();
			result.add(parent);
			return result;
		}
		else {
			ArrayList<Node> nextLevel = new ArrayList<Node>();
			ArrayList<Node> children = parent.children;
			for(Node child:children) {
				for(Node next:child.children) {
					nextLevel.add(next);
				}
			}
			ArrayList<Node> result = new ArrayList<Node>();
			for(Node n:nextLevel) {
				ArrayList<Node> leaves = getLeaves(n);
				for(Node leaf:leaves){
					result.add(leaf);
				}
			}
			return result;
		}
	}


	private void generateTree(int plies, int[][] state) {
		if(plies <= 0) {
			System.err.println("Called generateTree with invalid plies: " + plies);
			return;
		}
		System.out.println("Generating tree with " + plies + " plies");
		LeafNode leafRoot = new LeafNode(null, state, this.color, this.color);
		root = leafToNode(leafRoot, plies);
	}

	private ArrayList<ArrayList<Integer[]>> newCorners(Integer[] move, int[][] newState, int[][] state) {
		//Make better later if have time
		//maybe won't
		return findCorners(newState);
	}

	/**
	 * generates new state
	 * @param integers
	 * @param oldState
	 * @return
	 */

	int[][] getNewState(Integer[] move, int[][] oldState) { //move[4] = player color
		//		System.out.println("color " + color + " in getNewState with move " + move[0] + ", " + move[1] + ", " + move[2] + ", " + move[3] + ", " + move[4] +
		//				" and oldstate " +  boardToString((oldState) + "\n");
		if(move.length != 5){
			System.err.println("Make sure you are passing move in correctly:\n" +
					"shapeId, direction, x, y, color");
			return null;
		}
		int[][] newState = new int[oldState.length][oldState[0].length];

		int[][] stateBoard = new int[20][20];
		for(int i = 0; i<20; i++) {
			for (int j = 0; j< 20; j++){
				stateBoard[i][j] = oldState[i][j];
				newState[i][j] = oldState[i][j];
			}
		}

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


		//		for (int i = 0; i < 4; i++){
		//			int num = oldState[20][i];
		//			if(i == move[4]){
		//				num -= Math.pow(2, move[0]);
		//			}
		//			newState[20][i] = num;
		//		}


		int[][] shape = getShape(move);
		int x, y;
		boolean cornerAdjacent = false;
		for(int i = 0; i< shape.length; i++){
			x = shape[i][0];
			y = shape[i][1];
			//			System.out.println("(" + x + ", " + y + ")");
			if(oldState[y][x] == -1 && !orthAdjacent(x, y, move[4], stateBoard)) {
				if(cornerAdjacent(x,y, move[4], stateBoard) || round == 0) {
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
			System.out.println("Trying to generate new state for invalid move: not corner adjacent");
			System.err.println(move[0] + ", " + move[1] + ", " + move[2] + ", " + move[3] + ", " + move[4]);
			return null;
		}
		return newState;
	}

	int[][] getShape(Integer[] move) {
		int[][] oldShape = controller.parent.rotate(controller.parent.flip(controller.parent.pieceShapes[move[0]], move[1]), move[1]);
		int[][] newShape = new int[oldShape.length][2];
		for(int i = 0; i< newShape.length; i++){
			newShape[i][0] = oldShape[i][0] + move[2];
			newShape[i][1] = oldShape[i][1] + move[3];
			if(newShape[i][0] > 20) {
				System.out.println("woop");
			}
		}
		return newShape;
	}


	/**
	 * Finds available corners where we can play
	 * @param state - board and scores
	 * @return board indices of corners for each player
	 */
	public ArrayList<ArrayList<Integer[]>>  findCorners(int[][] state){
		//check four corners to see if our color is in any of them. if not, return all of the corners that are empty. if so, proceed
		ArrayList<ArrayList<Integer[]>> freeSquares = new ArrayList<ArrayList<Integer[]>>(4);

		for(int i = 0; i<4; i++){
			freeSquares.add(i, new ArrayList<Integer[]>());
		}
		int x, y;
		for(int y0 = 0; y0<2; y0++) {
			for (int x0 = 0; x0<2; x0++) {
				y = y0 * 19;
				x = x0 * 19;

				for(int i = 0; i<4; i++){
					if (state[y][x] == -1) {
						freeSquares.get(i).add(new Integer[] {x,y});
					}
				}
			}
		}
		if(freeSquares.get(color).size() > 0) {
			return freeSquares;
		}
		freeSquares = new ArrayList<ArrayList<Integer[]>>(4);
		for(int i = 0; i<4; i++){
			freeSquares.add(i, new ArrayList<Integer[]>());
		}

		//Go through the whole board and for each color, see if a corner is adjacent to that color and not edge adjacent to same color
		for(y = 0; y<state.length-1; y++){
			for(x = 0; x<state.length-1; x++){
				for(int i = 0; i<4; i++){
					//check corner adjacent
					boolean corAdj = cornerAdjacent(x,y,i);
					//check orth adj
					boolean orthAdj = orthAdjacent(x,y,i);
					//if corner not check 
					if(corAdj && !orthAdj){
						freeSquares.get(i).add(new Integer[] {x,y});
					}
				}
			}
		}
		return freeSquares;
	}

	//Calculate moves given our hand
	public ArrayList<ArrayList<Integer[]>> findAllMoves(int[][] state, ArrayList<ArrayList<Integer[]>> allCorners){
		//For each player, find all possible moves
		//Return list of {pieceID index, x, y, direction (incl. flip)
		//Later we may include the heuristic value of the move


		//Calculates everyone's hand as a boolean array
		boolean[][] hands = new boolean[4][21];
		for (int i = 0; i<hands.length; i ++) {
			hands[i] = hexToHand(state[state.length-1][i]);
		}

		ArrayList<ArrayList<Integer[]>> possibleMoves = new ArrayList<ArrayList<Integer[]>>(4);
		for(int i = 0; i<4; i++){
			possibleMoves.add(i, new ArrayList<Integer[]>());
		}
		if(allCorners == null) {
			allCorners = findCorners(state);
		}
		for(int color=0; color<4; color++){
			for(int s=0; s<hands[color].length;s++){
				//calculate shape
				if(hands[color][s]){
					//					if(color == 3)
					//						System.out.println(controller.parent.pieceNames[s] + ": ");
					int[][] shape = controller.parent.getShape(controller.parent.pieceNames[s]);
					for(int f=-1; f<2; f+=2){
						if ((pieceDirections[s] == 3) || (pieceDirections[s] < 3 && f == -1)) {
							//this flips the shape
							for(int r=1; r<5;r++){
								if ((pieceDirections[s] > 1) || (pieceDirections[s] == 1 && (r == 1 || r == 2)) ||
										(pieceDirections[s] == 0 && r == 1)) {
									int d = f*r; //direction of piece
									int[][] spots = controller.parent.rotate(controller.parent.flip(shape, d), d);
									for(Integer[] cornerSlot : allCorners.get(color)) {
										for(int[] corner : spots){
											//need the new spots to be the actual board indices of where we want to place this piece
											int[][] newSpots = new int[spots.length][2];
											for(int block = 0; block < spots.length; block++) {
												newSpots[block][0] = spots[block][0] + cornerSlot[0] - corner[0];
												newSpots[block][1] = spots[block][1] + cornerSlot[1] - corner[1];
												if(newSpots[block][0] > 20) {
													//													System.out.println("(" + newSpots[block][0] + ", " + newSpots[block][1] + ") ");
													//													System.out.print("");
												}
											}
											if(legalMove(newSpots, color, state)) {
												for(int block = 0; block < newSpots.length; block++) {
													if(newSpots[block][0]  > 20 || newSpots[block][1] > 20) {
														//														System.out.println("(" + newSpots[block][0] + ", " + newSpots[block][1] + ") ");
														//														System.out.print("");
													}
												}
												//use spots to find 0,0 spot on board
												int x = newSpots[0][0] - spots[0][0];
												int y = newSpots[0][1] - spots[0][1];
												Integer[] move = {s, d, x, y, color};
												possibleMoves.get(color).add(move);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		// find the shape of each available piece, check it in each free corner
		// for that color, and then try that in every direction and flip (ew)

		// To "check it in each free corner" we should probably write our own "legalMove" function. 
		// Maybe copy it. Maybe just use it.
		//for each shape, flip, rotation, corner

		return possibleMoves;
	}

	int heuristic(int[][] state){
		int[] scores = {0,0,0,0};
		boolean[][] hands = {hexToHand(state[20][0]), hexToHand(state[20][1]), hexToHand(state[20][2]), hexToHand(state[20][3])}; 
		for(int i = 0; i<hands.length; i++) {
			scores[i] = 85;
			for(int j = 0; j<hands[i].length; j++) {
				if(hands[i][j]) {
					scores[i] -= controller.parent.pieceShapes[i].length;
				}
			}
		}
		int sum = 0;
		int me;
		for(int i = 0; i<4; i++){
			me = (i==color)?1:-1;
			sum += me * scores[i];
		}
		return sum;
	}

	//Next move function
	//Get new state
	//get all children
	//heuristic
	//node
	//leafnode
	/**
	 * Takes in a point and a color
	 * Returns true if the points orthogonally adjacent to it are that color and 
	 * the point we passed in has not been used yet
	 * @param x1
	 * @param y1
	 * @param c
	 * @return
	 */
	private boolean orthAdjacent(int x1, int y1, int c){
		//check left right
		for(int y=-1; y<2; y+=2){
			if(y1 + y <= 19 && y1 + y >= 0 && board.simpleBoard[y1+y][x1] == c && board.simpleBoard[y1][x1] == -1){
				return true;
			}
		}
		//check up down
		for(int x=-1; x<2; x+=2){
			if(x1 + x <= 19 && x1 + x >= 0 && board.simpleBoard[y1][x1+x] == c && board.simpleBoard[y1][x1] == -1){
				return true;
			}
		}
		return false;
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
	 * Takes in a point and a color
	 * Returns true if the points adjacent to its corners are that color and 
	 * the point we passed in has not been used yet
	 * @param x1
	 * @param y1
	 * @param c
	 * @return
	 */
	private boolean cornerAdjacent(int x1, int y1, int c){
		for(int y=-1; y<2; y+=2){
			for(int x=-1; x<2; x+=2){
				if(y1 + y <= 19 && y1 + y >= 0 && x1 + x <=19 && x1 + x >= 0 && board.simpleBoard[y1+y][x1+x] == c && board.simpleBoard[y1][x1] == -1){
					return true;
				}
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

	private boolean legalMove(int[][] spots, int color, int[][] sb){
		//go through each spot, see that at least one of them is corner adjacent (do we need this?)
		//see if it is overlapping any color or if it is orthogonally adjacent to its own color
		for(int[] spot : spots){
			if(spot[0] < 0 || spot[0] > 19 || spot[1] < 0 || spot[1] > 19) {
				return false;
			}
			else if(sb[spot[1]][spot[0]] != -1){
				return false;
			}else if(orthAdjacent(spot[0], spot[1], color)){
				return false;
			}
		}
		return true;
	}

	public int[] generateHexHands(BlokusPiece[][] hands) {
		boolean[][] boolHands = new boolean[4][21];
		int[] hexHands = new int[4];

		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 21; j++) {
				boolHands[i][j] = !hands[i][j].isPlayed();
			}
			hexHands[i] = handToHex(boolHands[i]);
		}
		return hexHands;
	}

	// given a board (simpleBoard), and the hands in hex format, return
	// the proper state.
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

	// only call on first turn. Sets the instance variable corner
	// to be the id of the corner we started at.
	private void setCorner(int[][] state) {
		ArrayList<Integer[]> corners = findCorners(state).get(color);
		if(corners.size() > 4 || corners.size() == 0) {
			System.err.println("Improper setCorner call. Maybe it's not the first turn?");
			return;
		}
		if (corners.get(0)[0] == 0) {
			if(corners.get(0)[1] == 0) {
				corner = 0;
				return;
			} else if (corners.get(0)[1] == 19){
				corner = 1;
				return;
			}
		} else if (corners.get(0)[0] == 19){
			if(corners.get(0)[1] == 0) {
				corner = 2;
				return;
			} else if (corners.get(0)[1] == 19) {
				corner = 3;
				return;
			}
		}
		System.err.println("Improper setCorner call. Maybe it's not the first turn?");
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
		if(handString.length() > 21) {
			System.out.println("whoops");
		}
		for(int i=0; i<handString.length();i++){
			hand[i] = (handString.charAt(i) == '0')?false:true;
		}
		return hand;
	}

	public int getColor() {
		return color;
	}

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


	class Node {
		int[][] state;
		ArrayList<Node> children;
		ArrayList<Integer[]> moveList;
		int value;
		int color;
		int parentColor;
		boolean isLeaf = false;
		Node parent;
		ArrayList<ArrayList<Integer[]>> corners = null;
		Node bestChild = null;


		Node(Node parent, int[][] state, ArrayList<Node> children, ArrayList<Integer[]> moveList, int color, int parentColor) {
			this.parent = parent;
			this.state = state;
			this.children = children;
			this.moveList = moveList;
			this.color = color;
			this.parentColor = parentColor;
			this.value = calcValue();
		}

		Node(Node parent, int[][] state, int color, int parentColor) {
			this.parent = parent;
			this.state = state;
			this.children = new ArrayList<Node>();
			this.color = color;
			this.parentColor = parentColor;
			this.value = 0;
		}

		int calcValue(){
			if(children == null || children.size() == 0) {
				return 0;
			}
			int min = children.get(0).value;
			int max = children.get(0).value;
			Node minChild = children.get(0);
			Node maxChild = children.get(0);

			for(int i = 0; i < children.size(); i++){
				int val = children.get(i).value;
				if(min>val) {
					min = val;
					minChild = children.get(i);
				}
				if(max<val) {
					max = val;
					maxChild = children.get(i);
				}
			}
			this.bestChild = (this.color == this.parentColor)?maxChild:minChild;
			return (this.color==this.parentColor)?max:min;
		}

		void addChild(Node child, Integer[] move){
			children.add(child);
			moveList.add(move);
		}

		void passUpValue() {
			if(parent != null){
				parent.addValue(this);
			}
		}

		public void addValue(Node child) {
			boolean valueChanged = false;
			if(this.color == this.parentColor && child.value > this.value) {
				this.bestChild = child;
				this.value = child.value;
				valueChanged = true;
			} else if (this.color != this.parentColor && child.value < this.value){
				this.bestChild = child;
				this.value = child.value;
				valueChanged = true;
			}
			passUpValue();
		}
	}

	class LeafNode extends Node{

		LeafNode(Node parent, int[][] state, ArrayList<ArrayList<Integer[]>> corners, int color, int parentColor) {
			super(parent, state, null, null, color, parentColor);
			this.corners = corners;
			this.value = heuristic(state);
			this.isLeaf = true;
			passUpValue();
		}

		LeafNode(Node parent, int[][] state, int color, int parentColor) {
			super(parent, state, null, null, color, parentColor);
			this.corners = findCorners(state);
			this.isLeaf = true;
			this.value = heuristic(state);
			passUpValue();
		}

		@Override
		void addChild(Node child, Integer[] move) {
			System.err.println("A leaf not should not have children");
			return;
		}

	}

}
