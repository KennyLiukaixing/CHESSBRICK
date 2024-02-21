import java.util.Arrays;


public class Rook extends Piece{
	public Rook(int x, int y, char tag, Board b) {
		super(x, y, tag, b);
		mat = 5;
		moves.addAll(Arrays.asList(
				new DeltaMovement(1,0,true,false),
				new DeltaMovement(-1,0,true,false),
				new DeltaMovement(0,1,true,false),
				new DeltaMovement(0,-1,true,false)
		));
		captureMoves.addAll(moves);
		xPos = x;
		yPos = y;
	}
}
