import java.util.ArrayList;

public abstract class Piece {
	public int mat;
	public boolean moved = false;
	public char tag;
	public Board board;
	public ArrayList<DeltaMovement> moves = new ArrayList<DeltaMovement>();
	public ArrayList<DeltaMovement> captureMoves = new ArrayList<DeltaMovement>();
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

	public boolean makeMovePlayer(int Tgtx, int Tgty) {
		boolean success = false;
		if (this.isPlayerSide) {
			for (DeltaMovement d : this.legalMoves()) {
				if (d.dx == Tgtx && d.dy == Tgty) {
					success = true;
					board.replace(Tgtx, Tgty, this, this.xPos, this.yPos);
					xPos = d.dx;
					yPos = d.dy;
					moved = true;
				}
			}
		}
		return success;
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
							board.remove(board.getPiece(xPos + d.dx, yPos + d.dy));
							legals.add(new DeltaMovement(xPos + d.dx, yPos + d.dy, true));
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
					promote();
				} else {
					if (board.isEmpty(xPos + d.dx, yPos + d.dy))
						legals.add(
								new DeltaMovement(xPos + d.dx, yPos + d.dy));
					if (moved == false) {
						legals.add(
								new DeltaMovement(xPos + d.dx * 2, yPos + d.dy * 2));
					}
				}
			}
			for (DeltaMovement d : captureMoves) {
				if (!(xPos + d.dx < 0 || xPos + d.dx > 7 || yPos + d.dy < 0 || yPos + d.dy > 7)) {
					if (!board.isEmpty(xPos + d.dx, yPos + d.dy)&&
							(!board.sameSide(this, board.board[xPos+d.dx][yPos+d.dy]))) {
						legals.add(
								new DeltaMovement(xPos + d.dx, yPos + d.dy));
						if (yPos + d.dy == 7 || yPos + d.dy == 0) {}
							promote();
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
							// System.out.println((xPos+d.dx*i)+" "+(yPos+d.dy*i));
							break;
						} else {
							legals.add(new DeltaMovement(xPos + d.dx * i, yPos + d.dy * i, true));
							break;
						}
					}

					else {
						legals.add(new DeltaMovement(xPos + d.dx * i, yPos + d.dy * i));
						// System.out.println((xPos+d.dx*i)+" "+(yPos+d.dy*i));
					}
				}
			}
			return legals;
		}
	}

	public void promote() {
		if (this.tag == 'p')
			this.tag = 'q';
		else
			this.tag = 'Q';
	}
}
