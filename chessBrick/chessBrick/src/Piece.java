import java.util.ArrayList;

public abstract class Piece {
	public int mat;
	public boolean moved = false;
	public char tag;
	public Board board;
	public ArrayList<DeltaMovement> moves = new ArrayList<DeltaMovement>();
	public ArrayList<DeltaMovement> captureMoves = new ArrayList<DeltaMovement>();
	public ArrayList<DeltaMovement> legalNoCheck = legalNoCheck();

	int xPos, yPos;
	boolean isPlayerSide = false;

	public Piece(int x, int y, char tag, Board board) {
		this.xPos = x;
		this.yPos = y;
		this.tag = tag;
		this.board = board;
		if (tag == 'p' || tag == 'k' || tag == 'n' || tag == 'q' || tag == 'b' || tag == 'r')
			isPlayerSide = true;
	}

	public void forceMove(int Tgtx, int Tgty) {
		board.replace(Tgtx, Tgty, this, this.xPos, this.yPos);
		this.xPos = Tgtx;
		this.yPos = Tgty;
		moved = true;
	}

	public boolean makeMovePlayer(int Tgtx, int Tgty) {
		legalNoCheck = legalNoCheck();
		boolean success = false;
		if (this.isPlayerSide) {
			for (DeltaMovement d : this.legalNoCheck()) {
				if (d.dx == Tgtx && d.dy == Tgty) {
					success = true;

					if (board.board[Tgtx][Tgty] != null) {
						board.fiftyMove = 0;
					}
					else if (this.tag == 'p' || this.tag == 'P') {
						board.fiftyMove = 0;
					} else {
						board.fiftyMove++;
					}
					
					board.replace(Tgtx, Tgty, this, this.xPos, this.yPos);
					xPos = d.dx;
					yPos = d.dy;
					moved = true;

					if (d.castle == true) {
						if (d.dx == 2) {
							board.board[0][7].forceMove(3, 7);
						} else {
							board.board[7][7].forceMove(5, 7);
						}
					}
				}
			}
		}
		return success;
	}

	public void makeMoveAI(int Tgtx, int Tgty) {
		// Handle AI side castling
	}

	// is this move legal if nothing else is on the board
	// esoteric use of deltaMovement class, returns coordinates
	// uses ext to denote removal ////IMPORTANT////
	public ArrayList<DeltaMovement> legalMoves() {
		ArrayList<DeltaMovement> legals = new ArrayList<>();

		if (this.tag == 'k' || this.tag == 'n'
				|| this.tag == 'K' || this.tag == 'N') {
			for (DeltaMovement d : moves) {
				if (xPos + d.dx < 0 || xPos + d.dx > 7 || yPos + d.dy < 0 || yPos + d.dy > 7) {
					// do nothing
				} else {
					if (board.isEmpty(xPos + d.dx, yPos + d.dy))
						legals.add(
								new DeltaMovement(xPos + d.dx, yPos + d.dy));
					else {
						if (!board.sameSide(this,
								board.getPiece(xPos + d.dx, yPos + d.dy))) {
							// board.remove(board.getPiece(xPos + d.dx, yPos + d.dy));
							legals.add(new DeltaMovement(xPos + d.dx, yPos + d.dy, true, false));
						}
					}
				}
			}
			if (this.tag == 'k' && !this.moved) {
				if (board.board[0][7] != null) {
					if (board.board[0][7].tag == 'r' && !board.board[0][7].moved) {
						if (board.board[1][7] == null &&
								board.board[2][7] == null &&
								board.board[3][7] == null) {
							legals.add(new DeltaMovement(2, 7, false, true));
						}
					}
				}
				if (board.board[7][7] != null) {
					if (board.board[7][7].tag == 'r' && !board.board[7][7].moved) {
						if (board.board[5][7] == null &&
								board.board[6][7] == null) {
							legals.add(new DeltaMovement(6, 7, false, true));
						}
					}
				}
			}

			if (this.tag == 'K' && !this.moved) {
				if (board.board[0][0] != null) {
					if (board.board[0][0].tag == 'R' && !board.board[0][0].moved) {
						if (board.board[1][0] == null &&
								board.board[2][0] == null &&
								board.board[3][0] == null) {
							legals.add(new DeltaMovement(2, 0, false, true));
						}
					}
				}
				if (board.board[7][0] != null) {
					if (board.board[7][0].tag == 'R' && !board.board[7][0].moved) {
						if (board.board[5][0] == null &&
								board.board[6][0] == null) {
							legals.add(new DeltaMovement(6, 0, false, true));
						}
					}
				}
			}
			return legals;
		} else if (this.tag == 'P' || this.tag == 'p') { // handles pawn movement
			for (DeltaMovement d : moves) {
				if (xPos + d.dx < 0 || xPos + d.dx > 7 || yPos + d.dy < 0 || yPos + d.dy > 7) {
					// do nothing
				} else if (yPos + d.dy == 7 || yPos + d.dy == 0) {
					// promote();
				} else {
					if (board.isEmpty(xPos + d.dx, yPos + d.dy))
						legals.add(
								new DeltaMovement(xPos + d.dx, yPos + d.dy));
					if (moved == false) {
						if (board.isEmpty(xPos + d.dx * 2, yPos + d.dy * 2)) {
							legals.add(
									new DeltaMovement(xPos + d.dx * 2, yPos + d.dy * 2));
						}
					}
				}
			}
			for (DeltaMovement d : captureMoves) {
				if (!(xPos + d.dx < 0 || xPos + d.dx > 7 || yPos + d.dy < 0 || yPos + d.dy > 7)) {
					if (!board.isEmpty(xPos + d.dx, yPos + d.dy) &&
							(!board.sameSide(this, board.board[xPos + d.dx][yPos + d.dy]))) {
						legals.add(
								new DeltaMovement(xPos + d.dx, yPos + d.dy));
						if (yPos + d.dy == 7 || yPos + d.dy == 0) {
						}
						// promote();
					}
				}
			}
			return legals;
		} else {
			for (DeltaMovement d : moves) {
				for (int i = 1; i < 8; i++) {
					if (xPos + d.dx * i < 0 || xPos + d.dx * i > 7 || yPos + d.dy * i < 0 || yPos + d.dy * i > 7)
						break;
					if (!board.isEmpty(xPos + d.dx * i, yPos + d.dy * i)) {
						if (board.sameSide(this,
								board.getPiece(xPos + d.dx * i, yPos + d.dy * i))) {
							break;
						} else {
							legals.add(new DeltaMovement(xPos + d.dx * i, yPos + d.dy * i, true, false));
							break;
						}
					}

					else {
						legals.add(new DeltaMovement(xPos + d.dx * i, yPos + d.dy * i));
					}
				}
			}
			return legals;
		}
	}

