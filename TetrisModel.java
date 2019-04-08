import java.awt.Color;
import java.util.ArrayList;

public class TetrisModel {

	private TetrisObject currentTetris;
	private ArrayList<StatsObserver> statsObservers;
	private TetrisView view;
	private Color tetrisColor;
	private TetrisBoard board;
	private boolean needNewTetris;
	private int numRows = 18;
	private int numCols = 12;
	private int colorIndex, shapeIndex;
	private int tetrisX, tetrisY;
	private int blockSize;
	private int rowX, columnY;
	private int tetrisWidth;
	private int rotate;
	private boolean gameOver;
	private int score = 0;
	private int line = 0;
	
	private static int[][][] shapes = {
			// Case 0: Shape for left L.
			{ {0, 0}, {0, 1}, {0, 2}, {1, 2},
		      {0, 2}, {1, 2}, {2, 1}, {2, 2},
			  {0, 0}, {1, 0}, {1, 1}, {1, 2},
			  {0, 1}, {0, 2}, {1, 1}, {2, 1} },
			// Case 1: Shape for right L.
			{ {0, 2}, {1, 0}, {1, 1}, {1, 2},
			  {0, 1}, {1, 1}, {2, 1}, {2, 2},
			  {0, 0}, {0, 1}, {0, 2}, {1, 0},
			  {0, 1}, {0, 2}, {1, 2}, {2, 2} },
			// Case 2: Shape for square.
			{ {0, 0}, {0, 1}, {1, 0}, {1, 1}, 
			  {0, 0}, {0, 1}, {1, 0}, {1, 1},
			  {0, 0}, {0, 1}, {1, 0}, {1, 1},
			  {0, 0}, {0, 1}, {1, 0}, {1, 1} },
			// Case 3: Shape of left Z.
			{ {0, 1}, {1, 1}, {1, 2}, {2, 2},
			  {0, 1}, {0, 2}, {1, 0}, {1, 1},
			  {0, 1}, {1, 1}, {1, 2}, {2, 2},
			  {0, 1}, {0, 2}, {1, 0}, {1, 1} }, 
			// Case 4: Shape of right Z.
			{ {0, 2}, {1, 1}, {1, 2}, {2, 1},
			  {0, 0}, {0, 1}, {1, 1}, {1, 2},
			  {0, 2}, {1, 1}, {1, 2}, {2, 1},
			  {0, 0}, {0, 1}, {1, 1}, {1, 2} }, 
			// Case 5: Shape of T.
			{ {0, 2}, {1, 2}, {1, 1}, {2, 2},
			  {0, 1}, {1, 0}, {1, 1}, {1, 2},
			  {0, 1}, {1, 1}, {2, 1}, {1, 2},
			  {0, 0}, {0, 1}, {0, 2}, {1, 1} },
			// Case 6: Shape of I.
			{ {0, 0}, {0, 1}, {0, 2}, {0, 3},
			  {0, 3}, {1, 3}, {2, 3}, {3, 3},
			  {0, 0}, {0, 1}, {0, 2}, {0, 3},
			  {0, 3}, {1, 3}, {2, 3}, {3, 3} }
	};

	private static int[][] width = {
			/* 
			 * Case 0: Shape for left L.  First number is for no rotation.
			 * Second number is for 1 rotation. Third number is for 2 rotations.
			 * Fourth number is for 3 rotations 
			 */
			{1, 2, 1, 2}, 
			{1, 2, 1, 2}, // Case 1: Shape for right L.
			{1, 1, 1, 1}, // Case 2: Shape for square.
			{2, 1, 2, 1}, // Case 3: Shape of left Z.
			{2, 1, 2, 1}, // Case 4: Shape of right Z.
			{2, 1, 2, 1}, // Case 5: Shape of T.
			{0, 3, 0, 3} // Case 6: Shape of I.
	};
	
