import java.util.Arrays;


public class Queen extends Piece{
	public Queen(int x, int y, char tag, Board board) {
		super(x,y,tag,board);
		moves.addAll(Arrays.asList(
			new DeltaMovement(1,0,true),
			new DeltaMovement(-1,0,true),
			new DeltaMovement(0,1,true),
			new DeltaMovement(0,-1,true),
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
