package chessBrick;

import java.util.Arrays;


public class Knight extends Piece{
	public Knight(int x, int y, char tag, Board board) {
		super(x,y,tag,board);
		moves.addAll(Arrays.asList(
			new DeltaMovement(2,1),
			new DeltaMovement(-2,1),
			new DeltaMovement(1,2),
			new DeltaMovement(-1,2),
			new DeltaMovement(-2,-1),
			new DeltaMovement(2,-1),
			new DeltaMovement(-1,-2),
			new DeltaMovement(1,-2)
		));   
		captureMoves.addAll(moves);
		xPos = x;
		yPos = y;
	}
}
