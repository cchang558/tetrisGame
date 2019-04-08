import java.awt.Color;
import java.awt.Graphics;

public class TetrisObject {

	private final static int blockSize = 20;
	private int x, y;
	private int rotate = 0;
	private int shapeHeight = 4;

	private int[][] shapes;
	private Color color;

	public TetrisObject(int x, int y, int[][] shapes, Color color) {
		this.x = x;
		this.y = y;
		this.shapes = shapes;
		this.color = color;
	}

	public void draw(Graphics g) {
		g.setColor(color);
		for(int i = 0; i < shapeHeight; i++) {
			g.fillRect(x + shapes[4 * rotate + i][0] * blockSize, 
					y + shapes[4 * rotate + i][1] * blockSize, blockSize, blockSize);
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void moveDown() {
		y += blockSize;
		if(y > 360 - blockSize - 3) {
			y = 360 - blockSize - 3;
		}
	}

	public static int getBlocksize() {
		return blockSize;
	}

	public void moveRight() {
		x += blockSize;
	}

	public void moveLeft() {
		x -= blockSize;
	}

	public int getRotate() {
		return rotate;
	}

	public void setRotate(int rotate) {
		this.rotate = rotate;
	}

	public void rotate() {
		rotate = (rotate + 1) % 4;
	}
	
	public void drop() {
		y += blockSize;
	}

	public boolean ableToRotate() {
		return true;
	}
}