	public ArrayList<DeltaMovement> legalNoCheck() {

		ArrayList<DeltaMovement> legals = new ArrayList<>();
		ArrayList<DeltaMovement> possibles = legalMoves();

		for (DeltaMovement move : possibles) {
			// Create a temporary board to simulate the move
			Board temp = new Board(board); // Assuming you have a constructor to create a deep copy of the board

			// Apply the move to the temporary board
			temp.board[xPos][yPos].forceMove(move.dx, move.dy);

			if (isWhite(this.tag)) {

				DeltaMovement kingPos = temp.getKingPos('W');
				if (!isKingUnderAttack('W', kingPos.dx, kingPos.dy, temp)) {
					legals.add(move);
				} else {
				} // System.out.println(kingPos.dx+" "+kingPos.dy);

			} else {

				DeltaMovement kingPos = temp.getKingPos('B');
				if (!isKingUnderAttack('B', kingPos.dx, kingPos.dy, temp)) {
					legals.add(move);
				}
			}

		}
		for (int j = 0; j < legals.size(); j++) {
			legals.get(j).p = this;
		}
		return legals;
	}

	public static boolean isKingUnderAttack(char WorB, int x, int y, Board b) {
		// Loop through all pieces on the board
		for (int i = 0; i < b.onBoard.size(); i++) {
			Piece piece = b.onBoard.get(i);

			// Check if the piece belongs to the opposing side
			if ((WorB == 'W' && !Piece.isWhite(piece.tag)) || (WorB == 'B' && Piece.isWhite(piece.tag))) {
				// Check if the piece can attack the king position
				for (DeltaMovement move : piece.legalMoves()) {
					int newX = move.dx;
					int newY = move.dy;

					// sussssss
					// If the piece can reach the king position, return true
					if (newX == x && newY == y) {
						// System.out.println(newX + " " + newY + " " + piece.tag);
						return true;
					}
				}
			}
		}
		// If no enemy piece can attack the king position, return false
		return false;
	}

	public void promote() {
		if (this.tag == 'p')
			this.tag = 'q';
		else
			this.tag = 'Q';
	}

	public static boolean isWhite(char tag) {
		if (tag == 'k' || tag == 'q' || tag == 'b' || tag == 'n' || tag == 'r' || tag == 'p') {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isValid(int x, int y) {
		if (x < 8 && y < 8 && x >= 0 && y >= 0) {
			return true;
		}
		return false;
	}
}
