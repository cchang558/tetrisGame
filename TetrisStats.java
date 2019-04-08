public class TetrisStats {
	
	public int score;
	public boolean isGameOver;
	public int line;
	
	public TetrisStats(int score, int line, boolean isGameOver) {
		this.score = score;
		this.line = line;
		this.setGameOver(isGameOver);
	}

	public boolean isGameOver() {
		return isGameOver;
	}

	public void setGameOver(boolean isGameOver) {
		this.isGameOver = isGameOver;
	}

}