	private static int[][] height = {
			/* Case 0: Shape for left L. First number is for no rotation.
			 * Second number is for 1 rotation. Third number is for 2 rotations.
			 * Fourth number is for 3 rotations 
			 */
			{2, 2, 2, 2}, 
			{2, 2, 2, 2}, // Case 1: Shape for right L.
			{1, 1, 1, 1}, // Case 2: Shape for square.
			{2, 2, 2, 2}, // Case 3: Shape of left Z.
			{2, 2, 2, 2}, // Case 4: Shape of right Z.
			{2, 2, 2, 2}, // Case 5: Shape of T.
			{3, 3, 3, 3} // Case 6: Shape of I.
	};
	
	private static Color[] colors = {
			Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
			Color.BLUE, Color.PINK, Color.MAGENTA, Color.LIGHT_GRAY, Color.CYAN
	};

	public TetrisModel() {
		view = new TetrisView(this);
		board = new TetrisBoard(numCols, numRows);
		statsObservers = new ArrayList<StatsObserver>();
		needNewTetris = true;
		gameOver = false;
		startGame();
	}

	public void startGame() {
		board.newBoard();
		if(needNewTetris) {
			createTetrisObject();
		}
		
		// http://tutorials.jenkov.com/java-concurrency/creating-and-starting-threads.html
		new Thread() {
			public void run() {
				while(true) {
					if(canMoveDown()) {
						currentTetris.drop();
					} else if(!canMoveDown() && (tetrisY == 0)){
						storeTetris(currentTetris);
						gameOver = true;
						notifyStatsObservers();
					} else {	
						storeTetris(currentTetris);
						score += 10;
						notifyStatsObservers();
						board.boardRemoveRow();
						if(board.isCanRemove()) {
							score += 30;
							line++;
							notifyStatsObservers();
						}
						createTetrisObject();
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) { }
				}
			}
		}.start();
	}

	public void createTetrisObject() {
		colorIndex = (int)(Math.random() * colors.length);
		shapeIndex = (int)(Math.random() * shapes.length);
		currentTetris = new TetrisObject((view.getWidth() - 80) / 2, 0, shapes[shapeIndex], colors[colorIndex]);
		needNewTetris = false;
	}

	public TetrisObject getCurrentTetris() {
		return currentTetris;
	}
	
	public void notifyStatsObservers() {
		for(StatsObserver observer : statsObservers) {
			observer.statsChanged();
		}
	}
	
	public void registerStatsObserver(StatsObserver observer) {
		statsObservers.add(observer);
	}
	
	public TetrisStats getStats() {
		return new TetrisStats(score, line, gameOver);
	}

	public void storeTetris(TetrisObject currentTetris) {
		tetrisColor = currentTetris.getColor();
		tetrisX = currentTetris.getX();
		tetrisY = currentTetris.getY();
		blockSize = TetrisObject.getBlocksize();
		columnY = tetrisX / blockSize;
		rowX = tetrisY / blockSize;
		rotate = currentTetris.getRotate();

		// Store Tetris color.
		for(int i = 0; i < 4; i++) {
			board.setColor(columnY + shapes[shapeIndex][rotate * 4 + i][0], 
			rowX + shapes[shapeIndex][rotate * 4 + i][1], tetrisColor);		
		}
	}

	public boolean canMoveLeft() {
		boolean possible = true;
		tetrisX = currentTetris.getX();
		blockSize = TetrisObject.getBlocksize();
		tetrisWidth = width[shapeIndex][currentTetris.getRotate()];
		int row = tetrisY / blockSize + 1;
		int column = tetrisX / blockSize;

		// Check left.
		if(tetrisX <= 0) {
			possible = false;
			return possible;
		} else if(tetrisX + tetrisWidth * blockSize <= view.getWidth() - 80 - blockSize) {
			possible = true;
			for(int i = 0; i < 4; i++) {
				if(board.getColor(column + shapes[shapeIndex][rotate * 4 + i][0] - 1, 
					row + shapes[shapeIndex][rotate * 4 + i][1]) != Color.BLACK) {
					possible = false;
					return possible;
				}		
			}
		} 
		return possible;
	}
	
