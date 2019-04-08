import java.awt.Color;
import java.awt.Graphics;

public class TetrisBoard {

	private int numRows;
	private int numCols;
	private Color[][] boardCell;
	private boolean canRemove;

	public TetrisBoard(int numRows, int numCols) {
		this.numRows = numRows;
		this.numCols = numCols;
		boardCell = new Color[numRows][numCols];
	}

	public void newBoard() {
		for(int j = 0; j < numCols; j++) {
			for(int i = 0; i < numRows; i++) {
				boardCell[i][j] = Color.BLACK;	
			}
		}
	}
	
	public void clearBoard() {
		for(int j = 0; j < numCols; j++) {
			for(int i = 0; i < numRows; i++) {
				boardCell[i][j] = null;
			}
		}
	}

	public void setColor(int x, int y, Color color) {
		boardCell[x][y] = color;
	}

	public Color getColor(int x, int y) {
		return boardCell[x][y];
	}

	public void drawBoard(Graphics g) {
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numCols; j++) {

				g.setColor(boardCell[i][j]);
				g.fillRect(i * TetrisObject.getBlocksize(), j * TetrisObject.getBlocksize(),
						TetrisObject.getBlocksize(), TetrisObject.getBlocksize());
			}
		}
	}

	public void setBoardCell(Color[][] boardCell) {
		this.boardCell = boardCell;
	}


	public boolean isRowFull(int x) {
		int counter = 0;
		for(int i = 0; i < numRows; i++) {
			if (boardCell[i][x] != Color.BLACK) {
				counter++;
			}
		}
		if(counter == numRows) {
			return true;
		} else {
			return false;
		}
	}

	public void removeRow(int x) {
		for(int j = x; j > 3; j--) {
			for(int i = 0; i < numRows; i++) {
				boardCell[i][j] = boardCell[i][j - 1];	
			}
		}
		// Color top row black.
		for(int i = 0; i < numRows; i++) {
			boardCell[i][3] = Color.BLACK;	
		}
	}

	public void boardRemoveRow() {
		canRemove = false;
		for(int i = numCols - 1; i > 3; i--) {
			if (isRowFull(i)) {
				removeRow(i);
				canRemove = true;
				i++;
			}
		}
	}

	public boolean isCanRemove() {
		return canRemove;
	}

	public void setCanRemove(boolean canRemove) {
		this.canRemove = canRemove;
	}
}