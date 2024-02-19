package ChessBrickCopy;

import java.util.Arrays;
public class King extends Piece{
	public King(int x, int y, char tag, Board board) {
		super(x,y,tag,board);
		moves.addAll(Arrays.asList(
			new DeltaMovement(0,1),
			new DeltaMovement(0,-1),
			new DeltaMovement(1,0),
			new DeltaMovement(1,-1),
			new DeltaMovement(1,1),
			new DeltaMovement(-1,0),
			new DeltaMovement(-1,1),
			new DeltaMovement(-1,-1)
		));
		captureMoves.addAll(moves);
		xPos = x;
		yPos = y;
	}
}
