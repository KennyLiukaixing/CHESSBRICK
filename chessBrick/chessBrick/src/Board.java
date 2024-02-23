import java.util.ArrayList;

public class Board {

	float score = 0;
	Piece board[][] = new Piece[8][8];
	ArrayList<Piece> onBoard = new ArrayList<>();

	public Board() {

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
	}
	public Board boardWithMove(Piece p, DeltaMovement d){
		Board bo = new Board(this);
		//System.out.println(p.xPos+" "+p.yPos+"/"+d.dx+" "+d.dy);
		//Game.printTestBoard(bo);
		bo.getPiece(p.xPos,p.yPos).forceMove(d.dx,d.dy);
		
		return bo;
	}

	public DeltaMovement getBestMove(boolean isWhiteTurn) {
		DeltaMovement bestMove = null;
		double max = -10000;
		int bestx = 0;
		int besty = 0;
		for (int i = 0; i < onBoard.size(); i++) {
			Piece p = onBoard.get(i);
			if (!isWhiteTurn) {
				if (!Piece.isWhite(p.tag)) {
					for (DeltaMovement dm : p.legalNoCheck()) {
						Board temp = new Board(this);// Deep copy board
						Piece tPiece = temp.getPiece(p.xPos, p.yPos);// get temp piece from board
						// save coordinates for the piece
						int tempx, tempy;
						tempx = p.xPos;
						tempy = p.yPos;
						tPiece.forceMove(dm.dx, dm.dy);
						// change board for evaluation
						if (temp.evalPosition(false) >= max) {
							// save board info
							bestMove = dm;
							bestx = tempx;
							besty = tempy;
							max = temp.evalPosition(false);
							// printBoard(temp);
							// System.out.println(bestx+" "+besty+" "+temp.evalBoard());
						}
					}
				}
			} else {
				if (Piece.isWhite(p.tag)) {
					for (DeltaMovement dm : p.legalNoCheck()) {
						Board temp = new Board(this);// Deep copy board
						Piece tPiece = temp.getPiece(p.xPos, p.yPos);// get temp piece from board
						// save coordinates for the piece
						int tempx, tempy;
						tempx = p.xPos;
						tempy = p.yPos;
						tPiece.forceMove(dm.dx, dm.dy);
						// change board for evaluation
						if (temp.evalPosition(true) >= max) {
							// save board info
							bestMove = dm;
							bestx = tempx;
							besty = tempy;
							max = temp.evalPosition(true);
							// printBoard(temp);
							// System.out.println(bestx+" "+besty+" "+temp.evalBoard());
						}
					}
				}
			}
		}
		return new DeltaMovement(bestMove.dx,bestMove.dy,this.getPiece(bestx,besty),this.evalPosition(isWhiteTurn));
		//this.getPiece(bestx, besty).forceMove(bestMove.dx, bestMove.dy);
	}

	public float evalPosition(boolean isWhiteTurn) {
		float matStat = 0;
		for (int j = 0; j < onBoard.size(); j++) {
			Piece p = onBoard.get(j);
			double fileWeights[] = { 1, 1, 1.1, 1.3, 1.5, 1.3, 1.1, 1 };
			if(!isWhiteTurn){
				if (Piece.isWhite(p.tag)) {
					matStat -= p.mat * fileWeights[p.xPos];
					for (int i = 0; i < p.legalMoves().size(); i++) {
						DeltaMovement d = p.legalMoves().get(i);
						if (d.ext) {
							if (isWhiteTurn)
								matStat -= 0.3 * board[d.dx][d.dy].mat;
							else
								matStat -= 0.05 * board[d.dx][d.dy].mat;
						} else {
							matStat -= 0.1 * p.mat;
						}
					}
				} else {
					matStat += p.mat * fileWeights[p.xPos];
					for (int i = 0; i < p.legalMoves().size(); i++) {
						DeltaMovement d = p.legalMoves().get(i);
						if (d.ext) {
							if (!isWhiteTurn)
								matStat += 0.3 * board[d.dx][d.dy].mat;
							else
								matStat += 0.05 * board[d.dx][d.dy].mat;
						} else {
							// System.out.println(p + " " + d.dx + " " + d.dy);
							matStat += 0.1 * p.mat;
						}
					}
				}
			}
			else{
				if (!Piece.isWhite(p.tag)) {
					matStat -= p.mat * fileWeights[p.xPos];
					for (int i = 0; i < p.legalMoves().size(); i++) {
						DeltaMovement d = p.legalMoves().get(i);
						if (d.ext) {
							if (isWhiteTurn)
								matStat -= 0.3 * board[d.dx][d.dy].mat;
							else
								matStat -= 0.05 * board[d.dx][d.dy].mat;
						} else {
							matStat -= 0.1 * p.mat;
						}
					}
				} else {
					matStat += p.mat * fileWeights[p.xPos];
					for (int i = 0; i < p.legalMoves().size(); i++) {
						DeltaMovement d = p.legalMoves().get(i);
						if (d.ext) {
							if (!isWhiteTurn)
								matStat += 0.3 * board[d.dx][d.dy].mat;
							else
								matStat += 0.05 * board[d.dx][d.dy].mat;
						} else {
							// System.out.println(p + " " + d.dx + " " + d.dy);
							matStat += 0.1 * p.mat;
						}
					}
				}
			}
		}
		// System.out.println(matStat);
		return matStat;
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
	//DeltaMovement can store piece, xy target, score
	public DeltaMovement miniMax(Board b, int depth, boolean isWhiteTurn){
		if(depth == 0) return getBestMove(isWhiteTurn);
		else{
			if(isWhiteTurn){
				float minEval = 10000;
				ArrayList<DeltaMovement> moves = new ArrayList<>();
				for(Piece p:onBoard){
					if(Piece.isWhite(p.tag)){
						for(DeltaMovement d:p.legalNoCheck()){
							moves.add(new DeltaMovement(d.dx, d.dy, p, 0));
						}	
					}
				}
				for(DeltaMovement m:moves){
					Board newBoard = this.boardWithMove(m.p, m);
					float eval = miniMax(newBoard, depth-1,false).score;
					if(eval<minEval){
						minEval = eval;
					}
				}//I cannot express with words how disgusting this is
				return new DeltaMovement(0,0,null,minEval);
			}
			else{
				float maxEval = -10000;
				ArrayList<DeltaMovement> moves = new ArrayList<>();
				for(Piece p:onBoard){
					if(!Piece.isWhite(p.tag)){
						for(DeltaMovement d:p.legalNoCheck()){
							moves.add(new DeltaMovement(d.dx, d.dy, p, 0));
						}	
					}
				}
				for(DeltaMovement m:moves){
					Board newBoard = this.boardWithMove(m.p, m);
					float eval = miniMax(newBoard, depth-1,true).score;
					if(eval>maxEval){
						maxEval = eval;
					}
				}//I cannot express with words how nasty this is
				return new DeltaMovement(0,0,null,maxEval);
			}
		}
	}
	// WHITE: r
	// BLACK: R
	// Something broke here but ima leave it as is because I dont want to fix it
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
}
