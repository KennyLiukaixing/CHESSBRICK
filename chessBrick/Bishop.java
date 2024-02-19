package chessBrick;

import java.util.Arrays;

public class Bishop extends Piece{
	public Bishop(int x, int y, char tag, Board board) {
		super(x, y, tag, board);
		moves.addAll(Arrays.asList(
			new DeltaMovement(1,1,true),
			new DeltaMovement(-1,1,true),
			new DeltaMovement(-1,-1,true),
			new DeltaMovement(1,-1,true)
			
		));
		captureMoves.addAll(moves);
		xPos = x;
		yPos = y;
	}
}
