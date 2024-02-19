import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Game {
	public static void printBoard(Board board) {
		System.out.println();
		System.out.println("  0 1 2 3 4 5 6 7");
		for (int i = 0; i < 8; i++) {
			System.out.print(i + " ");
			for (int j = 0; j < 8; j++) {
				if (board.board[j][i] != null)
					System.out.print(board.board[j][i].tag);
				else
					System.out.print(' ');
				System.out.print(" ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(System.in));
		Board b = new Board();
		b.makeDefault();
		boolean isPlayerTurn = true;
		while (true) {
			printBoard(b);
			if (isPlayerTurn) {
				System.out.println("Please type inputs as curX,curY;tgtX,tgtY, eg. 1,2;3,4");
				String s = reader.readLine();
				int curX, curY, tgtX, tgtY;
				curX = s.charAt(0) - 48;
				curY = s.charAt(2) - 48;
				tgtX = s.charAt(4) - 48;
				tgtY = s.charAt(6) - 48;
				if (b.board[curX][curY].makeMovePlayer(tgtX, tgtY)) {
					isPlayerTurn = false;
				}
			} else {
				for (Piece p : b.onBoard) {
					for (DeltaMovement d : p.legalMoves()) {
						if (Math.random() > 0.8) {
							// p.makeMove(d.dx, d.dy);
						}
					}
				}
				isPlayerTurn = true;
			}
		}
	}

	public static int[][] notation(String notation, Board b) {
		// Step 1: convert human notation into coordinates

		// Step 2: handle promotion

		// Step 3: check if legal move

	}

	public int quickEval(Board b) {
		// player
	}

	/*
	 * TODO:
	 * 
	 * 
	 * Castling: add to legal moves a boolean whether the king has moved, after that
	 * castling is impossible. Also a boolean whether the rook has moved
	 * and depending on these three (two rooks, two booleans), check whether we can
	 * castle kingside or queenside. Make sure nothing is blocking, and
	 * we are not moving through check (legal moves of opponent). Or if the king
	 * ends up in check.
	 * 
	 * En passant: Only possible for one move. therefore, we should add en passant
	 * by checking, right after a pawn moves two squares, whether there
	 * are any enemy pawns to the two squares left and right. Then, we add en
	 * passant as a move to the enemy legal moves because it is guaranteed that
	 * the square behind is empty, and en passant is a possibility in this case. Maybe call an en passant method.
	 * 
	 * Promotion:
	 * Check for this in the notation. If you see an e8=Q, for example, you need to
	 * change the tag to the index 3, depending upper or lowercase on whose move it
	 * is. You move the pawn to e8 if it is legal, and change it to a different
	 * piece. Change the promotion function to take input the type of piece promoting to.
	 * 
	 * Notation:
	 * Possibilities: Ne8, e4, Nxe4, Nbxd4, N5xd4, e8=N, exd4, exd4 (en passant),
	 * Qh5+, Qh5#, Qxh5#, e8=N+, O-O, O-O-O.
	 * 
	 * - First, eliminate all spaces in the input string.
	 * - convert file names to numbers.
	 * - search each of your pieces legal moves to find the one that can work. Use the file name in, for example, Nbxd4,
	 * to search only that file.
	 * This means check if it is a legal move (depends on the legal move function working properly and including checks, etc.)
	 * If not, reprompt the user.
	 * - Handle en passant if it is a legal move (these things handled in the legal move function), handle promotion (handled 
	 * in the promotion function).
	 * 
	 * 
	 * King moves:
	 * Special, because you include
	 * -two kings cannot touch (IMPORTANT)
	 * -castling
	 * -check
	 * 
	 * Check:
	 * - Just restrict the legal moves to those that don't leave the king under attack. We will have to rotate through the enemy pieces'
	 * legal moves to check if one of them is the king square. Eliminate moves that leave the king in check or move the king into a new
	 * check by checking legal moves each time. Figure out how to do this more efficiently than n^2.
	 * 
	 * Checkmate:
	 * - In the check function, if we notice that we don't have any legal moves such that the king is not in check, we're done for.
	 * 
	 * Stalemate:
	 * - If our legal moves are empty, but our king is not in check.
	 * 
	 * 50-move rule:
	 * Keep a counter of how many moves have passed without a capture or pawn move. If this reaches 100 (a move consists of your move
	 * and your opponent's move, not just a single 'move'), set a draw. Reset the counter every time a capture or pawn moves.
	 * 
	 * 3-move repetition:
	 * Important, does not need to be consecutive. That makes it difficult. Just save all the boards and compare them against each other.
	 * This is too much run time though. Maybe have the AI ignore special draws and instead check if it is going to play the same move three times,
	 * and if the eval says the engine is better, don't play that move. 
	 * 
	 * Checkmate impossible:
	 * Two kings are on the board. King vs king and knight, king vs. king and bishop. If these cases show up, call a draw.
	 * 
	 */
}
