package ChessBrickCopy;
import java.util.ArrayList;

public class Board {

	Piece board[][] = new Piece[8][8];
	ArrayList<Piece> onBoard = new ArrayList<>();
	
	public Board() {
		
	}
	//WHITE: r
	//BLACK: R
	public void makeDefault() {
		board[0][7] = new Rook(0,7,'r',this);
		board[7][7] = new Rook(7,7,'r',this);
		board[0][0] = new Rook(0,0,'R',this);
		board[7][0] = new Rook(7,0,'R',this);
		board[1][7] = new Rook(1,7,'h',this);
		board[6][7] = new Rook(6,7,'h',this);
		board[1][0] = new Rook(1,0,'H',this);
		board[6][0] = new Rook(6,0,'H',this);
		//TODO: fill out
	}
	
	public boolean isEmpty(int x, int y) {
		return board[x][y] == null;
	}

	public Piece getPiece(int x, int y) {
		return board[x][y];
	}

	public boolean sameSide(Piece piece, Piece piece2) {
		if(piece.tag == 'k'||piece.tag == 'q'||piece.tag == 'h'||
		   piece.tag == 'p'||piece.tag == 'b'||piece.tag == 'r') {
			if(piece2.tag == 'K'||piece2.tag == 'Q'||piece2.tag == 'H'||
			   piece2.tag == 'P'||piece2.tag == 'B'||piece2.tag == 'R') {
				return false;
			}
		}
		if(piece2.tag == 'k'||piece2.tag == 'q'||piece2.tag == 'h'||
		   piece2.tag == 'p'||piece2.tag == 'b'||piece2.tag == 'r') {
			if(piece.tag == 'K'||piece.tag == 'Q'||piece.tag == 'H'||
		       piece.tag == 'P'||piece.tag == 'B'||piece.tag == 'R') {
				return false;
			}
		}
		return true;
	}

	public void remove(Piece piece) {
		onBoard.remove(piece);
	}

	public void replace(int tgtx, int tgty, Piece p, int oldx, int oldy) {
		remove(this.getPiece(tgtx, tgty));
		board[tgtx][tgty] = p;
		board[oldx][oldy] = null;
	}

}
