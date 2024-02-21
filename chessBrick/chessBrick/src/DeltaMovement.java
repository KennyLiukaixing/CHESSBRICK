public class DeltaMovement {
	public int dx, dy;
	public boolean ext;
	public boolean castle = false;

	public DeltaMovement(int x, int y) {
		this.dx = x;
		this.dy = y;
		ext = false;
	}

	public DeltaMovement(int x, int y, boolean ext, boolean castle) {
		this.dx = x;
		this.dy = y;
		this.ext = ext;
		this.castle = castle;
	}

	public String toString() {
		System.out.print(this.dx + " " + this.dy + "; ");
		return this.dx + " " + this.dy;
	}
}
