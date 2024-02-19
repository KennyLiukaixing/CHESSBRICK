package ChessBrickCopy;

import java.util.Arrays;

public class Pawn extends Piece{
	public Pawn(int x, int y, char tag, Board board) {
		super(x,y,tag,board);
		moves.addAll(Arrays.asList(
			new DeltaMovement(0,1)
		));
		captureMoves.addAll(Arrays.asList(
			new DeltaMovement(1,1), 
			new DeltaMovement(-1,1)
		));
		xPos = x;
		yPos = y;
	}
}
