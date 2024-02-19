package chessBrick;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Game {
	public static void printBoard(Board board) {
		System.out.println();
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
	public static void main(String[] args) throws IOException{
		BufferedReader reader = new BufferedReader(
	            new InputStreamReader(System.in));
		Board b = new Board();
		b.makeDefault();
		boolean isPlayerTurn = true;
		while(true) {
			printBoard(b);
			if(isPlayerTurn) {
				System.out.println("Please type inputs as curX,curY,tgtX,tgtY, eg. 1,2,3,4");
				String s = reader.readLine();
				int curX, curY, tgtX, tgtY;
				curX = s.charAt(0)-48;
				curY = s.charAt(2)-48;
				tgtX = s.charAt(4)-48;
				tgtY = s.charAt(6)-48;
				if(b.board[curX][curY].makeMove(tgtX, tgtY)) {
					isPlayerTurn = false;
				}
			}
			else {
				String s = reader.readLine();
				isPlayerTurn = true;
			}
		}
	}

}
