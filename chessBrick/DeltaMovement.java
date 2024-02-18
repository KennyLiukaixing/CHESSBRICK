package chessBrick;

public class DeltaMovement {
	public int dx, dy;
	public boolean ext;
	public DeltaMovement(int x, int y) {
		this.dx = x;
		this.dy = y;
		ext = false;
	}
	public DeltaMovement(int x, int y, boolean ext) {
		this.dx = x;
		this.dy = y;
		this.ext = ext;
	}
}
