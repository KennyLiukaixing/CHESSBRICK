package ChessBrickCopy;

public class Game {
	public static void printBoard(Board board) {
		System.out.println("  0 1 2 3 4 5 6 7");
		for(int i = 0; i<8;i++) {
			System.out.print(i+" ");
			for(int j = 0; j<8; j++) {
				if(board.board[j][i]!=null) System.out.print(board.board[j][i].tag);
				else System.out.print(' ');
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	public static void main(String[] args) {
		Board b = new Board();
		b.makeDefault();
		printBoard(b);
		Piece p = b.board[3][0];
		for(DeltaMovement d:p.legalMoves()) {
			d.toString();
		}
	}

}
