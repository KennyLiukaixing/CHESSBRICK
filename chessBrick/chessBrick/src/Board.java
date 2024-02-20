import java.util.ArrayList;

public class Board {

	Piece board[][] = new Piece[8][8];
	ArrayList<Piece> onBoard = new ArrayList<>();

	public Board() {

	}
	// WHITE: Positive Better
	// BLACK: Negative Better
	public float evalBoard(){
		float matStat = 0;
		for(Piece p: onBoard){
			char c = p.tag;
			if(c=='r'||c=='n'||c=='b'||c=='q'||c=='k'||c=='p'){
				matStat -= p.mat;
				for(DeltaMovement d:p.legalMoves()){
					if(d.ext) {
						matStat -= 0.1*board[d.dx][d.dy].mat;
					}
					else {
						//System.out.println(p);
						matStat -= 0.1;
					}
				}
			}
			else{
				matStat += p.mat;
				for(DeltaMovement d:p.legalMoves()){
					if(d.ext) {
						matStat += 0.1*board[d.dx][d.dy].mat;
					}
					else {
						//System.out.println(p + " " + d.dx + " "+ d.dy);
						matStat += 0.1;
					}
				}
			}
		}
		System.out.println(matStat);
		return matStat;
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
		
		for(int i = 0;i<8;i++) {
			for(int j = 0;j<8;j++) {
				if(board[i][j]!=null) onBoard.add(board[i][j]);
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

}
