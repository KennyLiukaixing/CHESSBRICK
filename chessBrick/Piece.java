package ChessBrickCopy;

import java.util.ArrayList;


public abstract class Piece {
	public char tag;
	public Board board;
	public ArrayList<DeltaMovement> moves = 
			new ArrayList<DeltaMovement>();
	public ArrayList<DeltaMovement> captureMoves = 
			new ArrayList<DeltaMovement>();
	int xPos, yPos;
	boolean canPromote;
	
	
	public Piece(int x, int y, char tag, Board board) {
		this.xPos = x;
		this.yPos = y;
		this.tag = tag;
		this.board = board;
	}
	
	public boolean makeMove(int Tgtx, int Tgty) {
		boolean success = false;
		for(DeltaMovement d:this.legalMoves()) {
			if(d.dx==Tgtx&&d.dy==Tgty) {
				success = true;
				board.replace(Tgtx, Tgty, this, this.xPos, this.yPos);
			}
		}
		return success;
	}
	
	//is this move legal if nothing else is on the board
	//esoteric use of deltaMovement class, returns coordinates
	//uses ext to denote removal ////IMPORTANT////
	public ArrayList<DeltaMovement> legalMoves() {
		ArrayList<DeltaMovement> legals = new ArrayList<>();
		if(this.tag == 'p'||this.tag == 'k'||this.tag == 'h'
				||this.tag == 'P'||this.tag == 'K'||this.tag == 'H') {
			for(DeltaMovement d: moves) {
				if(board.isEmpty(xPos+d.dx,yPos+d.dy)) legals.add(d);
				else {
					if(!board.sameSide(this,
							board.getPiece(xPos+d.dx,yPos+d.dy))){
						board.remove(board.getPiece(xPos+d.dx,yPos+d.dy));
						DeltaMovement nd = new DeltaMovement(d.dx,d.dy,true);
						legals.add(d);
					}
				}
			}
			return legals;
		}
		else {
			for(int i = 0; i<8;i++) {
				for(DeltaMovement d:moves) {
					if(!board.isEmpty(xPos+d.dx*i,yPos+d.dy*i)) {
						if(board.sameSide(this,
								board.getPiece(xPos+d.dx*i,yPos+d.dy*i))) {
							break;
						}
						else {
							legals.add(d);
							break;
						}
					}
					else legals.add(d);
				}
			}
			return legals;
		}
	}
}
