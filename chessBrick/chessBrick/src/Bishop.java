import java.util.Arrays;

public class Bishop extends Piece{
	public Bishop(int x, int y, char tag, Board board) {
		super(x, y, tag, board);
		mat = 3;
		moves.addAll(Arrays.asList(
			new DeltaMovement(1,1,true,false),
			new DeltaMovement(-1,1,true,false),
			new DeltaMovement(-1,-1,true,false),
			new DeltaMovement(1,-1,true,false)
		));
		captureMoves.addAll(moves);
		xPos = x;
		yPos = y;
	}
}
