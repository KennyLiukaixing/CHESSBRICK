package ChessBrickCopy;

import java.util.Arrays;


public class Rook extends Piece{
	public Rook(int x, int y, char tag, Board b) {
		super(x, y, tag, b);
		moves.addAll(Arrays.asList(
				new DeltaMovement(1,0,true),
				new DeltaMovement(-1,0,true),
				new DeltaMovement(0,1,true),
				new DeltaMovement(0,-1,true)
		));
		captureMoves.addAll(moves);
		xPos = x;
		yPos = y;
	}
}