	public boolean canMoveRight() {
		boolean possible = true;
		tetrisX = currentTetris.getX();
		blockSize = TetrisObject.getBlocksize();
		tetrisWidth = width[shapeIndex][currentTetris.getRotate()];
		int row = tetrisY / blockSize + 1;
		int column = tetrisX / blockSize;
		rotate = currentTetris.getRotate();
		
		// Check right.
		if(tetrisX + tetrisWidth * blockSize >= view.getWidth() - 80 - blockSize) {
			possible = false;
			return false;
		} else if(tetrisX >= 0) {
			for(int i = 0; i < 4; i++) {
				if(board.getColor(column+shapes[shapeIndex][rotate * 4 + i][0] + 1, 
					row + shapes[shapeIndex][rotate * 4 + i][1]) != Color.BLACK) {
					possible = false;
					return possible;
				}
			}
		}	
		return possible;
	}

	public boolean canMoveDown() {
		boolean possible = true;
		tetrisX = currentTetris.getX();
		tetrisY = currentTetris.getY();
		int tetrisHeight = height[shapeIndex][currentTetris.getRotate()];
		blockSize = TetrisObject.getBlocksize();
		rotate = currentTetris.getRotate();		
		int i;
		int row = tetrisY / blockSize;
		int column = tetrisX / blockSize;
		
		// Check bottom.
		if((tetrisY + tetrisHeight * blockSize + blockSize / 2) >= (view.getHeight() - blockSize)) {
			possible = false;
			return false;
		} else if(tetrisY >= 0) {
			for(i = 0; i < 4; i++) {
				if(board.getColor(column + shapes[shapeIndex][rotate * 4 + i][0], 
					row + shapes[shapeIndex][rotate * 4 + i][1] + 1) != Color.BLACK) {
					possible = false;
					return possible;
				}
			}
		}
		return possible;
	}	
	
	public boolean canRotate() {
		boolean possible = true;
		tetrisX = currentTetris.getX();
		tetrisY = currentTetris.getY();
		int tetrisHeight = height[shapeIndex][currentTetris.getRotate()];
		int tetrisWidth = width[shapeIndex][currentTetris.getRotate()];
		int rotateTetrisHeight = height[shapeIndex][(currentTetris.getRotate() + 1) % 4];
		int rotateTetrisWidth = width[shapeIndex][(currentTetris.getRotate() + 1) % 4];
		blockSize = TetrisObject.getBlocksize();
		rotate = currentTetris.getRotate();
		int nextRotate = (rotate + 1) % 4;
		int i;
		int row = tetrisY / blockSize;
		int column = tetrisX / blockSize;
		
		// Check right.
		if(tetrisX + rotateTetrisWidth * blockSize > view.getWidth() - 80 - blockSize) {
			possible = false;
			return false;
		} else if(tetrisX < 0) { // Check left.
			possible = false;
			return possible; 
			// Check bottom.
		} else if(tetrisY + rotateTetrisHeight * blockSize + blockSize / 2 >= view.getHeight() - blockSize) {
			possible = false;
			return false;
		} 
	
		for(i = 0; i < 4; i++) {
			if(board.getColor(column + shapes[shapeIndex][nextRotate * 4 + i][0], 
				row + shapes[shapeIndex][nextRotate * 4 + i][1]) != Color.BLACK) {
				possible = false;
				return possible;
			}
		}
		possible = true;
		return possible;
	}
	
	public int getShapeIndex() {
		return shapeIndex;
	}

	public void setShapeIndex(int shapeIndex) {
		this.shapeIndex = shapeIndex;
	}
	
	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
	
	public TetrisBoard getBoard() {
		return board;
	}

	public void setBoard(TetrisBoard board) {
		this.board = board;
	}
}