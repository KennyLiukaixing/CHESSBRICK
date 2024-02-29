import java.util.*;


public class Board {

	float score = 0;
	Piece board[][] = new Piece[8][8];
	ArrayList<Piece> onBoard = new ArrayList<>();
	public int fiftyMove;

	public Board() {
		fiftyMove = 0;
	}

	public Board(Board original) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Piece originalPiece = original.board[i][j];
				if (originalPiece != null) {
					Piece newPiece = null;
					// Clone the piece based on its type
					if (originalPiece instanceof Rook) {
						newPiece = new Rook(originalPiece.xPos, originalPiece.yPos, originalPiece.tag, this);
					} else if (originalPiece instanceof Knight) {
						newPiece = new Knight(originalPiece.xPos, originalPiece.yPos, originalPiece.tag, this);
					} else if (originalPiece instanceof Bishop) {
						newPiece = new Bishop(originalPiece.xPos, originalPiece.yPos, originalPiece.tag, this);
					} else if (originalPiece instanceof Queen) {
						newPiece = new Queen(originalPiece.xPos, originalPiece.yPos, originalPiece.tag, this);
					} else if (originalPiece instanceof King) {
						newPiece = new King(originalPiece.xPos, originalPiece.yPos, originalPiece.tag, this);
					} else if (originalPiece instanceof Pawn) {
						newPiece = new Pawn(originalPiece.xPos, originalPiece.yPos, originalPiece.tag, this);
					}
					if (newPiece != null) {
						newPiece.moved = originalPiece.moved;
						this.board[i][j] = newPiece;
						this.onBoard.add(newPiece);
					}
				}
			}
		}
		this.fiftyMove = original.fiftyMove;
	}

	public Board boardWithMove(Piece p, DeltaMovement d) {
		Board bo = new Board(this);
		bo.getPiece(p.xPos, p.yPos).forceMove(d.dx, d.dy);

		return bo;
	}

	
	public float eval() {
		float eval = 0;
		if (gameEnd(true) == 1) {
			eval = -80;
		} else if (gameEnd(false) == -1) {
			eval = 80;
		}
		for (int i = 0; i < onBoard.size(); i++) {
			Piece p = onBoard.get(i);
			int sign;

			if (Piece.isWhite(p.tag)) {
				sign = 1;
			} else {
				sign = -1;
			}

			eval += sign * p.mat;

			for (int j = 0; j < p.legalNoCheck().size(); j++) {
				if (Game.unNull(board[p.legalNoCheck().get(j).dx][p.legalNoCheck().get(j).dy])) {
					eval += sign * 0.05 * board[p.legalNoCheck().get(j).dx][p.legalNoCheck().get(j).dy].mat;
				} else {
					eval += sign * 0.05;
				}
			}
			
		}
		return eval;
	}

	public DeltaMovement miniMax2() {
		ArrayList<ArrayList<DeltaMovement>> moves = allMoves(false);
		ArrayList<ArrayList<Float>> evals = new ArrayList<>(); // Initialize the evals ArrayList
	
		// Populate the evals ArrayList
		for (int i = 0; i < moves.size(); i++) {
			evals.add(new ArrayList<Float>()); // Initialize inner ArrayList<Float>
			for (int j = 0; j < moves.get(i).size(); j++) {
				// Populate the inner ArrayList<Float> with float values
				evals.get(i).add(recurse(boardWithMove(black().get(i), moves.get(i).get(j)), 2, true, -Float.MAX_VALUE, Float.MAX_VALUE));
			}
		}
	
		int[] minimum = minimum(evals); 
		DeltaMovement move = moves.get(minimum[0]).get(minimum[1]);
		move.p = black().get(minimum[0]);
		return move;
	}
	

	public int[] minimum(ArrayList<ArrayList<Float>> moves) {
		float[] mins = new float[moves.size()];
		int[] indexes = new int[moves.size()];
	
		for (int i = 0; i < moves.size(); i++) {
			if (moves.get(i).isEmpty()) {
				mins[i] = Float.MAX_VALUE; // Set to a large value
				indexes[i] = -1;
			} else {
				float localMin = Float.MAX_VALUE; // Initialize to a large value
				int index = -1;
	
				for (int j = 0; j < moves.get(i).size(); j++) {
					if (moves.get(i).get(j) < localMin) {
						localMin = moves.get(i).get(j);
						index = j;
					}
				}
	
				mins[i] = localMin;
				indexes[i] = index;
			}
		}
	
		float largeMin = Float.MAX_VALUE; // Initialize to a large value
		int index = -1;
	
		for (int i = 0; i < mins.length; i++) {
			if (mins[i] < largeMin && indexes[i] != -1) {
				largeMin = mins[i];
				index = i;
			}
		}
	
		// Return the array of indexes
		System.out.println(moves.get(index).get(indexes[index]));
		return new int[]{index, indexes[index]};
	}
	

	public float recurse(Board b, int depth, boolean isWhiteTurn, float alpha, float beta) {
		if (depth == 0) {
			return b.eval();
		} else {
			boolean shouldBreak = false;
			ArrayList<ArrayList<DeltaMovement>> moves = b.allMoves(isWhiteTurn);
			if(isWhiteTurn){
				float value = -Float.MAX_VALUE;
				for (int i = 0; i < moves.size(); i++) {
					if(shouldBreak) break;
					ArrayList<DeltaMovement> pieceMoves = moves.get(i);
					for (DeltaMovement move : pieceMoves) {
						if(shouldBreak) break;
						/*Board newBoard;
						newBoard = b.boardWithMove(b.white().get(i), move);*/
						Piece p = b.white().get(0);

						value = Math.max(recurse(b.makeMove(p.xPos,p.yPos,move.dx,move.dy), depth - 1, false, alpha, beta), value);

						alpha = Math.max(alpha,value);

						if(value>beta) shouldBreak = true;
					}
				}
				return value;
			}
			else {
				float value = Float.MAX_VALUE;
				for (int i = 0; i < moves.size(); i++) {
					if(shouldBreak) break;
					ArrayList<DeltaMovement> pieceMoves = moves.get(i);
					for (DeltaMovement move : pieceMoves) {
						if(shouldBreak) break;
						//Board newBoard;
						//newBoard = b.boardWithMove(b.black().get(i), move);
						Piece p = b.black().get(0);

						value = Math.min(recurse(b.makeMove(p.xPos,p.yPos,move.dx,move.dy), depth - 1, true, alpha, beta), value);

						beta = Math.min(beta,value);

						if(value<alpha) shouldBreak = true;
					}
				}
				return value;
			}			
		}
	}
	
	public Board makeMove(int origX, int origY, int tgtX, int tgtY){
		Piece p = board[origX][origY];
		p.forceMove(tgtX, tgtY);
		return this;
	}

	public ArrayList<ArrayList<DeltaMovement>> allMoves(boolean isWhite) {
		ArrayList<ArrayList<DeltaMovement>> moves = new ArrayList<>();
		for (int i = 0; i < onBoard.size(); i++) {
			Piece p = onBoard.get(i);
			if (Piece.isWhite(p.tag) == isWhite) {
				moves.add(p.legalNoCheck());
			}
		}
		return moves;
	}
	
	public int gameEnd(boolean isPlayerMove) {
		if (isPlayerMove) {
			for (Piece p : onBoard) {
				if (Piece.isWhite(p.tag) && p.legalNoCheck().size() > 0) {
					return 2;
				}
			}
			DeltaMovement kingPos = getKingPos('W');
			if (Piece.isKingUnderAttack('W', kingPos.dx, kingPos.dy, this)) {
				return 1;
			} else {
				return 0;
			}

		} else {
			for (Piece p : onBoard) {
				if (!Piece.isWhite(p.tag) && p.legalNoCheck().size() > 0) {
					return 2;
				}
			}
			DeltaMovement kingPos = getKingPos('B');
			if (Piece.isKingUnderAttack('B', kingPos.dx, kingPos.dy, this)) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	public void makeDefault() {
		board[0][7] = new Rook(0, 7, 'r', this);
		board[7][7] = new Rook(7, 7, 'r', this);
		board[0][0] = new Rook(0, 0, 'R', this);
		board[7][0] = new Rook(7, 0, 'R', this);

		board[1][7] = new Knight(1, 7, 'n', this);
		board[6][7] = new Knight(6, 7, 'n', this);
		board[1][0] = new Knight(1, 0, 'N', this);
		board[6][0] = new Knight(6, 0, 'N', this);

		board[2][7] = new Bishop(2, 7, 'b', this);
		board[5][7] = new Bishop(5, 7, 'b', this);
		board[2][0] = new Bishop(2, 0, 'B', this);
		board[5][0] = new Bishop(5, 0, 'B', this);

		board[3][7] = new Queen(3, 7, 'q', this);
		board[4][7] = new King(4, 7, 'k', this);
		board[3][0] = new Queen(3, 0, 'Q', this);
		board[4][0] = new King(4, 0, 'K', this);

		board[0][1] = new Pawn(0, 1, 'P', this);
		board[1][1] = new Pawn(1, 1, 'P', this);
		board[2][1] = new Pawn(2, 1, 'P', this);
		board[3][1] = new Pawn(3, 1, 'P', this);
		board[4][1] = new Pawn(4, 1, 'P', this);
		board[5][1] = new Pawn(5, 1, 'P', this);
		board[6][1] = new Pawn(6, 1, 'P', this);
		board[7][1] = new Pawn(7, 1, 'P', this);

		board[0][6] = new Pawn(0, 6, 'p', this);
		board[1][6] = new Pawn(1, 6, 'p', this);
		board[2][6] = new Pawn(2, 6, 'p', this);
		board[3][6] = new Pawn(3, 6, 'p', this);
		board[4][6] = new Pawn(4, 6, 'p', this);
		board[5][6] = new Pawn(5, 6, 'p', this);
		board[6][6] = new Pawn(6, 6, 'p', this);
		board[7][6] = new Pawn(7, 6, 'p', this);

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] != null)
					onBoard.add(board[i][j]);
			}
		}
	}

	public boolean isEmpty(int x, int y) {
		if (x >= 0 && y >= 0)
			return board[x][y] == null;
		else
			return false;
	}

	public Piece getPiece(int x, int y) {
		if (x >= 0 && y >= 0)
			return board[x][y];
		return null;
	}

	public boolean sameSide(Piece piece, Piece piece2) {
		if (piece.tag == 'k' || piece.tag == 'q' || piece.tag == 'n' ||
				piece.tag == 'p' || piece.tag == 'b' || piece.tag == 'r') {
			if (piece2.tag == 'K' || piece2.tag == 'Q' || piece2.tag == 'N' ||
					piece2.tag == 'P' || piece2.tag == 'B' || piece2.tag == 'R') {
				return false;
			}
		}
		if (piece2.tag == 'k' || piece2.tag == 'q' || piece2.tag == 'n' ||
				piece2.tag == 'p' || piece2.tag == 'b' || piece2.tag == 'r') {
			if (piece.tag == 'K' || piece.tag == 'Q' || piece.tag == 'N' ||
					piece.tag == 'P' || piece.tag == 'B' || piece.tag == 'R') {
				return false;
			}
		}
		return true;
	}

	public void remove(Piece piece) {
		onBoard.remove(piece);
	}

	public void replace(int tgtx, int tgty, Piece p, int oldx, int oldy) {
		if (this.getPiece(tgtx, tgty) != null)
			remove(this.getPiece(tgtx, tgty));
		board[tgtx][tgty] = p;
		board[oldx][oldy] = null;
	}

	public DeltaMovement getKingPos(char WorB) {
		for (Piece p : onBoard) {
			if ((p.tag == 'k' && WorB == 'W') || (p.tag == 'K' && WorB == 'B')) {
				return new DeltaMovement(p.xPos, p.yPos);
			}
		}
		return null;
	}
	public int blackPieces() {
		int counter = 0;
		for (int i = 0; i < onBoard.size(); i++) {
			Piece p = onBoard.get(i);
			if (!Piece.isWhite(p.tag)) {
				counter++;
			}
		}
		return counter;
	}

	public ArrayList<Piece> black() {
		ArrayList<Piece> black = new ArrayList<>();
		for (int i = 0; i < onBoard.size(); i++) {
			Piece p = onBoard.get(i);
			if (!Piece.isWhite(p.tag)) {
				black.add(p);
			}
		}
		return black;
	}

	public ArrayList<Piece> white() {
		ArrayList<Piece> white = new ArrayList<>();
		for (int i = 0; i < onBoard.size(); i++) {
			Piece p = onBoard.get(i);
			if (Piece.isWhite(p.tag)) {
				white.add(p);
			}
		}
		return white;
	}

	public int whitePieces() {
		return onBoard.size() - blackPieces();
	}
}
