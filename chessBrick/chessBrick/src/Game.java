import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Game {

	public static boolean isPlayerTurn = true;

	public static void printTestBoard(Board board) {
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

	public static void printGood(Board board) {
		System.out.println();
		System.out.println("   -----------------");
		for (int i = 8; i > 0; i--) {

			System.out.print(i + " | ");

			for (int j = 0; j < 8; j++) {
				if (board.board[j][8 - i] != null)
					System.out.print(board.board[j][8 - i].tag);
				else
					System.out.print(' ');
				System.out.print(" ");
			}
			System.out.println("|");
		}
		System.out.println("   -----------------");
		System.out.println("    a b c d e f g h");
	}

	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		Board board = new Board();
		board.makeDefault();
	
		while (true) {
			// board.evalBoard();
			printGood(board);
			if (board.fiftyMove >= 100) {
				System.out.println("FIFTY MOVE DRAW");
				break;
			}
			System.out.println(board.eval());
			if (board.gameEnd(isPlayerTurn) != 2) {
				if (board.gameEnd(isPlayerTurn) == 0) {
					System.out.println("DRAW");
					break;
				} else if (board.gameEnd(isPlayerTurn) == 1) {
					System.out.println("BLACK WINS");
					break;
				} else {
					System.out.println("WHITE WINS");
					break;
				}
			}
			if (isPlayerTurn) {
				String s = reader.readLine();
		
				if (notation(s, board))
					isPlayerTurn = false;
			} else {
				DeltaMovement move = MCTS.monteCarlo(board, isPlayerTurn);
				if (move != null && move.p != null) {
					board.board[move.p.xPos][move.p.yPos].forceMove(move.dx, move.dy);
				} else {
					// Handle case where no valid move is found
					// For example, print an error message or handle the situation accordingly
					System.out.println("No valid move found.");
				}
				isPlayerTurn = true;
			}
		}
		
	}
	

	public static boolean notation(String notation, Board b) {

		if (notation.length() < 2) {
			return false;
		}

		notation = notation.replaceAll(" ", "");
		Map<Character, Integer> filesToNum = new HashMap<>();

		filesToNum.put('a', 0);
		filesToNum.put('b', 1);
		filesToNum.put('c', 2);
		filesToNum.put('d', 3);
		filesToNum.put('e', 4);
		filesToNum.put('f', 5);
		filesToNum.put('g', 6);
		filesToNum.put('h', 7);

		if (!Character.isLetterOrDigit(notation.charAt(notation.length() - 1))) {
			notation = notation.substring(0, notation.length() - 1);
		}

		if (Character.isLowerCase(notation.charAt(0))) {
			int xcoord, ycoord;
			if (notation.charAt(notation.length() - 2) == '=') {
				xcoord = filesToNum.get(notation.charAt(notation.length() - 4));
				ycoord = 7 - Character.getNumericValue(notation.charAt(notation.length() - 3)) + 1;
			} else {
				xcoord = filesToNum.get(notation.charAt(notation.length() - 2));
				ycoord = 7 - Character.getNumericValue(notation.charAt(notation.length() - 1)) + 1;
			}

			if (notation.charAt(notation.length() - 2) == '=') {

				for (int i = -1; i <= 1; i++) {
					if ((xcoord + i < 8 && xcoord + i >= 0) && unNull(b.board[xcoord + i][1])
							&& b.board[xcoord + i][1].makeMovePlayer(xcoord, ycoord)) {
						char newPiece = notation.charAt(notation.length() - 1);
						if (newPiece == 'N') {
							b.board[xcoord + i][0] = new Knight(xcoord + i, 0, 'n', b);
						} else if (newPiece == 'Q') {
							b.board[xcoord + i][0] = new Queen(xcoord + i, 0, 'q', b);
						} else if (newPiece == 'R') {
							b.board[xcoord + i][0] = new Rook(xcoord + i, 0, 'r', b);
						} else {
							b.board[xcoord + i][0] = new Bishop(xcoord + i, 0, 'b', b);
						}
						return true;
					}
				}
			} else if (notation.charAt(1) == 'x') {
				if ((unNull(b.board[filesToNum.get(notation.charAt(0))][ycoord + 1]) && xcoord > 0
						&& b.board[filesToNum.get(notation.charAt(0))][ycoord + 1].makeMovePlayer(xcoord, ycoord))) {
					return true;
				}
			} else {
				for (int i = -2; i < 0; i++) {
					if (unNull(b.board[xcoord][ycoord - i])
							&& b.board[xcoord][ycoord - i].makeMovePlayer(xcoord, ycoord)) {
						return true;
					}
				}
			}
		} else if (notation.equals("O-O")) {
			b.board[4][7].makeMovePlayer(6, 7);
			return true;
		} else if (notation.equals("O-O-O")) {
			b.board[4][7].makeMovePlayer(2, 7);
			return true;
		} else if (Character.isLetter(notation.charAt(0))) {

			int x = filesToNum.get(notation.charAt(notation.length() - 2));
			int y = 7 - Character.getNumericValue(notation.charAt(notation.length() - 1)) + 1;

			ArrayList<Integer> coordinate = generateMove(Character.toLowerCase(notation.charAt(0)), x, y, b);
			if (coordinate.size() > 2) {
				if (Character.isLetter(notation.charAt(1))) {
					int xc = filesToNum.get(notation.charAt(1));

					for (int k = 0; k < coordinate.size(); k += 2) {
						if (coordinate.get(k) == xc) {
							b.board[coordinate.get(k)][coordinate.get(k + 1)].makeMovePlayer(x, y);
							return true;
						}
					}
				} else {
					int yc = 7 - Character.getNumericValue(notation.charAt(1)) + 1;

					for (int k = 0; k < coordinate.size(); k += 2) {
						if (coordinate.get(k) == yc && unNull(b.board[coordinate.get(k - 1)][coordinate.get(k)])) {
							b.board[coordinate.get(k - 1)][coordinate.get(k)].makeMovePlayer(x, y);
							return true;
						}
					}
				}
			} else {
				if (coordinate.size() > 0) {
					if (unNull(b.board[coordinate.get(0)][coordinate.get(1)])
							&& b.board[coordinate.get(0)][coordinate.get(1)].makeMovePlayer(x, y)) {

						return true;
					}

				} else
					return false;
			}
			return false;
		} else {
			return false;
		}

		return false;
	}

	public static boolean isLower(char c) {
		// Check if the character is lowercase by comparing it with its lowercase
		// counterpart
		return (c >= 'a' && c <= 'z');
	}

	public static ArrayList<Integer> generateMove(char tag, int x, int y, Board b) {
		ArrayList<Integer> coordinates = new ArrayList<>();

		for (int i = 0; i < b.onBoard.size(); i++) {

			if (b.onBoard.get(i).tag == tag) {
				ArrayList<DeltaMovement> possible = b.onBoard.get(i).legalMoves();

				for (int k = 0; k < possible.size(); k++) {

					if (possible.get(k).dx == x && possible.get(k).dy == y) {
						coordinates.add(b.onBoard.get(i).xPos);
						coordinates.add(b.onBoard.get(i).yPos);
					}
				}
			}
		}
		return coordinates;
	}

	public static boolean unNull(Piece p) {
		if (p == null) {
			return false;
		}
		return true;
	}

	/*
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
	 * the square behind is empty, and en passant is a possibility in this case.
	 * Maybe call an en passant method.
	 * 
	 * Promotion:
	 * Check for this in the notation. If you see an e8=Q, for example, you need to
	 * change the tag to the index 3, depending upper or lowercase on whose move it
	 * is. You move the pawn to e8 if it is legal, and change it to a different
	 * piece. Change the promotion function to take input the type of piece
	 * promoting to.
	 * 
	 * Notation:
	 * Possibilities: Ne8, e4, Nxe4, Nbxd4, N5xd4, e8=N, exd4, exd4 (en passant),
	 * Qh5+, Qh5#, Qxh5#, e8=N+, O-O, O-O-O.
	 * 
	 * - First, eliminate all spaces in the input string.
	 * - convert file names to numbers.
	 * - search each of your pieces legal moves to find the one that can work. Use
	 * the file name in, for example, Nbxd4,
	 * to search only that file.
	 * This means check if it is a legal move (depends on the legal move function
	 * working properly and including checks, etc.)
	 * If not, reprompt the user.
	 * - Handle en passant if it is a legal move (these things handled in the legal
	 * move function), handle promotion (handled
	 * in the promotion function).
	 * 
	 * 
	 * King moves:
	 * Special, because you include
	 * -two kings cannot touch (IMPORTANT)
	 * -castling
	 * -check
	 * 
	 * 
	 * Check:
	 * - Just restrict the legal moves to those that don't leave the king under
	 * attack. We will have to rotate through the enemy pieces'
	 * legal moves to check if one of them is the king square. Eliminate moves that
	 * leave the king in check or move the king into a new
	 * check by checking legal moves each time. Figure out how to do this more
	 * efficiently than n^2.
	 * 
	 * Checkmate:
	 * - In the check function, if we notice that we don't have any legal moves such
	 * that the king is not in check, we're done for.
	 * 
	 * Stalemate:
	 * - If our legal moves are empty, but our king is not in check.
	 * 
	 * 50-move rule:
	 * Keep a counter of how many moves have passed without a capture or pawn move.
	 * If this reaches 100 (a move consists of your move
	 * and your opponent's move, not just a single 'move'), set a draw. Reset the
	 * counter every time a capture or pawn moves.
	 * 
	 * 3-move repetition:
	 * Important, does not need to be consecutive. That makes it difficult. Just
	 * save all the boards and compare them against each other.
	 * This is too much run time though. Maybe have the AI ignore special draws and
	 * instead check if it is going to play the same move three times,
	 * and if the eval says the engine is better, don't play that move.
	 * 
	 * Checkmate impossible:
	 * Two kings are on the board. King vs king and knight, king vs. king and
	 * bishop. If these cases show up, call a draw.
	 * 
	 */
}
